package game;

import java.util.List;

/**
 * Gère la logique principale du jeu.
 */
public class Game {
    private GameMap map;
    private List<Player> players;
    private int currentTurn;

    /**
     * Initialise une nouvelle partie.
     * @param map la carte du jeu
     * @param players la liste des joueurs
     */
    public Game(GameMap map, List<Player> players) {
        this.map = map;
        this.players = players;
        this.currentTurn = 0;
        updateOwnerships();

    }

    /**
     * Effectue le traitement d'un tour de jeu.
     */
    public void nextTurn() {
        updateOwnerships();
        if (isGameOver()) return;

        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                p.playTurn(this);
            }
        }
        map.moveFleets(5.0);
        map.updatePlanetsGrowth();
        currentTurn++;
        updateOwnerships();
    }

    private void updateOwnerships() {
        for (Player p : players) {
            p.clearPlanets();
        }
        for (Planet planet : map.getPlanets()) {
            int owner = planet.getOwnerId();
            if (owner > 0 && owner <= players.size()) {
                players.get(owner - 1).addPlanet(planet);
            }
        }
    }

    public GameMap getMap() {
        return map;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        int alive = 0;
        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                alive++;
            }
        }
        return alive <= 1;
    }

    public Player getWinner() {
        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                return p;
            }
        }
        return null;
    }

    public void eliminateDefeatedPlayers() {
        for (Player p : players) {
            if (p.getOwnedPlanets().isEmpty()) {
                // Annuler les flottes en cours de ce joueur
                map.removeFleetsByPlayer(p.getId());
                System.out.println("Le joueur " + p.getId() + " a été éliminé.");
            }
        }
    }





}
