package com.ipartek.auxiliares;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;

public class Log {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void generarLog(int codigo, String usuario, String ip) {
		String tipoLog = "";
		Date fecha = new Date();
		String direccionIP = ip;

		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fechaFormateada = formatoFecha.format(fecha);
		
		System.out.println("****************************************");
		System.out.println("****************************************");
		System.out.println("****************************************");
		System.out.println("****************************************");
		

		switch (codigo) {
		case 1:
			tipoLog = "Usuario loggeado. Usuario: " + usuario;
			break;
		case 2:
			tipoLog = "Cierre de sesion. Usuario: " + usuario;
			break;
		case 3:
			tipoLog = "Intento de sesion fallido. Usuario: " + usuario;
			break;
		case 4:
			tipoLog = "BLOQUEO DE CUENTA. Usuario: " + usuario;
			break;
		case 5:
			tipoLog = "Intento de inicio de sesion de una cuenta bloqueada. Usuario: " + usuario;
			break;
		default:
			tipoLog = "Log no identificado.";
			break;
		}

		System.out.println(tipoLog + " " + fechaFormateada+ "IP: ");
		Log.guardarCSV(tipoLog, usuario, fechaFormateada, direccionIP);
	}
	
	public static void guardarCSV(String log, String usuario, String fecha, String ip) {
        String csvFile = "C:\\Users\\html\\Desktop\\logs.csv";
        File file = new File(csvFile);
        
       // String linea = "Log,Usuario,fecha,IP\nJuan,Pérez,30\nAna,López,25";

        try (FileWriter writer = new FileWriter(csvFile, true)){
			
			if (!file.exists() || file.length() == 0) {
                writer.write("Log,Usuario,Fecha,IP\n");
            }
			
			writer.write(log + "," + usuario + "," + fecha + "," + ip + "\n");
			System.out.println("Datos guardados en " + csvFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }

}
