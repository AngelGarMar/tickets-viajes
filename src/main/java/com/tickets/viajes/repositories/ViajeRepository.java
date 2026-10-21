package com.tickets.viajes.repositories;

import com.tickets.viajes.entities.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
}
