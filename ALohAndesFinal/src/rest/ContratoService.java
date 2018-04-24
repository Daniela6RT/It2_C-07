/**-------------------------------------------------------------------
 * ISIS2304 - Sistemas Transaccionales
 * Departamento de Ingenieria de Sistemas
 * Universidad de los Andes
 * Bogota, Colombia
 * 
 * Actividad: Tutorial AlohaAndes: Arquitectura
 * Autores:
 * 			Santiago Cortes Fernandez	-	s.cortes@uniandes.edu.co
 * 			Juan David Vega Guzman		-	jd.vega11@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package rest;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.DAOContrato;
import tm.AlohaAndesTransactionManager;
import vos.Contrato;
import vos.ContratoApartamento;
import vos.Contrato;
import vos.Contrato;

/**
 * @author Santiago Cortes Fernandez 	- 	s.cortes@uniandes.edu.co
 * @author Juan David Vega Guzman		-	jd.vega11@uniandes.edu.co
 * Clase que expone servicios REST con ruta base: http://localhost:8080/TutorialAlohaAndes/rest/Contratoes/...
 */
@Path("contratos")
public class ContratoService {

	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS REST
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo GET que trae a todos los Contratoes en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohaAndes/rest/Contratoes <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los Contratoes que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getContratos() {

		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());

			List<Contrato> Contratoes;
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			Contratoes = tm.getAllContratos();
			return Response.status(200).entity(Contratoes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al Contrato en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohaAndes/rest/Contratoes/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Contrato que contiene al Contrato cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getContratoById( @PathParam( "id" ) int id )
	{
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );

			Contrato Contrato = tm.getContratoById( id );
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	
	/**
	 * Metodo que recibe un Contrato en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Contrato. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/Contratos <br/>
	 * @param Contrato JSON con la informacion del Contrato que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Contrato que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContrato(Contrato Contrato) {
		
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addContrato(Contrato);;
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo POST que recibe un Contrato en formato JSON y lo agrega a la Base de Datos unicamente 
	 * si el número de Contratos que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Contrato. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/Contratos <br/>
	 * @param cantidadMaxima representa la cantidad maxima de Contratos que pueden haber en la misma ciudad
	 * @return	<b>Response Status 200</b> - JSON que contiene al Contrato que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Path( "restriccionCantidad" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContratoWithLimitations(Contrato Contrato) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addContratoWithLimitations(Contrato);;
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	

	/**
	 * Metodo que recibe un Contrato en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al Contrato.<br/>
	 * @param Contrato JSON con la informacion del Contrato que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Contrato que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */

	@PUT
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateContrato(Contrato Contrato) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.updateContrato(Contrato);
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un Contrato en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al Contrato con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/Contratos <br/>
	 * @param Contrato JSON con la informacion del Contrato que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Contrato que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteContrato(Contrato Contrato) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteContrato(Contrato);
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}

	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelContrato(Contrato Contrato) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteContrato(Contrato);
			return Response.status( 200 ).entity( Contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}
	
	/**
	 * 	Metodo que crea una reserva
	 * @param contrato 
	 * @param tipoAlojamiento
	 * @param servicios
	 * @return
	 */
	
	@POST
	@Path( "crearReserva" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearReserva(Contrato contrato,String tipoAlojamiento, String servicios) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.crearReserva(contrato, tipoAlojamiento, servicios);
			return Response.status( 200 ).entity( contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
/**
 * Metodo que crea una reserva colectiva
 * @param contratos
 * @param tipoAlojamiento
 * @param servicios
 * @param cantidadReserva
 * @param idRC
 * @return
 */
	@POST
	@Path( "crearReservaColectiva" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearReservaColectiva(ArrayList<Contrato> contratos, String tipoAlojamiento, String servicios, Integer cantidadReserva, Integer idRC) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.crearReservaColectiva(contratos, tipoAlojamiento, servicios, cantidadReserva, idRC);;
			return Response.status( 200 ).entity( contratos ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo que cancela una reserva
	 * @param contrato
	 * @return
	 */
	@DELETE
	@Path( "cancelarReserva" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarReserva(Contrato contrato) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.cancelarReserva(contrato);
			return Response.status( 200 ).entity( contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}
	
	/**
	 * Metodo que cancela una reserva colectiva
	 * @param idRC
	 * @return
	 */
	@DELETE
	@Path( "cancelarReservaColectiva" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarReservaColectiva(Integer idRC) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.cancelarReservaColectiva(idRC);
			return Response.status( 200 ).entity( idRC ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}
	
	
	/**
	 * Metodo que desahbilita un alojamiento
	 * @param contrato
	 * @param tipoAlojamiento
	 * @return
	 */
	@PUT
	@Path( "desahbilitarOferta" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response desahbilitarOferta(Contrato contrato, String tipoAlojamiento) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.desahbilitarOferta(contrato, tipoAlojamiento);
			return Response.status( 200 ).entity( contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	/**
	 * Metodo que habilita un alojamiento
	 * @param contrato
	 * @param tipoAlojamiento
	 * @return
	 */
	@PUT
	@Path( "habilitarOferta" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response habilitarOferta(Contrato contrato, String tipoAlojamiento) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.habilitarOferta(contrato, tipoAlojamiento);
			return Response.status( 200 ).entity( contrato ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

}
