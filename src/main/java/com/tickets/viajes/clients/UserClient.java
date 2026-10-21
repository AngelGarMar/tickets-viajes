package com.tickets.viajes.clients;

import com.tickets.viajes.models.ResponseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/tickets/users/{userId}")
    ResponseService getUser(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader("X-User-Roles") String userRoles);
}
