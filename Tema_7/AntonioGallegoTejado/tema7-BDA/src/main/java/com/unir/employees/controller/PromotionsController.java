package com.unir.employees.controller;


import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.model.request.PromotionRequest;
import com.unir.employees.service.PromotionsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PromotionsController {

	private final PromotionsService promotionsService;

	@PostMapping("/api/promotions")
	public ResponseEntity<String> promote(@RequestBody PromotionRequest promotionRequest) throws ParseException{
		return ResponseEntity.ok(promotionsService.promote(promotionRequest));
	}
}