package game.physics;

import shadow.math.SFVertex3f;

public class Wall implements Collidable{
	
	private SFVertex3f v;
	private Box box;
	
	public Wall(SFVertex3f v, Box box) {
		super();
		this.v = v;
		this.box = box;
	}

	public SFVertex3f getPos(){
		return v;
	}

	@Override
	public CollisionBox getBox() {
		box.setPos(v);
		return box;
	}

}
