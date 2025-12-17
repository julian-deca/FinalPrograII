/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Julian
 */
public interface FilePaths {
    static final String BASE = "src/resources/saved files";
    static final String FILE_DAT = "vehiculos.dat";
    
    public static Path getPathBinario(){
        return Paths.get(BASE,FILE_DAT);
    }
    
    public static String getPathBinarioString(){
        return getPathBinario().toString();
    }
    
}
