package game.gui;

import game.Fleet;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Représentation graphique d'une flotte en déplacement.
 */
public class FleetView extends Circle {

    private final Fleet fleet;
    private final int scale;

    public FleetView(Fleet fleet, int scale) {
        this.fleet = fleet;
        this.scale = scale;

        setRadius(4);
        setFill(getColorForPlayer(fleet.getOwnerId()));
        update();
    }

    public void update() {
        setCenterX(fleet.getX() * scale);
        setCenterY(fleet.getY() * scale);
    }

    private Color getColorForPlayer(int id) {
        return switch (id) {
            case 1 -> Color.ROYALBLUE;
            case 2 -> Color.CRIMSON;
            case 3 -> Color.GREEN;
            case 4 -> Color.WHITE;
            default -> Color.LIGHTGRAY;
        };
    }

    public Fleet getFleet() {
        return fleet;
    }
}
