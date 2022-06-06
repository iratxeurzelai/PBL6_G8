package com.example.modelo;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class RecetaSemana implements Serializable{  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recetasemanaid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recetaid")
    private Receta receta;

    private Date fecha;
    
    public boolean primer_plato;
    
    public RecetaSemana(){}
    	

    public RecetaSemana(User user, Receta receta, Date fecha) {
        this.user = user;
        this.fecha = fecha;
        this.receta = receta;
    }

 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecetaSemana)) return false;
        RecetaSemana that = (RecetaSemana) o;
        return Objects.equals(receta.getId(), that.receta.getId()) &&
                Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receta.getId(), fecha);
    }


	public Long getRecetasemanaid() {
		return recetasemanaid;
	}


	public void setRecetasemanaid(Long recetasemanaid) {
		this.recetasemanaid = recetasemanaid;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Receta getReceta() {
		return receta;
	}


	public void setReceta(Receta receta) {
		this.receta = receta;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
    
    
}
