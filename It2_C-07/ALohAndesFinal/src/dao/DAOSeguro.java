package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

public class DAOSeguro 
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
	 * Arraylists de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;


	private int numSeguros;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase DAOSeguro <br/>
	 */
	public DAOSeguro() {
		recursos = new ArrayList<Object>();
		numSeguros=0;
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
	public void addSeguro(Seguro seguro) throws SQLException, Exception {


		numSeguros++;
		char incendio='T';
		if(seguro.isIncendio()==false) {
			incendio='F';
		}
		
		char inundacion='T';
		if(seguro.isInundaciones()==false) {
			inundacion='F';
		}
		char robo='T';
		if(seguro.isRobo()) {
			robo='F';
		}
		char fenomeno = 'T';
		if(seguro.isFenomeno()==false) 	{
			robo ='F';
		}


		String sql = String.format("INSERT INTO %1$s.SEGURO ( IDSEGURO, COSTO, INCENDIO, ROBO, INUNDACION, FENOMENO, IDVIVIENDA) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s')", 
				USUARIO,  
				seguro.getCosto(), 
				incendio,
				robo, 
				inundacion,
				numSeguros);

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que obtiene la informacion del seguro en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del seguro
	 * @return la informacion del seguro que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el seguro conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Seguro findSeguroById(int id) throws SQLException, Exception 
	{
		Seguro seguro = null;

		String sql = String.format("SELECT * FROM %1$s.SEGURO WHERE ID = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			seguro = convertResultSetToSeguro(rs);

		}

		return seguro;
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
	public Seguro convertResultSetToSeguro(ResultSet resultSet) throws SQLException {
		//Requerimiento 1G: Complete el metodo con los atributos agregados previamente en la clase Bebedor. 
		//						 Tenga en cuenta los nombres de las columnas de la Tabla en la Base de Datos (ID, NOMBRE, PRESUPUESTO, CIUDAD)
		boolean incendioS=false;
		boolean roboS=false;
		boolean inundacionesS=false;
		boolean fenomenosS=false;
		

		String idS = resultSet.getString("IDSEGURO");
		int id = Integer.parseInt(idS);
		
		String costoS = resultSet.getString("COSTO");
		double costo = Double.parseDouble(costoS);
		
		String incendio =resultSet.getString("INCENDIO");
		String robo=resultSet.getString("ROBO");
		String inundacion=resultSet.getString("INUNDACION");
		String fenomeno = resultSet.getString("FENOMENO");
		
		String idViviendaS = resultSet.getString("IDVIVIENDA");
		int idVivienda = Integer.parseInt(idViviendaS);
		
		if(incendio.charAt(0)=='T')
		{
			incendioS=true;
		}

		if(robo.charAt(0)=='T')
		{
			roboS=true;
		}

		if(inundacion.charAt(0)=='T')
		{
			inundacionesS=true;
		}
		
		if(fenomeno.charAt(0)=='T')
		{
			fenomenosS=true;
		}
		

		Seguro seguro = new Seguro(id, costo, incendioS, roboS, inundacionesS, fenomenosS, idVivienda);

		return seguro;
	}
	
	public void deleteSeguro (Seguro seguro) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.MENAJE WHERE ID = %2$d", USUARIO, seguro.getId());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();	
	}

	public ArrayList<Seguro> getSeguros() throws SQLException
	{
		ArrayList<Seguro> seguro = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.CONTRATOS ) ", USUARIO ); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			seguro.add(convertResultSetToSeguro(rs));
		}

		return seguro;
	}


}
