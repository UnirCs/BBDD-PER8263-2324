package com.unir.model.mysql;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "titles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TitlesId.class)
@Data
public class Title {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee empNo;

    @Id
    @Column(name = "from_date", columnDefinition="DATE")
    private Date fromDate;

    @Id
    @Column(name = "title")
    private String title;

    @Column(name = "to_date", columnDefinition="DATE")
    private Date toDate;
}
