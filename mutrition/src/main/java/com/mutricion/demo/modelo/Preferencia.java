package com.mutricion.demo.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Preferencia")
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PreferenciaID")
    private int id;
    @Column(name = "Descripcion")
    private String descripcion;
}
