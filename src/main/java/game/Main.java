package game;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primarystage) {
        GameGUI gui = new GameGUI();
        gui.start(primarystage);
    }

    public static void main(String[] args){
        launch(args);
    }
}
