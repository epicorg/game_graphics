package objLoader;

import java.util.ArrayList;

import shadow.math.SFVertex4f;

public class ObjGroup {

    private String name;
    private ArrayList<ObjObject> indices = new ArrayList<ObjObject>();

    public ObjGroup(String name) {
        super();
        this.name = name;
    }

    public ArrayList<ObjObject> getObjects() {
        return indices;
    }

    public String getName() {
        return name;
    }

    public ObjObject getAsObject() {
        ObjObject object = new ObjObject(name);

        int vPos = 0;
        int vtPos = 0;
        int vnPos = 0;

        for (int i = 0; i < indices.size(); i++) {
            ObjObject obj = indices.get(i);

            for (SFVertex4f v : obj.getVertices()) {
                object.getVertices().add(v);
            }
            for (SFVertex4f n : obj.getNormals()) {
                object.getNormals().add(n);
            }
            for (SFVertex4f vt : obj.getTxCoord()) {
                object.getTxCoord().add(vt);
            }
            for (ObjIndex[] sets : obj.getIndices()) {
                ObjIndex[] newSets = new ObjIndex[sets.length];
                for (int j = 0; j < newSets.length; j++) {
                    newSets[j] = new ObjIndex(sets[j].getvIndex() + vPos,
                            sets[j].getVtIndex() + vtPos,
                            sets[j].getVnIndex() + vnPos);
                }
                object.getIndices().add(newSets);
            }
            vPos += obj.getVertices().size();
            vnPos += obj.getNormals().size();
            vtPos += obj.getTxCoord().size();
        }

        return object;
    }
}
