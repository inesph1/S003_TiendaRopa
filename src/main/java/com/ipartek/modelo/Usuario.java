package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "usuario", unique=true)
	private String usuario;
	@Column(name = "contrasena")
	private String contrasena;
	@ManyToOne
	private Rol rol;

	public Usuario() {
		super();
		this.id = 0;
		this.usuario = "";
		this.contrasena = "";
		this.rol = new Rol();
	}

	public Usuario(int id, String usuario, String contrasena, Rol rol) {
		super();
		this.id= id;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.rol = rol;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", usuario=" + usuario + ", contrasena=" + contrasena + ", rol=" + rol + "]";
	}

	
	

}
