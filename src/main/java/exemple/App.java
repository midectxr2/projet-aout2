package exemple;
import exemple.Util;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label lbl = new Label(
                "Hello, JavaFX " + javafxVersion + 
                ", running on Java " + javaVersion + "." +
                "\nAlso the answer is " + Util.theAnswer());
        lbl.setWrapText(true);
        /* The image is load from the resources. */
        ImageView marvin = new ImageView(new Image("/marvin.png"));
        VBox vbox = new VBox(8, lbl, marvin);
        vbox.setAlignment(Pos.CENTER);
        marvin.setPreserveRatio(true);
        marvin.fitWidthProperty().bind(lbl.widthProperty().multiply(0.75));
        marvin.fitHeightProperty().bind(vbox.heightProperty().multiply(0.75));
        vbox.setMinWidth(lbl.getMinWidth());
        System.out.println(lbl.getMinWidth());
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setWidth(640);
        stage.setHeight(480);
        stage.setTitle("My Java Project");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
