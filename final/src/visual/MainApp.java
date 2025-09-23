package visual;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Auto;
import modelo.Color;
import modelo.Combustible;
import modelo.Condicion;
import modelo.Marca;

/**
 *
 * @author Julian
 */
public class MainApp extends Application {
    
    public static void main(String[] args) {
        //
        //System.out.println(coche.toString());
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Scene escena = new Scene(root, 400, 500);
        Auto coche = new Auto("pas354", Marca.CHEVROLET, LocalDate.now(), 100.00, Color.ROJO, Condicion.NUEVO, 4, Combustible.NAFTA, false);
        Label etiqueta = new Label(coche.toString());
        root.getChildren().add(etiqueta);
        stage.setScene(escena);
        stage.show();
    }
    
}
