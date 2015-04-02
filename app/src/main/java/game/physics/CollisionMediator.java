package game.physics;

import java.util.LinkedList;
import java.util.List;

import shadow.math.SFVertex3f;

public class CollisionMediator {

    public static final String LOG_TAG = "CollisionMediator";

    private List<Square> squareList = new LinkedList<>();
    private List<Circle> circleList = new LinkedList<>();

    public void addObject(Square s) {
        squareList.add(s);
    }

    public void addObject(Circle c) {
        circleList.add(c);
    }

    public boolean collide(Square square) {
        for (Square s : squareList) {
            if (checkCollision(s, square)) {
                return true;
            }
        }
        for (Circle c : circleList) {
            if (checkCollision(c, square)) {
                return true;
            }
        }
        return false;
    }

    public boolean collide(Circle circle) {
        for (Square s : squareList) {
            if (checkCollision(s, circle)) {
                return true;
            }
        }
        for (Circle c : circleList) {
            if (checkCollision(c, circle)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollision(Circle circle1, Circle circle2) {
        float d = circle1.getRadius() + circle2.getRadius();
        SFVertex3f v0 = new SFVertex3f(circle1.getPos());
        v0.subtract(circle2.getPos());
        return (v0.getSquareModulus() < d * d);
    }

    public boolean checkCollision(Circle circle1, Square square2) {
        float w = square2.getxSize() / 2, h = square2.getzSize() / 2;
        float dx = circle1.getPos().getX() - square2.getPos().getX();
        float dz = circle1.getPos().getZ() - square2.getPos().getZ();
        float mx = circle1.getRadius() + w;
        float mz = circle1.getRadius() + h;
        if (dx * dx > mx * mx || dz * dz > mz * mz)
            return false;
        float q = Math.abs(dx) - w, k = Math.abs(dz) - h;
        if (q > 0 && k > 0 && k * k + q * q > circle1.getRadius() * circle1.getRadius()) {
            return false;
        }
        return true;
    }

    public boolean checkCollision(Square square2, Circle circle1) {
        return checkCollision(circle1, square2);
    }

    public boolean checkCollision(Square square1, Square square2) {
        float x1 = square1.getPos().getX(), x2 = square2.getPos().getX();
        float z1 = square1.getPos().getZ(), z2 = square2.getPos().getZ();
        return ((Math.abs(x1 - x2) < (square1.getxSize() + square2.getxSize()) / 2)
                && (Math.abs(z1 - z2) < (square1.getzSize() + square2.getzSize()) / 2));
    }

    public void clear() {
        squareList.clear();
        circleList.clear();
    }

}
