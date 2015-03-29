package game.physics;

import shadow.math.SFVertex3f;

public interface CollisionBox {
	
	public boolean checkPoint(SFVertex3f v);
	public CollisionBox setPos(SFVertex3f v);
	public boolean checkCollision(CollisionBox box);

}
