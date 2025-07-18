package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour charger une partie depuis un fichier texte.
 */
public class GameLoader {
    /**
     * Charge une partie à partir d'un fichier texte.
     * @param filename chemin vers le fichier
     * @return une instance de Game contenant la carte et les joueurs
     * @throws IOException en cas d'erreur de lecture
     */
    public static Game loadGameFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dims = br.readLine().split(" ");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            GameMap map = new GameMap();
            map.setHeight(height);
            map.setWidth(width);
            List<Player> players = new ArrayList<>();
            int maxPlayerId = 0;
            int planetId = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split(" ");
                if (tokens.length < 6) continue;
                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);
                double size = Double.parseDouble(tokens[2]);
                double richness = Double.parseDouble(tokens[3]);
                int ownerId = Integer.parseInt(tokens[4]);
                double ships = Double.parseDouble(tokens[5]);
                map.addPlanet(new Planet(planetId++, x, y, size, richness, ownerId, ships));
                if (ownerId > maxPlayerId) {
                    maxPlayerId = ownerId;
                }
            }

            for (int i = 1; i <= maxPlayerId; i++) {
                if (i == 1) {
                    players.add(new HumanPlayer(i));
                } else {
                    players.add(new DummyPlayer(i));
                }
            }

            return new Game(map, players);
        }
    }
}