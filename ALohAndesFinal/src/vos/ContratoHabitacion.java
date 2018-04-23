package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoHabitacion extends Contrato {
	
	@JsonProperty(value="idHabitacion")
	int idHabitacion;
	
	public ContratoHabitacion(int id, Date fechaInicio, Date fechaFinal, String idCliente, String idProveedor,
			String estado) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idHabitacion = this.idHabitacion;
	}

	public int getIdHabitacion() {
		return idHabitacion;
	}
	
	public void setIdHabitacion(int idHabitacion) {
		this.idHabitacion=idHabitacion;
	}
	
}
