package com.tickets.viajes.repositories;

import com.tickets.viajes.entities.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
}
