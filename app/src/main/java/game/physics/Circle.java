package game.physics;

import shadow.math.SFVertex3f;

public class Circle {

    private SFVertex3f pos;
    private float radius;

    public Circle(SFVertex3f pos, float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public SFVertex3f getPos() {
        return pos;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Circle. Pos:" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }
}
