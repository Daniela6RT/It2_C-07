package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Alojamiento {
    /**
     * Id del alojamiento
     */
    @JsonProperty(value="IDALOJAMIENTO")
    private int idAlojamiento;

    /**
     * Tipo de alojamiento
     */
    @JsonProperty(value="TIPO")
    private String tipo;

    /**
     * Id del tipo de alojamiento.
     */

    @JsonProperty(value="IDTIPO")
    private int idTipo;

    /**
     * Constante que define Apartamento
     */
    public static final String APARTAMENTO ="Apartamento";

    /**
     * Constante que define Vivienda
     */
    public static final String VIVIENDA ="Vivienda";

    /**
     * Constante que define Habitacion
     */
    public static final String HABITACION ="Habitacion";

    /**
     * Metodo constructor de Cliente
     * <b>post: </b> Crea el cliente con los valores que entran por parametro
     * @param pIdAlojamiento - IdCliente del cliente.
     * @param pTipo - Nombre del cliente.
     * @param pIdTipo - Apellido del cliente.
     */
    public Alojamiento(@JsonProperty(value="IDALOJAMIENTO")int pIdAlojamiento,@JsonProperty(value="TIPO")String pTipo, @JsonProperty(value="IDTIPO") int pIdTipo)
    {
        this.idAlojamiento = pIdAlojamiento;
        this.tipo= pTipo;
        this.idTipo = pIdTipo;

    }

    public int getIdAlojamiento(){
        return idAlojamiento;
    }

    public void setIdAlojamiento(int pIdAlojamiento) {
        idAlojamiento=pIdAlojamiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setApartamento()
    {
        tipo=APARTAMENTO;
    }

    public void setHabitacion()
    {
        tipo=HABITACION;
    }

    public void setVivienda()
    {
        tipo=VIVIENDA;
    }

}
