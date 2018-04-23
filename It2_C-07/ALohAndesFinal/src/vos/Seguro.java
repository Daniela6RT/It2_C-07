package vos;

import org.codehaus.jackson.annotate.JsonProperty;



public class Seguro
{
	@JsonProperty(value="id")
	private int id;
	
	@JsonProperty(value="costo")
	private double costo;

	@JsonProperty(value="incendio")
	private boolean incendio;

	@JsonProperty(value="robo")
	private boolean robo;

	@JsonProperty(value="inundacion")
	private boolean inundacion;

	@JsonProperty(value="fenomenos")
	private boolean fenomenoNatural;

	@JsonProperty(value="idVivienda")
	private int idVivienda;



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public boolean isIncendio() {
		return incendio;
	}

	public void setIncendio(boolean incendio) {
		this.incendio = incendio;
	}

	public boolean isRobo() {
		return robo;
	}

	public void setRobo(boolean robo) {
		this.robo = robo;
	}

	public boolean isInundaciones() {
		return inundacion;
	}

	public void setInundaciones(boolean inundaciones) {
		this.inundacion = inundaciones;
	}

	public boolean isFenomeno() {
		return fenomenoNatural;
	}

	public void setFenomeno(boolean fenomenoNatural) {
		this.fenomenoNatural = fenomenoNatural;
	}

	public int getIdVivienda() {
		return idVivienda;
	}

	public void setIdVivienda(int idVivienda) {
		this.idVivienda = idVivienda;
	}

	public Seguro(  @JsonProperty(value="id")int id,
					@JsonProperty(value="costo")double costo, 
					@JsonProperty(value="incendio")boolean incendio, 
					@JsonProperty(value="robo")boolean robo, 
					@JsonProperty(value="inundaciones")boolean inundaciones,
					@JsonProperty(value="fenomenos")boolean fenomenos,
					@JsonProperty(value="idVivienda")int idVivienda) {

		super();
		this.id=id;
		this.costo = costo;
		this.incendio = incendio;
		this.robo = robo;
		this.inundacion = inundaciones;
		this.fenomenoNatural = fenomenos;
		this.idVivienda = idVivienda;
	}



}

