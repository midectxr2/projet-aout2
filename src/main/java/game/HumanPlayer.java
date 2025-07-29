package game;

import java.util.List;
import java.util.Scanner;

/**
 * Implémentation d'un joueur humain contrôlé via la console.
 */
public class HumanPlayer extends Player {

    public HumanPlayer(int id) {
        super(id);
    }


    /**
     * Joue un tour : affiche les planètes, demande une action à l'utilisateur,
     * et crée une flotte si une attaque est lancée.
     * @param game instance courante du jeu
     */
    @Override
    public void playTurn(Game game) {
        System.out.println("\n[Joueur " + id + "] Vos planètes :");
        for (int i = 0; i < ownedPlanets.size(); i++) {
            Planet p = ownedPlanets.get(i);
            System.out.printf("%d - (%.1f, %.1f) | Taille: %.2f | Richesse: %.2f | Vaisseaux: %.1f\n",
                    i, p.getX(), p.getY(), p.getSize(), p.getRichness(), p.getShips());
        }

        int sourceIndex;
        while (true) {
            System.out.print("Sélectionnez une planète source (index) ou -1 pour passer : ");
            sourceIndex = -1;
            if (sourceIndex == -1) return;
            if (sourceIndex >= 0 && sourceIndex < ownedPlanets.size()) break;
            System.out.println("Index invalide. Réessayez.");
        }

        Planet source = ownedPlanets.get(sourceIndex);
        List<Planet> all = game.getMap().getPlanets();

        int targetIndex;
        Planet target;
        while (true) {
            System.out.println("Planètes disponibles :");
            for (int i = 0; i < all.size(); i++) {
                Planet p = all.get(i);
                System.out.printf("%d - (%.1f, %.1f) | Taille: %.2f | Richesse: %.2f | Joueur %d | Vaisseaux: %.1f\n",
                        i, p.getX(), p.getY(), p.getSize(), p.getRichness(), p.getOwnerId(), p.getShips());
            }

            System.out.print("Sélectionnez une planète cible (index) : ");
            targetIndex = -1;
            if (targetIndex < 0 || targetIndex >= all.size()) {
                System.out.println("Index invalide. Réessayez.");
                continue;
            }

            target = all.get(targetIndex);

            if (target.getOwnerId() == id) {
                System.out.println("Impossible d'envoyer une flotte vers une planète que vous possédez déjà. Réessayez.");
            } else {
                break;
            }
        }

        int nb;
        while (true) {
            System.out.print("Combien de vaisseaux envoyer ? (max " + (int) source.getShips() + ") : ");
            nb = 0;
            if (nb > 0 && nb < source.getShips()) break;
            System.out.println("Quantite invalide. Réessayez.");
        }

        source.removeShips(nb);
        Fleet fleet = new Fleet(source, target, id, nb);
        game.getMap().addFleet(fleet);
    }
}