package graphic.objLoader;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

import graphic.integration.ArrayObject;
import graphic.shadow.math.SFVertex4f;

/**
 * @author Alessandro Martinelli
 * @date 16/03/2015
 */
public class ObjLoader {

    public static ArrayObject[] arrayObjectFromFile(Context context, String filename) {

        ArrayList<ObjGroup> groups01 = loadGroupSet(context, filename);

        ArrayObject[] objects = new ArrayObject[groups01.size()];

        for (int i = 0; i < objects.length; i++) {
            objects[i] = generateModel(groups01.get(i).getAsObject());
        }

        return objects;
    }

    public static ArrayList<ObjGroup> loadGroupSet(Context context, String filename) {
        Iterator<String> iterator = new ObjFileIterator(context, filename);

        ArrayList<ObjGroup> groups = new ArrayList<>();

        ObjGroup lastGroup = null;
        ObjObject lastObject = null;

        ObjIndex set = new ObjIndex();

        while (iterator.hasNext()) {
            String line = iterator.next().trim();

            String[] splits = line.split(" ");

            switch (splits[0]) {
                case "v":
                    assert lastObject != null;
                    lastObject.getVertices().add(readValue(line));
                    set.setVIndex(set.getVIndex() + 1);
                    break;
                case "vt":
                    assert lastObject != null;
                    lastObject.getTxCoordinates().add(readValue(line));
                    set.setVtIndex(set.getVtIndex() + 1);
                    break;
                case "vn":
                    assert lastObject != null;
                    lastObject.getNormals().add(readValue(line));
                    set.setVnIndex(set.getVnIndex() + 1);
                    break;
                case "f":
                    ObjIndex[] sets = new ObjIndex[splits.length - 1];
                    for (int i = 1; i < splits.length; i++) {
                        String[] indexSplits = splits[i].split("/");
                        if (indexSplits.length == 1) {
                            String index = indexSplits[0];
                            indexSplits = new String[3];
                            indexSplits[0] = index;
                            indexSplits[1] = index;
                            indexSplits[2] = index;
                        }
                        for (int j = 0; j < indexSplits.length; j++)
                            if (indexSplits[j].length() == 0)
                                indexSplits[j] = "1";

                        sets[i - 1] = new ObjIndex(Integer.parseInt(indexSplits[0]),
                                Integer.parseInt(indexSplits[1]), Integer.parseInt(indexSplits[2]));
                    }

                    assert lastObject != null;
                    lastObject.addIndices(sets);

                    break;
                case "g":
                case "o":
                    if (lastGroup != null) {
                        lastGroup.getObjects().add(lastObject);
                        lastObject = null;
                        groups.add(lastGroup);
                    }
                    lastGroup = new ObjGroup(line.split(" ")[1]);
                    if (lastObject != null) {
                        lastGroup.getObjects().add(lastObject);
                    }
                    lastObject = new ObjObject(line.split(" ")[1]);
                    lastObject.getPosition().set(set);
                    break;
                default:
                    System.err.println(line);
                    break;
            }
        }

        if (lastGroup != null) {
            lastGroup.getObjects().add(lastObject);
            groups.add(lastGroup);
        }
        return groups;
    }

    private static ArrayObject generateModel(ObjObject object) {
        ArrayList<ObjIndex[]> indices__ = object.getIndices();

        float[] verticesFinal = new float[9 * indices__.size()];
        float[] normalsFinal = new float[9 * indices__.size()];
        float[] txCoordsFinal = new float[9 * indices__.size()];

        for (int i = 0; i < indices__.size(); i++) {
            ObjIndex[] set = indices__.get(i);
            verticesFinal[i * 9] = object.getVertices().get(set[0].getVIndex() - 1).getX();
            normalsFinal[i * 9] = object.getNormals().get(set[0].getVnIndex() - 1).getX();
            txCoordsFinal[i * 9] = object.getTxCoordinates().get(set[0].getVtIndex() - 1).getX();
            verticesFinal[i * 9 + 1] = object.getVertices().get(set[0].getVIndex() - 1).getY();
            normalsFinal[i * 9 + 1] = object.getNormals().get(set[0].getVnIndex() - 1).getY();
            txCoordsFinal[i * 9 + 1] = object.getTxCoordinates().get(set[0].getVtIndex() - 1).getY();
            verticesFinal[i * 9 + 2] = object.getVertices().get(set[0].getVIndex() - 1).getZ();
            normalsFinal[i * 9 + 2] = object.getNormals().get(set[0].getVnIndex() - 1).getZ();
            txCoordsFinal[i * 9 + 2] = object.getTxCoordinates().get(set[0].getVtIndex() - 1).getZ();

            verticesFinal[i * 9 + 3] = object.getVertices().get(set[1].getVIndex() - 1).getX();
            normalsFinal[i * 9 + 3] = object.getNormals().get(set[1].getVnIndex() - 1).getX();
            txCoordsFinal[i * 9 + 3] = object.getTxCoordinates().get(set[1].getVtIndex() - 1).getX();
            verticesFinal[i * 9 + 4] = object.getVertices().get(set[1].getVIndex() - 1).getY();
            normalsFinal[i * 9 + 4] = object.getNormals().get(set[1].getVnIndex() - 1).getY();
            txCoordsFinal[i * 9 + 4] = object.getTxCoordinates().get(set[1].getVtIndex() - 1).getY();
            verticesFinal[i * 9 + 5] = object.getVertices().get(set[1].getVIndex() - 1).getZ();
            normalsFinal[i * 9 + 5] = object.getNormals().get(set[1].getVnIndex() - 1).getZ();
            txCoordsFinal[i * 9 + 5] = object.getTxCoordinates().get(set[1].getVtIndex() - 1).getZ();

            verticesFinal[i * 9 + 6] = object.getVertices().get(set[2].getVIndex() - 1).getX();
            normalsFinal[i * 9 + 6] = object.getNormals().get(set[2].getVnIndex() - 1).getX();
            txCoordsFinal[i * 9 + 6] = object.getTxCoordinates().get(set[2].getVtIndex() - 1).getX();
            verticesFinal[i * 9 + 7] = object.getVertices().get(set[2].getVIndex() - 1).getY();
            normalsFinal[i * 9 + 7] = object.getNormals().get(set[2].getVnIndex() - 1).getY();
            txCoordsFinal[i * 9 + 7] = object.getTxCoordinates().get(set[2].getVtIndex() - 1).getY();
            verticesFinal[i * 9 + 8] = object.getVertices().get(set[2].getVIndex() - 1).getZ();
            normalsFinal[i * 9 + 8] = object.getNormals().get(set[2].getVnIndex() - 1).getZ();
            txCoordsFinal[i * 9 + 8] = object.getTxCoordinates().get(set[2].getVtIndex() - 1).getZ();
        }

        short[] indices = new short[3 * indices__.size()];
        for (short i = 0; i < (3 * indices__.size()); i++) {
            indices[i] = i;
        }

        return new ArrayObject(verticesFinal, normalsFinal, txCoordsFinal, indices);
    }

    private static SFVertex4f readValue(String line) {
        String[] elements = line.split(" ");
        SFVertex4f value = new SFVertex4f(0, 0, 0, 0);
        for (int i = 1; i < elements.length; i++) {
            value.getV()[i - 1] = Float.parseFloat(elements[i]);
        }
        return value;
    }

}
