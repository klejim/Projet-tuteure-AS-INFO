/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.fxview;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.network.*;

/**
 * FXML Controller class
 *
 * @author jimenezc
 */
public class FXMLController implements Initializable {
    private Network network;
    private Stage stage;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TextArea test;
    public String rapport() {
        ArrayList<Node> unconnected = new ArrayList<>();
        String str = "Eléments connectés : \n";
        for (Node n : this.network.getNodes()) {
            if (n instanceof SubStation) {
                str += "" + n.getName() + "\n";
                for (Line l : ((SubStation) n).getLines()) {
                    str += "| <-- " + l.getIn().getName() + " " + l.getActivePower() + " kW\n";
                }
                for (Group g : ((SubStation) n).getGroups()) {
                    str += "| --> " + g.getName() + " " + g.getConsumption() + " kW\n";
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
        return str;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     * @throws java.io.FileNotFoundException
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        
    }
    
    void setStage(Stage stge){
        stage = stge;
        stage.setOnCloseRequest((event)->{
            onClose(event);
        });
    }
    
    @FXML
    private void onClose(Event event){
        System.out.println("closing");
        // todo : avertissement et demande de sauvegarde
        Platform.exit();
    }

    @FXML
    private void onIter(ActionEvent event) {
        network.handleErrors(network.analyze());
        network.update();
	test.setText(rapport());
    }

    @FXML
    private void onCreate(ActionEvent event) {
        try {
            network = new Network(0, 0, 0);
            test.setText(rapport());
        }catch (FileNotFoundException e){
        }
        
            
    }

    
}
