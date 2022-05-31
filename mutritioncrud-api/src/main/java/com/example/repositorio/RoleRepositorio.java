package com.example.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.Role;

@Repository
public interface RoleRepositorio extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
    Role findById(int id);
    List<Role> findAll();
}
