package controlador;

/**
 *
 * @author Julian
 */

import java.io.IOException;
import java.time.LocalDate;
import modelo.gestion.GestorVehiculos;
import modelo.entidades.*;
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
    
    @FXML private TextField txtPatente, txtPrecio, txtPuertas, txtCarga, txtEjes, txtCilindrada;
    @FXML private ComboBox<Marca> cmbMarca, cmbFiltroMarca;
    @FXML private ComboBox<Color> cmbColor;
    @FXML private ComboBox<Condicion> cmbCondicion, cmbFiltroCondicion;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<Combustible> cmbCombustible;
    @FXML private CheckBox chkCaja, chkAcoplado, chkSidecar;
    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private VBox panelAuto, panelCamion, panelMoto;
    @FXML private DatePicker datePickerFabricacion;
    
    
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
    
    
    
    
    public GestorVehiculos gestor;
    
    public void initialize() {
        gestor = new GestorVehiculos();
        
        // Inicializar comboboxes
        cmbMarca.setItems(FXCollections.observableArrayList(Marca.values()));
        cmbColor.setItems(FXCollections.observableArrayList(Color.values()));
        cmbCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbFiltroCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbFiltroMarca.setItems(FXCollections.observableArrayList(Marca.values()));
        cmbCombustible.setItems(FXCollections.observableArrayList(Combustible.values()));
        cmbTipo.setItems(FXCollections.observableArrayList("Auto", "Camión", "Moto"));
        
        // Configurar listener para cambio de tipo
        cmbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mostrarPanelEspecifico(newVal);
        });
        
        
        
        
        patenteColumn.setCellValueFactory(new PropertyValueFactory<>("patente"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        fabricacionColumn.setCellValueFactory(new PropertyValueFactory<>("fabricacion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        condicionColumn.setCellValueFactory(new PropertyValueFactory<>("condicion"));

        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
        
        
        
        
        
        actualizarTabla();
    }
    
    @FXML
    private void openNewForm() {
        try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/vehiculosForm.fxml"));
             Parent root = loader.load();
                     System.out.println(root);

            ControladorForm controlador = loader.getController();
            controlador.setControladorPrincipal(this);
            Stage newStage = new Stage();
            newStage.setTitle("New Form");
            newStage.setScene(new Scene(root, 350, 500)); 

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
    
    private void mostrarPanelEspecifico(String tipo) {
        System.out.println(tipo);
        panelAuto.setVisible(false);
        panelCamion.setVisible(false);
        panelMoto.setVisible(false);
        
        System.out.println(gestor.listarTodos());
        if (tipo != null){
        switch (tipo) {
            case "Auto" -> panelAuto.setVisible(true);
            case "Camión" -> panelCamion.setVisible(true);
            case "Moto" -> panelMoto.setVisible(true);
        }}
    }
    
    @FXML
    private void agregarVehiculo(ActionEvent event) {
        try {
            Vehiculo vehiculo = crearVehiculoDesdeFormulario();
            gestor.agregar(vehiculo);
            limpiarFormulario();
            actualizarTabla();
            mostrarAlerta("Éxito", "Vehículo agregado correctamente", Alert.AlertType.INFORMATION);
        } catch (DuplicateVehiculoException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void actualizarVehiculo(ActionEvent event) {
        try {
            Vehiculo vehiculo = crearVehiculoDesdeFormulario();
            gestor.actualizar(vehiculo);
            actualizarTabla();
            mostrarAlerta("Éxito", "Vehículo actualizado correctamente", Alert.AlertType.INFORMATION);
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void eliminarVehiculo(ActionEvent event) {
        String patente = txtPatente.getText();
        if (patente.isEmpty()) {
            mostrarAlerta("Error", "Ingrese una patente para eliminar", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            gestor.eliminar(patente);
            actualizarTabla();
            mostrarAlerta("Éxito", "Vehículo eliminado correctamente", Alert.AlertType.INFORMATION);
            limpiarFormulario();
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
        cmbFiltroCondicion.setValue(null);
        cmbFiltroMarca.setValue(null);
        actualizarTabla();
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
    private void guardarCSV(ActionEvent event) {
        try {
            PersistenciaCSV persistencia = new PersistenciaCSV();
            persistencia.guardar(gestor.listarTodos(), "vehiculos.csv");
            mostrarAlerta("Éxito", "Datos guardados en vehiculos.csv", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void cargarCSV(ActionEvent event) {
        try {
            PersistenciaCSV persistencia = new PersistenciaCSV();
            List<Vehiculo> vehiculosCargados = persistencia.cargar("vehiculos.csv");
            // Limpiar y cargar nuevos datos
            gestor = new GestorVehiculos();
            for (Vehiculo v : vehiculosCargados) {
                gestor.agregar(v);
            }
            actualizarTabla();
            mostrarAlerta("Éxito", "Datos cargados desde vehiculos.csv", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private Vehiculo crearVehiculoDesdeFormulario() {
        String patente = txtPatente.getText();
        Marca marca = cmbMarca.getValue();
        Color color = cmbColor.getValue();
        LocalDate fabricacion = datePickerFabricacion.getValue();
        double precio = Double.parseDouble(txtPrecio.getText());
        Condicion condicion = cmbCondicion.getValue();
        String tipo = cmbTipo.getValue();
        
        switch (tipo) {
            case "Auto" -> {
                int puertas = Integer.parseInt(txtPuertas.getText());
                Combustible combustible = cmbCombustible.getValue();
                boolean aire = chkCaja.isSelected();
                return new Auto(patente, marca, fabricacion, precio,color, condicion, puertas, combustible, aire);
            }
                
            case "Camión" -> {
                double carga = Double.parseDouble(txtCarga.getText());
                int ejes = Integer.parseInt(txtEjes.getText());
                boolean refri = chkAcoplado.isSelected();
                return new Camion(patente, marca, fabricacion, precio,color, condicion, carga, ejes, refri);
            }
                
            case "Moto" -> {
                int cilindrada = Integer.parseInt(txtCilindrada.getText());
                boolean maletero = chkSidecar.isSelected();
                return new Moto(patente, marca, fabricacion, precio,color, condicion, cilindrada, maletero);
            }
                
            default -> throw new IllegalArgumentException("Tipo de vehículo no válido");
        }
    }
    
    public void actualizarTabla() {
        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
    }
    
    private void limpiarFormulario() {
        txtPatente.clear();
        datePickerFabricacion.setValue(null);
        txtPrecio.clear();
        txtPuertas.clear();
        txtCarga.clear();
        txtEjes.clear();
        txtCilindrada.clear();
        cmbMarca.setValue(null);
        cmbCondicion.setValue(null);
        cmbTipo.setValue(null);
        cmbCombustible.setValue(null);
        chkCaja.setSelected(false);
        chkAcoplado.setSelected(false);
        chkSidecar.setSelected(false);
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}