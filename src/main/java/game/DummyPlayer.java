package game;

import java.util.List;
import java.util.Random;

/**
 * Implémentation simple d'un joueur IA qui envoie aléatoirement des flottes.
 */
public class DummyPlayer extends Player {
    private Random rand = new Random();
    private int fleetId;

    public DummyPlayer(int id) {
        super(id);
    }

    @Override
    public void playTurn(Game game) {
        List<Planet> owned = this.getOwnedPlanets();
        List<Planet> targets = game.getMap().getPlanets();

        for (Planet source : owned) {
            if (source.getShips() > 5) {
                Planet target = targets.get(rand.nextInt(targets.size()));
                if (target != source) {
                    int shipsToSend = (int) (source.getShips() * 0.5);
                    source.removeShips(shipsToSend);
                    Fleet fleet = new Fleet(fleetId, source, target, id, shipsToSend);
                    fleetId = fleetId + 1;
                    game.getMap().addFleet(fleet);
                }
            }
        }
    }
}

