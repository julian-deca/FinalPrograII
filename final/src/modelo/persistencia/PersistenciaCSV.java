package modelo.persistencia;

/**
 *
 * @author Julian
 */
import modelo.entidades.*;
import java.util.List;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PersistenciaCSV implements Persistencia {
    
    @Override
    public void guardar(List<Vehiculo> vehiculos, String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("TIPO,PLACA,MARCA,AÑO,PRECIO,ESTADO,ATRIBUTOS_ESPECIFICOS");
            
            for (Vehiculo v : vehiculos) {
                StringBuilder linea = new StringBuilder();
                linea.append(v.obtenerTipo()).append(",");
                linea.append(v.getPatente()).append(",");
                linea.append(v.getMarca()).append(",");
                linea.append(v.getFabricacion()).append(",");
                linea.append(v.getPrecio()).append(",");
                linea.append(v.getCondicion()).append(",");
                
                if (v instanceof Auto auto) {
                    linea.append("Puertas:").append(auto.getNumeroPuertas()).append(";");
                    linea.append("Combustible:").append(auto.getCombustible()).append(";");
                    linea.append("A/C:").append(auto.isTieneCajaAutomatica());
                } else if (v instanceof Camion camion) {
                    linea.append("Carga:").append(camion.getCapacidadCarga()).append(";");
                    linea.append("Ejes:").append(camion.getNumeroEjes()).append(";");
                    linea.append("Acoplado:").append(camion.isTieneAcoplado());
                } else if (v instanceof Moto moto) {
                    linea.append("Cilindrada:").append(moto.getCilindrada()).append(";");
                    linea.append("Sidecar:").append(moto.isTieneSidecar()).append(";");
                }
                
                writer.println(linea.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar CSV: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Vehiculo> cargar(String archivo) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                String[] partes = linea.split(",");
                if (partes.length >= 6) {
                    String tipo = partes[0];
                    String patente = partes[1];
                    Marca marca = Marca.valueOf(partes[2]);
                    LocalDate fabricacion = LocalDate.parse(partes[3]);
                    double precio = Double.parseDouble(partes[4]);
                    Color color = Color.valueOf(partes[5]);
                    Condicion condicion = Condicion.valueOf(partes[6]);
                    
                    Vehiculo vehiculo = null;
                    
                    switch (tipo) {
                        case "Automóvil" -> {
                            String[] attrsAuto = partes[7].split(";");
                            int puertas = Integer.parseInt(attrsAuto[0].split(":")[1]);
                            Combustible combustible = Combustible.valueOf(attrsAuto[1].split(":")[1]);
                            boolean cajaAutomatica = Boolean.parseBoolean(attrsAuto[2].split(":")[1]);
                            vehiculo = new Auto(patente, marca, fabricacion, precio, color, condicion, puertas, combustible, cajaAutomatica);
                        }
                            
                        case "Camión" -> {
                            String[] attrsCamion = partes[7].split(";");
                            double carga = Double.parseDouble(attrsCamion[0].split(":")[1]);
                            int ejes = Integer.parseInt(attrsCamion[1].split(":")[1]);
                            boolean acoplado = Boolean.parseBoolean(attrsCamion[2].split(":")[1]);
                            vehiculo = new Camion(patente, marca, fabricacion, precio, color, condicion, carga, ejes, acoplado);
                        }
                            
                        case "Motocicleta" -> {
                            String[] attrsMoto = partes[7].split(";");
                            int cilindrada = Integer.parseInt(attrsMoto[0].split(":")[1]);
                            boolean sidecar = Boolean.parseBoolean(attrsMoto[1].split(":")[1]);
                            vehiculo = new Moto(patente, marca, fabricacion, precio, color, condicion, cilindrada, sidecar);
                        }
                    }
                    
                    if (vehiculo != null) {
                        vehiculos.add(vehiculo);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar CSV: " + e.getMessage(), e);
        }
        
        return vehiculos;
    }
}