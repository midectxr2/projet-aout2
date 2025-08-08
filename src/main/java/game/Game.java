package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère la logique principale du jeu.
 */
public class Game {
    private double fleetSpeed = 1.0;
    private GameMap map;
    private List<Player> players = new ArrayList<>();
    private int currentTurn;

    /**
     * Définit la vitesse globale des flottes.
     * @param speed valeur en unités de distance par tour
     */
    public void setFleetSpeed(double speed) {
        this.fleetSpeed = speed;
    }

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
     * Avance d'un tour dans la partie :
     * - met à jour les planètes possédées par chaque joueur,
     * - fait jouer les joueurs actifs,
     * - déplace les flottes,
     * - fait croître les planètes.
     * La progression est interrompue si la partie est terminée.
     */
    public void nextTurn() {
        updateOwnerships();
        if (isGameOver()) return;

        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                p.playTurn(this);
            }
        }
        map.moveFleets(fleetSpeed);
        map.updatePlanetsGrowth();
        currentTurn++;
        updateOwnerships();
    }

    /**
     * Met à jour la liste des planètes possédées par chaque joueur.
     * Doit être appelée après chaque changement de propriété planétaire.
     */
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


    /**
     * Vérifie si un seul joueur ou moins possède encore des planètes.
     * @return true si la partie est terminée
     */
    public boolean isGameOver() {
        int alive = 0;
        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                alive++;
            }
        }
        return alive <= 1;
    }




    /**
     * Retourne le joueur encore en vie (non éliminé), s’il y en a un.
     * @return le gagnant ou null si aucun joueur n’a de planète
     */
    public Player getWinner() {
        for (Player p : players) {
            if (!p.getOwnedPlanets().isEmpty()) {
                return p;
            }
        }
        return null;
    }


    /**
     * Élimine les joueurs sans planète.
     * Supprime leurs flottes restantes.
     */
    public void eliminateDefeatedPlayers() {
        for (Player p : players) {
            if (p.getOwnedPlanets().isEmpty()) {
                // Annuler les flottes en cours de ce joueur
                map.removeFleetsByPlayer(p.getId());
                System.out.println("Le joueur " + p.getId() + " a été éliminé.");
            }
        }
    }

    public void clearPlayers() {
        players.clear();
    }

    public void addPlayer(Player p) {
        players.add(p);
    }


    public void cancelFleet(Player p){
     map.removeFleetsByPlayer(p.getId());
    }









}
