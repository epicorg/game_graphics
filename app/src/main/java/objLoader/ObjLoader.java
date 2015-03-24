package objLoader;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;
import shadow.math.SFVertex3f;
import shadow.math.SFVertex4f;

/**
 * Created by Alessandro on 16/03/15.
 */
public class ObjLoader {

    public static ArrayObject[] arrayObjectFromFile(Context context,String filename){

        ArrayList<ObjGroup> groups01 = loadGroupSet(context,filename);
        //ArrayList<ObjGroup> groups01 = loadGroupSet("files/Palma_pgons_high.obj");

        ArrayObject[] objects=new ArrayObject[groups01.size()];

        for (int i=0;i<objects.length;i++) {
            objects[i]=generateModel(groups01.get(i).getAsObject());
        }
        //ArrayObject arrayObject= createModelFile("../MakeyourDodoJavaDev/building/models/models_oPalm01.sfb",
        //        "PalmModel01", groups01.get(3).getAsObject());

        return objects;
    }

    public static ArrayList<ObjGroup> loadGroupSet(Context context,String filename) {
        Iterator<String> iterator=new ObjFileIterator(context,filename);

        ArrayList<ObjGroup> groups = new ArrayList<ObjGroup>();

        ObjGroup lastGroup=null;
        ObjObject lastObject=null;

        ObjIndex set = new ObjIndex();

        while(iterator.hasNext()){
            String line=iterator.next().trim();

            String[] splits=line.split(" ");

            if(splits[0].equals("v")){
                lastObject.getVertices().add(readValue(line));
                set.setvIndex(set.getvIndex()+1);
            }else if(splits[0].equals("vt")){
                lastObject.getTxCoord().add(readValue(line));
                set.setVtIndex(set.getVtIndex()+1);
            }else if(splits[0].equals("vn")){
                lastObject.getNormals().add(readValue(line));
                set.setVnIndex(set.getVnIndex()+1);
            }else if(splits[0].equals("f")){

                ObjIndex[] sets=new ObjIndex[splits.length-1];
                for (int i = 1; i < splits.length; i++) {
                    String[] indexSplits=splits[i].split("/");
                    if(indexSplits.length==1){
                        String index=indexSplits[0];
                        indexSplits=new String[3];
                        indexSplits[0]=index;
                        indexSplits[1]=index;
                        indexSplits[2]=index;
                    }
                    for (int j = 0; j < indexSplits.length; j++) {
                        //System.err.println("{"+indexSplits[j]+"}"+j);
                        if(indexSplits[j].length()==0){
                            indexSplits[j]="1";
                        }
                    }
                    sets[i-1]=new ObjIndex(Integer.parseInt(indexSplits[0]),
                            Integer.parseInt(indexSplits[1]),Integer.parseInt(indexSplits[2]));
                    //indices.add(lastObject.getIndices().size()-1);
                }

                lastObject.addIndices(sets);

            }else if(splits[0].equals("g") || splits[0].equals("o")){
                if(lastGroup!=null){
                    if(lastObject!=null){
                        lastGroup.getObjects().add(lastObject);
                        lastObject=null;
                    }
                    groups.add(lastGroup);
                }
                lastGroup=new ObjGroup(line.split(" ")[1]);
                if(lastObject!=null){
                    lastGroup.getObjects().add(lastObject);
                }
                lastObject=new ObjObject(line.split(" ")[1]);
                lastObject.getPosition().set(set);
            } else{
                // System.err.println(line);
            }
        }

        if(lastGroup!=null){
            if(lastObject!=null){
                lastGroup.getObjects().add(lastObject);
                lastObject=null;
            }
            groups.add(lastGroup);
        }
        return groups;
    }

    private static ArrayObject generateModel(ObjObject object) {

        float[] vertices=new float[3*object.getVertices().size()];
        float[] normals=new float[3*object.getNormals().size()];
        float[] txCoords=new float[3*object.getTxCoord().size()];
        //ArrayList<SFVertex3f> vertices=new ArrayList<SFVertex3f>();
        //ArrayList<SFVertex3f> normals=new ArrayList<SFVertex3f>();
        //ArrayList<SFVertex3f> indices=new ArrayList<SFVertex3f>();
        ArrayList<Integer> indices_=new ArrayList<Integer>();

        ArrayList<ObjIndex[]> indices__=object.getIndices();
        for (int i = 0; i < indices__.size(); i++) {
            ObjIndex[] set=indices__.get(i);
            if(set.length==4){
                indices_.add(set[0].getvIndex());
                indices_.add(set[1].getvIndex());
                indices_.add(set[2].getvIndex());
                indices_.add(set[0].getvIndex());
                indices_.add(set[2].getvIndex());
                indices_.add(set[3].getvIndex());
            }else{
                indices_.add(set[0].getvIndex());
                indices_.add(set[1].getvIndex());
                indices_.add(set[2].getvIndex());
            }
        }


        for (int i = 0; i < object.getVertices().size(); i++) {
            SFVertex4f v=object.getVertices().get(i);
            vertices[3*i]=v.getX();
            vertices[3*i+1]=v.getY();
            vertices[3*i+2]=v.getZ();
        }

        for (int i = 0; i < object.getNormals().size(); i++) {
            SFVertex4f n=object.getNormals().get(i);
            n.normalize3f();
            normals[3*i]=n.getX();
            normals[3*i+1]=n.getY();
            normals[3*i+2]=n.getZ();
        }

        for (int i = 0; i < object.getTxCoord().size(); i++) {
            SFVertex4f texCoord=object.getTxCoord().get(i);
            txCoords[3*i]=texCoord.getX();
            txCoords[3*i+1]=texCoord.getY();
            txCoords[3*i+2]=texCoord.getZ();
        }

        short[] indices=new short[indices_.size()];
        for (int i = 0; i < indices_.size(); i++) {
            indices[i]=(short)(indices_.get(i)-1);
        }

        return new ArrayObject(vertices,normals,txCoords,indices);
    }

    private static SFVertex4f readValue(String line) {
        String[] elements = line.split(" ");
        SFVertex4f value = new SFVertex4f(0,0,0,0);
        for (int i = 1; i < elements.length; i++) {
            value.getV()[i-1]=Float.parseFloat(elements[i]);
        }
        return value;
    }

}
