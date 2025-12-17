package main;

import controlador.ControladorPrincipal;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Julian
 */
public class Main extends Application{

    /**
     * @param args the command line arguments
     */
        
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/vehiculos.fxml"));
        Parent root = loader.load();
        ControladorPrincipal controller = loader.getController(); 
        
        primaryStage.setTitle("Sistema de Gestión de Vehículos");
        primaryStage.setScene(new Scene(root, 1000, 600));
        
        primaryStage.setOnCloseRequest(event -> {
            controller.cerrarAplicacion(event, primaryStage);
        });
        
        primaryStage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
