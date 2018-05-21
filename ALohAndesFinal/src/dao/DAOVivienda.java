package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Apartamento;
import vos.Vivienda;

public class DAOVivienda 
{
	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	//TODO Requerimiento 1H: Modifique la constante, reemplazando al ususario PARRANDEROS por su ususario de Oracle
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
	 * Metodo constructor de la clase DAOVivieda <br/>
	*/
	public DAOVivienda() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que obtiene la informacion de todos los viviendas en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos las viviendas que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Vivienda> getViviendas() throws SQLException, Exception {
		ArrayList<Vivienda> viviendas = new ArrayList<Vivienda>();

		String sql = String.format("SELECT * FROM %1$s.VIVIENDAS WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			viviendas.add(convertResultSetToVivienda(rs));
		}
		return viviendas;
	}

	/**
	 * Metodo que obtiene la informacion de la vivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la vivienda
	 * @return la informacion de la vivienda que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la vivienda con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Vivienda findViviendaById(int id) throws SQLException, Exception 
	{
		Vivienda vivienda = null;

		String sql = String.format("SELECT * FROM %1$s.VIVIENDAS WHERE IDVIVIENDA = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			vivienda = convertResultSetToVivienda(rs);
		}

		return vivienda;
	}
	
	/**
	 * Metodo que agregar la informacion de un nuevo vivienda en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param vivienda vivienda que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addVivienda(Vivienda vivienda) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.VIVIENDAS (IDVIVIENDA, HABITACIONES, PRECIOBASE, UBICACION,IDPROVEEDOR, ESTADO) VALUES (%2$s, '%3$s', '%4$s', '%5$s','%6$s','%7$s')", 
									USUARIO, 
									vivienda.getIdVivienda(), 
									vivienda.getHabitaciones(),
									vivienda.getPrecioBase(), 
									vivienda.getUbicacion(),
									vivienda.getIdProveedor(),
									vivienda.getEstado());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza la informacion de la vivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param vivienda vivienda que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateVivienda(Vivienda vivienda) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.VIVIENDAS ", USUARIO));
		sql.append (String.format (
				"SET UBICACION = '%1$s', PRECIOBASE = '%2$s', HABITACIONES = '%3$s' ,IDPROVEEDOR = '%4$s'",
				vivienda.getUbicacion (), vivienda.getPrecioBase (),
				vivienda.getHabitaciones (),vivienda.getIdProveedor()));
		sql.append ("WHERE IDVIVIENDA = " + vivienda.getIdVivienda ());
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion de la vivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param vivienda Vivienda que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteVivienda(Vivienda vivienda) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.VIVIENDAS WHERE IDVIVIENDA = %2$d", USUARIO, vivienda.getIdVivienda());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla VIVIENDAS) en una instancia de la clase Vivienda.
	 * @param resultSet ResultSet con la informacion de un vivienda que se obtuvo de la base de datos.
	 * @return vivienda cuyos atributos corresponden a los valores asociados a un registro particular de la tabla VIVIENDAS.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Vivienda convertResultSetToVivienda(ResultSet resultSet) throws SQLException {
	
		int id = Integer.parseInt(resultSet.getString("IDVIVIENDA"));
		int precio = Integer.parseInt(resultSet.getString("PRECIOBASE"));
		int habitaciones = Integer.parseInt(resultSet.getString("HABITACIONES"));
		int idProveedor = Integer.parseInt(resultSet.getString("IDPROVEEDOR"));

		Vivienda viv = new Vivienda(id, precio, habitaciones,idProveedor);

		return viv;
	}
	
    /**
     * Metodo que obtiene el funcionamiento de todos las viviendas en la Base de Datos <br/>
     * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
     *
     * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
     * @throws Exception    Si se genera un error dentro del metodo.
     * @return lista con la informacion de todos los apartamentos que se encuentran en la Base de Datos
     */
    public ArrayList getFuncionamientoVivienda() throws SQLException, Exception {
        ArrayList viviendas = new ArrayList<Apartamento>();

        String sql = String.format("\r\n" + 
        		"SELECT  CONTRATOSVIVIENDAS.IDVIVIENDA,CONTRATOS.IDPROVEEDOR, to_char(FECHAINICIO,'ww')AS NUMSEMANA, COUNT(IDPROVEEDOR)AS CANTIDADPROVEEDOR\r\n" + 
        		"FROM CONTRATOS, CONTRATOSVIVIENDAS\r\n" + 
        		"WHERE CONTRATOS.IDCONTRATO = CONTRATOSVIVIENDAS.IDCONTRATO\r\n" + 
        		"GROUP BY CONTRATOSVIVIENDAS.IDVIVIENDA,CONTRATOS.IDPROVEEDOR, to_char(FECHAINICIO,'ww')\r\n" + 
        		"ORDER BY COUNT(IDPROVEEDOR);\r\n" + 
        		");\r\n" + 
        		"", USUARIO);

        PreparedStatement prepStmt = conn.prepareStatement(sql);
        recursos.add(prepStmt);
        ResultSet rs = prepStmt.executeQuery();

        while (rs.next()) {
        	viviendas.add(rs.getObject(sql));
        }
        return viviendas;
    }
}
