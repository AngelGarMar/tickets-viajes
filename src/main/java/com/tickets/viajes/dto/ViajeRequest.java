package com.tickets.viajes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ViajeRequest implements Serializable {
    @NotNull(message = "El campo origenId no debe estar vacío.")
    private Integer origenId;
    @NotNull(message = "El campo destinoId no debe estar vacío.")
    private Integer destinoId;
    @NotNull(message = "El campo lugares no debe estar vacío.")
    private Integer lugares;
    @NotNull(message = "El campo usuarioId no debe estar vacío.")
    private Integer usuarioId;
    @NotNull(message = "El campo clienteId no debe estar vacío.")
    private Integer clienteId;
    @NotNull(message = "El campo conductorId no debe estar vacío.")
    private Integer conductorId;
}
