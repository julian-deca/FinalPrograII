package modelo.gestion;

/**
 *
 * @author Julian
 */
import java.util.List;

public interface CRUD<T> {
    boolean agregar(T elemento);
    T buscar(String id);
    boolean actualizar(T elemento);
    boolean eliminar(String id);
    List<T> listarTodos();
    int tamaño();
}