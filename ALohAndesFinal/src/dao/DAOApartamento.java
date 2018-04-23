package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Apartamento;


public class DAOApartamento 
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
	 * Metodo constructor de la clase DAOApartamento <br/>
	*/
	public DAOApartamento() {
		recursos = new ArrayList<Object>();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos los apartamentos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los apartamentos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Apartamento> getApartamentos() throws SQLException, Exception {
		ArrayList<Apartamento> apartamentos = new ArrayList<Apartamento>();

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			apartamentos.add(convertResultSetToApartamento(rs));
		}
		return apartamentos;
	}


	/**
	 * Metodo que obtiene la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del apartamento
	 * @return la informacion del apartamento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el apartamento conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Apartamento findApartamentoById(int id) throws SQLException, Exception 
	{
		Apartamento apartamento = null;

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS WHERE ID = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			apartamento = convertResultSetToApartamento(rs);
		}

		return apartamento;
	}
	
	/**
	 * Metodo que agregar la informacion de un nuevo apartamento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.APARTAMENTO (IDAPARTAMENTO, AMOBLADO, HABITACIONES, PRECIOBASE,IDPROVEEDOR) VALUES (%2$s, '%3$s', '%4$s', '%5$s','%6$s')", 
									USUARIO, 
									apartamento.getidApartamento(), 
									apartamento.getAmoblado(),
									apartamento.getHabitaciones(), 
									apartamento.getPrecioBase(),
									apartamento.getIdProveedor());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateApartamento(Apartamento apartamento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.APARTAMENTOS ", USUARIO));
		sql.append (String.format (
				"SET AMOBLADO = '%1$s', HABITACIONES = '%2$s', PRECIOBASE = '%3$s',IDPROVEEDOR = '%4$s' ",
				apartamento.getAmoblado(), apartamento.getHabitaciones(),
				apartamento.getPrecioBase(),apartamento.getIdProveedor()));
		sql.append ("WHERE ID = " + apartamento.getidApartamento());
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea borrar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.APARTAMENTO WHERE IDAPARTAMENTO = %2$d", USUARIO, apartamento.getidApartamento());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla APARTAMENTOS) en una instancia de la clase Apartamento.
	 * @param resultSet ResultSet con la informacion de un apartamento que se obtuvo de la base de datos.
	 * @return Apartamento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla APARTAMENTOS.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Apartamento convertResultSetToApartamento(ResultSet resultSet) throws SQLException {
		//TODO Requerimiento 1G: Complete el metodo con los atributos agregados previamente en la clase Bebedor. 
		//						 Tenga en cuenta los nombres de las columnas de la Tabla en la Base de Datos (ID, NOMBRE, PRESUPUESTO, CIUDAD)

		int id = Integer.parseInt(resultSet.getString("IDAPARTAMENTO"));
		String amoblado = resultSet.getString("AMOBLADO");
		int precioBase =Integer.parseInt( resultSet.getString("PRECIOBASE"));
		int Habitaciones = Integer.parseInt(resultSet.getString("HABITACIONES"));
		int idProveedor = Integer.parseInt(resultSet.getString("IDPROVEEDOR"));
		Apartamento apto = new Apartamento(id, amoblado, precioBase, Habitaciones,idProveedor);

		return apto;
	}

}

