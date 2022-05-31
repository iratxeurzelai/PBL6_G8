package com.example.repositorio;

import com.example.modelo.User;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositorio extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByName(String name);
    User findById(Integer id);
}