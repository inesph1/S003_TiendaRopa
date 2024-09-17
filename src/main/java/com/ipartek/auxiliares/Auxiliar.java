package com.ipartek.auxiliares;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.ipartek.modelo.Producto;

public class Auxiliar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// LAS CLASES STATIC NO NECESITAN INSTANCIARSE PERO CONSUMEN MUCHOS RECURSOS DE
		// MEMORIA ASI QUE HAY QUE USARLAS CON CUIDADO
	}

	public static void guardarImagen(Producto prod, MultipartFile archivo) {
		if (!archivo.isEmpty()) {
			try {
				Date fecha = new Date();
				String nombreArchivo = fecha.getTime() + archivo.getOriginalFilename();
				Path ruta = Paths.get("src/main/resources/static/imagenes", nombreArchivo);

				Files.write(ruta, archivo.getBytes());
				prod.setFoto(nombreArchivo);
				// System.out.println("ATRIBUTO FOTO PRODUCTO "+prod.getFoto());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (!(prod.getFoto().equals(""))) {
			// System.out.println("hay input");
			// System.out.println("ATRIBUTO FOTO PRODUCTO NO ES NULO"+prod.getFoto());
			prod.setFoto(prod.getFoto());

		} else {
			System.out.println("ATRIBUTO FOTO PRODUCTO ES NULO" + prod.getFoto());
			prod.setFoto("default.jpg");
		}
	}

	/*
	 * public static void guardarImagenMetodoLargo(Producto producto, MultipartFile
	 * foto) { //es importante porque al estar ejecutandose el programa no permite
	 * guardar la foto en la ruta por lo que hay que usar esto String uploadDir =
	 * new File("src/main/resources/static/imagenes").getAbsolutePath(); if
	 * (!foto.isEmpty()) { String filename = foto.getOriginalFilename(); Path
	 * filePath = Paths.get(uploadDir, filename);
	 * 
	 * try { Files.createDirectories(filePath.getParent()); // comprueba si existe
	 * directorio foto.transferTo(filePath.toFile()); producto.setFoto(filename);
	 * //atributo foto del obj producto } catch (IOException e) {
	 * e.printStackTrace();
	 * 
	 * } } else { producto.setFoto("default.jpg"); //valor por defecto } }
	 */

	public static String borrarImagenServidor(Producto prod, File archivoFoto) {

		String ruta = "src/main/resources/static/imagenes/" + prod.getFoto();
		String feed ="";

		if (archivoFoto.exists()) {
			if (!(ruta.equals("src/main/resources/static/imagenes/default.jpg"))) {
				if (archivoFoto.delete()) {
					System.out.println("Foto borrada de la BD");
					feed = "Foto borrada de la BD";
				} else {
					System.out.println("No se ha podido borrar la foto de la BD");
					feed = Auxiliar.gestionErrores(4);
				}
			} else {
				System.out.println("La foto default no se puede borrar");
				feed = Auxiliar.gestionErrores(5);
			}
		} else {
			System.out.println("Foto no encontrada");
			feed = Auxiliar.gestionErrores(6);
		}
		
		return feed;
	}
	
	public static String gestionErrores(int codigo) {
		String error = "";

		switch (codigo) {
		case 1:
			error = "Error al guardar el producto";
			break;
		case 2:
			error = "Credenciales incorrectas. ";
			break;
		case 3:
			error ="Cuenta bloqueada. Pongase en contacto con el administrador";
			break;
		case 4:
			error = "No se ha podido borrar la foto de la BD";
			break;
		case 5:
			error="La foto default no se puede borrar";
			break;
		case 6:
			error="Foto no encontrada";
			break;
		}
		
		return error;
			
	}

}
