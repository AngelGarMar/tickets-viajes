package com.tickets.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehiculo {
    private Integer id;
    private String nombre;
    private String creado;
    private int estatus;
}
