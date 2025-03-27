package game.tetrisgame.Config;

public class Const {
    public int BLOCK_SIZE = 40;
    public int GRID_WIDTH = 15;
    public int GRID_HEIGHT = 20;
    private static Const instance;
    public static Const getInstance(){
        if (instance == null){
            instance = new Const();
        }
        return instance;
    }

}
