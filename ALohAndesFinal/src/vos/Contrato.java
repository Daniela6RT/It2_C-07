package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;




public abstract class Contrato
{
	public final static String HABITACION = "Habitación";
	public final static String APARTAMENTO = "Apartamento";
	public final static String VIVIENDA = "Vivienda";
	public final static String EN_CURSO = "En curso";
	public final static String CANCELADO = "Cancelado";
	
	
	@JsonProperty(value="fechaInicio")
	private Date fechaInicio;
	
	@JsonProperty(value="fechaFinal")
	private Date fechaFinal;
	
	@JsonProperty(value="costo")
	private double costo;

	@JsonProperty(value="id")
	private Integer id;

	@JsonProperty(value="idCliente")
	private Integer idCliente;

	@JsonProperty(value="idProveedor")
	private Integer idProveedor;
	
	@JsonProperty(value="fechaCreacion")
	private Date fechaCreacion;

	@JsonProperty(value="tipo")
	private String tipo;
	
	@JsonProperty(value="idTipo")
	private Integer idTipo;

	@JsonProperty(value="idVivienda")
	private Integer idVivienda;

	@JsonProperty(value="estado")
	private String estado;
		
	public Contrato(
			@JsonProperty(value="id")Integer id, 
			@JsonProperty(value="fechaInicio")Date fechaInicio,
			@JsonProperty(value="fechaFinal")Date fechaFinal,
			@JsonProperty(value="idCliente") Integer idCliente,
			@JsonProperty(value="idProveedor") Integer idProveedor,
			@JsonProperty(value="estado") String estado
			) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.idCliente = idCliente;
		this.idProveedor = idProveedor;
		this.estado = estado;
		
	}

	
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}



	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}



	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFinal = fechaFin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getIdCliente() {
		return idCliente;
	}


	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdProveedor() {
		return idProveedor;
	}


	public void setIdProveedor(Integer idProveedor) {
		this.idProveedor= idProveedor;
	}

	public void setCancelado() {
		estado=CANCELADO;
	}

	public Integer getIdVivienda() {
		return idVivienda;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo= tipo;
	}
	
	public Integer getIdTipo() {
		return idTipo;
	}


	public void setIdTipo(Integer idTipo) {
		this.idTipo= idTipo;
	}

	public String getEstado()
	{
		return estado;
	}

}

