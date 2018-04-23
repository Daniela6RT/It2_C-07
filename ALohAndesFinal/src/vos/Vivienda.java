package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Vivienda 
{
	/**
	 * Id del apartamento
	 */
	@JsonProperty(value="IdVivienda")
	private int IdVivienda;
	
	/**
	 * Amoblado del apartamento
	 */
	@JsonProperty(value="ubicacion")
	private String ubicacion;

	/**
	 * Precio base del apartamento.
	 */
	
	@JsonProperty(value="precioBase")
	private int precioBase;
	
	/**
	 * Habitaciones del aparatamento
	 */
	
	@JsonProperty (value = "habitaciones")
	private int habitaciones;
	/**
	 *Id del Proveedor del apartamento
	 */
	@JsonProperty (value = "idProveedor")
	private int idProveedor;

	/**
	 * Atributo que define si se encuentra reservado
	 */
	private boolean reservado;
	
	
	/**
	 * Metodo constructor de la Vivienda
	 * <b>post: </b> Crea la Vivienda con los valores que entran por parametro
	 * @param pIdVivienda - Id de la vivienda.
	 * @param pUbicacino - Ubicacion de la vivienda.
	 * @param pPrecioBase - Precio Base de la vivienda.
	 * @param pHabitaciones - Habitaciones de la vivienda.
	 * @param pIdProveedor - Id del proveedor de la vivienda.
	 */
	public Vivienda(@JsonProperty(value="IdVivienda")int pIdVivienda,@JsonProperty(value="ubicacion")String pUbicacion, @JsonProperty(value="precioBase") int pPrecioBase, @JsonProperty(value="habitaciones")int pHabitaciones,@JsonProperty(value="idProveedor")int pIdProveedor) 
	{
		this.IdVivienda = pIdVivienda;
		this.ubicacion= pUbicacion;
		this.precioBase = pPrecioBase;
		this.habitaciones = pHabitaciones;
		this.idProveedor = pIdProveedor;
	}

	public int getIdVivienda()
	{
		return IdVivienda;
	}
	
	public void setIdVivienda(int pId)
	{
		IdVivienda= pId;
	}
	public String getUbicacion()
	{
		return ubicacion;
	}
	public void setUbicacion(String pUbicacion)
	{
		ubicacion = pUbicacion;
	}
	public int getPrecioBase()
	{
		return precioBase;
	}
	
	public void setPrecioBase(int pPrecioBase)
	{
		precioBase = pPrecioBase;
	}
	public int getHabitaciones ()
	{
		return habitaciones;
	}
	public void setHabitaciones(int pHabitaciones)
	{
		habitaciones = pHabitaciones;
	}
	
	public int getIdProveedor ()
	{
		return idProveedor;
	}
	public void setIdProveedor(int pIdProveedor)
	{
		idProveedor = pIdProveedor;
	}
	public boolean getReservado()
	{
		return reservado;
	}
	
	public void setReservado(String tf)
	{
		if(tf.equals(true)) reservado = true;
		else if(tf.equals(false)) reservado = false;
		
	}
}
