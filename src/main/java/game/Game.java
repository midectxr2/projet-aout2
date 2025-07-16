package game;

import java.util.List;

public class Game {
    private GameMap map;
    private List<Player> players;
    private int currentTurn;

    public Game(GameMap map, List<Player> players){
        this.map = map;
        this.players = players;
        this.currentTurn = 0;
    }
    public void nextTurn(){
        //logique d'un tour complet : croissance des planètes, déplacements, résolutions
    }

    public GameMap getMap(){
        return map;
    }

}

