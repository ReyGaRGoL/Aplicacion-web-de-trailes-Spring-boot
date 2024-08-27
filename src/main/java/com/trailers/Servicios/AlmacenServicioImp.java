package com.trailers.Servicios;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.annotation.PostConstruct;
import javax.print.DocFlavor.INPUT_STREAM;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.trailers.Excepciones.AlmacenExcepcion;
import com.trailers.Excepciones.FileNotFoundException;

@Service
public class AlmacenServicioImp implements AlmacenServicio{

	@Value("${storage.location}")
	private String storageLocation;
	
	
	@Override
	@PostConstruct  //para indicar que se ejecuta como un constructor
	public void iniciarAlmacenDeArchivos() {
		try {
			Files.createDirectories(Paths.get(storageLocation));
		} catch (IOException excepcion) {
			throw new AlmacenExcepcion("Error al inicialisar la ubicacion en el almacen de archivos");
			
		}
	}

	@Override
	public String AlmacenarArchivo(MultipartFile archivo) {
		String nombreArchivo = archivo.getOriginalFilename();
		if (archivo.isEmpty()) {
			throw new AlmacenExcepcion("No se puede almacenar un archivo vacio");
		}
		try {
			InputStream inputStream = archivo.getInputStream();
			Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException excepcion) {
			throw new AlmacenExcepcion("Error al almacenar el archivo "+ nombreArchivo,excepcion);
		}
		return nombreArchivo;
	}

	@Override
	public Path cargarArchivo(String nombreArchivo) {
		return Paths.get(storageLocation).resolve(nombreArchivo);
	}

	@Override
	public Resource cargarComoRecurso(String nombreArchivo) {
		
		try {
			Path archivo = cargarArchivo(nombreArchivo);
			Resource recurso =  new UrlResource(archivo.toUri());
			
			if (recurso.exists() || recurso.isReadable()) {
				return recurso;
			} else {
				throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreArchivo);
			}
		} catch (MalformedURLException excepcion) {
			throw new FileNotFoundException("No se pudo encontrar el archvo "+ nombreArchivo,excepcion);
		}
		
	}

	@Override
	public void eliminarArchivo(String nombreArchivo) {
		Path Archivo = cargarArchivo(nombreArchivo);
		try {
			FileSystemUtils.deleteRecursively(Archivo);
		} catch (Exception excepcion) {
			System.out.println(excepcion);
		}
	}

	
}
