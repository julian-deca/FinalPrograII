package controlador;

/**
 *
 * @author Julian
 */

import config.FilePaths;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import modelo.gestion.GestorVehiculos;
import modelo.entidades.*;
import modelo.persistencia.PathFinder;
import modelo.persistencia.PersistenciaCSV;
import modelo.persistencia.ExportadorTXT;
import modelo.excepciones.VehiculoNotFoundException;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.persistencia.Persistencia;
import modelo.persistencia.PersistenciaJSON;
import modelo.persistencia.Serializador;
   

public class ControladorPrincipal {
    
    @FXML private ComboBox<Marca> cmbFiltroMarca;
    @FXML private ComboBox<Condicion>  cmbFiltroCondicion;
    @FXML private TableView<Vehiculo> tablaVehiculos;

    
    
    @FXML
    private TableColumn<Vehiculo, String> patenteColumn;
    @FXML
    private TableColumn<Vehiculo, String> tipoColumn;
    @FXML
    private TableColumn<Vehiculo, String> marcaColumn;
    @FXML
    private TableColumn<Vehiculo, String> colorColumn;
    @FXML
    private TableColumn<Vehiculo, LocalDate> fabricacionColumn;
    @FXML
    private TableColumn<Vehiculo, Double> precioColumn;
    @FXML
    private TableColumn<Vehiculo, String> condicionColumn;
    
    
    @FXML private TableView.TableViewSelectionModel<Vehiculo> seleccionTabla;

    
    public GestorVehiculos gestor;
    public Vehiculo vehiculoSeleccionado;
    
