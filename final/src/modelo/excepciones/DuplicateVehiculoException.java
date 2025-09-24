package modelo.excepciones;

/**
 *
 * @author Julian
 */
public class DuplicateVehiculoException extends RuntimeException {
    public DuplicateVehiculoException(String mensaje) {
        super(mensaje);
    }
    
    public DuplicateVehiculoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}