package com.bridgelabz.qmaservice.controller;

import com.bridgelabz.qmaservice.dto.QuantityRequestDTO;
import com.bridgelabz.qmaservice.dto.ResponseDTO;
import com.bridgelabz.qmaservice.service.IQuantityService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quantity")
public class QuantityController {

    private static final Logger logger = LogManager.getLogger(QuantityController.class);
    private final IQuantityService quantityService;

    @Autowired
    public QuantityController(IQuantityService quantityService) {
        this.quantityService = quantityService;
    }

    @PostMapping("/convert")
    public ResponseEntity<ResponseDTO> convertQuantity(@Valid @RequestBody QuantityRequestDTO requestDTO) {
        logger.info("REST request to convert quantity: value1={}, unit1={}, targetUnit={}, category={}",
                requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getTargetUnit(), requestDTO.getCategory());
        double result = quantityService.convertQuantity(requestDTO);
        logger.info("Conversion successful, result={}", result);
        return ResponseEntity.ok(new ResponseDTO("Converted Successfully", result));
    }

    @PostMapping("/compare")
    public ResponseEntity<ResponseDTO> compareQuantities(@Valid @RequestBody QuantityRequestDTO requestDTO) {
        logger.info("REST request to compare quantities: value1={}, unit1={}, value2={}, unit2={}, category={}",
                requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(), requestDTO.getUnit2(), requestDTO.getCategory());
        double result = quantityService.compareQuantities(requestDTO);
        boolean isEqual = (result == 1.0);
        logger.info("Comparison successful, isEqual={}", isEqual);
        return ResponseEntity.ok(new ResponseDTO("Compared Successfully", isEqual));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addQuantities(@Valid @RequestBody QuantityRequestDTO requestDTO) {
        logger.info("REST request to add quantities: value1={}, unit1={}, value2={}, unit2={}, targetUnit={}, category={}",
                requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(), requestDTO.getUnit2(), requestDTO.getTargetUnit(), requestDTO.getCategory());
        double result = quantityService.addQuantities(requestDTO);
        logger.info("Addition successful, result={}", result);
        return ResponseEntity.ok(new ResponseDTO("Added Successfully", result));
    }

    @PostMapping("/subtract")
    public ResponseEntity<ResponseDTO> subtractQuantities(@Valid @RequestBody QuantityRequestDTO requestDTO) {
        logger.info("REST request to subtract quantities: value1={}, unit1={}, value2={}, unit2={}, targetUnit={}, category={}",
                requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(), requestDTO.getUnit2(), requestDTO.getTargetUnit(), requestDTO.getCategory());
        double result = quantityService.subtractQuantities(requestDTO);
        logger.info("Subtraction successful, result={}", result);
        return ResponseEntity.ok(new ResponseDTO("Subtracted Successfully", result));
    }
}
