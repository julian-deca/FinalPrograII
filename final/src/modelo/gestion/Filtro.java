package modelo.gestion;

/**
 *
 * @author Julian
 */
import modelo.entidades.Vehiculo;
import modelo.entidades.Marca;
import modelo.entidades.Condicion;
import java.util.List;
import java.util.stream.Collectors;

public class Filtro {
    
    // Wildcard con límite superior
    public static List<? extends Vehiculo> filtrarPorCondicion(List<? extends Vehiculo> vehiculos, 
                                                           Condicion condicion) {
        return vehiculos.stream()
                       .filter(v -> v.getCondicion() == condicion)
                       .collect(Collectors.toList());
    }
    
    // Wildcard con límite superior
    public static List<? extends Vehiculo> filtrarPorMarca(List<? extends Vehiculo> vehiculos, 
                                                          Marca marca) {
        return vehiculos.stream()
                       .filter(v -> v.getMarca() == marca)
                       .collect(Collectors.toList());
    }
   
}