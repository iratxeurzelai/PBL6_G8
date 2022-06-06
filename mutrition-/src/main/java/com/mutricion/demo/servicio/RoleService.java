package com.mutricion.demo.servicio;

import java.util.List;


import com.mutricion.demo.modelo.Role;
import com.mutricion.demo.repositorio.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    
    @Autowired
    RoleRepository roleRepositorio;

    public List<Role> buscarRoles(){
        return roleRepositorio.findAll();
    }

    public Role buscarRoleId(int id){
        return roleRepositorio.findById(id);
    }

    public Role buscarRoleDescripcion(String descripcion){
        return roleRepositorio.findByRole(descripcion);
    }
}
