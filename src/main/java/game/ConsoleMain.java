package game;

import java.io.IOException;

/**
 * Classe permettant de lancer une simulation console de la partie.
 */
public class ConsoleMain {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage : java game.ConsoleMain <fichier_carte.txt>");
            return;
        }

        try {
            String resourcePath = ConsoleMain.class.getClassLoader().getResource(args[0]).getPath();
            Game game = GameLoader.loadGameFromFile(resourcePath);
            while (!game.isGameOver()) {
                System.out.println("\n--- Tour " + game.getCurrentTurn() + " ---");
                for (Planet p : game.getMap().getPlanets()) {
                    System.out.printf("Planète (%.1f, %.1f) | Taille: %.2f | Richesse: %.2f | Joueur %d | Vaisseaux: %.1f\n",
                            p.getX(), p.getY(), p.getSize(), p.getRichness(), p.getOwnerId(), p.getShips());
                }
                game.nextTurn();
            }

            Player winner = game.getWinner();
            if (winner != null) {
                System.out.println("\nPartie terminée. Le joueur " + winner.getId() + " a gagné !");
            } else {
                System.out.println("\nPartie terminée. Aucun gagnant.");
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de fichier : " + e.getMessage());
        }
    }
}