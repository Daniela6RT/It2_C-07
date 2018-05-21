package rest;

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

import tm.AlohaAndesTransactionManager;

import vos.Vivienda;
@Path("viviendas")
public class ViviendaService 
{

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;


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


	/**
	 * Metodo GET que trae a todos los viviendas en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/viviendas <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los viviendas que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getviviendas() {
		
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Vivienda> viviendas;
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			viviendas = tm.getAllviviendas();
			return Response.status(200).entity(viviendas).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al Vivienda en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/viviendas/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Vivienda que contiene al Vivienda cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getViviendaById( @PathParam( "id" ) int id )
	{
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			Vivienda Vivienda = tm.getViviendaById( id );
			return Response.status( 200 ).entity( Vivienda ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	/**
	 * Metodo que recibe un Vivienda en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Vivienda. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/viviendas <br/>
	 * @param Vivienda JSON con la informacion del Vivienda que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Vivienda que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVivienda(Vivienda Vivienda) {
		
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addVivienda(Vivienda);;
			return Response.status( 200 ).entity( Vivienda ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo POST que recibe un Vivienda en formato JSON y lo agrega a la Base de Datos unicamente 
	 * si el número de viviendas que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Vivienda. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/viviendas <br/>
	 * @param cantidadMaxima representa la cantidad maxima de viviendas que pueden haber en la misma ciudad
	 * @return	<b>Response Status 200</b> - JSON que contiene al Vivienda que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Path( "restriccionCantidad" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addViviendaWithLimitations(Vivienda Vivienda) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addViviendaWithLimitations(Vivienda);;
			return Response.status( 200 ).entity( Vivienda ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	

	/**
	 * Metodo que recibe un Vivienda en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al Vivienda.<br/>
	 * @param Vivienda JSON con la informacion del Vivienda que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Vivienda que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */

	@PUT
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateVivienda(Vivienda Vivienda) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.updateVivienda(Vivienda);
			return Response.status( 200 ).entity( Vivienda ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un Vivienda en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al Vivienda con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/viviendas <br/>
	 * @param Vivienda JSON con la informacion del Vivienda que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Vivienda que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteVivienda(Vivienda Vivienda) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteVivienda(Vivienda);
			return Response.status( 200 ).entity( Vivienda ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}
	
			
	@GET
	@Path("funcionalidadV")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getFuncionalidadViviendas() {
		
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List viviendas;
			
			viviendas = tm.getFuncionamiento("Vivienda");
			return Response.status(200).entity(viviendas).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

}
