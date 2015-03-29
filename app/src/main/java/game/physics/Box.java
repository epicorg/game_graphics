package game.physics;

import shadow.math.SFVertex3f;

public class Box implements CollisionBox{
	
	private float width,length,height;
	private SFVertex3f pos;
	
	public Box(float width, float length, float height) {
		super();
		this.width = width;
		this.length = length;
		this.height = height;
	}
	
	@Override
	public Box setPos(SFVertex3f v){
		this.pos=v;
		return this;
	}
	
	@Override
	public boolean checkPoint(SFVertex3f v) {
		Vector3D w=new Vector3D(v).sub(new Vector3D(pos));
		return ((w.getX()*w.getX()<width*width/4)
				&& (w.getY()*w.getY()<length*length/4)
				&& (w.getZ()*w.getZ()<height*height/4));
	}
	
	public SFVertex3f getPos() {
		return pos;
	}
	
	public float getWidth() {
		return width;
	}

	public float getLength() {
		return length;
	}
	
	public float getHeight() {
		return height;
	}

	@Override
	public boolean checkCollision(CollisionBox box) {
		if (box instanceof Box){
			Box b=(Box) box;
			if (!new CircleBox(this).setPos(pos)
					.checkCollision(new CircleBox(b).setPos(b.pos)))
				return  false;
			float x1=pos.getX(),x2=b.pos.getX(),y1=pos.getY(),y2=b.pos.getY(),z1=pos.getZ(),z2=b.pos.getZ();
			return ((Math.abs(x1-x2)<(width+b.width)/2)
					&& (Math.abs(y1-y2)<(length+b.length)/2)
					&& (Math.abs(z1-z2)<(height+b.height)/2));
		}
		else if (box instanceof CircleBox){
			return ((CircleBox)box).checkCollision(this);
		}
			return false;
	}
	
	@Override
	public String toString() {
		return "["+pos+";"+"("+width+","+length+","+height+")"+"]";
	}

}
