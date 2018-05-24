package project.network;

import java.util.ArrayList;

/**
 *
 * @author Jimenez
 */
public class TooMuchPowerError extends PowerError{
    TooMuchPowerError(SubStation s, int p){
        super(s, p);
    }
    /**
     * @see NetworkError#solve() 
     */
    @Override
    public void solve(){
        if (solvePowerOverflow(this.getStation(), this.getPower())) {
            this.setSolved(true);
            this.getStation().updateInput();
        } else {
            this.setMessage("Correction automatique impossible");
        }
    }
    
    /**
     * Tente de résoudre une surcharge au niveau d'une sous-station en libèrant de la puissance.
     * <p>On travaille sur les lignes transmettant le moins de puissance en premier afin de privilégier les centrales les plus souples 
     * (hydrauliques plutôt que nucléaires) et de libérer autant de centrales que possible.</p>
     * @param station la sous-station concernée par l'erreur.
     * @param overflow la puissance en trop.
     * @return {@code true} si l'erreur a été corrigée et {@code false} sinon.
     */
    private boolean solvePowerOverflow(SubStation station, int overflow) {
        boolean ok = false;
        ArrayList<Line> lines = new ArrayList<>();
        lines.addAll(station.getLines());
        lines.sort(Line.TypeAndPowerComparator);
        int i = 0, powerOverflow = overflow;
        while (i < lines.size() && !ok) {
            Line line = lines.get(i++);
            PowerPlant plant = line.getIn();
            int tmp = (powerOverflow > line.getActivePower()) ? line.getActivePower() : powerOverflow;
            powerOverflow -= station.giveBackPower(line, tmp);
            // on éteint ensuite la centrale si elle n'est plus utile
            if (plant.getActivePower() == plant.getPower()) {
                plant.stop();
            }
            ok = (powerOverflow == 0);
        }
        return ok;
    }
}
