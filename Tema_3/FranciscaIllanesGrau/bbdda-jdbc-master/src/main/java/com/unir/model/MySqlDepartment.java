package com.unir.model;  // Asegúrate de que el paquete coincida con tu estructura de proyecto

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Anotaciones de Lombok para generar constructor, getters y setters
@AllArgsConstructor
@NoArgsConstructor  // Si necesitas un constructor sin argumentos
@Getter
@Setter
public class MySqlDepartment {
    private String deptNo;
    private String deptName;

    // Con Lombok, no es necesario escribir explícitamente los constructores, getters y setters
}

