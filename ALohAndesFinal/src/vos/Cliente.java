package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Cliente {

		/**
		 * Id del cliente
		 */
		@JsonProperty(value="idCliente")
		private int idCliente;
		
		/**
		 * Nombre del cliente
		 */
		@JsonProperty(value="nombre")
		private String nombre;

		/**
		 * Apellido del cliente.
		 */
		
		@JsonProperty(value="apellido")
		private String apellido;
		
		/**
		 * login del cliente
		 */
		
		@JsonProperty (value = "login")
		private String login;
		/**
		 * password del cliente
		 */
		@JsonProperty (value = "password")
		private String password;


	/**
	 * Metodo constructor de Cliente
	 * <b>post: </b> Crea el cliente con los valores que entran por parametro
	 * @param pIdCliente - IdCliente del cliente.
	 * @param pNombre - Nombre del cliente.
	 * @param pApellido - Apellido del cliente.
	 */
	public Cliente(@JsonProperty(value="idCliente")int pIdCliente,@JsonProperty(value="nombre")String pNombre, @JsonProperty(value="apellido") String pApellido)
	{
		this.idCliente = pIdCliente;
		this.nombre= pNombre;
		this.apellido = pApellido;

	}


	/**
		 * Metodo constructor de Cliente
		 * <b>post: </b> Crea el cliente con los valores que entran por parametro
		 * @param pIdCliente - IdCliente del cliente.
		 * @param pNombre - Nombre del cliente.
		 * @param pApellido - Apellido del cliente.
		 * @param pLogin - Login del cliente.
		 * @param pPassword - Password del cliente.
		 */
		public Cliente(@JsonProperty(value="idCliente")int pIdCliente,@JsonProperty(value="nombre")String pNombre, @JsonProperty(value="apellido") String pApellido, @JsonProperty(value="login")String pLogin,@JsonProperty(value="password")String pPassword) 
		{
			this.idCliente = pIdCliente;
			this.nombre= pNombre;
			this.apellido = pApellido;
			this.login = pLogin;
			this.password = pPassword;
		}
	
		public int getIdCliente()
		{
			return idCliente;
		}
		
		public void setIdCliente(int pId)
		{
			idCliente= pId;
		}
		public String getNombre()
		{
			return nombre;
		}
		public void setNombre(String pNombre)
		{
			nombre = pNombre;
		}
		public String getApellido()
		{
			return apellido;
		}
		
		public void setApellido(String pApellido)
		{
			apellido = pApellido;
		}
		public String getLogin ()
		{
			return login;
		}
		public void setLogin(String pLogin)
		{
			login = pLogin;
		}
		
		public String getPassword ()
		{
			return password;
		}
		public void setPassword(String pPassword)
		{
			password = pPassword;
		}
}

