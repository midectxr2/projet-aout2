package game;

import java.util.Comparator;
import java.util.List;

/**
 * IA "Greedy" qui tente de capturer le plus de planètes possible.
 * Elle envoie la moitié de ses vaisseaux disponibles vers la planète la plus proche non possédée.
 */
public class GreedyPlayer extends Player {

    public GreedyPlayer(int id) {
        super(id);
    }

    @Override
    public void playTurn(Game game) {
        List<Planet> owned = getOwnedPlanets();
        List<Planet> all = game.getMap().getPlanets();

        for (Planet source : owned) {
            if (source.getShips() < 2) continue;

            // Trouver la planète non possédée la plus proche
            Planet target = all.stream()
                    .filter(p -> p.getOwnerId() != this.getId())
                    .min(Comparator.comparingDouble(p -> distance(source, p)))
                    .orElse(null);

            if (target != null && target != source) {
                int shipsToSend = (int) (source.getShips() * 0.5);
                source.removeShips(shipsToSend);
                Fleet fleet = new Fleet(source, target, this.getId(), shipsToSend);
                game.getMap().addFleet(fleet);
            }
        }
    }

    private double distance(Planet a, Planet b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

