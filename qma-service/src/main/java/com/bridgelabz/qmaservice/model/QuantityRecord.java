package com.bridgelabz.qmaservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_records")
public class QuantityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation; 
    private Double value1;
    private String unit1;
    private Double value2;
    private String unit2;
    private String targetUnit;
    private Double result;
    private LocalDateTime timestamp;

    private String userEmail;

    public QuantityRecord() {
        this.timestamp = LocalDateTime.now();
    }

    public QuantityRecord(String operation, Double value1, String unit1, Double value2, String unit2, String targetUnit, Double result, String userEmail) {
        this();
        this.operation = operation;
        this.value1 = value1;
        this.unit1 = unit1;
        this.value2 = value2;
        this.unit2 = unit2;
        this.targetUnit = targetUnit;
        this.result = result;
        this.userEmail = userEmail;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Double getValue1() { return value1; }
    public void setValue1(Double value1) { this.value1 = value1; }

    public String getUnit1() { return unit1; }
    public void setUnit1(String unit1) { this.unit1 = unit1; }

    public Double getValue2() { return value2; }
    public void setValue2(Double value2) { this.value2 = value2; }

    public String getUnit2() { return unit2; }
    public void setUnit2(String unit2) { this.unit2 = unit2; }

    public String getTargetUnit() { return targetUnit; }
    public void setTargetUnit(String targetUnit) { this.targetUnit = targetUnit; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
