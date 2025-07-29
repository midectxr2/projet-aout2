package game.gui;

import game.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interface graphique avec menu principal : jouer, configurer ou quitter.
 */
public class GameGUI extends Application {

    private Stage primaryStage;
    private double fleetSpeed = 1.0; // valeur par défaut
    private final List<PlanetView> planetViews = new ArrayList<>();
    private final List<FleetView> fleetViews = new ArrayList<>();
    private final static int WINDOW_WIDTH = 800;
    private final static int WINDOW_HEIGHT = 600;

    private Game game;
    private PlanetView selectedSource = null;
    private int shipsToSend = 0;
    private Pane root;
    private int scale;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showMainMenu();
    }

    private void showMainMenu() {
        VBox menu = new VBox(20);
        menu.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        menu.setStyle("-fx-alignment: center; -fx-background-color: black;");

        Label title = new Label("Choisissez un mode de jeu");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button mode1v1 = new Button("1 Joueur vs 1 IA");
        mode1v1.setOnAction(e -> startGameWithMap("map_1v1.txt", 2));

        Button mode1v1v1 = new Button("1 Joueur vs 2 IA");
        mode1v1v1.setOnAction(e -> startGameWithMap("map_1v1v1.txt", 3));

        Button mode1v1v1v1 = new Button("1 Joueur vs 3 IA");
        mode1v1v1v1.setOnAction(e -> startGameWithMap("map_1v1v1v1.txt", 4));

        Button configButton = new Button("Configurer la vitesse des flottes");
        configButton.setOnAction(e -> showConfigMenu());

        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(title, mode1v1, mode1v1v1, mode1v1v1v1, configButton, quitButton);
        Scene scene = new Scene(menu);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Principal");
        primaryStage.show();
    }

    private void startGameWithMap(String filename, int totalPlayers) {
        try {
            game = GameLoader.loadGameFromFile("src/main/resources/" + filename);
            game.setFleetSpeed(fleetSpeed);

            // Crée les joueurs
            game.clearPlayers();
            game.addPlayer(new HumanPlayer(1));
            for (int i = 2; i <= totalPlayers; i++) {
                game.addPlayer(new GreedyPlayer(i));
            }

            // Répartit aléatoirement les planètes entre les joueurs
            List<Planet> planets = game.getMap().getPlanets();
            Collections.shuffle(planets);
            for (int i = 0; i < planets.size(); i++) {
                int ownerId = (i % totalPlayers) + 1;
                planets.get(i).setOwnerId(ownerId);
            }

            int mapWidth = game.getMap().getWidth();
            int mapHeight = game.getMap().getHeight();
            scale = Math.min(WINDOW_WIDTH / mapWidth, WINDOW_HEIGHT / mapHeight);

            root = new Pane();
            planetViews.clear();
            fleetViews.clear();

            for (Planet p : planets) {
                PlanetView view = new PlanetView(p, scale, 25);
                planetViews.add(view);
                root.getChildren().add(view);

                view.setOnMouseClicked(e -> {
                    if (selectedSource == null && p.getOwnerId() == 1) {
                        openShipSelectionDialog(view);
                    } else if (selectedSource != null && p.getOwnerId() != 1) {
                        confirmFleetDispatch(selectedSource.getPlanet(), view.getPlanet(), shipsToSend);
                        selectedSource = null;
                        shipsToSend = 0;
                    }
                });
            }

            Button nextTurn = new Button("Tour suivant");
            nextTurn.setLayoutX(10);
            nextTurn.setLayoutY(10);
            nextTurn.setOnAction(e -> {
                game.nextTurn();
                game.eliminateDefeatedPlayers();

                for (PlanetView pv : planetViews) {
                    pv.update();
                }

                updateFleetViews();
            });

            root.getChildren().add(nextTurn);

            Scene gameScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Partie en cours");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showConfigMenu() {
        VBox config = new VBox(15);
        config.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        config.setStyle("-fx-alignment: center; -fx-background-color: black;");

        Label label = new Label("Vitesse des flottes :");
        label.setStyle("-fx-text-fill: white;");

        Slider speedSlider = new Slider(0.5, 10, fleetSpeed);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setBlockIncrement(0.5);

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> showMainMenu());

        Button applyButton = new Button("Appliquer");
        applyButton.setOnAction(e -> {
            fleetSpeed = speedSlider.getValue();
            showMainMenu();
        });

        config.getChildren().addAll(label, speedSlider, applyButton, backButton);
        Scene scene = new Scene(config);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configuration");
    }

    private void openShipSelectionDialog(PlanetView view) {
        selectedSource = view;
        Planet source = view.getPlanet();

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Envoyer des vaisseaux");
        dialog.setHeaderText("Planete " + source.getId());

        Label label = new Label("Nombre de vaisseaux a envoyer (max: " + (int) source.getShips() + "):");
        Spinner<Integer> spinner = new Spinner<>(1, (int) source.getShips(), 1);

        VBox content = new VBox(10, label, spinner);
        content.setAlignment(Pos.CENTER_LEFT);
        dialog.getDialogPane().setContent(content);

        ButtonType okButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return spinner.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(val -> shipsToSend = val);
    }

    private void confirmFleetDispatch(Planet source, Planet target, int ships) {
        if (ships > 0 && source.getShips() >= ships) {
            source.removeShips(ships);
            Fleet fleet = new Fleet(source, target, 1, ships);
            game.getMap().addFleet(fleet);

            FleetView fleetView = new FleetView(fleet, scale);
            fleetViews.add(fleetView);
            root.getChildren().add(fleetView);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Flotte envoyee");
            alert.setContentText("Vous avez envoye " + ships + " vaisseaux vers la planete " + target.getId());
            alert.showAndWait();
        }
    }

    private void updateFleetViews() {
        List<Fleet> fleets = game.getMap().getFleets();


        for (Fleet f : fleets) {
            boolean alreadyPresent = fleetViews.stream().anyMatch(fv -> fv.getFleet() == f);
            if (!alreadyPresent) {
                FleetView newView = new FleetView(f, scale);
                fleetViews.add(newView);
                root.getChildren().add(newView);
            }
        }


        fleetViews.removeIf(view -> !fleets.contains(view.getFleet()));
        root.getChildren().removeIf(node -> node instanceof FleetView && !fleets.contains(((FleetView) node).getFleet()));

        // Maj pos
        for (FleetView fv : fleetViews) {
            fv.update();
        }
    }
}
