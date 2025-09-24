package modelo.gestion;

/**
 *
 * @author Julian
 */
import modelo.entidades.Vehiculo;
import java.util.Comparator;

public class ComparadorPrecio implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Double.compare(v1.getPrecio(), v2.getPrecio());
    }
}