package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap {
    private List<Planet> planets = new ArrayList<>();
    private List<Fleet> fleets = new ArrayList<>();

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int height;

    public List<Planet> getPlanets() {
        return planets;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void addFleet(Fleet fleet) {
        fleets.add(fleet);
    }


    /**
     * Fait avancer toutes les flottes d'une distance donnée.
     * Applique leur effet à l'arrivée.
     * @param speed distance parcourue par tour
     */
    public void moveFleets(double speed) {
        List<Fleet> arrived = new ArrayList<>();
        Collections.shuffle(fleets);
        for (Fleet f : fleets) {
            f.advance(speed);
            if (f.hasArrived()) {
                f.applyArrival();
                arrived.add(f);
            }
        }
        fleets.removeAll(arrived);
    }


    /**
     * Fait croître chaque planète en fonction de sa richesse.
     */
    public void updatePlanetsGrowth() {
        for (Planet p : planets) {
            p.growShips();
        }
    }


    /**
     * Supprime toutes les flottes appartenant à un joueur donné.
     * @param playerId identifiant du joueur éliminé
     */
    public void removeFleetsByPlayer(int playerId) {
        fleets.removeIf(fleet -> fleet.getOwnerId() == playerId);
    }

    public List<Fleet> getFleets() {
        return fleets;
    }

    public List<Planet> getPlanetsOwnedBy(int id) {
        List<Planet> res = new ArrayList<>();
        for (Planet p : planets) {
            if (p.getId() == id) {
                res.add(p);
            }
        }
        return res;
    }
}