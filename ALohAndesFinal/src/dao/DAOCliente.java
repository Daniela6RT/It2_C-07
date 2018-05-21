package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import vos.*;

public class DAOCliente
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
	 * Metodo constructor de la clase DAOCliente <br/>
	 */
	public DAOCliente() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que obtiene la informacion de todos los clientes en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los clientes que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Cliente> getClientes() throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT * FROM %1$s.clientes WHERE ROWNUM <= 50", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
	}
	/**
	 * Metodo que obtiene la informacion del cliente en la Base de Datos que tiene el login dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param login el login del cliente
	 * @return la informacion del cliente que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el cliente conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Cliente findClienteByLogin(String login) throws SQLException, Exception
	{
		Cliente cliente = null;

		String sql = String.format("SELECT * FROM %1$s.clientes WHERE LOGIN = %5$d", USUARIO, login);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			cliente = convertResultSetToCliente(rs);
		}

		return cliente;
	}


	/**
	 * Metodo que obtiene la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param id el identificador del cliente
	 * @return la informacion del cliente que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el cliente conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Cliente findClienteById(int id) throws SQLException, Exception
	{
		Cliente cliente = null;

		String sql = String.format("SELECT * FROM %1$s.clientes WHERE IDCLIENTE = %2$d", USUARIO, id);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			cliente = convertResultSetToCliente(rs);
		}

		return cliente;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo cliente en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param cliente cliente que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addCliente(Cliente cliente) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.clientes (IDCLIENTE, NOMBRE, APELLIDO, LOGIN,PASSOWRD) VALUES (%2$s, '%3$s', '%4$s', '%5$s','%6$s')",
				USUARIO,
				cliente.getIdCliente(),
				cliente.getNombre(),
				cliente.getApellido(),
				cliente.getLogin(),
				cliente.getPassword());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param cliente cliente que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateCliente(Cliente cliente) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.CLIENTE ", USUARIO));
		sql.append (String.format (
				"SET NOMBRE = '%1$s', APELIIDO = '%2$s', LOGIN = '%3$s', PASSOWORD ='%4$s'  ",
				cliente.getNombre (), cliente.getApellido (),
				cliente.getLogin (),cliente.getPassword()));
		sql.append ("WHERE IDCLIENTE = " + cliente.getIdCliente ());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que borra la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param cliente Cliente que desea borrar de la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteCliente(Cliente cliente) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CLIENTE WHERE IDCLIENTE = %2$d", USUARIO, cliente.getIdCliente());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla CLIENTE) en una instancia de la clase Cliente.
	 * @param resultSet ResultSet con la informacion de un cliente que se obtuvo de la base de datos.
	 * @return Cliente cuyos atributos corresponden a los valores asociados a un registro particular de la tabla CLIENTE.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Cliente convertResultSetToCliente(ResultSet resultSet) throws SQLException {

		int id = Integer.parseInt(resultSet.getString("IDCLIENTE"));
		String nombre = resultSet.getString("NOMBRE");
		String apellido = resultSet.getString("APELLIDO");


		Cliente cli = new Cliente(id, nombre, apellido);

		return cli;
	}

    /**
     * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla CLIENTE) en una instancia de la clase Cliente.
     * @param resultSet ResultSet con la informacion de un cliente que se obtuvo de la base de datos.
     * @return Cliente cuyos atributos corresponden a los valores asociados a un registro particular de la tabla CLIENTE.
     * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
     */
    public Cliente convertResultSetToClienteConPW(ResultSet resultSet) throws SQLException {

        int id = Integer.parseInt(resultSet.getString("IDCLIENTE"));
        String nombre = resultSet.getString("NOMBRE");
        String apellido = resultSet.getString("APELLIDO");
        String login = resultSet.getString("APELLIDO");
        String password = resultSet.getString("APELLIDO");


        Cliente cli = new Cliente(id, nombre, apellido, login,password);

        return cli;
    }

	/**
	 *
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<Cliente> getClientesFrecuentes() throws SQLException, Exception
	{
		ArrayList<Cliente> clientes = new ArrayList<>();

		String sql = String.format("(\r\n" +
				"Select *\r\n" +
				"from (\r\n" +
				"select Cliente.idCliente, Cliente.Nombre,Cliente.apellido,clientes.idcliente, count(idcliente) as cantidadDeVeces\r\n" +
				"from CLIENTE,clientes\r\n" +
				"where CLIENTE.IDCLIENTE =clientes.IDCLIENTE \r\n" +
				"group by Cliente.idCliente, Cliente.Nombre,Cliente.apellido,clientes.idcliente\r\n" +
				")\r\n" +
				"where cantidadDeVeces > 2;\r\n" +
				"", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			Cliente aniadir =null;

			clientes.add(aniadir);
		}

		return clientes;
	}

	public ArrayList<Cliente> consultarConsumoAlohAndesRFC10(Alojamiento alojamiento, Date fecha1, Date fecha2 , String organizacion) throws Exception {
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
                        "      OR FECHAFIN BETWEEN %4$d AND %5$d" +
                        "     AND (IDCLIENTE=%6$d) ORDER BY %7$d",
                alojamiento.getIdAlojamiento(),
                fecha1,
                fecha2,
                fecha1,
                fecha2,
                this.getClientes().get(0).getIdCliente(),
                organizacion);

        PreparedStatement prepStmt = conn.prepareStatement(sql);
        recursos.add(prepStmt);
        ResultSet rs = prepStmt.executeQuery();

        while (rs.next()) {
            respuesta.add(convertResultSetToClienteConPW(rs));
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
        		"      OR FECHAFIN BETWEEN %5$d AND %6$d\r\n" + 
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
            respuesta.add(convertResultSetToClienteConPW(rs));
        }

        return respuesta;
    }
	
	/**
	 * Metodo que obtiene la informacion de todos los clientes en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los clientes que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Cliente> getClientesBuenos() throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT X.IDCLIENTE\r\n" + 
				"FROM(\r\n" + 
				"SELECT IDCLIENTE, EXTRACT(MONTH FROM FECHAINICIO)AS MES, COUNT(IDCLIENTE) AS CANTIDADDEVECES\r\n" + 
				"FROM CONTRATOS\r\n" + 
				"\r\n" + 
				"GROUP BY IDCLIENTE, EXTRACT(MONTH FROM FECHAINICIO)\r\n" + 
				"ORDER BY COUNT(IDCLIENTE)\r\n" + 
				")X,CONTRATOS, CONTRATOSHABITACIONES, HABITACIONES\r\n" + 
				"WHERE X.IDCLIENTE = CONTRATOS.IDCLIENTE AND (X.CANTIDADDEVECES >=1 OR CONTRATOS.COSTO >=150 OR (CONTRATOS.IDCONTRATO = CONTRATOSHABITACIONES.IDCONTRATO AND CONTRATOSHABITACIONES.IDHABITACION = HABITACIONES.IDHABITACION AND HABITACIONES.TIPO = 'Suite') )\r\n" + 
				"GROUP BY X.IDCLIENTE\r\n" + 
				"ORDER BY X.IDCLIENTE;", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		String a =rs.getArray(sql).getArray().toString();
		while (rs.next()) {
			
			clientes.add(findClienteById(Integer.parseInt(a)));
		}
		return clientes;
	}
}
