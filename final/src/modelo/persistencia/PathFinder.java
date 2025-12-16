/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

import config.FilePaths;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Julian
 */
public class PathFinder {
    
       
       private File obtenerPath(ActionEvent event,Boolean guardar, Boolean esTXT){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(guardar ? "Guardar archivo" : "Abrir archivo");
        
        
        if(esTXT){
            fileChooser.getExtensionFilters().add(
               new FileChooser.ExtensionFilter("Archivo TXT (*.txt)", "*.txt")
            );      
        }
        else{
            fileChooser.getExtensionFilters().add(
               new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv")
            );
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos JSON (*.json)", "*.json")
            );
        }
       
         
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        File file;
        
        File initDir = new File(FilePaths.BASE);
        if (initDir.exists() && initDir.isDirectory()) {
            fileChooser.setInitialDirectory(initDir);
        }
        if(guardar){
            fileChooser.setInitialFileName("vehiculos");
            file = fileChooser.showSaveDialog(stage);
        }
        else{
            file = fileChooser.showOpenDialog(stage); 
        }
        return file;
       }
      public File obtenerPathGuardar(ActionEvent event){
        File file = obtenerPath(event,true);
        return file;
    }
      
     public File obtenerPathCargar(ActionEvent event){
        File file = obtenerPath(event,false);
        return file;
    }
     
    public File obetenerPathTXT(ActionEvent event){
        return obtenerPath(event,true,true);
    }
    
    private File obtenerPath(ActionEvent event,Boolean guardar){
        return obtenerPath(event, guardar, false);
    }
      
    public static String getExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
        }
    
}
