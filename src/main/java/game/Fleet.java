package game;

/**
 * Représente une flotte de vaisseaux en déplacement d'une planète source vers une planète cible.
 * Une flotte avance à chaque tour selon une vitesse donnée, et applique son effet en arrivant à destination.
 */
public class Fleet {
    private int Id;
    private Planet source;
    private Planet target;
    private int ownerId;
    private double ships;
    private double x, y;
    private double dx, dy;
    private double distanceRemaining;



    /**
     * Crée une flotte à partir d'une planète source et à destination d'une planète cible.
     * La flotte est initialisée à la position de la source et suit une trajectoire droite.
     *
     * @param source    la planète de départ
     * @param target    la planète cible
     * @param ownerId   l'identifiant du joueur propriétaire de la flotte
     * @param ships     le nombre de vaisseaux transportés
     */
    public Fleet(int Id, Planet source, Planet target, int ownerId, double ships) {
        this.Id = Id;
        this.source = source;
        this.target = target;
        this.ownerId = ownerId;
        this.ships = ships;
        this.x = source.getX();
        this.y = source.getY();

        double totalDistance = distance(source.getX(), source.getY(), target.getX(), target.getY());
        this.distanceRemaining = totalDistance;


        double dirX = target.getX() - source.getX();
        double dirY = target.getY() - source.getY();


        this.dx = dirX / totalDistance;
        this.dy = dirY / totalDistance;
    }

    /**
     * Fait avancer la flotte d'une certaine distance en direction de la cible.
     *
     * @param speed la distance à parcourir ce tour-ci
     */
    public void advance(double speed) {
        //éviter de dépasser
        double step = Math.min(speed, distanceRemaining);

        x += dx * step;
        y += dy * step;
        distanceRemaining -= step;
    }

    /**
     * Vérifie si la flotte est arrivée à destination.
     *
     * @return true si la flotte a atteint sa planète cible, false sinon
     */
    public boolean hasArrived() {
        return distanceRemaining <= 0;
    }


    /**
     * Applique les effets de la flotte à son arrivée sur la planète cible.
     */
    public void applyArrival() {
        target.receiveFleet(this);
    }



    //getters
    public double getX() { return x; }
    public double getY() { return y; }
    public Planet getSource() { return source; }
    public Planet getTarget() { return target; }
    public int getOwnerId() { return ownerId; }
    public double getShips() { return ships; }
    public int getId() {return Id;}



    /**
     * Calcule la distance euclidienne entre deux points (x1, y1) et (x2, y2).
     *
     * @param x1 abscisse du premier point
     * @param y1 ordonnée du premier point
     * @param x2 abscisse du second point
     * @param y2 ordonnée du second point
     * @return la distance entre les deux points
     */
    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}