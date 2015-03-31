package game.physics;

import shadow.math.SFVertex3f;

public class Vector3D {
	
	private final float x,y,z;
	
	public Vector3D(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Vector3D v){
		x=v.x;
		y=v.y;
		z=v.z;
	}

    public Vector3D(SFVertex3f v){
        x=v.getX();
        y=v.getY();
        z=v.getZ();
    }
	
	public Vector3D dirVector(float dir, float hdir){
		float c=(float)Math.PI/180;
		float mod=(float)Math.sqrt(squareModulus());
		float x0=mod*(float)Math.cos(hdir*c);
		float xx=x0*(float)Math.cos(dir*c),yy=x0*(float)Math.sin(dir*c);
		return new Vector3D(xx, yy, mod*(float)Math.sin(hdir*c));
	}
	
	public float squareModulus(){
		return x*x+y*y+z*z;
	}
	
	public float dotP(Vector3D v){
		return x*v.x+y*v.y+z*v.z;
	}
	
	public Vector3D mul(float k){
		return new Vector3D(x*k,y*k,z*k);
	}
	
	public Vector3D add(Vector3D v){
		return new Vector3D(x+v.x, y+v.y, z+v.z);
	}
	
	public Vector3D sub(Vector3D v){
		return new Vector3D(x-v.x, y-v.y, z-v.z);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}

}
