package game;

import java.util.ArrayList;
import java.util.List;

/**
 * IA un peu plus avancée
 */
public class SmartPlayer extends Player {


    private int fleetId;

    public SmartPlayer(int id) {
        super(id);
    }

    //Alogrithme dans le rapport
    @Override
    public void playTurn(Game game) {
        List<Planet> owned = getOwnedPlanets();
        List<Planet> all = game.getMap().getPlanets();

        //Phase de défence
        defendPlanets(game, owned);


        //Phase d'attaque
        for (Planet source : owned) {

            //10 nombre arbitraire
            if (source.getShips() < 10) continue;

            Planet closest = null;
            double bestDist = Double.MAX_VALUE;


            //Planète la plus proche
            for (Planet candidate : all) {
                if (candidate.getOwnerId() != this.getId()) {
                    double d = distance(source, candidate);
                    if (d < bestDist) {
                        bestDist = d;
                        closest = candidate;
                    }
                }
            }

            if (closest != null) {
                int shipsToSend = (int) (source.getShips() * 0.5);
                source.removeShips(shipsToSend);
                Fleet fleet = new Fleet(fleetId ,source, closest, this.getId(), shipsToSend);
                fleetId = fleetId + 1;
                game.getMap().addFleet(fleet);
            }
        }

    }

    private double distance(Planet a, Planet b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * Retourne le nombre de vaisseaux ennemis se dirigeant vers la planète p.
     */
    private double incomingShips(Game game, Planet p){
        double sum = 0;
        List<Fleet> fleets = game.getMap().getFleets();
        for (Fleet f: fleets){
            if(f.getTarget() == p && f.getOwnerId() != this.id){
                sum = sum + f.getShips();
            }
        }
        return sum;
    }

    private void defendPlanets(Game game, List<Planet> myPlanets){
        for(Planet p: myPlanets){
            double incoming = incomingShips(game, p);
            if(incoming > 0 && incoming>p.getShips()){
                System.out.println("danger," + incoming + "vaisseaux arrivent");

                //vaisseaux qui arrivent en aide + marge de 2
                double need = incoming - p.getShips() + 2;
                System.out.println(need);

                //on ajoute dans une liste la liste des planètes qui peuvent aider
                List<Planet> potential = new ArrayList<>();
                for (Planet planet1: myPlanets){

                    //5 nombre arbitraire
                    if(planet1.getShips()>need + 5 && p.getId() != planet1.getId()){
                        potential.add(planet1);
                        System.out.println(planet1.getId());
                    }
                }

                //pas d'aide possible
                if(potential.isEmpty()){
                    continue;
                }
                //si possible on cherche la plus proche
                else{
                    double bestDist = Double.MAX_VALUE;
                    Planet choosen = null;
                    for(Planet planet2: potential){
                        double d = distance(planet2, p);
                        if (d < bestDist) {
                            bestDist = d;
                            choosen = planet2;
                        }
                    }

                    //apres avoir eu la plus proche, elle envoie une flotte
                    Fleet fleet = new Fleet(fleetId ,choosen, p, this.getId(), need + 2);
                    fleetId = fleetId + 1;
                    game.getMap().addFleet(fleet);
                }
            }
        }
    }







}
