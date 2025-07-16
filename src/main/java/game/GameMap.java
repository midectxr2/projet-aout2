package game;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private List<Planet> planets;

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public GameMap(){
        this.planets = new ArrayList<>();

    }
    public void addPlanet(Planet p){
        planets.add(p);

    }

    public void updatePlanetsGrowth() {
        for (Planet p : planets) {
            p.growShips();
        }
    }

}
