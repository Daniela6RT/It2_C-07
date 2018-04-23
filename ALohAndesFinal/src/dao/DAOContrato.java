package dao;

import java.sql.*;
import java.util.*;
import java.util.Date;
import vos.*;

public class DAOContrato 
{


	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	//Requerimiento 1H: Modifique la constante, reemplazando al ususario PARRANDEROS por su ususario de Oracle
	public final static String USUARIO = "ISIS2304A961810";

	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	private int numContratos;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase DAOContrato <br/>
	 */
	public DAOContrato() {
		recursos = new ArrayList<Object>();
		numContratos=0;
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	
		
	/**
	 * Metodo que agregar la informacion de un nuevo operador en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addContratoApartamentoContratos(ContratoApartamento contrato) throws SQLException, Exception {

		Date actual=new Date();
		String fecha= (actual.getYear()+"")+"-"+(actual.getMonth()+"")+"-"+(actual.getDate()+"");
		numContratos++;
		String sql = String.format("INSERT INTO %1$s.CONTRATOS (IDCONTRATO, FECHAINICIO, FECHAFIN, IDCLIENTE, IDPROVEEDOR, ESTADO) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", 
				USUARIO,  

				contrato.getId(),
				contrato.getFechaInicio(),
				contrato.getFechaFinal(), 
				contrato.getIdCliente(),
				contrato.getIdProveedor(),
				contrato.getEstado()
				);

		System.out.println(sql);

		PreparedStatement prepStmt6 = conn.prepareStatement(sql);
		recursos.add(prepStmt6);
		prepStmt6.executeQuery();
		
		
	}


	/**
	 * Metodo que agregar la informacion de un nuevo operador en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addContratoViviendaContratos(ContratoVivienda contrato) throws SQLException, Exception {

		Date actual=new Date();
		String fecha= (actual.getYear()+"")+"-"+(actual.getMonth()+"")+"-"+(actual.getDate()+"");
		numContratos++;
		String sql = String.format("INSERT INTO %1$s.CONTRATOS (IDCONTRATO, FECHAINICIO, FECHAFINAL, IDCLIENTE, IDPROVEEDOR, FECHACREACION, COSTO, TIPO, IDVIVIENDA) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s', '%9$s', '%10$s')", 
				USUARIO,  

				contrato.getId(),
				contrato.getFechaInicio(),
				contrato.getFechaFinal(), 
				contrato.getIdCliente(),
				contrato.getIdProveedor(),
				contrato.getEstado()
				);

		System.out.println(sql);

		PreparedStatement prepStmt6 = conn.prepareStatement(sql);
		recursos.add(prepStmt6);
		prepStmt6.executeQuery();
	}

	/**
	 * Metodo que agregar la informacion de un nuevo operador en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addContratoHabitacion(ContratoHabitacion contrato) throws SQLException, Exception {

		Date actual=new Date();
		String fecha= (actual.getYear()+"")+"-"+(actual.getMonth()+"")+"-"+(actual.getDate()+"");
		numContratos++;
		String sql = String.format("INSERT INTO %1$s.CONTRATOS (IDCONTRATO, FECHAINICIO, FECHAFINAL, IDCLIENTE, IDPROVEEDOR, FECHACREACION, COSTO, TIPO, IDHABITACION) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s', '%9$s', '%10$s')", 
				USUARIO,  

				contrato.getId(),
				contrato.getFechaInicio(),
				contrato.getFechaFinal(), 
				contrato.getIdCliente(),
				contrato.getIdProveedor(),
				contrato.getEstado()
				);

		System.out.println(sql);

		PreparedStatement prepStmt6 = conn.prepareStatement(sql);
		recursos.add(prepStmt6);
		prepStmt6.executeQuery();
	}

	public int darUsoEnEsteAno(Contrato contrato) throws SQLException
	{
		Date hoy= new Date();
		String fechaAcutual=hoy.getYear()+"";
		String capacidad= String.format("SELECT sum(DIFF(DAY,FECHA_INICIO, FECHA_FIN)) as DIAS FROM %1$s.c WHERE id_vivienda= %2$s AND (SELECT YEAR(FECHA_INICIO))= %3$s;",USUARIO, contrato.getIdVivienda(), fechaAcutual);

		PreparedStatement prepStmt3 = conn.prepareStatement(capacidad);
		ResultSet resultado3=prepStmt3.executeQuery();
		return Integer.parseInt(resultado3.getString("DIAS"));
	}

	public ArrayList<Contrato> getContratos() throws SQLException
	{
		ArrayList<Contrato> contrato = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.CONTRATOS ) ", USUARIO ); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			contrato.add(convertResultSetToContrato(rs));
		}

		return contrato;
	}
	
	
	public ArrayList<Contrato> getContratoByidApartamentoEnFechas(String id, Date fechaInicio, Date fechaFin) throws SQLException
	{
		ArrayList<Contrato> contrato = new ArrayList<>();

		String sql = String.format("SELECT c.IDCONTRATO, FECHAINICIO, FECHAFIN, IDAPARTAMENTO FROM %1$s.CONTRATOS c FULL OUTER "
				+ "JOIN CONTRATOSAPARTAMENTOS ON c.IDCONTRATO = CONTRATOSAPARTAMENTOS.IDCONTRATO WHERE IDAPARTAMENTO = %2$d AND (FECHAINICIO BETWEEN %3$d AND %4$d "
				+ "OR FECHAFIN BETWEEN %5$d AND %6$d",
				USUARIO, id,fechaInicio, fechaFin,fechaInicio, fechaFin ); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			contrato.add(convertResultSetToContrato(rs));
		}


		return contrato;
	}
	
	
	public ArrayList<Contrato> getContratoByidHabitacionEnFechas(String id, Date fechaInicio, Date fechaFin) throws SQLException
	{
		ArrayList<Contrato> contrato = new ArrayList<>();

		String sql = String.format("SELECT c.IDCONTRATO, FECHAINICIO, FECHAFIN, IDHABITACION FROM %1$s.CONTRATOS c FULL OUTER "
				+ "JOIN CONTRATOSHABITACIONES ON c.IDCONTRATO = CONTRATOSHABITACIONES.IDCONTRATO WHERE IDHABITACION = %2$d AND (FECHAINICIO BETWEEN %3$d AND %4$d "
				+ "OR FECHAFIN BETWEEN %5$d AND %6$d",
				USUARIO, id,fechaInicio, fechaFin,fechaInicio, fechaFin ); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			contrato.add(convertResultSetToContrato(rs));
		}


		return contrato;
	}
	
	public ArrayList<Contrato> getContratoByidViviendaEnFechas(String id, Date fechaInicio, Date fechaFin) throws SQLException
	{
		ArrayList<Contrato> contrato = new ArrayList<>();

		String sql = String.format("SELECT c.IDCONTRATO, FECHAINICIO, FECHAFIN, IDVIVIENDA FROM %1$s.CONTRATOS c FULL OUTER "
				+ "JOIN CONTRATOSVIVIENDAS ON c.IDCONTRATO = CONTRATOSVIVIENDAS.IDCONTRATO WHERE IDVIVIENDA = %2$d AND (FECHAINICIO BETWEEN %3$d AND %4$d "
				+ "OR FECHAFIN BETWEEN %5$d AND %6$d",
				USUARIO, id,fechaInicio, fechaFin,fechaInicio, fechaFin ); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			contrato.add(convertResultSetToContrato(rs));
		}


		return contrato;
	}
	
	public ArrayList<Contrato> getContratoByidCliente(String id) throws SQLException
	{
		ArrayList<Contrato> contrato = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.CONTRATOS WHERE IDCLIENTE = %2$d ; ", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			contrato.add(convertResultSetToContrato(rs));
		}


		return contrato;
	}
	
	public ArrayList<String> get20OfertasPopulares() throws SQLException, Exception
	{
		ArrayList<String> ofertas = new ArrayList<>();
		
		String sql = String.format("(select count(idcontrato) as cantidadpedidas, idapartamento as idalojamiento,tipoalojamiento from CONTRATOSAPARTAMENTOS\n" + 
				"group by idapartamento,tipoalojamiento) union all ( (select count(idcontrato) as cantidadpedidas, idhabitacion as idalojamiento,tipoalojamiento\n" + 
				"from %1$s.CONTRATOSHABITACIONES group by idhabitacion,tipoalojamiento) union all (select count(idcontrato) as cantidadpedidas, idvivienda as idalojamiento, tipoalojamiento\n" + 
				"from CONTRATOSviviendas group by idvivienda, tipoalojamiento) ) order by cantidadpedidas DESC;", USUARIO);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			String añadir ="";
			
			ofertas.add(añadir);
		}

		return ofertas;
	}
	
	/**
	 * Metodo que actualiza la informacion del contrato en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param contrato contrato que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteContrato(Contrato contrato) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CONTRATOS WHERE IDCONTRATO = %2$d", USUARIO, contrato.getId());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void cancelarContrato(Contrato contrato) throws SQLException, Exception {
		String sql = String.format("UPDATE %1$s.CONTRATOS SET Estado= 'Cancelado' WHERE IDCONTRATO=%2$d", USUARIO, contrato.getId());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del contrato en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateContrato(Contrato contrato) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.CONTRATOS SET ", USUARIO));
		sql.append(String.format("COSTO = '%1$s' AND FECHAFIN = '%2$s' AND FECHAINICIO = '%3$s' ", contrato.getCosto(), contrato.getFechaFinal(), contrato.getFechaInicio()));

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Metodo que obtiene la informacion del contrato en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del contrato
	 * @return la informacion del contrato que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el contrato conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Contrato findContratoById(int id) throws SQLException, Exception 
	{
		Contrato contrato = null;

		String sql = String.format("SELECT * FROM %1$s.CONTRATOS WHERE IDCONTRATO = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			contrato = convertResultSetToContrato(rs);
		}

		return contrato;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS AUXILIARES
	//----------------------------------------------------------------------------------------------------------------------------------

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Bebedor.
	 * @param resultSet ResultSet con la informacion de un bebedor que se obtuvo de la base de datos.
	 * @return Operador cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Contrato convertResultSetToContrato(ResultSet resultSet) throws SQLException {
		//Requerimiento 1G: Complete el metodo con los atributos agregados previamente en la clase Bebedor. 
		//						 Tenga en cuenta los nombres de las columnas de la Tabla en la Base de Datos (ID, NOMBRE, PRESUPUESTO, CIUDAD)

		
		String idContratoS = resultSet.getString("IDCONTRATO");
		int idContrato = Integer.parseInt(idContratoS);

		String arrFechaInicio = resultSet.getString("FECHAINICIO");
		String[] diasInicio=arrFechaInicio.split("-");
		Date fechaInicio= new Date();
		fechaInicio.setYear(Integer.parseInt(diasInicio[0]));
		fechaInicio.setMonth(Integer.parseInt(diasInicio[1]));
		fechaInicio.setDate(Integer.parseInt(diasInicio[2]));

		String arrFechaFin = resultSet.getString("FECHAFINAL");
		String[] diasFin=arrFechaFin.split("-");
		Date fechaFinal= new Date();
		fechaFinal.setYear(Integer.parseInt(diasFin[0]));
		fechaFinal.setMonth(Integer.parseInt(diasFin[1]));
		fechaFinal.setDate(Integer.parseInt(diasFin[2]));

		Integer idCliente = resultSet.getInt("IDCLIENTE");

		Integer idProveedor = resultSet.getInt("IDPROVEEDOR");

		
		String arrFechaCreacion = resultSet.getString("FECHACREACION");
		String[] diasCreacion=arrFechaCreacion.split("-");
		Date fechaCreacion= new Date();
		fechaCreacion.setYear(Integer.parseInt(diasCreacion[0]));
		fechaCreacion.setMonth(Integer.parseInt(diasCreacion[1]));
		fechaCreacion.setDate(Integer.parseInt(diasCreacion[2]));

		
		String costoS = resultSet.getString("COSTO");
		int costo = Integer.parseInt(costoS);

		
		String tipo = resultSet.getString("TIPO");
		
		String estado = resultSet.getString("ESTADO");
		Contrato contrato = null;
		
		if (tipo.equals(Contrato.APARTAMENTO)) {
			String idApartamentoS = resultSet.getString("IDAPARTAMENTO");
			Integer idApartamento = Integer.parseInt(idApartamentoS);
			contrato= new ContratoApartamento(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		}
		
		else if (tipo.equals(Contrato.HABITACION)) {
			String idHabitacionS = resultSet.getString("IDHABITACION");
			Integer idHabitacion = Integer.parseInt(idHabitacionS);
			contrato= new ContratoHabitacion(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		}
		
		else if (tipo.equals(Contrato.VIVIENDA)) {
			String idViviendaS = resultSet.getString("IDVIVIENDA");
			Integer idVivienda = Integer.parseInt(idViviendaS);
			contrato= new ContratoVivienda(idContrato, fechaInicio, fechaFinal, idCliente, idProveedor, estado);
		}
		
		return contrato;
	}
}
