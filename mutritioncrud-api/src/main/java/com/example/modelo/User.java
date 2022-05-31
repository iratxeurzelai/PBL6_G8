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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.jboss.aerogear.security.otp.api.Base32;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

	    public User (UserParser parser){
	        super();
	        //this.username = parser.getUsername();
	        this.password = parser.getPassword();
	        this.name=parser.getName();
	        this.lastname = parser.getLastname();
	        this.email=parser.getEmail();
	        this.sexo=parser.getSexo();
	        this.peso = parser.getPeso();
	        this.altura = parser.getAltura();
	        this.secret = Base32.random();
	    }

	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "UserID")
	    private Integer id;
	    /*@Column(name = "Username")
	    @Length(min = 5, message = "*Your user name must have at least 5 characters")
	    @NotEmpty(message = "*Please provide a username")
	    private String username;*/
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
	    //@NotEmpty(message = "*Por favor inserte el peso")
	    private float peso;
	    @Column(name = "altura")
	    //@NotEmpty(message = "*Por favor inserte la altura")
	    private float altura;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "RoleID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "RoleID"))
	    private Set<Role> roles;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "AlergiaID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "AlergiaID"))
	    private Set<Alergia> alergias;
	    @OneToMany(mappedBy = "receta", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	    private Set<RecetaSemana> recetaSemana;

		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLastname() {
			return lastname;
		}
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Boolean getSexo() {
			return sexo;
		}
		public void setSexo(Boolean sexo) {
			this.sexo = sexo;
		}
		public float getPeso() {
			return peso;
		}
		public void setPeso(float peso) {
			this.peso = peso;
		}
		public float getAltura() {
			return altura;
		}
		public void setAltura(float altura) {
			this.altura = altura;
		}
		
		public Set<Role> getRoles() {
			return roles;
		}
		public void setRoles(Set<Role> roles) {
			this.roles = roles;
		}
		public Set<Alergia> getAlergias() {
			return alergias;
		}
		public void setAlergias(Set<Alergia> alergias) {
			this.alergias = alergias;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		

}
