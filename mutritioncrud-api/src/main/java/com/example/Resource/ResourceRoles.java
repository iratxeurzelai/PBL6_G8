package com.example.Resource;

import java.util.List;

import javax.ws.rs.core.Response;

import com.example.modelo.Role;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.repositorio.RoleRepositorio;

@RestController
public class ResourceRoles {
	@Autowired
	private RoleRepositorio repoRoles;

	@GetMapping("/getRoles")
	public Response getAllRoles(){
		List<Role> lista=repoRoles.findAll();
		Response res;
        if (lista.isEmpty()){
            res= Response.status(Response.Status.NOT_FOUND).build();
        }
        else{        
            res= Response.ok().entity(lista).build();
        }
        return res;
	}
}
