package project;

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
    	int iteration; //compteur des itérations.
        final int MAX_ITE=10; //Itération maximale (sisi)
        
        
    	Network network = new Network(1, 2, 10);
        System.out.println(Arrays.toString(network.count(SubStation.class, Group.class, PowerPlant.class, Node.class, NuclearPlant.class)));
        View view = new View();
        
        for (iteration=0;iteration<MAX_ITE;iteration++){
        	
        	System.out.print(view.rapport(network,iteration));
        	network.run(iteration);
        	System.out.println();
            System.out.println();
            System.out.println();
        	
        }
        
        
        
       
        
        
    }
}
