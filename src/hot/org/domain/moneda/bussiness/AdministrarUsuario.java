package org.domain.moneda.bussiness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Activacion;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Rol;
import org.domain.moneda.entity.Usuario;
import org.domain.moneda.entity.Usuariorol;
import org.domain.moneda.entity.UsuariorolId;
import org.domain.moneda.session.UsuarioHome;
import org.domain.moneda.util.EnviarMailAlertas;
import org.domain.moneda.util.ExpresionesRegulares;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("AdministrarUsuario")
@Scope(ScopeType.CONVERSATION)
public class AdministrarUsuario
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In Identity identity;
    
  
    

    public void administrarUsuario()
    {
        // implement your business logic here
        log.info("AdministrarUsuario.administrarUsuario() action called");
        statusMessages.add("administrarUsuario");
    }

    // add additional action methods
    
    @In private EntityManager entityManager;
	@In private FacesMessages facesMessages;
	@In (required=false) private UsuarioHome usuarioHome;
	
	
	
	private List<Rol> rolesSeleccionados=null;		//ROLES O PRIVILEGIOS ASOCIADOS
	private List<Rol> rolesASeleccionar=null;		//CONCEPTOS A ASOCIAR A UN MODELO SELECCIONADO
	

	EnviarMailAlertas enviarMail = new EnviarMailAlertas();

	public List<Rol> getRolesSeleccionados() {
		return rolesSeleccionados;
	}


	public void setRolesSeleccionados(List<Rol> rolesSeleccionados) {
		this.rolesSeleccionados = rolesSeleccionados;
	}


	public List<Rol> getRolesASeleccionar() {
		return rolesASeleccionar;
	}


	public void setRolesASeleccionar(List<Rol> rolesASeleccionar) {
		this.rolesASeleccionar = rolesASeleccionar;
	}
	
	
	//CARGA LOS MODELOS ASOCIADOS Y LOS MODELOS A ASOCIAR DE UN CONCEPTO SELECCIONADO
	public void cargar(){		
		this.rolesASeleccionar = 
			entityManager.createQuery("SELECT r FROM Rol r WHERE r.idrol NOT IN (SELECT ur.id.rol FROM Usuariorol ur where ur.id.usuario =:cod)")
		.setParameter("cod", this.usuarioHome.getInstance().getUsuario()).getResultList();
		this.rolesSeleccionados=
			entityManager.createQuery("SELECT r FROM Rol r, Usuariorol ur where r.idrol = ur.id.rol and ur.id.usuario =:cod")
		.setParameter("cod", this.usuarioHome.getInstance().getUsuario()).getResultList();
	}
	
	
	public String asociarRolUsuario(String username)
	{
		Usuario u=entityManager.find(Usuario.class, username);
		
		for(int i=0;i<this.rolesSeleccionados.size();i++)
		{
			UsuariorolId urId=new UsuariorolId(u.getUsuario(), this.rolesSeleccionados.get(i).getIdrol());			
			Usuariorol ur=new Usuariorol(urId),aux;
			aux=entityManager.find(Usuariorol.class, urId);
			if(aux==null)
			{	
				entityManager.persist(ur);
				//this.facesMessages.add("Se han asignado los privilegios al usuario "+modelosSeleccionados.get(i).getDescripcion()+" al concepto "+c.getDescripcion());
			}
		}
		
		for(int i=0;i<this.rolesASeleccionar.size();i++)
		{
			UsuariorolId urId=new UsuariorolId(u.getUsuario(), this.rolesASeleccionar.get(i).getIdrol());;			
			Usuariorol aux;
			aux=entityManager.find(Usuariorol.class, urId);
			if(aux!=null)
			{	
				entityManager.remove(aux);
				//this.facesMessages.add("El modelo "+aux.getModelo().getDescripcion()+" se ha desvinculado satisfactoriamente del concepto "+c.getDescripcion());
			}
		}		
		return "/UsuarioList.xhtml";
	}
	
	
	/**
	 * Este metodo consulta las activaciones que estan proximas a comenzar dentro 
	 * de los siguientes 10 dias a la fecha actual, y cuales activaciones comenzaron
	 * viaje y no han facturado. Estas dos colecciones son pasadas como argumentos
	 * al metedo enviarReporteAsesoras()
	 */
	public void procesarEmailActivaciones(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
        try {
			Date fin;
			
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(new Date()); 
	        calendar.add(Calendar.DAY_OF_YEAR, 7);//añadimos 7 dias a la fecha cactual
	        fin = sdf.parse(sdf.format( calendar.getTime()));
	        
	        //coleccion con las actvaciones que esta por comenzar
			List< Activacion > actProx = entityManager.createQuery(" select " +
					"a from Activacion a where a.fechaInicioViaje between '" +  
					sdf.format(new Date()) + "' AND '" +sdf.format(fin)+ "' AND "  +
					" a.promotor.asesor.documento = '" + identity.getUsername() +"' order by a.fechaInicioViaje").getResultList();
			
			//coleccion con las activaciones pasadas sin viaje	
			String sql="SELECT "+
					   "public.activacion.cedula, "+
					   "public.activacion.nombre, "+
					   "banco.nombrebanco, "+
					   "activacion.fechainicioviaje, "+
					   "public.personal.nombre ||' '||  public.personal.apellido "+
					   "FROM "+
					   "public.activacion "+
					   "INNER JOIN public.promotor ON ( public.activacion.promotor = public.promotor.documento ) "+
					   "INNER JOIN public.personal ON ( public.promotor.documento = public.personal.documento ) "+
					   "INNER JOIN public.banco ON ( public.activacion.codbanco = public.banco.codbanco ) "+
					   "LEFT JOIN  ( "+
						  "	SELECT "+
						  " public.viaje.* "+
						  " FROM "+
						  " public.viaje "+
						  " WHERE "+
						  " (public.viaje.cedulatarjetahabiente, public.viaje.fechainicio  ) IN ( "+
						  " select "+
						  " viaje.cedulatarjetahabiente, "+
						  " max(viaje.fechainicio) "+
						  " from "+
						  " viaje "+
						  " group by "+
						  " viaje.cedulatarjetahabiente) "+
						  ") AS maxviaje ON ( public.activacion.cedula = maxviaje.cedulatarjetahabiente ) "+
					   "LEFT JOIN tarjetaviaje ON (maxviaje.consecutivo = tarjetaviaje.consecutivoviaje) "+
					   "LEFT JOIN tarjeta ON ( tarjetaviaje.numerotarjeta = tarjeta.numerotarjeta ) "+
					   "LEFT JOIN (SELECT "+
									"transaccion.numerotarjeta, "+
						            "max(transaccion.fechatx)as fechatx "+
						            "from "+
						            "transaccion "+
						            "WHERE EXTRACT(YEAR FROM transaccion.fechatx) = EXTRACT ( YEAR FROM CURRENT_DATE) " +
						            "group by "+
						            "transaccion.numerotarjeta) mxtx ON ( mxtx.numerotarjeta = tarjeta.numerotarjeta ) "+
						"WHERE "+
						"public.activacion.estado = 'AC' AND "+
						"public.activacion.fechainicioviaje < '" + sdf.format(new Date()) + "' AND "+
						     /*Asesor*/
						"public.promotor.asesor= '" + identity.getUsername() +"' AND "+
						/*Determina que tenga un viaje creado y valido*/
						 "("+
						 "(( maxviaje.fechainicio IS NULL OR "+
						   "EXTRACT(YEAR FROM maxviaje.fechainicio) != EXTRACT( YEAR FROM  date ('"+sdf.format(new Date())+"')) OR"+
						   "(EXTRACT(YEAR FROM maxviaje.fechainicio) = EXTRACT( YEAR  FROM  date ('"+sdf.format(new Date())+"'))"+
						   " AND (maxviaje.cupoinicialviajero IS NULL  OR maxviaje.cupoinicialviajero = 0 )) )  AND"+
						 "( EXTRACT(YEAR FROM date('"+sdf.format(new Date())+"')) - 1900 =  public.activacion.ano ) )"+
						/* Determina que si tiene un viaje pero no ha facturado  */
						" OR "+
						" (( EXTRACT(YEAR FROM maxviaje.fechainicio) = EXTRACT(YEAR FROM date ('"+sdf.format(new Date())+"')) and (EXTRACT(YEAR FROM  date('"+sdf.format(new Date())+"')) - 1900 =  public.activacion.ano) )"+
						"   AND (mxtx.fechatx IS NULL OR mxtx.fechatx NOT BETWEEN maxviaje.fechainicio AND maxviaje.fechafin))) " +
						"GROUP BY "+
						"public.activacion.cedula, "+
						"public.activacion.nombre, "+
						"banco.nombrebanco, "+
						"activacion.fechainicioviaje, "+
						"public.personal.nombre ||' '||  public.personal.apellido "+
						"order by "+
						"4";
			
			System.out.println(sql);
			List<Object[]> actSinViaje = entityManager.createNativeQuery(sql).getResultList();
			
			
			System.out.println("Sin Viaje: " +  actSinViaje.isEmpty() );
			System.out.println("Proximas: " +  actProx.isEmpty()  );
			
			if( !actProx.isEmpty() || !actSinViaje.isEmpty() ){
				Asesor asesora =  (Asesor) entityManager.createQuery(
						"select a from Asesor a " + "where a.documento = '"
								+ identity.getUsername() + "'").getSingleResult();
				enviarMail.enviarReporteAsesoras( actProx, actSinViaje, asesora);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Envia un correo a todos los clientes dando informacion de contacto
	 * en el loggin de las asesoras para un determinado dia de la semana.
	 */
	public void emailClientesInfo(){
		//1. Determino si es el dia de enviar
		Calendar cal = Calendar.getInstance();
        //int year = cal.get(Calendar.YEAR);
		//int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        
       // coleccion con los clientes de la asesora actual  
        if(day == 1){//se envia el correo si es dia martes <-- desactivado se tiene el domingo
        	List< Promotor >promotores = entityManager.createQuery("select pr from Promotor pr where " +
        			"pr.asesor = '" + identity.getUsername() +"'").getResultList();
        	
        	for(Promotor pr: promotores){
        		if(!pr.getPersonal().getCorreo().equals("")){
        			enviarMail.enviarEmailContactoGerencia(pr);
        		}
        	}
        }
	}
	
	public void nombreAMayuscula(String Nombre){
		this.usuarioHome.getInstance().setNombre(
				ExpresionesRegulares.eliminarEspacios(Nombre, true));
	}
	
	
	public void auditarUsuario(Integer operacion, String observacion){	
		String sql = "insert into audit_usuario " +
		"values ('"+identity.getUsername()+"',now(), " +
		""+operacion+",'"+observacion+"', null)";
		entityManager.createNativeQuery(sql).executeUpdate();
		entityManager.flush();
	}
	
	public void auditarUsuarioLogin(Integer operacion, String observacion, String ip){	
		String queryString = "insert into audit_usuario " +
		"values ('"+identity.getUsername()+"',now(), " +operacion+ ",'"+observacion+"', '"+ip+"')";
		entityManager.createNativeQuery(queryString).executeUpdate();
		entityManager.flush();
	}
	
	
	
	public void activarUsuario(){
		System.out.println("VALOR CASILLA: " + usuarioHome.getInstance().getActivo());
		if (usuarioHome.getInstance().getActivo()) {
			auditarUsuario(30,
					"Se desbloqueo el usuario: " + usuarioHome.getInstance().getUsuario() );
			
			String sql = "insert into audit_usuario " +
			"values ('"+usuarioHome.getInstance().getUsuario()+"',now(), 30, 'Usuario desbloqueado', null)";
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();

		}
		
	}
	
	public void activarUsuarioMultiSesion(){		
		if (usuarioHome.getInstance().getAdministrador()) {
			auditarUsuario(30,
					"Se desbloqueo el usuario: " + usuarioHome.getInstance().getUsuario() );
			
			String sql = "insert into audit_usuario " +
			"values ('"+usuarioHome.getInstance().getUsuario()+"',now(), 30, 'Usuario desbloqueado', null)";
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();
		}		
	}
	
	
	public String ocultarClave(String clave) {
		StringBuffer hide = new StringBuffer();
		char c;
		for (int i = 0; i < clave.length(); i++) {
			c = clave.charAt(i);
			c = '*';
			hide.append(c); 
		}
		return hide.toString(); 
	}


}
