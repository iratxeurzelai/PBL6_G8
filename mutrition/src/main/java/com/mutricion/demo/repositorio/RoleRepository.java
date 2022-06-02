package com.mutricion.demo.repositorio;



import com.mutricion.demo.modelo.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
    Role findById(int id);

}
