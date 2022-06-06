package com.example.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Alergia;
import com.example.modelo.Role;
import com.example.modelo.User;
import com.example.modelo.UserParser;
import com.example.repositorio.AlergiaRepositorio;
import com.example.repositorio.RoleRepositorio;
import com.example.repositorio.UserRepositorio;

@RestController
public class ResourceUser {
	@Autowired
	private UserRepositorio repoUser;
	
	@Autowired
	private RoleRepositorio repoRoles;
	
	@Autowired
	private AlergiaRepositorio repoAlergias;

	@GetMapping("/getUsers")
	public Response getAllUsers(){
		List <User> lista=repoUser.findAll();
		Response res;
		if (lista.isEmpty()){
			res= Response.status(Response.Status.NOT_FOUND).build();
		}
		else{        
			res= Response.ok().entity(lista).build();
		}
		return res;
	}
	@GetMapping("/getUser/{id}")
	public Response getUser(@PathVariable int id){
		Response res;
        User nuevoUser= repoUser.findById(id);
		if (Objects.isNull(nuevoUser)){
			res=Response.status(Response.Status.NOT_FOUND).build();
			
		}
		else{
			res=Response.ok().entity(nuevoUser).build();  
		}  
		return res;
	}
	@GetMapping("/login/{correo}")
	public Response loginUser(@PathVariable String correo) {
		Response res;
		User user=repoUser.findByEmail(correo);
		if(Objects.isNull(user)) {
			res=Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			res=Response.ok().entity(user.getPassword()).build();
			
		}
		return res;
	}

	@PostMapping("/addUser")
	public Response createUser(@RequestBody UserParser userParser){
		User userExists=repoUser.findByEmail(userParser.getEmail());
		System.out.println("las alergias sonnn " + userParser.getAlergias());
        if(userExists==null){
			User user=new User(userParser);
			Set<Role> rolesList=new HashSet<>();
			String rolesStr=userParser.getRoles();
			Role role=repoRoles.findByRole(rolesStr);
			rolesList.add(role);
			user.setRoles(rolesList);
			
			String alergiasStr = userParser.getAlergias();
			Set<Alergia> alergiasList=new HashSet<>();
			String [] alergiasData=alergiasStr.split("[,]");
			if(alergiasData.length>1){
				for(String a:alergiasData){
					Alergia alergia=repoAlergias.findById(Integer. parseInt(a));
					System.out.println("el id de la alergia esss " + alergia.getId());
					alergiasList.add(alergia);
				}
			} else alergiasList.add(repoAlergias.findById(Integer.parseInt(alergiasData[0])));
			
			user.setAlergias(alergiasList);
			
            User userCreado=repoUser.save(user);
			return Response.ok(userCreado).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
	}
	
	@PutMapping("/updateUserCuenta/{id}")
	public Response updateUserCuenta(@RequestBody User user, @PathVariable int id) {
		
		User userUpdated=repoUser.findById(id);
		user.setPassword(userUpdated.getPassword());
		user.setAltura(userUpdated.getAltura());
		user.setPeso(userUpdated.getPeso());
		user.setAlergias(userUpdated.getAlergias());
		user.setSexo(userUpdated.getSexo());
		user.setRoles(userUpdated.getRoles());
		user.setEmail(userUpdated.getEmail());
		user.setSecret(userUpdated.getSecret());
		
		Response ret= null;
		if(Objects.isNull(userUpdated)) {
			ret=Response.status(Response.Status.NOT_FOUND).build();
		}else {
			user.setId(id);
			repoUser.save(user);
			ret= Response.ok().build();
		}
		
		return ret;
	}
	
	@PutMapping("/updateUserPassword/{id}")
	public Response updateUserPassword(@RequestBody User user, @PathVariable int id) {
		
		User userUpdated=repoUser.findById(id);
		user.setPassword(userUpdated.getPassword());
		user.setAltura(userUpdated.getAltura());
		user.setPeso(userUpdated.getPeso());
		user.setAlergias(userUpdated.getAlergias());
		user.setSexo(userUpdated.getSexo());
		//user.setRoles(userUpdated.getRoles());
		user.setEmail(userUpdated.getEmail());
		user.setSecret(userUpdated.getSecret());
		
		Response ret= null;
		if(Objects.isNull(userUpdated)) {
			ret=Response.status(Response.Status.NOT_FOUND).build();
		}else {
			user.setId(id);
			repoUser.save(user);
			ret= Response.ok().build();
		}
		
		
		return ret;
	}
	
	@PutMapping("/updateUserVip/{id}")
	public Response updateUserVip(@RequestBody User user, @PathVariable int id) {
		
		User userUpdated=repoUser.findById(id);
		user.setName(userUpdated.getName());
		user.setLastname(userUpdated.getLastname());
		user.setPassword(userUpdated.getPassword());
		user.setAltura(userUpdated.getAltura());
		user.setPeso(userUpdated.getPeso());
		user.setAlergias(userUpdated.getAlergias());
		user.setSexo(userUpdated.getSexo());
		//user.setRoles(userUpdated.getRoles());
		user.setEmail(userUpdated.getEmail());
		user.setSecret(userUpdated.getSecret());
		
		Response ret= null;
		if(Objects.isNull(userUpdated)) {
			ret=Response.status(Response.Status.NOT_FOUND).build();
		}else {
			user.setId(id);
			repoUser.save(user);
			ret= Response.ok().build();
		}
		
		
		return ret;
	}
	
	
	@DeleteMapping("/deleteUser/{id}")
    public Response deleteUser(@PathVariable int id) {
        User userToDelete= repoUser.findById(id);
        Response res;
        if (Objects.isNull(userToDelete)){ 
        	res= Response.status(Response.Status.NOT_FOUND).build();

        } else {
        	repoUser.delete(userToDelete);
        	res=Response.ok().build();
        }
        
        return res; 
        
	}

}
