package project.view;

import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import project.network.macro.ClusterGroup;
import project.network.Group;
import project.network.Line;
import project.network.Network;
import project.network.Node;
import project.network.PowerPlant;
import project.network.macro.RandomMacro;
import project.network.SubStation;

/**
 * Classe chargée du formatage et de l'affichage des données du réseau.
 * @author Jimenez
 */
public class View {

    private StatusWindow statWin;
    private InputWindow inputWin;
    private Network net;

    /**
     * Créé l'IHM à partir du réseau
     * @param network le modèle de réseau
     */
    public View(Network network) {
        this.net = network;
        this.statWin = new StatusWindow(network);
        this.statWin.createDisplay();
        
        this.inputWin = new InputWindow(network);
        this.inputWin.createDisplay();
    }

    /**
     * Met à jour l'IHM
     */
    public void updateView() {
        statWin.updateDisplay();
        inputWin.updateDisplay();
    }

    /**
     * Ferme l'IHM
     */
    public void deleteView() {
        statWin.dispatchEvent(new WindowEvent(statWin, WindowEvent.WINDOW_CLOSING));
        inputWin.dispatchEvent(new WindowEvent(inputWin, WindowEvent.WINDOW_CLOSING) );
    }

    /**
    * Génère une chaîne représentant le réseau donné en paramètre.
    * @param network le réseau à représenter
    * @return une chaîne représentant le réseau.
    */
    public String rapport() {
        ArrayList<Node> unconnected = new ArrayList<>();
        String str = "Eléments connectés : \n";
        for (Node n : this.net.getNodes()) {
            if (n instanceof SubStation) {
                str += "" + n.getName() + "\n";
                for (Line l : ((SubStation) n).getLines()) {
                    str += "| <-- " + l.getIn().getName() + " " + l.getActivePower() + " kW\n";
                }
                for (Group g : ((SubStation)n).getGroups()){
                    str += "| --> " + g.getName() + " " + g.getConsumption()+" kW" + "\n";
                    str += "| --> " + g.getName() + " dans -1 tours " + g.getConsumptionWithOffset(-1) + " kW" + "\n";
                    str += "| --> " + g.getName() + " dans 0 tours " + g.getConsumptionWithOffset(0) + " kW" + "\n";
                    str += "| --> " + g.getName() + " dans 1 tours " + g.getConsumptionWithOffset(1) + " kW" + "\n";
                }
                str += "Total IN: " + ((SubStation) n).getPowerIn() + " | Total OUT: " + ((SubStation) n).getPowerOut()
                        + "\n";
            } else if ((n instanceof Group || n instanceof PowerPlant) && !n.isConnected()) {
                unconnected.add(n);
            }
        }
        if (unconnected.size() > 0) {
            str += "Eléments isolés : \n";
            for (Node n : unconnected) {
                str += n.getName() + "\n";
            }
        }
        str+="\n \n \n";


        return str;
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Network myNetwork = new Network();
        View myView = new View(myNetwork);
        // les flux sont supposés être libérés après usage afin d'éviter des fuites de ressources mais fermer System.in semble
        // une mauvaise habitude, donc on se contente de préciser que l'on sait ce qu'on fait
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);

        int i=0;
        while (true) {
            myNetwork.update();
            myView.updateView();
            if (i > 18){
                System.out.print("entrer un id de groupe (0 pour fermer) : ");
                int id = sc.nextInt();
                if (id == 0) {
                    myView.deleteView();
                    System.exit(0);
                }
                System.out.print("entrer une conso pour le groupe : ");
                int conso = sc.nextInt();

                for (Node n : myNetwork.getNodes()) {
                    if (n.getClass().equals(Group.class) && ((Group) n).getId() == id) {
                        ((Group) n).setOriginalconsumption(conso);
                    }
                }
            }
            myNetwork.handleErrors(myNetwork.analyze());
            myNetwork.predictFutureAndReact();
            i++;
        }
    }
}
