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
     * Teste le cas où une flotte du même joueur arrive sur une planète.
     * Vérifie que les vaisseaux sont ajoutés au total existant et que
     * le propriétaire de la planète ne change pas.
     */
    @Test
    public void testAjoutVaisseauxMemeJoueur() {
        p.receiveFleet(1, 5);
        assertEquals(15, (int) p.getShips());
        assertEquals(1, p.getOwnerId());
    }

    /**
     * Teste le cas où une flotte ennemie, plus nombreuse que la défense
     * restante, capture la planète. Vérifie que le nombre de vaisseaux
     * restants correspond à la différence et que le propriétaire change.
     */
    @Test
    public void testCaptureParAutreJoueur() {
        p.receiveFleet(2, 15);
        assertEquals(5, (int) p.getShips());
        assertEquals(2, p.getOwnerId());
    }

    /**
     * Teste le cas où une flotte ennemie moins nombreuse que la défense
     * échoue à capturer la planète. Vérifie que les forces de défense
     * restantes correspondent à la différence et que le propriétaire
     * reste inchangé.
     */
    @Test
    public void testDefaiteAttaquant() {
        p.receiveFleet(2, 5);
        assertEquals(5, (int) p.getShips());
        assertEquals(1, p.getOwnerId());
    }

    /**
     * Teste le cas où une flotte ennemie possède exactement autant
     * de vaisseaux que la défense. Vérifie que la planète devient neutre
     * et que le nombre de vaisseaux restants est nul.
     */

    @Test
    public void testNeutralisationParEgalite() {
        p.receiveFleet(2, 10);
        assertEquals(0, (int) p.getShips());
        assertEquals(0, p.getOwnerId());
    }

    /**
     * Vérifie qu'une flotte ne peut pas contenir plus de vaisseaux
     * que ceux présents sur la planète source au moment de son envoi.
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
        Planet target = new Planet(2,3, 4, 1, 1, 2, 5);
        Fleet f = new Fleet(1,source, target, 1, 5);
        f.advance(2);
        assertEquals(1.2, f.getX());
        assertEquals(1.6, f.getY());
    }

    /**
     * Vérifie qu'une flotte arrivée applique correctement ses effets sur la planète cible :
     * déplacement jusqu'à la planète, arrivée confirmée, application des effets (ajout ou conquête),
     * et vérification du nombre final de vaisseaux ainsi que du propriétaire.
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
