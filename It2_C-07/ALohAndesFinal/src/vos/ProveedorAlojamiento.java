package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProveedorAlojamiento {

	/**
	 * Id del proveedor
	 */
	@JsonProperty(value="id")
	private int id;
	
	/**
	 * Nombre del proveedor
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Tipo proveedor de alojamiento.
	 */
	
	@JsonProperty(value="tipoProveedorAlojamiento")
	private int tipoProveedorAlojamiento;
	
	
	public static int hotel =1;
	
	public static int hostal= 2;

	/**
	 * Metodo constructor del Apartamento
	 * <b>post: </b> Crea el Apartamento con los valores que entran por parametro
	 * @param pIdProveedor - Id del proveedor.
	 * @param pNombre - Nombre del proveedor.
	 * @param pTipoProveedorAlojamiento - Tipo de proveedor de alojamiento.
	 */
	public ProveedorAlojamiento(@JsonProperty(value="id")int pIdProveedor,@JsonProperty(value="nombre")String pNombre, @JsonProperty(value="tipoProveedorAlojamiento") int pTipoProveedorAlojamiento) 
	{
		this.id = pIdProveedor;
		this.nombre= pNombre;
		this.tipoProveedorAlojamiento = pTipoProveedorAlojamiento;
	}

	public int getIdProveedor()
	{
		return id;
	}
	
	public void setIdProveedor(int pId)
	{
		id= pId;
	}
	public String getNombre()
	{
		return nombre;
	}
	public void setNombre(String pNombre)
	{
		nombre = pNombre;
	}
	public int getTipoProveedorAlojamiento()
	{
		return tipoProveedorAlojamiento;
	}
	
	public void setTipoProveedorAlojamiento(int pTipoProveedorAlojamiento)
	{
		tipoProveedorAlojamiento = pTipoProveedorAlojamiento;
	}


}
