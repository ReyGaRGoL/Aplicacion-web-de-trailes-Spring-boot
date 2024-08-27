package com.trailers.Controladores;

import java.util.List;

import javax.naming.Binding;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.*;

import com.trailers.Modelos.Genero;
import com.trailers.Modelos.Pelicula;
import com.trailers.Servicios.AlmacenServicioImp;
import com.trailers.repositorios.GeneroRepositorio;
import com.trailers.repositorios.PeliculaRepositorio;

@Controller
@RequestMapping("/Admin")
public class AdminControlador {

	@Autowired
	private PeliculaRepositorio peliculaRepositorio;

	@Autowired
	private GeneroRepositorio generoRepositorio;

	@Autowired
	private AlmacenServicioImp servicio;

	@GetMapping("")
	public ModelAndView verPaginaInicio(@PageableDefault(sort = "titulo", size = 5) Pageable pageable) {
		Page<Pelicula> peliculas = peliculaRepositorio.findAll(pageable);
		return new ModelAndView("admin/index").addObject("peliculas", peliculas);
	}

	@GetMapping("/peliculas/nuevo")
	public ModelAndView MostrarFormularioDeNuevaPelicula() {
		List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
		return new ModelAndView("nueva-pelicula").addObject("peliculas", new Pelicula()).addObject("generos",
				generos);
	}

	@PostMapping("/peliculas/nuevo")
	public ModelAndView RegistrarNuevaPelicula(@Validated Pelicula pelicula, BindingResult binRes) {
		if (binRes.hasErrors() || pelicula.getPortada().isEmpty()) {

			if (pelicula.getPortada().isEmpty()) {
				binRes.rejectValue("portada", "MultipartNotEmpty");
			}

			List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));

			
			return new ModelAndView("nueva-pelicula").addObject("peliculas", pelicula).addObject("generos",
					generos);
			
		}
		String RutaPortada = servicio.AlmacenarArchivo(pelicula.getPortada());
		pelicula.setRutaPortada(RutaPortada);
		peliculaRepositorio.save(pelicula);

		return new ModelAndView("redirect:/Admin");
	}

	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView editarPelicula(@PathVariable int id) {
		Pelicula peli = peliculaRepositorio.getOne(id);
		List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
		return new ModelAndView("Admin/editar-pelicula2").addObject("peliculas", peli).addObject("generos", generos);
	}
	
	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView ActualizarPelicula(@PathVariable int id,@Validated Pelicula pelicula, BindingResult binRes) {
		
		List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
		if (binRes.hasErrors()) {
			return new ModelAndView("Admin/editar-pelicula2").addObject("peliculas", pelicula).addObject("generos", generos);
		}
		Pelicula peliculadb = peliculaRepositorio.getOne(id);
		peliculadb.setTitulo(pelicula.getTitulo());
		peliculadb.setSinopsis(pelicula.getSinopsis());
		peliculadb.setFechaEstreno(pelicula.getFechaEstreno());
		peliculadb.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
		peliculadb.setGeneros(pelicula.getGeneros());
		
		if (!pelicula.getPortada().isEmpty()) {
			servicio.eliminarArchivo(peliculadb.getRutaPortada());
			String ruta = servicio.AlmacenarArchivo(pelicula.getPortada());
			peliculadb.setRutaPortada(ruta);
		}
		
		peliculaRepositorio.save(peliculadb);
		return new ModelAndView("redirect:/Admin");
		
	}
	@PostMapping("/peliculas/{id}/eliminar")
	public String EliminarPelicula(@PathVariable int id ) {
		Pelicula peli = peliculaRepositorio.getOne(id);
		peliculaRepositorio.delete(peli);
		servicio.eliminarArchivo(peli.getRutaPortada());
		return "redirect:/Admin";
		
	}

}
