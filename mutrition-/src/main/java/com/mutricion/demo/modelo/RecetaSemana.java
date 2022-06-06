package com.mutricion.demo.modelo;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    public boolean primer_plato;

    public Date fecha;

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


    @Override
    public String toString() {
        return "{" +
            ", primer_plato='" + isPrimer_plato() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }

}