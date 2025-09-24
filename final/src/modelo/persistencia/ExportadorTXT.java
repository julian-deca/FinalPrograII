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
import java.util.function.Predicate;

public class ExportadorTXT {
    
    public void exportarFiltrado(List<Vehiculo> vehiculos, Predicate<Vehiculo> filtro, String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("=== REPORTE DE VEHÍCULOS FILTRADO ===");
            writer.println("Generado el: " + new java.util.Date());
            writer.println("=" .repeat(50));
            writer.println();
            
            int contador = 0;
            for (Vehiculo v : vehiculos) {
                if (filtro.test(v)) {
                    contador++;
                    writer.println("Vehículo #" + contador);
                    writer.println("Tipo: " + v.obtenerTipo());
                    writer.println("Patente: " + v.getPatente());
                    writer.println("Marca: " + v.getMarca());
                    writer.println("Fabricacion: " + v.getFabricacion());
                    writer.println("Precio: $" + String.format("%,.2f", v.getPrecio()));
                    writer.println("Condicion: " + v.getCondicion());
                    writer.println("Impuesto: $" + String.format("%,.2f", v.calcularImpuesto()));
                    writer.println("-".repeat(30));
                    writer.println();
                }
            }
            
            writer.println("Total de vehículos en el reporte: " + contador);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar TXT: " + e.getMessage(), e);
        }
    }
}