package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.ProductosRepo;

@Controller
public class UsuarioControlador {

	@Autowired
	private ProductosRepo repoProductos;

	@Autowired
	private CategoriaRepo repoCategoria;

	@RequestMapping("/home")
	public String inicio() {
		return "home";
	}

	@RequestMapping("/menuGenerico")
	public String secciones(Model modelo, @RequestParam(value = "id", required = false) Integer valorId) {

		// obtener el id y compararlo y secun que sea hacer el redirect
		String ruta = "home";
		
		//habria que modificar esto porque si se odifica la bd daria enlaces rotos HACER DINAMICO quizas con el id
		if (valorId != null) {
			switch (valorId) {
			case 1:
				ruta = "redirect:/camisetas";
				break;
			case 2:
				ruta = "redirect:/sudaderas";
				break;
			case 3:
				ruta = "redirect:/pantalones";
				break;
			}
		}

		return ruta;
	}

	@RequestMapping("/camisetas")
	public String seccionCamisetas(Model modelo) {
		System.out.println("CAMISETAS");
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaProductos", repoProductos.obtenerProductosPorTipo(1));
		return "camisetas";
	}

	@RequestMapping("/sudaderas")
	public String seccionSudaderas(Model modelo) {
		System.out.println("SUDADERAS");
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaProductos", repoProductos.obtenerProductosPorTipo(2));
		return "sudaderas";
	}

	@RequestMapping("/pantalones")
	public String seccionPantalones(Model modelo) {
		System.out.println("PANTALONES");
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		modelo.addAttribute("atr_listaProductos", repoProductos.obtenerProductosPorTipo(3));
		return "pantalones";
	}
}
