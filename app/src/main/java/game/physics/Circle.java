package game.physics;

import java.util.ArrayList;
import shadow.math.SFVertex3f;

public class Circle implements CollisionBox{

    private SFVertex3f pos;
    private float radius;

    public Circle(SFVertex3f pos, float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    @Override
    public ArrayList<SFVertex3f> getAxes(SFVertex3f otherCenter) {
        SFVertex3f axis=new SFVertex3f(otherCenter.getX(),0,otherCenter.getZ());
        axis.subtract(new SFVertex3f(getPos().getX(), 0, getPos().getZ()));
        axis.normalize3f();
        ArrayList<SFVertex3f> list=new ArrayList<>();
        list.add(axis);
        return list;
    }

    @Override
    public float[] getProjections(SFVertex3f axis) {
        float[] ps=new float[2];
        float p0=(new SFVertex3f(getPos().getX(),0,getPos().getZ())).dot3f(axis);
        ps[0]=p0-radius;
        ps[1]=p0+radius;
        return ps;
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
