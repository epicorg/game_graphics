package graphic.integration;

/**
 * Geometry + Material
 *
 * @author Alessandro Martinelli
 */
public class Model {

    private Material material;
    private Mesh geometry = null;

    public Material getMaterialComponent() {
        return material;
    }

    public void setMaterialComponent(Material material) {
        this.material = material;
    }

    public void setRootGeometry(Mesh geometry) {
        this.geometry = geometry;
    }

    public void draw() {
        material.getProgram().getShader().apply();
        material.loadData();
        geometry.draw(material.getProgram().getShader());
    }

    public ShadingProgram getProgram() {
        return material.getProgram();
    }

}
