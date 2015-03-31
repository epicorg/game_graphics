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
        this.radius = new SFVertex3f(box.getLength(), box.getWidth(), 0).getLength() / 4;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public boolean checkPoint(SFVertex3f v) {
        SFVertex3f v0=new SFVertex3f(this.v);
        v0.subtract(v);
        return (v0.getSquareModulus() < radius * radius);
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
            SFVertex3f v0=new SFVertex3f(v);
            v0.subtract(b.v);
            return (v0.getSquareModulus() < d * d);
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
