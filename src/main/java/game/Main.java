package game;


import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale qui lance l'application JavaFX.
 */

public class Main extends Application{
    /**
     * Point d'entrée JavaFX. Lance l'interface graphique.
     * @param primarystage la fenêtre principale
     */
    @Override
    public void start(Stage primarystage) {
        GameGUI gui = new GameGUI();
        gui.start(primarystage);
    }

    /**
     * Point d'entrée principal du programme.
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args){
        launch(args);
    }
}
