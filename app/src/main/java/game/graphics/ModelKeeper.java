package game.graphics;

import android.content.Context;

import java.util.HashMap;
import java.util.Objects;

import graphic.integration.Material;
import graphic.integration.Mesh;
import graphic.integration.Model;

/**
 * Singleton that manages the {@link Model} loaded from files.
 * It allows to access the {@link Model} without load them several times or
 * automatically load them when needed.
 */
public enum ModelKeeper {

    MODEL_KEEPER;

    private final HashMap<ModelData, Model> map = new HashMap<>();

    /**
     * Builds a <code>Model</code> from file, or returns it immediately in case it was already loaded.
     *
     * @param context  <code>Context</code> to load the files.
     * @param name     Name of the file that contains the model geometry.
     * @param material <code>Material</code> to use to build the Model.
     * @return The required Model; if a <code>Model</code> from the same file name was previously requested and loaded, that one is
     * returned, regardless of the other parameters.
     */
    public Model getModel(Context context, String name, Material material) {
        ModelData modelData = new ModelData(name, material);
        if (map.containsKey(modelData))
            return map.get(modelData);
        else {
            Model model = getModelFromObjectAndMaterial(context, name, material);
            map.put(modelData, model);
            return model;
        }
    }

    private Model getModelFromObjectAndMaterial(Context context, String name, Material material) {
        Mesh meshPos = MeshKeeper.MESH_KEEPER.getMesh(context, name);
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(material);
        return modelPos;
    }

    /**
     * Clears the mapped data.
     */
    public void clear() {
        map.clear();
    }

    private static class ModelData {

        private final String obj;
        private final Material material;

        public ModelData(String obj, Material material) {
            this.obj = obj;
            this.material = material;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelData modelData = (ModelData) o;

            if (!Objects.equals(obj, modelData.obj)) return false;
            return Objects.equals(material, modelData.material);
        }

        @Override
        public int hashCode() {
            int result = obj != null ? obj.hashCode() : 0;
            result = 31 * result + (material != null ? material.hashCode() : 0);
            return result;
        }

    }

}
