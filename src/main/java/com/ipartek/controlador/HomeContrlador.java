package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ipartek.repositorio.CategoriaRepo;
import com.ipartek.repositorio.ProductosRepo;

@Controller
public class HomeContrlador {
	
	@Autowired
	private ProductosRepo repoProductos;
	
	@Autowired
	private CategoriaRepo repoCategoria;
	
	@RequestMapping("/")
	public String menuInicial(Model modelo) {
		modelo.addAttribute("atr_listaProductos", repoProductos.findAll());
		modelo.addAttribute("atr_listaCategorias", repoCategoria.findAll());
		//System.out.println("SYSOOOOOOOOO"+modelo.getAttribute("atr_listaProductos"));
		return "home";
	}

}
