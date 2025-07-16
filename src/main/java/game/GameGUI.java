package game;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameGUI {
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("SolarMax Tour par Tour");
        stage.setScene(scene);
        stage.show();
    }
}

