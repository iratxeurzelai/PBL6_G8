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
public class Finde implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long findeid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recetaid")
    private Receta receta;

    public Date fecha;

    public Finde(User user, Receta receta, Date fecha) {
        this.user = user;
        this.fecha = fecha;
        this.receta = receta;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Finde)) return false;
        Finde that = (Finde) o;
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
            " findeid='" + getFindeid() + "'" +
            ", user='" + getUser() + "'" +
            ", receta='" + getReceta() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}