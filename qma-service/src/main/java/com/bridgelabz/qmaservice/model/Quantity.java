package com.bridgelabz.qmaservice.model;

import com.bridgelabz.qmaservice.interfaces.Unit;
import com.bridgelabz.qmaservice.enums.ArithmeticOperation;
import java.util.Objects;

public class Quantity<T extends Unit> {

    private final double value;
    private final T unit;
    private static final double DIFFERENCE = 0.0001;

    public Quantity(double value, T unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public T getUnit() {
        return unit;
    }

    public Quantity<T> convertTo(T targetUnit) {
        double newBaseValue = this.getBaseValue();
        double targetValue = targetUnit.convertFromBaseUnit(newBaseValue);
        return new Quantity<>(targetValue, targetUnit);
    }

    private double getBaseValue() {
        return this.unit.convertToBaseUnit(this.value);
    }

    private void validateArithmeticOperation(Quantity<T> other, T targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Cannot perform arithmetic with a null quantity or specify a null target unit.");
        }

        if (!this.unit.supportsArithmetic() || !other.getUnit().supportsArithmetic()) {
            throw new UnsupportedOperationException("Arithmetic operations are not supported for this unit category.");
        }
        
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Cannot perform arithmetic on quantities of different measurement categories.");
        }
    }

    private Quantity<T> performOperation(Quantity<T> other, T targetUnit, ArithmeticOperation operation) {
        this.validateArithmeticOperation(other, targetUnit);
        double resultBaseValue = operation.apply(this.getBaseValue(), other.getBaseValue());
        return new Quantity<>(targetUnit.convertFromBaseUnit(resultBaseValue), targetUnit);
    }

    public Quantity<T> add(Quantity<T> other) {
        return this.add(other, this.unit);
    }

    public Quantity<T> add(Quantity<T> other, T targetUnit) {
        return performOperation(other, targetUnit, ArithmeticOperation.ADD);
    }

    public Quantity<T> subtract(Quantity<T> other) {
        return this.subtract(other, this.unit);
    }

    public Quantity<T> subtract(Quantity<T> other, T targetUnit) {
        return performOperation(other, targetUnit, ArithmeticOperation.SUBTRACT);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Quantity<?> other = (Quantity<?>) obj;
        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }
        return Math.abs(this.getBaseValue() - other.getBaseValue()) < DIFFERENCE;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBaseValue());
    }
}
