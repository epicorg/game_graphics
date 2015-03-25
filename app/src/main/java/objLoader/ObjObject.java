package objLoader;

import java.util.ArrayList;

import shadow.math.SFVertex3f;
import shadow.math.SFVertex4f;

public class ObjObject {

    private String name;

    private ArrayList<SFVertex4f> vertices = new ArrayList<SFVertex4f>();
    private ArrayList<SFVertex4f> normals = new ArrayList<SFVertex4f>();
    private ArrayList<SFVertex4f> txCoord = new ArrayList<SFVertex4f>();

    private ObjIndex position = new ObjIndex();

    private ArrayList<ObjIndex[]> indices = new ArrayList<ObjIndex[]>();

    public ObjObject(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ObjIndex[]> getIndices() {
        return indices;
    }

    public void addIndices(ObjIndex[] indices) {
        //TODO : check if there is redundancy..
        //AND triangulate
        for (int i = 0; i < indices.length; i++) {
            indices[i].subtract(position);
        }
        this.indices.add(indices);
    }

    public ObjIndex getPosition() {
        return position;
    }

    public ArrayList<SFVertex4f> getVertices() {
        return vertices;
    }

    public ArrayList<SFVertex4f> getNormals() {
        return normals;
    }

    public ArrayList<SFVertex4f> getTxCoord() {
        return txCoord;
    }

    public ObjObject generateReworked() {
        ObjObject object = new ObjObject(name);

        //Positions
        ArrayList<Integer> positions = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> indices = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < vertices.size(); i++) {
            int found = -1;
            for (int j = 0; j < i; j++) {
                SFVertex4f v1 = vertices.get(i);
                SFVertex4f v2 = vertices.get(j);
                SFVertex3f distance = new SFVertex3f();
                distance.set(v2);
                distance.subtract(v1);

                float dot3f = distance.dot3f(distance);
                if (dot3f < 0.0001) {
                    found = j;
                }
            }

            if (found != -1) {
                positions.add(positions.get(found));
                indices.get(positions.get(found)).add(i);
            } else {
                positions.add(indices.size());
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(i);
                indices.add(list);
            }
            //so that : indices.get(positions.get(i)).contains(i)
            if (!(indices.get(positions.get(i)).contains(i))) {
                System.err.println("!!!Error");
            }
        }


        for (int i = 0; i < this.indices.size(); i++) {
            ObjIndex[] set = this.indices.get(i);
            ObjIndex[] newSet = new ObjIndex[set.length];
            for (int j = 0; j < newSet.length; j++) {
                int position = positions.get(set[j].getvIndex() - 1);
                newSet[j] = new ObjIndex(position + 1, position + 1, position + 1);
            }
            object.indices.add(newSet);
        }

        for (int i = 0; i < indices.size(); i++) {
            ArrayList<Integer> list = indices.get(i);
            SFVertex4f v = new SFVertex4f();
            SFVertex4f vn = new SFVertex4f();
            SFVertex4f vt = new SFVertex4f();
            v.set(this.vertices.get(list.get(0)));
            vn.set(this.normals.get(list.get(0)));
            vt.set(this.txCoord.get(list.get(0)));
            for (int j = 1; j < list.size(); j++) {
                v.add(this.vertices.get(list.get(j)));
                vn.add(this.normals.get(list.get(j)));
                vt.add(this.txCoord.get(list.get(j)));
            }
            float rec = 1.0f / list.size();
            v.mult(rec);
            vn.mult(rec);
            vn.normalize4f();
            vt.mult(rec);
            object.getVertices().add(v);
            object.getNormals().add(vn);
            object.getTxCoord().add(vt);
        }

        return object;
    }

    public void rewriteNormals() {

        ArrayList<ArrayList<SFVertex3f>> referenceVertices = new ArrayList<ArrayList<SFVertex3f>>();

        for (int i = 0; i < normals.size(); i++) {
            referenceVertices.add(new ArrayList<SFVertex3f>());
        }

        for (int i = 0; i < indices.size(); i++) {
            ObjIndex[] set = indices.get(i);
            int[] indices = new int[set.length];
            for (int j = 0; j < set.length; j++) {
                indices[j] = set[j].getvIndex() - 1;
            }

            SFVertex3f A = new SFVertex3f(vertices.get(indices[0]));
            SFVertex3f B = new SFVertex3f(vertices.get(indices[1]));
            SFVertex3f C = new SFVertex3f(vertices.get(indices[2]));
            B.subtract3f(A);
            C.subtract3f(A);
            SFVertex3f N = B.cross(C);

            for (int j = 0; j < indices.length; j++) {
                referenceVertices.get(indices[j]).add(N);
            }
        }

        for (int i = 0; i < normals.size(); i++) {
            SFVertex3f n = new SFVertex3f(0, 0, 0);
            for (int j = 0; j < referenceVertices.get(i).size(); j++) {
                n.add3f(referenceVertices.get(i).get(j));
            }
            n.normalize3f();
            normals.get(i).set(n);

            System.err.println("Original Normal " + normals.get(i) + " New One " + n);
        }

    }

    public void checkSimilar() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (i != j) {
                    SFVertex4f v1 = vertices.get(i);
                    SFVertex4f v2 = vertices.get(j);
                    SFVertex3f distance = new SFVertex3f();
                    distance.set(v2);
                    distance.subtract(v1);

                    float dot3f = distance.dot3f(distance);
                    if (dot3f < 0.00001)
                        System.err.println(distance + " " + normals.get(i) + " " + normals.get(j));
                }
            }
        }
    }
}



