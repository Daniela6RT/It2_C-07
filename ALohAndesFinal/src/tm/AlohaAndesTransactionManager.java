package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import dao.DAOApartamento;
import dao.DAOCliente;
import dao.DAOContrato;
import dao.DAOHabitacion;
import dao.DAOMenaje;
import dao.DAOProveedorAlojamiento;
import dao.DAOSeguro;
import dao.DAOServicio;
import dao.DAOVivienda;
import dao.DAOApartamento;
import vos.Apartamento;
import vos.Cliente;
import vos.Contrato;
import vos.ContratoApartamento;
import vos.ContratoHabitacion;
import vos.ContratoVivienda;
import vos.Habitacion;
import vos.Menaje;
import vos.ProveedorAlojamiento;
import vos.Seguro;
import vos.Servicio;
import vos.Vivienda;
import vos.Apartamento;

public class AlohaAndesTransactionManager
{
	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private static String CONNECTION_DATA_PATH;


	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	private Connection conn;


	/**
	 * <b>Metodo Contructor de la Clase ParranderosTransactionManager</b> <br/>
	 * <b>Postcondicion: </b>	Se crea un objeto  ParranderosTransactionManager,
	 * 						 	Se inicializa el path absoluto del archivo de conexion,
	 * 							Se inicializna los atributos para la conexion con la Base de Datos
	 * @param contextPathP Path absoluto que se encuentra en el servidor del contexto del deploy actual
	 * @throws IOException Se genera una excepcion al tener dificultades con la inicializacion de la conexion<br/>
	 * @throws ClassNotFoundException 
	 */
	public AlohaAndesTransactionManager(String contextPathP) {

		try {
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * @throws IOException Se genera una excepcion al no encontrar el archivo o al tener dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException 
	 */
	private void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(new File(AlohaAndesTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();

		properties.load(fileInputStream);
		fileInputStream.close();

		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");


	}

	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han sido inicializados<br/>
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de datos
	 * @throws SQLException Cualquier error que se pueda llegar a generar durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("[PARRANDEROS APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}


	/**
	 * Metodo que modela la transaccion que retorna todos los apartamentos de la base de datos. <br/>
	 * @return List<Apartamento> - Lista de apartamentos que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Apartamento> getAllApartamentos() throws Exception {
		DAOApartamento daoApartamento = new DAOApartamento();
		List<Apartamento> apartamentos;
		try 
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);
			apartamentos = daoApartamento.getApartamentos();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return apartamentos;
	}

	/**
	 * Metodo que modela la transaccion que busca el Apartamento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Apartamento a buscar. id != null
	 * @return Apartamento - Apartamento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Apartamento getApartamentoById(int id) throws Exception {
		DAOApartamento daoApartamento = new DAOApartamento();
		Apartamento Apartamento = null;
		try 
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);
			Apartamento = daoApartamento.findApartamentoById(id);
			if(Apartamento == null)
			{
				throw new Exception("El Apartamento con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Apartamento;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Apartamento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Apartamento que entra como parametro <br/>
	 * @param Apartamento - el Apartamento a agregar. Apartamento != null
	 * @throws Exception - Cualquier error que se genere agregando el Apartamento
	 */
	public void addApartamento(Apartamento Apartamento) throws Exception 
	{

		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{

			this.conn = darConexion();
			daoApartamento.setConn(conn);

			daoApartamento.addApartamento(Apartamento);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Apartamento a la base de datos  <br/>
	 * unicamente si el número de apartamentos que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Apartamento que entra como parametro  <br/>
	 * @param Apartamento - el Apartamento a agregar. Apartamento != null
	 * @param cantidadMaxima -representa la cantidad maxima de apartamentos que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Apartamento
	 */
	public void addApartamentoWithLimitations(Apartamento Apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);

			if(daoApartamento.findApartamentoById(Apartamento.getidApartamento())==null && Apartamento.getIdProveedor()!= 0)
			{
				addApartamento(Apartamento);
			}
			else
			{
				throw new Exception ("Ese id ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Apartamento que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Apartamento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Apartamento que entra como parametro <br/>
	 * @param Apartamento - Apartamento a actualizar. Apartamento != null
	 * @throws Exception - Cualquier error que se genere actualizando al Apartamento.
	 */
	public void updateApartamento(Apartamento Apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn( conn );

			if(daoApartamento.findApartamentoById(Apartamento.getidApartamento())==null)
			{
				throw new Exception ("no existe el apartamento");
			}
			else
			{
				daoApartamento.updateApartamento(Apartamento);
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Apartamento que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Apartamento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Apartamento que entra por parametro <br/>
	 * @param Apartamento - Apartamento a eliminar. Apartamento != null
	 * @throws Exception - Cualquier error que se genere eliminando al Apartamento.
	 */
	public void deleteApartamento(Apartamento Apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn( conn );


			if(daoApartamento.findApartamentoById(Apartamento.getidApartamento())==null)
			{
				throw new Exception ("no existe el apartamento");
			}
			else
			{
				daoApartamento.deleteApartamento(Apartamento);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	/**
	 * Metodo que modela la transaccion que retorna todos los clientes de la base de datos. <br/>
	 * @return List<Cliente> - Lista de clientes que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Cliente> getAllClientes() throws Exception {
		DAOCliente daoCliente = new DAOCliente();
		List<Cliente> clientes;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			clientes = daoCliente.getClientes();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes;
	}

	/**
	 * Metodo que modela la transaccion que busca el Cliente en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Cliente a buscar. id != null
	 * @return Cliente - Cliente que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Cliente getClienteById(int id) throws Exception {
		DAOCliente daoCliente = new DAOCliente();
		Cliente Cliente = null;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			Cliente = daoCliente.findClienteById(id);
			if(Cliente == null)
			{
				throw new Exception("El Cliente con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Cliente;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Cliente a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Cliente que entra como parametro <br/>
	 * @param Cliente - el Cliente a agregar. Cliente != null
	 * @throws Exception - Cualquier error que se genere agregando el Cliente
	 */
	public void addCliente(Cliente Cliente) throws Exception 
	{

		DAOCliente daoCliente = new DAOCliente( );
		try
		{

			this.conn = darConexion();
			daoCliente.setConn(conn);

			daoCliente.addCliente(Cliente);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Cliente a la base de datos  <br/>
	 * unicamente si el número de clientes que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Cliente que entra como parametro  <br/>
	 * @param Cliente - el Cliente a agregar. Cliente != null
	 * @param cantidadMaxima -representa la cantidad maxima de clientes que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Cliente
	 */
	public void addClienteWithLimitations(Cliente Cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente( );
		try
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);

			if(daoCliente.findClienteById(Cliente.getIdCliente())==null && daoCliente.findClienteByLogin(Cliente.getLogin())==null)
			{
				addCliente(Cliente);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Cliente que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Cliente en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Cliente que entra como parametro <br/>
	 * @param Cliente - Cliente a actualizar. Cliente != null
	 * @throws Exception - Cualquier error que se genere actualizando al Cliente.
	 */
	public void updateCliente(Cliente Cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente( );
		try
		{
			this.conn = darConexion();
			daoCliente.setConn( conn );

			if(daoCliente.findClienteById(Cliente.getIdCliente())==null)
			{
				throw new Exception ("no existe el Cliente");
			}
			else
			{
				daoCliente.updateCliente(Cliente);
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Cliente que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Cliente en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Cliente que entra por parametro <br/>
	 * @param Cliente - Cliente a eliminar. Cliente != null
	 * @throws Exception - Cualquier error que se genere eliminando al Cliente.
	 */
	public void deleteCliente(Cliente Cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente( );
		try
		{
			this.conn = darConexion();
			daoCliente.setConn( conn );


			if(daoCliente.findClienteById(Cliente.getIdCliente())==null)
			{
				throw new Exception ("no existe el Cliente");
			}
			else
			{
				daoCliente.deleteCliente(Cliente);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}



	/**
	 * Metodo que modela la transaccion que retorna todos los habitaciones de la base de datos. <br/>
	 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Habitacion> getAllHabitaciones() throws Exception {
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		List<Habitacion> habitaciones;
		try 
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);
			habitaciones = daoHabitacion.getHabitaciones();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return habitaciones;
	}

	/**
	 * Metodo que modela la transaccion que busca el Habitacion en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Habitacion a buscar. id != null
	 * @return Habitacion - Habitacion que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Habitacion getHabitacionById(int id) throws Exception {
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		Habitacion Habitacion = null;
		try 
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);
			Habitacion = daoHabitacion.findHabitacionById(id);
			if(Habitacion == null)
			{
				throw new Exception("El Habitacion con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Habitacion;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Habitacion a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Habitacion que entra como parametro <br/>
	 * @param Habitacion - el Habitacion a agregar. Habitacion != null
	 * @throws Exception - Cualquier error que se genere agregando el Habitacion
	 */
	public void addHabitacion(Habitacion Habitacion) throws Exception 
	{

		DAOHabitacion daoHabitacion = new DAOHabitacion( );
		try
		{

			this.conn = darConexion();
			daoHabitacion.setConn(conn);

			daoHabitacion.addHabitacion(Habitacion);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Habitacion a la base de datos  <br/>
	 * unicamente si el número de habitaciones que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Habitacion que entra como parametro  <br/>
	 * @param Habitacion - el Habitacion a agregar. Habitacion != null
	 * @param cantidadMaxima -representa la cantidad maxima de habitaciones que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Habitacion
	 */
	public void addHabitacionWithLimitations(Habitacion Habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion( );
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);

			if(daoHabitacion.findHabitacionById(Habitacion.getIdHabitacion())==null && Integer.parseInt(Habitacion.getHoraApertura())<Integer.parseInt(Habitacion.getHoraCierre())&&Habitacion.getTamanio()!=0 && Habitacion.getCapacidad()!=0 && Habitacion.getIdProveedor()!=0)
			{
				addHabitacion(Habitacion);
			}
			else
			{
				throw new Exception ("Ese id ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Habitacion que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Habitacion en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Habitacion que entra como parametro <br/>
	 * @param Habitacion - Habitacion a actualizar. Habitacion != null
	 * @throws Exception - Cualquier error que se genere actualizando al Habitacion.
	 */
	public void updateHabitacion(Habitacion Habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion( );
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn( conn );

			if(daoHabitacion.findHabitacionById(Habitacion.getIdHabitacion())==null)
			{
				throw new Exception ("no existe el Habitacion");
			}
			else if (Integer.parseInt(Habitacion.getHoraApertura())<Integer.parseInt(Habitacion.getHoraCierre())&&Habitacion.getTamanio()!=0 && Habitacion.getCapacidad()!=0 && Habitacion.getIdProveedor()!=0)
			{
				daoHabitacion.updateHabitacion(Habitacion);
			}
			else {
				throw new Exception ("no cumple con las reglas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Habitacion que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Habitacion en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Habitacion que entra por parametro <br/>
	 * @param Habitacion - Habitacion a eliminar. Habitacion != null
	 * @throws Exception - Cualquier error que se genere eliminando al Habitacion.
	 */
	public void deleteHabitacion(Habitacion Habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion( );
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn( conn );


			if(daoHabitacion.findHabitacionById(Habitacion.getIdHabitacion())==null)
			{
				throw new Exception ("no existe el Habitacion");
			}
			else
			{
				daoHabitacion.deleteHabitacion(Habitacion);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}


	/**
	 * Metodo que modela la transaccion que retorna todos los viviendas de la base de datos. <br/>
	 * @return List<Vivienda> - Lista de viviendas que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Vivienda> getAllviviendas() throws Exception {
		DAOVivienda daoVivienda = new DAOVivienda();
		List<Vivienda> viviendas;
		try 
		{
			this.conn = darConexion();
			daoVivienda.setConn(conn);
			viviendas = daoVivienda.getViviendas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return viviendas;
	}

	/**
	 * Metodo que modela la transaccion que busca el Vivienda en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Vivienda a buscar. id != null
	 * @return Vivienda - Vivienda que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Vivienda getViviendaById(int id) throws Exception {
		DAOVivienda daoVivienda = new DAOVivienda();
		Vivienda Vivienda = null;
		try 
		{
			this.conn = darConexion();
			daoVivienda.setConn(conn);
			Vivienda = daoVivienda.findViviendaById(id);
			if(Vivienda == null)
			{
				throw new Exception("El Vivienda con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Vivienda;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Vivienda a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Vivienda que entra como parametro <br/>
	 * @param Vivienda - el Vivienda a agregar. Vivienda != null
	 * @throws Exception - Cualquier error que se genere agregando el Vivienda
	 */
	public void addVivienda(Vivienda Vivienda) throws Exception 
	{

		DAOVivienda daoVivienda = new DAOVivienda( );
		try
		{

			this.conn = darConexion();
			daoVivienda.setConn(conn);

			daoVivienda.addVivienda(Vivienda);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Vivienda a la base de datos  <br/>
	 * unicamente si el número de viviendas que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Vivienda que entra como parametro  <br/>
	 * @param Vivienda - el Vivienda a agregar. Vivienda != null
	 * @param cantidadMaxima -representa la cantidad maxima de viviendas que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Vivienda
	 */
	public void addViviendaWithLimitations(Vivienda Vivienda) throws Exception 
	{
		DAOVivienda daoVivienda = new DAOVivienda( );
		try
		{
			this.conn = darConexion();
			daoVivienda.setConn(conn);

			if(daoVivienda.findViviendaById(Vivienda.getIdVivienda())==null && Vivienda.getPrecioBase()!=0 && Vivienda.getIdProveedor()!=0&& Vivienda.getUbicacion()!=null )
			{
				addVivienda(Vivienda);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Vivienda que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Vivienda en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Vivienda que entra como parametro <br/>
	 * @param Vivienda - Vivienda a actualizar. Vivienda != null
	 * @throws Exception - Cualquier error que se genere actualizando al Vivienda.
	 */
	public void updateVivienda(Vivienda Vivienda) throws Exception 
	{
		DAOVivienda daoVivienda = new DAOVivienda( );
		try
		{
			this.conn = darConexion();
			daoVivienda.setConn( conn );

			if(daoVivienda.findViviendaById(Vivienda.getIdVivienda())==null)
			{
				throw new Exception ("no existe el Vivienda");
			}
			else if(Vivienda.getPrecioBase()!=0 && Vivienda.getIdProveedor()!=0&& Vivienda.getUbicacion()!=null)
			{
				daoVivienda.updateVivienda(Vivienda);
			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Vivienda que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Vivienda en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Vivienda que entra por parametro <br/>
	 * @param Vivienda - Vivienda a eliminar. Vivienda != null
	 * @throws Exception - Cualquier error que se genere eliminando al Vivienda.
	 */
	public void deleteVivienda(Vivienda Vivienda) throws Exception 
	{
		DAOVivienda daoVivienda = new DAOVivienda( );
		try
		{
			this.conn = darConexion();
			daoVivienda.setConn( conn );


			if(daoVivienda.findViviendaById(Vivienda.getIdVivienda())==null)
			{
				throw new Exception ("no existe el Vivienda");
			}
			else
			{
				daoVivienda.deleteVivienda(Vivienda);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoVivienda.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	/**
	 * Metodo que modela la transaccion que retorna todos los ProveedoresAlojamiento de la base de datos. <br/>
	 * @return List<ProveedorAlojamiento> - Lista de ProveedoresAlojamiento que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<ProveedorAlojamiento> getAllProveedoresAlojamiento() throws Exception {
		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento();
		List<ProveedorAlojamiento> ProveedoresAlojamiento;
		try 
		{
			this.conn = darConexion();
			daoProveedorAlojamiento.setConn(conn);
			ProveedoresAlojamiento = daoProveedorAlojamiento.getProveedoresAlojamiento();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ProveedoresAlojamiento;
	}

	/**
	 * Metodo que modela la transaccion que busca el ProveedorAlojamiento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del ProveedorAlojamiento a buscar. id != null
	 * @return ProveedorAlojamiento - ProveedorAlojamiento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public ProveedorAlojamiento getProveedorAlojamientoById(int id) throws Exception {
		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento();
		ProveedorAlojamiento ProveedorAlojamiento = null;
		try 
		{
			this.conn = darConexion();
			daoProveedorAlojamiento.setConn(conn);
			ProveedorAlojamiento = daoProveedorAlojamiento.findProveedorAlojamientoById(id);
			if(ProveedorAlojamiento == null)
			{
				throw new Exception("El ProveedorAlojamiento con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ProveedorAlojamiento;
	}


	/**
	 * Metodo que modela la transaccion que agrega un ProveedorAlojamiento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el ProveedorAlojamiento que entra como parametro <br/>
	 * @param ProveedorAlojamiento - el ProveedorAlojamiento a agregar. ProveedorAlojamiento != null
	 * @throws Exception - Cualquier error que se genere agregando el ProveedorAlojamiento
	 */
	public void addProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws Exception 
	{

		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento( );
		try
		{

			this.conn = darConexion();
			daoProveedorAlojamiento.setConn(conn);

			daoProveedorAlojamiento.addProveedorAlojamiento(ProveedorAlojamiento);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un ProveedorAlojamiento a la base de datos  <br/>
	 * unicamente si el número de ProveedoresAlojamiento que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el ProveedorAlojamiento que entra como parametro  <br/>
	 * @param ProveedorAlojamiento - el ProveedorAlojamiento a agregar. ProveedorAlojamiento != null
	 * @param cantidadMaxima -representa la cantidad maxima de ProveedoresAlojamiento que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el ProveedorAlojamiento
	 */
	public void addProveedorAlojamientoWithLimitations(ProveedorAlojamiento ProveedorAlojamiento) throws Exception 
	{
		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoProveedorAlojamiento.setConn(conn);

			if(daoProveedorAlojamiento.findProveedorAlojamientoById(ProveedorAlojamiento.getIdProveedor())==null && ProveedorAlojamiento.getNombre()!=null && (ProveedorAlojamiento.getTipoProveedorAlojamiento()==1||ProveedorAlojamiento.getTipoProveedorAlojamiento()==0)  )
			{
				addProveedorAlojamiento(ProveedorAlojamiento);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al ProveedorAlojamiento que entra por parametro.<br/>
	 * Solamente se actualiza si existe el ProveedorAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el ProveedorAlojamiento que entra como parametro <br/>
	 * @param ProveedorAlojamiento - ProveedorAlojamiento a actualizar. ProveedorAlojamiento != null
	 * @throws Exception - Cualquier error que se genere actualizando al ProveedorAlojamiento.
	 */
	public void updateProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws Exception 
	{
		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoProveedorAlojamiento.setConn( conn );

			if(daoProveedorAlojamiento.findProveedorAlojamientoById(ProveedorAlojamiento.getIdProveedor())==null)
			{
				throw new Exception ("no existe el ProveedorAlojamiento");
			}
			else if(ProveedorAlojamiento.getNombre()!=null && (ProveedorAlojamiento.getTipoProveedorAlojamiento()==1||ProveedorAlojamiento.getTipoProveedorAlojamiento()==0))
			{
				daoProveedorAlojamiento.updateProveedorAlojamiento(ProveedorAlojamiento);
			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al ProveedorAlojamiento que entra por parametro. <br/>
	 * Solamente se actualiza si existe el ProveedorAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el ProveedorAlojamiento que entra por parametro <br/>
	 * @param ProveedorAlojamiento - ProveedorAlojamiento a eliminar. ProveedorAlojamiento != null
	 * @throws Exception - Cualquier error que se genere eliminando al ProveedorAlojamiento.
	 */
	public void deleteProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws Exception 
	{
		DAOProveedorAlojamiento daoProveedorAlojamiento = new DAOProveedorAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoProveedorAlojamiento.setConn( conn );


			if(daoProveedorAlojamiento.findProveedorAlojamientoById(ProveedorAlojamiento.getIdProveedor())==null)
			{
				throw new Exception ("no existe el ProveedorAlojamiento");
			}
			else
			{
				daoProveedorAlojamiento.deleteProveedorAlojamiento(ProveedorAlojamiento);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoProveedorAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}



	/**
	 * Metodo que modela la transaccion que retorna todos los Contratos de la base de datos. <br/>
	 * @return List<Contrato> - Lista de Contratos que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Contrato> getAllContratos() throws Exception {
		DAOContrato daoContrato = new DAOContrato();
		List<Contrato> Contratos;
		try 
		{
			this.conn = darConexion();
			daoContrato.setConn(conn);
			Contratos = daoContrato.getContratos();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Contratos;
	}

	/**
	 * Metodo que modela la transaccion que busca el Contrato en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Contrato a buscar. id != null
	 * @return Contrato - Contrato que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Contrato getContratoById(int id) throws Exception {
		DAOContrato daoContrato = new DAOContrato();
		Contrato Contrato = null;
		try 
		{
			this.conn = darConexion();
			daoContrato.setConn(conn);
			Contrato = daoContrato.findContratoById(id);
			if(Contrato == null)
			{
				throw new Exception("El Contrato con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Contrato;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Contrato a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Contrato que entra como parametro <br/>
	 * @param contrato - el Contrato a agregar. Contrato != null
	 * @throws Exception - Cualquier error que se genere agregando el Contrato
	 */
	public void addContrato(Contrato contrato) throws Exception 
	{

		
		///apartamento ,vivienda , cliente ,proveedoralojamiento y habitación
		
		DAOContrato daoContrato = new DAOContrato( );
		try
		{

			this.conn = darConexion();
			daoContrato.setConn(conn);

			Date fechaFinal= contrato.getFechaFinal();
			Date fechaInicial= contrato.getFechaInicio();
			int duracionContratoEnDias = (int)( (fechaFinal.getTime() - fechaInicial.getTime()) 
					/ (1000 * 60 * 60 * 24) );

			if(contrato.getTipo().equals(Contrato.HABITACION))
			{if(duracionContratoEnDias>=1){
				daoContrato.addContratoHabitacion((ContratoHabitacion) contrato);
			}
			}

			else if(contrato.getTipo().equals(Contrato.APARTAMENTO))
			{
				if(duracionContratoEnDias>=30){
					daoContrato.addContratoApartamentoContratos((ContratoApartamento) contrato);
				}
			}

			else if(contrato.getTipo().equals(Contrato.VIVIENDA))
			{
				if(duracionContratoEnDias>=30){
					daoContrato.addContratoViviendaContratos((ContratoVivienda) contrato);
				}
			}

		}

		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Contrato a la base de datos  <br/>
	 * unicamente si el número de Contratos que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Contrato que entra como parametro  <br/>
	 * @param Contrato - el Contrato a agregar. Contrato != null
	 * @param cantidadMaxima -representa la cantidad maxima de Contratos que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Contrato
	 */
	public void addContratoWithLimitations(Contrato Contrato) throws Exception 
	{
		DAOContrato daoContrato = new DAOContrato( );
		try
		{
			this.conn = darConexion();
			daoContrato.setConn(conn);

			if(daoContrato.findContratoById(Contrato.getIdProveedor())==null )
			{
				addContrato(Contrato);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Contrato que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Contrato en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Contrato que entra como parametro <br/>
	 * @param Contrato - Contrato a actualizar. Contrato != null
	 * @throws Exception - Cualquier error que se genere actualizando al Contrato.
	 */
	public void updateContrato(Contrato Contrato) throws Exception 
	{
		DAOContrato daoContrato = new DAOContrato( );
		try
		{
			this.conn = darConexion();
			daoContrato.setConn( conn );

			if(daoContrato.findContratoById(Contrato.getIdProveedor())==null)
			{
				throw new Exception ("no existe el Contrato");
			}
			//			else if(Contrato.getNombre()!=null && (Contrato.getTipoContrato()==1||Contrato.getTipoContrato()==0))
			//			{
			//				daoContrato.updateContrato(Contrato);
			//			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Contrato que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Contrato en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Contrato que entra por parametro <br/>
	 * @param Contrato - Contrato a eliminar. Contrato != null
	 * @throws Exception - Cualquier error que se genere eliminando al Contrato.
	 */
	public void deleteContrato(Contrato Contrato) throws Exception 
	{
		DAOContrato daoContrato = new DAOContrato( );
		try
		{
			this.conn = darConexion();
			daoContrato.setConn( conn );


			if(daoContrato.findContratoById(Contrato.getIdProveedor())==null)
			{
				throw new Exception ("no existe el Contrato");
			}
			else
			{
				daoContrato.deleteContrato(Contrato);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}



	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Contrato que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Contrato en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Contrato que entra por parametro <br/>
	 * @param Contrato - Contrato a eliminar. Contrato != null
	 * @throws Exception - Cualquier error que se genere eliminando al Contrato.
	 */
	public void cancelarContrato(Contrato contrato) throws Exception 
	{
		DAOContrato daoContrato = new DAOContrato( );
		try
		{
			this.conn = darConexion();
			daoContrato.setConn( conn );


			if(daoContrato.findContratoById(contrato.getIdProveedor())==null)
			{
				throw new Exception ("no existe el Contrato");
			}
			else
			{
			//	if()
				daoContrato.cancelarContrato(contrato);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoContrato.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}





	/**
	 * Metodo que modela la transaccion que retorna todos los Menajes de la base de datos. <br/>
	 * @return List<Menaje> - Lista de Menajes que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Menaje> getAllMenajes() throws Exception {
		DAOMenaje daoMenaje = new DAOMenaje();
		List<Menaje> Menajes;
		try 
		{
			this.conn = darConexion();
			daoMenaje.setConn(conn);
			Menajes = daoMenaje.getMenajes();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Menajes;
	}

	/**
	 * Metodo que modela la transaccion que busca el Menaje en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Menaje a buscar. id != null
	 * @return Menaje - Menaje que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Menaje getMenajeById(int id) throws Exception {
		DAOMenaje daoMenaje = new DAOMenaje();
		Menaje Menaje = null;
		try 
		{
			this.conn = darConexion();
			daoMenaje.setConn(conn);
			Menaje = daoMenaje.findMenajeById(id);
			if(Menaje == null)
			{
				throw new Exception("El Menaje con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Menaje;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Menaje a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Menaje que entra como parametro <br/>
	 * @param Menaje - el Menaje a agregar. Menaje != null
	 * @throws Exception - Cualquier error que se genere agregando el Menaje
	 */
	public void addMenaje(Menaje Menaje) throws Exception 
	{

		DAOMenaje daoMenaje = new DAOMenaje( );
		try
		{

			this.conn = darConexion();
			daoMenaje.setConn(conn);

			daoMenaje.addMenaje(Menaje);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Menaje a la base de datos  <br/>
	 * unicamente si el número de Menajes que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Menaje que entra como parametro  <br/>
	 * @param Menaje - el Menaje a agregar. Menaje != null
	 * @param cantidadMaxima -representa la cantidad maxima de Menajes que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Menaje
	 */
	public void addMenajeWithLimitations(Menaje Menaje) throws Exception 
	{
		DAOMenaje daoMenaje = new DAOMenaje( );
		try
		{
			this.conn = darConexion();
			daoMenaje.setConn(conn);

			if(Menaje.getNombre()!=null )
			{
				addMenaje(Menaje);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Menaje que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Menaje en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Menaje que entra como parametro <br/>
	 * @param Menaje - Menaje a actualizar. Menaje != null
	 * @throws Exception - Cualquier error que se genere actualizando al Menaje.
	 */
	public void updateMenaje(Menaje Menaje) throws Exception 
	{
		DAOMenaje daoMenaje = new DAOMenaje( );
		try
		{
			this.conn = darConexion();
			daoMenaje.setConn( conn );

			if(daoMenaje.findMenajeById(Menaje.getId())==null)
			{
				throw new Exception ("no existe el Menaje");
			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Menaje que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Menaje en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Menaje que entra por parametro <br/>
	 * @param Menaje - Menaje a eliminar. Menaje != null
	 * @throws Exception - Cualquier error que se genere eliminando al Menaje.
	 */
	public void deleteMenaje(Menaje Menaje) throws Exception 
	{
		DAOMenaje daoMenaje = new DAOMenaje( );
		try
		{
			this.conn = darConexion();
			daoMenaje.setConn( conn );


			if(daoMenaje.findMenajeById(Menaje.getId())==null)
			{
				throw new Exception ("no existe el Menaje");
			}
			else
			{
				daoMenaje.deleteMenaje(Menaje);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoMenaje.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}











	/**
	 * Metodo que modela la transaccion que retorna todos los Seguros de la base de datos. <br/>
	 * @return List<Seguro> - Lista de Seguros que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Seguro> getAllSeguros() throws Exception {
		DAOSeguro daoSeguro = new DAOSeguro();
		List<Seguro> Seguros;
		try 
		{
			this.conn = darConexion();
			daoSeguro.setConn(conn);
			Seguros = daoSeguro.getSeguros();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Seguros;
	}

	/**
	 * Metodo que modela la transaccion que busca el Seguro en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Seguro a buscar. id != null
	 * @return Seguro - Seguro que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Seguro getSeguroById(int id) throws Exception {
		DAOSeguro daoSeguro = new DAOSeguro();
		Seguro Seguro = null;
		try 
		{
			this.conn = darConexion();
			daoSeguro.setConn(conn);
			Seguro = daoSeguro.findSeguroById(id);
			if(Seguro == null)
			{
				throw new Exception("El Seguro con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Seguro;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Seguro a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Seguro que entra como parametro <br/>
	 * @param Seguro - el Seguro a agregar. Seguro != null
	 * @throws Exception - Cualquier error que se genere agregando el Seguro
	 */
	public void addSeguro(Seguro Seguro) throws Exception 
	{

		DAOSeguro daoSeguro = new DAOSeguro( );
		try
		{

			this.conn = darConexion();
			daoSeguro.setConn(conn);

			daoSeguro.addSeguro(Seguro);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Seguro a la base de datos  <br/>
	 * unicamente si el número de Seguros que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Seguro que entra como parametro  <br/>
	 * @param seguro - el Seguro a agregar. Seguro != null
	 * @param cantidadMaxima -representa la cantidad maxima de Seguros que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Seguro
	 */
	public void addSeguroWithLimitations(Seguro seguro) throws Exception 
	{
		DAOSeguro daoSeguro = new DAOSeguro( );
		try
		{
			this.conn = darConexion();
			daoSeguro.setConn(conn);

			if(daoSeguro.findSeguroById(seguro.getId())==null )
			{
				addSeguro(seguro);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Seguro que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Seguro en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Seguro que entra como parametro <br/>
	 * @param seguro - Seguro a actualizar. Seguro != null
	 * @throws Exception - Cualquier error que se genere actualizando al Seguro.
	 */
	public void updateSeguro(Seguro seguro) throws Exception 
	{
		DAOSeguro daoSeguro = new DAOSeguro( );
		try
		{
			this.conn = darConexion();
			daoSeguro.setConn( conn );

			if(daoSeguro.findSeguroById(seguro.getId())==null)
			{
				throw new Exception ("no existe el Seguro");
			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Seguro que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Seguro en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Seguro que entra por parametro <br/>
	 * @param seguro - Seguro a eliminar. Seguro != null
	 * @throws Exception - Cualquier error que se genere eliminando al Seguro.
	 */
	public void deleteSeguro(Seguro seguro) throws Exception 
	{
		DAOSeguro daoSeguro = new DAOSeguro( );
		try
		{
			this.conn = darConexion();
			daoSeguro.setConn( conn );


			if(daoSeguro.findSeguroById(seguro.getId())==null)
			{
				throw new Exception ("no existe el Seguro");
			}
			else
			{
				daoSeguro.deleteSeguro(seguro);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoSeguro.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	/**
	 * Metodo que modela la transaccion que retorna todos los servicios de la base de datos. <br/>
	 * @return List<Servicio> - Lista de servicios que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Servicio> getAllServicios() throws Exception {
		DAOServicio daoServicio = new DAOServicio();
		List<Servicio> servicios;
		try 
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);
			servicios = daoServicio.getServicios();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return servicios;
	}

	/**
	 * Metodo que modela la transaccion que busca el Servicio en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del Servicio a buscar. id != null
	 * @return Servicio - Servicio que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Servicio getServicioById(int id) throws Exception {
		DAOServicio daoServicio = new DAOServicio();
		Servicio Servicio = null;
		try 
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);
			Servicio = daoServicio.findServicioById(id);
			if(Servicio == null)
			{
				throw new Exception("El Servicio con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return Servicio;
	}


	/**
	 * Metodo que modela la transaccion que agrega un Servicio a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el Servicio que entra como parametro <br/>
	 * @param Servicio - el Servicio a agregar. Servicio != null
	 * @throws Exception - Cualquier error que se genere agregando el Servicio
	 */
	public void addServicio(Servicio Servicio) throws Exception 
	{

		DAOServicio daoServicio = new DAOServicio( );
		try
		{

			this.conn = darConexion();
			daoServicio.setConn(conn);

			daoServicio.addServicio(Servicio);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega un Servicio a la base de datos  <br/>
	 * unicamente si el número de servicios que existen en su ciudad es menor la constante CANTIDAD_MAXIMA <br/>
	 * <b> post: </b> Si se cumple la condicion, se ha agregado el Servicio que entra como parametro  <br/>
	 * @param Servicio - el Servicio a agregar. Servicio != null
	 * @param cantidadMaxima -representa la cantidad maxima de servicios que pueden haber en la misma ciudad
	 * @throws Exception - Cualquier error que se genere agregando el Servicio
	 */
	public void addServicioWithLimitations(Servicio Servicio) throws Exception 
	{
		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);

			if(daoServicio.findServicioById(Servicio.getId())==null )
			{
				addServicio(Servicio);
			}
			else
			{
				throw new Exception ("Ese id o login ya se esta utilizando");
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al Servicio que entra por parametro.<br/>
	 * Solamente se actualiza si existe el Servicio en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el Servicio que entra como parametro <br/>
	 * @param Servicio - Servicio a actualizar. Servicio != null
	 * @throws Exception - Cualquier error que se genere actualizando al Servicio.
	 */
	public void updateServicio(Servicio Servicio) throws Exception 
	{
		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn( conn );

			if(daoServicio.findServicioById(Servicio.getId())==null)
			{
				throw new Exception ("no existe el Servicio");
			}
			else 
			{
				throw new Exception ("no cumple con las normas del negocio");
			}

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al Servicio que entra por parametro. <br/>
	 * Solamente se actualiza si existe el Servicio en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el Servicio que entra por parametro <br/>
	 * @param Servicio - Servicio a eliminar. Servicio != null
	 * @throws Exception - Cualquier error que se genere eliminando al Servicio.
	 */
	public void deleteServicio(Servicio Servicio) throws Exception 
	{
		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn( conn );


			if(daoServicio.findServicioById(Servicio.getId())==null)
			{
				throw new Exception ("no existe el Servicio");
			}
			else
			{
				daoServicio.deleteServicio(Servicio);
			}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	public void crearReserva(Integer idAlojamiento, Date fechaInicio, Date fechaFinal, Integer idCliente, Integer idProveedor, Integer idContrato, String tipoAlojamiento, String servicios) throws Exception
	{	
		Contrato reserva = null;
		
		if(tipoAlojamiento.equals("Apartamento"))
		{
			 reserva = new ContratoApartamento(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, "En curso",idAlojamiento);
		}
		else if(tipoAlojamiento.equals("Vivienda")) 
		{
			 reserva = new ContratoVivienda(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, "En curso",idAlojamiento);
		}
		else if(tipoAlojamiento.equals("Habitacion"))
		{
			 reserva = new ContratoHabitacion(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, "En curso",idAlojamiento);
		}
		
		addContrato(reserva);
	}
	public void cancelarReserva(Integer idContrato) throws SQLException, Exception 
	{
		DAOContrato daoContrato = new DAOContrato( );
		
			
			this.conn = darConexion();
			daoContrato.setConn( conn );
			Contrato contrato = null;
			contrato = daoContrato.findContratoById(idContrato);
			contrato.setCancelado();
			
			updateContrato(contrato);
	}

}
