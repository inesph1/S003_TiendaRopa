package com.ipartek.controlador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import com.ipartek.servicios.InicializarDatos;

import ch.qos.logback.core.model.Model;

@Controller //necesario para usar inicializarDatos
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
	
	@Autowired
	private InicializarDatos datosInicio; //IMPORTANTE A LA HORA DE PASAR EL BACKUP_OLD
	
	
	@RequestMapping("/GuardarCopiaSeguridad")
	public String guardarDatosJsonMode(Model modelo, RedirectAttributes redirectAttributes) {
			
		//SE PUEDE EXTRAER COMO OBJ (NO ES UNA BUENA PRACTICA HACER LISTA DE OBJECTS) Y PASARLE INDIVIDUALMENTE 
		//POR PARAMETROS LOS LISTADOS Y GUARDAR EN ARCHIVOS DIFERENTES UNO PARA GENEROS, OTRO PARA USUARIOS...
		List<Object> backup = new ArrayList();
		List<Genero> listadoGeneros = repoGenero.findAll();
		List<Categoria> listadoCategorias = repoCategoria.findAll();
		List<Usuario> listadoUsuarios = repoUsuario.findAll();
		List<Producto> listadoProductos = repoProducto.findAll();
		List<Rol> listadoRoles = repoRol.findAll();
		
		File backUp_old = new File("C:\\Users\\html\\Desktop\\backup_old.txt");
		File backUp = new File("C:\\Users\\html\\Desktop\\backup.txt");
		if(backUp.exists()) {
			backUp.renameTo(backUp_old);
		} 

		backup.addAll(listadoCategorias);
		backup.addAll(listadoGeneros);
		backup.addAll(listadoProductos);
		//el orden importa en caso de que la bd este vacia no puede acceder al id de un rol si no existe
		backup.addAll(listadoRoles); 
		backup.addAll(listadoUsuarios);

		
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
	
	
	@RequestMapping("/RestaurarCopiaSeguridad")
	public String restaurarCopia(Model modelo, RedirectAttributes redirectAttributes) {
		
		
		datosInicio.rellenarBD("C:\\Users\\html\\Desktop\\backup_old.txt");
		redirectAttributes.addFlashAttribute("feedback", "Copia de seguridad restaurada.");
		return "redirect:/superuser";
	}
}
