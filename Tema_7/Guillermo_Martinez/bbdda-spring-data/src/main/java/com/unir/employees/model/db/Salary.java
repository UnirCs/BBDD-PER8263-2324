//Ejercicio Tema7
package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.unir.employees.model.db.Salary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Entity
@Table(name = "salaries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Salary {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no", nullable = false)
    private Employee employee;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "from_date", nullable = false)
    private Date fromDate;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date", nullable = false)
    private Date toDate;
}

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("SELECT s.salary FROM Salary s " +
            "WHERE s.employee.empNo = :employeeId " +
            "AND s.toDate IS NULL")
    Integer findCurrentSalaryByEmployeeId(@Param("employeeId") Integer employeeId);
}
