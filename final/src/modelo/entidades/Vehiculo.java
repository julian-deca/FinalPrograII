package modelo.entidades;

/**
 *
 * @author Julian
 */
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Vehiculo implements Comparable<Vehiculo>, Serializable {
    private static final long serialVersionUID = 1l;
    protected String patente;
    protected Marca marca;
    protected LocalDate fabricacion;
    protected double precio;
    protected Condicion condicion;
    protected Color color;
    protected Tipo tipo;
    
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion,double precio, Color color, Condicion condicion, Tipo tipo ) {
        this.patente = patente;
        this.marca = marca;
        this.fabricacion = fabricacion;
        this.precio = precio;
        this.condicion = condicion;
        this.color = color;
        this.tipo = tipo;
    }
    
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion, double precio,Color color, Tipo tipo) {
        this(patente, marca, fabricacion, precio, color, Condicion.NUEVO, tipo);
    }
    
    public Vehiculo(String patente, Marca marca, LocalDate fabricacion,double precio, Tipo tipo) {
        this(patente, marca, fabricacion, precio,Color.BLANCO, Condicion.NUEVO, tipo);
    }
    
    public abstract double calcularImpuesto();
    public abstract void realizarMantenimiento();
    
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
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public Tipo getTipo() {return tipo;}

    
    @Override
    public int compareTo(Vehiculo otro) {
        return this.patente.compareTo(otro.patente);
    }
    
    @Override
    public String toString() {
        return String.format("Patente: %s, Marca: %s, Fabricacion: %d, Precio: $%.2f, Condicion: %s, Color: %s, Tipo: %s", 
                           patente, marca, fabricacion.getYear(), precio, condicion, color, tipo);
    }
}