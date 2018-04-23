package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Habitacion 
{
	/**
	 * Id del apartamento
	 */
	@JsonProperty(value="idHabitacion")
	private int idHabitacion;
	
	/**
	 * Amoblado del apartamento
	 */
	@JsonProperty(value="tipo")
	private String tipo;

	/**
	 * Precio base del apartamento.
	 */
	
	@JsonProperty(value="precioBase")
	private int precioBase;
	
	/**
	 * Habitaciones del aparatamento
	 */
	
	@JsonProperty (value = "compartida")
	private String compartida;
	/**
	 *Id del Proveedor del apartamento
	 */
	@JsonProperty (value = "horaApertura")
	private String horaApertura;
	
	@JsonProperty (value = "minApertura")
	private String minApertura;
	
	@JsonProperty (value = "horaCierre")
	private String horaCierre;
	
	@JsonProperty (value = "minCierre")
	private String minCierre;
	
	@JsonProperty (value = "tamanio")
	private int tamanio;
	
	@JsonProperty (value = "capacidad")
	private int capacidad;
	
	@JsonProperty (value = "idProveedor")
	private int idProveedor;

	/**
	 * Atributo que define si se encuentra reservado
	 */
	private boolean reservado;
		
	
	/**
	 * Metodo constructor de la habitacion
	 * <b>post: </b> Crea la habitacion con los valores que entran por parametro
	 * @param pIdHabitacion - Id de la habitacion.
	 * @param pTipo - tipo de la habitacion.
	 * @param pPrecioBase - Precio Base de a habitacion.
	 * @param pCompartida - Sí es compartida o no la habitacion.
	 * @param pHotaApertura - Hora de apertura de la habitacion.
	 * @param pHoraCierre - Hora de cierre de la habitacion.
	 * @param pMinCierre - Minimo para el cierre de la habitacion.
	 * @param pMinApertura - Minimo para la apertura de la habitacion.
	 * @param pTamanio - Tamaño de la habitacion.
	 * @param pCapacidad - Capacidad de la habitacion.
	 * @param pIdProveedor - Id del proveedor de la habitacion.
	 */
	public Habitacion(@JsonProperty(value="idHabitacion")int pIdHabitacion,@JsonProperty(value="tipo")String pTipo, @JsonProperty(value="precioBase") int pPrecioBase, @JsonProperty(value="compartida")String pCompartida,@JsonProperty(value="horaApertura")String pHoraApertura,@JsonProperty(value="minApertura")String pMinApertura,@JsonProperty(value="horaCierre")String pHoraCierre,@JsonProperty(value="minCierre")String pMinCierre,@JsonProperty(value="tamanio")int pTamanio,@JsonProperty(value="capacidad")int pCapacidad,@JsonProperty(value="idProveedor")int pIdProveedor) 
	{
		this.idHabitacion = pIdHabitacion;
		this.tipo= pTipo;
		this.precioBase = pPrecioBase;
		this.compartida = pCompartida;
		this.horaApertura=pHoraApertura;
		this.minApertura = pMinApertura;
		this.horaCierre=pHoraCierre;
		this.minCierre=pMinCierre;
		this.tamanio=pTamanio;
		this.capacidad =pCapacidad;
		this.idProveedor = pIdProveedor;
	}

	public int getIdHabitacion()
	{
		return idHabitacion;
	}
	
	public void setIdHabitacion(int pId)
	{
		idHabitacion= pId;
	}
	public String getTipo()
	{
		return tipo;
	}
	public void setTipo(String pTipo)
	{
		tipo = pTipo;
	}
	public int getPrecioBase()
	{
		return precioBase;
	}
	
	public void setPrecioBase(int pPrecioBase)
	{
		precioBase = pPrecioBase;
	}
	
	public String getCompartida()
	{
		return compartida;
	}
	public void setCompartida(String pCompartida)
	{
		compartida = pCompartida;
	}
	
	public String getHoraApertura()
	{
		return horaApertura;
	}
	public void setHoraApertura(String pHoraApertura)
	{
		horaApertura = pHoraApertura;
	}
	
	public String getHoraCierre()
	{
		return horaCierre;
	}
	public void setHoraCierre(String pHoraCierre)
	{
		horaCierre = pHoraCierre;
	}
	public String getMinCierre()
	{
		return minCierre;
	}
	public void setMinApertura(String pMinApertura)
	{
		minApertura = pMinApertura;
	}
	
	public String getMinApertura()
	{
		return minApertura;
	}
	public void setMinCierre(String pMinCierre)
	{
		minCierre = pMinCierre;
	}
	
	public int getCapacidad ()
	{
		return capacidad;
	}
	public void setCapacidad(int pCapacidad)
	{
		tamanio = pCapacidad;
	}
	

	public int getTamanio ()
	{
		return tamanio;
	}
	public void setTamanio(int pTamanio)
	{
		tamanio = pTamanio;
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
