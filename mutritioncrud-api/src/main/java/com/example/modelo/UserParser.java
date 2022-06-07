package com.example.modelo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder = {"password", "name", "lastname", "email", "sexo", "peso", "altura", "roles", "alergias" })
public class UserParser {

    //private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private Boolean sexo;
    private float peso;
    private float altura;
    private String roles;
    private String alergias;
    private String preferenciaStr;
    private String noPrefiereStr;

    public UserParser() {
    }

    public UserParser(String password, String name, String lastname, String email, Boolean sexo, float peso, float altura) {
       // this.username = username;
        this.password = password;
        this.name = name;
        this.lastname =lastname;
        this.email = email;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
    }

	/*public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}*/

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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}
    public String getPreferenciaStr() {
        return this.preferenciaStr;
    }

    public void setPreferenciaStr(String preferenciaStr) {
        this.preferenciaStr = preferenciaStr;
    }

    public String getNoPrefiereStr() {
        return this.noPrefiereStr;
    }

    public void setNoPrefiereStr(String noPrefiereStr) {
        this.noPrefiereStr = noPrefiereStr;
    }
    
}
