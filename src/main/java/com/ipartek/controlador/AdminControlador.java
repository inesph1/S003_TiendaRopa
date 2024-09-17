package com.ipartek.controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.ipartek.auxiliares.Auxiliar;
import com.ipartek.modelo.Producto;
import com.ipartek.modelo.Rol;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.GeneroRepo;
import com.ipartek.repositorio.ProductosRepo;
import com.ipartek.repositorio.RolRepo;
import com.ipartek.repositorio.RolRepo;
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
	@Autowired
	private RolRepo repoRol;

	@RequestMapping("/superuser")
	public String inicioAdmin(Model modelo, HttpSession session) {
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaProductos", repoProductos.findAll());
		modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());

		// crear un producto vacio y pasarselo al formulario para que los th:field no
		// den error
		modelo.addAttribute("obj_producto", new Producto());
		if (session.getAttribute("sesion_usuario") != null) {
			if (session.getAttribute("sesion_usuario").equals("admin")) {

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

		String errorMsj = "";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuarioOpcional = repoUsuario.findByNombreUsuario(user.getUsuario());

		if (usuarioOpcional.isPresent()) {
			Usuario usuario = usuarioOpcional.get();
			if (usuario.getRol().getId() != 3) {
				if (user.getUsuario().equals(usuario.getUsuario())
						&& passwordEncoder.matches(user.getContrasena(), usuario.getContrasena())) {
					// crear sesion
					session.setAttribute("sesion_usuario", user.getUsuario());
					System.out.println("CREDENCIALES CORRECTAS");
					return "redirect:/superuser";
				} else {
					System.out.println("NOPE");
					if (session.getAttribute("intentos") != null) {
						int valorAnterior = (int) session.getAttribute("intentos");
						int valorActual = valorAnterior + 1;
						session.setAttribute("intentos", valorActual);
						errorMsj = "Credenciales incorrectas. \nIntentos restantes: " + (3 - valorActual);
						if (valorActual >= 3) {
							errorMsj = "Cuenta bloqueada. Pongase en contacto con el administrador";
							// BANEAR USUARIO
							Rol rolBaneado = repoRol.findById(3)
									.orElseThrow(() -> new RuntimeException("Rol Baneado no encontrado"));
							usuario.setRol(rolBaneado);
							repoUsuario.save(usuario);
						}
					} else {
						session.setAttribute("intentos", 1);
						errorMsj = "Credenciales incorrectas. \nIntentos restantes: 2";
					}

				}

			} else {
				// entra aqui porque rol es 3 y la cuenta esta baneada
				errorMsj = "Cuenta bloqueada. Pongase en contacto con el administrador";
			}
		} else {
			errorMsj = "Credenciales incorrectas.";
		}
		modelo.addAttribute("error", errorMsj); // lo pasa al msj de error del html
		return "login";
	}

	@RequestMapping("/login")
	public String login(Model modelo) {
		modelo.addAttribute("obj_usuario", new Usuario());
		return "login";
	}

	@RequestMapping("/guardarProducto")
	public String nuevoProducto(Model modelo, @ModelAttribute("obj_producto") Producto producto,
			@RequestParam("imagen") MultipartFile foto, RedirectAttributes redirectAttributes) {

		Auxiliar.guardarImagen(producto, foto);
		repoProductos.save(producto);
		redirectAttributes.addFlashAttribute("feedback", "Prenda guardada");
		return "redirect:/superuser";
	}

	@RequestMapping("/adminBorrarPrenda")
	public String borrarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId, RedirectAttributes redirectAttributes) {

		if (valorId != null) {
			Producto prod = new Producto();
			prod = repoProductos.findById(valorId).orElse(prod);
			String ruta = "src/main/resources/static/imagenes/" + prod.getFoto();
			File archivoFoto = new File(ruta);

			// BORRAR FOTO SERVIDOR
			Auxiliar.borrarImagenServidor(prod, archivoFoto);

			// BORRAR EL PRODUCTO DE LA BD
			repoProductos.deleteById(valorId);
			redirectAttributes.addFlashAttribute("feedback", "Prenda eliminada de la base de datos");
		}

		return "redirect:/superuser";
	}

	@RequestMapping("/adminModificarPrenda")
	public String modificarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId, RedirectAttributes redirectAttributes) {

		System.out.println("VALOR ID" + valorId);
		Producto prod = new Producto();

		if (valorId != null) {
			prod = repoProductos.findById(valorId).orElse(new Producto());
		}

		modelo.addAttribute("obj_producto", prod);
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());
		//como se redirige a un controlador hay que gacer uso de esto
		redirectAttributes.addFlashAttribute("feedback", "Prenda modificada con éxito");
		return "form_modificar";
	}

	@RequestMapping("/borrarImagenServidor")
	public String borrarImagenServidor(Model modelo, @RequestParam(value = "id", required = false) Integer valorId, RedirectAttributes redirectAttributes) {

		Producto prod = new Producto();
		prod = repoProductos.findById(valorId).orElse(prod);
		String ruta = "src/main/resources/static/imagenes/" + prod.getFoto();
		File archivoFoto = new File(ruta);

		// BORRAR FOTO SERVIDOR
		Auxiliar.borrarImagenServidor(prod, archivoFoto);

		// CAMBIAR LA RUTA DEL PRODUCTO A DEFAULT Y GUARDAR
		prod.setFoto("default.jpg");
		repoProductos.save(prod);
		redirectAttributes.addFlashAttribute("feedback", "Imagen borrada del servidor");
		return "redirect:/superuser";
	}
	
	/* TRAMPA PARA GUARDAR REGISTROS 
	  @RequestMapping("/guardarUsuarios")
	public String guardarUsuarios() {
		
		String pass = "1234";
		 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hash = passwordEncoder.encode(pass);
		
		Rol rolBaneado = repoRol.findById(3)
			    .orElseThrow(() -> new RuntimeException("Rol Baneado no encontrado"));
		Rol rolAdmin = repoRol.findById(1)
			    .orElseThrow(() -> new RuntimeException("Rol Baneado no encontrado"));
		Rol rolUser = repoRol.findById(2)
			    .orElseThrow(() -> new RuntimeException("Rol Baneado no encontrado"));
		
		Usuario u1 = new Usuario(0,"admin", hash, rolAdmin);
		Usuario u2 = new Usuario(0,"usuario", hash, rolUser);
		Usuario u3 = new Usuario(0,"baneado", hash, rolBaneado);
		
		repoUsuario.save(u1);
		repoUsuario.save(u2);
		repoUsuario.save(u3);
				
		return "home";
	}*/
}
