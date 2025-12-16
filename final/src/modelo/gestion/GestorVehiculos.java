package modelo.gestion;

/**
 *
 * @author Julian
 */

import modelo.entidades.Vehiculo;
//import excepciones.VehiculoNotFoundException;
//import excepciones.DuplicateVehiculoException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import modelo.entidades.Condicion;
import modelo.entidades.Marca;
import modelo.excepciones.DuplicateVehiculoException;
import modelo.excepciones.VehiculoNotFoundException;

public class GestorVehiculos implements CRUD<Vehiculo> {
    private final List<Vehiculo> vehiculos;
    
    public GestorVehiculos() {
        this.vehiculos = new ArrayList<>();
    }
    
    @Override
    public boolean agregar(Vehiculo vehiculo) {
        if (buscar(vehiculo.getPatente()) != null) {
            throw new DuplicateVehiculoException("Vehículo con patente " + vehiculo.getPatente() + " ya existe");
        }
        return agregarVehiculo(vehiculos,vehiculo);
    }
    
    // Wildcard con límite inferior
    public static boolean agregarVehiculo(List <? super Vehiculo> destino, Vehiculo vehiculo) {
        return destino.add(vehiculo);
    }
    
    @Override
    public Vehiculo buscar(String patente) {
        return vehiculos.stream()
                       .filter(v -> v.getPatente().equals(patente))
                       .findFirst()
                       .orElse(null);
    }
    
    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        String patente = vehiculo.getPatente();
        Vehiculo vehiculoOriginal = buscar(patente);
        
        int index = vehiculos.indexOf(vehiculoOriginal);
        if (index != -1) {
            vehiculos.set(index, vehiculo);
            return true;
        }
       throw new VehiculoNotFoundException("Vehículo no encontrado para actualizar");
    }
    
    @Override
    public boolean eliminar(String patente) {
        Vehiculo vehiculo = buscar(patente);
        if (vehiculo != null) {
            return vehiculos.remove(vehiculo);
        }
        throw new VehiculoNotFoundException("Vehículo con patente " + patente + " no encontrado");
    }
    
    @Override
    public List<Vehiculo> listarTodos() {
        return new ArrayList<>(vehiculos);
    }
    
    @Override
    public int tamaño() {
        return vehiculos.size();
    }
    
    // Iterator personalizado
    public Iterator<Vehiculo> iterator() {
        return new VehiculoIterator(vehiculos);
    }
    
    // Métodos con wildcards
    public void procesarVehiculos(List<? extends Vehiculo> listaVehiculos, Consumer<? super Vehiculo> action) {
        listaVehiculos.forEach(action);
    }
    
    public <T> List<T> transformarVehiculos(List<? extends Vehiculo> listaVehiculos, 
                                          Function<? super Vehiculo, ? extends T> mapper) {
        return listaVehiculos.stream().map(mapper).collect(Collectors.toList());
    }
    
    // Ordenamiento
    public void ordenarNatural() {
        Collections.sort(vehiculos);
    }
    
    public void ordenarPorPrecio() {
        Collections.sort(vehiculos, new ComparadorPrecio());
    }
    
    public void ordenarPorFabricacion() {
        Collections.sort(vehiculos, new ComparadorFabricacion());
    }
    
    // Filtrado con wildcards
    public List<Vehiculo> filtrarPorCondicion(List<Vehiculo> listVehiculos, Condicion condicion) {
        if (condicion == null){
            return listVehiculos;
        }
        return (List<Vehiculo>) Filtro.filtrarPorCondicion(listVehiculos, condicion);
    }
    
    public List<Vehiculo> filtrarPorMarca(List<Vehiculo> listVehiculos,Marca marca) {
        if (marca == null){
            return listVehiculos;
        }
        return (List<Vehiculo>) Filtro.filtrarPorMarca(listVehiculos, marca);
    }
    
    // Métodos con interfaces funcionales
    public void aplicarDescuento(double porcentaje) {
        Consumer<Vehiculo> descuento = v -> v.setPrecio(v.getPrecio() * (1 - porcentaje/100));
        vehiculos.forEach(descuento);
    }
    
    public void actualizarCondicion(Condicion nuevoCondicion, Function<Vehiculo, Boolean> condicion) {
        Consumer<Vehiculo> actualizador = v -> {
            if (condicion.apply(v)) {
                v.setCondicion(nuevoCondicion);
            }
        };
        vehiculos.forEach(actualizador);
    }
}