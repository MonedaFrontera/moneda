package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Autovoz;
import org.domain.moneda.entity.Establecimiento;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.Viaje;
import org.domain.moneda.session.AutovozHome;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TransaccionHome;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;

@Scope(CONVERSATION)
@Name("AdministrarAutovoz")
public class AdministrarAutovoz
{
    @Logger private Log log;

    @In StatusMessages statusMessages;

    @In
    private FacesMessages facesMessages;
    
    private int value;
    
    public String tarjetafin; // Almacena los ultimos 4 digitos de la tarjeta
    
    @In
	private EntityManager entityManager;
    
    List<String> lista = new ArrayList<String>();
    
    List<String> listaHabiente = new ArrayList<String>();
    
	private String sugestion = "";
	
	private String tarjetahabiente = "";
	
	@In(create=true) @Out 
	private EstablecimientoHome	establecimientoHome;

	@In(create=true) @Out 
	private TarjetaHome	tarjetaHome;
	
	@In(create=true) @Out 
	private AutovozHome	autovozHome;
	
	@In(create=true) @Out 
	private TransaccionHome	transaccionHome;
	
	@In(create=true) @Out 
	private AdministrarTransaccion	AdministrarTransaccion;
	
	@In Identity identity;
	
	@In (create=true)
	@Out
	private AdministrarUsuario AdministrarUsuario;
    
    public void administrarAutovoz()
    {
        // implement your business logic here
        log.info("AdministrarAutovoz.administrarAutovoz() action called");
        statusMessages.add("administrarAutovoz");
    }
    
    public List<String> autocompletar(Object suggest) {
		llenar(); 							// Metodo que carga la informacion de los establecimientos
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = lista.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;

	}
	
	public List<String> autocompletarHabiente(Object suggest) {
		if (tarjetafin!=null){
		if (tarjetafin.length() == 4){ 
			llenarHabiente();
			if(listaHabiente!=null){
			String pref = (String) suggest;
			ArrayList<String> result = new ArrayList<String>();
			Iterator<String> iterator = listaHabiente.iterator();
			while (iterator.hasNext()) {
				String elem = ((String) iterator.next());
				if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
					result.add(elem);
				}
			}
			return result;
			}
		}
			
		}
		
