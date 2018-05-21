package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.Alojamiento;
import vos.Apartamento;

public class DAOAlojamiento 
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
	 * Metodo constructor de la clase DAOAlojamiento <br/>
	*/
	public DAOAlojamiento() {
		recursos = new ArrayList<Object>();
	}
	

	/**
	 * Metodo que obtiene la informacion de todas las Alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las Alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Alojamiento> getAlojamientos() throws SQLException, Exception {
		ArrayList<Alojamiento> Alojamientos = new ArrayList<Alojamiento>();

		String sql = String.format("SELECT * FROM %1$s.Alojamientos WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Alojamientos.add(convertResultSetToAlojamiento(rs));
		}
		return Alojamientos;
	}

	/**
	 * Metodo que obtiene la informacion de la habitacio en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la Alojamiento
	 * @return la informacion de la Alojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la Alojamiento conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Alojamiento findAlojamientoById(int id) throws SQLException, Exception 
	{
		Alojamiento Alojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.Alojamientos WHERE IDAlojamiento = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Alojamiento = convertResultSetToAlojamiento(rs);
		}

		return Alojamiento;
	}
	
	/**
	 * Metodo que agregar la informacion de una nueva Alojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param Alojamiento Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.ALOJAMIENTOS (IDALOJAMIENTO, TIPO, IDTIPO) VALUES (%2$s, '%3$s', '%4$s'", 
									USUARIO, 
									Alojamiento.getIdAlojamiento(),
									Alojamiento.getTipo(),
									Alojamiento.getIdTipo());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza la informacion de la Alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacio Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.Alojamientos ", USUARIO));
		sql.append (String.format (
				"SET TIPO = '%1$s', IDTIPO = '%2$s'",
				Alojamiento.getTipo(), Alojamiento.getIdTipo()));
		sql.append ("WHERE IDALOJAMIENTO = " + Alojamiento.getIdAlojamiento());
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion de la Alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.Alojamientos WHERE IDALOJAMIENTO = %2$d", USUARIO, Alojamiento.getIdAlojamiento());

		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public Apartamento getApartamento() throws SQLException
	{

		String sql = String.format("SELECT APARTAMENTOS.* FROM %1$s.ALOJAMIENTOS  JOIN APARTAMENTOS ON ALOJAMIENTOS.IDALOJAMIENTO = APARTAMENTOS.IDAPARTAMENTO", USUARIO); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		Apartamento apa = convertResultSetToApartamento(rs);
		return apa;
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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla Alojamientos) en una instancia de la clase Alojamiento.
	 * @param resultSet ResultSet con la informacion de una Alojamiento que se obtuvo de la base de datos.
	 * @return Alojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla Alojamientos.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Alojamiento convertResultSetToAlojamiento(ResultSet resultSet) throws SQLException {
	

		int idAlojamiento = Integer.parseInt(resultSet.getString("IDAlojamiento"));
		String tipo = resultSet.getString("TIPO");
		int idTipo = Integer.parseInt(resultSet.getString("IDTIPO"));

		Alojamiento aloj = new Alojamiento(idAlojamiento, tipo, idTipo);

		return aloj;
	}
	
	public Apartamento convertResultSetToApartamento(ResultSet resultSet) throws SQLException {
		

		int idApartamento = Integer.parseInt(resultSet.getString("idApartamento"));
		String amoblado = resultSet.getString("amoblado");
		int precioBase = Integer.parseInt(resultSet.getString("precioBase"));
		int habitaciones = Integer.parseInt(resultSet.getString("habitaciones"));
		int idProveedor = Integer.parseInt(resultSet.getString("idProveedor"));
		

		Apartamento apar = new Apartamento(idApartamento,amoblado, precioBase, habitaciones, idProveedor);

		return apar;
	}
	
}
