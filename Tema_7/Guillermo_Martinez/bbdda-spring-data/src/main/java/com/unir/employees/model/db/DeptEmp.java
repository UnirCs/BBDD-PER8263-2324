package com.unir.employees.model.db;

import jakarta.persistence.*;
import lombok.*;
import com.unir.employees.model.db.DeptEmp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Entity
@Table(name = "dept_emp")
@IdClass(DeptEmpId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeptEmp {

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_no")
    private Employee employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "dept_no")
    private Department department;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;
}

@Data
class DeptEmpId implements java.io.Serializable {
    private Integer employee;
    private String department;
}
@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, Long> {

    @Query("SELECT d.department FROM DeptEmp d " +
            "WHERE d.employee.emp_no = :employeeId " +
            "AND d.toDate IS NULL")
    String findCurrentDepartmentByEmployeeId(@Param("employeeId") Integer employeeId);
}
