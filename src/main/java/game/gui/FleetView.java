package game.gui;

import game.Fleet;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Représentation graphique d'une flotte en déplacement sur la carte.
 * Cette vue est un cercle coloré, dont la couleur dépend du joueur propriétaire.
 * La position du cercle est mise à jour à partir des coordonnées actuelles de l'objet.
 */
public class FleetView extends Circle {

    private final Fleet fleet;
    private final int scale;


    /**
     * Construit la vue graphique d'une flotte.
     *
     * @param fleet flotte associée à cette vue
     * @param scale facteur d'échelle pour le positionnement (doit être positif)
     */
    public FleetView(Fleet fleet, int scale) {
        this.fleet = fleet;
        this.scale = scale;

        setRadius(4);
        setFill(getColorForPlayer(fleet.getOwnerId()));
        update();
    }



    /**
     * Met à jour la position graphique de la flotte en fonction
     * de ses coordonnées actuelles dans le modèle
     */
    public void update() {
        setCenterX(fleet.getX() * scale);
        setCenterY(fleet.getY() * scale);
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
