package com.example.modelo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "Receta")
public class Receta implements Serializable{
    private static final long serialVersionUID = 1L;

    public Receta(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recetaid")
    private Integer id;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RecetaSemana> recetaSemana;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<RecetaSemana> getRecetaSemana() {
		return recetaSemana;
	}
	public void setRecetaSemana(Set<RecetaSemana> recetaSemana) {
		this.recetaSemana = recetaSemana;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
}
