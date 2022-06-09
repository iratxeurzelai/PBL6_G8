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
	        this.password = parser.getPassword();
	        this.name=parser.getName();
	        this.lastname = parser.getLastname();
	        this.email=parser.getEmail();
	        this.sexo=parser.getSexo();
	        this.peso = parser.getPeso();
	        this.altura = parser.getAltura();
	        this.secret = Base32.random();
	    }
	    
	    public User (UserRequestModel user){
	        super();
	        this.password = user.getPassword();
	        this.name=user.getName();
	        this.lastname = user.getLastname();
	        this.email=user.getEmail();
	        this.sexo=user.getSexo();
	        this.peso = user.getPeso();
	        this.altura = user.getAltura();
	        this.secret = user.getSecret();
	        this.alergias = user.getAlergias();
	        this.prefiere = user.getPrefiere();
	        this.noprefiere = user.getNoprefiere();
	        this.recetaSemana = user.getRecetaSemana();
	        this.roles = user.getRoles();
	        this.cuentaCorriente = user.getCuentaCorriente();
	        this.id = user.getId();
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
	    private float cuentaCorriente;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "RoleID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "RoleID"))
	    private Set<Role> roles;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "AlergiaID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "AlergiaID"))
	    private Set<Alergia> alergias;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "PreferenciaID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "PreferenciaID"))
	    private Set<Preferencia> prefiere;
	    @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(
	        name = "NoPrefiere", 
	        joinColumns = { @JoinColumn(name = "UserID") }, 
	        inverseJoinColumns = { @JoinColumn(name = "PrefiereID") }
	    )
	    Set<Preferencia> noprefiere;
	    
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

		public float getCuentaCorriente() {
			return cuentaCorriente;
		}

		public void setCuentaCorriente(float cuentaCorriente) {
			this.cuentaCorriente = cuentaCorriente;
		}

		public Set<Preferencia> getPreferencias() {
			return prefiere;
		}

		public void setPreferencias(Set<Preferencia> prefiere) {
			this.prefiere = prefiere;
		}

		public Set<Preferencia> getNoPreferencias() {
			return noprefiere;
		}

		public void setNoPreferencias(Set<Preferencia> noprefiere) {
			this.noprefiere = noprefiere;
		}

		public Set<RecetaSemana> getRecetaSemana() {
			return recetaSemana;
		}

		public void setRecetaSemana(Set<RecetaSemana> recetaSemana) {
			this.recetaSemana = recetaSemana;
		}

		public Set<Preferencia> getPrefiere() {
			return prefiere;
		}

		public void setPrefiere(Set<Preferencia> prefiere) {
			this.prefiere = prefiere;
		}

		public Set<Preferencia> getNoprefiere() {
			return noprefiere;
		}

		public void setNoprefiere(Set<Preferencia> noprefiere) {
			this.noprefiere = noprefiere;
		}
		
}
