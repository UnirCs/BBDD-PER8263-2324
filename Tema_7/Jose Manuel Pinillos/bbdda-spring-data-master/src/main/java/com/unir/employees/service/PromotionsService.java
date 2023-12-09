package com.unir.employees.service;

import com.unir.employees.data.*;
import com.unir.employees.model.request.PromotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class PromotionsService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final SalaryRepository salaryRepository;
    private final DeptEmpRepository deptEmpRepository;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public String promote(PromotionRequest promotionRequest) throws ParseException {

        try {
            
        }


        return "Promocionado :D";
    }
}