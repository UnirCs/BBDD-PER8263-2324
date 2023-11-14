select * from departments order by dept_no desc;
select * from employees where emp_no between 100 and 200;
select * from dept_emp d where d.emp_no in (select emp_no from employees where emp_no between 100 and 200);
-- Hay una vista creada en la base de datos que nos da esta información
select * from current_dept_emp