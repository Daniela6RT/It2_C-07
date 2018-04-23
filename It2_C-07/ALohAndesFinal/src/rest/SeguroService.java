/**-------------------------------------------------------------------
 * ISIS2304 - Sistemas Transaccionales
 * Departamento de Ingenieria de Sistemas
 * Universidad de los Andes
 * Bogota, Colombia
 * 
 * Actividad: Tutorial AlohAndes: Arquitectura
 * Autores:
 * 			Santiago Cortes Fernandez	-	s.cortes@uniandes.edu.co
 * 			Juan David Vega Guzman		-	jd.vega11@uniandes.edu.co
 * -------------------------------------------------------------------
 */
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

import dao.DAOSeguro;
import tm.AlohaAndesTransactionManager;
import vos.Seguro;
import vos.Seguro;

/**
 * @author Santiago Cortes Fernandez 	- 	s.cortes@uniandes.edu.co
 * @author Juan David Vega Guzman		-	jd.vega11@uniandes.edu.co
 * Clase que expone servicios REST con ruta base: http://localhost:8080/TutorialAlohAndes/rest/Seguroes/...
 */
@Path("seguros")
public class SeguroService {

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
	 * Metodo GET que trae a todos los Seguroes en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohAndes/rest/Seguroes <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los Seguroes que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSeguroes() {

		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());

			List<Seguro> Seguroes;
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			Seguroes = tm.getAllSeguros();
			return Response.status(200).entity(Seguroes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al Seguro en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohAndes/rest/Seguroes/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Seguro que contiene al Seguro cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getSeguroById( @PathParam( "id" ) int id )
	{
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );

			Seguro Seguro = tm.getSeguroById( id );
			return Response.status( 200 ).entity( Seguro ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	/**
	 * Metodo que recibe un Seguro en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Seguro. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/seguros <br/>
	 * @param Seguro JSON con la informacion del Seguro que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Seguro que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSeguro(Seguro Seguro) {
		
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addSeguro(Seguro);;
			return Response.status( 200 ).entity( Seguro ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo POST que recibe un Seguro en formato JSON y lo agrega a la Base de Datos unicamente 
	 * si el número de seguros que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Seguro. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/seguros <br/>
	 * @param cantidadMaxima representa la cantidad maxima de seguros que pueden haber en la misma ciudad
	 * @return	<b>Response Status 200</b> - JSON que contiene al Seguro que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Path( "restriccionCantidad" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSeguroWithLimitations(Seguro Seguro) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addSeguroWithLimitations(Seguro);;
			return Response.status( 200 ).entity( Seguro ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	

	/**
	 * Metodo que recibe un Seguro en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al Seguro.<br/>
	 * @param Seguro JSON con la informacion del Seguro que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Seguro que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */

	@PUT
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSeguro(Seguro Seguro) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.updateSeguro(Seguro);
			return Response.status( 200 ).entity( Seguro ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un Seguro en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al Seguro con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/seguros <br/>
	 * @param Seguro JSON con la informacion del Seguro que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Seguro que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSeguro(Seguro Seguro) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteSeguro(Seguro);
			return Response.status( 200 ).entity( Seguro ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}

}
