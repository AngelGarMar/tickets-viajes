package com.tickets.viajes.dto;

import com.tickets.viajes.models.ViajeStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class ViajeResponse implements Serializable {
    private int id;
    private String  origen;
    private String destino;
    private int lugares;
    private String creado;
    private String cliente;
    private String conductor;
    private ViajeStatus status;
}
