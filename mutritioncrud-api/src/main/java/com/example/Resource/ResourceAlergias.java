package com.example.Resource;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import com.example.modelo.Alergia;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import com.example.repositorio.AlergiaRepositorio;


@RestController
public class ResourceAlergias {
	@Autowired
	private AlergiaRepositorio repoAlergias;

	@GetMapping("/getAlergias")
	public Response getAllRoles(){
		List<Alergia> lista = repoAlergias.findAll();
		Response res;
        if (lista.isEmpty()){
            res= Response.status(Response.Status.NOT_FOUND).build();
        }
        else{        
            res= Response.ok().entity(lista).build();
        }
        return res;
	}
	
    @GetMapping("getAlergia/{alergiaStr}")
	public Response getRole(@PathVariable String alergia){
		Response res;
        Alergia newAlergia= repoAlergias.findByDescripcion(alergia);
        if (Objects.isNull(newAlergia)){
            res=Response.status(Response.Status.NOT_FOUND).build();
        }
        else{
            res=Response.ok().entity(newAlergia).build();  
        }  
        return res;
	}
}
