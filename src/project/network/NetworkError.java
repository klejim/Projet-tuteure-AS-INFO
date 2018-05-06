package project.network;

import java.util.Comparator;

/**
 * Une classe regroupant des modèles d'erreurs susceptibles d'être générées à l'analyse du réseau.
 * @author Jimenez
 */
public class NetworkError {
    public static final Comparator<PowerError> powerComparator = (n1, n2)->{
        return n1.getPower() - n2.getPower();
    };
    public static final Comparator<NetworkError> typeAndPowerComparator = (n1, n2)->{
        int cmp = 0;
        // l'important est que les erreurs sur les plus petites puissances soient traitées d'abord
        // et que TooMuchPower passe avant NotEnoughPower (permet d'avoir plus de puissance à assigner)
        if (n1.getClass() == n2.getClass() && n1 instanceof PowerError){
            return NetworkError.powerComparator.compare((PowerError)n1, (PowerError)n2);
        }
        else if (n1.getClass() == n2.getClass()){
            return 0;
        }
        else if (n1 instanceof TooMuchPowerError){
            cmp = -1;
        }
        else if (n1 instanceof NodeNotConnectedError){
            cmp = 1;
        }
        else if (n1 instanceof NotEnoughPowerError && n2 instanceof NodeNotConnectedError){
            cmp = -1;
        }
        else if (n1 instanceof NotEnoughPowerError && n2 instanceof TooMuchPowerError){
            cmp = 1;
        }
        return cmp;
    };
    private boolean solved;
    private String message;
    NetworkError(){
        solved = false;
        message = "";
    }
    void setMessage(String str){
        message = str;
    }
    public String getMessage(){
        return message;
    }
    void setSolved(boolean s){
        solved = s;
    }
    public boolean isSolved() {
        return solved;
    }
}