package com.tickets.viajes.clients;

import com.tickets.viajes.models.ResponseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ClienteClient {
    @GetExchange("/api/tickets/clients/{idCliente}")
    ResponseService getCliente(@PathVariable Integer idCliente);
}
