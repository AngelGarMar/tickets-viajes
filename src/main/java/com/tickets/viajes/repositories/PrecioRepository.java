package com.tickets.viajes.repositories;

import com.tickets.viajes.entities.Precio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecioRepository extends JpaRepository<Precio, Integer> {
}
