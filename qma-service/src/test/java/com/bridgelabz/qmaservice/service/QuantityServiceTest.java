package com.bridgelabz.qmaservice.service;

import com.bridgelabz.qmaservice.client.AuthClient;
import com.bridgelabz.qmaservice.dto.QuantityRequestDTO;
import com.bridgelabz.qmaservice.model.QuantityRecord;
import com.bridgelabz.qmaservice.model.User;
import com.bridgelabz.qmaservice.repository.IQuantityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuantityServiceTest {

    @Mock
    private IQuantityRepository repository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private QuantityService quantityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test@test.com", null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        when(authClient.getUserDetails("test@test.com")).thenReturn(mockUser);
    }

    @Test
    void testConvertQuantity_Length_InchesToFeet() {
        QuantityRequestDTO requestDTO = new QuantityRequestDTO();
        requestDTO.setCategory("LENGTH");
        requestDTO.setValue1(12);
        requestDTO.setUnit1("INCH");
        requestDTO.setTargetUnit("FEET");

        double result = quantityService.convertQuantity(requestDTO);

        assertEquals(1.0, result);
        ArgumentCaptor<QuantityRecord> captor = ArgumentCaptor.forClass(QuantityRecord.class);
        verify(repository, times(1)).save(captor.capture());
    }

    @Test
    void testCompareQuantities_Weight_KilogramToGram() {
        QuantityRequestDTO requestDTO = new QuantityRequestDTO();
        requestDTO.setCategory("WEIGHT");
        requestDTO.setValue1(1);
        requestDTO.setUnit1("KILOGRAM");
        requestDTO.setValue2(1000);
        requestDTO.setUnit2("GRAM");

        double result = quantityService.compareQuantities(requestDTO);

        assertEquals(1.0, result);
        ArgumentCaptor<QuantityRecord> captor = ArgumentCaptor.forClass(QuantityRecord.class);
        verify(repository, times(1)).save(captor.capture());
    }

    @Test
    void testAddQuantities_Volume_GallonAndLitre() {
        QuantityRequestDTO requestDTO = new QuantityRequestDTO();
        requestDTO.setCategory("VOLUME");
        requestDTO.setValue1(1);
        requestDTO.setUnit1("GALLON");
        requestDTO.setValue2(3.78);
        requestDTO.setUnit2("LITRE");
        requestDTO.setTargetUnit("LITRE");

        double result = quantityService.addQuantities(requestDTO);

        assertEquals(7.56, result);
        ArgumentCaptor<QuantityRecord> captor = ArgumentCaptor.forClass(QuantityRecord.class);
        verify(repository, times(1)).save(captor.capture());
    }

    @Test
    void testSubtractQuantities_Length_InchesAndInches() {
        QuantityRequestDTO requestDTO = new QuantityRequestDTO();
        requestDTO.setCategory("LENGTH");
        requestDTO.setValue1(2);
        requestDTO.setUnit1("INCH");
        requestDTO.setValue2(1);
        requestDTO.setUnit2("INCH");
        requestDTO.setTargetUnit("INCH");

        double result = quantityService.subtractQuantities(requestDTO);

        assertEquals(1.0, result);
        ArgumentCaptor<QuantityRecord> captor = ArgumentCaptor.forClass(QuantityRecord.class);
        verify(repository, times(1)).save(captor.capture());
    }
}
