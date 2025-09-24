package modelo.entidades;

/**
 *
 * @author Julian
 */
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Vehiculo implements Comparable<Vehiculo>, Serializable {
    protected String patente;
    protected Marca marca;
    protected LocalDate fabricacion;
    protected double precio;
    protected Condicion condicion;
    protected Color color;
    
    // Constructores sobrecargados
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion,double precio, Color color, Condicion condicion ) {
        this.patente = patente;
        this.marca = marca;
        this.fabricacion = fabricacion;
        this.precio = precio;
        this.condicion = condicion;
        this.color = color;
    }
    
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion, double precio,Color color) {
        this(patente, marca, fabricacion, precio, color, Condicion.NUEVO);
    }
    
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion,double precio) {
        this(patente, marca, fabricacion, precio,Color.BLANCO, Condicion.NUEVO);
    }
    
    // Métodos abstractos
    public abstract double calcularImpuesto();
    public abstract String obtenerTipo();
    public abstract void realizarMantenimiento();
    
    // Getters y Setters
    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }
    
    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
    
    public LocalDate getFabricacion() { return fabricacion; }
    public void setFabricacion(LocalDate fabricacion) { this.fabricacion = fabricacion; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public Condicion getCondicion() { return condicion; }
    public void setCondicion(Condicion condicion) { this.condicion = condicion; }
    
    @Override
    public int compareTo(Vehiculo otro) {
        return this.patente.compareTo(otro.patente);
    }
    
    @Override
    public String toString() {
        return String.format("Patente: %s, Marca: %s, Año de Fabricacion: %d, Precio: $%.2f, Condicion: %s", 
                           patente, marca, fabricacion.getYear(), precio, condicion);
    }
}