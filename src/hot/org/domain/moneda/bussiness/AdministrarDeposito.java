package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;
import static org.jboss.seam.ScopeType.SESSION;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Banco;
import org.domain.moneda.entity.Depositostarjeta;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Viaje;
import org.domain.moneda.session.BancoHome;
import org.domain.moneda.session.DepositostarjetaHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TransaccionHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarDeposito")
@Scope(CONVERSATION)
public class AdministrarDeposito
{
    @Logger private Log log; 

    @In StatusMessages statusMessages;
    
    public String tarjetafin; // Almacena los ultimos 4 digitos de la tarjeta
    
    @In private FacesMessages facesMessages;
    
    @In (create=true)
	@Out
	private AdministrarUsuario AdministrarUsuario;
    
    @In
	private EntityManager entityManager;
    
    @In(create=true) @Out 
	private TarjetaHome	tarjetaHome;
    
    @In(create=true) @Out 
	private DepositostarjetaHome	depositostarjetaHome;
    
    @In(create=true) @Out 
	private BancoHome	bancoHome;
    
    List<String> listaHabiente = new ArrayList<String>();
    
    List<String> listaPromotor = new ArrayList<String>();
    
    List<String> listaAsesor = new ArrayList<String>();
    
    private String tarjetahabiente = "";
    
    private String promotor = "";
    
    private String asesor = "";
    
    @In Identity identity;

