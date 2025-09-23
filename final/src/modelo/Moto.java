package modelo;

import java.time.LocalDate;

/**
 *
 * @author Julian
 */
public class Moto extends Vehiculo {
    private int cilindrada;
    private boolean tieneSidecar;
    
    public Moto(String patente, Marca marca, LocalDate fabricacion, double precio, Color color, Condicion condicion,
               int cilindrada, boolean tieneSidecar) {
        super(patente, marca, fabricacion, precio, color, condicion);
        this.cilindrada = cilindrada;
        this.tieneSidecar = tieneSidecar;
    }
    
    public Moto(String patente, Marca marca, LocalDate fabricacion, double precio,
              Color color, int cilindrada, boolean tieneSidecar) {
        this(patente, marca, fabricacion, precio,color, Condicion.NUEVO, cilindrada, tieneSidecar);
    }
    
    public Moto(String patente, Marca marca, LocalDate fabricacion, double precio, int cilindrada) {
        this(patente, marca, fabricacion, precio, Color.BLANCO, Condicion.NUEVO, cilindrada, false);
    }
    
    @Override
    public double calcularImpuesto() {
        return precio * 0.015;
    }
    
    @Override
    public String obtenerTipo() {
        return "Moto";
    }
    
    @Override
    public void realizarMantenimiento() {
        System.out.println("Realizando mantenimiento de moto: ajuste de cadena y frenos");
    }
    
    // Getters y Setters adicionales
    public int getCilindrada() { return cilindrada; }
    public void setCilindrada(int cilindrada) { this.cilindrada = cilindrada; }
    
    public boolean isTieneSidecar() { return tieneSidecar; }
    public void setTieneSidecar(boolean tieneSidecar) { this.tieneSidecar = tieneSidecar; }
    
 
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Cilindrada: %d cc, Sidecar: %s,", 
                                               cilindrada, tieneSidecar);
    }
}