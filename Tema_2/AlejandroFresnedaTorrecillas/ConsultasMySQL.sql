/* Consulta nº de mujeres y hombres */
select GENDER, count(GENDER) as TOTAL from employees.employees group by GENDER;
/* Consulta salario mas alto*/
select e.first_name, e.last_name, s.salary, cd.dept_no from employees e
                INNER JOIN salaries s ON e.emp_no=s.emp_no
                INNER JOIN dept_emp d ON d.emp_no=e.emp_no
                INNER JOIN current_dept_emp cd ON d.emp_no=cd.emp_no
    where s.salary=(select max(salary) from salaries
                    INNER JOIN dept_emp ON salaries.emp_no=dept_emp.emp_no where dept_emp.dept_no='d009')
      and d.dept_no='d009';
/* Consulta 2º salario mas alto*/
select e.first_name, e.last_name, s.salary, cd.dept_no from employees e
                INNER JOIN salaries s ON e.emp_no = s.emp_no
                INNER JOIN dept_emp d ON d.emp_no = e.emp_no
                INNER JOIN current_dept_emp cd ON d.emp_no=cd.emp_no
    where s.salary = (select MAX(s.salary) from salaries s
        INNER JOIN dept_emp de ON s.emp_no = de.emp_no where de.dept_no = 'd009' AND s.salary < (
        select MAX(salary) from salaries INNER JOIN dept_emp ON salaries.emp_no = dept_emp.emp_no
        where dept_emp.dept_no = 'd009')
) AND d.dept_no = 'd009';
/* Consulta nº mas alto de empleados contratados en un mes concreto */
select count(hire_date) as cuenta, hire_date from employees
    where hire_date like '%-06-%' group by hire_date order by cuenta desc limit 1;


