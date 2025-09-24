package modelo.gestion;

/**
 *
 * @author Julian
 */
import modelo.entidades.Vehiculo;
import java.util.Comparator;

public class ComparadorFabricacion implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return v1.getFabricacion().compareTo(v2.getFabricacion());
    }
}