    private Marca filtroMarca;
    private Condicion filtroCondicion;

    
    public void initialize() {
        gestor = new GestorVehiculos();
        
     
        cmbFiltroCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbFiltroMarca.setItems(FXCollections.observableArrayList(Marca.values()));
        patenteColumn.setCellValueFactory(new PropertyValueFactory<>("patente"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        fabricacionColumn.setCellValueFactory(new PropertyValueFactory<>("fabricacion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        condicionColumn.setCellValueFactory(new PropertyValueFactory<>("condicion"));

        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
        seleccionTabla = tablaVehiculos.getSelectionModel();
        
        deserializar();
    }
    
    private void setFiltroMarca(Marca filtro){
        filtroMarca = filtro;
    }
    private Marca getFiltroMarca(){
        return filtroMarca;
    }
    
    private void setFiltroCondicion(Condicion filtro){
        filtroCondicion = filtro;
    }
    private Condicion getFiltroCondicion(){
        return filtroCondicion;
    }
    
    
    @FXML
    private void openNewForm(Boolean editar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/vehiculosForm.fxml"));
            Parent root = loader.load();

            ControladorForm controlador = loader.getController();
            controlador.setControladorPrincipal(this);
            controlador.setEditar(editar);
            Stage newStage = new Stage();
            newStage.setTitle(editar? "Editar Vehiculo" :"Crear Vehiculo");
            newStage.setScene(new Scene(root, 350, 450)); 

            newStage.show();
            
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al abrir el formulario", Alert.AlertType.ERROR);
            System.out.println("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void openNewForm(){
        this.openNewForm(false);
    }
    
   
    @FXML
    private void agregarVehiculo(ActionEvent event) {
            this.openNewForm(); 
    }
    
    @FXML
    private void actualizarVehiculo(ActionEvent event) {
        try {
            vehiculoSeleccionado = seleccionTabla.getSelectedItem();
            if(vehiculoSeleccionado == null){
                mostrarAlerta("Atencion", "Seleccione un vehiculo para actualizar", Alert.AlertType.ERROR);
                return;
            }
            
            System.out.println(vehiculoSeleccionado.getTipo());
            this.openNewForm(true);
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ha ocurrido un error al intentar modificar el vehiculo", Alert.AlertType.ERROR);
            System.out.println("Error al actualizar: " + e.getMessage());

        }
    }
    
    @FXML
    private void eliminarVehiculo(ActionEvent event) {

        try{
            vehiculoSeleccionado = seleccionTabla.getSelectedItem();
            if (vehiculoSeleccionado == null) {
                mostrarAlerta("Atencion", "Selceccione un vehiculo para eliminar", Alert.AlertType.ERROR);
                return;
            }
            String patente = vehiculoSeleccionado.getPatente();
            gestor.eliminar(patente);
            actualizarTabla();
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        catch (Exception e) {
            mostrarAlerta("Error", "Ha ocurrido un error al intentar eliminar el vehiculo", Alert.AlertType.ERROR);
            System.out.println("Error al eliminar: " + e.getMessage());
        }
            
    }
    
    @FXML
    private void ordenarPorPatente(ActionEvent event) {
        gestor.ordenarNatural();
        actualizarTabla();
    }
    
    @FXML
    private void ordenarPorPrecio(ActionEvent event) {
        gestor.ordenarPorPrecio();
        actualizarTabla();
    }
    
    @FXML
    private void ordenarPorFabricacion(ActionEvent event) {
        gestor.ordenarPorFabricacion();
        actualizarTabla();
    }
    
    @FXML
    private void setFiltros(){
        setFiltroMarca(cmbFiltroMarca.getValue());
        setFiltroCondicion(cmbFiltroCondicion.getValue());
        actualizarTabla();
    }
    
    
    private List<Vehiculo> getVehiculosFiltrados() {
        List<Vehiculo> vehiculosFiltrados;
        
        vehiculosFiltrados = gestor.filtrarPorCondicion(gestor.listarTodos(),getFiltroCondicion());
        vehiculosFiltrados = gestor.filtrarPorMarca(vehiculosFiltrados, getFiltroMarca());
        
        return vehiculosFiltrados;
    }
    
    @FXML
    private void limpiarFiltro(ActionEvent event) {
        resetComboBox(cmbFiltroCondicion);
        resetComboBox(cmbFiltroMarca);
        setFiltroMarca(null);
        setFiltroCondicion(null);

        actualizarTabla();
    }
    
    private <T> void resetComboBox(ComboBox<T> combo) {
        combo.setValue(null);
        combo.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
    }
    
    
    @FXML
    private void aplicarDescuento(ActionEvent event) {
        gestor.aplicarDescuento(10);
        actualizarTabla();
        mostrarAlerta("Éxito", "Descuento del 10% aplicado a todos los vehículos", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void realizarMantenimiento(){
       try{
        for(Vehiculo v : gestor){
            v.realizarMantenimiento();
        }
        mostrarAlerta("Éxito", "Mantenimiento realizado con éxito", Alert.AlertType.INFORMATION);

       }
       catch(Exception e){
            mostrarAlerta("Error", "Error al realizar mantenimiento", Alert.AlertType.ERROR);
            System.out.println("Error al realizar mantenimiento: " + e.getMessage());
       }
       
    }
    
    @FXML
    private void exportarTXT(ActionEvent event) {
        try {
            PathFinder pathFinder = new PathFinder();
            File file = pathFinder.obetenerPathTXT(event);
            ExportadorTXT exportador = new ExportadorTXT();
            
            List<Vehiculo> listaFiltrada = getVehiculosFiltrados();
            exportador.exportarFiltrado(listaFiltrada, getFiltroMarca(),getFiltroCondicion(), file.getAbsolutePath());
            mostrarAlerta("Éxito", "Datos exportados a " + file.getName(), Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al exportar TXT", Alert.AlertType.ERROR);
            System.out.println("Error al exportar TXT: " + e.getMessage());

        }
    }
    
    @FXML
    private void importar(ActionEvent event){
        try {
                PathFinder pathFinder = new PathFinder();
                File file = pathFinder.obtenerPathCargar(event);
                if(file != null){
                    String extension = PathFinder.getExtension(file);
                    if(extension != null && !extension.isEmpty()){
                        
                       
                        switch (extension) {
                        case "json" -> {
                            PersistenciaJSON persistencia = new PersistenciaJSON();
                            cargarArchivo(file, persistencia);
                            }
                        case "csv" ->{
                            PersistenciaCSV persistencia = new PersistenciaCSV();
                            cargarArchivo(file, persistencia);
                            }
                        default -> mostrarAlerta("Error", "Formato de Archivo Invalido", Alert.AlertType.ERROR);
                        }
                    }
                }
                   
                }
           
           
         catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar", Alert.AlertType.ERROR);
            System.out.println("Error al importar: " + e.getMessage());

        }
    }
    
    @FXML
    private void exportar(ActionEvent event){
        try {
                PathFinder pathFinder = new PathFinder();
                File file = pathFinder.obtenerPathGuardar(event);
                if(file != null){
                    String extension = PathFinder.getExtension(file);
                    if(extension != null && !extension.isEmpty()){
                        
                        switch (extension) {
                        case "json" -> {
                            PersistenciaJSON persistencia = new PersistenciaJSON();
                            guardarArchivo(file,persistencia);
                            }
                        case "csv" ->{
                            PersistenciaCSV persistencia = new PersistenciaCSV();
                            guardarArchivo(file,persistencia);
                            }
                        default -> mostrarAlerta("Error", "Formato de Archivo Invalido", Alert.AlertType.ERROR);
                        }
                    }
                        
                }
                   
                }
           
           
         catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar", Alert.AlertType.ERROR);
            System.out.println("Error al exportar: " + e.getMessage());

        }
    }
    
    private void guardarArchivo(File file, Persistencia persistencia){
        persistencia.guardar(gestor.listarTodos(), file.getAbsolutePath());
        mostrarAlerta("Éxito", "Datos guardados en " + file.getName(), Alert.AlertType.INFORMATION);
           
    }
    
    private void cargarArchivo(File file, Persistencia persistencia){
        try {
                List<Vehiculo> vehiculosCargados = persistencia.cargar(file.getAbsolutePath());
                gestor = new GestorVehiculos();
                for (Vehiculo v : vehiculosCargados) {
                    gestor.agregar(v);
                                }
                actualizarTabla();
                mostrarAlerta("Éxito", "Datos cargados desde " + file.getName(), Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar", Alert.AlertType.ERROR);
            System.out.println("Error al cargar archivo: " + e.getMessage());

        }
    }
    
    @FXML
    public boolean serializar() {
        try {
            Serializador persistencia = new Serializador();
            persistencia.guardar(gestor.listarTodos(), FilePaths.getPathBinarioString());
            mostrarAlerta("Éxito", "Datos guardados en " + FilePaths.FILE_DAT, Alert.AlertType.INFORMATION);
            return true;
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar los datos", Alert.AlertType.ERROR);
            System.out.println("Error al serializar: " + e.getMessage());

            return false;
        }
    }

    @FXML
    private void deserializar() {
        try {
            String path = FilePaths.getPathBinarioString();
            File archivo = new File(path);
            
            if(!archivo.exists()){
                return;
            }
            
            Serializador persistencia = new Serializador();
            
            List<Vehiculo> vehiculosCargados = persistencia.cargar(path);
            gestor = new GestorVehiculos();
            for (Vehiculo v : vehiculosCargados) {
                gestor.agregar(v);
            }
            actualizarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar los datos", Alert.AlertType.ERROR);
            System.out.println("Error al deserializar: s" + e.getMessage());

        }
    }
    
    public void actualizarTabla() {
        List<Vehiculo> listaActualizada = getVehiculosFiltrados();
        
        tablaVehiculos.setItems(FXCollections.observableArrayList(listaActualizada));
        tablaVehiculos.refresh();
    }
    
    public void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public void cerrarAplicacion(Event event, Stage stage) {
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
                serializar(); 
                stage.close();
            } else if (result.get() == btnNoGuardar) {
                stage.close();
            } else {
                event.consume(); 
            }
        }
    }
    
}