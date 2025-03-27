package game.tetrisgame;

import game.tetrisgame.Config.Const;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Game extends Application {

    private final Const aConst = Const.getInstance();

    private double time;
    private GraphicsContext g;

    private final int[][] grid = new int[aConst.GRID_WIDTH][aConst.GRID_HEIGHT];

    private final List<Pattern> original = new ArrayList<>();
    private final List<Pattern> patterns = new ArrayList<>();

    private Pattern selected;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(aConst.GRID_WIDTH * aConst.BLOCK_SIZE, aConst.GRID_HEIGHT * aConst.BLOCK_SIZE);

        Canvas canvas = new Canvas(aConst.GRID_WIDTH * aConst.BLOCK_SIZE, aConst.GRID_HEIGHT * aConst.BLOCK_SIZE);
        g = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas);

        original.add(new Pattern(Color.BLUE,
                new Block(0, Direction.DOWN),
                new Block(1, Direction.LEFT),
                new Block(1, Direction.RIGHT),
                new Block(2, Direction.RIGHT)
        ));
        original.add(new Pattern(Color.RED,
                new Block(0, Direction.DOWN),
                new Block(1, Direction.LEFT),
                new Block(1, Direction.RIGHT),
                new Block(1, Direction.DOWN)
        ));

        original.add(new Pattern(Color.GREEN,
                new Block(0, Direction.DOWN),
                new Block(1, Direction.RIGHT),
                new Block(2, Direction.RIGHT),
                new Block(1, Direction.DOWN)));

        original.add(new Pattern(Color.GRAY,
                new Block(0, Direction.DOWN),
                new Block(1, Direction.RIGHT),
                new Block(1, Direction.RIGHT, Direction.DOWN),
                new Block(1, Direction.DOWN)));

        spawn();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    update();
                    render();
                    time = 0;
                }
            }
        };
        timer.start();

        return root;
    }

    private void update() {
        makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
    }

    private void render() {
        g.clearRect(0, 0, aConst.GRID_WIDTH * aConst.BLOCK_SIZE, aConst.GRID_HEIGHT * aConst.BLOCK_SIZE);

        patterns.forEach(p -> p.draw(g));
    }

    private void placePiece(Block block) {
        grid[block.x][block.y]++;
    }

    private void removePiece(Block block) {
        grid[block.x][block.y]--;
    }

    private boolean isOffscreen(Block block) {
        return block.x < 0 || block.x >= aConst.GRID_WIDTH
                || block.y < 0 || block.y >= aConst.GRID_HEIGHT;
    }

    private void makeMove(Consumer<Pattern> onSuccess, Consumer<Pattern> onFail, boolean endMove) {
        selected.getBlocks().forEach(this::removePiece);

        onSuccess.accept(selected);

        boolean offscreen = selected.getBlocks().stream().anyMatch(this::isOffscreen);

        if (!offscreen) {
            selected.getBlocks().forEach(this::placePiece);
        } else {
            onFail.accept(selected);

            selected.getBlocks().forEach(this::placePiece);

            if (endMove) {
                sweep();
            }

            return;
        }

        if (!isValidState()) {
            selected.getBlocks().forEach(this::removePiece);

            onFail.accept(selected);

            selected.getBlocks().forEach(this::placePiece);

            if (endMove) {
                sweep();
            }
        }
    }

    private boolean isValidState() {
        for (int y = 0; y < aConst.GRID_HEIGHT; y++) {
            for (int x = 0; x < aConst.GRID_WIDTH; x++) {
                if (grid[x][y] > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    private void sweep() {
        List<Integer> rows = sweepRows();
        rows.forEach(row -> {
            for (int x = 0; x < aConst.GRID_WIDTH; x++) {
                for (Pattern pattern : patterns) {
                    pattern.detach(x, row);
                }
                grid[x][row]--;
            }
        });

        rows.forEach(row -> {
            patterns.stream().forEach(pattern -> {
                pattern.getBlocks().stream()
                        .filter(block -> block.y < row)
                        .forEach(block -> {
                            removePiece(block);
                            block.y++;
                            placePiece(block);
                        });
            });
        });

        spawn();
    }

    private List<Integer> sweepRows() {
        List<Integer> rows = new ArrayList<>();

        outer:
        for (int y = 0; y < aConst.GRID_HEIGHT; y++) {
            for (int x = 0; x < aConst.GRID_WIDTH; x++) {
                if (grid[x][y] != 1) {
                    continue outer;
                }
            }

            rows.add(y);
        }

        return rows;
    }

    private void spawn() {
        Pattern pattern = original.get(new Random().nextInt(original.size())).copy();
        pattern.move(aConst.GRID_WIDTH / 2, 0);

        selected = pattern;

        patterns.add(pattern);
        pattern.getBlocks().forEach(this::placePiece);

        if (!isValidState()) {
            System.out.println("Game Over");
            System.exit(0);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                makeMove(p -> p.rotate(), p -> p.rotateReverse(), false);
            } else if (e.getCode() == KeyCode.LEFT) {
                makeMove(p -> p.move(Direction.LEFT), p -> p.move(Direction.RIGHT), false);
            } else if (e.getCode() == KeyCode.RIGHT) {
                makeMove(p -> p.move(Direction.RIGHT), p -> p.move(Direction.LEFT), false);
            } else if (e.getCode() == KeyCode.DOWN) {
                makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
            }

            render();
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}