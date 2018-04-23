package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoApartamento extends Contrato {
	
	@JsonProperty(value="idApartamento")
	Integer idApartamento;
	
	public ContratoApartamento(Integer id, Date fechaInicio, Date fechaFinal, Integer idCliente, Integer idProveedor,
			String estado) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idApartamento = this.idApartamento;
	}

	public Integer getIdApartamento() {
		return idApartamento;
	}
	
	public void setIdApartamento(Integer idApartamento) {
		this.idApartamento=idApartamento;
	}
}
