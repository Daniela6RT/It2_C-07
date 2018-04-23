package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoVivienda extends Contrato {
	
	@JsonProperty(value="idVivienda")
	int idVivienda;
	
	public ContratoVivienda(int id, Date fechaInicio, Date fechaFinal, String idCliente, String idProveedor,
			String estado) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idVivienda = this.idVivienda;
	}

	public int getIdVivienda() {
		return idVivienda;
	}
	
	public void setIdVivienda(int idVivienda) {
		this.idVivienda=idVivienda;
	}
}
