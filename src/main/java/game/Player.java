package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un joueur (humain ou IA).
 */
public abstract class Player {
    protected int id;
    protected List<Planet> ownedPlanets;

    public Player(int id) {
        this.id = id;
        this.ownedPlanets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void addPlanet(Planet p) {
        ownedPlanets.add(p);
    }

    public void clearPlanets() {
        ownedPlanets.clear();
    }

    public List<Planet> getOwnedPlanets() {
        return ownedPlanets;
    }

    public abstract void playTurn(Game game);
}