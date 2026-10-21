package com.tickets.viajes.entities;

import com.tickets.viajes.models.ViajeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "viajes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer origenId;
    @NotNull
    private Integer destinoId;
    @NotNull
    private Integer lugares;
    private Timestamp creado;
    private Timestamp modificado;
    @NotNull
    private Integer estatus;
    @NotNull
    private Integer usuarioId;
    @NotNull
    private Integer clienteId;
    @NotNull
    private Integer conductorId;
    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Boleto> boletos;
    @Enumerated(EnumType.STRING)
    private ViajeStatus status;
}
