package vos;
import org.codehaus.jackson.annotate.*;


public class Menaje {

	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	@JsonProperty(value="id")
	private int id;
	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="descripcion")
	private String descripcion;
	
	@JsonProperty(value="idVivienda")
	private int idVivienda;
	
	
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODO CONSTRUCTOR
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase Bebedor
	 * <b>post: </b> Crea el bebedor con los valores que entran por parametro
	 * @param id - Id del bebedor.
	 * @param nombre - Nombre del bebedor.
	 * @param presupuesto - Presupuesto del bebedor.
	 * @param ciudad - Ciudad del bebedor.
	 */
	// Requerimiento 1E: Complete el metodo constructor (parametros y contenido) con los atributos agregados anteriormente
	public Menaje(
			@JsonProperty(value="id")int id,
			@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="descripcion")String descripcion,
			@JsonProperty(value="idVivienda")int idVivienda) {

		this.id = id;
		this.nombre = nombre;
		this.descripcion=descripcion;
		this.idVivienda = idVivienda;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE LA CLASE
	//----------------------------------------------------------------------------------------------------------------------------------


	// Requerimiento 1F: Genere los Getters y Setter de los atributos utilizando la opcion en Source-> Generate Getters And Setters
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
	public int getIdVivienda() {
		return idVivienda;
	}

	public void setIdVivienda(int idVivienda) {
		this.idVivienda = idVivienda;
	}


}
