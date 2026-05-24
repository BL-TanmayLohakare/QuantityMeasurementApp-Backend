package com.bridgelabz.qmaservice.service;

import com.bridgelabz.qmaservice.dto.QuantityRequestDTO;
import com.bridgelabz.qmaservice.enums.LengthUnit;
import com.bridgelabz.qmaservice.enums.TemperatureUnit;
import com.bridgelabz.qmaservice.enums.VolumeUnit;
import com.bridgelabz.qmaservice.enums.WeightUnit;
import com.bridgelabz.qmaservice.interfaces.Unit;
import com.bridgelabz.qmaservice.model.Quantity;
import com.bridgelabz.qmaservice.model.QuantityRecord;
import com.bridgelabz.qmaservice.client.AuthClient;
import com.bridgelabz.qmaservice.repository.IQuantityRepository;
import com.bridgelabz.qmaservice.model.User;
import com.bridgelabz.qmaservice.service.IQuantityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Service
@Scope("singleton")
public class QuantityService implements IQuantityService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityService.class);
    private final IQuantityRepository repository;
    private final AuthClient authClient;

    @Autowired
    public QuantityService(IQuantityRepository repository, AuthClient authClient) {
        this.repository = repository;
        this.authClient = authClient;
        logger.info("QuantityService Initialized (Singleton Scope)");
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof String) {
            String email = (String) auth.getPrincipal();
            return authClient.getUserDetails(email);
        }
        return null;
    }

    private Unit getUnitEnum(String category, String unitStr) {
        if (category == null || unitStr == null) {
            logger.error("Category or Unit is null");
            throw new IllegalArgumentException("Category and Unit must not be null.");
        }
        try {
            switch (category.toUpperCase()) {
                case "LENGTH":
                    return LengthUnit.valueOf(unitStr.toUpperCase());
                case "WEIGHT":
                    return WeightUnit.valueOf(unitStr.toUpperCase());
                case "VOLUME":
                    return VolumeUnit.valueOf(unitStr.toUpperCase());
                case "TEMPERATURE":
                    return TemperatureUnit.valueOf(unitStr.toUpperCase());
                default:
                    logger.error("Unknown Category: {}", category);
                    throw new IllegalArgumentException("Unknown Category: " + category);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid unit '{}' for category '{}'", unitStr, category);
            throw new IllegalArgumentException("Invalid unit '" + unitStr + "' for category '" + category + "'.");
        }
    }

    @Override
    public double convertQuantity(QuantityRequestDTO requestDTO) {
        logger.info("Processing conversion request for {} to {}", requestDTO.getUnit1(), requestDTO.getTargetUnit());
        Unit sourceUnit = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit1());
        Unit targetUnitEnum = getUnitEnum(requestDTO.getCategory(), requestDTO.getTargetUnit());

        Quantity<Unit> quantity = new Quantity<>(requestDTO.getValue1(), sourceUnit);
        Quantity<Unit> converted = quantity.convertTo(targetUnitEnum);
        double result = converted.getValue();

        
        if (repository != null) {
            User user = getCurrentUser();
            String email = (user != null) ? user.getEmail() : "Anonymous";
            repository.save(new QuantityRecord(
                    "CONVERT", requestDTO.getValue1(), requestDTO.getUnit1(), null, null, requestDTO.getTargetUnit(),
                    result, email));
            logger.info("Saved CONVERT operation to database");
        }

        return result;
    }

    @Override
    public double compareQuantities(QuantityRequestDTO requestDTO) {
        logger.info("Processing comparison request for {} and {}", requestDTO.getUnit1(), requestDTO.getUnit2());
        Unit unit1 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit1());
        Unit unit2 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit2());

        Quantity<Unit> q1 = new Quantity<>(requestDTO.getValue1(), unit1);
        Quantity<Unit> q2 = new Quantity<>(requestDTO.getValue2(), unit2);

        double result = q1.equals(q2) ? 1.0 : 0.0;

        
        if (repository != null) {
            User user = getCurrentUser();
            String email = (user != null) ? user.getEmail() : "Anonymous";
            repository.save(new QuantityRecord(
                    "COMPARE", requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(),
                    requestDTO.getUnit2(), null, result, email));
            logger.info("Saved COMPARE operation to database");
        }

        return result;
    }

    @Override
    public double addQuantities(QuantityRequestDTO requestDTO) {
        logger.info("Processing addition request for {} and {} to target {}", requestDTO.getUnit1(),
                requestDTO.getUnit2(), requestDTO.getTargetUnit());
        Unit unit1 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit1());
        Unit unit2 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit2());
        Unit targetUnitEnum = getUnitEnum(requestDTO.getCategory(), requestDTO.getTargetUnit());

        Quantity<Unit> q1 = new Quantity<>(requestDTO.getValue1(), unit1);
        Quantity<Unit> q2 = new Quantity<>(requestDTO.getValue2(), unit2);

        Quantity<Unit> res = q1.add(q2, targetUnitEnum);
        double result = res.getValue();

        
        if (repository != null) {
            User user = getCurrentUser();
            String email = (user != null) ? user.getEmail() : "Anonymous";
            repository.save(new QuantityRecord(
                    "ADD", requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(), requestDTO.getUnit2(),
                    requestDTO.getTargetUnit(), result, email));
            logger.info("Saved ADD operation to database");
        }

        return result;
    }

    @Override
    public double subtractQuantities(QuantityRequestDTO requestDTO) {
        logger.info("Processing subtraction request for {} and {} to target {}", requestDTO.getUnit1(),
                requestDTO.getUnit2(), requestDTO.getTargetUnit());
        Unit unit1 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit1());
        Unit unit2 = getUnitEnum(requestDTO.getCategory(), requestDTO.getUnit2());
        Unit targetUnitEnum = getUnitEnum(requestDTO.getCategory(), requestDTO.getTargetUnit());

        Quantity<Unit> q1 = new Quantity<>(requestDTO.getValue1(), unit1);
        Quantity<Unit> q2 = new Quantity<>(requestDTO.getValue2(), unit2);

        Quantity<Unit> res = q1.subtract(q2, targetUnitEnum);
        double result = res.getValue();

        
        if (repository != null) {
            User user = getCurrentUser();
            String email = (user != null) ? user.getEmail() : "Anonymous";
            repository.save(new QuantityRecord(
                    "SUBTRACT", requestDTO.getValue1(), requestDTO.getUnit1(), requestDTO.getValue2(),
                    requestDTO.getUnit2(), requestDTO.getTargetUnit(), result, email));
            logger.info("Saved SUBTRACT operation to database");
        }

        return result;
    }
}
