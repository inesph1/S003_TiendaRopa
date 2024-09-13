package com.ipartek.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Producto;

@Repository
public interface ProductosRepo extends JpaRepository<Producto, Integer> {
	@Query(value="SELECT * FROM productos WHERE categoria_id = :valorFK", nativeQuery = true)
	List<Producto> obtenerProductosPorTipo(@Param("valorFK") int valor);

}
