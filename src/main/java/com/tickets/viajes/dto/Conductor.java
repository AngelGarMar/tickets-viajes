package com.tickets.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conductor {
    private Integer id;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private Integer edad;
    private String celular;
    private Integer estatus;
    private String creado;
    private String modificado;
    private Vehiculo vehiculo;
}
