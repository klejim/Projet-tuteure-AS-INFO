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

    public static void main(String... args) {
        Network network = null;
        try {
            network = new Network();
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier config");
        }
        if (network != null) {
            System.out.println(Arrays.toString(
                    network.count(SubStation.class, Group.class, PowerPlant.class, Node.class, NuclearPlant.class)));
            View view = new View(network);
            System.out.println("=== Etat initial ===");
            System.out.print(view.rapport());
            for (int i = 0; i < 1000; i++) {
                network.update();
                network.handleErrors(network.analyze());
                System.out.println("=== Itération n°" + i + " ===");
                System.out.print(view.rapport());
                view.updateView();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }
}
