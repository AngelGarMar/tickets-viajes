package com.tickets.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cliente {
    private int id;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private int edad;
    private String celular;
    private String tarjeta;
    private String insen;
    private String creado;
    private String modificado;
    private int estatus;
    private int puntos;
}
