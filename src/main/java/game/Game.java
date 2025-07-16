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
    public Game(GameMap map, List<Player> players){
        this.map = map;
        this.players = players;
        this.currentTurn = 0;
    }


    /**
     * Effectue le traitement d'un tour de jeu.
     */
    public void nextTurn(){
        //logique d'un tour complet : croissance des planètes, déplacements, résolutions
    }


    /**
     * Retourne la carte de jeu actuelle.
     * @return la carte de jeu
     */
    public GameMap getMap(){
        return map;
    }

}

