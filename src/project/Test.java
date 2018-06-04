package project;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import project.network.base.Group;
import project.network.base.Network;
import project.network.base.Node;
import project.network.base.NuclearPlant;
import project.network.base.PowerPlant;
import project.network.base.SubStation;
import project.view.View;

/**
 * @author Jimenez
 */
public class Test {
    public static final boolean JUMP_TO_BALANCED = false;
    public static final int DELAY = 1000; // en ms
    public static HashMap<SubStation, Integer> testMap;
    public static int finalIter;
    static{
        testMap = new HashMap<>();
    }
    public static void initTest(Network network){
        for (SubStation station : network.getSubStations()){
            testMap.put(station, 0);
        }
    }
    
    public static void updateTest(Network network){
        for (SubStation station : network.getSubStations()){
            if (station.getDiff() > 0){
                Integer val = testMap.get(station);
                testMap.put(station, val + 1);
            }
        }
    }
    
    public static void printResult(Network network, int nbIter) throws IOException{
        List<String> lines = new ArrayList<>();
        lines.add("***Pourcentages de réussite***");
        for (SubStation station : testMap.keySet()){
            double perc = (double)testMap.get(station) / nbIter;
            lines.add(station.getName() + " : " + perc);
        }
        lines.add("\n");
        Path file = Paths.get("results.txt");
        Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    }
    
    public static void main(String... args) {
        Network network = null;
        try {
            network = new Network();
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier config");
        }
        if (network != null) {
            Test.initTest(network);
            System.out.println(Arrays.toString(
                    network.count(SubStation.class, Group.class, PowerPlant.class, Node.class, NuclearPlant.class)));
            View view = new View(network);
            System.out.println("=== Etat initial ===");
            System.out.print(view.rapport());
            int i;
            for (i = 0; i < 1000; i++) {
                Test.finalIter = i;
                network.update();
                Test.updateTest(network);
                view.updateView();
                network.handleErrors(network.analyze());
                System.out.println("=== Itération n°" + i + " ===");
                System.out.print(view.rapport());
                if (i < 20 && Test.JUMP_TO_BALANCED){}
                else {
                    try {
                        Thread.sleep(Test.DELAY);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                network.predictFutureAndReact();
            }
            
        }
    }
}
