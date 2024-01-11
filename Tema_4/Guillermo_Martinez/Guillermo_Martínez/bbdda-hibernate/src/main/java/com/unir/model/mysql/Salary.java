package com.unir.model.mysql;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;



@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @Column(name = "from_date", columnDefinition="DATE")
    private Date fromDate;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "to_date", columnDefinition="DATE")
    private Date toDate;


}