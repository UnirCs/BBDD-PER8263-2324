package com.unir.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@Getter
public class MySqlEmployee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
    private Date birthDate;
    private String departmentId;
    private Date fromDate;
    private Date toDate;
}