    public void administrarDeposito()
    {
        // implement your business logic here
        log.info("AdministrarDeposito.administrarDeposito() action called");
        statusMessages.add("administrarDeposito");
    }
    
    
    public List<String> autocompletarHabiente(Object suggest) {
			llenarHabiente();
			String pref = (String) suggest;
			ArrayList<String> result = new ArrayList<String>();
			Iterator<String> iterator = listaHabiente.iterator();
			while (iterator.hasNext()) {
				String elem = ((String) iterator.next());
				if ((elem != null && elem.toLowerCase().contains(pref.toLowerCase()))
					|| "".equals(pref)) {
					result.add(elem);
				}
			}
			return result;		
	}
    
    
/*    
    public List<String> autocompletarPromotor(Object suggest) 
    {
		llenarPromotor();
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = listaPromotor.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ( ( elem != null && elem.toLowerCase().contains(pref.toLowerCase()) )
				|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
		
    }
*/    
    /**
	 * Nueva version del metodo autocompletar. Comparacion basada en 
	 * expresiones regulares.
	 * @param nom
	 * @return
	 */
	public List<String> autocompletarPromotor(Object nom)
	{
		llenarPromotor(); 		// Metodo que carga la informacion de los nombres de las personas
		String nombre = (String) nom;
		List<String> result = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(nombre.toLowerCase());
		StringBuilder bldr = new StringBuilder();//builder usado para formar el patron
		
		long t1 = System.currentTimeMillis();
		// creamos el patron para la busqueda
		int lengthToken = nombre.split("\\s+").length;// longitud de palabras
														// en el nombre
		int cont = 1;
		while (tokens.hasMoreTokens()) {			
			if (cont == lengthToken && lengthToken == 1) {
				bldr.append(".*").append(tokens.nextToken()).append(".*");
			} else {
				if (cont++ < lengthToken--) {
					bldr.append(".*").append(tokens.nextToken()).append(".*");
					lengthToken--;
				} else {
					bldr.append(tokens.nextToken()).append(".*");
				}
			}
		}		
		Pattern p = Pattern.compile(bldr.toString().trim());
		Matcher match;		
		// realiza la busqueda
		for (String promo : listaPromotor) {//lista de promotores que es iterada			
			match = p.matcher(promo.toLowerCase());
			boolean b = match.find();
			if (b) {
				result.add(promo);				
			}		
		}
		long t2 = System.currentTimeMillis() - t1;
		System.out.println(">>>Tiempo total de la busqueda: " + t2 + "ms");		
		return result;
	}
    
    
    
    
    public List<String> autocompletarAsesor(Object suggest) {
		llenarAsesor();
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = listaAsesor.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().contains(pref.toLowerCase()) )
				|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;		
}
    // add additional action methods
    
    
    public void ubicarTarjeta(){
		entityManager.clear();
		List<Tarjeta> t=null; 
		String sql = "";
		if (tarjetaHome.getInstance().getNumerotarjeta() != null && 
				!tarjetaHome.getInstance().getNumerotarjeta().contentEquals("")){
			//System.out.println("Punto 1");
			sql = "select t from Tarjeta t where replace(t.tarjetahabiente,' ','')=replace('" +this.tarjetahabiente+ "',' ','')" +
			"and t.numerotarjeta like '%"+tarjetaHome.getInstance().getNumerotarjeta()+"'";
			
			if(identity.hasRole("Asesor")){
				sql = sql + " and t.promotor.asesor.documento = '"+identity.getUsername()+"'";
			}
			
			t = (ArrayList)entityManager
			.createQuery(sql).getResultList();
			if (t.size()<=0) {
				//System.out.println("Punto 2");
				
				sql = "select t from Tarjeta t where t.numerotarjeta like '%"+tarjetaHome.getInstance().getNumerotarjeta()+"'";
				
				if(identity.hasRole("Asesor")){
					sql = sql + " and t.promotor.asesor.documento = '"+identity.getUsername()+"'";
				}
				
				t = (ArrayList)entityManager
				.createQuery(sql).getResultList();
			}
		}else{
			//System.out.println("Punto 3");
			if (this.tarjetahabiente != null && 
					!this.tarjetahabiente.contentEquals("")){
			sql = "select t from Tarjeta t where replace(t.tarjetahabiente,' ','')=replace('"+this.tarjetahabiente+"',' ','') ";
			
			if(identity.hasRole("Asesor")){
				sql = sql + " and t.promotor.asesor.documento = '"+identity.getUsername()+"'";
			}
			t = (ArrayList)entityManager
			.createQuery(sql).getResultList();
			}
				
		}
		if (t != null)
		if (t.size() > 0) {
			Tarjeta ta; 
			if (tarjetaHome.getInstance().getNumerotarjeta() != null){
				if (t.size()>1){
					facesMessages.addToControl("numerotarjeta", "Estos numeros de tarjeta tienen asociada mas de una tarjeta, revise si el numero es el correcto");
				}
				ta = t.get(0);
				tarjetaHome.getInstance().setNumerotarjeta(ta.getNumerotarjeta());
				/*
				ta = (Tarjeta)entityManager
				.createQuery(sql)
				.getSingleResult();*/
			}else{
				if(t.size()>1){
					ta = (Tarjeta)(entityManager
					.createQuery(sql)
					.getResultList()).get(0);
					facesMessages.addToControl("numerotarjeta", "Este tarjetahabiente tiene asociada mas de una tarjeta, revise si el numero es el correcto");
					tarjetaHome.getInstance().setNumerotarjeta(ta.getNumerotarjeta());
					
					//Mensaje de doble tarjeta
				}else{
					ta = (Tarjeta)entityManager
					.createQuery(sql)
					.getSingleResult();
					tarjetaHome.getInstance().setNumerotarjeta(ta.getNumerotarjeta());
					
				}
				
				
			}
			if(ta!=null)
			cargarTotalesDepositos(ta.getNumerotarjeta());
			tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
			tarjetahabiente = ta.getTarjetahabiente();
			depositostarjetaHome.getInstance().setValordeposito(new BigDecimal(ta.getLimite()));
			bancoHome.setBancoCodbanco(ta.getBanco().getCodbanco());
			
			tarjetaHome.setInstance(ta);
		}else{
			sql = "select t from Tarjeta t where t.numerotarjeta like '%"+tarjetaHome.getInstance().getNumerotarjeta()+"'";
			
			
			
			t = (ArrayList)entityManager
			.createQuery(sql).getResultList();
			
			if(t.size()>0)
				facesMessages.addToControl("numerotarjeta", "Este numero de tarjeta existe pero no esta registrada en sus promotores");
			else
			facesMessages.addToControl("numerotarjeta", "Este numero de tarjeta no existe o no hay tarjetas registradas para este tarjetahabiente");
		}
	}
    
    Banco banco = null;
     
    public Banco getBanco() {
		return banco;
	}
    
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	
	BigDecimal totalDepositosPesos = new BigDecimal(0);
    BigDecimal totalDepositosBolivares = new BigDecimal(0);
    
    BigDecimal valorUltimoDeposito = new BigDecimal(0);
    
    BigDecimal totalTransaccionPesos = new BigDecimal(0);
    BigDecimal totalTransaccionDolares = new BigDecimal(0);
    
    BigDecimal totalTransaccionDolaresViajeV = new BigDecimal(0);
    BigDecimal totalTransaccionDolaresViajeI = new BigDecimal(0);
    
    
    BigDecimal totalDepositosBolivaresViajeV = new BigDecimal(0);
    BigDecimal totalDepositosBolivaresViajeI = new BigDecimal(0);
    
    BigDecimal saldoDepositosBolivaresViajeV = new BigDecimal(0);
    
    BigDecimal saldoCupoViajero = new BigDecimal(0);
    BigDecimal saldoCupoInternet = new BigDecimal(0);
    
    
    Date ultimodeposito = null;
    
    Date ultimotransaccion = null;
   
    
    public BigDecimal getValorUltimoDeposito() {
		return valorUltimoDeposito;
	}


	public void setValorUltimoDeposito(BigDecimal valorUltimoDeposito) {
		this.valorUltimoDeposito = valorUltimoDeposito;
	}


	public Date getUltimodeposito() {
		return ultimodeposito;
	}


	public void setUltimodeposito(Date ultimodeposito) {
		this.ultimodeposito = ultimodeposito;
	}


	public Date getUltimotransaccion() {
		return ultimotransaccion;
	}


	public void setUltimotransaccion(Date ultimotransaccion) {
		this.ultimotransaccion = ultimotransaccion;
	}
	
	


	public BigDecimal getTotalDepositosBolivaresViajeI() {
		return totalDepositosBolivaresViajeI;
	}


	public void setTotalDepositosBolivaresViajeI(
			BigDecimal totalDepositosBolivaresViajeI) {
		this.totalDepositosBolivaresViajeI = totalDepositosBolivaresViajeI;
	}


	public BigDecimal getSaldoDepositosBolivaresViajeV() {
		return saldoDepositosBolivaresViajeV;
	}


	public void setSaldoDepositosBolivaresViajeV(
			BigDecimal saldoDepositosBolivaresViajeV) {
		this.saldoDepositosBolivaresViajeV = saldoDepositosBolivaresViajeV;
	}


	public BigDecimal getSaldoCupoViajero() {
		return saldoCupoViajero;
	}


	public void setSaldoCupoViajero(BigDecimal saldoCupoViajero) {
		this.saldoCupoViajero = saldoCupoViajero;
	}


	public BigDecimal getSaldoCupoInternet() {
		return saldoCupoInternet;
	}


	public void setSaldoCupoInternet(BigDecimal saldoCupoInternet) {
		this.saldoCupoInternet = saldoCupoInternet;
	}


	public void cargarTotalesDepositos(String numerotarjeta){
    	//entityManager.clear();
		
		List<Viaje> vs = entityManager.createQuery("select v from Viaje v where v.consecutivo = " +
				"(select max(tv.id.consecutivoviaje) from Tarjetaviaje tv " +
				"where tv.id.numerotarjeta = '"+numerotarjeta+"')").getResultList();
		
		Viaje v = null;
		
		if (vs.size()>0){
			v = vs.get(0);
		
			
			
		
		totalDepositosBolivaresViajeV = (BigDecimal) entityManager
		.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.tipodep = 'V' and d.tarjeta.numerotarjeta in (select tv.id.numerotarjeta " +
				"from Tarjetaviaje tv where tv.id.consecutivoviaje = " +
				"(select max(tv.id.consecutivoviaje) from Tarjetaviaje tv " +
				"where tv.id.numerotarjeta = '"+numerotarjeta+"'))").getSingleResult();
		if(totalDepositosBolivaresViajeV==null)
			totalDepositosBolivaresViajeV = BigDecimal.ZERO;
		
		totalDepositosBolivaresViajeI = (BigDecimal) entityManager
		.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.tipodep = 'I' and d.tarjeta.numerotarjeta in (select tv.id.numerotarjeta " +
				"from Tarjetaviaje tv where tv.id.consecutivoviaje = " +
				"(select max(tv.id.consecutivoviaje) from Tarjetaviaje tv " +
				"where tv.id.numerotarjeta = '"+numerotarjeta+"'))").getSingleResult();
		if(totalDepositosBolivaresViajeI==null)
			totalDepositosBolivaresViajeI = BigDecimal.ZERO;
		
		
		valorUltimoDeposito = (BigDecimal) entityManager
		.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.tipodep = 'V' and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
				"d.fecha = (select max(d.fecha) from Depositostarjeta d " +
				"where d.tarjeta.numerotarjeta = '"+numerotarjeta+"')").getSingleResult();
		if(valorUltimoDeposito==null)
			valorUltimoDeposito = BigDecimal.ZERO;
		
		
		
		//this.saldoDepositosBolivaresViajeV = new BigDecimal(Math.round(v.getCupoinicialviajero()*4.3 - totalDepositosBolivaresViajeV.intValue()));
		if (v.getCupoinicialviajero() != null && v.getCupoinicialinternet() != null){
			this.saldoDepositosBolivaresViajeV = new BigDecimal(Math.round((v.getCupoinicialviajero()+v.getCupoinicialinternet())*4.3 
				- totalDepositosBolivaresViajeV.intValue() - totalDepositosBolivaresViajeI.intValue()));
		}else if (v.getCupoinicialviajero() != null ){
			this.saldoDepositosBolivaresViajeV = new BigDecimal(Math.round((v.getCupoinicialviajero())*4.3 
					- totalDepositosBolivaresViajeV.intValue() - totalDepositosBolivaresViajeI.intValue()));
			}else if (v.getCupoinicialinternet() != null){
				this.saldoDepositosBolivaresViajeV = new BigDecimal(Math.round((v.getCupoinicialinternet())*4.3 
						- totalDepositosBolivaresViajeV.intValue() - totalDepositosBolivaresViajeI.intValue()));
				}
		
		totalTransaccionDolaresViajeV = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.tipotx = 'V' and t.tarjeta.numerotarjeta in (select tv.id.numerotarjeta " +
				"from Tarjetaviaje tv where tv.id.consecutivoviaje = " +
				"(select max(tv.id.consecutivoviaje) from Tarjetaviaje tv " +
				"where tv.id.numerotarjeta = '"+numerotarjeta+"'))").getSingleResult();
		if(totalTransaccionDolaresViajeV==null)
			totalTransaccionDolaresViajeV = BigDecimal.ZERO;
		
		totalTransaccionDolaresViajeI = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.tipotx = 'I' and t.tarjeta.numerotarjeta in (select tv.id.numerotarjeta " +
				"from Tarjetaviaje tv where tv.id.consecutivoviaje = " +
				"(select max(tv.id.consecutivoviaje) from Tarjetaviaje tv " +
				"where tv.id.numerotarjeta = '"+numerotarjeta+"'))").getSingleResult();
		if(totalTransaccionDolaresViajeI==null)
			totalTransaccionDolaresViajeI = BigDecimal.ZERO;
		
		if (v.getCupoinicialviajero() !=null) 
		this.saldoCupoViajero = new BigDecimal(v.getCupoinicialviajero() - totalTransaccionDolaresViajeV.intValue());
		
		if (v.getCupoinicialinternet() !=null) 
		this.saldoCupoInternet = new BigDecimal(v.getCupoinicialinternet() - totalTransaccionDolaresViajeI.intValue());
		
    	totalDepositosPesos = (BigDecimal) entityManager
		.createQuery("select sum(d.depositopesos) " +
				"from Depositostarjeta d " +
				"where d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosPesos==null)
			totalDepositosPesos = BigDecimal.ZERO;
		totalDepositosBolivares = (BigDecimal) entityManager
		.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosBolivares==null)
			totalDepositosBolivares = BigDecimal.ZERO;
		totalTransaccionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionPesos==null)
			totalTransaccionPesos = BigDecimal.ZERO;
		totalTransaccionDolares = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionDolares==null)
			totalTransaccionDolares = BigDecimal.ZERO;
		
		this.ultimotransaccion = (Date) entityManager
		.createQuery("select max(t.fechatx) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		
		this.ultimodeposito = (Date) entityManager
		.createQuery("select max(d.fecha) " +
				"from Depositostarjeta d " +
				"where d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		}
    }

	public BigDecimal getTotalDepositosPesos() {
		return totalDepositosPesos;
	}


	public void setTotalDepositosPesos(BigDecimal totalDepositosPesos) {
		this.totalDepositosPesos = totalDepositosPesos;
	}


	public BigDecimal getTotalDepositosBolivares() {
		return totalDepositosBolivares;
	}


	public void setTotalDepositosBolivares(BigDecimal totalDepositosBolivares) {
		this.totalDepositosBolivares = totalDepositosBolivares;
	}


	public BigDecimal getTotalTransaccionPesos() {
		return totalTransaccionPesos;
	}


	public void setTotalTransaccionPesos(BigDecimal totalTransaccionPesos) {
		this.totalTransaccionPesos = totalTransaccionPesos;
	}


	public BigDecimal getTotalTransaccionDolares() {
		return totalTransaccionDolares;
	}


	public void setTotalTransaccionDolares(BigDecimal totalTransaccionDolares) {
		this.totalTransaccionDolares = totalTransaccionDolares;
	}


	public BigDecimal getTotalTransaccionDolaresViajeV() {
		return totalTransaccionDolaresViajeV;
	}


	public void setTotalTransaccionDolaresViajeV(
			BigDecimal totalTransaccionDolaresViajeV) {
		this.totalTransaccionDolaresViajeV = totalTransaccionDolaresViajeV;
	}
	
	public BigDecimal getTotalTransaccionDolaresViajeI() {
		return totalTransaccionDolaresViajeI;
	}


	public void setTotalTransaccionDolaresViajeI(
			BigDecimal totalTransaccionDolaresViajeI) {
		this.totalTransaccionDolaresViajeI = totalTransaccionDolaresViajeI;
	}


	public BigDecimal getTotalDepositosBolivaresViajeV() {
		return totalDepositosBolivaresViajeV;
	}


	public void setTotalDepositosBolivaresViajeV(
			BigDecimal totalDepositosBolivaresViajeV) {
		this.totalDepositosBolivaresViajeV = totalDepositosBolivaresViajeV;
	}


	public void llenarHabiente(){
		
		entityManager.clear();
		List<String> resultList;
		
		String sql = "";
		
		if(identity.hasRole("Asesor")){
			sql = " and tarjeta.promotor.asesor.documento = '"+identity.getUsername()+"'";
		}
		
		if(this.tarjetaHome.getInstance().getNumerotarjeta() != null 
				&& !this.tarjetaHome.getInstance().getNumerotarjeta().contentEquals("")){
			resultList = entityManager.createQuery("select distinct tarjeta.tarjetahabiente from Tarjeta tarjeta " +
				"where tarjeta.numerotarjeta = '"+this.tarjetaHome.getInstance().getNumerotarjeta()+"' " + sql)
				.getResultList();
		}else{
			resultList = entityManager.createQuery("select distinct tarjeta.tarjetahabiente from Tarjeta tarjeta " +
					" where 1 = 1 " + sql)
				.getResultList();
		}
		for(int i = 0; i < resultList.size(); i ++){
			String nov = resultList.get(i);
		}
		listaHabiente = resultList;
	}
    
    public void llenarPromotor(){
		
		entityManager.clear();
		List<String> resultList;
		String sql = "";
		if(identity.hasRole("Asesor")){
			sql = " where p.asesor.documento = '"+identity.getUsername()+"'";
		}
		
			resultList = entityManager.createQuery("select p.personal.nombre||' '||p.personal.apellido from Promotor p "+sql)
				.getResultList();
		
		for(int i = 0; i < resultList.size(); i ++){
			String nov = resultList.get(i);
		}
		listaPromotor = resultList;
	}
    
    public void llenarAsesor(){
		
		entityManager.clear();
		List<String> resultList;
		
			resultList = entityManager.createQuery("select a.personal.nombre||' '||a.personal.apellido from Asesor a ")
				.getResultList();
		
		for(int i = 0; i < resultList.size(); i ++){
			String nov = resultList.get(i);
		}
		listaAsesor = resultList;
	}
    
    public void asociarTarjetahabiente(String tarjetahabiente){
    	System.out.println("Tarjetahabiente " + tarjetahabiente);
    	this.tarjetahabiente = tarjetahabiente;
    }
    
    public void asociarPromotor(String promotor){
    	System.out.println("Promotor " + promotor);
    	this.promotor = promotor;
    }
    
    public void asociarAsesor(String asesor){
    	System.out.println("Asesor " + asesor);
    	this.asesor = asesor;
    }

	public String getTarjetahabiente() {
		return tarjetahabiente;
	}


	public void setTarjetahabiente(String tarjetahabiente) {
		this.tarjetahabiente = tarjetahabiente;
	}
	
	
	
	public List<String> getListaPromotor() {
		return listaPromotor;
	}


	public void setListaPromotor(List<String> listaPromotor) {
		this.listaPromotor = listaPromotor;
	}


	public String getPromotor() {
		return promotor;
	}


	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}


	public List<String> getListaHabiente() {
		return listaHabiente;
	}


	public void setListaHabiente(List<String> listaHabiente) {
		this.listaHabiente = listaHabiente;
	}


	public List<String> getListaAsesor() {
		return listaAsesor;
	}


	public void setListaAsesor(List<String> listaAsesor) {
		this.listaAsesor = listaAsesor;
	}


	public String getAsesor() {
		return asesor;
	}


	public void setAsesor(String asesor) {
		this.asesor = asesor;
	}


	@Begin(join=true)
	public void programarDeposito(){
		if(depositostarjetaHome.getInstance().getFecha()==null)
			depositostarjetaHome.getInstance().setFecha(new Date());
		if(depositostarjetaHome.getInstance().getTipodebolivar()==null)
			depositostarjetaHome.getInstance().setTipodebolivar("OFI");
		
		
		
	}
	
	@Begin(join=true)
	public void registrarDeposito(){
		
	}
	
	 
	@Begin(join=true) 
	public void registrarDeposito(Integer consecutivo){
		depositostarjetaHome.clearInstance();
		
		Depositostarjeta dpt = (Depositostarjeta) entityManager.createQuery("select d from Depositostarjeta d " +
				"where d.consecutivo = "+consecutivo+"").getSingleResult();
		
		
		Depositostarjeta dt = entityManager.find(Depositostarjeta.class, consecutivo);
		
		
		depositostarjetaHome.setDepositostarjetaConsecutivo(consecutivo);
		//depositostarjetaHome.wire();
		tarjetaHome.setTarjetaNumerotarjeta(depositostarjetaHome.getInstance().getTarjeta().getNumerotarjeta());
		bancoHome.setBancoCodbanco(tarjetaHome.getInstance().getBanco().getCodbanco());
		this.tarjetahabiente = tarjetaHome.getInstance().getTarjetahabiente();
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		if(depositostarjetaHome.getInstance().getTipodebolivar().equals("NEG")){
    		List<BigDecimal> tasabolivar = (List<BigDecimal>)entityManager.createQuery("select t.preciobolivar from Tasabolivarnegociado t " +
    				"where t.id.fecha = '"+sdf.format(depositostarjetaHome.getInstance().getFecha())+"' and " +
    						"t.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"' and " +
    								"t.id.tipo = 'D'")
			.getResultList();
    		if(tasabolivar.size()>0){
    			depositostarjetaHome.getInstance().setPreciobolivar(tasabolivar.get(0));
    		}else{
    			
    			//this.facesMessages.add("No hay una tasa de bolivar negociada para este promotor en la fecha asociada al deposito");
    			///return;
    		}
    	}else{
    		List<BigDecimal> tasabolivar = (List<BigDecimal>)entityManager.createQuery("select t.preciobolivar from Tasadebolivaroficina t " +
    				"where t.id.fecha = '"+sdf.format(depositostarjetaHome.getInstance().getFecha())+"' " +
    				"and t.id.tipo = 'D'")
			.getResultList();
    		if(tasabolivar.size()>0){
    			depositostarjetaHome.getInstance().setPreciobolivar(tasabolivar.get(0));
    		}else{
    			
    			//this.facesMessages.add("No hay una tasa de bolivar de oficina para la fecha asociada a este deposito");
    			
    		}
    	}
		cargarTotalesDepositos(depositostarjetaHome.getInstance().getTarjeta().getNumerotarjeta());
		return;
	}
	
	@End
    public void guardarDeposito(){
		/* Lo utilizan las asesoras para guardar un deposito
		 * 
		 */
    	depositostarjetaHome.getInstance().setTarjeta(tarjetaHome.getInstance());
    	
    	

    	BigInteger query = (BigInteger)entityManager
		.createNativeQuery("select nextval('depositostarjeta_consecutivo_seq')").getSingleResult();
    	depositostarjetaHome.getInstance().setConsecutivo(query.intValue());
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		depositostarjetaHome.getInstance().setPromotor(tarjetaHome.getInstance().getPromotor().getDocumento());
		depositostarjetaHome.getInstance().setAsesor(identity.getUsername());
		depositostarjetaHome.setCreatedMessage(
				expressions.createValueExpression("El deposito a la tarjeta " + 
						tarjetaHome.getInstance().getNumerotarjeta() + 
						" se ha programado exitosamente"));
		depositostarjetaHome.persist();
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		AdministrarUsuario.auditarUsuario(18, "Se ha programado un deposito para la tarjeta con numero " + 
				this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
				" para la fecha " + sdf.format(depositostarjetaHome.getInstance().getFecha()) + 
				" por valor de "+ depositostarjetaHome.getInstance().getValordeposito()+ 
				" con tipo bolivar " + depositostarjetaHome.getInstance().getTipodebolivar() + " " +
				"y con precio de bolivar " + depositostarjetaHome.getInstance().getPreciobolivar());
    	
		
		/*
		depositostarjetaHome.clearInstance();
		bancoHome.clearInstance();
		tarjetaHome.clearInstance();
		
		tarjetahabiente = "";
		tarjetafin = "";
    	
		depositostarjetaHome.getInstance().setFecha(new Date());
		depositostarjetaHome.getInstance().setTipodebolivar("OFI");
		
		this.totalDepositosBolivares = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeI = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeV = BigDecimal.ZERO;*/
		
		cancelar();
    	
    }
	
	@End
	public void cancelar(){
		System.out.println("Cancelar Deposito");
		depositostarjetaHome.clearInstance();
		bancoHome.clearInstance();
		tarjetaHome.clearInstance();
		
		tarjetahabiente = "";
		tarjetafin = "";
    	
		depositostarjetaHome.getInstance().setFecha(new Date());
		depositostarjetaHome.getInstance().setTipodebolivar("OFI");
		
		this.totalDepositosBolivares = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeI = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeV = BigDecimal.ZERO;
		this.totalDepositosPesos = BigDecimal.ZERO;
		this.totalTransaccionDolares = BigDecimal.ZERO;
		this.totalTransaccionDolaresViajeI = BigDecimal.ZERO;
		this.totalTransaccionDolaresViajeV = BigDecimal.ZERO;
		this.totalTransaccionPesos = BigDecimal.ZERO;
		this.valorUltimoDeposito = BigDecimal.ZERO;
		this.saldoDepositosBolivaresViajeV = BigDecimal.ZERO;
		this.saldoCupoInternet = BigDecimal.ZERO;
		this.saldoCupoViajero = BigDecimal.ZERO;
		
		this.ultimodeposito = null;
		this.ultimotransaccion = null;
		
	}
	
	public void limpiar(){
		System.out.println("Limpiar Deposito");
		depositostarjetaHome.clearInstance();
		bancoHome.clearInstance();
		tarjetaHome.clearInstance();
		
		tarjetahabiente = "";
		tarjetafin = "";
    	
		depositostarjetaHome.getInstance().setFecha(new Date());
		depositostarjetaHome.getInstance().setTipodebolivar("OFI");
		
		this.totalDepositosBolivares = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeI = BigDecimal.ZERO;
		this.totalDepositosBolivaresViajeV = BigDecimal.ZERO;
		this.totalDepositosPesos = BigDecimal.ZERO;
		this.totalTransaccionDolares = BigDecimal.ZERO;
		this.totalTransaccionDolaresViajeI = BigDecimal.ZERO;
		this.totalTransaccionDolaresViajeV = BigDecimal.ZERO;
		this.totalTransaccionPesos = BigDecimal.ZERO;
		this.valorUltimoDeposito = BigDecimal.ZERO;
		this.saldoDepositosBolivaresViajeV = BigDecimal.ZERO;
		this.saldoCupoInternet = BigDecimal.ZERO;
		this.saldoCupoViajero = BigDecimal.ZERO;
		
		this.ultimodeposito = null;
		this.ultimotransaccion = null;
		
	}
	
	public void registrarCuentaBanco(Depositostarjeta depositostarjeta){
		MathContext mc = new MathContext(3);
		System.out.println("Numero Consecutivo " + depositostarjeta.getConsecutivo());
		System.out.println("Numero de Tarjeta " + depositostarjeta.getTarjeta().getNumerotarjeta());
		if(depositostarjeta.getCuenta()!=null){
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			if(depositostarjeta.getTipodebolivar().equals("NEG")){
	    		List<BigDecimal> tasabolivar = entityManager.createQuery("select tbn.preciobolivar from Tasabolivarnegociado tbn where " +
	    					" tbn.id.documento = '" + depositostarjeta.getTarjeta().getPromotor().getDocumento() + "' and " +
	    					" tbn.id.fecha = (select max(tbn.id.fecha) from Tasabolivarnegociado tbn " +
	    					" where " +
	    					" tbn.id.documento = '" + depositostarjeta.getTarjeta().getPromotor().getDocumento() + "' and " +
	    					" to_char(tbn.id.fecha,'dd/MM/yyyy') = '" + sdf.format(depositostarjeta.getFecha())  + "' " +
	    					" and tbn.id.tipo = 'D') " +
	    					" and tbn.id.tipo = 'D' order by tbn.id.fecha desc").getResultList();
	    			
	    			/*
	    			(List<BigDecimal>)entityManager.
	    		createQuery("select t.preciobolivar from Tasabolivarnegociado t " +
	    			"where to_char(t.id.fecha,'dd/MM/yyyy') = '"+sdf.format(depositostarjeta.getFecha())+"' and " +
	    			"t.id.documento = '"+depositostarjeta.getTarjeta().getPromotor().getDocumento()+"' and " +
	    					"t.id.tipo = 'D'")
				.getResultList();*/
	    		if(tasabolivar.size()>0){
	    			depositostarjeta.setPreciobolivar(tasabolivar.get(0));
	    			depositostarjeta.setDepositopesos((tasabolivar.get(0)
	    					.multiply(depositostarjeta.getValordeposito())));
	    		}
	    	}else{
	    		List<BigDecimal> tasabolivar = (List<BigDecimal>) entityManager.createQuery( 
	    				"select t.preciobolivar from Tasadebolivaroficina t " +
	    				"where to_char(t.id.fecha,'dd/MM/yyyy') = '"+ 
	    				sdf.format(depositostarjeta.getFecha())+"' " +
	    				"and t.id.tipo = 'D'")
				.getResultList();
	    		if(tasabolivar.size()>0){
	    			depositostarjeta.setPreciobolivar(tasabolivar.get(0));
	    			System.out.println("*"+tasabolivar.get(0));
	    			System.out.println("*"+depositostarjeta.getValordeposito());
	    			depositostarjeta.setDepositopesos((tasabolivar.get(0)
	    					.multiply(depositostarjeta.getValordeposito())));
	    		}
	    	}
			depositostarjeta.setFechamod(new Date());
			depositostarjeta.setUsuariomod(identity.getUsername());
			entityManager.merge(depositostarjeta);
			entityManager.flush();
			
		}
		
		
		
	}
	
	//@End
    public void actualizarDeposito(){
    	Boolean estado = true;
    	
    	depositostarjetaHome.getInstance().setTarjeta(tarjetaHome.getInstance());
    	depositostarjetaHome.getInstance().setPromotor(tarjetaHome.getInstance().getPromotor().getDocumento());
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	
    	
    	if(depositostarjetaHome.getInstance().getTipodebolivar().equals("NEG")){
    		List<BigDecimal> tasabolivar = (List<BigDecimal>)entityManager.createQuery("select t.preciobolivar from Tasabolivarnegociado t " +
    				"where t.id.fecha = '"+sdf.format(depositostarjetaHome.getInstance().getFecha())+"' and " +
    						"t.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"'")
			.getResultList();
    		if(tasabolivar.size()>0){
    			depositostarjetaHome.getInstance().setPreciobolivar(tasabolivar.get(0));
    		}else{
    			estado = false;
    			this.facesMessages.add("No hay una tasa de bolivar negociada para este promotor en la fecha asociada al deposito");
    			return;
    		}
    	}else{
    		List<BigDecimal> tasabolivar = (List<BigDecimal>)entityManager.createQuery("select t.preciobolivar from Tasadebolivaroficina t " +
    				"where t.fecha = '"+sdf.format(depositostarjetaHome.getInstance().getFecha())+"'")
			.getResultList();
    		if(tasabolivar.size()>0){
    			
    			depositostarjetaHome.getInstance().setPreciobolivar(tasabolivar.get(0));
    		}else{
    			estado = false;
    			this.facesMessages.add("No hay una tasa de bolivar de oficina asociada a la fecha");
    			return;
    		}
    	}
    	//if(estado){
    	//System.out.println("Valor bolivar " + depositostarjetaHome.getInstance().getPreciobolivar().toString());
    	BigDecimal dp = new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue());
    	//System.out.println("Valor pesos " + dp.toString());    	
    	depositostarjetaHome.getInstance().setDepositopesos(new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue()));
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		depositostarjetaHome.setCreatedMessage(
				expressions.createValueExpression("El deposito a la tarjeta " + tarjetaHome.getInstance().getNumerotarjeta() + " se ha realizado exitosamente"));
		depositostarjetaHome.persist();
		
		cancelar();
		
    	//}
		//depositostarjetaHome.getInstance().setFecha(new Date());
		//depositostarjetaHome.getInstance().setTipodebolivar("OFI");
    	
    }
	
    
    public String actualizarDep(){
    	//Boolean estado = true;
    	
    	depositostarjetaHome.getInstance().setTarjeta(tarjetaHome.getInstance());
    	depositostarjetaHome.getInstance().setPromotor(tarjetaHome.getInstance().getPromotor().getDocumento());
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	BigDecimal dp = BigDecimal.ZERO;
    	
    	//if(estado){
    	//System.out.println("Valor bolivar " + depositostarjetaHome.getInstance().getPreciobolivar().toString());
    	if(depositostarjetaHome.getInstance().getValordeposito() != null && depositostarjetaHome.getInstance().getPreciobolivar() != null){
	    	dp = new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue());
	    	//System.out.println("Valor pesos " + dp.toString());    	
	    	depositostarjetaHome.getInstance().setDepositopesos(new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue()));
    	}
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		depositostarjetaHome.setUpdatedMessage(
				expressions.createValueExpression("El deposito a la tarjeta " + tarjetaHome.getInstance().getNumerotarjeta() + " se ha actualizado exitosamente"));
		depositostarjetaHome.update();
		
		
		AdministrarUsuario.auditarUsuario(19, "Se ha actualizado el deposito para la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
		" para la fecha " + sdf.format(depositostarjetaHome.getInstance().getFecha()) + " con tipo bolivar " + depositostarjetaHome.getInstance().getTipodebolivar() + " " +
				"y con precio de bolivar " + depositostarjetaHome.getInstance().getPreciobolivar());
    	
		
		cancelar();
		
		return "updated";
		
    	//}
		//depositostarjetaHome.getInstance().setFecha(new Date());
		//depositostarjetaHome.getInstance().setTipodebolivar("OFI");
    	
    }
	
	
	/*******  Proceso de Busqueda de Informacion de Depositos  *******/
	/**  Fecha: 16/01/2012
	 * 	 Autor: Carlos Rene Angarita Sanguino
	 */
	
	
	Date fechainicio = new Date();
	Date fechafin = new Date();
	Date fechadeposito = new Date((new Date()).getTime()+86400000);
	
	String numtarjeta = "";
	String tipodep = "";
	
	
	
	List<Depositostarjeta> depositostarjetas = new ArrayList<Depositostarjeta>();

	public List<Depositostarjeta> getDepositostarjetas() {
		return depositostarjetas;
	}

	public void setDepositostarjetas(List<Depositostarjeta> depositostarjetas) {
		this.depositostarjetas = depositostarjetas;
	}


	public Date getFechadeposito() {
		return fechadeposito;
	}


	public void setFechadeposito(Date fechadeposito) {
		this.fechadeposito = fechadeposito;
	}


	public Date getFechainicio() {
		return fechainicio;
	}


	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}


	public Date getFechafin() {
		return fechafin;
	}


	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}


	public String getNumtarjeta() {
		return numtarjeta;
	}


	public void setNumtarjeta(String numtarjeta) {
		this.numtarjeta = numtarjeta;
	}


	public String getTipodep() {
		return tipodep;
	}


	public void setTipodep(String tipodep) {
		this.tipodep = tipodep;
	}
	
	public void buscar(){
		String sql = "";
		String sqlcampos = "select d ";
		
		if(true){
			sql = "from Depositostarjeta d where 1 = 1 ";
		}else{
			sql = "from Depositostarjeta d, Baucher b where b.id.consecutivo = t.consecutivo ";
		}
		
		
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		
				
		if(this.fechainicio!=null){
			String fechai = sdf.format(this.fechainicio);
			sql = sql + " and d.fecha >= '" + fechai + "'";
		}
		
		if(this.fechafin!=null){
			String fechaf = sdf.format(this.fechafin);
			sql = sql + " and d.fecha <= '" + fechaf + "'";
		}
		
		if(!this.numtarjeta.contentEquals("")){
			
			sql = sql + " and d.tarjeta.numerotarjeta like   '%" + this.numtarjeta + "%'";
		}
		
		if(!this.tarjetahabiente.contentEquals("")){
			
			sql = sql + " and d.tarjeta.tarjetahabiente = '" + this.tarjetahabiente + "'";
		}

		if(!this.asesor.contentEquals("")){
	
			sql = sql + " and replace(d.tarjeta.promotor.asesor.personal.nombre || ' ' || d.tarjeta.promotor.asesor.personal.apellido,' ','') = replace('" + this.asesor + "',' ','')";
		}
		
		if(identity.hasRole("Asesor")){
			sql = sql + " and d.tarjeta.promotor.asesor.documento = '"+identity.getUsername()+"'";
		}
		
		if(this.banco!=null){
			
			sql = sql + " and d.tarjeta.banco.codbanco = '" + this.banco.getCodbanco() + "'";
		}
		
		if(!this.promotor.contentEquals("")){
			
			sql = sql + " and replace(d.tarjeta.promotor.personal.nombre || ' ' || d.tarjeta.promotor.personal.apellido,' ','') = replace('" + this.promotor + "',' ','')";
		}
		
		if(this.tipodep!=null)
		if(!this.tipodep.contentEquals("")){
			
			sql = sql + " and d.tipodep = '" + this.tipodep + "'";
		}
		
		
		if(this.estado.contentEquals("p")){
			sql = sql + " and d.cuenta.numcuenta is null ";
		}else if(this.estado.contentEquals("r")){
			sql = sql + " and d.cuenta.numcuenta is not null ";
		}
		
		this.depositostarjetas = (ArrayList)entityManager
		.createQuery(sqlcampos+sql).setMaxResults(100).getResultList();
		
		this.depositosDataModel = (ArrayList)entityManager
		.createQuery(sqlcampos+sql).setMaxResults(100).getResultList();
				
		//sql = "select sum(d.valordeposito), sum(d.depositopesos) " + sql;
		
		//this.totales = (Object)entityManager
		//.createQuery(sql).setMaxResults(100).getSingleResult();
		
	}
	
	
	public void reiniciar(){
		this.fechainicio = null;
		this.fechafin = null;
		this.numtarjeta = null;
		this.tarjetahabiente = null;
		this.promotor = null;
		
		if (!identity.hasRole("Asesor")){
			this.asesor = null;		
		}
		this.depositostarjetas = null;
		
	}
	
	
	/**** Integrando un DATAMODEL para edicion en la tabla   *****/
	
	
	@DataModel
	private List<Depositostarjeta> depositosDataModel=new ArrayList<Depositostarjeta>();

	public List<Depositostarjeta> getDepositosDataModel() {
		return depositosDataModel;
	}


	public void setDepositosDataModel(List<Depositostarjeta> depositosDataModel) {
		this.depositosDataModel = depositosDataModel;
	}
	
	public void cambiarValor(Depositostarjeta depositostarjeta){
		System.out.println("");
	}
	
	String estado = "p";
	
	
	
	
	
	
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**** Consulta de aproximacion de deposito  ****/
	
	@DataModel
	private List<Object[]> depositosProgramar = new ArrayList<Object[]>();

	public List<Object[]> getDepositosProgramar() {
		return depositosProgramar;
	}


	public void setDepositosProgramar(List<Object[]> depositosProgramar) {
		this.depositosProgramar = depositosProgramar;
	}
	
	public void consultarDepositos(){
		String sql = "SELECT "+
  "  public.tarjeta.numerotarjeta,"+ //0
  "  public.banco.nombrebanco,"+ //1
  "  public.banco.dias,"+ //2
  "  public.franquicia.nombrefranquicia,"+ //3
  "  public.tarjeta.limite,"+ //4
  "  public.tarjeta.fechainicio,"+ //5
  "  public.tarjeta.fechafin,"+ //6
  "  public.tarjeta.tarjetahabiente,"+ //7
  "  public.viaje.cupoinicialinternet," + //8
  "  public.viaje.cupoinicialviajero," + //9
  "  public.viaje.cupointernet," + //10
  "  public.viaje.cupoviajero," + //11
  "  trans.fechatx as transfechatx, " + //12
  "  trans.tipotx as transtipotx," + //13
  "  trans.valortxpesos as transvalortxpesos," + //14
  "  trans.valortxdolares as transvalortxdolares," + //15
  "  depo.valordeposito as depovalordeposito," + //16
  "  depo.preciobolivar," +
  "  depo.fecha as depofecha," +
  "  depo.depositopesos," +
  "  depo.numcuenta," +
  "  depototal.total," +
  "  trans.fechatx + CAST(''||(banco.dias+case when extract(dow from trans.fechatx)>" +
  "  extract(dow from trans.fechatx + CAST(''||banco.dias||' days' AS INTERVAL))" +
  "  then 1 else 0  end)||' days' AS INTERVAL) as fechadepopost, " +
  "  now()+'1 day'," +
  "  true " + 
" FROM" +
"  public.viaje" +
"  INNER JOIN public.tarjetaviaje ON (public.viaje.consecutivo = public.tarjetaviaje.consecutivoviaje)" +
"  INNER JOIN public.tarjeta ON (public.tarjetaviaje.numerotarjeta = public.tarjeta.numerotarjeta)" +
"  INNER JOIN public.promotor ON (public.tarjeta.documento = public.promotor.documento)" +
"  INNER JOIN public.personal ON (public.tarjeta.documento = public.personal.documento)" +
"  INNER JOIN public.personal p2 ON (public.promotor.asesor = p2.documento)" +
"  INNER JOIN public.banco ON (public.tarjeta.bancoemisor = public.banco.codbanco)" +
"  INNER JOIN public.franquicia ON (public.tarjeta.franquicia = public.franquicia.codfranquicia)" +
"  LEFT OUTER JOIN (SELECT" +
"  	public.depositostarjeta.numerotarjeta," +
"  	sum(public.depositostarjeta.valordeposito) AS total," +
" 	sum(public.depositostarjeta.depositopesos) AS totalpesos" +
"	FROM" +
"  	public.depositostarjeta" +
"	GROUP BY" +
"  	public.depositostarjeta.numerotarjeta) depototal" +
"   ON (public.tarjeta.numerotarjeta = depototal.numerotarjeta)" +
"  LEFT OUTER JOIN (SELECT" +
"  public.depositostarjeta.numerotarjeta," +
"  public.depositostarjeta.valordeposito," +
"  public.depositostarjeta.preciobolivar," +
"  public.depositostarjeta.fecha," +
"  public.depositostarjeta.consecutivo," +
"  public.depositostarjeta.depositopesos," +
"  public.depositostarjeta.tipodep," +
"  public.depositostarjeta.numcuenta" +
" FROM" +
"  public.depositostarjeta" +
" WHERE" +
"   (public.depositostarjeta.numerotarjeta," +
"  public.depositostarjeta.consecutivo)IN (SELECT" +
"  public.depositostarjeta.numerotarjeta," +
"  max(public.depositostarjeta.consecutivo) AS maxdepositos" +
" FROM" +
" public.depositostarjeta" +
" GROUP BY" +
"  public.depositostarjeta.numerotarjeta)) depo" +
" ON (public.tarjeta.numerotarjeta = depo.numerotarjeta)" +
"  LEFT OUTER JOIN (SELECT" +
"  public.transaccion.numerotarjeta," +
"  public.transaccion.fechatx," +
"  public.transaccion.tipotx," +
" sum(public.transaccion.valortxpesos) as valortxpesos," +
"  sum(public.transaccion.valortxdolares) as valortxdolares" +
" FROM" +
" public.transaccion " +
" WHERE" +
"   (public.transaccion.numerotarjeta," +
"   public.transaccion.fechatx) IN ( SELECT" +
"  public.transaccion.numerotarjeta," +
"  max(public.transaccion.fechatx) AS maxtransaccion" +
" FROM" +
" public.transaccion" +
"  GROUP BY" +
"  public.transaccion.numerotarjeta) " +
"GROUP BY public.transaccion.numerotarjeta," +
"  public.transaccion.fechatx," +
"  public.transaccion.tipotx)  trans" +
"  ON (public.tarjeta.numerotarjeta = trans.numerotarjeta)" +
" WHERE" +
"  public.viaje.cupoviajero > 10" +
"  AND (depo.numcuenta is not null or depo.valordeposito is null) " +
"  AND trans.fechatx + CAST(''||(banco.dias+case when extract(dow from trans.fechatx)>" +
"  extract(dow from trans.fechatx + CAST(''||banco.dias||' days' AS INTERVAL))" +
"  then 1 else 0" +
"  end)||' days' AS INTERVAL)<=now()+'1 days'";
		
		if(!this.asesor.contentEquals("")){
			
			sql = sql + " and p2.nombre || ' ' || p2.apellido = '" + this.asesor + "'";
		}
		
		if(!this.promotor.contentEquals("")){
			
			sql = sql + " and public.personal.nombre || ' ' || public.personal.apellido = '" + this.promotor + "'";
		}
		
		if(identity.hasRole("Asesor")){
			sql = sql + " and p2.documento = '" + identity.getUsername() + "' ";
		}
		
if(!this.numtarjeta.contentEquals("")){
			
			sql = sql + " and public.tarjeta.numerotarjeta = '" + this.numtarjeta + "'";
		}
		
		if(!this.tarjetahabiente.contentEquals("")){
			
			sql = sql + " and public.tarjeta.tarjetahabiente = '" + this.tarjetahabiente + "'";
		}
		
	this.depositosProgramar = entityManager.createNativeQuery(sql)
	.getResultList();
	
	//System.out.println(this.depositosProgramar.get(0));
	}
	
	public void generarDepositos(List<Object[]> datamodel){
		System.out.println("GENERAR DEPOSITOS");
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		for(int i=0;i<datamodel.size();i++){
			Object[] o = datamodel.get(i);
			System.out.println("Prueba "+o[0]+"--"+o[23]+"--"+o[24]);
			
			if((Boolean)o[24]){
			
			Depositostarjeta dt = new Depositostarjeta();
			
	    	//System.out.println("Valor bolivar " + depositostarjetaHome.getInstance().getPreciobolivar().toString());
	    	//BigDecimal dp = new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue());
	    	//System.out.println("Valor pesos " + dp.toString());    	
	    	//depositostarjetaHome.getInstance().setDepositopesos(new BigDecimal(depositostarjetaHome.getInstance().getValordeposito().intValue()*depositostarjetaHome.getInstance().getPreciobolivar().intValue()));
			System.out.println("Aca "+o[4]);
	    	dt.setTarjeta(entityManager.find(Tarjeta.class, o[0]));
	    	BigDecimal valor = BigDecimal.ZERO;
	    	try{
	    		valor = ((BigDecimal)o[4]);
	    	}catch(Exception e){
	    		valor = new BigDecimal((String)o[4]);
	    	}
	    	
	    	dt.setUsuariomod(identity.getUsername());
	    	dt.setFechamod(new Date());
	    	dt.setAsesor(identity.getUsername());
	    	dt.setValordeposito(valor);
	    	dt.setTipodebolivar(tipobolivar);
	    	//if((Date) o[22]<= this.fechadeposito){
	    		//dt.setFecha((Date) o[22]);
	    	//}
	    	dt.setFecha(this.fechadeposito);
	    	BigInteger query = (BigInteger)entityManager
			.createNativeQuery("select nextval('depositostarjeta_consecutivo_seq')").getSingleResult();
	    	dt.setConsecutivo(query.intValue());
			
	    	entityManager.clear();
	    	entityManager.persist(dt);
	    	entityManager.flush();
			}
			//if (o instanceof Map) {
			      //Map map = (Map) o;
			      //System.out.println("Heureka!");
			//    }

			//List a = (List) o;
			//Field[] lista = o.getClass().getFields();
			//System.out.println(lista[0].toString());
		}
	}
	
	public void marcar(Object[] x){
		System.out.println("Inicio..."+ x[24]);
		/*
		if(x[24].equals(true)){
			x[24]=false;
		}else{
			x[24]=true;
		}
		*/
		System.out.println("Entro..."+ x[24]);
	}
	
	private String tipobolivar="OFI";

	public String getTipobolivar() {
		return tipobolivar;
	}


	public void setTipobolivar(String tipobolivar) {
		this.tipobolivar = tipobolivar;
	}
	
	@In(create=true)
	AdministrarVariable AdministrarVariable;
	
	
	public String eliminarDeposito(){
		
		//auditoria
		String depoEstado = 
			depositostarjetaHome.getInstance().getDepositopesos() == null ? 
					"Programado" : "Procesado";		
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy");		
				
		AdministrarUsuario.auditarUsuario(22, "Elimino Deposito de la tarjeta: " +
				depositostarjetaHome.getInstance().getTarjeta().getNumerotarjeta() +
				" fecha: " + sdf.format(depositostarjetaHome.getInstance().getFecha() ) +
				" valor: " +  depositostarjetaHome.getInstance().getDepositopesos() +
				" del promotor: " + depositostarjetaHome.getInstance().getPromotor() +
				" estado deposito: " + depoEstado );
		depositostarjetaHome.remove();		
		reiniciar();
		entityManager.clear();
		//buscar();
		return "removed";
	}	
	
}
