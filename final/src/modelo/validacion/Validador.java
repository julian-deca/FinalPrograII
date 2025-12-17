/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo.validacion;

import modelo.excepciones.InputValidationException;

/**
 *
 * @author Julian
 */
public interface Validador {
    public static int parseAndValidateInt(String value, String fieldName) throws InputValidationException {
    try {
        if (value == null || value.trim().isEmpty()) {
            throw new InputValidationException("El campo '" + fieldName + "' no puede estar vacío");
        }
        return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
        throw new InputValidationException("El campo '" + fieldName + "' debe ser un número entero");
    }
}

// Helper to parse Doubles
public static double parseAndValidateDouble(String value, String fieldName) throws InputValidationException {
    try {
        if (value == null || value.trim().isEmpty()) {
            throw new InputValidationException("El campo '" + fieldName + "' no puede estar vacío");
        }
        return Double.parseDouble(value.trim());
    } catch (NumberFormatException e) {
        throw new InputValidationException("El campo '" + fieldName + "' debe ser un número decimal");
    }
}

// Helper to validate Objects (ComboBox/DatePicker)
public static <T> T validateSelection(T value, String fieldName) throws InputValidationException {
    if (value == null) {
        throw new InputValidationException("Debe seleccionar un valor para '" + fieldName + "'");
    }
    return value;
}
}
