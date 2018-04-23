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

import dao.DAOMenaje;
import tm.AlohaAndesTransactionManager;
import vos.Menaje;
import vos.Menaje;

@Path("menaje")
public class MenajeService {

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
	 * Metodo GET que trae a todos los Menajees en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohaAndes/rest/Menajees <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los Menajees que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getMenajees() {

		try {
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager(getPath());

			List<Menaje> menajes;
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			menajes = tm.getAllMenajes();
			return Response.status(200).entity(menajes).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al Menaje en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialAlohaAndes/rest/Menajees/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Menaje que contiene al Menaje cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getMenajeById( @PathParam( "id" ) int id )
	{
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );

			Menaje Menaje = tm.getMenajeById( id );
			return Response.status( 200 ).entity( Menaje ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}



	/**
	 * Metodo que recibe un Menaje en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Menaje. <br/>
	 * <b>URL: </b> http://localhost:8080/AlohAndesFinal/rest/menajes <br/>
	 * @param Menaje JSON con la informacion del Menaje que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Menaje que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMenaje(Menaje Menaje) {
		
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addMenaje(Menaje);;
			return Response.status( 200 ).entity( Menaje ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * Metodo POST que recibe un Menaje en formato JSON y lo agrega a la Base de Datos unicamente 
	 * si el número de menajes que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al Menaje. <br/>
	 * <b>URL: </b> http://localhost:8080/AlohAndesFinal/rest/menajes <br/>
	 * @param cantidadMaxima representa la cantidad maxima de menajes que pueden haber en la misma ciudad
	 * @return	<b>Response Status 200</b> - JSON que contiene al Menaje que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Path( "restriccionCantidad" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMenajeWithLimitations(Menaje Menaje) {
		
			try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.addMenajeWithLimitations(Menaje);;
			return Response.status( 200 ).entity( Menaje ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	

	/**
	 * Metodo que recibe un Menaje en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al Menaje.<br/>
	 * @param Menaje JSON con la informacion del Menaje que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Menaje que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */

	@PUT
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenaje(Menaje Menaje) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.updateMenaje(Menaje);
			return Response.status( 200 ).entity( Menaje ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un Menaje en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al Menaje con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/AlohAndesFinal/rest/menajes <br/>
	 * @param Menaje JSON con la informacion del Menaje que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al Menaje que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	
	@DELETE
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMenaje(Menaje Menaje) {
		try{
			AlohaAndesTransactionManager tm = new AlohaAndesTransactionManager( getPath( ) );
			
			tm.deleteMenaje(Menaje);
			return Response.status( 200 ).entity( Menaje ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	
	}

}