		return null;
		

	}
	
	public void llenar(){
		entityManager.clear();
		List<String> resultList = entityManager.createQuery("select establecimiento.nombreestable from Establecimiento establecimiento").getResultList();
		lista = resultList;
	}
	
	public void llenarHabiente(){
		
		entityManager.clear();
		List<String> resultList = entityManager.createQuery("select tarjeta.tarjetahabiente from Tarjeta tarjeta where " +
				"tarjeta.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		if(resultList.size()>0){
			String sql = "select tarjeta.tarjetahabiente from Tarjeta tarjeta where " +
			"tarjeta.numerotarjeta like '%"+this.tarjetafin+"' ";
			
			if(identity.hasRole("Asesor")){
				sql = sql + "and tarjeta.promotor.asesor.documento='"+identity.getUsername()+"'";
			}
		    System.out.println("Sql"+sql);
			resultList = entityManager.createQuery(sql).getResultList();
			if(resultList.size()>0){
			     listaHabiente = resultList;
			}else{
				facesMessages.addToControl("numerotarjeta", "Hay tarjeta registradas con estos 4 numeros pero pertenecen a promotores que se encuentran asignados a otro Asesor ");	
				listaHabiente=null;
				return;
			}
		}else{
			facesMessages.addToControl("numerotarjeta", "No hay una tarjeta asociada a estos 4 numeros");
		    listaHabiente=null;
			return;	
		}
		
	}
	
	public void verificarTarjeta(){
		entityManager.clear();
		String sql = "select t from Tarjeta t where t.numerotarjeta like '%"+this.tarjetafin+"'";
		
		List<Tarjeta> t = (ArrayList)entityManager
		.createQuery(sql).getResultList();
		
		
		
		if(t.size()>0){
			if(identity.hasRole("Asesor")){
				sql = sql + " and t.promotor.asesor.documento = '"+identity.getUsername()+"'";
			}
			t = (ArrayList)entityManager
			.createQuery(sql).getResultList();
			if(t.size()>0){
				if (t.size() == 1) {
					this.tarjetahabiente = t.get(0).getTarjetahabiente();
					Tarjeta ta = t.get(0);		
					tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
					tarjetaHome.setInstance(ta);
				}
			}else{
				facesMessages.addToControl("numerotarjeta", "Hay tarjeta registradas con estos 4 numeros pero pertenecen a promotores que se encuentran asignados a otro Asesor ");	
				listaHabiente=null;
				return;
			}
		}else{
			facesMessages.addToControl("numerotarjeta", "No hay tarjetas registradas con estos 4 numeros");	
			listaHabiente=null;
			return;
		}
	}
	
	public void ubicarEstablecimiento(){

		entityManager.clear();
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable)=trim('"+this.sugestion+"')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 
				/*
				(Establecimiento)entityManager
			.createQuery("select e from Establecimiento e where e.nombreestable='"+this.sugestion+"'")
			.getSingleResult();
			*/
			
			establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
		}
		
		
	}
	
	public String retornarFactura(Integer numtransaccion){
		if(numtransaccion!=null){
			List<String> b =  entityManager.createNativeQuery("select numfactura from transaccion " +
				" where consecutivo = " + numtransaccion +"").getResultList();
			if (b.size()>0) {
				return b.get(0);
			}
			return null;
		}
		return null;
	}
	
	
	
	public void ubicarTarjeta(){
		entityManager.clear();
		List<Tarjeta> t = (ArrayList)entityManager
		.createQuery("select t from Tarjeta t where replace(t.tarjetahabiente,' ','')=replace('"+this.tarjetahabiente+"',' ','') " +
				" and t.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		
		if (t.size() > 0) {
			Tarjeta ta = t.get(0);
			
			tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
			tarjetaHome.setInstance(ta);
		}
	}
	
	public String getSugestion() {
		return sugestion;
	}


	public void setSugestion(String sugestion) {
		this.sugestion = sugestion;
	}

	public String getTarjetafin() {
		return tarjetafin;
	}

	public void setTarjetafin(String tarjetafin) {
		this.tarjetafin = tarjetafin;
	}

	public String getTarjetahabiente() {
		return tarjetahabiente;
	}

	public void setTarjetahabiente(String tarjetahabiente) {
		this.tarjetahabiente = tarjetahabiente;
	}
	
	public void editarAutovoz(int consecutivo){ 
		
		autovozHome.setAutovozConsecutivo(consecutivo);
		this.sugestion=autovozHome.getInstance().getEstablecimiento().getNombreestable();
		this.tarjetahabiente = autovozHome.getInstance().getTarjeta().getTarjetahabiente();
		this.tarjetafin = autovozHome.getInstance().getTarjeta().getNumerotarjeta()
		.substring(4,autovozHome.getInstance().getTarjeta().getNumerotarjeta().length()-4);
		
	}
	
	public String actualizarAutovoz(){
		try{
			//autovozHome.getInstance().setEstablecimiento(establecimientoHome.getInstance());
			//autovozHome.getInstance().setTarjeta(tarjetaHome.getInstance());
			//autovozHome.getInstance().setAsesor(identity.getUsername());
			String pais = establecimientoHome.getInstance().getPais().getCodigopais();
			
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
			String fecha = sdf.format(autovozHome.getInstance().getFechatx());
			
			List<Autovoz> la = entityManager.createQuery("select a from Autovoz a where a.fechatx = '"+fecha+"' and " +
					"a.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"' ").getResultList();
			if(la.size()>0){
				facesMessages.add("Esta tarjeta ya cuenta con una autorizacion de voz para esta fecha.  Verifique la informacion");
				return null;
			}
			
			List<Tasadolar> tds = entityManager.createQuery("select td from Tasadolar td " +
					"where td.id.codigopais = '" + pais + "' and td.id.fecha = '"+fecha+"'")
					.getResultList();
			if(tds.size()>0){
				List<Object> resultList = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
						"where tv.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"' and tv.viaje.consecutivo = v.consecutivo " +
								"" +
								"order by fechafin desc").getResultList();
				if(resultList.size()>0){
					//viajeactual = (Viaje) resultList.get(0);
					
					//BigInteger query = (BigInteger)entityManager
					//.createNativeQuery("select nextval('autovoz_consecutivo_seq')").getSingleResult();
					//autovozHome.getInstance().setConsecutivo(query.intValue());
					
					Expressions expressions = new Expressions();
					ValueExpression mensaje;
					autovozHome.setUpdatedMessage(
							expressions.createValueExpression("La autorizacion de voz para ************" + tarjetafin + " se ha actualizado"));
					autovozHome.update();
					AdministrarUsuario.auditarUsuario(6, "La autorizacion de voz para la tarjeta" + tarjetaHome.getTarjetaNumerotarjeta() + " se ha actualizado en la fecha "+sdf.format(autovozHome.getInstance().getFechatx())+" por un valor de " + autovozHome.getInstance().getValortxpesos() + "");

					this.tarjetafin = "";
					this.tarjetahabiente = "";
					this.sugestion = "";
					this.tarjetaHome.clearInstance();
					this.establecimientoHome.clearInstance();
					return "updated";
				}else{
					facesMessages.add("No hay un viaje asociado para esta tarjeta");
					return null;
				}
				
			}else{
				facesMessages.addToControl("fechatx", "No hay una tasa de dolar asociada para esta fecha");
				return null;
			}
			}catch(Exception e){
				facesMessages.add("Se ha presentado un inconveniente al actualizar la autorizacion de voz, verifique la informacion");
				facesMessages.add(e.getMessage());
				return null;
			}
	}
	
	
	public String guardarAutovoz(){ 
		try{
		autovozHome.getInstance().setEstablecimiento(establecimientoHome.getInstance());
		autovozHome.getInstance().setTarjeta(tarjetaHome.getInstance());
		autovozHome.getInstance().setAsesor(identity.getUsername());
		String pais = establecimientoHome.getInstance().getPais().getCodigopais();
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		String fecha = sdf.format(autovozHome.getInstance().getFechatx());
		
		/*
		List<Autovoz> la = entityManager.createQuery("select a from Autovoz a where a.fechatx = '"+fecha+"' and " +
				"a.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"' ").getResultList();
		if(la.size()>0){
			facesMessages.add("Esta tarjeta ya cuenta con una autorizacion de voz para esta fecha.  Verifique la informacion");
			return null;
		}
		*/
		List<Tasadolar> tds = entityManager.createQuery("select td from Tasadolar td " +
				"where td.id.codigopais = '" + pais + "' and td.id.fecha = '"+fecha+"'")
				.getResultList();
		if(tds.size()>0){
			List<Object> resultList = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
					"where tv.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"' and tv.viaje.consecutivo = v.consecutivo " +
							"" +
							"order by fechafin desc").getResultList();
			if(resultList.size()>0){
				//viajeactual = (Viaje) resultList.get(0);
				
				BigInteger query = (BigInteger)entityManager
				.createNativeQuery("select nextval('autovoz_consecutivo_seq')").getSingleResult();
				autovozHome.getInstance().setConsecutivo(query.intValue());
				
				Expressions expressions = new Expressions();
				ValueExpression mensaje;
				autovozHome.setCreatedMessage(
						expressions.createValueExpression("La autorizacion de voz para ************" + tarjetafin + " se ha registrado"));
				autovozHome.persist();
				
				AdministrarUsuario.auditarUsuario(5, 
						"Registro autorizacion de voz para la tarjeta" + tarjetaHome.getTarjetaNumerotarjeta() + 
						" Fecha "+sdf.format(autovozHome.getInstance().getFechatx())+
						" por un valor de " + autovozHome.getInstance().getValortxpesos() + "");

				
				autovozHome.clearInstance();
				
				this.tarjetafin = "";
				this.tarjetahabiente = "";
				this.sugestion = "";
				this.tarjetaHome.clearInstance();
				this.establecimientoHome.clearInstance();

				
				return "persisted";
				
			}else{
				facesMessages.add("No hay un viaje asociado para esta tarjeta");
				return null;
			}
			
		}else{
			facesMessages.addToControl("fechatx", "No hay una tasa de dolar asociada para esta fecha");
			return null;
		}
		}catch(Exception e){
			facesMessages.add("Se ha presentado un inconveniente al registrar la autorizacion de voz, verifique la informacion");
			return null;
		}
		
	}
    // add additional action methods

}
