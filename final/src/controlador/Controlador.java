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
import javafx.scene.layout.VBox;

public class Controlador {
    
    @FXML private TextField txtPatente, txtFabricacion, txtPrecio, txtPuertas, txtCarga, txtEjes, txtCilindrada, txtManillar;
    @FXML private ComboBox<Marca> cmbMarca, cmbFiltroMarca;
    @FXML private ComboBox<Color> cmbColor;
    @FXML private ComboBox<Condicion> cmbCondicion, cmbFiltroCondicion;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<Combustible> cmbCombustible;
    @FXML private CheckBox chkAire, chkRefri, chkMaletero;
    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private VBox panelAuto, panelCamion, panelMoto;
    
    private GestorVehiculos gestor;
    
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
        
        actualizarTabla();
    }
    
    private void mostrarPanelEspecifico(String tipo) {
        panelAuto.setVisible(false);
        panelCamion.setVisible(false);
        panelMoto.setVisible(false);
        
        switch (tipo) {
            case "Auto": panelAuto.setVisible(true); break;
            case "Camión": panelCamion.setVisible(true); break;
            case "Moto": panelMoto.setVisible(true); break;
        }
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
            limpiarFormulario();
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
        LocalDate fabricacion = LocalDate.parse(txtFabricacion.getText());
        double precio = Double.parseDouble(txtPrecio.getText());
        Condicion condicion = cmbCondicion.getValue();
        String tipo = cmbTipo.getValue();
        
        switch (tipo) {
            case "Auto":
                int puertas = Integer.parseInt(txtPuertas.getText());
                Combustible combustible = cmbCombustible.getValue();
                boolean aire = chkAire.isSelected();
                return new Auto(patente, marca, fabricacion, precio,color, condicion, puertas, combustible, aire);
                
            case "Camión":
                double carga = Double.parseDouble(txtCarga.getText());
                int ejes = Integer.parseInt(txtEjes.getText());
                boolean refri = chkRefri.isSelected();
                return new Camion(patente, marca, fabricacion, precio,color, condicion, carga, ejes, refri);
                
            case "Moto":
                int cilindrada = Integer.parseInt(txtCilindrada.getText());
                boolean maletero = chkMaletero.isSelected();
                return new Moto(patente, marca, fabricacion, precio,color, condicion, cilindrada, maletero);
                
            default:
                throw new IllegalArgumentException("Tipo de vehículo no válido");
        }
    }
    
    private void actualizarTabla() {
        tablaVehiculos.setItems(FXCollections.observableArrayList(gestor.listarTodos()));
    }
    
    private void limpiarFormulario() {
        txtPatente.clear();
        txtFabricacion.clear();
        txtPrecio.clear();
        txtPuertas.clear();
        txtCarga.clear();
        txtEjes.clear();
        txtCilindrada.clear();
        txtManillar.clear();
        cmbMarca.setValue(null);
        cmbCondicion.setValue(null);
        cmbTipo.setValue(null);
        cmbCombustible.setValue(null);
        chkAire.setSelected(false);
        chkRefri.setSelected(false);
        chkMaletero.setSelected(false);
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}