/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

/**
 *
 * @author Julian
 */
import modelo.entidades.Vehiculo;
import java.util.List;
import java.io.*;
import modelo.entidades.Auto;
import modelo.entidades.Camion;
import modelo.entidades.Condicion;
import modelo.entidades.Marca;
import modelo.entidades.Moto;

public class ExportadorTXT {
    
    public void exportarFiltrado(List<Vehiculo> vehiculos, Marca filtroMarca, Condicion filtroCondicion, String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("=== REPORTE DE VEHÍCULOS ===");
            if(filtroMarca != null){
                writer.println("Marca: " + filtroMarca.toString());
            }
            if(filtroCondicion != null){
                writer.println("Condicion: " + filtroCondicion.toString());
            }
            writer.println("=" .repeat(50));

            
            writer.println();
            
            int contador = 0;
            for (Vehiculo v : vehiculos) {
                    contador++;
                    writer.println("Vehículo #" + contador);
                    writer.println("Tipo: " + v.getTipo());
                    writer.println("Patente: " + v.getPatente());
                    writer.println("Marca: " + v.getMarca());
                    writer.println("Fabricacion: " + v.getFabricacion());
                    writer.println("Precio: $" + String.format("%,.2f", v.getPrecio()));
                    writer.println("Condicion: " + v.getCondicion());
                    writer.println("Impuesto: $" + String.format("%,.2f", v.calcularImpuesto()));
                    
                    switch (v) {
                        case Auto auto -> {
                            writer.println("Numero de Puertas: " + String.valueOf(auto.getNumeroPuertas()));
                            writer.println("Combustible: " + auto.getCombustible());
                            writer.println("Tiene Caja Automatica: " + (auto.getTieneCajaAutomatica()? "Si" : "No"));
                        }
                        case Camion camion -> {
                            writer.println("Capacidad de Carga: " + camion.calcularCapacidadCarga());
                            writer.println("Nro de Ejes: " + String.valueOf(camion.getNumeroEjes()));
                            writer.println("Tiene Acoplado: " + (camion.getTieneAcoplado()? "Si" : "No"));
                        }
                        case Moto moto -> {
                            writer.println("Cilindrada: " + String.valueOf(moto.getCilindrada()));
                            writer.println("Tiene Sidecar: " + (moto.getTieneSidecar()? "Si" : "No"));
                        }
                        default -> {}
                    }
                writer.println("-".repeat(30));
                writer.println();
            }
            
            writer.println("Total de vehículos en el reporte: " + contador);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar TXT: " + e.getMessage(), e);
        }
    }
}