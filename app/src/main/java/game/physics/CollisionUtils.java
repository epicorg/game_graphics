package game.physics;

import java.util.ArrayList;
import shadow.math.SFVertex3f;

public class CollisionUtils {
	
	public static boolean checkCollision(CollisionBox box1, CollisionBox box2){
		SFVertex3f temp=new SFVertex3f(box2.getPos());
		float d=box1.getRadius()+box2.getRadius();
		temp.subtract(box1.getPos());
        return (temp.getSquareModulus()<=d*d && (checkOneBoxAxes(box1, box2) && checkOneBoxAxes(box2, box1)));
	}
	
	private static boolean checkOneBoxAxes(CollisionBox box1, CollisionBox box2){
		ArrayList<SFVertex3f> list=box1.getAxes(box2.getPos());
		for (SFVertex3f v : list) {
			if (!checkIntervals(box1.getProjections(v), box2.getProjections(v)))
				return false;
		}
		return true;
	}
	
	private static boolean checkIntervals(float[] i1, float[] i2){
		return (i1[1]-i2[0])*(i2[1]-i1[0])>0;
	}

}
