package com.tickets.viajes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "boletos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer numero;
    private Timestamp creado;
    private Timestamp modificado;
    @NotNull
    private Integer estatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viajes_id")
    private Viaje viaje;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "precios_id")
    private Precio precio;
}
