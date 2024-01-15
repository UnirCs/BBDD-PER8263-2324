package com.unir.employees.model.request;

import java.util.Date;

import lombok.Data;

@Data
public class PromotionRequest {

	private Integer employeeId;
	private String departmentId;	// Nuevo departamento
	private String title;			// Nuevo titulo
	private Integer salary;			// Nuevo salario
	private Date fromDate;
}