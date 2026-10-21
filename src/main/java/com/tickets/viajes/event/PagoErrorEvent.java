package com.tickets.viajes.event;

public record PagoErrorEvent(Integer viajeId, String reason) {
}
