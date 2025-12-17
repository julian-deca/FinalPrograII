package controlador;

/**
 *
 * @author Julian
 */

import java.time.LocalDate;
import modelo.entidades.*;
import modelo.excepciones.VehiculoNotFoundException;
import modelo.excepciones.DuplicateVehiculoException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import modelo.excepciones.InputValidationException;
import static modelo.validacion.Validador.parseAndValidateDouble;
import static modelo.validacion.Validador.parseAndValidateInt;
import static modelo.validacion.Validador.validateSelection;

public class ControladorForm {
    
    @FXML private TextField txtPatente, txtPrecio, txtPuertas, txtCarga, txtEjes, txtCilindrada;
    @FXML private ComboBox<Marca> cmbMarca;
    @FXML private ComboBox<Color> cmbColor;
    @FXML private ComboBox<Condicion> cmbCondicion;
    @FXML private ComboBox<Tipo> cmbTipo;
    @FXML private ComboBox<Combustible> cmbCombustible;
    @FXML private CheckBox chkCaja, chkAcoplado, chkSidecar;
    @FXML private VBox panelAuto, panelCamion, panelMoto,panelDatos;
    @FXML private DatePicker datePickerFabricacion;
    @FXML private Button btnCrear,btnActualizar;
    
    private ControladorPrincipal controladorPrincipal;
    
