package modelo.gestion;

/**
 *
 * @author Julian
 */
import modelo.entidades.Vehiculo;
import java.util.Iterator;
import java.util.List;

public class VehiculoIterator implements Iterator<Vehiculo> {
    private final List<Vehiculo> vehiculos;
    private int posicion;
    
    public VehiculoIterator(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
        this.posicion = 0;
    }
    
    @Override
    public boolean hasNext() {
        return posicion < vehiculos.size();
    }
    
    @Override
    public Vehiculo next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        return vehiculos.get(posicion++);
    }
    
    @Override
    public void remove() {
        if (posicion <= 0) {
            throw new IllegalStateException();
        }
        vehiculos.remove(--posicion);
    }
}