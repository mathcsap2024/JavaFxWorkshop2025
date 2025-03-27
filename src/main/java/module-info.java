module game.tetrisgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.tetrisgame to javafx.fxml;
    exports game.tetrisgame;
}