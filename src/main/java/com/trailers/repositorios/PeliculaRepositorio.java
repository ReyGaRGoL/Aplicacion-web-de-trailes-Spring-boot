package com.trailers.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trailers.Modelos.Genero;
import com.trailers.Modelos.Pelicula;

public interface PeliculaRepositorio extends JpaRepository<Pelicula, Integer>{

}
