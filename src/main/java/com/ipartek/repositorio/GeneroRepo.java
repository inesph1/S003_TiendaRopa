package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Genero;

@Repository
public interface GeneroRepo extends JpaRepository<Genero, Integer> {

}
