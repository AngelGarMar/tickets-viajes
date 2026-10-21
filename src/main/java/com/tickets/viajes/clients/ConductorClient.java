package com.tickets.viajes.clients;

import com.tickets.viajes.models.ResponseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ConductorClient {
    @GetExchange("/api/tickets/drivers/{conductorId}")
    ResponseService getConductor(@PathVariable Integer conductorId);
}
