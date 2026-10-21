package com.tickets.viajes.controllers;

import com.tickets.viajes.constants.Constants;
import com.tickets.viajes.dto.ViajeRequest;
import com.tickets.viajes.models.ResponseService;
import com.tickets.viajes.services.ViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets/viajes")
public class ViajeController {
    @Autowired
    private ViajeService viajeService;

    @PostMapping
    public ResponseEntity<ResponseService> createViaje(
            @RequestBody @Valid ViajeRequest viaje,
            BindingResult result,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String userRoles) {
        if (result.hasFieldErrors()) return validation(result);
        ResponseService response = viajeService.createViaje(viaje, authorization, userId, userRoles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<ResponseService> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return new ResponseEntity<>(new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.MISSED_INFORMATION, errors), HttpStatus.OK);
    }
}
