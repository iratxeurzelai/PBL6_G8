package com.mutricion.demo.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.jboss.aerogear.security.otp.api.Base32;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "secret")
    private String secret;

    public User() {
        super();
        this.secret = Base32.random();
    }

    public User (UserForm parser){
        super();
        this.password = parser.getPassword();
        this.name=parser.getName();
        this.lastname = parser.getLastname();
        this.email=parser.getEmail();
        this.sexo=parser.getSexo();
        this.peso = parser.getPeso();
        this.altura = parser.getAltura();
        this.secret = Base32.random();
        this.cuentaCorriente = parser.getCuentaCorriente();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserID")
    private Integer id;
    @Column(name = "Password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "First_name")
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "Last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastname;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "sexo")
    private Boolean sexo;
    @Column(name = "peso")
    private float peso;
    @Column(name = "altura")
    private float altura;
    @Column(name = "cuentaCorriente")
    private String cuentaCorriente;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "RoleID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "RoleID"))
    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AlergiaID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "AlergiaID"))
    private Set<Alergia> alergias;
    @OneToMany(mappedBy = "receta", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<RecetaSemana> recetaSemana;
}