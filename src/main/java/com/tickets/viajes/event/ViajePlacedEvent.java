package com.tickets.viajes.event;

public record ViajePlacedEvent(
        Integer viajeId,
        Integer clienteId,
        Integer usuarioId,
        String costo
) {

}
