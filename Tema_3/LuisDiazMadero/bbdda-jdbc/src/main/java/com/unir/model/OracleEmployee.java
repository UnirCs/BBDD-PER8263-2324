package com.unir.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class OracleEmployee {
    private int employee_id;
    @Setter
    private String first_name;
    @Setter
    private String last_name;
    @Setter
    private String email;
    @Setter
    private String phone_number;
    @Setter
    private Date hire_date;
    @Setter
    private String job_id;
    @Setter
    private int salary;
    @Setter
    private double commission_pct;
    @Setter
    private int manager_id;
    @Setter
    private int department_id;
}
