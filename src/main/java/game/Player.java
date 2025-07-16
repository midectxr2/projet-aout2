package game;

public abstract class Player {
    protected int id;

    public Player(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public abstract void playTurn(Game game);
}
