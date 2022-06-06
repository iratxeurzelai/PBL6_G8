package com.mutricion.demo.modelo;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class ChangePasswordForm {
    private Integer id;

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String actPassword;

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String newPassword;

    public ChangePasswordForm(){}
    public ChangePasswordForm(Integer id) {this.id=id;}

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id=id;
    }
    public String getActPassword() {
        return this.actPassword;
    }
    public void setActPassword(String actPassword) {
        this.actPassword=actPassword;
    }    
    public String getNewPassword() {
        return this.newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword=newPassword;
    } 
}
