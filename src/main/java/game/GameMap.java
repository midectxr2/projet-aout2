package game;

import java.util.ArrayList;
import java.util.List;


/**
 * Représente la carte de jeu contenant planètes et flottes.
 */
public class GameMap {

    private List<Planet> planets;
    private List<Fleet> fleets;

    /**
     * guetteur et setteurs de planetes
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Crée une nouvelle carte vide.
     */
    public GameMap(){
        this.planets = new ArrayList<>();
        this.fleets = new ArrayList<>();

    }


    /**
     * Ajoute une planète à la carte.
     * @param p la planète à ajouter
     */
    public void addPlanet(Planet p){
        planets.add(p);

    }


    /**
     * Ajoute une flotte à la carte.
     * @param f la flotte à ajouter
     */
    public void addFleet(Fleet f){
        fleets.add(f);
    }


    /**
     * Retourne la liste des planètes.
     * @return liste des planètes
     */
    public List<Fleet> getFleets() {
        return fleets;
    }

    /**
     * Met à jour la croissance des vaisseaux sur chaque planète.
     */
    public void updatePlanetsGrowth() {
        for (Planet p : planets) {
            p.growShips();
        }
    }


    /**
     * Déplace toutes les flottes selon leur vitesse et traite leur arrivée.
     * @param speed la distance maximale par tour
     */
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
}



