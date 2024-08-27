package com.trailers.Controladores;

import java.util.List;

import org.hibernate.validator.internal.engine.groups.GroupWithInheritance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trailers.Modelos.Pelicula;
import com.trailers.repositorios.PeliculaRepositorio;

@Controller
@RequestMapping("")
public class homeControlador {

	@Autowired
	private PeliculaRepositorio Pelirepo;

	@GetMapping("")
	public ModelAndView verInicio() {
		List<Pelicula> ultimas = Pelirepo.findAll(PageRequest.of(0, 4, Sort.by("fechaEstreno").descending())).toList();
		return new ModelAndView("index").addObject("ultimas",ultimas);
		
	}
	
	@GetMapping("/peliculas")
	public ModelAndView listarPelis(@PageableDefault(sort = "fechaEstreno", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Pelicula> peliculas = Pelirepo.findAll(pageable);
		return new ModelAndView("pelis").addObject("pelis",peliculas);
	}
	
	@GetMapping("/peliculas/{id}")
	public ModelAndView MostarDetallesPelis(@PathVariable int id) {
		Pelicula peli = Pelirepo.getOne(id);
		return new ModelAndView("peli").addObject("peli",peli);
	}
}
