package com.ipartek.controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.ipartek.repositorio.UsuariosRepo;

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
	@Autowired
	private UsuariosRepo repoUsuario;

	@RequestMapping("/superuser")
	public String inicioAdmin(Model modelo, HttpSession session) {
		if (session.getAttribute("sesion_usuario") != null) {
			if (session.getAttribute("sesion_usuario").equals("admin")) {
				modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
				modelo.addAttribute("atr_listaProductos", repoProductos.findAll());
				modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());

				// crear un producto vacio y pasarselo al formulario para que los th:field no
				// den error
				modelo.addAttribute("obj_producto", new Producto());
				return "admin";
			}
			return "home";
		} else {
			return "redirect:/login";
		}

	}

	@RequestMapping("/comprobacionCredenciales")
	public String comprobacionCredenciales(Model modelo, @ModelAttribute("obj_usuario") Usuario user,
			HttpSession session) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Usuario usuario = new Usuario();
		usuario = repoUsuario.getReferenceById(3);
		//String pass =  passwordEncoder.encode(user.getContrasena()); Comparar sin encriptar no encriptar y comparar
		System.out.println("USEEEEEEEER"+usuario.getContrasena());

		
		if (user.getUsuario().equals(usuario.getUsuario()) && passwordEncoder.matches(user.getContrasena(), usuario.getContrasena())) {
			// crear sesion
			session.setAttribute("sesion_usuario", user.getUsuario());
			System.out.println("CREDENCIALES CORRECTAS");
			return "redirect:/superuser";
		} else {
			System.out.println("NOPE");
			return "login";
		}

	}

	@RequestMapping("/login")
	public String login(Model modelo) {
		modelo.addAttribute("obj_usuario", new Usuario());
		return "login";
	}

	@RequestMapping("/guardarProducto")
	public String nuevoProducto(Model modelo, @ModelAttribute("obj_producto") Producto producto,
			@RequestParam("imagen") MultipartFile foto) {

		Auxiliar.guardarImagen(producto, foto);
		repoProductos.save(producto);
		return "redirect:/superuser";
	}

	@RequestMapping("/adminBorrarPrenda")
	public String borrarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {

		if (valorId != null) {
			Producto prod = new Producto();
			prod = repoProductos.findById(valorId).orElse(prod);
			String ruta = "src/main/resources/static/imagenes/" + prod.getFoto();
			File archivoFoto = new File(ruta);

			//BORRADO DE LA IMAGEN DEL "SERVIDOR"
			if (archivoFoto.exists()) {
				if (!(ruta.equals("src/main/resources/static/imagenes/default.jpg"))) {
					if (archivoFoto.delete()) {
						System.out.println("Foto borrada de la BD");
					} else {
						System.out.println("No se ha podido borrar la foto de la BD");
					}
				} else {
					System.out.println("La foto default no se puede borrar");
				}

			} else {
				System.out.println("Foto no encontrada");
			}

			repoProductos.deleteById(valorId);
		}

		return "redirect:/superuser";
	}

	@RequestMapping("/adminModificarPrenda")
	public String modificarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {

		System.out.println("VALOR ID" + valorId);
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
