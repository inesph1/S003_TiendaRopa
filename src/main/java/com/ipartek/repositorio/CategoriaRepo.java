package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Categoria;

@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Integer>{

}
