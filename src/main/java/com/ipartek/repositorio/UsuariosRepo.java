package com.ipartek.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Usuario;

@Repository
public interface UsuariosRepo extends JpaRepository<Usuario, Integer>{
	@Query(value="SELECT * FROM usuarios WHERE usuario = :nombreUsuario", nativeQuery = true)
	 Optional<Usuario> findByNombreUsuario(String nombreUsuario); //busca por nombre de usuario es un servicio

}
