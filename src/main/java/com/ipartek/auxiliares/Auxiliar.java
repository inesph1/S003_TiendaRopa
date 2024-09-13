package com.ipartek.auxiliares;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

import com.ipartek.modelo.Producto;

public class Auxiliar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//METER AQUI CODIGO GUARDR FOTO
		//public guardarImagen(Producto prod, archivo) {
			
		//}
		

	}
	

	public static void guardarImagen(Producto prod, MultipartFile archivo) {
		if(!archivo.isEmpty()) {
			try {
				String nombreArchivo = archivo.getOriginalFilename();
				Path ruta = Paths.get("src/main/resources/static/imagenes", nombreArchivo);
				
				Files.write(ruta, archivo.getBytes());
				
				prod.setFoto(nombreArchivo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			prod.setFoto("default.jpg");
		}
	}
	
	public static void guardarImagenMetodoLargo(Producto producto, MultipartFile foto) {
		//es importante porque al estar ejecutandose el programa no permite guardar la foto en la ruta por lo que hay que usar esto
		 String uploadDir =  new File("src/main/resources/static/imagenes").getAbsolutePath(); 
	        if (!foto.isEmpty()) {
	            String filename =  foto.getOriginalFilename();
	            Path filePath = Paths.get(uploadDir, filename);

	            try {
	                Files.createDirectories(filePath.getParent()); // comprueba si existe directorio
	                foto.transferTo(filePath.toFile());
	                producto.setFoto(filename); //atributo foto del obj producto
	            } catch (IOException e) {
	                e.printStackTrace();

	            }
	        } else {
	            producto.setFoto("default.jpg"); //valor por defecto
	        }
	}

}
