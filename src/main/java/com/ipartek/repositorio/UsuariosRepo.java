package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Usuario;

@Repository
public interface UsuariosRepo extends JpaRepository<Usuario, Integer>{
	

}
