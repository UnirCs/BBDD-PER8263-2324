package com.unir.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class MySqlEmpleado {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private Date hireDate;
    private String dept_no;
}
