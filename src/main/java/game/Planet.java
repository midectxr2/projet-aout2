package game;
/**
 * Représente une planète possédant des caractéristiques (taille, richesse, position)
 * et un certain nombre de vaisseaux.
 * <p>
 * Une planète peut être neutre, propriétaire avec ownerId = 0 ou appartenir
 * à un joueur identifié par son ownerId.
 */
public class Planet {


    private int id;
    private double x,y ;
    private double size;
    private double richness;
    private int ownerId; // 0 = neutre
    private double ships;


    /**
     * Crée une planète avec ses propriétés.
     *
     * @param id       identifiant unique de la planète
     * @param x        coordonnée horizontale
     * @param y        coordonnée verticale
     * @param size     taille de la planète
     * @param richness richesse de la planète (impact sur croissance)
     * @param ownerId  identifiant du propriétaire (0 si neutre)
     * @param ships    nombre initial de vaisseaux
     */
    public Planet(int id, double x, double y, double size, double richness, int ownerId, double ships){
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = size;
        this.richness = richness;
        this.ownerId = ownerId;
        this.ships = ships;
    }

    /**
     * Met à jour la croissance ou la décroissance des vaisseaux sur la planète.
     *Si la planète est occupée par un joueur et en dessous de sa capacité maximale,
     *le nombre de vaisseaux augmente en fonction de sa richesse.
     *Si la planète dépasse sa capacité, les vaisseaux en excès sont supprimés progressivement (-1)
     */
    public void growShips(){
        if(ships<getCapacity() && ownerId != 0){
            ships = Math.min(getCapacity(), ships + richness);
        } else if (ships > getCapacity()) {
            ships = Math.max(getCapacity(), ships - 1);
        }
    }


    /**
     * Calcule la capacité maximale de vaisseaux de la planète.
     * @return capacité en nombre de vaisseaux
     */
    public double getCapacity(){
        return 20.0*size;
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getRichness() {
        return richness;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public double getShips() {
        return ships;
    }

    public int getId() {
        return id;
    }


    /**
     * Gère l'arrivée d'une flotte sur la planète et applique les règles de combat ou de renfort.
     * Si la flotte appartient au propriétaire actuel, elle renforce les défenses.
     * Si elle est ennemie, un combat a lieu et la planète peut changer de propriétaire.
     * En cas d'égalité parfaite, la planète devient neutre.
     *
     * @param attackerId    identifiant du joueur attaquant
     * @param incomingShips nombre de vaisseaux arrivant
     */
    public void receiveFleet(int attackerId, int incomingShips) {
        int currentShips = (int) Math.floor(this.ships);
        if (ownerId == attackerId) {
            this.ships += incomingShips;
        } else {
            int result = incomingShips - currentShips;
            if (result > 0) {
                this.ships = result;
                this.ownerId = attackerId;
            } else if (result < 0) {
                this.ships = -result;
                // ownerId reste inchangé
            } else {
                this.ships = 0;
                this.ownerId = 0; // devient neutre
            }
        }
    }

    /**
     * Retire un nombre donné de vaisseaux à la planète.
     * @param n le nombre de vaisseaux à retirer
     */
    public void removeShips(int n) {
        this.ships = Math.max(0, this.ships - n);
    }


    //surchage de la fonction pour lui permettre de prendre un objet fleet et pas planet uniquement
    public void receiveFleet(Fleet fleet) {
        receiveFleet(fleet.getOwnerId(), (int) fleet.getShips());
    }


    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Calcule la distance entre cette planète et une autre.
     *
     * @param planet autre planète
     * @return distance euclidienne
     */
    public double distanceTo(Planet planet){
        double dx = planet.x - this.x;
        double dy = planet.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }








}
