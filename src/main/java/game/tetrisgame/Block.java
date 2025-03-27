package game.tetrisgame;

import java.util.Arrays;
import java.util.List;

public class Block {

    public int distance;
    public List<Direction> directions;
    public Pattern parent;
    public int x, y;

    public Block(int distance, Direction... direction) {
        this.distance = distance;
        this.directions = Arrays.asList(direction);
    }

    public void setParent(Pattern parent) {
        this.parent = parent;

        updateCoordinates(parent);
    }

    private void updateCoordinates(Pattern parent) {
        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.getX();
            dy += distance * d.getY();
        }

        x = parent.getX() + dx;
        y = parent.getY() + dy;
    }

    public void setDirection(Direction... direction) {
        this.directions = Arrays.asList(direction);

        updateCoordinates(parent);
    }

    public Block copy() {
        return new Block(distance, directions.toArray(new Direction[0]));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}