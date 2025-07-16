package game;
/**
 * Représente une flotte de vaisseaux en déplacement entre deux planètes.
 */
public class Fleet {
    private Planet source;
    private Planet destination;
    private double remainingDistance;
    private int ownerId;
    private int shipCount;


    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Crée une nouvelle flotte.
     * @param source planète de départ
     * @param destination planète cible
     * @param ownerId identifiant du joueur propriétaire
     * @param shipCount nombre de vaisseaux
     */
    public Fleet(Planet source, Planet destination, int ownerId, int shipCount) {
        this.source = source;
        this.destination = destination;
        this.ownerId = ownerId;
        this.shipCount = shipCount;
        this.remainingDistance = calculateDistance();
    }


    /**
     * Calcule la distance entre les deux planètes.
     * @return distance entre source et destination
     */
    private double calculateDistance() {
        double dx = destination.getX() - source.getX();
        double dy = destination.getY() - source.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * Fait avancer la flotte d'une certaine distance.
     * @param speed distance à parcourir ce tour
     */
    public void advance(double speed) {
        remainingDistance -= speed;
    }


    /**
     * Indique si la flotte est arrivée à destination.
     * @return vrai si distance restante <= 0
     */
    public boolean hasArrived() {
        return remainingDistance <= 0;
    }


    /**
     * Applique l'effet de l'arrivée de la flotte sur la planète cible.
     */
    public void applyArrival() {
        destination.receiveFleet(ownerId, shipCount);
    }

    // Getters si besoin
}
