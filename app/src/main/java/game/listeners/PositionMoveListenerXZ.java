package game.listeners;

import android.util.Log;

import game.physics.Collidable;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import game.physics.Vector3D;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PositionMoveListenerXZ implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";

    private SFVertex3f position;
    private SFVertex3f direction;
    private CollisionMediator cm;
    private CollisionBox box;

    public PositionMoveListenerXZ(SFVertex3f position, SFVertex3f direction, CollisionMediator cm, CollisionBox box) {
        this.position = position;
        this.direction = direction;
        this.cm=cm;
        this.box=box;
    }

    @Override
    public void move(float angleXZ, float angleYZ) {
        SFVertex3f v0=new SFVertex3f(position.getX(),position.getY(),position.getZ());
        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tmp1 = new SFVertex3f(direction.getX(),0,direction.getZ());
        SFVertex3f tmp2 = rotationMatrix.Mult(tmp1);
        tmp2.normalize3f();
        tmp2.mult(0.5f);

        position.add3f(tmp2);
        box.setPos(position);
        Collidable c=cm.collide(box);
        if (c!=null) {
            Log.d("Collision", "Coll. with: " + c);
            float s = (float) Math.sqrt(new Vector3D(tmp2).squareModulus());
            position.set3f(v0.getX(), v0.getY(), v0.getZ());
            correctMotion(c, s, 20);
            box.setPos(position);
            if (cm.collide(box) != null) {
                Log.d("Collision","Other coll. with: "+cm.collide(box));
                position.set3f(v0.getX(), v0.getY(), v0.getZ());
            }
        }
    }

    private void correctMotion(Collidable c, float s, int n){
        if (n<2)
            n=2;
        SFVertex3f motion=new SFVertex3f(direction.getX(),0,direction.getZ());
        motion.normalize3f();
        motion.mult3f(s);
        Vector3D v,v0=new Vector3D(position);
        SFVertex3f position0=new SFVertex3f(position.getX(),position.getY(),position.getZ());
        Vector3D k,v1=new Vector3D(motion),w;
        SFTransform3f rot=new SFTransform3f();
        mainloop:
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                v=v0;
                rot.setMatrix(SFMatrix3f.getRotationY((float)((1-2*j)*Math.PI*0.5*i/n)));
                SFVertex3f kv=new SFVertex3f(motion.getX(),motion.getY(),motion.getZ());
                rot.transform(kv);
                k=new Vector3D(kv);
                w=k.mul(v1.dotP(k)/k.squareModulus());
                v=v.add(w);
                position.set3f(v.getX(),v.getY(),v.getZ());
                box.setPos(position);
                if (!box.checkCollision(c.getBox())){
                    break mainloop;
                }
                else {
                    position.set3f(position0.getX(),position0.getY(),position0.getZ());
                }
            }
        }
    }
}
