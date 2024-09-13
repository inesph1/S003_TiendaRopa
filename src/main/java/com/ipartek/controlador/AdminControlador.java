package com.ipartek.controlador;

import java.io.File;
import java.io.IOException;
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

import com.ipartek.modelo.Producto;
import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.GeneroRepo;
import com.ipartek.repositorio.ProductosRepo;

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
	public String inicioAdmin(Model modelo) {
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaProductos", repoProductos.findAll());
		modelo.addAttribute("atr_listaGeneros", repoGenero.findAll());

		// crear un producto vacio y pasarselo al formulario para que los th:field no
		// den error
		modelo.addAttribute("obj_producto", new Producto());
		return "admin";
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
		System.out.println("--------------------------------------------------------------");
		System.out.println(producto);

		//String uploadDir = "./imagenes/"; // Cambia esto a la ruta de tu directorio

		if (!foto.isEmpty()) {
			// Genera un nombre único para el archivo
			String filename = foto.getOriginalFilename();
			producto.setFoto(filename);
//			String filePath = uploadDir + File.separator + filename;
//
//			// Guarda el archivo en el servidor
//			File file = new File(filePath);
//			try {
//				foto.transferTo(file);
//				// Guarda la URL o el nombre del archivo en la base de datos
//				producto.setFoto(filename); // Asegúrate de que esta ruta sea accesible
//			} catch (IOException e) {
//				e.printStackTrace();
//				modelo.addAttribute("error", "Error al guardar el archivo");
//				System.out.println("ERROR");
//				return "redirect:/superuser"; // LLEVAR A ERROR
//			}
		} else {
			
			producto.setFoto("default.jpg"); 
		}
		repoProductos.save(producto);
		return "redirect:/superuser";
	}

	@RequestMapping("/adminBorrarPrenda")
	public String borrarPrenda(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {
		repoProductos.deleteById(valorId);
		return "redirect:/superuser";
	}

	/*
	 * @RequestMapping("/adminModificarPrenda") public String modificarPrenda(Model
	 * modelo, @RequestParam(value="id", required=false) Integer valorId) {
	 * 
	 * Producto prod = new Producto();
	 * 
	 * if(valorId!=null) { prod = repoProductos.findById(valorId).orElse(new
	 * Producto()); }
	 * 
	 * 
	 * modelo.addAttribute("obj_producto", prod);
	 * modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
	 * modelo.addAttribute("atr_listaGeneros", repoGenero.findAll()); return ""; }
	 *
	 *
	 *pasar como parametros el input hidden del string de la imagen y el objeto para guardarlo en la bd
	 *public String formularioRelleno(){
	 *Auxiliares.guardarImagen(producto, archivo); //revisar aun no es funcional esta guardada en auxiliares}
	 */
}
