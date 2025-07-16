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

                System.out.println("Flottes en transit :");
                for (Fleet f : game.getMap().getFleets()) {
                    System.out.printf("Joueur %d : %.1f vaisseaux de (%.1f, %.1f) vers (%.1f, %.1f) | Position actuelle : (%.2f, %.2f)\n",
                            f.getOwnerId(), f.getShips(),
                            f.getSource().getX(), f.getSource().getY(),
                            f.getTarget().getX(), f.getTarget().getY(),
                            f.getX(), f.getY());
                }


                game.nextTurn();
                game.eliminateDefeatedPlayers();
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