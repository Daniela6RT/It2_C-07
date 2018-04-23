package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import vos.Habitacion;

public class DAOHabitacion 
{
	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	
	public final static String USUARIO = "ISIS2304A961810";
	

	/**
	 * Arraylists de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;
	

	/**
	 * Metodo constructor de la clase DAOHabitacion <br/>
	*/
	public DAOHabitacion() {
		recursos = new ArrayList<Object>();
	}
	

	/**
	 * Metodo que obtiene la informacion de todas las habitaciones en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las habitaciones que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Habitacion> getHabitaciones() throws SQLException, Exception {
		ArrayList<Habitacion> habitaciones = new ArrayList<Habitacion>();

		String sql = String.format("SELECT * FROM %1$s.HABITACIONES WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			habitaciones.add(convertResultSetToHabitacion(rs));
		}
		return habitaciones;
	}

	/**
	 * Metodo que obtiene la informacion de la habitacio en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la habitacion
	 * @return la informacion de la habitacion que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la habitacion conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Habitacion findHabitacionById(int id) throws SQLException, Exception 
	{
		Habitacion habitacion = null;

		String sql = String.format("SELECT * FROM %1$s.HABITACIONES WHERE IDHABITACION = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			habitacion = convertResultSetToHabitacion(rs);
		}

		return habitacion;
	}
	
	/**
	 * Metodo que agregar la informacion de una nueva habitacion en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacion Habitacion que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHabitacion(Habitacion habitacion) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HABITACIONES (IDHABITACION, TIPO, PRECIOBASE, COMPARTIDA,HORAAPERTURA,MINAPERTURA,HORACIERRE,MINCIERRE,TAMAÑO,CAPACIDAD,IDPROVEEDOR, ESTADO) VALUES (%2$s, '%3$s', '%4$s', '%5$s','%6$s',%7$s','%8$s','%9$s','%10$s','%11$s','%12$s','%13$s')", 
									USUARIO, 
									habitacion.getIdHabitacion(), 
									habitacion.getTipo(),
									habitacion.getPrecioBase(), 
									habitacion.getCompartida(),
									habitacion.getHoraApertura(),
									habitacion.getMinApertura(),
									habitacion.getHoraCierre(),
									habitacion.getMinCierre(),
									habitacion.getTamanio(),
									habitacion.getCapacidad(),
									habitacion.getIdProveedor(),
									habitacion.getEstado());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza la informacion de la habitacion en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacio Habitacion que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHabitacion(Habitacion habitacion) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.HABITACIONES ", USUARIO));
		sql.append (String.format (
				"SET TIPO = '%1$s', PRECIOBASE = '%2$s', COMPARTIDA = '%3$s' ,HORAAPERTURA='%4$s',MINAPERTURA = '%5$s',HORACIERE='%6$s',MINCIERRE = '%7$s', TAMAÑO = '%8$s', CAPACIDAD ='%9$s', IDPROVEEDOR = '%10$s'",
				habitacion.getTipo(), habitacion.getPrecioBase(),
				habitacion.getCompartida(), habitacion.getHoraApertura(),habitacion.getMinApertura()
				, habitacion.getHoraCierre(), habitacion.getMinCierre(),
				habitacion.getTamanio(),habitacion.getCapacidad(),habitacion.getIdProveedor()));
		sql.append ("WHERE ID = " + habitacion.getIdHabitacion());
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion de la habitacion en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHabitacion(Habitacion habitacion) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HABITACIONES WHERE ID = %2$d", USUARIO, habitacion.getIdHabitacion());

		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	

	
	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * @param connection la conexion generada en el TransactionManager para la comunicacion con la Base de Datos
	 */
	public void setConn(Connection connection){
		this.conn = connection;
	}
	
	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido cerrados.
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}
	
	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla HABITACIONES) en una instancia de la clase Habitacion.
	 * @param resultSet ResultSet con la informacion de una habitacion que se obtuvo de la base de datos.
	 * @return Habitacion cuyos atributos corresponden a los valores asociados a un registro particular de la tabla HABITACIONES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Habitacion convertResultSetToHabitacion(ResultSet resultSet) throws SQLException {
	

		int id = Integer.parseInt(resultSet.getString("IDHABITACION"));
		String tipo = resultSet.getString("TIPO");
		int precioBase = Integer.parseInt(resultSet.getString("PRECIOBASE"));
		String compartida = resultSet.getString("COMPARTIDA");
		String horaApertura = resultSet.getString("HORAAPERTURA");
		String minApertura = resultSet.getString("MINAPERTURA");
		String horaCierre = resultSet.getString("HORACIERRE");
		String minCierre = resultSet.getString("MINCIERRE");
		int tamanio = Integer.parseInt(resultSet.getString("TAMAÑO"));
		int capacidad = Integer.parseInt(resultSet.getString("CAPACIDAD"));
		int idProveedor = Integer.parseInt(resultSet.getString("IDPROVEEDOR"));

		Habitacion hab = new Habitacion(id, tipo, precioBase, compartida,horaApertura,minApertura,horaCierre,minCierre,tamanio, capacidad, idProveedor);

		return hab;
	}
}
