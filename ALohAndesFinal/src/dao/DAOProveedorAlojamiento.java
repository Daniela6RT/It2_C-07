
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.Alojamiento;
import vos.Cliente;
import vos.ProveedorAlojamiento;

public class DAOProveedorAlojamiento 
{
	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	//TODO Requerimiento 1H: Modifique la constante, reemplazando al ususario PARRANDEROS por su ususario de Oracle
	public final static String USUARIO = "ISIS2304A961810";


	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	private DAOCliente daoCliente;
	/**
	 * Metodo constructor de la clase DAOProveedorAlojamiento <br/>
	 */
	public DAOProveedorAlojamiento() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los ProveedoresAlojamiento en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los ProveedorAlojamiento que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<ProveedorAlojamiento> getProveedoresAlojamiento() throws SQLException, Exception {
		ArrayList<ProveedorAlojamiento> ProveedorAlojamiento = new ArrayList<ProveedorAlojamiento>();

		String sql = String.format("SELECT * FROM %1$s.PROVEEDORALOJAMIENTO WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ProveedorAlojamiento.add(convertResultSetToProveedorAlojamiento(rs));
		}
		return ProveedorAlojamiento;
	}


	/**
	 * Metodo que obtiene la informacion del ProveedorAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del ProveedorAlojamiento
	 * @return la informacion del ProveedorAlojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el ProveedorAlojamiento con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ProveedorAlojamiento findProveedorAlojamientoById(int id) throws SQLException, Exception 
	{
		ProveedorAlojamiento ProveedorAlojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.PROVEEDORALOJAMIENTO WHERE ID = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			ProveedorAlojamiento = convertResultSetToProveedorAlojamiento(rs);
		}

		return ProveedorAlojamiento;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo ProveedorAlojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param ProveedorAlojamiento Proveedor Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.PROVEEDORALOJAMIENTO (ID, NOMBRE, TIPO) VALUES (%2$s, '%3$s', '%4$s')", 
				USUARIO, 
				ProveedorAlojamiento.getIdProveedor(), 
				ProveedorAlojamiento.getNombre(),
				ProveedorAlojamiento.getTipoProveedorAlojamiento());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del ProveedorAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param ProveedorAlojamiento ProveedorAlojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.PROVEEDORALOJAMIENTO ", USUARIO));
		sql.append (String.format (
				"SET NOMBRE = '%1$s', TIPO = '%2$s'",
				ProveedorAlojamiento.getNombre(),
				ProveedorAlojamiento.getTipoProveedorAlojamiento()));
		sql.append ("WHERE ID = " + ProveedorAlojamiento.getIdProveedor ());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion del ProveedorAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param ProveedorAlojamiento ProveedorAlojamiento que desea borrar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteProveedorAlojamiento(ProveedorAlojamiento ProveedorAlojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.ProveedoresAlojamiento WHERE ID = %2$d", USUARIO, ProveedorAlojamiento.getIdProveedor());

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


	public ArrayList<Cliente> consultarConsumoAlohAndesRFC10(Alojamiento alojamiento, Date fecha1, Date fecha2 , String organizacion) throws SQLException {
		ArrayList<Cliente> respuesta=null;
		String sql = String.format("SELECT IDCLIENTE, NOMBRE, APELLIDO FROM\n" +
				"  (SELECT * FROM CLIENTE cl NATURAL JOIN (\n" +
				"select * from CONTRATOS  natural JOIN CONTRATOSAPARTAMENTOS ca\n" +
				"UNION\n" +
				"(select * from CONTRATOS natural JOIN CONTRATOSHABITACIONES ch)\n" +
				"UNION\n" +
				"(select * from CONTRATOS natural JOIN CONTRATOSVIVIENDAS cv)))\n" +
				"WHERE (IDAPARTAMENTO=%1%s) AND (ESTADO='En curso' OR ESTADO = 'Exitoso') AND\n" +
				"      (FECHAINICIO BETWEEN %2$d AND %3$d \n" +
				"      OR FECHAFIN BETWEEN %4$d AND %5$d)" +
				"      ORDER BY %6$d",
				fecha1,
				fecha2,
				fecha1,
				fecha2,
				organizacion);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			respuesta.add(daoCliente.convertResultSetToCliente(rs));
		}

		return respuesta;
	}

	public ArrayList<Cliente> consultarConsumoAlohAndesRFC11(Alojamiento alojamiento, Date fecha1, Date fecha2 , String organizacion) throws SQLException {
		ArrayList<Cliente> respuesta=null;

		String sql = String.format("SELECT IDCLIENTE, NOMBRE, APELLIDO FROM\r\n" + 
				"  (SELECT * FROM %1$s.CLIENTE NATURAL JOIN (\r\n" + 
				"select * from CONTRATOS  natural JOIN CONTRATOSAPARTAMENTOS ca\r\n" + 
				"UNION\r\n" + 
				"(select * from CONTRATOS natural JOIN CONTRATOSHABITACIONES ch)\r\n" + 
				"UNION\r\n" + 
				"(select * from CONTRATOS natural JOIN CONTRATOSVIVIENDAS cv)))\r\n" + 
				"WHERE (IDAPARTAMENTO<> %2$d) AND (ESTADO<>'En curso' OR ESTADO <> 'Exitoso') AND\r\n" + 
				"      (FECHAINICIO BETWEEN %3$d AND %4$d\r\n" + 
				"      OR FECHAFIN BETWEEN %5$d AND %6$d)\r\n" + 
				"        ORDER BY %7$d",
				USUARIO,
				alojamiento.getIdAlojamiento(),
				fecha1,
				fecha2,
				fecha1,
				fecha2,
				organizacion);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			respuesta.add(daoCliente.convertResultSetToCliente(rs));
		}

		return respuesta;
	}

	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla ProveedoresAlojamiento) en una instancia de la clase ProveedorAlojamiento.
	 * @param resultSet ResultSet con la informacion de un ProveedorAlojamiento que se obtuvo de la base de datos.
	 * @return ProveedorAlojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla ProveedoresAlojamiento.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public ProveedorAlojamiento convertResultSetToProveedorAlojamiento(ResultSet resultSet) throws SQLException {
		//TODO Requerimiento 1G: Complete el metodo con los atributos agregados previamente en la clase ProveedorAlojamiento. 
		//						 Tenga en cuenta los nombres de las columnas de la Tabla en la Base de Datos (ID, NOMBRE, PRESUPUESTO, CIUDAD)

		int id = Integer.parseInt(resultSet.getString("ID"));
		String nombre = resultSet.getString("PRESUPUESTO");
		int tipo = Integer.parseInt(resultSet.getString("CIUDAD"));

		ProveedorAlojamiento prov = new ProveedorAlojamiento(id, nombre, tipo);

		return prov;
	}
	/**
	 * Metodo que obtiene la informacion del dinero obtenido por los proveedores  <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los ProveedorAlojamiento que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList getDineroProveedores() throws SQLException, Exception {
		ArrayList ProveedorAlojamiento = new ArrayList<ProveedorAlojamiento>();

		String sql = String.format("	Select idproveedor, sum(costo), extract (year from fechainicio)\r\n" + 
				"	from contratos\r\n" + 
				"	group by idproveedor, extract (year from fechainicio);", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ProveedorAlojamiento.add(rs.getObject(sql));
		}
		return ProveedorAlojamiento;
	}
}