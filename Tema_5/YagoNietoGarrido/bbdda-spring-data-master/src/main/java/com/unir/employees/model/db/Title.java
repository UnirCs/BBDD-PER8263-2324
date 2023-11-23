package com.unir.employees.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "titles")
@IdClass(TitleId.class)
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
    @Column(name = "from_date", columnDefinition = "DATE")
    private Date fromDate;

    @Id
    @Column(name = "title")
    private String title;

    @Column(name = "to_date", columnDefinition = "DATE")
    private Date toDate;
}


@Data
class TitleId implements Serializable {
    private Integer empNo;
    private Date fromDate;
    private String title;
}