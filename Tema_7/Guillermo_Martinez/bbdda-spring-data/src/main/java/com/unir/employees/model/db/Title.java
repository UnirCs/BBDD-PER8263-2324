//Ejercicio Tema7
package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Entity
@Table(name = "titles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Title {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no", nullable = false)
    private Employee employee;

    @Id
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "from_date", nullable = false)
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;
}
@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

    @Query("SELECT t.title FROM Title t " +
            "WHERE t.employee.empNo = :employeeId " +
            "AND t.toDate IS NULL")
    String findCurrentTitleByEmployeeId(@Param("employeeId") Integer employeeId);
}