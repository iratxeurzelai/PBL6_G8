package com.example.modelo;

import java.util.Set;

public class UserRequestModel {
	private Integer id;
	private String password;
	private String name;
	private String lastname;
	private String email;
	private Boolean sexo;
	private float peso;
	private float altura;
	private float cuentaCorriente;
	private Set<Role> roles;
	private Set<Alergia> alergias;
	private Set<Preferencia> prefiere;
	Set<Preferencia> noprefiere;
	private Set<RecetaSemana> recetaSemana;
	private String secret;
	
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
	public float getCuentaCorriente() {
		return cuentaCorriente;
	}
	public void setCuentaCorriente(float cuentaCorriente) {
		this.cuentaCorriente = cuentaCorriente;
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
	public Set<RecetaSemana> getRecetaSemana() {
		return recetaSemana;
	}
	public void setRecetaSemana(Set<RecetaSemana> recetaSemana) {
		this.recetaSemana = recetaSemana;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
}
