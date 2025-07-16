package game;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
/**
 * Classe responsable de l'interface graphique du jeu.
 */
public class GameGUI {

    /**
     * Lance l'affichage de la fenêtre principale du jeu.
     * @param stage la fenêtre JavaFX principale
     */
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("SolarMax Tour par Tour");
        stage.setScene(scene);
        stage.show();
    }
}

