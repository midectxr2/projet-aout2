package game.gui;

import game.Fleet;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Représentation graphique d'une flotte en déplacement sur la carte.
 * Affiche un point coloré + le nombre de vaisseaux sous la flotte.
 */
public class FleetView extends Group {

    private final Fleet fleet;
    private final int scale;

    private final Circle dot;
    private final Text shipsText;

    /**
     * @param fleet flotte associée à cette vue
     * @param scale facteur d'échelle
     */
    public FleetView(Fleet fleet, int scale) {

        this.fleet = fleet;
        this.scale = scale;

        double cx = fleet.getX() * scale;
        double cy = fleet.getY() * scale;

        dot = new Circle(cx, cy, 4);
        dot.setFill(getColorForPlayer(fleet.getOwnerId()));

        shipsText = new Text(cx, cy + 12, String.valueOf((int) Math.round(fleet.getShips())));
        shipsText.setFont(Font.font(11));
        shipsText.setFill(Color.BLACK);


        getChildren().addAll(dot, shipsText);
        // on clique sur les enfants, pas sur le bounding box vide
        setPickOnBounds(false);
    }



    /** Met à jour la position + couleur + valeur affichée. */
    public void update() {
        double cx = fleet.getX() * scale;
        double cy = fleet.getY() * scale;

        dot.setCenterX(cx);
        dot.setCenterY(cy);
        dot.setFill(getColorForPlayer(fleet.getOwnerId()));

        shipsText.setX(cx);
        shipsText.setY(cy + 12);
        shipsText.setText(String.valueOf((int) Math.round(fleet.getShips())));
    }



    /**
     * Retourne la couleur associée à un identifiant de joueur.
     *
     * @param id identifiant du joueur propriétaire (0 = neutre)
     * @return couleur associée à ce joueur ou gris clair si neutre/inconnu
     */
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
