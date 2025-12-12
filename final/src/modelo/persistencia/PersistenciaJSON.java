/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.entidades.Auto;
import modelo.entidades.Camion;
import modelo.entidades.Color;
import modelo.entidades.Combustible;
import modelo.entidades.Condicion;
import modelo.entidades.Marca;
import modelo.entidades.Moto;
import modelo.entidades.Tipo;
import modelo.entidades.Vehiculo;

/**
 *
 * @author Julian
 */
public class PersistenciaJSON implements Persistencia {

   @Override
    public void guardar(List<Vehiculo> vehiculos, String archivo) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            json.append("  {\n");
            
            // Atributos comunes
            appendJsonField(json, "tipo", v.getTipo().toString(), true);
            appendJsonField(json, "patente", v.getPatente(), true);
            appendJsonField(json, "marca", v.getMarca().toString(), true);
            appendJsonField(json, "fabricacion", v.getFabricacion().toString(), true);
            appendJsonField(json, "precio", String.valueOf(v.getPrecio()), false); // Numérico, sin comillas
            appendJsonField(json, "color", v.getColor().toString(), true);
            appendJsonField(json, "condicion", v.getCondicion().toString(), true);

            // Atributos específicos
            switch (v) {
                case Auto auto -> {
                    appendJsonField(json, "numeroPuertas", String.valueOf(auto.getNumeroPuertas()), false);
                    appendJsonField(json, "combustible", auto.getCombustible().toString(), true);
                    appendJsonField(json, "tieneCajaAutomatica", String.valueOf(auto.isTieneCajaAutomatica()), false, true); 
                }
                case Camion camion -> {
                    appendJsonField(json, "capacidadCarga", String.valueOf(camion.getCapacidadCarga()), false);
                    appendJsonField(json, "numeroEjes", String.valueOf(camion.getNumeroEjes()), false);
                    appendJsonField(json, "tieneAcoplado", String.valueOf(camion.isTieneAcoplado()), false, true);
                }
                case Moto moto -> {
                    appendJsonField(json, "cilindrada", String.valueOf(moto.getCilindrada()), false);
                    appendJsonField(json, "tieneSidecar", String.valueOf(moto.isTieneSidecar()), false, true);
                }
                default -> {
                }
            }

            json.append("  }");
            if (i < vehiculos.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(json.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar JSON: " + e.getMessage(), e);
        }
    }

    // Métodos auxiliares para no repetir código de formateo
    private void appendJsonField(StringBuilder sb, String key, String value, boolean isString) {
        appendJsonField(sb, key, value, isString, false);
    }

    private void appendJsonField(StringBuilder sb, String key, String value, boolean isString, boolean isLast) {
        sb.append("    \"").append(key).append("\": ");
        if (isString) sb.append("\"");
        sb.append(value);
        if (isString) sb.append("\"");
        if (!isLast) sb.append(",");
        sb.append("\n");
    }

    @Override
    public List<Vehiculo> cargar(String archivo) {
        List<Vehiculo> lista = new ArrayList<>();
        StringBuilder contentBuilder = new StringBuilder();

        // 1. Leer todo el archivo a un String
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line.trim()); // Trim para quitar espacios extra
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer JSON: " + e.getMessage(), e);
        }

        String json = contentBuilder.toString();
        
        // Validar si está vacío o es solo []
        if (json.length() <= 2) return lista;

        // 2. Quitar los corchetes exteriores []
        json = json.substring(1, json.length() - 1);

        // 3. Separar los objetos. Usamos "},{" como delimitador
        // Esto asume que no hay objetos anidados complejos, lo cual es cierto para tu modelo.
        String[] objetos = json.split("\\},\\{");

        for (String objStr : objetos) {
            // Limpiar llaves sobrantes del split
            objStr = objStr.replace("{", "").replace("}", "");
            
            // Mapa temporal para guardar los valores del objeto actual
            java.util.Map<String, String> mapa = new java.util.HashMap<>();
            
            // 4. Separar por comas para obtener los pares clave:valor
            // Usamos el split asegurando que la coma no esté dentro de comillas (básico)
            String[] pares = objStr.split(",\""); 
            
            for (String par : pares) {
                String[] kv = par.split(":");
                String key = kv[0].replace("\"", "").trim(); // Limpiar comillas de la clave
                String val = kv[1].replace("\"", "").trim(); // Limpiar comillas del valor
                
                // Corrección si el split se comió la comilla inicial del siguiente campo
                if (!mapa.isEmpty() && !key.isEmpty() && !par.startsWith("\"")) { 
                   // Lógica simple: en este caso manual asumimos formato correcto generado por 'guardar'
                }
                mapa.put(key, val);
            }

            // 5. Reconstruir el objeto
            try {
                Tipo tipoEnum = Tipo.valueOf(mapa.get("tipo"));
                String patente = mapa.get("patente");
                Marca marca = Marca.valueOf(mapa.get("marca"));
                LocalDate fabricacion = LocalDate.parse(mapa.get("fabricacion"));
                double precio = Double.parseDouble(mapa.get("precio"));
                Color color = Color.valueOf(mapa.get("color"));
                Condicion condicion = Condicion.valueOf(mapa.get("condicion"));

                switch (tipoEnum) {
                    case AUTO -> {
                        int puertas = Integer.parseInt(mapa.get("numeroPuertas"));
                        Combustible comb = Combustible.valueOf(mapa.get("combustible"));
                        boolean caja = Boolean.parseBoolean(mapa.get("tieneCajaAutomatica"));
                        lista.add(new Auto(patente, marca, fabricacion, precio, color, condicion, puertas, comb, caja));
                    }
                    case CAMION -> {
                        double carga = Double.parseDouble(mapa.get("capacidadCarga"));
                        int ejes = Integer.parseInt(mapa.get("numeroEjes"));
                        boolean acoplado = Boolean.parseBoolean(mapa.get("tieneAcoplado"));
                        lista.add(new Camion(patente, marca, fabricacion, precio, color, condicion, carga, ejes, acoplado));
                    }
                    case MOTO -> {
                        int cilindrada = Integer.parseInt(mapa.get("cilindrada"));
                        boolean sidecar = Boolean.parseBoolean(mapa.get("tieneSidecar"));
                        lista.add(new Moto(patente, marca, fabricacion, precio, color, condicion, cilindrada, sidecar));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al parsear objeto: " + e.getMessage());
                // Continuar con el siguiente o lanzar error según preferencia
            }
        }
        return lista;
    }
    
}
