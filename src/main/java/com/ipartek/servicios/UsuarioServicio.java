package com.ipartek.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipartek.repositorio.UsuariosRepo;

@Service
public class UsuarioServicio {
	@Autowired
	private UsuariosRepo repoUsuarios;
	
	
	
}
