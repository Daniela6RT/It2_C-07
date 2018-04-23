package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoApartamento extends Contrato {
	
	@JsonProperty(value="idApartamento")
	int idApartamento;
	
	public ContratoApartamento(int id, Date fechaInicio, Date fechaFinal, String idCliente, String idProveedor,
			String estado) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		
		idApartamento = this.idApartamento;
	}

	public int getIdApartamento() {
		return idApartamento;
	}
	
	public void setIdApartamento(int idApartamento) {
		this.idApartamento=idApartamento;
	}
}
