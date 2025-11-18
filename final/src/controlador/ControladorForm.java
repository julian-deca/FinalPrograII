package controlador;

/**
 *
 * @author Julian
 */

import java.time.LocalDate;
import modelo.gestion.GestorVehiculos;
import modelo.entidades.*;
import modelo.persistencia.PersistenciaCSV;
import modelo.persistencia.ExportadorTXT;
import modelo.excepciones.VehiculoNotFoundException;
import modelo.excepciones.DuplicateVehiculoException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.function.Predicate;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;

public class ControladorForm {
    
    @FXML private TextField txtPatente, txtPrecio, txtPuertas, txtCarga, txtEjes, txtCilindrada;
    @FXML private ComboBox<Marca> cmbMarca;
    @FXML private ComboBox<Color> cmbColor;
    @FXML private ComboBox<Condicion> cmbCondicion;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<Combustible> cmbCombustible;
    @FXML private CheckBox chkCaja, chkAcoplado, chkSidecar;
    @FXML private VBox panelAuto, panelCamion, panelMoto,panelDatos;
    @FXML private DatePicker datePickerFabricacion;
    
    
    private ControladorPrincipal controladorPrincipal;

    
    
    
    
    public void initialize() {
        
        // Inicializar comboboxes
        cmbMarca.setItems(FXCollections.observableArrayList(Marca.values()));
        cmbColor.setItems(FXCollections.observableArrayList(Color.values()));
        cmbCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbCombustible.setItems(FXCollections.observableArrayList(Combustible.values()));
        cmbTipo.setItems(FXCollections.observableArrayList("Auto", "Camión", "Moto"));
        
        // Configurar listener para cambio de tipo
        cmbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mostrarPanelEspecifico(newVal);
        });
        
    }
    
    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal){
        this.controladorPrincipal = controladorPrincipal;
    }
    
    private void mostrarPanelEspecifico(String tipo) {
        System.out.println(tipo);
        panelAuto.setVisible(false);
        panelCamion.setVisible(false);
        panelMoto.setVisible(false);
        panelDatos.setVisible(false);
        
        System.out.println(this.controladorPrincipal.gestor.listarTodos());
        if (tipo != null){
        panelDatos.setVisible(true);
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
            this.controladorPrincipal.gestor.agregar(vehiculo);
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
            this.controladorPrincipal.gestor.actualizar(vehiculo);
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
            this.controladorPrincipal.gestor.eliminar(patente);
            actualizarTabla();
            mostrarAlerta("Éxito", "Vehículo eliminado correctamente", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
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
    
    
    private void actualizarTabla() {
        this.controladorPrincipal.actualizarTabla();
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