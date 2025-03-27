package game.tetrisgame;

import game.tetrisgame.Config.Const;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Pattern {
    private final Const aConst = Const.getInstance();
    private int x, y;
    private final Color color;
    private final List<Block> blocks;

    public Pattern(Color color, Block... pieces) {
        this.color = color;
        this.blocks = new ArrayList<>(Arrays.asList(pieces));

        for (Block piece : this.blocks)
            piece.setParent(this);
    }

    public Pattern(Color color, List<Block> pieces) {
        this.color = color;
        this.blocks = pieces;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;

        blocks.forEach(b -> {
            b.setX(b.getX() + dx);
            b.setY(b.getY() + dy);
        });
    }

    public void move(Direction direction) {
        move(direction.getX(), direction.getY());
    }

    public void draw(GraphicsContext g) {
        g.setFill(color);
        blocks.forEach(b -> g.fillRect(b.getX() * aConst.BLOCK_SIZE, b.getY() * aConst.BLOCK_SIZE, aConst.BLOCK_SIZE, aConst.BLOCK_SIZE));
    }

    public void rotateReverse() {
        blocks.forEach(b -> b.setDirection(b.directions.stream().map(Direction::prev).toList().toArray(new Direction[0])));
    }

    public void rotate() {
        blocks.forEach(p -> p.setDirection(p.directions.stream().map(Direction::next).toList().toArray(new Direction[0])));
    }

    public void detach(int x, int y) {
        blocks.removeIf(b -> b.getX() == x && b.getY() == y);
    }

    public Pattern copy(){
        return new Pattern(color, blocks.stream().map(Block::copy)
                .toList().toArray(new Block[0]));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
