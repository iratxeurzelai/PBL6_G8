package com.mutricion.demo.modelo;

<<<<<<< HEAD
public class UserForm {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private Boolean sexo;
    private float peso;
    private float altura;
    private String rolesStr;
    private String alergiasStr;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getPeso() {
        return peso;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getAltura() {
        return altura;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }
=======

public class UserForm {
    private static final long serialVersionUID = 1L;

    private String name;
    private String password;
    private String username;
    private String lastname;
    private String email;
    private float peso;
    private float altura;
    private Boolean active;

>>>>>>> db6cca3bb5613cdb9d0d3d1dfbd5851e30ddccfe

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

<<<<<<< HEAD
    public void setname(String name) {
=======
    public void setName(String name) {
>>>>>>> db6cca3bb5613cdb9d0d3d1dfbd5851e30ddccfe
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

<<<<<<< HEAD
    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getRolesStr() {
        return rolesStr;
    }

    public void setRolesStr(String rolesStr) {
        this.rolesStr = rolesStr;
    }

    public String getAlergiasStr() {
        return alergiasStr;
    }

    public void setAlergiasStr(String alergiasStr) {
        this.alergiasStr = alergiasStr;
=======
    public String getUsername() {
        return username;
    }

    public void sertUsername(String username) {
        this.username = username;
    }

    public Boolean active() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
>>>>>>> db6cca3bb5613cdb9d0d3d1dfbd5851e30ddccfe
    }
}
