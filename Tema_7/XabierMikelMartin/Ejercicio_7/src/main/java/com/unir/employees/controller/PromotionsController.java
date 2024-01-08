package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.data.SalaryRepository;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import com.unir.employees.model.request.PromotionRequest;
import com.unir.employees.service.PromotionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PromotionsController {


    private final PromotionsService promotionsService;

    @PostMapping("/api/promotions")
    public ResponseEntity<String> promote(@RequestBody PromotionRequest promotionRequest) throws ParseException {
        return ResponseEntity.ok(promotionsService.promote(promotionRequest));
    }





}