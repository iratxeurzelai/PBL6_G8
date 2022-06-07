package com.example.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.User;

@Repository
public interface UserRepositorio extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByName(String name);
    User findById(Integer id);
}