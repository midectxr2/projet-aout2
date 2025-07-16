package game;
/**
 * Représente un joueur (humain ou IA).
 */
public abstract class Player {
    protected int id;

    /**
     * Crée un joueur avec un identifiant.
     * @param id identifiant du joueur
     */
    public Player(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }


    /**
     * Définit la méthode de jeu du joueur pour un tour.
     * @param game l'état du jeu
     */
    public abstract void playTurn(Game game);
}
