package com.unir.employees.model.db;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;
}

@Data
class TitleId implements java.io.Serializable {
	private Integer employee;
	private String title;
	private Date fromDate;
}
