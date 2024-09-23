package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <p> <b>Clase categoria</b> </p>
 * <p> Clase DTO para generar de manera automatica la Tabla "Categorias" dentro de
 * la BD. </p>
 * 
 */

@Entity
@Table(name = "categorias")
public class Categoria {

	/**
	 * <p><b>Atributo id</b></p>
	 * <p> Clave primaria del elemento en la base de datos que genera el campo id en la
	 * tabla categorias </p>
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * <p><b>Atributo categoria</b> </p>
	 * <p>Atributo que genera el campo categoria en la tabla y guarda el tipo de
	 * categoria. </p>
	 **/
	
	@Column(name = "categoria")
	private String categoria;

	/**
	 * <p><b>Constructor vacio</b> </p>
	 * <p>Inicializa objetos tipo categoria sin pasar valores </p>
	 */
	
	public Categoria() {
		super();
		this.id = 0;
		this.categoria = "";
	}

	/**
	 * <p> <b>Constructor completo</b> </p>
	 * <p> Inicializa un objeto tipo catecoria pasandole todos los valores de sus
	 * campos, el valor de id es guardado como autoincremental en la BD a no ser que
	 * algo falle </p>
	 */
	
	public Categoria(int id, String categoria) {
		super();
		this.id = id;
		this.categoria = categoria;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", categoria=" + categoria + "]";
	}

}
