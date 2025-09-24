package pkgfinal;

import java.time.LocalDate;
import modelo.entidades.Auto;
import modelo.entidades.Color;
import modelo.entidades.Combustible;
import modelo.entidades.Condicion;
import modelo.entidades.Marca;

/**
 *
 * @author Julian
 */
public class Final {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Auto coche = new Auto("pas354", Marca.CHEVROLET, LocalDate.now(), 100.00, Color.ROJO, Condicion.NUEVO, 4, Combustible.NAFTA, false);
        System.out.println(coche.toString());
    }
    
}
