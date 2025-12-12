package controlador;

/**
 *
 * @author Julian
 */

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import modelo.gestion.GestorVehiculos;
import modelo.entidades.*;
import modelo.persistencia.PathFinder;
import modelo.persistencia.PersistenciaCSV;
import modelo.persistencia.ExportadorTXT;
import modelo.excepciones.VehiculoNotFoundException;
import modelo.excepciones.DuplicateVehiculoException;
import visual.Form;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.function.Predicate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
   

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

    
    public void initialize() {
        gestor = new GestorVehiculos();
        
        // Inicializar comboboxes
     
        cmbFiltroCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbFiltroMarca.setItems(FXCollections.observableArrayList(Marca.values()));
      
        // Configurar listener para cambio de tipo
      
        
        
        
        
        patenteColumn.setCellValueFactory(new PropertyValueFactory<>("patente"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        fabricacionColumn.setCellValueFactory(new PropertyValueFactory<>("fabricacion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        condicionColumn.setCellValueFactory(new PropertyValueFactory<>("condicion"));

        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
        
        seleccionTabla = tablaVehiculos.getSelectionModel();
        
        
        
        actualizarTabla();
    }
    
    @FXML
    private void openNewForm(Boolean editar) {
        try {
            
            
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/vehiculosForm.fxml"));
             Parent root = loader.load();
                     System.out.println(root);

            ControladorForm controlador = loader.getController();
            controlador.setControladorPrincipal(this);
            controlador.setEditar(editar);
            Stage newStage = new Stage();
            newStage.setTitle("New Form");
            newStage.setScene(new Scene(root, 350, 600)); 

            newStage.show();
            
            
           // Form formVehiculos = new Form("/visual/vehiculosForm.fxml","Formulario",400,300);
            
           // formVehiculos.openNewForm();
            /*
            System.out.println(getClass().getResource("/visual/vehiculosForm.fxml"));
            // Load the FXML for the new form
            Parent root = FXMLLoader.load(getClass().getResource("/visual/vehiculosForm.fxml"));

            // Create a new Stage (window)
            Stage newStage = new Stage();
            newStage.setTitle("New Form");
            newStage.setScene(new Scene(root, 400, 300)); // Set scene with dimensions

            // Show the new stage
            newStage.show();

            // Optional: Hide the current window if desired
            // ((Node)(event.getSource())).getScene().getWindow().hide();
*/

        } catch (IOException e) {
            System.out.println(e);
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void openNewForm(){
    this.openNewForm(false);
    }
    
   
    @FXML
    private void agregarVehiculo(ActionEvent event) {
        try {
            this.openNewForm();
        } catch (DuplicateVehiculoException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void actualizarVehiculo(ActionEvent event) {
        try {
            vehiculoSeleccionado = seleccionTabla.getSelectedItem();
            System.out.println(vehiculoSeleccionado.getTipo());
            this.openNewForm(true);
           // Vehiculo vehiculo = crearVehiculoDesdeFormulario();
           // gestor.actualizar(vehiculo);
            //actualizarTabla();
            //mostrarAlerta("Éxito", "Vehículo actualizado correctamente", Alert.AlertType.INFORMATION);
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void eliminarVehiculo(ActionEvent event) {

            try{
                vehiculoSeleccionado = seleccionTabla.getSelectedItem();
                if (vehiculoSeleccionado == null) {
                    mostrarAlerta("Error", "Selceccione un vehiculo para eliminar", Alert.AlertType.ERROR);
                    return;
                }
                String patente = vehiculoSeleccionado.getPatente();
                gestor.eliminar(patente);
                actualizarTabla();
                mostrarAlerta("Éxito", "Vehículo eliminado correctamente", Alert.AlertType.INFORMATION);
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
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
    private void aplicarFiltro(ActionEvent event) {
        Predicate<Vehiculo> filtro = v -> true;
        
        if (cmbFiltroCondicion.getValue() != null) {
            filtro = filtro.and(v -> v.getCondicion() == cmbFiltroCondicion.getValue());
            System.out.println(cmbFiltroCondicion.getValue());
        }
        
        if (cmbFiltroMarca.getValue() != null) {
            filtro = filtro.and(v -> v.getMarca() == cmbFiltroMarca.getValue());
        }
        
        List<Vehiculo> vehiculosFiltrados = gestor.listarTodos().stream()
                                                .filter(filtro)
                                                .collect(java.util.stream.Collectors.toList());
        
        tablaVehiculos.setItems(FXCollections.observableArrayList(vehiculosFiltrados));
    }
    
    @FXML
    private void limpiarFiltro(ActionEvent event) {
        resetComboBox(cmbFiltroCondicion);
        resetComboBox(cmbFiltroMarca);
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
    private void exportarTXT(ActionEvent event) {
        try {
            ExportadorTXT exportador = new ExportadorTXT();
            Predicate<Vehiculo> filtro = v -> v.getPrecio() > 10000; // Ejemplo: vehículos caros
            exportador.exportarFiltrado(gestor.listarTodos(), filtro, "vehiculos_filtrados.txt");
            mostrarAlerta("Éxito", "Datos exportados a vehiculos_filtrados.txt", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al exportar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void guardar(ActionEvent event){
        try {
                PathFinder pathFinder = new PathFinder();
                File file = pathFinder.obtenerPathGuardar(event, "hola");
                if(file != null){
                    String extension = PathFinder.getExtension(file);
                    if(extension != null && !extension.isEmpty()){
                        
                        switch (extension) {
                        case "json" -> guardarCSV(file);
                        case "csv" ->guardarCSV(file);
                        default -> mostrarAlerta("Error", "Formato de Archivo Invalido", Alert.AlertType.ERROR);
                        }
                    }
                        
                }
                   
                }
           
           
         catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void guardarCSV(File file) {
        try {
                PersistenciaCSV persistencia = new PersistenciaCSV();
                persistencia.guardar(gestor.listarTodos(), file.getAbsolutePath());
                mostrarAlerta("Éxito", "Datos guardados en " + file.getName(), Alert.AlertType.INFORMATION);
           
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void cargarCSV(ActionEvent event) {
        try {
            PathFinder pathFinder = new PathFinder();
            File file = pathFinder.obtenerPathCargar(event, "hola");
            if(file != null){
                PersistenciaCSV persistencia = new PersistenciaCSV();
                List<Vehiculo> vehiculosCargados = persistencia.cargar(file.getAbsolutePath());
                gestor = new GestorVehiculos();
                for (Vehiculo v : vehiculosCargados) {
                    gestor.agregar(v);
                }
                actualizarTabla();
                mostrarAlerta("Éxito", "Datos cargados desde vehiculos.csv", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    

    
    public void actualizarTabla() {
        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
        tablaVehiculos.refresh();
    }
    
  
    
    public void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}