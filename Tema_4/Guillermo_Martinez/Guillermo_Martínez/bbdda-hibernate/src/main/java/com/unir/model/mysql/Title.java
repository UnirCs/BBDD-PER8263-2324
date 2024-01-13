package com.unir.model.mysql;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @Column(name = "title", length = 50)
    private String title;

    @Id
    @Column(name = "from_date", columnDefinition = "DATE")
    private Date fromDate;

    @Column(name = "to_date", columnDefinition = "DATE")
    private Date toDate;
}
