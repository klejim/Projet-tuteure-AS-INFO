package project;

import java.io.IOException;
import java.util.Arrays;
import project.network.Group;
import project.network.Network;
import project.network.Node;
import project.network.NuclearPlant;
import project.network.PowerPlant;
import project.network.SubStation;
import project.view.View;
/**
 * @author Jimenez
 */
public class Test {
    public static void main(String... args){
        Network network = null;
        try{
            network = new Network(1, 2, 10);
        }
        catch (IOException e){
            System.out.println("Erreur de lecture du fichier config");
        }
        if (network != null){
            System.out.println(Arrays.toString(network.count(SubStation.class, Group.class, PowerPlant.class, Node.class, NuclearPlant.class)));
            View view = new View();
            System.out.print(view.rapport(network));
        }
        
    }
}
