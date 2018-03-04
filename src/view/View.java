package view;

import java.util.ArrayList;
import java.util.Arrays;
import network.Group;
import network.Line;
import network.Network;
import network.Node;
import network.PowerPlant;
import network.SubStation;
/**
 *
 * @author Jimenez
 */
public class View {
    
    public String rapport(Network network){
        ArrayList<Node> unconnected = new ArrayList<>();
        String str = "Eléments connectés : \n";
        for (Node n : network.getNodes()){
            if (n instanceof SubStation){
                str += "" + n.getName() + "\n";
                for (Line l : ((SubStation)n).getLines()){
                    str += "| <-- " + l.getIn().getName() + "\n";
                }
                for (Group g : ((SubStation)n).getGroups()){
                    str += "| --> " + g.getName() + "\n";
                }
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
