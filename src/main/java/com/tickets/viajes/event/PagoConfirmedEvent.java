package com.tickets.viajes.event;

import lombok.Builder;

@Builder
public record PagoConfirmedEvent(Integer viajeId) {
}
