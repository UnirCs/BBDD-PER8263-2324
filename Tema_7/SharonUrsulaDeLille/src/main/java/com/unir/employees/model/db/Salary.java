package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "salaries")
@IdClass(SalariesId.class)
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
    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "title")
    private String title;
}

@Data
class SalariesId implements java.io.Serializable {

    private Integer empNo;
    private Date fromDate;
}
