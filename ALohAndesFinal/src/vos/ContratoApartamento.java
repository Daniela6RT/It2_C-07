package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContratoApartamento extends Contrato {
	
	@JsonProperty(value="idApartamento")
	int idApartamento;
	
	public ContratoApartamento(int id, Date fechaInicio, Date fechaFinal, String idCliente, String idProveedor,
			Date fechaCreacion, int costo, int idApartamento) {
		super(id, fechaInicio, fechaFinal, idCliente, idProveedor, fechaCreacion, costo, idProveedor, idApartamento);
		
		idApartamento = this.idApartamento;
	}

	public int getIdApartamento() {
		return idApartamento;
	}
	
	public void setIdApartamento(int idApartamento) {
		this.idApartamento=idApartamento;
	}
}
