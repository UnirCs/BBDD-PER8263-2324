package com.unir.employees.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "salaries")
@IdClass(SalaryId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Salary {
    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee empNo;

    @Id
    @Column(name = "from_date", columnDefinition="DATE")
    private Date fromDate;

    @Column(name = "to_date", columnDefinition="DATE")
    private Date toDate;

    @Column(name = "salary")
    private int salary;
}

@Data
class SalaryId implements java.io.Serializable {
    private Integer empNo;
    private Date fromDate;

}