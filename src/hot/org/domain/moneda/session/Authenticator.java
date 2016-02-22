package org.domain.moneda.session;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domain.moneda.bussiness.AdministrarUsuario;
import org.domain.moneda.bussiness.AdministrarVariable;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.AuditUsuario;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Rol;
import org.domain.moneda.entity.Usuario;
import org.domain.moneda.util.EnviarMailAlertas;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.security.auth.spi.Users.User;

@Name("authenticator")
public class Authenticator {
	@Logger
	private Log log;

	private Set<Rol> roles;
	private Usuario user;

	@In
	Identity identity;
	@In
	Credentials credentials;

	@In
	private EntityManager entityManager;

	@In(create = true)
	@Out
	private AdministrarVariable AdministrarVariable;

	@In(create = true)
	@Out
	private AdministrarUsuario AdministrarUsuario;
	
	@In
    private FacesMessages facesMessages;
	
	
	private EnviarMailAlertas enviarMail = new EnviarMailAlertas();	
	
	//Variables de control de seguridad	
	private String lastIpSession;
	private String currentIpSession;
	private Date lastDateLogin;
	private Date currentIpLogin;
	
	private Boolean asesor = false;
	private Boolean autentica = false;
	private String sql = null;
	
	private long milisegundos_dia =  60 * 60 * 1000;
	
	

	public boolean authenticate() {
		
		// write your authentication logic here,
		// return true if the authentication was
		// successful, false otherwise
		
		log.info("authenticating {0}", credentials.getUsername());

		
		sql = "select u from Usuario u where u.usuario='"
				+ credentials.getUsername().trim() + "' and u.clave='"
				+ credentials.getPassword().trim() + "' ";
		List<Usuario> results = entityManager.createQuery(sql).getResultList();

		sql = "";
		
		if (results.size() > 0){//Es un usuario
			
			this.user = (Usuario) results.get(0);			
						
			if( user.getActivo() != null && user.getActivo() == true ){//valida que el usuario este activo
				
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext(); 
				HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();		
						
			  //String agent = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("user-agent");
				String agent = request.getHeader("user-agent"); 
				this.currentIpSession = request.getRemoteAddr();//IP del host donde se realiza el loggin
				boolean administrador = user.getAdministrador() == null ? 
													false : user.getAdministrador();
				
				if( administrador ){ //si es administrador no hace validacion de IP
					logginCorrecto();//acciones a realizar si es correcto el loggin
				}else{
					if( validaUsuarioLogin( this.currentIpSession ) ){ //Aca valida que no se inicie sesion en mas 
						   											   //de un host en menos del tiempo de una session
						logginCorrecto();// acciones a realizar si es correctoel loggin										
					} else {
						// si no es la misma ip aca compara que se halla
						// presentado un Desbloqueo para el mismo dia
						if ( desbloqueo() ) {							
							logginCorrecto();// acciones a realizar si es
											 //correcto el loggin							
						} else {// se bloquea el usuario							
							autentica = false;
							user.setActivo(false);
							entityManager.persist(user);
							entityManager.flush();
							// envia un correo alertando al administrador
							this.enviarMail.emailAlertLoginUsuario(user.getNombre(), this.lastDateLogin,
													this.lastIpSession, new Date(), this.currentIpSession);
							// registro en auditoria
							AdministrarUsuario.auditarUsuario(29, "Usuario Bloqueado por acceso " +
									"no autorizado desde el dispositivo con IP: >" + this.currentIpSession + "<");

							facesMessages.add("Se detecto que este usuario ya habia ingresado en el dispositivo con IP: " + this.lastIpSession + ". Por" +
									" lo tanto, esta sesion no esta autorizada. Se bloqueo el usuario." );
							// Cerramos la sesion del otro que este abierta *pendiente por implementar esta accion
						}
					}	
				}
			}else{
				facesMessages.add("Este usuario esta bloqueado contacte al Administrador.");
				//se registra en el auditor un ingreso incorrecto
				AdministrarUsuario.auditarUsuario(13, "Ingreso Incorrecto El Usuario esta Bloqueado");
			}
		}else{
			//se registra en el auditor un ingreso incorrecto
			AdministrarUsuario.auditarUsuario(13, "Ingreso Incorrecto");
		}	
		return autentica;
		
	}//fin del metodo authenticate


	public void logginCorrecto(){		
		cargaRoles();//en este metodo se establece autentica true
		procesosLoginAsesor();
		AdministrarUsuario.auditarUsuarioLogin(0, "Ingreso Correcto", this.currentIpSession );
	}

