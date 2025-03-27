module game.tetrisgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.tetrisgame to javafx.fxml;
    opens game.tetrisgame.Controllers to javafx.fxml;
    exports game.tetrisgame;
    exports game.tetrisgame.Controllers;
}