package com.ipartek.servicios;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.ipartek.modelo.Categoria;
import com.ipartek.modelo.Genero;
import com.ipartek.modelo.Producto;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.GeneroRepo;
import com.ipartek.repositorio.ProductosRepo;
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
	
	@PostConstruct //se ejecuta cuando detectaun cambio en el pom
	@Transactional //guarda en memoria y cuando indicas hace el commit y rollback si falla uno lo anterior no se guarda
	
//restaura desde la copia de seguridad
	public void inicializarDatos() {
		
		//leer archivo csv
		/*repoGenero.save(new Genero(4,"Unisex"));
		repoGenero.save(new Genero(5,"Infantil niño"));
		repoGenero.save(new Genero(6,"Infantil niña"));
		
		repoCategoria.save(new Categoria(0, "Shorts"));
		repoCategoria.save(new Categoria(0, "Blazers"));
		repoCategoria.save(new Categoria(0, "Vestidos"));*/
				
	}
	
	@PostConstruct 
	@Transactional
	public void guardarDatosJson() {
		List<Object> backup = new ArrayList();
		List<Genero> listadoGeneros = repoGenero.findAll();
		List<Categoria> listadoCategorias = repoCategoria.findAll();
		List<Usuario> listadoUsuarios = repoUsuario.findAll();
		List<Producto> listadoProductos = repoProducto.findAll();
		
		backup.addAll(listadoCategorias);
		backup.addAll(listadoGeneros);
		backup.addAll(listadoProductos);
		backup.addAll(listadoUsuarios);
		
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 try (FileWriter writer = new FileWriter("C:\\Users\\html\\Desktop\\backup.txt")) {
			gson.toJson(backup, writer);
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
