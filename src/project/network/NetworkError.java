package project.network;

/**
 * Une classe regroupant des modèles d'erreurs susceptibles d'être générées à l'analyse du réseau.
 * @author Jimenez
 */
public class NetworkError {
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
