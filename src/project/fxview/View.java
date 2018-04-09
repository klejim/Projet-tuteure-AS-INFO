/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.fxview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jimenezc
 */
public class View extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLView.fxml"));//FXMLLoader.load(getClass().getResource("FXMLView.fxml"));
        Parent root = loader.load();
        // il est nécessaire de récupérer le contrôleur pour pouvoir l'initialiser avec le stage
        FXMLController controller = loader.getController();
        controller.setStage(stage);
        
        Scene scene = new Scene(root);
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
