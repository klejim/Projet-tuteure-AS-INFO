package project.network;

import java.util.ArrayList;

/**
 *
 * @author Jimenez
 */
public class NotEnoughPowerError extends PowerError{
    NotEnoughPowerError(SubStation s, int p){
        super(s, p);
    }
    /**
     * @see NetworkError#solve()
     */
    @Override
    public void solve(){
        if (solvePowerShortage(this.getStation(), this.getPower())){
            this.setSolved(true);
            this.getStation().updateInput();
        }
        else{
            this.setMessage("Correction automatique impossible");
        }
    }
    
    /**
     * Tente de résoudre un manque de puissance au niveau d'une sous-station.
     * <p>On effectue la recherche sur les centrales liées à la station et ordonnée par état (ON &lt; STARTING &lt; OFF) et par puissance
     * disponible croissante.</p>
     * <p>Ordre de recherche de solutions:</p>
     * <ul>
     * <li>on regarde si les centrales allumées ont encore de la puissance de disponible</li>
     * <li>si ce n'est pas le cas on voit s'il existe une centrale en démarrage qui pourrait aider</li>
     * <li>s'il n'y en a pas on essaie d'allumer une centrale</li>
     * <li>si c'est impossible on panique et on retourne false</li>
     * </ul>
     * <p>L'ordre de recherche est assuré par le tri précédent. On emprunte en priorité aux centrales ayant le moins de puissance disponible.
     *  Si une centrale ne peut pas combler toute l'erreur on prend ce qu'elle propose.</p>
     * @param station la sous-station concernée par l'erreur.
     * @param p la puissance qu'il manque pour alimenter correctement la sous-station.
     * @return {@code true} si l'erreur a pu être corrigée et {@code false} sinon.
     */
    private boolean solvePowerShortage(SubStation station, int p) {
        boolean ok = false;
        ArrayList<PowerPlant> plants = new ArrayList<>();
        for (Line line : station.getLines()) {
            plants.add(line.getIn());
        }
        // avant de poursuivre on trie les centrales
        // ordre : ON < STARTING < OFF et à catégorie égale la centrale ayant la plus grande puissance disponible est placée avant
        plants.sort(PowerPlant.stateAndPowerComparator);
        // Recherche de solution
        int i = 0, powerNeeded = p;
        while (i < plants.size() && !ok) {
            PowerPlant plant = plants.get(i++);
            if (Math.abs(plant.getActivePower()) > 0) {
                if (plant.getState() == PowerPlant.State.OFF) {
                    plant.start();
                }
                int powerAsked = (powerNeeded <= Math.abs(plant.getActivePower()))?powerNeeded:Math.abs(plant.getActivePower());
                powerNeeded -= plant.grantToStation(station, powerAsked);
                ok = powerNeeded <= 0;
            }
        }
        return ok;
    }
}
