package com.javatechie.rabbitmq.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {

    private String usernameId;
    private String nombre;
    private String apellido;
    private double peso;
    private double altura;
    private String dieta;
    private String actividad;
    private String sexo;
    private String edad;
    
}
