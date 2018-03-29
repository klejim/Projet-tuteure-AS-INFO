package project.view;

import java.util.ArrayList;
import project.network.Group;
import project.network.Line;
import project.network.Network;
import project.network.Node;
import project.network.PowerPlant;
import project.network.SubStation;
/**
 * Classe chargée du formatage et de l'affichage des données du réseau.
 * @author Jimenez
 */
public class View {
   /**
    * Génère une chaîne représentant le réseau donné en paramètre.
    * @param network le réseau à représenter
    * @return une chaîne représentant le réseau.
    */ 
    public String rapport(Network network){
        ArrayList<Node> unconnected = new ArrayList<>();
        String str = "Eléments connectés : \n";
        for (Node n : network.getNodes()){
            if (n instanceof SubStation){
                str += "" + n.getName() + "\n";
                for (Line l : ((SubStation)n).getLines()){
                    str += "| <-- " + l.getIn().getName()+ "  " + l.getPower()+ "kW \n";
                }
                for (Group g : ((SubStation)n).getGroups()){
                    str += "| --> " + g.getName() +" " + g.getConsumption()+" kW" + "\n";
                }
            
            str+= "TotalIN: "+ ((SubStation)n).getPowerIn() + "TotalOUT: "+ ((SubStation)n).getPowerOut()+ "\n";
            }
            else if ((n instanceof Group || n instanceof PowerPlant) && !n.isConnected()){
                unconnected.add(n);
            }
        }
        if (unconnected.size() > 0){
            str += "Eléments isolés : \n";
            for (Node n : unconnected){
                str += n.getName() + "\n";
            }    
        }
        return str;
    }
}
