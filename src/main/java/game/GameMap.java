package game;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private List<Planet> planets = new ArrayList<>();
    private List<Fleet> fleets = new ArrayList<>();

    public List<Planet> getPlanets() {
        return planets;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void addFleet(Fleet fleet) {
        fleets.add(fleet);
    }

    public void moveFleets(double speed) {
        List<Fleet> arrived = new ArrayList<>();
        for (Fleet f : fleets) {
            f.advance(speed);
            if (f.hasArrived()) {
                f.applyArrival();
                arrived.add(f);
            }
        }
        fleets.removeAll(arrived);
    }

    public void updatePlanetsGrowth() {
        for (Planet p : planets) {
            p.growShips();
        }
    }

    public void removeFleetsByPlayer(int playerId) {
        fleets.removeIf(fleet -> fleet.getOwnerId() == playerId);
    }
}