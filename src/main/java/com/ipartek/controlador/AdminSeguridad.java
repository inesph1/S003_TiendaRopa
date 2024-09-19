package com.ipartek.controlador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
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

import ch.qos.logback.core.model.Model;

@Controller
public class AdminSeguridad {
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
	
	
	@RequestMapping("/GuardarCopiaSeguridad")
	public String guardarDatosJsonMode(Model modelo, RedirectAttributes redirectAttributes) {
		List<Object> backup = new ArrayList();
		List<Genero> listadoGeneros = repoGenero.findAll();
		List<Categoria> listadoCategorias = repoCategoria.findAll();
		List<Usuario> listadoUsuarios = repoUsuario.findAll();
		List<Producto> listadoProductos = repoProducto.findAll();
		List<Rol> listadoRoles = repoRol.findAll();
		
		backup.addAll(listadoCategorias);
		backup.addAll(listadoGeneros);
		backup.addAll(listadoProductos);
		backup.addAll(listadoUsuarios);
		backup.addAll(listadoRoles);
		
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 try (FileWriter writer = new FileWriter("C:\\Users\\html\\Desktop\\backup.txt")) {
			gson.toJson(backup, writer);
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		 
		 redirectAttributes.addFlashAttribute("feedback", "Copia de seguridad guardada con exito");
		 return "redirect:/superuser";
	}
}
