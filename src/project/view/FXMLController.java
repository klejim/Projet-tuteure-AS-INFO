/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.view;

import static java.awt.SystemColor.window;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.network.Network;

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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    
}
