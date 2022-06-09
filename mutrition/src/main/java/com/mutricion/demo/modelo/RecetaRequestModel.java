package com.mutricion.demo.modelo;

import java.util.Set;

public class RecetaRequestModel {
    private Integer id;
    private String title;
    private Set<RecetaSemana> recetaSemana;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Set<RecetaSemana> getRecetaSemana() {
        return this.recetaSemana;
    }

    public void setRecetaSemana(Set<RecetaSemana> recetaSemana) {
        this.recetaSemana = recetaSemana;
    }

}