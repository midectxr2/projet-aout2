package gametest;

import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires.
 */
public class GameTests {

    private Planet p;

    @BeforeEach
    public void setup() {
        p = new Planet(1, 0, 0, 1.0, 1.0, 1, 10); // planète de joueur 1 avec 10 vaisseaux
    }

    /**
     * Vérifie qu'une flotte alliée ajoute bien ses vaisseaux à la planète.
     */
    @Test
    public void testAjoutVaisseauxMemeJoueur() {
        p.receiveFleet(1, 5);
        assertEquals(15, (int) p.getShips());
        assertEquals(1, p.getOwnerId());
    }

    /**
     * Vérifie qu'une flotte ennemie plus grande capture la planète.
     */
    @Test
    public void testCaptureParAutreJoueur() {
        p.receiveFleet(2, 15);
        assertEquals(5, (int) p.getShips());
        assertEquals(2, p.getOwnerId());
    }

    /**
     * Vérifie qu'une attaque plus faible échoue et que la défense est réduite.
     */
    @Test
    public void testDefaiteAttaquant() {
        p.receiveFleet(2, 5);
        assertEquals(5, (int) p.getShips());
        assertEquals(1, p.getOwnerId());
    }

    /**
     * Vérifie qu'en cas d'égalité, la planète devient neutre.
     */
    @Test
    public void testNeutralisationParEgalite() {
        p.receiveFleet(2, 10);
        assertEquals(0, (int) p.getShips());
        assertEquals(0, p.getOwnerId());
    }

    /**
     * Vérifie qu'on ne peut pas envoyer tous les vaisseaux d'une planète.
     */
    @Test
    public void testFlotteNePeutPasViderPlanete() {
        Planet source = new Planet(1,1, 1, 1.0, 1.0, 1, 10);
        Fleet f = new Fleet(1,source, p, 1, 10);
        assertFalse(f.getShips() < source.getShips());
    }

    /**
     * Vérifie que la flotte avance bien dans l'espace après un déplacement.
     */
    @Test
    public void testAvancementFlotte() {
        Planet source = new Planet(1,0, 0, 1, 1, 1, 10);
        Planet target = new Planet(2,3, 4, 1, 1, 2, 5); // distance = 5
        Fleet f = new Fleet(1,source, target, 1, 5);
        f.advance(2);
        //distance = 5 -> direction: (0.6,0.8), donc position après 2 tours: (1.2, 1.8).
        //Voir graphe dans le rapport.
        assertEquals(1, Math.round(f.getX()));
        assertEquals(2, Math.round(f.getY()));
    }

    /**
     * Vérifie qu'une flotte arrivée applique ses effets sur la planète cible.
     */
    @Test
    public void testArriveeFlotteAppliqueEffets() {
        Planet cible = new Planet(2, 5, 5, 1, 1, 2, 3);
        Fleet f = new Fleet(1, p, cible, 1, 10);
        f.advance(100);
        assertTrue(f.hasArrived());
        f.applyArrival();
        assertEquals(7, (int) cible.getShips());
        assertEquals(1, cible.getOwnerId());
    }

    /**
     * Vérifie que les planètes du joueur gagnent bien des vaisseaux à chaque tour.
     */
    @Test
    public void testCroissancePlanete() {
        Planet croissante = new Planet(1, 0, 0, 1, 1.5, 1, 10);
        GameMap map = new GameMap();
        map.addPlanet(croissante);
        map.updatePlanetsGrowth();
        assertEquals(11.5, croissante.getShips());
    }

    /**
     * Vérifie qu'une planète neutre ne croît pas.
     */
    @Test
    public void testPasDeCroissanceSiNeutre() {
        Planet neutre = new Planet(0,0, 0, 1, 2.0, 0, 5);
        GameMap map = new GameMap();
        map.addPlanet(neutre);
        map.updatePlanetsGrowth();
        assertEquals(5, neutre.getShips());
    }

    /**
     * Vérifie que la suppression des flottes d'un joueur fonctionne.
     */
    @Test
    public void testSuppressionFlottesJoueur() {
        GameMap map = new GameMap();
        Planet a = new Planet(1,0, 0, 1, 1, 1, 10);
        Planet b = new Planet(1, 1, 1, 1, 1, 2, 10);
        map.addFleet(new Fleet(1,a, b, 1, 5));
        map.addFleet(new Fleet(1,b, a, 2, 5));
        map.removeFleetsByPlayer(1);
        assertEquals(1, map.getFleets().size());
        assertEquals(2, map.getFleets().get(0).getOwnerId());
    }
}
