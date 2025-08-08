package game.gui;

import game.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.security.KeyStore;
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
    private int id;


    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showMainMenu();
    }

    private void showMainMenu() {
        VBox menu = new VBox(20);
        menu.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        menu.setStyle("-fx-alignment: center; -fx-background-color: black;");

        Label title = new Label("Carte chargee. Selectionnez les types de joueurs :");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button playButton = new Button("Lancer la partie");
        playButton.setOnAction(e -> showPlayerAssignmentMenu());

        Button configButton = new Button("Configurer la vitesse des flottes");
        configButton.setOnAction(e -> showConfigMenu());

        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(title, playButton, configButton, quitButton);
        Scene scene = new Scene(menu);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Principal");
        primaryStage.show();
    }

    private void showPlayerAssignmentMenu() {
        try {
            game = GameLoader.loadGameFromFile("src/main/resources/map.txt");
            game.setFleetSpeed(fleetSpeed);

            VBox assignMenu = new VBox(10);
            assignMenu.setStyle("-fx-alignment: center; -fx-padding: 20;");

            Label title = new Label("Assignez les types de joueurs :");
            List<ComboBox<String>> selectors = new ArrayList<>();

            int basePlayers = 2;
            int maxPlayers = 4;
            for (int i = 1; i <= maxPlayers; i++) {
                Label label = new Label("Joueur " + i);
                ComboBox<String> selector = new ComboBox<>();

                //suppression de la possibilité de choisir un autre humain dans la partie
                selector.getItems().addAll("AUCUN", "IA : Dummy", "IA : Greedy", "IA : Smart");
                if (i == 1) selector.setValue("HUMAIN");
                else if (i <= basePlayers) selector.setValue("IA : Greedy");
                else selector.setValue("AUCUN");
                selectors.add(selector);
                assignMenu.getChildren().addAll(label, selector);
            }

            Button startButton = new Button("Démarrer la partie");
            startButton.setOnAction(e -> {
                game.clearPlayers();
                int playerId = 1;
                for (ComboBox<String> selector : selectors) {
                    String choice = selector.getValue();
                    switch (choice) {
                        case "HUMAIN" -> game.addPlayer(new HumanPlayer(playerId++));
                        case "IA : Dummy" -> game.addPlayer(new DummyPlayer(playerId++));
                        case "IA : Greedy" -> game.addPlayer(new GreedyPlayer(playerId++));
                        case "IA : Smart" -> game.addPlayer(new SmartPlayer(playerId++));
                        default -> {} // AUCUN ou non sélectionné
                    }
                }
                reassignPlanets();
                launchGame();
            });

            assignMenu.getChildren().add(startButton);
            primaryStage.setScene(new Scene(assignMenu, WINDOW_WIDTH, WINDOW_HEIGHT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reassignPlanets() {
        List<Planet> planets = game.getMap().getPlanets();
        int totalPlayers = game.getPlayers().size();
        Collections.shuffle(planets);
        for (int i = 0; i < planets.size(); i++) {
            int ownerId = (i % totalPlayers) + 1;
            planets.get(i).setOwnerId(ownerId);
        }
    }

    private void launchGame() {
        System.out.println(fleetSpeed);
        List<Planet> planets = game.getMap().getPlanets();
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

        Button cancelAllFleetsBtn = new Button("Annuler toutes mes flottes");
        cancelAllFleetsBtn.setLayoutX(10);
        cancelAllFleetsBtn.setLayoutY(40);
        cancelAllFleetsBtn.setOnAction(e -> {
            //dans tout les cas dans la liste de joueur le joueur indice 0 sera le joueur humain
            game.cancelFleet(game.getPlayers().get(0));
        });

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

            if(game.isGameOver()){
                Player winner = game.getWinner();
                showGameOverScreenModal(root.getScene().getWindow(), winner);

            }
        });

        root.getChildren().addAll(nextTurn, cancelAllFleetsBtn);

        Scene gameScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Partie en cours");
    }


    private void showConfigMenu() {
        VBox config = new VBox(15);
        config.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        config.setStyle("-fx-alignment: center; -fx-background-color: black;");

        Label label = new Label("Vitesse des flottes :");
        label.setStyle("-fx-text-fill: white;");

        Slider speedSlider = new Slider(0.5, 5, fleetSpeed);
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
            id = id + 1;
            Fleet fleet = new Fleet(id, source, target, 1, ships);
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
    private void showGameOverScreenModal(Window owner, Player winner) {
        VBox content = new VBox(12);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        Label title = new Label("Partie terminee");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");

        Label winnerLbl = new Label("Vainqueur : " + (winner != null ? winner.getId() : "—"));

        Button replayBtn = new Button("Rejouer");
        Button menuBtn   = new Button("Menu principal");
        Button quitBtn   = new Button("Quitter");
        HBox buttons = new HBox(10, replayBtn, menuBtn, quitBtn);
        buttons.setAlignment(Pos.CENTER);

        content.getChildren().addAll(title, winnerLbl, buttons);


        //ChatGPT
        Stage dlg = new Stage(StageStyle.DECORATED);
        dlg.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) dlg.initOwner(owner);
        dlg.setTitle("Fin de partie");
        dlg.setScene(new Scene(content));

        replayBtn.setOnAction(e -> {
            dlg.close();
            showPlayerAssignmentMenu();
        });
        menuBtn.setOnAction(e -> {
            dlg.close();
           showMainMenu();
        });
        quitBtn.setOnAction(e -> Platform.exit());

        dlg.showAndWait();
    }
}
