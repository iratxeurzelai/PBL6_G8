package com.mutricion.demo.repositorio;


import java.util.List;

import com.mutricion.demo.modelo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(int id);
    List<User> findAll();
}