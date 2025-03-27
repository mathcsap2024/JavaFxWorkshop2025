package game.tetrisgame.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML
    private Button button;
    @FXML
    private Pane pane;

    @FXML
    public void test(){
        System.out.println("salam");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Circle circle =  new Circle(10);
        circle.setFill(Color.RED);
        circle.setTranslateX(100);
        circle.setTranslateY(button.getLayoutY());
        pane.getChildren().add(circle);
        button.setText("abbas");
    }
}
