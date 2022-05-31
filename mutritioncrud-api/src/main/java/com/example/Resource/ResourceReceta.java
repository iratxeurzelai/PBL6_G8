package com.example.Resource;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Receta;
import com.example.repositorio.RecetaRepositorio;



@RestController
public class ResourceReceta {
	
	@Autowired
	private RecetaRepositorio repoReceta;

	@GetMapping("/getRecetas")
	public Response getAllUsers(){
		List <Receta> lista=repoReceta.findAll();
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
