package visual;
/**
 *
 * @author Julian
 */

import controlador.ControladorPrincipal;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vehiculos.fxml"));
        Parent root = loader.load();
        ControladorPrincipal controller = loader.getController();
        
        primaryStage.setTitle("Sistema de Gestión de Vehículos");
        primaryStage.setScene(new Scene(root, 1000, 600));
        
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            cerrarAplicacion(primaryStage, controller);
        });
        
        primaryStage.show();
    }
    
    private void cerrarAplicacion(Stage stage, ControladorPrincipal controller) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Guardar los cambios antes de salir?");

        ButtonType btnGuardar = new ButtonType("Guardar");
        ButtonType btnNoGuardar = new ButtonType("No Guardar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnGuardar, btnNoGuardar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == btnGuardar) {
                boolean guardadoExitoso = controller.serializar();
                if (guardadoExitoso) {
                    stage.close();
                }
            } else if (result.get() == btnNoGuardar) {
                stage.close();
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}