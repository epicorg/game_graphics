package game.physics;

import java.util.ArrayList;
import shadow.math.SFVertex3f;

public class Square implements CollisionBox{

    private SFVertex3f pos;
    private SFVertex3f[] vertices=new SFVertex3f[4];
    private float xSize, ySize, zSize, radius;

    public Square(SFVertex3f pos, float xSize, float ySize, float zSize) {
        this.pos = pos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.radius=(float)Math.sqrt(xSize*xSize+zSize*zSize)/2;
        createVertices();
    }

    private void createVertices(){
        vertices[0]=new SFVertex3f(-xSize/2, 0, zSize/2);
        vertices[1]=new SFVertex3f(xSize/2, 0, zSize/2);
        vertices[2]=new SFVertex3f(xSize/2, 0, -zSize/2);
        vertices[3]=new SFVertex3f(-xSize/2, 0, -zSize/2);
    }

    public float getRadius(){
        return radius;
    }

    @Override
    public ArrayList<SFVertex3f> getAxes(SFVertex3f otherCenter) {
        ArrayList<SFVertex3f> list=new ArrayList<>();
        float x,z;
        SFVertex3f v0;
        for (int i = 0; i < 4; i++) {
            if (i<3){
                v0=new SFVertex3f(vertices[i+1]);
                v0.subtract(vertices[i]);
            }
            else{
                v0=new SFVertex3f(vertices[0]);
                v0.subtract(vertices[i]);
            }
            if (v0.getZ()==0){
                x=0;
                z=1;
            }else{
                x=1;
                z=-v0.getX()/v0.getZ();
            }
            v0=new SFVertex3f(x,0,z);
            v0.normalize3f();
            list.add(v0);
        }
        return list;
    }

    @Override
    public float[] getProjections(SFVertex3f axis) {
        float[] ps=new float[2];
        float min=0, max=0,
                base=(new SFVertex3f(getPos().getX(),0,getPos().getZ())).dot3f(axis), value;
        for (int i = 0; i < 4; i++) {
            value=base+vertices[i].dot3f(axis);
            if (i==0)
                min=max=value;
            else{
                min=Math.min(min, value);
                max=Math.max(max, value);
            }
        }
        ps[0]=min;
        ps[1]=max;
        return ps;
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
