package com.trailers.Modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Genero {

	@Id
	@Column(name = "id_genero")
	private int id;
	
	private String titulo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Genero() {
		super();
	}

	public Genero(int id, String titulo) {
		super();
		this.id = id;
		this.titulo = titulo;
	}

	public Genero(int id) {
		super();
		this.id = id;
	}

	public Genero(String titulo) {
		super();
		this.titulo = titulo;
	}
	
	
	
}
