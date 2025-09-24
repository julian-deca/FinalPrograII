package modelo.excepciones;

/**
 *
 * @author Julian
 */

public class VehiculoNotFoundException extends RuntimeException {
    public VehiculoNotFoundException(String mensaje) {
        super(mensaje);
    }
    
    public VehiculoNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}