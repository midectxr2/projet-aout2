package game.gui;

import game.Planet;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Représentation d'une planète.
 */
public class PlanetView extends Group {

    private final Planet planet;
    private final Circle circle;
    private final Text shipsText;
    private final Text infoText;
    private final int scale;
    private final int sizeScale;

    public PlanetView(Planet planet, int scale, int sizeScale) {
        this.planet = planet;
        this.scale = scale;
        this.sizeScale = sizeScale;

        double x = planet.getX() * scale;
        double y = planet.getY() * scale;
        double radius = planet.getSize() * sizeScale;

        circle = new Circle(x, y, radius);
        circle.setFill(getColorForPlayer(planet.getOwnerId()));
        circle.setStroke(Color.DARKRED);
        circle.setStrokeWidth(2);

        shipsText = new Text(x - 8, y + 5, String.valueOf((int) Math.round(planet.getShips())));
        shipsText.setFill(Color.BLACK);
        shipsText.setFont(Font.font("Arial", 14));

        infoText = new Text(x - 40, y + radius + 15,
                "Planete " + planet.getId() + "\nTaille : " + planet.getSize() + ", Richesse : " + planet.getRichness());
        infoText.setFill(Color.BLACK);
        infoText.setFont(Font.font("Arial", 11));

        this.getChildren().addAll(circle, shipsText, infoText);
    }

    public void update() {
        circle.setFill(getColorForPlayer(planet.getOwnerId()));
        shipsText.setText(String.valueOf((int) Math.round(planet.getShips())));
    }

    private Color getColorForPlayer(int id) {
        return switch (id) {
            case 1 -> Color.ROYALBLUE;
            case 2 -> Color.CRIMSON;
            default -> Color.LIGHTGRAY;
        };
    }

    public Planet getPlanet() {
        return planet;
    }

}
