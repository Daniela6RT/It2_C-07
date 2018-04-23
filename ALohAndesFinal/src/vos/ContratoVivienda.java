package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoVivienda extends Contrato {
	
	@JsonProperty(value="idVivienda")
	Integer idVivienda;
	
	public ContratoVivienda(Integer id, Date fechaInicio, Date fechaFinal, Integer idCliente, Integer idProveedor, 
			String estado, Integer idVivienda) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idVivienda = this.idVivienda;
	}

	public Integer getIdVivienda() {
		return idVivienda;
	}
	
	public void setIdVivienda(Integer idVivienda) {
		this.idVivienda=idVivienda;
	}
}
