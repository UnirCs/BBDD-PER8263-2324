package com.unir.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.sql.Date;

@AllArgsConstructor
@Getter
public class MySqlEmployee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
    private Date birthDate;
    private String dept_no;
    private Date fromDate;
    private Date toDate;
}
