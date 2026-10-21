package com.tickets.viajes.clients;

import com.tickets.viajes.models.ParaderosRequest;
import com.tickets.viajes.models.ResponseService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ParaderoClient {
    @PostExchange("/api/tickets/paraderos")
    ResponseService existParaderos(@RequestBody ParaderosRequest request);
}
