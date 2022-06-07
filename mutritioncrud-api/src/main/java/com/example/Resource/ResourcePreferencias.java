package com.example.Resource;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Preferencia;
import com.example.repositorio.PreferenciasRepositorio;

@RestController
public class ResourcePreferencias {
	@Autowired
	private PreferenciasRepositorio repoPreferencias;

	@GetMapping("/getPreferencias")
	public Response getAllRoles(){
		List<Preferencia> lista = repoPreferencias.findAll();
		for(Preferencia p : lista){
	        System.err.println("Descripcion " + p.getDescripcion());
	    }
		Response res;
        if (lista.isEmpty()){
            res= Response.status(Response.Status.NOT_FOUND).build();
        }
        else{        
            res= Response.ok().entity(lista).build();
        }
        return res;
	}
	
    @GetMapping("getPreferencia/{preferenciaStr}")
	public Response getRole(@PathVariable String preferencia){
		Response res;
		Preferencia newPreferencia= repoPreferencias.findByDescripcion(preferencia);
        if (Objects.isNull(newPreferencia)){
            res=Response.status(Response.Status.NOT_FOUND).build();
        }
        else{
            res=Response.ok().entity(newPreferencia).build();  
        }  
        return res;
	}
}
