package com.unir.model.mysql;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "salaries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Salary {

	@Id
	@ManyToOne
	@JoinColumn(name = "emp_no")
	private Employee empNo;

	@Column(name = "salary")
	private Integer salary;

	@Id
	@Column(name = "from_date", columnDefinition = "DATE")
	private Date fromDate;

	@Column(name = "to_date", columnDefinition = "DATE")
	private Date toDate;
}
