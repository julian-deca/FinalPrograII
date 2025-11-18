package visual;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Julian
 */

import controlador.ControladorForm;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Form {
    private String route;
    private int height;
    private int width;
    private String title;
    
    
    
    public Form(String route,String title,int height,int width){
        this.route = route;
        this.title = title;
        this.height = height;
        this.width = width;
    }
    public void openNewForm(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(this.route));
        Parent root = loader.load();
        
       // ControladorForm controlador = loader.getController();
        Stage newStage = new Stage();
        newStage.setTitle(title);
        newStage.setScene(new Scene(root, this.height, this.width)); // Set scene with dimensions
        
        
        // Show the new stage
        newStage.show();
       // return controlador;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
