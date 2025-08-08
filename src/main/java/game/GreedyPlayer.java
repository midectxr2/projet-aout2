package game;

import java.util.List;

/**
 * IA Greedy qui tente de capturer le plus de planètes possible.
 * Elle envoie la moitié de ses vaisseaux disponibles vers la planète la plus proche non possédée.
 */
public class GreedyPlayer extends Player {
    int fleetId;

    public GreedyPlayer(int id) {
        super(id);
    }

    @Override
    public void playTurn(Game game) {
        List<Planet> owned = getOwnedPlanets();
        List<Planet> all = game.getMap().getPlanets();

        for (Planet source : owned) {

            //10 nombre arbitraire
            if (source.getShips() < 10) continue;

            Planet closest = null;
            double bestDist = Double.MAX_VALUE;


            //Planète la plus proche
            for (Planet candidate : all) {
                if (candidate.getOwnerId() != this.getId()) {
                    double d = distance(source, candidate);
                    if (d < bestDist) {
                        bestDist = d;
                        closest = candidate;
                    }
                }
            }

            if (closest != null) {
                int shipsToSend = (int) (source.getShips() * 0.5);
                source.removeShips(shipsToSend);
                Fleet fleet = new Fleet(fleetId ,source, closest, this.getId(), shipsToSend);
                fleetId = fleetId + 1;
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