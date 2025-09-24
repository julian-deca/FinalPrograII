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

public interface Persistencia {
    void guardar(List<Vehiculo> vehiculos, String archivo);
    List<Vehiculo> cargar(String archivo);
}