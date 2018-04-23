package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoHabitacion extends Contrato {
	
	@JsonProperty(value="idHabitacion")
	Integer idHabitacion;
	
	public ContratoHabitacion(Integer id, Date fechaInicio, Date fechaFinal, Integer idCliente, Integer idProveedor,
			String estado, Integer idHabitacion) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idHabitacion = this.idHabitacion;
	}

	public Integer getIdHabitacion() {
		return idHabitacion;
	}
	
	public void setIdHabitacion(Integer idHabitacion) {
		this.idHabitacion=idHabitacion;
	}
	
}
