package com.tickets.viajes.listener;

import com.tickets.viajes.event.ViajePlacedEvent;
import com.tickets.viajes.models.ViajeStatus;
import com.tickets.viajes.services.ViajeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViajeEventListener {
    private final ViajeService viajeService;

    @RabbitListener(queues = "viaje-confirmed-queue")
    public void handleViajeConfirmed(ViajePlacedEvent event) {
        viajeService.updateViaje(event.viajeId(), ViajeStatus.CONFIRMADO);
    }

    @RabbitListener(queues = "viaje-cancelled-queue")
    public void handleViajeCancelled(ViajePlacedEvent event) {
        viajeService.updateViaje(event.viajeId(), ViajeStatus.CANCELADO);
    }
}
