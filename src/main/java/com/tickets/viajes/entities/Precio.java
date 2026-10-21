package com.tickets.viajes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "precios")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String descripcion;
    @NotBlank
    private String precio;
    @NotNull
    private Integer estatus;
    private Timestamp creado;
    private Timestamp modificado;
}
