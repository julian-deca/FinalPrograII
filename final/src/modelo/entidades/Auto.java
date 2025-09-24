
package modelo.entidades;

import java.time.LocalDate;

/**
 *
 * @author Julian
 */
public class Auto extends Vehiculo {
    private int numeroPuertas;
    private Combustible combustible;
    private boolean tieneCajaAutomatica;
    
    public Auto(String patente, Marca marca, LocalDate fabricacion, double precio, Color color, Condicion condicion,
               int numeroPuertas, Combustible combustible, boolean tieneCajaAutomatica) {
        super(patente, marca, fabricacion, precio, color, condicion);
        this.numeroPuertas = numeroPuertas;
        this.combustible = combustible;
        this.tieneCajaAutomatica = tieneCajaAutomatica;
    }
    
    public Auto(String patente, Marca marca, LocalDate fabricacion, double precio,
              Color color, int numeroPuertas, Combustible combustible) {
        this(patente, marca, fabricacion, precio,color, Condicion.NUEVO, numeroPuertas, combustible, false);
    }
    
    public Auto(String patente, Marca marca, LocalDate fabricacion,double precio, int numeroPuertas) {
        this(patente, marca, fabricacion, precio, Color.BLANCO, Condicion.NUEVO, numeroPuertas, Combustible.NAFTA, false);
    }
    
    @Override
    public double calcularImpuesto() {
        double base = precio * 0.02;
        //if (fabricacion < 2010) base *= 1.5;
        return base;
    }
    
    @Override
    public String obtenerTipo() {
        return "Auto";
    }
    
    @Override
    public void realizarMantenimiento() {
        System.out.println("Realizando mantenimiento de auto: cambio de aceite y filtros");
    }
    
    // Getters y Setters adicionales
    public int getNumeroPuertas() { return numeroPuertas; }
    public void setNumeroPuertas(int numeroPuertas) { this.numeroPuertas = numeroPuertas; }
    
    public Combustible getCombustible() { return combustible; }
    public void setCombustible(Combustible combustible) { this.combustible = combustible; }
    
    public boolean isTieneCajaAutomatica() { return tieneCajaAutomatica; }
    public void setTieneCajaAutomatica(boolean tieneCajaAutomatica) { 
        this.tieneCajaAutomatica = tieneCajaAutomatica; 
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Puertas: %d, Combustible: %s, Caja Automatica: %s", 
                                               numeroPuertas, combustible, tieneCajaAutomatica);
    }
}