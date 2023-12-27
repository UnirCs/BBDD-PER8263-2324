package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "salaries")
@IdClass(TitlesId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Title {
    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee empNo;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Id
    @Column(name = "title")
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;
}

@Data
class TitlesId implements java.io.Serializable {

    private Integer empNo;
    private Date fromDate;
    private String title;
}