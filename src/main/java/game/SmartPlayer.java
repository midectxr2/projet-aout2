package game;

import java.util.List;

/**
 * IA un peu plus avancée : attaque si la planète ennemie est proche et faiblement défendue.
 */
public class SmartPlayer extends Player {

    private static final int ATTACK_THRESHOLD = 20; // minimum de vaisseaux pour attaquer
    private static final double ATTACK_RATIO = 1.5;  // doit avoir 1.5x plus de vaisseaux que la cible

    public SmartPlayer(int id) {
        super(id);
    }

    @Override
    public void playTurn(Game game) {
        List<Planet> myPlanets = game.getMap().getPlanetsOwnedBy(id);
        List<Planet> allPlanets = game.getMap().getPlanets();

        for (Planet source : myPlanets) {
            if (source.getShips() < ATTACK_THRESHOLD) continue;

            Planet bestTarget = null;
            double bestDistance = Double.MAX_VALUE;

            for (Planet p : allPlanets) {
                if (p.getOwnerId() == id) continue;
                if (source.getShips() <= p.getShips() * ATTACK_RATIO) continue;

                double dist = source.distanceTo(p);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestTarget = p;
                }
            }

            if (bestTarget != null) {
                int shipsToSend = (int) (source.getShips() * 0.5);
                source.removeShips(shipsToSend);
                Fleet fleet = new Fleet(source, bestTarget, id, shipsToSend);
                game.getMap().addFleet(fleet);
            }
        }
    }
}
