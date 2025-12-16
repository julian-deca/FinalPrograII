package modelo.entidades;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Julian
 */
public class Camion extends Vehiculo implements VehiculoCarga, Serializable {
    private static final long serialVersionUID = 1L;
    private double capacidadCarga;
    private int numeroEjes;
    private boolean tieneAcoplado;
    
    public Camion(String patente, Marca marca, LocalDate fabricacion, double precio, Color color, Condicion condicion,
                 double capacidadCarga, int numeroEjes, boolean tieneAcoplado) {
        super(patente, marca, fabricacion, precio, color, condicion,Tipo.CAMION);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
        this.tieneAcoplado = tieneAcoplado;
    }
    
    public Camion(String patente, Marca marca, LocalDate fabricacion, double precio,
                 Color color, double capacidadCarga, int numeroEjes) {
        this(patente, marca, fabricacion, precio, color, Condicion.NUEVO, capacidadCarga, numeroEjes, false);
    }
    
    public Camion(String patente, Marca marca, LocalDate fabricacion, double precio, double capacidadCarga) {
        this(patente, marca, fabricacion, precio, Color.BLANCO, Condicion.NUEVO, capacidadCarga, 2, false);
    }
    
    @Override
    public double calcularImpuesto() {
        return precio * 0.05 + capacidadCarga * 0.01;
    }
    

    @Override
    public void realizarMantenimiento() {
        System.out.println("Realizando mantenimiento de camión: revisión de frenos y suspensión");
    }
    
    // Implementación de la interfaz VehiculoCarga
    @Override
    public double calcularCapacidadCarga() {
        return capacidadCarga;
    }
    
    @Override
    public void verificarDocumentacion() {
        System.out.println("Verificando documentación para transporte de carga");
    }
    
    // Getters y Setters adicionales
    public double getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(double capacidadCarga) { this.capacidadCarga = capacidadCarga; }
    
    public int getNumeroEjes() { return numeroEjes; }
    public void setNumeroEjes(int numeroEjes) { this.numeroEjes = numeroEjes; }
    
    public boolean getTieneAcoplado() { return tieneAcoplado; }
    public void setTieneAcoplado(boolean tieneAcoplado) { 
        this.tieneAcoplado = tieneAcoplado; 
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Carga: %.2f ton, Ejes: %d, Acoplado: %s", 
                                               capacidadCarga, numeroEjes, tieneAcoplado);
    }
}