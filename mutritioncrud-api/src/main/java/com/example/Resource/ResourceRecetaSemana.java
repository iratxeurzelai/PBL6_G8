package com.example.Resource;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.RecetaSemana;
import com.example.repositorio.RecetaSemanaRepositorio;

@RestController
public class ResourceRecetaSemana {

	@Autowired
	private RecetaSemanaRepositorio repoReceta;
	
	@GetMapping("/getRecetaSemana/{id}")
	public Response getAllUsers(@PathVariable int id){
		
		System.out.println("HA ENTRAUUUU");
		
		List <RecetaSemana> lista=repoReceta.findByUserId(id);
		
		List<RecetaSemana> listaFiltrada = lista.subList(Math.max(lista.size() - 10, 0), lista.size());
		
		Response res;
		if (lista.isEmpty()){
			res= Response.status(Response.Status.NOT_FOUND).build();
		}
		else{        
			res= Response.ok().entity(listaFiltrada).build();
		}
		return res;
	}
}
