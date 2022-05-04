package com.mutricion.demo.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserID")
    private Integer id;
    @Column(name = "Username")
    @Length(min = 5, message = "*Your user name must have at least 5 characters")
    @NotEmpty(message = "*Please provide a username")
    private String username;
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
    @Column(name = "active")
    private Boolean active;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "RoleID", joinColumns = @JoinColumn(name = "UserID"), inverseJoinColumns = @JoinColumn(name = "RoleID"))
    private Set<Role> roles;

}