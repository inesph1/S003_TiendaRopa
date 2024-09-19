package com.ipartek.servicios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.ipartek.modelo.Categoria;
import com.ipartek.modelo.Genero;
import com.ipartek.modelo.Producto;
import com.ipartek.modelo.Rol;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.GeneroRepo;
import com.ipartek.repositorio.ProductosRepo;
import com.ipartek.repositorio.RolRepo;
import com.ipartek.repositorio.UsuariosRepo;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class InicializarDatos {
	
	@Autowired
	private CategoriaRepo repoCategoria;
	@Autowired
	private GeneroRepo repoGenero;
	@Autowired
	private UsuariosRepo repoUsuario;
	@Autowired 
	private ProductosRepo repoProducto;
	@Autowired 
	private RolRepo repoRol;
	
	@PostConstruct //se ejecuta cuando detectaun cambio en el pom
	@Transactional //guarda en memoria y cuando indicas hace el commit y rollback si falla uno lo anterior no se guarda	
//restaura desde la copia de seguridad
	public void inicializarDatos() {
		
		String filePath = "C:\\Users\\html\\Desktop\\backup.txt";
        Gson gson = new Gson();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			Type listType = new TypeToken<List<JsonElement>>() {}.getType(); //OJO A LOS IMPORTS
			List<JsonElement> objetos = gson.fromJson(reader, listType);
			
			for (JsonElement elemento : objetos) {
				  if (elemento.isJsonObject()) {
					  JsonObject jsonObject = elemento.getAsJsonObject(); //obtener como objeto json
		              if(jsonObject.has("nombre")) {
		            	  // System.out.println("ES PRODUCTO");
		            	  Producto prod = gson.fromJson(elemento, Producto.class);
		            	  repoProducto.save(prod);
		              }else if(jsonObject.has("categoria")) {
		            	  //System.out.println("ES CATEGORIA");
		            	  Categoria cat = gson.fromJson(elemento, Categoria.class);
		            	  repoCategoria.save(cat);
		              }else if(jsonObject.has("genero")) {
		            	  //System.out.println("ES GENERO");
		            	  Genero genero = gson.fromJson(elemento, Genero.class);
		            	  repoGenero.save(genero);
		              }else if(jsonObject.has("usuario")) {
		            	 // System.out.println("ES USUARIO");        
		            	  Usuario user = gson.fromJson(jsonObject, Usuario.class);
		            	  repoUsuario.save(user);
		              }else if(jsonObject.has("rol")) {
		            	  //System.out.println("ES ROL");
		            	  Rol rol = gson.fromJson(jsonObject, Rol.class);
		            	  repoRol.save(rol);
		              }
				  }
			}
			
			
			//leer archivo csv
			//obtener los objetos, segun las instancia guardarlos en una tabla u otra
		} catch (JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*repoGenero.save(new Genero(4,"Unisex"));
		repoGenero.save(new Genero(5,"Infantil niño"));
		repoGenero.save(new Genero(6,"Infantil niña"));
		
		repoCategoria.save(new Categoria(0, "Shorts"));
		repoCategoria.save(new Categoria(0, "Blazers"));
		repoCategoria.save(new Categoria(0, "Vestidos"));*/
				
	}
	

}