    public void initialize() {
        
        cmbMarca.setItems(FXCollections.observableArrayList(Marca.values()));
        cmbColor.setItems(FXCollections.observableArrayList(Color.values()));
        cmbCondicion.setItems(FXCollections.observableArrayList(Condicion.values()));
        cmbCombustible.setItems(FXCollections.observableArrayList(Combustible.values()));
        cmbTipo.setItems(FXCollections.observableArrayList(Tipo.values()));
        
        cmbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mostrarPanelEspecifico(newVal);
        });
        
    }
    
    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal){
        this.controladorPrincipal = controladorPrincipal;
    }
    
    public void setEditar(Boolean editar){
        if(editar){
            this.mapearParaEdicion();
            btnActualizar.setVisible(true);
            btnActualizar.setManaged(true);
            btnCrear.setVisible(false);
            btnCrear.setManaged(false);
            txtPatente.setDisable(true);
        }
    }
    
    private void showPanel(VBox panel, Boolean estado){
        panel.setVisible(estado);
        panel.setManaged(estado);
    }
    
    private void mostrarPanelEspecifico(Tipo tipo) {
        showPanel(panelAuto,false);
        showPanel(panelCamion,false);
        showPanel(panelMoto,false);
        showPanel(panelDatos,false);

        if (tipo != null){
        showPanel(panelDatos,true);

        switch (tipo) {
            case Tipo.AUTO -> showPanel(panelAuto,true);
            case Tipo.CAMION ->showPanel(panelCamion,true);
            case Tipo.MOTO -> showPanel(panelMoto,true);
        }}
    }
    
    @FXML
    private void agregarVehiculo(ActionEvent event) {
        try {
            Vehiculo vehiculo = crearVehiculoDesdeFormulario();
            this.controladorPrincipal.gestor.agregar(vehiculo);
            limpiarFormulario();
            actualizarTabla();
            cerrarForm(event);
            mostrarAlerta("Éxito", "Vehículo agregado correctamente", Alert.AlertType.INFORMATION);

        }
         catch (InputValidationException e) {
            mostrarAlerta("Atencion", e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (DuplicateVehiculoException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al crear vehiculo", Alert.AlertType.ERROR);
            System.out.println("Error al crear vehiculo: " + e.getMessage());
        }
    }
    
    @FXML
    private void actualizarVehiculo(ActionEvent event) {
        try {
            Vehiculo vehiculo = crearVehiculoDesdeFormulario();
            this.controladorPrincipal.gestor.actualizar(vehiculo);
            actualizarTabla();
            cerrarForm(event);
            mostrarAlerta("Éxito", "Vehículo actualizado correctamente", Alert.AlertType.INFORMATION);
        }
         catch (InputValidationException e) {
            mostrarAlerta("Atencion", e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (VehiculoNotFoundException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void cerrarForm(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
   
    private Vehiculo crearVehiculoDesdeFormulario() throws InputValidationException {
        Tipo tipo = cmbTipo.getValue();
        if (tipo == null) {
            throw new InputValidationException("Seleccione un tipo para continuar");
        }
        String patente = validateSelection(txtPatente.getText(),"Patente");
        if(patente.trim().isEmpty()){
            throw new InputValidationException("Debe ingresar una Patente");
        }
        Marca marca = validateSelection(cmbMarca.getValue(),"Marca");
        Color color = validateSelection(cmbColor.getValue(),"Color");
        LocalDate fabricacion =validateSelection(datePickerFabricacion.getValue(),"Fabricacion");
        double precio = parseAndValidateDouble(txtPrecio.getText(),"Precio");
        Condicion condicion = validateSelection(cmbCondicion.getValue(),"Condicion");
        
        switch (tipo) {
            case Tipo.AUTO -> {
                int puertas = parseAndValidateInt(txtPuertas.getText(),"Número de Puertas");
                Combustible combustible = validateSelection(cmbCombustible.getValue(),"Combustible");
                boolean aire = chkCaja.isSelected();
                return new Auto(patente, marca, fabricacion, precio,color, condicion, puertas, combustible, aire);
            }
                
            case Tipo.CAMION -> {
                double carga = parseAndValidateDouble(txtCarga.getText(),"Capacidad de Carga");
                int ejes = parseAndValidateInt(txtEjes.getText(), "Número de Ejes");
                boolean refri = chkAcoplado.isSelected();
                return new Camion(patente, marca, fabricacion, precio,color, condicion, carga, ejes, refri);
            }
                
            case Tipo.MOTO -> {
                int cilindrada = parseAndValidateInt(txtCilindrada.getText(),"Cilindrada");
                boolean sideCar = chkSidecar.isSelected();
                return new Moto(patente, marca, fabricacion, precio,color, condicion, cilindrada, sideCar);
            }
                
            default -> throw new InputValidationException("Tipo de vehículo no válido seleccionado.");
        }
    }
    
    
    private void actualizarTabla() {
        if(this.controladorPrincipal != null){
        this.controladorPrincipal.actualizarTabla();
        }
    }
    
    private void mapearParaEdicion(){
        Vehiculo vehiculoEdicion = controladorPrincipal.vehiculoSeleccionado;
        
        txtPatente.setText(vehiculoEdicion.getPatente());
        cmbMarca.setValue(vehiculoEdicion.getMarca());
        cmbColor.setValue(vehiculoEdicion.getColor());
        datePickerFabricacion.setValue(vehiculoEdicion.getFabricacion());
        txtPrecio.setText(String.valueOf(vehiculoEdicion.getPrecio()));
        cmbCondicion.setValue(vehiculoEdicion.getCondicion());
        cmbTipo.setValue(vehiculoEdicion.getTipo());  

        txtPuertas.clear();
        cmbCombustible.setValue(null);
        chkCaja.setSelected(false);

        txtCarga.clear();
        txtEjes.clear();
        chkAcoplado.setSelected(false);

        txtCilindrada.clear();
        chkSidecar.setSelected(false);

        switch (vehiculoEdicion) {
            case Auto auto -> {
                txtPuertas.setText(String.valueOf(auto.getNumeroPuertas()));
                cmbCombustible.setValue(auto.getCombustible());
                chkCaja.setSelected(auto.getTieneCajaAutomatica()); // Ajusta según tu atributo
            }
            case Camion camion -> {
                txtCarga.setText(String.valueOf(camion.getCapacidadCarga()));
                txtEjes.setText(String.valueOf(camion.getNumeroEjes()));
                chkAcoplado.setSelected(camion.getTieneAcoplado());
            }
            case Moto moto -> {
                txtCilindrada.setText(String.valueOf(moto.getCilindrada()));
                chkSidecar.setSelected(moto.getTieneSidecar());
            }
            default -> throw new IllegalArgumentException("Tipo de vehículo no válido");
            
        }


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
        this.controladorPrincipal.mostrarAlerta(titulo, mensaje, tipo);
    }
}