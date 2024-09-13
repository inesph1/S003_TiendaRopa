package com.ipartek.controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.auxiliares.Auxiliar;
import com.ipartek.modelo.Producto;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.GeneroRepo;
import com.ipartek.repositorio.ProductosRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class AdminControlador {

	@Autowired
	private ProductosRepo repoProductos;
	@Autowired
	private CategoriaRepo repoCategoria;
	@Autowired
	private GeneroRepo repoGenero;

	// @Value("${upload.dir}")
	// private String uploadDir;

	@RequestMapping("/superuser")
	public String inicioAdmin(Model modelo, HttpSession session) {
		if(session.getAttribute("sesion_usuario") != null) {
			if(session.getAttribute("sesion_usuario").equals("admin")) {
			modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
			modelo.addAttribute("atr_listaProductos", repoProductos.findAll());
			modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());

			// crear un producto vacio y pasarselo al formulario para que los th:field no
			// den error
			modelo.addAttribute("obj_producto", new Producto());
			return "admin";
			}
			return "home";
		}else {
			return "redirect:/login";
		}
		
	}
	
	@RequestMapping("/comprobacionCredenciales")
	public String comprobacionCredenciales(Model modelo,  @ModelAttribute("obj_usuario") Usuario user, HttpSession session ) {
		if(user.getUsuario().equals("admin") && user.getContrasena().equals("1234")) {
			//crear sesion
			session.setAttribute("sesion_usuario", user.getUsuario());
			System.out.println("CREDENCIALES CORRECTAS");
			return "redirect:/superuser";
		}else {
			System.out.println("NOPE");
			return "login";
		}
		
	}
	
	@RequestMapping("/login")
	public String login(Model modelo) {
		modelo.addAttribute("obj_usuario", new Usuario());
		return "login";
	}


	/*
	 * public String nuevoProducto(Model modelo,
	 * 
	 * @ModelAttribute("obj_producto") Producto producto,
	 * 
	 * @RequestParam("file") MultipartFile foto) {
	 */

	@RequestMapping("/guardarProducto")
	public String nuevoProducto(Model modelo, @ModelAttribute("obj_producto") Producto producto,
			@RequestParam("imagen") MultipartFile foto) {

		System.out.println(producto.toString());
		System.out.println("VALOR ID EN GUARDAR PRODUCTO "+ producto.getId());
		Auxiliar.guardarImagen(producto, foto );
		repoProductos.save(producto);
		return "redirect:/superuser";
	}

	@RequestMapping("/adminBorrarPrenda")
	public String borrarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {
		repoProductos.deleteById(valorId);
		return "redirect:/superuser";
	}

	@RequestMapping("/adminModificarPrenda")
	public String modificarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {

		System.out.println("VALOR ID"+ valorId);
		Producto prod = new Producto();

		if (valorId != null) {
			prod = repoProductos.findById(valorId).orElse(new Producto());
		}

		modelo.addAttribute("obj_producto", prod);
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());
		return "form_modificar";
	}


}