	public Boolean desbloqueo(){
		Boolean desbloqueado = false;		
		List< String > audi = entityManager.createNativeQuery(
				"select a.usuario from audit_usuario a where a.operacion = 30 and " +
				"date( a.fecha ) = CURRENT_DATE and a.usuario = '" + 
				credentials.getUsername() +"'").getResultList();
		if(!audi.isEmpty()){
			desbloqueado = true;
		}
		return desbloqueado;
	}
	
	
	
	public void cargaRoles(){		
		this.roles = user.getRols();
		AdministrarVariable.setNombreusuario(user.getNombre());
		Iterator it = roles.iterator();
		while (it.hasNext()) {
			String s = "";
			s = ((Rol) it.next()).getDescripcion();
			if (s.contentEquals("Asesor")) {
				asesor = true;
			}
			Identity.instance().addRole(s);
			this.autentica = true; 
		}		
		Personal p = entityManager.find(Personal.class, credentials
				.getUsername());
		if (p != null)
			AdministrarVariable.setNombreusuario(p.getNombre());

		if (asesor) {
			sql = "select a from Asesor a where a.documento='"
					+ credentials.getUsername() + "'";
			List<Asesor> resultsa = entityManager.createQuery(sql)
					.getResultList();
			System.out.println("Es Asesor");
			if (resultsa.size() > 0) {
				AdministrarVariable.setAsesor((Asesor) resultsa.get(0));
			}
		} else {
			System.out.println("No Es Asesor");
		}
		
	}
	
	
	
	private void procesosLoginAsesor(){
		if (this.autentica) {
			//Valida si es el primer login del dia, si es asesor, enviara un correo
			//con las activaciones proximas a comenzar, y las que comenzaron y no han
			//empezado a consumir el viaje.
			if(asesor) {
				List< String > audi = entityManager.createNativeQuery(
						"select a.usuario from audit_usuario a where a.operacion = 0 and " +
						"date( a.fecha ) = CURRENT_DATE and a.usuario = '" + 
						credentials.getUsername() +"'").getResultList();
				if( audi.isEmpty() )
				{
					AdministrarUsuario.procesarEmailActivaciones();					
					AdministrarUsuario.emailClientesInfo();
				} 
			} 
		}
		
	}
	
	
	/**
	 * Este metodo valida que no se intentan hacer mas de 1 loggin desde 2 clientes distintos
	 * en menos de 1 hora. 
	 * @param currentIp
	 * @return
	 */
	public boolean validaUsuarioLogin( String currentIp){
		boolean valido = true;
		Object[] tupla = null;
		String queryString = "select a.usuario, a.fecha, a.operacion, a.descripcion, " +
						"a.ipcliente from audit_usuario a where a.operacion = 0 and " +
						"date( a.fecha ) = CURRENT_DATE and a.usuario = '" + 
						credentials.getUsername() +"' order by 2";		
		List< String > audi = entityManager.createNativeQuery( queryString ).getResultList();
		if( !audi.isEmpty() ){
			tupla = audi.toArray();
			Object[] tuplaReg = (Object[]) tupla[ tupla.length - 1 ];			
			this.lastDateLogin = (Date) tuplaReg[1];
			this.lastIpSession =  (String) tuplaReg[4];
			
			if(currentIp != null && this.lastIpSession != null){
				if( currentIp.equals(this.lastIpSession) ){
					valido = true;
				}else{
					Calendar fechaIni = Calendar.getInstance();
					Calendar fechaFin = Calendar.getInstance();
					fechaIni.setTime(this.lastDateLogin);
					fechaFin.setTime(new Date());
					long difHoras = this.diferenciaHorasDias(fechaIni, fechaFin);	
					// Valido que en el host anterior no este abierta aun la sesion *pendiente por implementar esta accion
					
					if( difHoras < 1){
						valido = false;
					}
				}
			}
		}
		return valido;
	}
	
	
	
	/*Metodo que calcula la diferencia de las horas que han pasado entre dos fechas en java */ 
	public long diferenciaHorasDias(Calendar fechaInicial ,Calendar fechaFinal){ 
		//Milisegundos al día 
		long diferenciaHoras=0; 
		//Restamos a la fecha final la fecha inicial y lo dividimos entre el numero de milisegundos al dia 
		diferenciaHoras=(
				fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis()) / milisegundos_dia; 

		System.out.println("Diferencia Horas: " + diferenciaHoras );

		return diferenciaHoras; 
	}
	
	

	
	

	
}
