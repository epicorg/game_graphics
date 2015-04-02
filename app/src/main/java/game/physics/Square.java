package game.physics;

import shadow.math.SFVertex3f;

public class Square {

    private SFVertex3f pos;
    private float xSize, ySize, zSize;

    public Square(SFVertex3f pos, float xSize, float ySize, float zSize) {
        this.pos = pos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public SFVertex3f getPos() {
        return pos;
    }

    public float getxSize() {
        return xSize;
    }

    public float getySize() {
        return ySize;
    }

    public float getzSize() {
        return zSize;
    }

    @Override
    public String toString() {
        return "Square. Pos:" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

}
