package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class Servicio
{
	@JsonProperty(value="id")
	private int id;
	
	@JsonProperty(value="nombre")
	private String nombre;

	@JsonProperty(value="descripcion")
	private String descripcion;
	
	@JsonProperty(value="precio")
	private int precio;
	
	
	
	public Servicio(
			@JsonProperty(value="id")int id, 
			@JsonProperty(value="nombre")String nombre, 
			@JsonProperty(value="descripcion")String descripcion,
			@JsonProperty(value="precio")int precio
			) {
		super();
		this.id = id;
		this.precio = precio;
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
		
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
		public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	

}

