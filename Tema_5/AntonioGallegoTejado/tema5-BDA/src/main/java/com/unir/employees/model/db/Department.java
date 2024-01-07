package com.unir.employees.model.db;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

	@Id
	@Column(name = "dept_no", length = 4)
	private String deptNo;

	@Column(name = "dept_name", length = 40)
	private String deptName;

	@JsonIgnore
	@OneToMany(mappedBy = "department")
	private Set<DeptEmp> deptEmps;

	@JsonIgnore
	@OneToMany(mappedBy = "department")
	private Set<DeptManager> deptManagers;
}
