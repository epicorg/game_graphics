package game.physics;

import shadow.math.SFVertex3f;

public class CircleBox implements CollisionBox {

    private SFVertex3f v;
    private float radius;

    public CircleBox(float radius) {
        super();
        this.radius = radius;
    }

    public CircleBox(Box box) {
        this.radius = (float) Math.sqrt(new Vector3D(box.getLength(), box.getWidth(), 0).squareModulus() / 4);
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public boolean checkPoint(SFVertex3f v) {
        return (new Vector3D(this.v).sub(new Vector3D(v)).squareModulus() < radius * radius);
    }

    @Override
    public CircleBox setPos(SFVertex3f v) {
        this.v = v;
        return this;
    }

    @Override
    public boolean checkCollision(CollisionBox box) {
        if (box instanceof CircleBox) {
            CircleBox b = (CircleBox) box;
            float d = radius + b.radius;
            return (new Vector3D(v).sub(new Vector3D(b.v)).squareModulus() < d * d);
        } else if (box instanceof Box) {
            Box b = (Box) box;
            float w = b.getWidth() / 2, h = b.getLength() / 2;
            float dx = v.getX() - b.getPos().getX(), dy = v.getY() - b.getPos().getY();
            float mx = radius + w, my = radius + h;
            if (dx * dx > mx * mx || dy * dy > my * my)
                return false;
            float q = Math.abs(dx) - w, k = Math.abs(dy) - h;
            if (q > 0 && k > 0 && k * k + q * q > radius * radius) {
                return false;
            }
            return true;
        }
        return false;
    }


}
