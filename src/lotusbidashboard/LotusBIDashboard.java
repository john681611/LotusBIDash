/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jamie
 */
public class LotusBIDashboard extends Application {
    
    private DashboardController dashboard;
    private LoginController login;
            
    @Override
    public void start(Stage stage) throws Exception {
        dashboard = new DashboardController();
        login = new LoginController();
        
        Parent root = FXMLLoader.load(getClass().getResource("LoginFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Lotus Dashboard Login");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
