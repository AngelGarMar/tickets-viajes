package com.tickets.viajes.services;

import com.tickets.viajes.dto.ViajeRequest;
import com.tickets.viajes.models.ResponseService;
import com.tickets.viajes.models.ViajeStatus;

public interface ViajeService {
    ResponseService createViaje(ViajeRequest request, String authorization, String userId, String userRoles);
    void updateViaje(Integer idViaje, ViajeStatus status);
}
