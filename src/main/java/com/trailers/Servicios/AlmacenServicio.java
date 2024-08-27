package com.trailers.Servicios;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AlmacenServicio {

	public void iniciarAlmacenDeArchivos();
	
	public String AlmacenarArchivo(MultipartFile archivo);
	
	public Path cargarArchivo(String nombreArchivo);
	
	public Resource cargarComoRecurso(String nombreArchivo);
	
	public void eliminarArchivo(String nombreArchivo);
}