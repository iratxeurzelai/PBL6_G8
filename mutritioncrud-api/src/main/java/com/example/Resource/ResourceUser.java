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
import com.example.modelo.Preferencia;
import com.example.modelo.Role;
import com.example.modelo.User;
import com.example.modelo.UserParser;
import com.example.modelo.UserRequestModel;
import com.example.repositorio.AlergiaRepositorio;
import com.example.repositorio.PreferenciasRepositorio;
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
	
	@Autowired
	private PreferenciasRepositorio repoPreferencia;

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
	@GetMapping("/getRole/{id}")
	public Response getRole(@PathVariable int id){
		Response res;
		User nuevoUser = repoUser.findById(id);
        Set<Role> roles= nuevoUser.getRoles();
		if (Objects.isNull(roles)){
			res=Response.status(Response.Status.NOT_FOUND).build();
			
		}
		else{
			res=Response.ok().entity(roles).build();  
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
		
        if(userExists==null){
			User user=new User(userParser);
			Set<Role> rolesList=new HashSet<>();
			String rolesStr=userParser.getRoles();
			Role role=repoRoles.findById(Integer.parseInt(rolesStr));
			rolesList.add(role);
			user.setRoles(rolesList);
			
			String alergiasStr = userParser.getAlergias();
			Set<Alergia> alergiasList=new HashSet<>();
			if(!alergiasStr.equals("")) {
				String [] alergiasData=alergiasStr.split("[,]");
				if(alergiasData.length>1){
					for(String a:alergiasData){
						Alergia alergia=repoAlergias.findById(Integer. parseInt(a));
						alergiasList.add(alergia);
					}
				} else alergiasList.add(repoAlergias.findById(Integer.parseInt(alergiasData[0])));
			}
			
			user.setAlergias(alergiasList);
			
			String prefiereStr = userParser.getAlergias();
			Set<Preferencia> prefiereList=new HashSet<>();
			if(!prefiereStr.equals("")) {
				String [] prefiereData=prefiereStr.split("[,]");
				if(prefiereData.length>1){
					for(String a:prefiereData){
						Preferencia preferencia=repoPreferencia.findById(Integer. parseInt(a));
						prefiereList.add(preferencia);
					}
				} else prefiereList.add(repoPreferencia.findById(Integer.parseInt(prefiereData[0])));
			}
			user.setPreferencias(prefiereList);
			
			String noPrefiereStr = userParser.getAlergias();
			Set<Preferencia> noPrefiereList=new HashSet<>();
			if(!noPrefiereStr.equals("")) {
				String [] noPrefiereData=noPrefiereStr.split("[,]");
				if(noPrefiereData.length>1){
					for(String a:noPrefiereData){
						Preferencia preferencia=repoPreferencia.findById(Integer. parseInt(a));
						noPrefiereList.add(preferencia);
					}
				} else noPrefiereList.add(repoPreferencia.findById(Integer.parseInt(noPrefiereData[0])));
			}
			user.setNoPreferencias(noPrefiereList);
			
            User userCreado=repoUser.save(user);
			return Response.ok(userCreado).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
	}
	
	@PutMapping("/updateUserCuenta/{id}")
    public Response updateUserCuenta(@RequestBody UserRequestModel userDTO, @PathVariable int id) {

		User user = new User(userDTO);
        User userUpdated=repoUser.findById(id);
        user.setPassword(userUpdated.getPassword());
        user.setAltura(userUpdated.getAltura());
        user.setPeso(userUpdated.getPeso());
        user.setAlergias(userUpdated.getAlergias());
        user.setPreferencias(userUpdated.getPreferencias());
        user.setNoPreferencias(userUpdated.getNoPreferencias());
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
	
	@PutMapping("/changePassword/{id}")
    public Response updateUserPassword(@RequestBody UserRequestModel userDTO, @PathVariable int id) {
		
		User user = new User(userDTO);
        User userUpdated=repoUser.findById(id);
        user.setName(userUpdated.getName());
        user.setLastname(userUpdated.getLastname());
        user.setCuentaCorriente(userUpdated.getCuentaCorriente());
        user.setAltura(userUpdated.getAltura());
        user.setPeso(userUpdated.getPeso());
        user.setAlergias(userUpdated.getAlergias());
        user.setPreferencias(userUpdated.getPreferencias());
        user.setNoPreferencias(userUpdated.getNoPreferencias());
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
	
	@PutMapping("/updateUserVip/{id}")
    public Response updateUserVip(@RequestBody UserRequestModel userDTO, @PathVariable int id) {

		User user = new User(userDTO);
        User userUpdated=repoUser.findById(id);
        user.setName(userUpdated.getName());
        user.setLastname(userUpdated.getLastname());
        user.setPassword(userUpdated.getPassword());
        user.setAltura(userUpdated.getAltura());
        user.setPeso(userUpdated.getPeso());
        user.setAlergias(userUpdated.getAlergias());
        user.setPreferencias(userUpdated.getPreferencias());
        user.setNoPreferencias(userUpdated.getNoPreferencias());
        user.setSexo(userUpdated.getSexo());
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
	
	@PutMapping("/updateUserDatosPer/{id}")
    public Response updateUserDatosPer(@RequestBody UserRequestModel userDTO, @PathVariable int id) {
        
		User user = new User(userDTO);
		User userUpdated=repoUser.findById(id);
        user.setName(userUpdated.getName());
        user.setLastname(userUpdated.getLastname());
        user.setPassword(userUpdated.getPassword());
        user.setRoles(userUpdated.getRoles());
        user.setEmail(userUpdated.getEmail());
        user.setSecret(userUpdated.getSecret());
        user.setPreferencias(userUpdated.getPreferencias());
        user.setNoPreferencias(userUpdated.getNoPreferencias());

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
