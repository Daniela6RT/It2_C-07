package rest;

import java.util.Date;
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
import vos.Alojamiento;
import vos.Cliente;
@Path("clientes")
public class ClienteService

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
	 * Metodo GET que trae a todos los clientes en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/clientes <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los clientes que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getclientes() {
		
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Cliente> clientes;
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			clientes = tm.getAllClientes();
			return Response.status(200).entity(clientes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al Cliente en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/clientes/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Cliente que contiene al Cliente cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getClienteById( @PathParam( "id" ) int id )
	{
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			Cliente Cliente = tm.getClienteById( id );
			return Response.status( 200 ).entity( Cliente ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	

	/**
	 * Metodo que recibe un Cliente en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Cliente. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/clientes <br/>
	 * @param Cliente JSON con la informacion del Cliente que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Cliente que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCliente(Cliente Cliente) {
		
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addCliente(Cliente);;
			return Response.status( 200 ).entity( Cliente ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo POST que recibe un Cliente en formato JSON y lo agrega a la Base de Datos unicamente 
	 * si el número de clientes que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Cliente. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/clientes <br/>
	 * @param cantidadMaxima representa la cantidad maxima de clientes que pueden haber en la misma ciudad
	 * @return	<b>Response Status 200</b> - JSON que contiene al Cliente que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Path( "restriccionCantidad" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addClienteWithLimitations(Cliente Cliente) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addClienteWithLimitations(Cliente);;
			return Response.status( 200 ).entity( Cliente ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	

	/**
	 * Metodo que recibe un Cliente en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al Cliente.<br/>
	 * @param Cliente JSON con la informacion del Cliente que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Cliente que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */

	@PUT
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCliente(Cliente Cliente) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.updateCliente(Cliente);
			return Response.status( 200 ).entity( Cliente ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un Cliente en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al Cliente con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/clientes <br/>
	 * @param Cliente JSON con la informacion del Cliente que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Cliente que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCliente(Cliente Cliente) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteCliente(Cliente);
			return Response.status( 200 ).entity( Cliente ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}
	
	/**
	 * Metodo que da los clientes frecuentes
	 * @return
	 */
	@GET
	@Path( "clientesFrecuentes" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClientesFrecuentes() {
		
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Cliente> clientes;
			clientes = tm.getClientesfrecuentes();
			return Response.status(200).entity(clientes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	
	/**
	 * Metodo que da los clientes frecuentes
	 * @return
	 */
	@GET
	@Path( "clientesBuenos" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClientesBuenos() {
		
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Cliente> clientes;
			clientes = tm.getClientesBuenos();
			return Response.status(200).entity(clientes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo que retorna el consumo den alohandes (version 1)
	 * @return
	 */
	@GET
	@Path( "consumoUnaReserva" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getConsumoAlohAndesRFC10(Alojamiento alojamiento, Date fecha1, Date fecha2 , String organizacion) {
		 
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Cliente> clientes;
			clientes = tm.consultarConsumoRFC10(alojamiento, fecha1, fecha2, organizacion);
			return Response.status(200).entity(clientes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	/**
	 * Metodo que retorna el consumo den alohandes (version 1)
	 * @return
	 */
	@GET
	@Path( "consumoNingunaReserva" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getConsumoAlohAndesRFC11(Alojamiento alojamiento, Date fecha1, Date fecha2 , String organizacion) {
		 
		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());
			
			List<Cliente> clientes;
			clientes = tm.consultarConsumoRFC11(alojamiento, fecha1, fecha2, organizacion);
			return Response.status(200).entity(clientes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	
}
