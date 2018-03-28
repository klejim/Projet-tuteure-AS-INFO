/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import project.network.Network;

/**
 * FXML Controller class
 *
 * @author jimenezc
 */
public class FXMLController implements Initializable {
    private Network network;
    @FXML
    private Button button;
    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonAction12(ActionEvent event) {
        try{
            network = new Network(1,2,3);
            label.setText("Réseau créé");
        }
        catch(IOException e){
            label.setText(e.getMessage());
        }
    }
    
}
