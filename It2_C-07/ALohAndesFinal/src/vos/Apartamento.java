package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Apartamento {
	/**
	 * Id del apartamento
	 */
	@JsonProperty(value="idApartamento")
	private int idApartamento;
	
	/**
	 * Amoblado del apartamento
	 */
	@JsonProperty(value="amoblado")
	private String amoblado;

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
	 * Metodo constructor del Apartamento
	 * <b>post: </b> Crea el Apartamento con los valores que entran por parametro
	 * @param pIdApartamento - Id del apartamento.
	 * @param pAmoblado - Amoblado del apartamento.
	 * @param pPrecioBase - Precio Base del apartamento.
	 * @param pHabitaciones - Habitaciones del apartamento.
	 * @param pIdProveedor - Id del proveedor del apartamento.
	 */
	public Apartamento(@JsonProperty(value="idApartamento")int pIdApartamento,@JsonProperty(value="amoblado")String pAmoblado, @JsonProperty(value="precioBase") int pPrecioBase, @JsonProperty(value="habitaciones")int pHabitaciones,@JsonProperty(value="idProveedor")int pIdProveedor) 
	{
		this.idApartamento = pIdApartamento;
		this.amoblado= pAmoblado;
		this.precioBase = pPrecioBase;
		this.habitaciones = pHabitaciones;
		this.idProveedor = pIdProveedor;
	}

	public int getidApartamento()
	{
		return idApartamento;
	}
	
	public void setidApartamento(int pId)
	{
		idApartamento= pId;
	}
	public String getAmoblado()
	{
		return amoblado;
	}
	public void setAmoblado(String pAmoblado)
	{
		amoblado = pAmoblado;
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
}
