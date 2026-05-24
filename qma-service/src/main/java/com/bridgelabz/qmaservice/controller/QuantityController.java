package com.bridgelabz.qmaservice.controller;

import com.bridgelabz.qmaservice.dto.QuantityRequestDTO;
import com.bridgelabz.qmaservice.dto.ResponseDTO;
import com.bridgelabz.qmaservice.service.IQuantityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;



@RestController
@RequestMapping("/api/quantity")
public class QuantityController {

    private final IQuantityService quantityService;

    @Autowired
    public QuantityController(IQuantityService quantityService) {
        this.quantityService = quantityService;
    }

    @PostMapping("/convert")
    public ResponseDTO convertQuantity(@RequestBody QuantityRequestDTO requestDTO) {
        double result = quantityService.convertQuantity(requestDTO);
        return new ResponseDTO("Converted Successfully", result);
    }

    @PostMapping("/compare")
    public ResponseDTO compareQuantities(@RequestBody QuantityRequestDTO requestDTO) {
        double result = quantityService.compareQuantities(requestDTO);
        boolean isEqual = (result == 1.0);
        return new ResponseDTO("Compared Successfully", isEqual);
    }

    @PostMapping("/add")
    public ResponseDTO addQuantities(@RequestBody QuantityRequestDTO requestDTO) {
        double result = quantityService.addQuantities(requestDTO);
        return new ResponseDTO("Added Successfully", result);
    }

    @PostMapping("/subtract")
    public ResponseDTO subtractQuantities(@RequestBody QuantityRequestDTO requestDTO) {
        double result = quantityService.subtractQuantities(requestDTO);
        return new ResponseDTO("Subtracted Successfully", result);
    }
}
