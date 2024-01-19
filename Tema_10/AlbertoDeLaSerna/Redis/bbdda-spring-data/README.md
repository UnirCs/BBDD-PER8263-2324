# BBDDA-Spring-Data + Redis caché Actividad 10

Ya he conseguido resolver, el problema era en la entidad Title, resulta que la definición de la clase TitleId asociada para hibernate no estaba bien puesta.

En @IdClass(TitleId.class) tenía puesto @IdClass(SalaryId.class), no comments ... (Disculpa las molestias ...).

```java
package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private Employee employee;

    @Id
    @Column(name = "title")
    private String title;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

}

@Data
class TitleId implements java.io.Serializable {
    private Integer employee;
    private String title;
    private Date fromDate;
}
```
