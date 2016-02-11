package org.domain.moneda.bussiness;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.xml.rpc.encoding.Deserializer;

import org.domain.moneda.entity.Activacion;
import org.domain.moneda.entity.Banco;
import org.domain.moneda.entity.Bindb;
import org.domain.moneda.entity.Depositostarjeta;
import org.domain.moneda.entity.Franquicia;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Porcentajecomisiontx;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tarjetaviaje;
import org.domain.moneda.entity.Transaccion;
import org.domain.moneda.entity.Viaje;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TarjetaList;
import org.domain.moneda.session.ViajeHome;
import org.domain.moneda.util.BinJson;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.EnviarMailAlertas;
import org.domain.moneda.util.ExpresionesRegulares;
import org.domain.moneda.util.ValidarTarjeta;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Name("AdministrarTarjeta") 
@Scope(ScopeType.CONVERSATION)
public class AdministrarTarjeta
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In
	private EntityManager entityManager;
    
    List<String> lista = new ArrayList<String>();
    
    @In
    private FacesMessages facesMessages;
    
    Boolean valida = true;
    
    @In(create=true) @Out 
	private TarjetaHome	tarjetaHome;
	
	@In(create=true) @Out 
	private PromotorHome promotorHome;
	
	@In(create=true) @Out 
	private ViajeHome viajeHome;
	 
	@In (create=true)
	@Out
	private EnviarMailAlertas enviarMailAlertas;
	
	List<Transaccion> tviajero = new ArrayList<Transaccion>();
	List<Transaccion> tinternet = new ArrayList<Transaccion>();
	
	Viaje viajeactual = new Viaje();
	
	List<Porcentajecomisiontx> listaporc = new ArrayList<Porcentajecomisiontx>();
	
	@In (create=true)
	@Out
	private AdministrarUsuario AdministrarUsuario;
	
	private boolean sinViaje = false;
    
    public void administrarTarjeta()
    {
        // implement your business logic here
        log.info("AdministrarTarjeta.administrarTarjeta() action called");
        statusMessages.add("administrarTarjeta");
    }

    // add additional action methods
    
    public void registrarTarjeta(){
    	//llenarPromotores();
    	//if(tarjetaHome.isManaged()){
    		//this.nombre = tarjetaHome.getInstance().getPromotor().getPersonal().getNombre() + " " +
    		//tarjetaHome.getInstance().getPromotor().getPersonal().getApellido();
    	//}
    }
    
    String nombreasesor ="";
    
    public String getNombreasesor() {
		return nombreasesor;
	}

	public void setNombreasesor(String nombreasesor) {
		this.nombreasesor = nombreasesor;
	}

	//@Begin(join=true)
    public void editarTarjeta(String numerotarjeta){
    	this.viajeasignado = false;
    	llenarPromotores() ;
    	this.viajeactual= new Viaje();
    	cargarViaje(numerotarjeta);
    	
    	ArrayList a = (ArrayList) entityManager.createNativeQuery(" select v.consecutivo from viaje v, " +
		" tarjetaviaje tv where v.consecutivo = tv.consecutivoviaje and " +
		" tv.estado = 1 and now() between v.fechainicio and v.fechafin and " +
		" tv.numerotarjeta='"+numerotarjeta+"'").getResultList();
    	if(a.size()>0){
    		this.activo = true;
    	}else{
    		this.activo = false;
    	}
    	
    	
    	this.bloqueos = (List<Object>) entityManager.createNativeQuery("SELECT public.bloqueotarjeta.fecha, public.bloqueotarjeta.observacion " +
    	"FROM  public.tarjetaviaje " +
    	"INNER JOIN public.tarjeta ON (public.tarjetaviaje.numerotarjeta = public.tarjeta.numerotarjeta) " +
    	"INNER JOIN public.bloqueotarjeta ON (public.tarjetaviaje.numerotarjeta = public.bloqueotarjeta.numerotarjeta) " +
    	"AND (public.tarjetaviaje.consecutivoviaje = public.bloqueotarjeta.consecutivo) " +
    	"INNER JOIN public.viaje ON (public.tarjetaviaje.consecutivoviaje = public.viaje.consecutivo) " +
    	"WHERE " +
    	"public.tarjetaviaje.estado = 0 AND " +
    	"public.bloqueotarjeta.numerotarjeta = '"+numerotarjeta+"' " +
    	"order by fecha desc").getResultList();
    	
    	
    	
    	
    	ArrayList b = (ArrayList) entityManager.createNativeQuery(" select v.consecutivo from viaje v, " +
    			" tarjetaviaje tv, tarjeta t where v.consecutivo = tv.consecutivoviaje and " +
    			" tv.numerotarjeta = t.numerotarjeta and " +
    			" t.fechainicio = v.fechainicio and " +
    			" tv.estado = 0 and " +
    			" tv.numerotarjeta='"+numerotarjeta+"'").getResultList();
    	    	if(b.size()>0){
    	    		this.bloqueada = true;
    	    	}else{
    	    		this.bloqueada = false;
    	    	}
    	
    	
    	
    	//if(tarjetaHome.isManaged()){
    	
    	tarjetaHome.setTarjetaNumerotarjeta(numerotarjeta);
    	
    	Tarjeta ta = (Tarjeta)entityManager
		.createQuery("select t from Tarjeta t where t.numerotarjeta = '"+numerotarjeta+"'")
		.getSingleResult();
    	
    	tarjetaHome.setInstance(ta);
    	
    	Personal pasesor = entityManager.find(Personal.class, ta.getPromotor().getAsesor().getDocumento());
    	if(pasesor!=null)
    		this.nombreasesor = pasesor.getNombre();
    	
    	this.nombre = tarjetaHome.getInstance().getPromotor().getPersonal().getNombre() + " " +
    	tarjetaHome.getInstance().getPromotor().getPersonal().getApellido();
    	
    	cargarComisiones();
    	
    	cargarTotales(numerotarjeta);
    	/*
    	tviajero = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'V'").getResultList();
    	
    	tinternet = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'I'").getResultList();
    	*/
    	
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
    	
    	
    	if(this.viajeactual.getConsecutivo()>0){
    	tviajero = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'V' and t.fechatx between '"+sdf.format(this.viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(this.viajeactual.getFechafin())+"'").getResultList();
    	
    	tinternet = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'I' and t.fechatx between '"+sdf.format(this.viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(this.viajeactual.getFechafin())+"'").getResultList();
    	
    	depositos = (List<Depositostarjeta>) entityManager.createQuery(
    			"select d from Depositostarjeta d " +
    			"where d.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and d.fecha between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"'").getResultList();}
    	
    	//}
    	
    	/* Carga los dias restantes del viaje */
    	Date d = new Date();
    	if (tarjetaHome.getInstance().getFechafin() != null){
    		long t = tarjetaHome.getInstance().getFechafin().getTime() - d.getTime();	
    		this.dias = t/(60*60*24*1000);
    	}
		//System.out.println("Dias " + dias);   	 
    	
		SimpleDateFormat sdf2 =new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		AdministrarUsuario.auditarUsuario(32, "Consulto la tarjeta  " 
				+ tarjetaHome.getInstance().getNumerotarjeta() + "");
    	 
    }
    
    
    
    Viaje viajeanterior;
    
    public Viaje getViajeanterior() {
		return viajeanterior;
	}

	public void setViajeanterior(Viaje viajeanterior) {
		this.viajeanterior = viajeanterior;
	}

	public void solicitarNuevoViaje(String numerotarjeta){
    	this.viajeasignado = false;
    	llenarPromotores() ;
    	this.viajeanterior = new Viaje();
    	this.viajeactual = new Viaje();
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
    	List<Viaje> viajesahora = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
				"where tv.tarjeta.numerotarjeta = '"+numerotarjeta+"' and tv.viaje.consecutivo = v.consecutivo " +
				"and to_char(v.fechainicio,'yyyy') = to_char(now(),'yyyy')" +
				"order by fechafin desc").getResultList();
    	if(viajesahora.size()>0){
    		Viaje v = viajesahora.get(0);
    		this.facesMessages.add("Ya se encuentra un viaje registrado para esta tarjeta en este periodo del año.  El viaje comienza " +
    				"" + sdf.format(v.getFechainicio()) + " hasta el " + sdf.format(v.getFechafin()) );
    	}
    	
    	List<Object> resultList = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
				"where tv.tarjeta.numerotarjeta = '"+numerotarjeta+"' and tv.viaje.consecutivo = v.consecutivo " +
						"" +
						"order by fechafin desc").getResultList();
		
		if(resultList.size()>0){
			this.viajeanterior = (Viaje) resultList.get(0);
		}
    	/*
    	ArrayList a = (ArrayList) entityManager.createNativeQuery(" select v.consecutivo from viaje v, " +
		" tarjetaviaje tv where v.consecutivo = tv.consecutivoviaje and " +
		" tv.estado = 1 and now() between v.fechainicio and v.fechafin and " +
		" tv.numerotarjeta='"+numerotarjeta+"'").getResultList();
    	if(a.size()>0){
    		this.activo = true;
    	}else{
    		this.activo = false;
    	}
    	
    	
    	this.bloqueos = (List<Object>) entityManager.createNativeQuery("SELECT public.bloqueotarjeta.fecha, public.bloqueotarjeta.observacion " +
    	"FROM  public.tarjetaviaje " +
    	"INNER JOIN public.tarjeta ON (public.tarjetaviaje.numerotarjeta = public.tarjeta.numerotarjeta) " +
    	"INNER JOIN public.bloqueotarjeta ON (public.tarjetaviaje.numerotarjeta = public.bloqueotarjeta.numerotarjeta) " +
    	"AND (public.tarjetaviaje.consecutivoviaje = public.bloqueotarjeta.consecutivo) " +
    	"INNER JOIN public.viaje ON (public.tarjetaviaje.consecutivoviaje = public.viaje.consecutivo) " +
    	"WHERE " +
    	"public.tarjetaviaje.estado = 0 AND " +
    	"public.bloqueotarjeta.numerotarjeta = '"+numerotarjeta+"' " +
    	"order by fecha desc").getResultList();
    	
    	
    	
    	
    	ArrayList b = (ArrayList) entityManager.createNativeQuery(" select v.consecutivo from viaje v, " +
    			" tarjetaviaje tv where v.consecutivo = tv.consecutivoviaje and " +
    			" tv.estado = 0 and " +
    			" tv.numerotarjeta='"+numerotarjeta+"'").getResultList();
    	    	if(b.size()>0){
    	    		this.bloqueada = true;
    	    	}else{
    	    		this.bloqueada = false;
    	    	}
    	
    	*/
    	
    	//if(tarjetaHome.isManaged()){
    	
    	tarjetaHome.setTarjetaNumerotarjeta(numerotarjeta);
    	
    	Tarjeta ta = (Tarjeta)entityManager
		.createQuery("select t from Tarjeta t where t.numerotarjeta = '"+numerotarjeta+"'")
		.getSingleResult();
    	
    	tarjetaHome.setInstance(ta);
    	
    	tarjetaHome.getInstance().setFechainicio(null);
    	tarjetaHome.getInstance().setFechafin(null);
    	
    	Personal pasesor = entityManager.find(Personal.class, ta.getPromotor().getAsesor().getDocumento());
    	if(pasesor!=null)
    		this.nombreasesor = pasesor.getNombre();
    	
    	this.nombre = tarjetaHome.getInstance().getPromotor().getPersonal().getNombre() + " " +
    	tarjetaHome.getInstance().getPromotor().getPersonal().getApellido();
    	
    	cargarComisiones();
    	
    	
    	
    	//}
    	
    	/* Carga los dias restantes del viaje */
    	Date d = new Date();
    	if (tarjetaHome.getInstance().getFechafin() != null){
    		long t = tarjetaHome.getInstance().getFechafin().getTime() - d.getTime();	
    		this.dias = t/(60*60*24*1000);
    	}
		System.out.println("Dias " + dias);
    	 
    	
    	 
    }
    
   
    
    public void bloquearViajeTarjeta(){
    	String sql="";
    	
    	sql = "update tarjetaviaje set " +
    		" estado = 0 " +
    		" where numerotarjeta = '" + this.tarjetaHome.getInstance().getNumerotarjeta() + "' and " +
    		" consecutivoviaje in (select v.consecutivo from viaje v, " +
    		" tarjetaviaje tv where v.consecutivo = tv.consecutivoviaje and " +
    		" tv.estado = 1 and now() between v.fechainicio and v.fechafin)";
    	
    	entityManager.createNativeQuery(sql).executeUpdate();
    	entityManager.flush();
    	facesMessages.add("Se ha bloqueado el viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
    			"si hay alguna otra tarjeta asociada al viaje se debera desactivar individualmente");
    	AdministrarUsuario.auditarUsuario(10, "Se ha bloqueado el viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
		"si hay alguna otra tarjeta asociada al viaje se debera desactivar individualmente");
    	
    	entityManager.createNativeQuery("insert into bloqueotarjeta " +
    			"(numerotarjeta, consecutivo, observacion, fecha) " +
    			"values ('"+this.tarjetaHome.getInstance().getNumerotarjeta()+"', "+this.viajeactual.getConsecutivo()+"," +
    					"'"+this.observacion+"',now())").executeUpdate();
    	entityManager.flush();
    	this.observacion = "";

    	return;
    }
    
    public void desbloquearViajeTarjeta(){
    	String sql="";
    	
    	sql = "update tarjetaviaje set " +
    		" estado = 1 " +
    		" where numerotarjeta = '" + this.tarjetaHome.getInstance().getNumerotarjeta() + "' and " +
    		" consecutivoviaje in (select v.consecutivo from viaje v, " +
    		" tarjetaviaje tv, tarjeta t where v.consecutivo = tv.consecutivoviaje and " +
    		" t.numerotarjeta = tv.numerotarjeta and " +
    		" tv.estado = 0 and (now() between v.fechainicio and v.fechafin or v.fechafin = t.fechafin ))";
    	
    	entityManager.createNativeQuery(sql).executeUpdate();
    	entityManager.flush();
    	facesMessages.add("Se ha desbloqueado el viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
    			"si hay alguna otra tarjeta asociada al viaje se debera desbloquear individualmente");
    	
    	AdministrarUsuario.auditarUsuario(11, "Se ha desbloqueado el viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
		"si hay alguna otra tarjeta asociada al viaje se debera desactivar individualmente");
    	
    	entityManager.createNativeQuery("insert into bloqueotarjeta " +
    			"(numerotarjeta, consecutivo, observacion, fecha) " +
    			"values ('"+this.tarjetaHome.getInstance().getNumerotarjeta()+"', "+this.viajeactual.getConsecutivo()+"," +
    					"'"+this.observacion+"',now())").executeUpdate();
    	entityManager.flush();
    	
    	return;
    }
    
    public Boolean activo = false;
    
    public Boolean bloqueada = false;
    
    
     
    public Boolean getBloqueada() {
		return bloqueada;
	}

	public void setBloqueada(Boolean bloqueada) {
		this.bloqueada = bloqueada;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void activarViajeTarjeta(){
    	String sql="";
    	
    	sql = "update tarjetaviaje set " +
    		" estado = 1 " +
    		" where numerotarjeta = '" + this.tarjetaHome.getInstance().getNumerotarjeta() + "' and " +
    		" consecutivoviaje in (select v.consecutivo from viaje v, " +
    		" tarjetaviaje tv where v.consecutivo = tv.consecutivoviaje and " +
    		" tv.estado = 1 and now() between v.fechainicio and v.fechafin)";
    	
    	entityManager.createNativeQuery(sql).executeUpdate();
    	entityManager.flush();
    	facesMessages.add("Se ha bloqueado el viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
    			"si hay alguna otra tarjeta asociada al viaje se debera desactivar individualmente");
    	
    	return;
    }
    
	/**
	 * Retorna la tasa de dolar para el detalle de las Tx
	 * @param fecha
	 * @param codpais
	 * @param documento
	 * @return tasa dolar del dia
	 */
    public BigDecimal tasaDolar(Date fecha, String codpais, String documento){
    	try{
    		BigDecimal t = BigDecimal.ZERO;
    		//busca tasa negocia del promotor del dolar
    		List<BigDecimal> ts =  entityManager.createQuery("select pt.tasa from Promotortasa pt " +
    				"where pt.id.codigopais = '" + codpais + "' and pt.id.fecha <= '"+fecha+"' and" +
    						" ('"+fecha+"' <= pt.fechafin or pt.fechafin is null) and " +
    				"pt.id.documento = '"+documento+"'" +
    						" and pt.id.fecha = (select min(pt.id.fecha) from Promotortasa pt " +
    				"where pt.id.codigopais = '" + codpais + "' and " +
    				"pt.id.documento = '"+documento+"' and pt.id.fecha <= '"+fecha+"' and" +
    						" ('"+fecha+"' <= pt.fechafin or pt.fechafin is null) " +
    						")")
    						.getResultList();
    		
    		if (ts.size()>0){
    			t = ts.get(0);
    			if(t.doubleValue()<=0){
    				t = (BigDecimal) entityManager.createQuery("select t.tasa from Tasadolar t where " +
	    			"t.id.codigopais = '"+codpais+"' and " +
	    			"t.id.fecha = '"+fecha+"'").getSingleResult();  	
    			}
    		}else{
    		    //sino hay tasa negociada asigna la tasa de oficial para ese pais
    			t = (BigDecimal) entityManager.createQuery("select t.tasa from Tasadolar t where " +
    	    			"t.id.codigopais = '"+codpais+"' and " +
    	    			"t.id.fecha = '"+fecha+"'").getSingleResult();  	
    		}
    	
    	
    	return t;
    	}catch(Exception e){
    		System.out.println("Error al ubicar la tasa de dolar de una Transaccion");
    		return null;
    	}
    }
    
    /**
	 * Retorna la tasa de dolar para el detalle de las Tx en la interfaz TarjetaEdit.xhtml
	 * Este nuevo metodo trae la tasa de dolar, con base en el dolar liquidado en el momento
	 * de la transaccion, y no los de las tablas de dolar negociado y global.
	 * @param consecutivo
	 *
	 */
    public String tasaDolarNew(int consecutivo){
    	try{

    		BigDecimal t = BigDecimal.ZERO;
    		//1. Busca la Tx con el Nro. de consecutivo
    		Transaccion tx = (Transaccion) entityManager.createQuery(
    				"from Transaccion t where t.consecutivo = " + consecutivo).getSingleResult();
    		
    		//2. Con el objeto Transaccion obtengo el establecimiento > pais > moneda  
    		//   para determinar la operacion que retorna la tasa de cambio 
    		//   de Dolar o Euro
    		String tipoMoneda = tx.getEstablecimiento().getPais().getPaisiso().getCodigomoneda();
    		if("EUR".equals(tipoMoneda)){    			
    			t = tx.getValortxpesos().divide(tx.getValortxeuros(), 2, RoundingMode.HALF_UP);
    			return new DecimalFormat("€ #,###,##0").format(t);
    		}else{
    			if( "COP".equals(tipoMoneda)){
    				//cambiar por una consulta
    				
    				t = tx.getValortxpesos().divide(tx.getValortxdolares(), 2, RoundingMode.HALF_UP);
    				return new DecimalFormat("USD #,###,##0").format(t);
    			}else{
    				t = tx.getValortxpesos().divide(tx.getValortxdolares(), 2, RoundingMode.HALF_UP);
    				return new DecimalFormat("USD #,###,##0").format(t);
    			}
    		}    
    	}catch(Exception e){
    		System.out.println("Error al ubicar la tasa de cambio de una Transaccion: " + consecutivo);
    		
    		e.printStackTrace();
    		return null;
    	}
    }
    
    
    public void guardarTarjeta()
    {    	
    	//valida el banco antes de guardar la tarjeta, si eligen un banco errado
    	String tarjetabin = tarjetaHome.getInstance().getNumerotarjeta().substring( 0, 6 );    	
    	
    	List<String> bancobin = entityManager.createNativeQuery(
    			"select codbanco from bancobin where bin =" +
    			"'" + tarjetabin + "'").getResultList();
    	
    	String bancoEmisor = "";
    	    	
    	if( !bancobin.isEmpty() )
    	{
    		if( tarjetaHome.getInstance().getBanco().getCodbanco() != bancobin.get(0))
        	{
        		bancoEmisor =(String) bancobin.get(0);    		
        		Banco bnco = entityManager.find(Banco.class, bancoEmisor );
    			tarjetaHome.getInstance().setBanco(bnco);    		
        	}    		
    	}
    	
    	//graba el nuevo bin en la base
    	if( bancobin.isEmpty()){
    		String banco = tarjetaHome.getInstance().getBanco().getCodbanco();
    		int rows = entityManager.createNativeQuery(
    			"INSERT INTO bancobin( bin, codbanco) VALUES('"+ 
    			tarjetabin +"', '"+ banco +"')").executeUpdate();    		
    	}
    	
    	if(tarjetaHome.getInstance().getPromotor()!=null){
    	/*
    	if((tarjetaHome.getInstance().getPromotor().getPersonal().getNombre()
    			+" "+tarjetaHome.getInstance().getPromotor().getPersonal().getApellido()).contentEquals(this.nombre)){
    		
    	}else{
    		ubicarPromotor();
    		return;
    	}
    	*/
    	}else {
    		if(!this.nombre.contentEquals("")){    			
    			ubicarPromotor();
    		}else{
    			return;
    		}
    	}
    	tarjetaHome.getInstance().setCedulatarjetahabiente("");
    	
    	Expressions expressions = new Expressions();
    	
    	tarjetaHome.setCreatedMessage(expressions.createValueExpression("La tarjeta ha sido creada de forma correcta"));
    	
    	if(tarjetaHome.getInstance().getTac() == null){
    		tarjetaHome.getInstance().setTac(false);
    	}if(tarjetaHome.getInstance().getTac() == false){
    		tarjetaHome.getInstance().setTac(false);
    	}
    	
    	tarjetaHome.persist();
    	
    	AdministrarUsuario.auditarUsuario(2, "Se ha creado la tarjeta con numero " +
    			this.tarjetaHome.getInstance().getNumerotarjeta() + " ");

    	if(this.tarjetaHome.getInstance().getCedulatarjetahabiente()!=null){
    		
    		if(!this.viajeasignado && (
    					(this.viajeactual.getCupointernet()!=null)||
    					(this.viajeactual.getCupoinicialinternet()!=null && 
    					tarjetaHome.getInstance().getFechainicio()!=null && 
    					tarjetaHome.getInstance().getFechafin()!=null)) && 
    		   tarjetaHome.getInstance().getCedulatarjetahabiente() != null && 
    		!tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals(""))
    		{
    			this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
    			this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
    	
    			this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
    			this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
    	
    			this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
    			.getCedulatarjetahabiente());
    	
    			this.viajeactual.setFechamod(new Date());
    			this.viajeactual.setUsuariomod(identity.getUsername());
    	
    			BigInteger query = (BigInteger)entityManager.createNativeQuery( 
    					"select nextval('viaje_consecutivo_seq')").getSingleResult();
    			
    			viajeactual.setConsecutivo(query.intValue());    	
		
    			//ViajeHome vh = new ViajeHome();
    			//ajeHome.setInstance(this.viajeactual);
		
    			//viajeHome.persist();
    			entityManager.persist(this.viajeactual);
    	
    			//entityManager.flush();    	
    	
    			entityManager.flush();
    	
    			entityManager.createNativeQuery("insert into tarjetaviaje (estado, " +
    					"consecutivoviaje, numerotarjeta)	" + "values ('1', '" +
    					this.viajeactual.getConsecutivo()+"', '" + 
    					tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
    	
    	
    	//entityManager.clear();
    	//entityManager.persist(tv);
    	entityManager.flush();
    	facesMessages.add("El viaje se ha creado de forma correcta");
    	AdministrarUsuario.auditarUsuario(8, 
    			"Se ha creado el viaje "+query+" para la tarjeta " + 
    			this.tarjetaHome.getInstance().getNumerotarjeta() + " ");

    	
    	}else{
    		if(this.viajeasignado){
    			List s = entityManager.createQuery("select tv from Tarjetaviaje tv " +
            			"where tv.id.consecutivoviaje = "+this.viajeactual.getConsecutivo()+" " +
            			"and tv.id.numerotarjeta='"+tarjetaHome.getInstance().getNumerotarjeta()+"'").getResultList();
            	if (s.size()<=0){
            		entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
            			"values ('1', '"+this.viajeactual.getConsecutivo()+"', '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
            		facesMessages.add("Se ha asignado un viaje ya creado a la tarjeta");
            		AdministrarUsuario.auditarUsuario(14, "Se ha asignado un viaje para la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + " ");

            	}
    			
    		}
    	}
    	
    	
    	/*
    	Tarjetaviaje tv = new Tarjetaviaje();
    	tv.setTarjeta(tarjetaHome.getInstance());
    	tv.setViaje(viajeactual);
    	tv.setId(new TarjetaviajeId(tarjetaHome.getInstance().getNumerotarjeta(), 
    			this.viajeactual.getConsecutivo()));
    	tv.setEstado(true);
    	*/
    	
    	/* Se realiza una insercion en codigo nativo para evitar
    	 *  un problema de Caused by javax.persistence.PersistenceException 
    	 *  with message: "org.hibernate.exception.SQLGrammarException:
    	 *  Could not execute JDBC batch update" 
		   */
    	
    	
    	tarjetaHome.clearInstance();
    	promotorHome.clearInstance();
    	this.nombre = "";
    	}else{
    		facesMessages.add("El viaje no ha podido ser creado por la falta del documento del tarjetahabiente");
    	}
    }
    
    public String observacion = "";
    
    public String getObservacion() {
		return observacion;
	}
    
    

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	
	
	public String actualizarTarjeta(){
		entityManager.clear();

    	//valida que el nombre del promotor no este vacio
		if(!this.nombre.contentEquals("")){
			ubicarPromotor();
		}else{
			facesMessages.add("Ha ocurrido un error al intentar ubicar el promotor ");
			return null;
		}

    		
		log.info("Actualizacion de Promotor"+promotorHome.getInstance().getDocumento());
    	
    	//actualizo el promotor
    	entityManager.merge(tarjetaHome.getInstance());
    	entityManager.flush();
        	
        AdministrarUsuario.auditarUsuario(3, 
        		"Se ha actualizado la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + "");
        	
        System.out.println("Cedula Tarjeta Habiente " + this.tarjetaHome.getInstance().getCedulatarjetahabiente());
        	
        if(this.tarjetaHome.getInstance().getCedulatarjetahabiente()!=null){
        	
        	System.out.println("0");
        	
        	if(this.viajeactual.getConsecutivo()>0){//valida si existe un viaje actual
        		
        		/* Permite cambiar la fecha de inicio y feha final del viaje que
        		 * se encuentra cargado.  En viajeactual se encuentra cargado el viaje
        		 * de la persona con la misma cedula
        		 */
        		 //if(this.)
        		System.out.println("1");
        		
        		this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
            	this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
            	
            	if(this.viajeactual.getCupointernet()==null){
            		this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
            	}
            	if(this.viajeactual.getCupoviajero()==null){
            		this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
            	}
            	
            	/*
            	this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
            			.getCedulatarjetahabiente());
            	*/
            	
            	this.viajeactual.setFechamod(new Date());
            	this.viajeactual.setUsuariomod(identity.getUsername());
            	
            	entityManager.merge(this.viajeactual);            	
            	
            	entityManager.flush();            	
            	
            	
            	AdministrarUsuario.auditarUsuario(9, "Se ha actualizado el viaje " + this.viajeactual.getConsecutivo() + "");

            	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
            	
            	entityManager.createNativeQuery("update public.tarjeta set " +
            			"fechainicio = '"+sdf.format(this.viajeactual.getFechainicio())+"', " +
            			"fechafin = '"+sdf.format(this.viajeactual.getFechafin())+"' where " +
            			"public.tarjeta.numerotarjeta in (SELECT " +
            			"public.tarjeta.numerotarjeta " +
            			"FROM " +
            			"public.tarjetaviaje " + 
            			"INNER JOIN " +
            			"public.tarjeta " + 
            			"ON public.tarjetaviaje.numerotarjeta = public.tarjeta.numerotarjeta " + 
            			"INNER JOIN " +
            			"public.viaje " + 
            			"ON public.tarjetaviaje.consecutivoviaje = public.viaje.consecutivo " + 
            		"WHERE public.viaje.consecutivo = "+this.viajeactual.getConsecutivo()+" ) ").executeUpdate();
            	
            	/* Se debe borrar el viaje actual asociado a la tarjeta si se va a cambiar
            	 * 
            	 */
            	entityManager.createNativeQuery("delete from Tarjetaviaje tv " +
            			"where (tv.numerotarjeta, tv.consecutivoviaje) in " +
            			"(select tv.numerotarjeta, tv.consecutivoviaje " +
            			"from Tarjetaviaje tv, Tarjeta t, Viaje v " +
            			"where tv.numerotarjeta = t.numerotarjeta and " +
            			"tv.consecutivoviaje = v.consecutivo and " +
            			"t.numerotarjeta='"+tarjetaHome.getInstance().getNumerotarjeta()+"' " +
            			"and v.fechainicio = t.fechainicio)").executeUpdate();
            	
            	/* Se registra la informacion del viaje que esta cargado*/
            	List s = entityManager.createQuery("select tv from Tarjetaviaje tv " +
            			"where tv.id.consecutivoviaje = "+this.viajeactual.getConsecutivo()+" " +
            			"and tv.id.numerotarjeta='"+tarjetaHome.getInstance().getNumerotarjeta()+"'").getResultList();
            	if (s.size()<=0){
            		System.out.println("3");
            		entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
            			"values ('1', '"+this.viajeactual.getConsecutivo()+"', '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
            		facesMessages.add("Se ha actualizado la informacion de la tarjeta y se ha asociado la tarjeta a " +
            				"un viaje existente");
            		AdministrarUsuario.auditarUsuario(14, "Se ha asignado el viaje a la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + "");
            	}else{
            		System.out.println("4");
            		facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
            				"asignado");
            	}            	
        	}else{
        		System.out.println("5");
        		if (tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals("")){
        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
    				"creado porque cedulatarjetahabiente es vacio");
        			return null;
        		}else if (tarjetaHome.getInstance().getCedulatarjetahabiente() == null){
        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
    				"creado porque cedulatarjetahabiente es nulo");
        			return null;
        		}else if(this.viajeasignado) {
        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
    				"creado porque el viaje esta asignado");
        			System.out.println("6");
        			System.out.println("viajeasignado en actualizacion");
        			return null;
        		}else if(!((this.viajeactual.getCupoinicialinternet()!=null) 
            			|| (this.viajeactual.getCupoinicialviajero()!=null && 
            					tarjetaHome.getInstance().getFechainicio()!=null && 
            					tarjetaHome.getInstance().getFechafin()!=null))) {
        			System.out.println("7");
        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
    				"creado porque hay problema con el cupo inicial ");
        			System.out.println("Cupo inicial Internet "+this.viajeactual.getCupoinicialinternet());
        			System.out.println("Cupo inicial Viajero "+this.viajeactual.getCupoinicialviajero());
        			System.out.println("Fecha Inicio "+tarjetaHome.getInstance().getFechainicio());
        			System.out.println("Fecha Fin "+tarjetaHome.getInstance().getFechafin());
        			return null;
        		}
        				
        				
        		System.out.println("8");	
        		
        		//se crea el nuevo viaje
        		if(!this.viajeasignado ){
        			
        			if( tarjetaHome.getInstance().getCedulatarjetahabiente() != null && 
            			!tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals("")){//valida que tenga nro de cedula
        				
        				//valida que exista tipo del cupo inicial fecha inicio y fin para crear el viaje	
        				if((this.viajeactual.getCupoinicialinternet()!=null && 
	            				tarjetaHome.getInstance().getFechainicio()!=null && 
	            				tarjetaHome.getInstance().getFechafin()!=null) ||  
			            		(this.viajeactual.getCupoinicialviajero()!=null && 
			            		 tarjetaHome.getInstance().getFechainicio()!=null && 
			            		 tarjetaHome.getInstance().getFechafin()!=null)){
			        			
				        		this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
				            	this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
				            	
				            	this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
				            	this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
				            	
				            	this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
				            			.getCedulatarjetahabiente().trim() );//se adicion el trim para eliminar espacios al final de la cc
				            	
				            	
				            	BigInteger query = (BigInteger)entityManager
				        		.createNativeQuery("select nextval('viaje_consecutivo_seq')").getSingleResult();
				        		viajeactual.setConsecutivo(query.intValue());
				        		
				        		this.viajeactual.setFechamod(new Date());
				            	this.viajeactual.setUsuariomod(identity.getUsername());
				        		
				        		entityManager.persist(this.viajeactual);
				        		
				        		entityManager.flush();
				        		
				        		AdministrarUsuario.auditarUsuario(8, "Se ha creado un viaje "+query+" y asignado a la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + "");
				        		entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
				            			"values ('1', '"+this.viajeactual.getConsecutivo()+"', '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
				        		
				        		entityManager.flush();
				        		facesMessages.add("Se ha actualizado la informacion de la tarjeta y se ha creado " +
				        				"un nuevo viaje para este tarjetahabiente");
			        		}else{
			        			System.out.println("Errror");
			        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
			            				"creado Cupo Internet (" + this.viajeactual.getCupoinicialinternet()+"), Cupo Viajero (" + this.viajeactual.getCupoinicialviajero()+"), " +
			            						"Fecha Inicio (" + tarjetaHome.getInstance().getFechainicio()+"), Fecha Fin (" + tarjetaHome.getInstance().getFechafin()+")");
			        			return null;
			        		}
        			}else{
        				
        				System.out.println("Errror");
	        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
	            				"creado porque el numero de cedula del tarjetahabiente esta vacio");
        			}
		        		}else{
		        			System.out.println("Errror");
		        			facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
		            				"creado porque el viaje se encontraba asignado");
		        		}
			            	
			        	}
        	}else{
        		facesMessages.add("Se ha actualizado la informacion de la tarjeta pero el viaje no ha podido ser " +
				"creado porque no hay cedula de tarjetahabiente");
        		return null;
        	}
        	/*
        	if(!this.viajeasignado){
        	this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
        	this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
        	
        	this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
        	this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
        	
        	this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
        			.getCedulatarjetahabiente());
        	
        	
        	BigInteger query = (BigInteger)entityManager
    		.createNativeQuery("select nextval('viaje_consecutivo_seq')").getSingleResult();
    		viajeactual.setConsecutivo(query.intValue());
        	
    		
    		//ViajeHome vh = new ViajeHome();
    		//viajeHome.setInstance(this.viajeactual);
    		
    		//viajeHome.persist();
        	entityManager.persist(this.viajeactual);
        	//entityManager.flush();
        	
        	
        	
        	}
        	*/
        	//entityManager.flush();
        	/*
        	Tarjetaviaje tv = new Tarjetaviaje();
        	tv.setTarjeta(tarjetaHome.getInstance());
        	tv.setViaje(viajeactual);
        	tv.setId(new TarjetaviajeId(tarjetaHome.getInstance().getNumerotarjeta(), 
        			this.viajeactual.getConsecutivo()));
        	tv.setEstado(true);
        	*/
        	
        	/* Se realiza una insercion en codigo nativo para evitar un problema de 
    		   Caused by javax.persistence.PersistenceException with message: "org.hibernate.exception.SQLGrammarException: 
    		   Could not execute JDBC batch update" 
    		   */
        	
        	
        	//entityManager.clear();
        	//entityManager.persist(tv);
        	this.viajeasignado = false;
        	this.viajeactual = null;
        	entityManager.flush();
        	
        	tarjetaHome.clearInstance();
        	promotorHome.clearInstance();
        	this.nombre = "";
    	return "updated";
		
    	 
    }
    
    Boolean viajeasignado = false;
    
    public Boolean getViajeasignado() {
		return viajeasignado;
	}

	public void setViajeasignado(Boolean viajeasignado) {
		this.viajeasignado = viajeasignado;
	}

	public void buscarTarjetahabiente()
	{
		viajeasignado = false;
    	List<Tarjeta> ta = 
    		entityManager.createQuery("select t from Tarjeta t where " +
				"t.cedulatarjetahabiente = '"+tarjetaHome.getInstance().getCedulatarjetahabiente()+"'")
		.getResultList();
    	
    	if (ta.size()>0){
    		Tarjeta t = ta.get(0);
    		tarjetaHome.getInstance().setDirecciontarjetahabiente(t.getDirecciontarjetahabiente());
    		tarjetaHome.getInstance().setTelefonotarjetahabiente(t.getTelefonotarjetahabiente());
    		tarjetaHome.getInstance().setTarjetahabiente(t.getTarjetahabiente());
    		System.out.println("BuscAr Tarjetahabiente");
    		List<Viaje> v = entityManager
    		.createQuery("select v from Viaje v where " +
    				"v.cedulatarjetahabiente = '"+ tarjetaHome.getInstance().getCedulatarjetahabiente()+"' " +
    						"order by v.fechafin desc")
    		.getResultList();
    		if(v.size()>0){
    			this.viajeactual = v.get(0);
    			tarjetaHome.getInstance().setFechainicio(viajeactual.getFechainicio());
    			tarjetaHome.getInstance().setFechafin(viajeactual.getFechafin());
    			
    			viajeasignado = true;
    		}
    	}else{
    		//para grabar el tarjetahabiente desde el CNE
    	}
    }
	
	
	public String buscarTarjetahabientCne( String nombre)
	{
		return "";
	}
    
	
 
    public Boolean existeTarjetahabiente()
    {
    	System.out.println("Revision Tarjetahabiente");
    	System.out.println("Cupo inicial Internet "+this.viajeactual.getCupoinicialinternet());
		System.out.println("Cupo inicial Viajero "+this.viajeactual.getCupoinicialviajero());
		System.out.println("Fecha Inicio "+tarjetaHome.getInstance().getFechainicio());
		System.out.println("Fecha Fin "+tarjetaHome.getInstance().getFechafin());
    	
    	if ( tarjetaHome.getInstance().getCedulatarjetahabiente()!=null && 
    		!tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals(""))
    	{    		
    		List<Viaje> ta = entityManager
    		.createQuery("select v from Viaje v where " +
    				"v.cedulatarjetahabiente = '"+tarjetaHome.getInstance().getCedulatarjetahabiente()+"'")
    		.getResultList();
    	
    	if (ta.size()>0){
    		
    		return true;
    		}
    	}//fin del if externo
    	
    	return false;
    }
    
    
    
    @In(create=true)
	private AdministrarVariable AdministrarVariable;
    
    @In Identity identity;
    
    public void llenarPromotores(){
		entityManager.clear();
		String sql = "";
		
		if(identity.hasRole("Asesor")){
			sql = " where p.asesor.documento = '" + identity.getUsername()+"'";
		}
		List<String> resultList = entityManager.createQuery("select " +
				"p.personal.nombre||' '||p.personal.apellido from Promotor p "+ 
				sql).getResultList();
		lista = resultList;
	}
        
    /**
     * @author Luis Fernando Ortiz 
     * Busqueda de nombres por medio de expresiones regulares.
     * @param nom
     * @return
     */
    public List<String> autocompletar(Object nom) {
		llenarPromotores(); 							// Metodo que carga la informacion de los nombres de las personas
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
		for (String promo : lista) {			
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


    
/*    public List<String> autocompletar(Object nombre) {
    	llenarPromotores(); 							// Metodo que carga la informacion de los nombres de las personas
    	String pref = (String) nombre;
    	ArrayList<String> result = new ArrayList<String>();
    	Iterator<String> iterator = lista.iterator();
    	while (iterator.hasNext()) {
    		String elem = ((String) iterator.next());
    		if ((elem != null && elem.toLowerCase().contains(pref.toLowerCase()))
    				|| "".equals(pref)) {
    			result.add(elem);
    		}
    	}
    	return result;
    }
    
*/    
    private String nombre = "";

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	@In(create=true)
	private CargarObjetos CargarObjetos;
    
	public void ubicarPromotor(){
		
		Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
		
		if(pr!=null){
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			
			tarjetaHome.getInstance().setPromotor(pr);
		}
		
		/*
		entityManager.clear();
		List<Promotor> p = (ArrayList)entityManager
		.createQuery("select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"+this.nombre+"'").getResultList();
		
		if (p.size() > 0) {
			Promotor pr = (Promotor)entityManager
			.createQuery("select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"+this.nombre+"'")
			.getSingleResult();
			
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			
			tarjetaHome.getInstance().setPromotor(pr);
		}
		
		*/
		
		
	}
	
	String promotoractual = null;
	
	
	
	public String getPromotoractual() {
		return promotoractual;
	}

	public void setPromotoractual(String promotoractual) {
		this.promotoractual = promotoractual;
	}
	
	@Begin(join=true)
	public void solicitarCambioPromotor(String numerotarjeta){
		llenarPromotores();
		this.nombre = "";
		tarjetaHome.setTarjetaNumerotarjeta(numerotarjeta);
		this.promotoractual = tarjetaHome.getInstance().getPromotor()
		.getPersonal().getNombre() + " " + tarjetaHome.getInstance().getPromotor()
		.getPersonal().getApellido();
		
	}
	
	
	public Boolean trasladar = false;
	
	public Boolean getTrasladar() {
		return trasladar;
	}

	public void setTrasladar(Boolean trasladar) {
		this.trasladar = trasladar;
	}

	public void cambiarPromotor(){
		try
		{
			System.out.println("Inicio de Cambio de Promotor");
			
			//actualiza el promotor en la tabla tarjeta
			String sql = "update public.tarjeta " +
					"set documento = '"+promotorHome.getPromotorDocumento()+"' where " +
					" numerotarjeta = '"+tarjetaHome.getTarjetaNumerotarjeta()+"'";
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();
			
			
			String arrastradortxt = "";
			BigDecimal comisionarrastrador = BigDecimal.ZERO;
			
			//valor del checkBox para trasaladar Tx y Depositos
			if(this.trasladar){				
				//se asigna el % del arrastrador
				if(promotorHome.getInstance().getArrastrador()
					.getDocumento()!=null){
					arrastradortxt = promotorHome.getInstance().getArrastrador().getDocumento();
					if(promotorHome.getInstance().getComisionarrastrador() != null){
						comisionarrastrador = promotorHome.getInstance()
								.getComisionarrastrador().divide(new BigDecimal("100"));
				}
			}		
			
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");    	
			
			//sentencia de actualizacion de Tx el documento del promo y arrastrador
			//y la comision de este ultimo en la tabla transaccion
			sql = "update public.transaccion " +
			"set promotor = '" + promotorHome.getPromotorDocumento() + "', " +
			"arrastrador = '" + arrastradortxt + "', " +
			"arrastradorcomision = round(valortxpesos*(" + comisionarrastrador + "),0) " +
			"where " +
			" numerotarjeta = '"+tarjetaHome.getTarjetaNumerotarjeta()+"' " +
			"and fechatx between '"+sdf.format(tarjetaHome.getInstance().getFechainicio())+"' and " +
					" '" + sdf.format(tarjetaHome.getInstance().getFechafin()) + "' " +
					" ";
			//actualizo la Tx 
			entityManager.createNativeQuery(sql).executeUpdate();
			
			//sentencia para actualizar Depositos
			sql = "update public.depositostarjeta " +
			"set promotor = '" + promotorHome.getPromotorDocumento() + "' " +
			"where " +
			" numerotarjeta = '"+tarjetaHome.getTarjetaNumerotarjeta()+"' " +
			" and fecha between '"+sdf.format(tarjetaHome.getInstance().getFechainicio())+"' and " +
			" '" + sdf.format(tarjetaHome.getInstance().getFechafin()) + "' " +
					" ";
			//actualiza los Depositos
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();
			
			//sentencia para actualizar la Comision del promotor de la Tx			
			sql = "update public.transaccion " +
			"set valorcomision = round(valortxpesos*(" + (new BigDecimal("100"))
			.subtract(promotorHome.getInstance().getComisionviajero()).divide((new BigDecimal("100")))+ "),0) " +
			"where " +
			"numerotarjeta = '"+tarjetaHome.getTarjetaNumerotarjeta()+"' and " +
			"codigounico in (select codigounico from establecimiento where codpais = 'CO') " +
			"and fechatx between '"+sdf.format(tarjetaHome.getInstance().getFechainicio())+"' and " +
			" '" + sdf.format(tarjetaHome.getInstance().getFechafin()) + "' ";			
			//actualiza la comision del promotor de la Tx 
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();
			
			//registro en Auditoria 
			AdministrarUsuario.auditarUsuario(15, "Se ha cambiado la tarjeta " + 
					this.tarjetaHome.getInstance().getNumerotarjeta() + 
					" de promotor y se trasladaron las transacciones");
			}else{
				//si no se trasladan Tx ni Depositos Audito
				AdministrarUsuario.auditarUsuario(15, "Se ha cambiado la tarjeta " +
						this.tarjetaHome.getInstance().getNumerotarjeta() + 
						" de promotor y no se trasladaron las transacciones");
			}//fin del else	
			
		}catch(Exception e)
		{
			facesMessages.add(
			"No se puede hacer el cambio de promotor, revise que el promotor tenga Arrastrador");			
			//e.printStackTrace();
		}		
	}//fin del metodo cambiarPromotor
	
	
	
	public void validarTarjeta(String numerotarjeta)
	{
		long t1 = System.currentTimeMillis();
		log.info("Validacion de tarjeta numero " + numerotarjeta);
		
		//revisa que la tarjeta no exista en la BD
		List<Tarjeta> ta = entityManager
		.createQuery("select t from Tarjeta t where " +
				"t.numerotarjeta = '"+numerotarjeta+"'").getResultList();
    	
		if (ta.size()>0)
		{
    		facesMessages.addToControl("numerotarjeta", 
    				"Este numero de tarjeta ya se encuentra registrada!");
    		valida = false;
    		return;
    	}else{   
    		// Aca comienza el proceso de Validacion de la tarjeta
    		ValidarTarjeta validarTarjeta ;
    		validarTarjeta  = new ValidarTarjeta(numerotarjeta); 
    		
			if(validarTarjeta.getTarjeta()!= null)
			{	
				
				if(validarTarjeta.getFranquicia()!= null)
				{
					Franquicia f = entityManager.find(Franquicia.class,
							validarTarjeta.getFranquicia());
					//se establece la franquicia y el numro de la tarjeta
					tarjetaHome.getInstance().setFranquicia(f);
					tarjetaHome.getInstance().setNumerotarjeta(validarTarjeta.getTarjeta());
					
					//Obtiene el bin de la tarjeta
					String tarjetabin = validarTarjeta.getTarjeta().substring( 0, 6 );
			    	//Busca el bin en la base de bines de Moneda Frontera
					List<String> bin = entityManager.createQuery("SELECT bcoBin.codbanco FROM " +
							"Bancobin bcoBin WHERE bcoBin.bin = '" + tarjetabin+"'" ).getResultList();
			    			    	
			    	String bancoEmisor = "";	
			    	
			    	if( bin.size()>0){			    		
			    		bancoEmisor =bin.get(0);			    		
			    		// si el bin existe se establece el banco Emisor de la tarjeta.
			    		Banco bnco = entityManager.find(Banco.class, bancoEmisor );
						tarjetaHome.getInstance().setBanco(bnco);	
						
			    	}else{
			    		//sino, se hace la busqueda del bin en la tabla de bines de seguridad
			    		List< Bindb > binAlerta = entityManager.createQuery("SELECT bindb FROM " +
			    				"Bindb bindb WHERE bindb.bin = '" + tarjetabin + "'" ).getResultList();
			    	 
			    		BinJson datosBin = null; //varibale usada para almacenar busqueda Web Services
			    		
			    		//Alerta de seguridad si el banco no es Venezolano
			    		if( binAlerta.isEmpty() || 
			    				!binAlerta.get(0).getNombrebanco().equals("Venezuela")) 
			    		{
			    			String paisBancoEmisor = "";
			    			
			    			if( !binAlerta.isEmpty() ){//Establece el nombre del emisor y pais de emisor
			    				paisBancoEmisor = binAlerta.get(0).getPais().trim().equals("Nd") ?
				    					"Indeterminado" : binAlerta.get(0).getPais().toUpperCase();
			    				bancoEmisor = binAlerta.get(0).getNombrebanco();
			    			}
			    			//Sino se encuentra el pais en la base de Moneda, se hace una consulta 
			    			//externa  por medio de Web Services (Rest Api - JSon) a BinList.net
			    			if( paisBancoEmisor.equals("Indeterminado") || paisBancoEmisor.equals("")){
			    				
			    				datosBin = getBinJson( tarjetabin );//aca se hace la consulta por Rest
			    				
			    				if( datosBin.getCountry_name() != null ){
			    					paisBancoEmisor = datosBin.getCountry_name().toUpperCase();	
			    					bancoEmisor = datosBin.getBank().toUpperCase();
			    				}
			    			}
			    			//si el pais no es Venezuela
			    			if( !paisBancoEmisor.equals("VENEZUELA") )
			    			{
			    				String franquiciaT = f.getNombrefranquicia();
				    			facesMessages.addToControl("numerotarjeta", 
								"ALERTA DE SEGURIDAD !, Pais origen: " + paisBancoEmisor);				    			
				    			 
				    			//graba en auditoria de usuario esta alerta.
				    			AdministrarUsuario.auditarUsuario(27, 
				    			"Alerta de seguridad al registrar la Tarjeta " + 
				    			this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
				    			"Pais origen de la tarjeta " + paisBancoEmisor 
				    			+" Banco Emisor " + bancoEmisor );
				    			
				    			String usuario = (String) entityManager.createQuery("select " +
				    					"u.nombre from Usuario u where " +
				    					"u.usuario = '" + identity.getUsername() +"'").getSingleResult();
				    			//se envia un correo de alerta
				    			this.enviarMailAlertas.enviarEmailAlertaHTMLV2( usuario,
				    					this.tarjetaHome.getInstance().getNumerotarjeta(),
				    					paisBancoEmisor, bancoEmisor, franquiciaT  );
			    			}
			    			//grabar nuevo bin desde BinList.net
			    			grabarNuevoBin( datosBin );//falta implementar este metodo
			    			//y se eliminaria el siguiente mensaje
			    			facesMessages.addToControl("numerotarjeta", 
							"Este numero de tarjeta corresponde a un Banco Venezolano, " +
							"pero no se encuentra en nuestra base.");
			    						    			 																				    			
			    		}else{
			    			facesMessages.addToControl("numerotarjeta", 
									"Alerta! Bin no encontrado Revise el Banco Emisor");
			    		}//fin del else interno				    	
			    	}			    		
					valida = true;				
				}else{
					
					//Modificado para las tarjetas debito
					if( !validarTarjeta.getTarjeta().substring(0, 1).equals("4")  && 
							!validarTarjeta.getTarjeta().substring(0, 1).equals("5") &&
								!validarTarjeta.getTarjeta().substring(0, 1).equals("8")){						
						
						System.out.println("IF sin franquiquicia---->>>	");
						
						valida = true;
						facesMessages.addToControl("numerotarjeta", 
								"No valido como tarjeta Credito. Valide que sea una Tarjeta Debito");
						
					}
					
				}//fin del if validar franquicia
			}else{
				
				valida = false;
				facesMessages.addToControl("numerotarjeta", 
						"Este numero no es valido para una tarjeta de credito");
			} 
    	}//fin del else externo
		
		System.out.println("Tiempo Total de Validacion: " + 
				(System.currentTimeMillis() - t1) + "ms.");
	}
	
	
	/**
	 * Devuelve un objeto BinJson que contiene toda la informacion del bin
	 * de la tarjeta, por medio de Web Sevices de BinList.net.<br/> 
	 * Para este tipo de Deserializacion no es necesario
	 * crear una implementacion de {@link Deserializer}
	 * ya que el objeto en Java contiene los mismos nombre de atributos que 
	 * el objeto JSon.
	 * Url BinList.net: http://www.binlist.net/json/{bin}
	 * @param binTc bin de la tarjeta a consultar
	 * @return BinJson 
	 * 
	 */
	public BinJson getBinJson( String binTc)
	{
		try {
			//url de BinList.net que recibe el bin a consultar
			String uri = "http://www.binlist.net/json/" + binTc;
			URL url = new URL(uri);
			URLConnection uc = url.openConnection();
			
			//propiedad del cabecero necesaria para obtner una respuesta legible
			//del flugo
			uc.addRequestProperty("User-Agent", 
	        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
	        uc.connect();
	        
	        //Lectura del Json 
	        BufferedReader  reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
			
            //Deserializacion del objeto JSon por medio de la api 
            //de google code.google.com/p/google-gson/
            Gson gson2 = new GsonBuilder().create();
			BinJson bin = gson2.fromJson(buffer.toString(), BinJson.class);
			System.out.println(bin);//imprime en la consola los datos
			return bin;
		
		} catch (Exception e) {
			System.out.println(":-(");
			e.printStackTrace();
		}
		
		return null;
	}
	

	public void grabarNuevoBin( BinJson binJson)
	{
		//aca se podria retornar un valor booleano para 
		//informar que se grabo el nuevo bin y que se puede
		//establecer el banco emisor de la tarjeta		
		
	}
	
	
	/**
	 * Busca la cedula en la base de datos de cne y carga el viaje de la tarjeta
	 * si la misma se activo en la oficina 
	 * @param cedula
	 */
	public void buscarTarjetahabienteCne( String cedula)
	{
		if( !cedula.equals("") && !cedula.equals(" ")  && !cedula.equals("  ") &&
				cedula!= null )
		{
			try
			{
				StringBuilder nombre = new StringBuilder();	
				
				List< String >tarjetahabiente =  entityManager.createNativeQuery( "SELECT "+
		               " cne.primer_nombre||' '||cne.segundo_nombre||' '||cne.primer_apellido||' '||" +
		               "cne.segundo_apellido FROM  cne  WHERE cne.cedula = '" + cedula.trim() + "'").getResultList();							
				
				if( !tarjetahabiente.isEmpty() ){
					
					StringTokenizer token = new StringTokenizer( tarjetahabiente.get(0) );
					
					while( token.hasMoreTokens()){
						String palabra = token.nextToken();
						if( !palabra.equals("") || !palabra.equals(" "))
							nombre.append(palabra).append(" ");							
					}								
					//registro el nombre en el componente jsf tarjetahabiente
					tarjetaHabienteAMayuscula(nombre.toString().trim());
					
					List< String >codDir =  entityManager.createNativeQuery( "SELECT "+
				               " cne.cod_centro FROM  cne  WHERE cne.cedula = '" + cedula.trim() + "'").getResultList();
					
					List< String >dir =  entityManager.createNativeQuery( "SELECT "+
							" cnedireccion.direccion_centro FROM  cnedireccion  WHERE cnedireccion.cod_centro = '" +
							codDir.get(0) + "'").getResultList();		
					
					//reduzco el tamanio de la direccion y registro la dir en el componente jsf
					String direccionFinal = dir.get(0).length() > 150 ? 
							dir.get(0).substring(0, 149).trim() : dir.get(0);
							
					direccionAMayuscula( direccionFinal );
				}//fin del if
					//trae la fecha del viaje si la tarjeta se activo en la oficina
					cargarFechaViaje( cedula );			
					
			}catch(Exception e){	
				System.out.println("Se genero una Excepcion al cargar los datos de la " +
						"cedula con el viaje y el CNE");
				e.printStackTrace();
				
				tarjetaHome.getInstance().setCedulatarjetahabiente("");				
			}
						
		}				
	}
	
	
	
	public void cargarFechaViaje( String cedula)
	{
		List< Activacion > act = entityManager.createQuery("select a from Activacion a where" +
				" a.cedula = '" + cedula + "' and " +
						" EXTRACT( YEAR FROM a.fechaInicioViaje)  =  EXTRACT( YEAR FROM CURRENT_DATE)").getResultList();
		if( !act.isEmpty())
		{
			tarjetaHome.getInstance().setFechainicio(act.get(0).getFechaInicioViaje());
			tarjetaHome.getInstance().setFechafin(act.get(0).getFechaFinViaje());	
			
			Integer cupoAprobado =  act.get(0).getCupoaprobado();
			
			if(cupoAprobado != null && cupoAprobado != 0){
				this.viajeactual.setCupoinicialviajero(act.get(0).getCupoaprobado());
			}
		}			
				
	}
	
	
	public void validarDepositosViaje() {
		
	}
	
	/* pasa a Mayuscula la primera letra del nombre del Tarjetahabiente 
	 * y elimina espacios en blanco, y establece el nombre
	 * en el componente
	 */ 
	public void tarjetaHabienteAMayuscula( String nombre )
	{
		log.info("Edicion tarejtahabiente " + nombre);
		String nombreTmp =  ExpresionesRegulares.eliminarEspacios(nombre, true);		
		tarjetaHome.getInstance().setTarjetahabiente(nombreTmp);
	}
	
	
	
	public void direccionAMayuscula( String direccion )
	{		
		String direcciontemp=  ExpresionesRegulares.eliminarEspacios(direccion, true);		
		tarjetaHome.getInstance().setDirecciontarjetahabiente(direcciontemp);
	}
	

	
	public List<Transaccion> getTviajero() {
		return tviajero;
	}

	public void setTviajero(List<Transaccion> tviajero) {
		this.tviajero = tviajero;
	}

	public List<Transaccion> getTinternet() {
		return tinternet;
	}

	public void setTinternet(List<Transaccion> tinternet) {
		this.tinternet = tinternet;
	}
    
    public void cargarViaje(String numerotarjeta){
		//entityManager.clear();
		List<Object> resultList = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
				"where tv.tarjeta.numerotarjeta = '"+numerotarjeta+"' and tv.viaje.consecutivo = v.consecutivo " +
						"" +
						"order by fechafin desc").getResultList();
		
		if(resultList.size()>0){
			this.viajeactual = (Viaje) resultList.get(0);
		}		
	}
    
    public void cargarComisiones(){
    	//entityManager.clear();
		List<Porcentajecomisiontx> resultList = 
			/*
			entityManager.createQuery("select c from Porcentajecomisiontx c " +
				"where c.fechafin is null and (substr(c.id.codpais,1,1) <> 'P' or " +
				"c.id.codpais = 'PA' )").getResultList();
			*/
			
			entityManager.createQuery("select c from Porcentajecomisiontx c " +
					"where c.fechafin is null and c.id.codpais in ('PA','ES','CO','W') )").getResultList();
		
		if(resultList.size()>0){
			listaporc = resultList;
			
			//Promotor p = promotorHome.getInstance()
			
			Porcentajecomisiontx pc = new Porcentajecomisiontx();
			pc.setPorcentaje(tarjetaHome.getInstance().getPromotor().getComisionviajero());
			
			listaporc.add(pc);
		}
    }
    
    long dias = 0;
    
    BigDecimal totalDepositosPesos = new BigDecimal(0);
    BigDecimal totalDepositosBolivares = new BigDecimal(0);
    BigDecimal totalDepositosBolivaresActual = new BigDecimal(0);
    BigDecimal totalTransaccionPesos = new BigDecimal(0);
    BigDecimal totalComisionPesos = new BigDecimal(0);
    BigDecimal totalTransaccionDolares = new BigDecimal(0);
    BigDecimal totalTransaccionPesosInt = new BigDecimal(0);
    BigDecimal totalComisionPesosInt = new BigDecimal(0);
    BigDecimal totalTransaccionDolaresInt = new BigDecimal(0);
    
    BigDecimal totalComisiones = new BigDecimal(0);
    BigDecimal totalTransacciones = new BigDecimal(0);
    BigDecimal totalConDeducciones = new BigDecimal(0);
    
    public void cargarTotalesDepositos(String numerotarjeta){
    	//entityManager.clear();
    	totalDepositosPesos = (BigDecimal) entityManager
		.createQuery("select sum(d.depositopesos) " +
				"from Depositostarjeta d " +
				"where d.fecha between d.tarjeta.fechainicio and d.tarjeta.fechafin and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosPesos==null)
			totalDepositosPesos = BigDecimal.ZERO;
		
		totalDepositosBolivaresActual = (BigDecimal) entityManager.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.fecha between d.tarjeta.fechainicio and d.tarjeta.fechafin and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		System.out.println("TOTAL DEPOSITOS ESTA TARJETA>> " + totalDepositosBolivaresActual);
		if(totalDepositosBolivaresActual==null)
			totalDepositosBolivaresActual = BigDecimal.ZERO;
		
		totalTransaccionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionPesos==null)
			totalTransaccionPesos = BigDecimal.ZERO;
		
		totalTransaccionDolares = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where  t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionDolares==null)
			totalTransaccionDolares = BigDecimal.ZERO;
    }
    
    
    
    public BigDecimal getTotalDepositosBolivaresActual() {
		return totalDepositosBolivaresActual;
	}

	public void setTotalDepositosBolivaresActual(
			BigDecimal totalDepositosBolivaresActual) {
		this.totalDepositosBolivaresActual = totalDepositosBolivaresActual;
	}

	public BigDecimal saldodepoviajero;
    public BigDecimal saldodepointernet;
    
    public BigDecimal getSaldodepoviajero() {
		return saldodepoviajero;
	}

	public void setSaldodepoviajero(BigDecimal saldodepoviajero) {
		this.saldodepoviajero = saldodepoviajero;
	}

	public BigDecimal getSaldodepointernet() {
		return saldodepointernet;
	}

	public void setSaldodepointernet(BigDecimal saldodepointernet) {
		this.saldodepointernet = saldodepointernet;
	}

	public void cargarTotales(String numerotarjeta){
    	//entityManager.clear();
		totalDepositosPesos = (BigDecimal) entityManager
		.createQuery("select sum(d.depositopesos) " +
				"from Depositostarjeta d " +
				"where d.fecha between d.tarjeta.fechainicio and d.tarjeta.fechafin and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosPesos==null)
			totalDepositosPesos = BigDecimal.ZERO;
		//total depositos en Bolivares por viaje
		totalDepositosBolivares = (BigDecimal) entityManager.createNativeQuery(
				"SELECT sum(public.depositostarjeta.valordeposito)" +
				"FROM public.depositostarjeta " +  
				"INNER JOIN public.tarjeta ON (public.depositostarjeta.numerotarjeta = public.tarjeta.numerotarjeta) " +
				"WHERE public.tarjeta.cedulatarjetahabiente = '" +
				tarjetaHome.getInstance().getCedulatarjetahabiente()+ "'" +
				" and public.depositostarjeta.fecha BETWEEN public.tarjeta.fechainicio and public.tarjeta.fechafin").getSingleResult();
		System.out.println("Saldo Depositos Bolivares>>" + totalDepositosBolivares);
		if(totalDepositosBolivares==null)
			totalDepositosBolivares = BigDecimal.ZERO;
		//total depositos en Bolivares por la tarjeta actual
		totalDepositosBolivaresActual = (BigDecimal) entityManager.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.fecha between d.tarjeta.fechainicio and d.tarjeta.fechafin and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		System.out.println("TOTAL DEPOSITOS ESTA TARJETA>> " + totalDepositosBolivaresActual);
		if(totalDepositosBolivaresActual==null)
			totalDepositosBolivaresActual = BigDecimal.ZERO;		
		
		totalTransaccionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionPesos==null)
			totalTransaccionPesos = BigDecimal.ZERO;
		
		totalTransaccionDolares = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionDolares==null)
			totalTransaccionDolares = BigDecimal.ZERO;
		
		totalComisionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalComisionPesos==null)
			totalComisionPesos = BigDecimal.ZERO;
		
		totalTransaccionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionPesosInt==null)
			totalTransaccionPesosInt = BigDecimal.ZERO;
		
		totalTransaccionDolaresInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionDolaresInt==null)
			totalTransaccionDolaresInt = BigDecimal.ZERO;
		
		totalComisionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.fechatx between t.tarjeta.fechainicio and t.tarjeta.fechafin and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalComisionPesosInt==null)
			totalComisionPesosInt = BigDecimal.ZERO;
		
		totalComisiones = totalComisionPesosInt.add(totalComisionPesos);
		totalTransacciones = totalTransaccionPesosInt.add(totalTransaccionPesos);
		totalConDeducciones = totalComisiones.subtract(totalDepositosPesos);
		
		this.saldodepoviajero = BigDecimal.ZERO;
		
		BigDecimal bd = (BigDecimal) entityManager.createNativeQuery("select preciodolarven from public.variable").getSingleResult();
		
		if (this.viajeactual.getCupoinicialviajero()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(this.viajeactual.getCupoinicialviajero()*bd.floatValue()));
		}
		
		if (this.viajeactual.getCupoinicialinternet()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(this.viajeactual.getCupoinicialinternet()*bd.floatValue()));
		}
		
		if(totalDepositosBolivares.longValue()>0){
			this.saldodepoviajero = this.saldodepoviajero.subtract(totalDepositosBolivares);
		}		
		System.out.println("SALDO DEPOSITOS >>>>>" +  saldodepoviajero );
		
    }

	
	/* Se sustituyo pensando en la tarjeta
	 * 


	public void cargarTotales(String numerotarjeta){
    	//entityManager.clear();
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
		
		totalComisionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalComisionPesos==null)
			totalComisionPesos = BigDecimal.ZERO;
		
		totalTransaccionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionPesosInt==null)
			totalTransaccionPesosInt = BigDecimal.ZERO;
		
		totalTransaccionDolaresInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionDolaresInt==null)
			totalTransaccionDolaresInt = BigDecimal.ZERO;
		
		totalComisionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalComisionPesosInt==null)
			totalComisionPesosInt = BigDecimal.ZERO;
		
		totalComisiones = totalComisionPesosInt.add(totalComisionPesos);
		totalTransacciones = totalTransaccionPesosInt.add(totalTransaccionPesos);
		totalConDeducciones = totalComisiones.subtract(totalDepositosPesos);
		
		this.saldodepoviajero = BigDecimal.ZERO;
		
		if (this.viajeactual.getCupoinicialviajero()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(this.viajeactual.getCupoinicialviajero()*4.3));
		}
		
		if (this.viajeactual.getCupoinicialinternet()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(this.viajeactual.getCupoinicialinternet()*4.3));
		}
		
		if(totalDepositosBolivares.longValue()>0){
			this.saldodepoviajero = this.saldodepoviajero.subtract(totalDepositosBolivares);
		}
		
		 
		
		//this.saldodepointernet = new BigDecimal(this.viajeactual.getCupointernet()*4.3 - )
		
		
		 
		//System.out.println("Total 0 " + resultTotal.get(0).toString());
		//System.out.println("Total 1 " + resultTotal.toString());
    }


	 * 
	 */
	
	
	
	public Viaje getViajeactual() {
		return viajeactual;
	}

	public void setViajeactual(Viaje viajeactual) {
		this.viajeactual = viajeactual;
	}

	public List<Porcentajecomisiontx> getListaporc() {
		return listaporc;
	}

	public void setListaporc(List<Porcentajecomisiontx> listaporc) {
		this.listaporc = listaporc;
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

	public BigDecimal getTotalComisionPesos() {
		return totalComisionPesos;
	}

	public void setTotalComisionPesos(BigDecimal totalComisionPesos) {
		this.totalComisionPesos = totalComisionPesos;
	}

	public BigDecimal getTotalTransaccionDolares() {
		return totalTransaccionDolares;
	}

	public void setTotalTransaccionDolares(BigDecimal totalTransaccionDolares) {
		this.totalTransaccionDolares = totalTransaccionDolares;
	}

	public BigDecimal getTotalTransaccionPesosInt() {
		return totalTransaccionPesosInt;
	}

	public void setTotalTransaccionPesosInt(BigDecimal totalTransaccionPesosInt) {
		this.totalTransaccionPesosInt = totalTransaccionPesosInt;
	}

	public BigDecimal getTotalComisionPesosInt() {
		return totalComisionPesosInt;
	}

	public void setTotalComisionPesosInt(BigDecimal totalComisionPesosInt) {
		this.totalComisionPesosInt = totalComisionPesosInt;
	}

	public BigDecimal getTotalTransaccionDolaresInt() {
		return totalTransaccionDolaresInt;
	}

	public void setTotalTransaccionDolaresInt(BigDecimal totalTransaccionDolaresInt) {
		this.totalTransaccionDolaresInt = totalTransaccionDolaresInt;
	}

	public BigDecimal getTotalComisiones() {
		return totalComisiones;
	}

	public void setTotalComisiones(BigDecimal totalComisiones) {
		this.totalComisiones = totalComisiones;
	}

	public BigDecimal getTotalConDeducciones() {
		return totalConDeducciones;
	}

	public void setTotalConDeducciones(BigDecimal totalConDeducciones) {
		this.totalConDeducciones = totalConDeducciones;
	}

	public BigDecimal getTotalTransacciones() {
		return totalTransacciones;
	}

	public void setTotalTransacciones(BigDecimal totalTransacciones) {
		this.totalTransacciones = totalTransacciones;
	}

	public long getDias() {
		return dias;
	}

	public void setDias(long dias) {
		this.dias = dias;
	}
    
	
	private String estado = "t";
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void cambiarEstado(String estado) {
		System.out.println("Aca " + estado);
		this.estado = estado;
		//AdministrarEnvios.setEstado(estado);
	}
	
	EntityQuery<Tarjeta> tarjetas = new EntityQuery<Tarjeta>();
	
	@In(create=true) 
	private TarjetaList	tarjetaList;

	public EntityQuery<Tarjeta> getTarjetas() {
		
		//tarjetas.setFirstResult(firstResult)		
		//tarjetas.setMaxResults(50);
		//tarjetas.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		return tarjetas;
		
	}

	public void setTarjetas(EntityQuery<Tarjeta> tarjetas) {
		this.tarjetas = tarjetas;
	}
	
	//Aplica filtros de busquda para TajetaList
    public void busqueda(){    	
    	
    	entityManager.clear();
    	//tarjetas.clear();
    	
    	/*
    	 * Estados de Tarjetas:
    	 * Todas:		"t"
    	 * Activa: 		"a"
    	 * Bloqueada:	"b"
    	 * Fin Viaje:	"f"
    	 * Sin Viaje:	"s"
    	 * */
    	
    	System.out.println("Estado Inicial: " + this.getEstado() );	
		
    	String sql = "";
		 
		if(!this.getEstado().contentEquals("b") && !this.getEstado().contentEquals("a") ){			
			sql = sql + "select tarjeta from Tarjeta tarjeta where 1 = 1 ";
		}else{			
			sql = sql + "select tarjeta from Tarjeta tarjeta, Tarjetaviaje tv, Viaje v " +
					"where tv.tarjeta.numerotarjeta = tarjeta.numerotarjeta and " +
					"tv.viaje.consecutivo = v.consecutivo and " +
					"tarjeta.fechainicio = v.fechainicio ";
		}
		
		if(tarjetaList.getTarjeta().getNumerotarjeta() != null && 
				!tarjetaList.getTarjeta().getNumerotarjeta().contentEquals("")) {
			sql = sql + " and lower(tarjeta.numerotarjeta) like lower(concat('%',concat('"+tarjetaList.getTarjeta().getNumerotarjeta()+"','%')))";
		}
		//reemplazado por el trim para los espacios en los nombres de promotor
		if(tarjetaList.getNombre()!=null && !tarjetaList.getNombre().contentEquals("")){
			sql = sql + " and replace(trim(tarjeta.promotor.personal.nombre)||' '||trim(tarjeta.promotor.personal.apellido),' ','') " +
					"= replace(trim('"+tarjetaList.getNombre()+"'),' ','')";
			
		}
		
		if(tarjetaList.getTarjeta().getTarjetahabiente() != null && 
				!tarjetaList.getTarjeta().getTarjetahabiente().contentEquals("")) {
			sql = sql + " and lower(tarjeta.tarjetahabiente) like " +
					"lower(concat('%',concat('"+tarjetaList.getTarjeta().getTarjetahabiente()+"','%')))";
		}
		
		
		if(tarjetaList.getDocasesor() != null && 
				!tarjetaList.getDocasesor().contentEquals("")) {
			sql = sql + " and tarjeta.promotor.asesor.documento like concat('"+tarjetaList.getDocasesor()+"','%')";
		}		
		
		if (this.getEstado().contentEquals("a")){
			sql = sql + " and( now() between tarjeta.fechainicio and tarjeta.fechafin) and tv.estado = 1";
		}else if (this.getEstado().contentEquals("b")){
			sql = sql + " and tv.estado = 0 ";
		}else if (this.getEstado().contentEquals("f")){
			sql = sql + " and now() > tarjeta.fechafin";
		}else if (this.getEstado().contentEquals("s")){
			sql = sql + " and tarjeta.fechafin is null";
		}else{
			//tarjetas.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		}
		
		tarjetas.setEjbql(sql);
		
		if(tarjetas.getResultCount()<25){
			tarjetas.setFirstResult(0);
		}
		
		tarjetas.setMaxResults(25);
		
    }
    
    
   
    
public void ubicarPromotorList(){
    	System.out.println("1");
    	//Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
    	System.out.println("2");
    	//tarjetaList.setNombre(this.nombre);
		/*
    	if(pr!=null){
			System.out.println("3");
			promotorHome.setPromotorDocumento(pr.getDocumento());
			System.out.println("4");
			promotorHome.setInstance(pr);
			System.out.println("5");
			tarjetaList.setNombre(pr.getPersonal().getNombre()+ " "+pr.getPersonal().getApellido());
			System.out.println("6");
			//enviosList.setPromotor(pr);
		}else{
			tarjetaList.setNombre(null);
		}
		*/
		
		
		
		
		
		
		
	}	
    
	Integer saldoinicialviajeronuevo;
	Integer saldoinicialinternetnuevo;
	
	
	

	public Integer getSaldoinicialviajeronuevo() {
		return saldoinicialviajeronuevo;
	}

	public void setSaldoinicialviajeronuevo(Integer saldoinicialviajeronuevo) {
		this.saldoinicialviajeronuevo = saldoinicialviajeronuevo;
	}

	public Integer getSaldoinicialinternetnuevo() {
		return saldoinicialinternetnuevo;
	}

	public void setSaldoinicialinternetnuevo(Integer saldoinicialinternetnuevo) {
		this.saldoinicialinternetnuevo = saldoinicialinternetnuevo;
	}

	public void recalcularInicial(){
		
		if(this.viajeactual.getCupoinicialviajero()==null)
			this.viajeactual.setCupoinicialviajero(0);
		
		if(this.viajeactual.getCupoviajero()==null)
			this.viajeactual.setCupoviajero(0);
		
		if (this.saldoinicialviajeronuevo != null){
		Integer sviajeroactual = this.viajeactual.getCupoinicialviajero() 
			- this.saldoinicialviajeronuevo;
		this.viajeactual.setCupoinicialviajero(saldoinicialviajeronuevo); 
		this.viajeactual.setCupoviajero(this.viajeactual.getCupoviajero() 
				- sviajeroactual);
		
		BigDecimal tv = (BigDecimal) entityManager.createNativeQuery("select sum(t.valortxdolares) " +
				"from transaccion t, tarjetaviaje tv, viaje v, tarjeta tr " +
				"where v.consecutivo = "+this.viajeactual.getConsecutivo()+" and tv.consecutivoviaje = v.consecutivo and " +
				"t.numerotarjeta = tv.numerotarjeta and t.tipotx = 'V' and " +
				"t.numerotarjeta = tr.numerotarjeta and tr.fechainicio = v.fechainicio and " +
				"tr.fechafin = v.fechafin and " +
				"t.fechatx between v.fechainicio and v.fechafin").getSingleResult();
		
		if (tv==null)
			tv = BigDecimal.ZERO;
		
		System.out.println("Cupo viajero " + this.saldoinicialviajeronuevo);
		
		this.viajeactual.setCupoviajero(this.saldoinicialviajeronuevo 
				- tv.intValue());
		}
		
		if(this.viajeactual.getCupoinicialinternet()==null)
			this.viajeactual.setCupoinicialinternet(0);
		
		if(this.viajeactual.getCupointernet()==null)
			this.viajeactual.setCupointernet(0);
		
		if (this.saldoinicialinternetnuevo != null){
		Integer sinternetactual = this.viajeactual.getCupoinicialinternet() 
		- this.saldoinicialinternetnuevo;
	this.viajeactual.setCupoinicialinternet(saldoinicialinternetnuevo);
		this.viajeactual.setCupointernet(this.viajeactual.getCupointernet() 
				- sinternetactual);
		
		BigDecimal ti = (BigDecimal) entityManager.createNativeQuery("select sum(t.valortxdolares) " +
				"from transaccion t, tarjetaviaje tv, viaje v, tarjeta tr " +
				"where v.consecutivo = "+this.viajeactual.getConsecutivo()+" and tv.consecutivoviaje = v.consecutivo and " +
				"t.numerotarjeta = tv.numerotarjeta and t.tipotx = 'I'and " +
				"t.numerotarjeta = tr.numerotarjeta and tr.fechainicio = v.fechainicio and " +
				"tr.fechafin = v.fechafin and " +
				"t.fechatx between v.fechainicio and v.fechafin").getSingleResult();
		
		if (ti==null)
			ti = BigDecimal.ZERO;
		
		this.viajeactual.setCupointernet(this.saldoinicialinternetnuevo 
				- ti.intValue());
		}
		
		
		
		this.viajeactual.setFechamod(new Date());
    	this.viajeactual.setUsuariomod(identity.getUsername());
		
		
		
		entityManager.merge(this.viajeactual);
		entityManager.flush();
		
		
		AdministrarUsuario.auditarUsuario(16, "Se ha cambiado el saldo inicial del viaje "+this.viajeactual.getConsecutivo()+"");

		saldoinicialviajeronuevo = null;
		saldoinicialinternetnuevo = null;
	}
	
	/**
	 * Elimina el viaje(s) que tenga la tarjeta
	 */
	public void liberarTarjeta(){
		
		/** Averigua el numero total de viajes que tiene esta tarjeta**/
		/** Obtengo una lista de los consecutivos de viajes de la tarjeta**/
		List< Integer > consecutivos = entityManager.createNativeQuery("SELECT " +
				"public.tarjetaviaje.consecutivoviaje " + 
				"FROM " +
				"public.tarjetaviaje " +
				"WHERE " +
				"public.tarjetaviaje.numerotarjeta = '" + 
				this.tarjetaHome.getInstance().getNumerotarjeta()+"'").getResultList();
		
		//System.out.println(">>>>TOTAL DE VIAJES: " + consecutivos.size());
		
		if(consecutivos.size() == 0){
			this.setSinViaje(true);
			return;
		}
		
		if( consecutivos.size() > 1 ){// si tiene varios viajes			
			
			for(Integer consecutivo: consecutivos){				
				
				Viaje viaje = entityManager.find(Viaje.class, consecutivo);
								
				AdministrarUsuario.auditarUsuario(31, "Se elimino viaje de la tarjeta con numero " + 
						this.tarjetaHome.getInstance().getNumerotarjeta() + " Consecutivo:  " + viaje.getConsecutivo() +
				" Cedula: " + viaje.getCedulatarjetahabiente() + " Fecha Inicio: " + viaje.getFechainicio() + " Fecha Fin: " + viaje.getFechafin() +
				" Viajero: " + viaje.getCupoinicialviajero() + " Internet: " + viaje.getCupoinicialinternet());
				
				
				/** Averigua el numero de tarjetas que tiene el viaje **/
				BigInteger numeroviajes = (BigInteger) entityManager.createNativeQuery("SELECT " +
							"count(public.tarjetaviaje.consecutivoviaje) " + 
							"FROM " +
							"public.tarjetaviaje " +
							"WHERE " +
							"public.tarjetaviaje.consecutivoviaje = "+consecutivo+"").getSingleResult();
				
				/** Elimina en tarjetaviaje la relacion **/
				entityManager.createNativeQuery("delete from public.tarjetaviaje " +
						"where consecutivoviaje = "+consecutivo+" and " +
								"numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"'").executeUpdate();
				
				/** Si el viaje tiene solo una tarjeta asociada, elimina el viaje **/
				if(numeroviajes.intValue() == 1){
					entityManager.createNativeQuery("delete from public.viaje " +
							"where consecutivo = "+consecutivo+"").executeUpdate();
				}
			}//fin del for
			
			/** Actualiza los datos del viaje en la tarjeta luego de borrar los viajes**/
			entityManager.createNativeQuery("update public.tarjeta set " +
					"fechainicio = null, fechafin = null, " +
					"cedulatarjetahabiente = '', " +
					"direcciontarjetahabiente='', " +
					"telefonotarjetahabiente='' where " +
					"numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"'").executeUpdate();			
		}else{//si solo tiene un viaje la tarjeta
			
			/** Averigua el consecutivo actual del viaje de la tarjeta **/
			Integer consecutivoviaje = (Integer) entityManager.createNativeQuery("SELECT "+ 
					"public.viaje.consecutivo "+
				"FROM "+
					"public.tarjeta "+
					"INNER JOIN public.tarjetaviaje ON (public.tarjeta.numerotarjeta = public.tarjetaviaje.numerotarjeta) "+
					"INNER JOIN public.viaje ON (public.tarjetaviaje.consecutivoviaje = public.viaje.consecutivo) "+
					"AND (public.tarjetaviaje.consecutivoviaje = public.viaje.consecutivo) "+
				"WHERE "+
					"public.viaje.fechainicio = public.tarjeta.fechainicio AND "+ 
					"public.viaje.fechafin = public.tarjeta.fechafin AND  "+
					"public.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"'").getSingleResult();
			
			Viaje viaje = entityManager.find(Viaje.class, consecutivoviaje);
			
			AdministrarUsuario.auditarUsuario(31, "Se elimino viaje de la tarjeta con numero " + this.tarjetaHome.getInstance().getNumerotarjeta() + " " +
			"Cedula: " + viaje.getCedulatarjetahabiente() + " Fecha Inicio: " + viaje.getFechainicio() + " Fecha Fin: " + viaje.getFechafin() +
			" Viajero: " + viaje.getCupoinicialviajero() + " Internet: " + viaje.getCupoinicialinternet());
			
			
			/** Averigua el numero de tarjetas que tiene un viaje **/
			BigInteger numeroviajes = (BigInteger) entityManager.createNativeQuery("SELECT " +
						"count(public.tarjetaviaje.consecutivoviaje) " + 
						"FROM " +
						"public.tarjetaviaje " +
						"WHERE " +
						"public.tarjetaviaje.consecutivoviaje = "+consecutivoviaje+"").getSingleResult();
			
			/** Elimina en tarjetaviaje la relacion **/
			entityManager.createNativeQuery("delete from public.tarjetaviaje " +
					"where consecutivoviaje = "+consecutivoviaje+" and " +
							"numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"'").executeUpdate();
			
			/** Si el viaje tiene solo una tarjeta asociada, elimina el viaje **/
			if(numeroviajes.intValue() == 1){
				entityManager.createNativeQuery("delete from public.viaje " +
						"where consecutivo = "+consecutivoviaje+"").executeUpdate();
			}
			
			/** Actualiza los datos del viaje en la tarjeta **/
			entityManager.createNativeQuery("update public.tarjeta set " +
					"fechainicio = null, fechafin = null, " +
					"cedulatarjetahabiente = '', " +
					"direcciontarjetahabiente='', " +
					"telefonotarjetahabiente='' where " +
					"numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"'").executeUpdate();			
		}
		tarjetaList.refresh();
	}
	
	
	public boolean isSinViaje() {
		return sinViaje;
	}

	public void setSinViaje(boolean sinViaje) {
		this.sinViaje = sinViaje;
	}

	List<Object> bloqueos = null;

	public List<Object> getBloqueos() {
		return bloqueos;
	}

	public void setBloqueos(List<Object> bloqueos) {
		this.bloqueos = bloqueos;
	}	
		
	//retorna la fecha en la que se creo la tarjeta en el sistema	
	Date fechaCreacionTarjeta;
	
	public Date getFechaCreacionTarjeta()
	{		

		fechaCreacionTarjeta = (Date) entityManager.createNativeQuery("select min(tv.fechamod) " +
				"from audit_tarjeta tv " +
				"where tv.numerotarjeta = '" +tarjetaHome.getInstance().getNumerotarjeta()+"'").getSingleResult();

		return fechaCreacionTarjeta;
	}

	public void setFechaCreacionTarjeta(Date fechaCreacionTarjeta) {
		this.fechaCreacionTarjeta = fechaCreacionTarjeta;
	}
	
	
	public String guardarNuevoViaje(){
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		List<Viaje> viajesahora = entityManager.createQuery(
				"select v from Viaje v, Tarjetaviaje tv " +
				"where tv.tarjeta.numerotarjeta = '"+this.tarjetaHome.getInstance().getNumerotarjeta() + 
				"' and tv.viaje.consecutivo = v.consecutivo " +
				"and (to_char(v.fechainicio,'yyyy') = " +
				"	to_char(to_date('"+sdf.format(tarjetaHome.getInstance().getFechainicio())+"','dd/mm/yyyy'),'yyyy') or " +
					"to_char(v.fechafin,'yyyy') = to_char(to_date('" 
				+sdf.format(tarjetaHome.getInstance().getFechafin())+"','dd/mm/yyyy'),'yyyy'))" +
				"order by fechafin desc").getResultList();
		
		if(viajesahora.size()>0){
    		Viaje v = viajesahora.get(0);
    		this.facesMessages.add("Ya se encuentra un viaje registrado para esta tarjeta en este periodo del año.  El viaje comienza " +
    				"" + sdf.format(v.getFechainicio()) + " hasta el " + sdf.format(v.getFechafin()) + " debe proceder a editarlo." );
    		return null;
    	}
		
    	System.out.println("Tarjeta " + tarjetaHome.getInstance().getPromotor().getDocumento());

    	
    	
    	Expressions expressions = new Expressions();
    	
    	tarjetaHome.setUpdatedMessage(expressions.createValueExpression("El viaje ha sido actualizado correctamente"));
    	
    	tarjetaHome.update();
    	
    	AdministrarUsuario.auditarUsuario(2, "Se ha actualizado las fechas de la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + " para el nuevo viaje");

    	
    	this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
    	this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
    	
    	this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
    	this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
    	
    	this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
    			.getCedulatarjetahabiente());
    	
    	this.viajeactual.setFechamod(new Date());
    	this.viajeactual.setUsuariomod(identity.getUsername());
    	
    	BigInteger query = (BigInteger)entityManager
		.createNativeQuery("select nextval('viaje_consecutivo_seq')").getSingleResult();
		viajeactual.setConsecutivo(query.intValue());
    	
		
		//ViajeHome vh = new ViajeHome();
		//viajeHome.setInstance(this.viajeactual);
		
		//viajeHome.persist();
    	entityManager.persist(this.viajeactual);
    	
    	//entityManager.flush();
    	
    	
    	
    	entityManager.flush();
    	
    	entityManager.createNativeQuery("update tarjeta set fechainicio = '"+sdf.format(this.viajeactual.getFechainicio())+"', " +
    			"fechafin = '"+sdf.format(this.viajeactual.getFechafin())+"' where " +
    					"numerotarjeta in " +
    			"(select numerotarjeta " +
    			"from tarjetaviaje where consecutivoviaje = "+this.viajeanterior.getConsecutivo()+")").executeUpdate();
    	
    	
    	entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
    			"select '1', '"+this.viajeactual.getConsecutivo()+"', numerotarjeta " +
    			"from tarjetaviaje where consecutivoviaje = "+this.viajeanterior.getConsecutivo()+"").executeUpdate();
    	
    	
    	//entityManager.clear();
    	//entityManager.persist(tv);
    	entityManager.flush();
    	
    	
    	
    	/*
    	
    	
    	if(this.tarjetaHome.getInstance().getCedulatarjetahabiente()!=null){
    	if(!this.viajeasignado && ((this.viajeactual.getCupointernet()!=null) 
    			|| (this.viajeactual.getCupoinicialinternet()!=null && 
    					tarjetaHome.getInstance().getFechainicio()!=null && 
    					tarjetaHome.getInstance().getFechafin()!=null)) && 
    			tarjetaHome.getInstance().getCedulatarjetahabiente() != null && 
    			!tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals("")){
    	this.viajeactual.setFechainicio(tarjetaHome.getInstance().getFechainicio());
    	this.viajeactual.setFechafin(tarjetaHome.getInstance().getFechafin());
    	
    	this.viajeactual.setCupointernet(this.viajeactual.getCupoinicialinternet());
    	this.viajeactual.setCupoviajero(this.viajeactual.getCupoinicialviajero());
    	
    	this.viajeactual.setCedulatarjetahabiente(tarjetaHome.getInstance()
    			.getCedulatarjetahabiente());
    	
    	this.viajeactual.setFechamod(new Date());
    	this.viajeactual.setUsuariomod(identity.getUsername());
    	
    	BigInteger query = (BigInteger)entityManager
		.createNativeQuery("select nextval('viaje_consecutivo_seq')").getSingleResult();
		viajeactual.setConsecutivo(query.intValue());
    	
		
		//ViajeHome vh = new ViajeHome();
		//viajeHome.setInstance(this.viajeactual);
		
		//viajeHome.persist();
    	entityManager.persist(this.viajeactual);
    	
    	//entityManager.flush();
    	
    	
    	
    	entityManager.flush();
    	
    	entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
    			"values ('1', '"+this.viajeactual.getConsecutivo()+"', '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
    	
    	
    	//entityManager.clear();
    	//entityManager.persist(tv);
    	entityManager.flush();
    	facesMessages.add("El viaje se ha creado de forma correcta");
    	AdministrarUsuario.auditarUsuario(8, "Se ha creado el viaje "+query+" para la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + " ");

    	
    	}else{
    		if(this.viajeasignado){
    			List s = entityManager.createQuery("select tv from Tarjetaviaje tv " +
            			"where tv.id.consecutivoviaje = "+this.viajeactual.getConsecutivo()+" " +
            			"and tv.id.numerotarjeta='"+tarjetaHome.getInstance().getNumerotarjeta()+"'").getResultList();
            	if (s.size()<=0){
            		entityManager.createNativeQuery("insert into tarjetaviaje (estado, consecutivoviaje, numerotarjeta)	" +
            			"values ('1', '"+this.viajeactual.getConsecutivo()+"', '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").executeUpdate();
            		facesMessages.add("Se ha asignado un viaje ya creado a la tarjeta");
            		AdministrarUsuario.auditarUsuario(14, "Se ha asignado un viaje para la tarjeta " + this.tarjetaHome.getInstance().getNumerotarjeta() + " ");

            	}
    			
    		}
    	}
    	*/
    	
    	/*
    	Tarjetaviaje tv = new Tarjetaviaje();
    	tv.setTarjeta(tarjetaHome.getInstance());
    	tv.setViaje(viajeactual);
    	tv.setId(new TarjetaviajeId(tarjetaHome.getInstance().getNumerotarjeta(), 
    			this.viajeactual.getConsecutivo()));
    	tv.setEstado(true);
    	*/
    	
    	/* Se realiza una insercion en codigo nativo para evitar un problema de 
		   Caused by javax.persistence.PersistenceException with message: "org.hibernate.exception.SQLGrammarException: 
		   Could not execute JDBC batch update" 
		   */
    	
    	
    	tarjetaHome.clearInstance();
    	promotorHome.clearInstance();
    	this.viajeactual = null;
    	this.nombre = "";
    	
    	return "viajecreado";
    	/*
    	}else{
    		facesMessages.add("El viaje no ha podido ser creado por la falta del documento del tarjetahabiente");
    	}
    	*/
    }
	Tarjetaviaje tarjetaviaje = new Tarjetaviaje();

	public Tarjetaviaje getTarjetaviaje() {
		return tarjetaviaje;
	}

	public void setTarjetaviaje(Tarjetaviaje tarjetaviaje) {
		this.tarjetaviaje = tarjetaviaje;
	}
	
	public void verHistorico(String numerotarjeta){
		log.info("Validacion de tarjeta numero " + numerotarjeta);
		
		List<Tarjeta> ta = entityManager
		.createQuery("select t from Tarjeta t where " +
				"t.numerotarjeta = '"+numerotarjeta+"'")
		.getResultList();
    	if (ta.size()>0){
    		facesMessages.addToControl("numerotarjeta", "Esta pagina es con informacion historica de los " +
    				"viajes de las tarjetas ");
    		tarjetaHome.setTarjetaNumerotarjeta(numerotarjeta);
    		
    		List<Tarjetaviaje> tav = entityManager
    		.createQuery("select t from Tarjetaviaje t where " +
    				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' order by t.viaje.fechainicio desc")
    		.getResultList();
    		
    		
    		this.tarjetaviaje = tav.get(0);
    		
    		this.viajeactual = tarjetaviaje.getViaje();
    		this.cargarViajeHistorico(numerotarjeta, viajeactual.getConsecutivo());
    		
    		this.nombre = tarjetaHome.getInstance().getPromotor().getPersonal().getNombre() + " " + 
    				tarjetaHome.getInstance().getPromotor().getPersonal().getApellido();
    		return;
    	}else{

    	}
	}
	
	public String anoFecha(Date d){
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy");
		return sdf.format(d);
		
	}
	
	
	List <Depositostarjeta> depositos = null;
	
	
	public List<Depositostarjeta> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Depositostarjeta> depositos) {
		this.depositos = depositos;
	}

	public void cargarViajeHistorico(String numerotarjeta, Integer numviaje){
		Viaje viajeactual = entityManager.find(Viaje.class, numviaje);
		this.viajeactual = viajeactual;
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		tviajero = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'V' and t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"'").getResultList();
    	
    	tinternet = (List<Transaccion>) entityManager.createQuery("select t from Transaccion t " +
    			"where t.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and t.tipotx = 'I' and t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"'").getResultList();
    	
    	depositos =  entityManager.createQuery("select d from Depositostarjeta d " +
    			"where d.tarjeta.numerotarjeta = '" + numerotarjeta + "' " + 
    			"and d.fecha between '"+ sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin()) + "'").getResultList();
    	
    	
    	totalDepositosPesos = (BigDecimal) entityManager
		.createQuery("select sum(d.depositopesos) " +
				"from Depositostarjeta d " +
				"where d.fecha between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosPesos==null)
			totalDepositosPesos = BigDecimal.ZERO;
		totalDepositosBolivares = (BigDecimal) entityManager
		.createQuery("select sum(d.valordeposito) " +
				"from Depositostarjeta d " +
				"where d.fecha between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"d.tarjeta.numerotarjeta = '"+numerotarjeta+"'").getSingleResult();
		if(totalDepositosBolivares==null)
			totalDepositosBolivares = BigDecimal.ZERO;
		totalTransaccionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionPesos==null)
			totalTransaccionPesos = BigDecimal.ZERO;
		totalTransaccionDolares = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalTransaccionDolares==null)
			totalTransaccionDolares = BigDecimal.ZERO;
		
		totalComisionPesos = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'V'").getSingleResult();
		if(totalComisionPesos==null)
			totalComisionPesos = BigDecimal.ZERO;
		
		totalTransaccionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxpesos) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionPesosInt==null)
			totalTransaccionPesosInt = BigDecimal.ZERO;
		
		totalTransaccionDolaresInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valortxdolares) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalTransaccionDolaresInt==null)
			totalTransaccionDolaresInt = BigDecimal.ZERO;
		
		totalComisionPesosInt = (BigDecimal) entityManager
		.createQuery("select sum(t.valorcomision) " +
				"from Transaccion t " +
				"where t.fechatx between '"+sdf.format(viajeactual.getFechainicio())+"' " +
    					"and '"+sdf.format(viajeactual.getFechafin())+"' and " +
				"t.tarjeta.numerotarjeta = '"+numerotarjeta+"' and " +
						"t.tipotx = 'I'").getSingleResult();
		if(totalComisionPesosInt==null)
			totalComisionPesosInt = BigDecimal.ZERO;
		
		totalComisiones = totalComisionPesosInt.add(totalComisionPesos);
		totalTransacciones = totalTransaccionPesosInt.add(totalTransaccionPesos);
		totalConDeducciones = totalComisiones.subtract(totalDepositosPesos);
		
		this.saldodepoviajero = BigDecimal.ZERO;
		
		if (this.viajeactual.getCupoinicialviajero()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(viajeactual.getCupoinicialviajero()*4.3));
		}
		
		if (this.viajeactual.getCupoinicialinternet()!=null){
			this.saldodepoviajero = this.saldodepoviajero.add(new BigDecimal(viajeactual.getCupoinicialinternet()*4.3));
		}
		
		if(totalDepositosBolivares.longValue()>0){
			this.saldodepoviajero = this.saldodepoviajero.subtract(totalDepositosBolivares);
		}
    	
	}
	
	
	public void auditarCaracteristicaTac(){
		
		if(tarjetaHome.getInstance().getTac()){
			AdministrarUsuario.auditarUsuario(37,
				"Marco como tarjeta TAC la tarjeta " + tarjetaHome.getInstance().getNumerotarjeta());
		}else{
			AdministrarUsuario.auditarUsuario(38,
					"Desmarco como tarjeta TAC la tarjeta " + tarjetaHome.getInstance().getNumerotarjeta());			
		}
		
	}
	
	
}
