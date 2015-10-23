package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Baucher;
import org.domain.moneda.entity.BaucherId;
import org.domain.moneda.entity.Deducciones;
import org.domain.moneda.entity.DeduccionesId;
import org.domain.moneda.entity.Establecimiento;
import org.domain.moneda.entity.Franquicia;
import org.domain.moneda.entity.Gravamen;
import org.domain.moneda.entity.Gravamenestablecimiento;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Porcentajecomisiontx;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Promotorcomisiontx;
import org.domain.moneda.entity.Promotorfranquicia;
import org.domain.moneda.entity.Promotortasa;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tarjetaviaje;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.Transaccion;
import org.domain.moneda.entity.Viaje;
import org.domain.moneda.session.AutovozHome;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.EstablecimientoList;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TransaccionHome;
import org.domain.moneda.session.ViajeHome;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.Reporteador;
import org.hibernate.Query;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;


@Scope(CONVERSATION)
@Name("AdministrarTransaccion")
public class AdministrarTransaccion 
{
    @Logger 
    private Log log;
    
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
	private TransaccionHome	transaccionHome;
	
	@In(create=true) @Out 
	private AdministrarAutovoz	AdministrarAutovoz;
	
	@In(create=true) @Out 
	private AdministrarFactura	AdministrarFactura;
	
	@In(create=true) @Out 
	AutovozHome autovozHome;
	
	@In(create=true) @Out 
	private Reporteador	Reporteador;
	
	@In Identity identity;
	
	@In(create=true) @Out 
    AdministrarUsuario AdministrarUsuario;
	
	@In(create = true)
	private CargarObjetos CargarObjetos;
	
	private String nombrePromotor;
	
	public AdministrarTransaccion  () {
		
	}

	
	public String getNombrePromotor() {
		return nombrePromotor;
	}


	public void setNombrePromotor(String nombrePromotor) {
		this.nombrePromotor = nombrePromotor;
	}
	
	
	public void ubicarPromotor(){
		Personal pr = CargarObjetos.ubicarPersonal(this.nombrePromotor);
		System.out.println("Nombre " + this.nombrePromotor);
		//System.out.println("Doc " + pr.getDocumento());
		if(pr!=null){
			transaccionHome.getInstance().setPromotor(pr.getDocumento());
		}
	}
	
	/**
	 * Nueva version del metodo autocompletar. Comparacion basada en 
	 * expresiones regulares.
	 * @param nom
	 * @return
	 */
	public List<String> autocompletarPromotor(Object nom)
	{
		llenarPromotores(); 		// Metodo que carga la informacion de los nombres de las personas
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
	
	
	public void llenarPromotores(){
		entityManager.clear();
		String sql = "";	
		
		/*
		if(identity.hasRole("Asesor")){
			sql = " where p.asesor.documento = '"+identity.getUsername()+"'";
		}*/
		List<String> resultList = 
			entityManager.createQuery("select p.personal.nombre||' '||p.personal.apellido from Promotor p " +
				"union select a.personal.nombre||' '||a.personal.apellido from Arrastrador a"+ sql).getResultList();
		lista = resultList;
		
	}


	
	public List<String> autocompletar(Object suggest) {
		long t1 = System.currentTimeMillis();

		llenarEstablcimiento();
		String nombre = (String) suggest;
		List<String> result = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(nombre.toLowerCase());
		StringBuilder bldr = new StringBuilder();//builder usado para formar el patron
		
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
		for (String establecimiento : lista) {			
			match = p.matcher(establecimiento.toLowerCase());
			boolean b = match.find();
			if (b) {
				result.add(establecimiento);				
			}		
		}
		long t2 = System.currentTimeMillis() - t1;
		System.out.println(">>>Tiempo total de la busqueda: " + t2 + "ms");	
	return result;
	}
	
	
	
	/**
	 * Este metodo busca y autocompleta el nombre del tarjetahabiente, 
	 * usando expresiones regulares (regex). Esta forma de autocompletar permite 
	 * buscar usando cualquier parte del nombre.
	 * Ej: El nombre "Manuel Ricardo Perez Avadia" se puede buscar 
	 * --> "Manuel Avadia" ó "Manuel Perez"  ó "Ma Pe"; cualquiera 
	 * de estos patrones puden usarse para busacar un nombre; o el que el 
	 * usuario elija guardando el orden de las palabras.
	 * http://docs.oracle.com/javase/tutorial/essential/regex/intro.html
	 * 
	 * @param suggest 
	 * @return List<String> nombres encontrados
	 */
	public List<String> autocompletarHabiente(Object suggest) {
		if (tarjetafin!=null){
			long t1 = System.currentTimeMillis();//me permite medir el tiempor de la busqueda
			if (tarjetafin.length() == 4){
				llenarHabiente();
				String nombre = (String) suggest;
				List<String> result = new ArrayList<String>();
				StringTokenizer tokens = new StringTokenizer(nombre.toLowerCase());
				StringBuilder bldr = new StringBuilder();//builder usado para formar el patron
				
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
				for (String tarjetahabiente : listaHabiente) {			
					match = p.matcher(tarjetahabiente.toLowerCase());
					boolean b = match.find();
					if (b) {
						result.add(tarjetahabiente);				
					}		
				}
				long t2 = System.currentTimeMillis() - t1;
				System.out.println(">>>Tiempo total de la busqueda: " + t2 + "ms");	
			return result;
			}
		}		
		return null; 
	}
	
	
	
	
	public void llenarEstablcimiento(){
		entityManager.clear();
		List<String> resultList = entityManager.createQuery("select establecimiento.nombreestable " +
				"from Establecimiento establecimiento").getResultList();
		lista = resultList;
	}
	
	
	public void llenarHabiente(){
		
		entityManager.clear();
		List<String> resultList =
			entityManager.createQuery("select tarjeta.tarjetahabiente from Tarjeta tarjeta " +
					"where tarjeta.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		
		for( String s: resultList)
			System.out.println( ">>" +s );
		
		listaHabiente = resultList;
	}
	
	public void verificarTarjeta(){
		entityManager.clear();
		List<Tarjeta> t = (ArrayList)entityManager
		.createQuery("select t from Tarjeta t where t.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		
		if (t.size() == 1) {
			this.tarjetahabiente = t.get(0).getTarjetahabiente();
			Tarjeta ta = t.get(0);		
			tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
			tarjetaHome.setInstance(ta);
		}
		llenarHabiente();
	}
	
	public void ubicarEstablecimiento()
	{
		entityManager.clear();
		
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable) = " +
				"trim('"+this.sugestion+"')").getResultList();
		
		
//	List<Establecimiento> e = (ArrayList)entityManager
//		.createQuery("select e from Establecimiento e where trim(e.nombreestable) = " +
//				"trim('Pagina JP')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 
			
			String factura = "";
			Integer nfactura;
		
			factura = (String) entityManager.createNativeQuery("select max(numfactura) " +
					"from transaccion where codigounico = '"+es.getCodigounico()+"'").getSingleResult();			
			if (factura == null ){
				nfactura = 0;
			}else{
				nfactura = Integer.parseInt(factura);
			}
			nfactura = nfactura + 1;			
			
			transaccionHome.getInstance().setNumfactura(nfactura.toString());			
			establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
		}		
	}
	
	public void ubicarEstablecimientoWeb()
	{
		entityManager.clear();
		
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable) = " +
				"trim('Pagina JP')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 
			
			String factura = "";
			Integer nfactura;
		
			factura = (String) entityManager.createNativeQuery("select max(numfactura) " +
					"from transaccion where codigounico = '"+es.getCodigounico()+"'").getSingleResult();			
			if (factura == null ){
				nfactura = 0;
			}else{
				nfactura = Integer.parseInt(factura);
			}
			nfactura = nfactura + 1;			
			
			transaccionHome.getInstance().setNumfactura(nfactura.toString());			
			establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
		}		
	}
	
	
	
	public void ubicarTarjeta(){		
		entityManager.clear();
		List<Tarjeta> t = (ArrayList)entityManager
		.createQuery("select t from Tarjeta t where replace(t.tarjetahabiente,' ','')=replace('"+this.tarjetahabiente+"',' ','') " +
				"and t.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		
		if (t.size() > 0) {
			Tarjeta ta = t.get(0);
			
			tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
			tarjetaHome.setInstance(ta);
		}
		llenarEstablcimiento();
	}
	
	public void ubicarFactura(){
		
		// Valida que no este digitada la misma factura para la misma tarjeta
		// en el mismo dia
		
//		if (existeFactura()) {
//			facesMessages.addToControl("numfactura", "Este numero de factura ya se registro para este establecimiento o para esta tarjeta");
//		}		
		
		
		// Establece como numero de autorizacion el mismo numero de factura
		// esto se hizo por que no se reportan numeros de autorizacion en las facturas
		this.baucher1 = this.transaccionHome.getInstance().getNumfactura();
		
		System.out.println("Numero de Factura: "+this.transaccionHome.getInstance().getNumfactura()  );
	}
	
	
	/**
	 * Metodo predicado que valida que no este digitada la misma 
	 * factura para la misma tarjeta en el mismo dia
	 * @return 
	 */
	public boolean existeFactura(){		
		entityManager.clear();
		List<Transaccion> t = (ArrayList)entityManager 
		.createQuery("select t from Transaccion t " +
				"where t.establecimiento.codigounico='"+this.establecimientoHome.getInstance().getCodigounico()+"' " +
				"and t.numfactura = '"+this.transaccionHome.getInstance().getNumfactura()+"' " +
				" and (t.tarjeta.cedulatarjetahabiente <> '"+tarjetaHome.getInstance().getCedulatarjetahabiente()+"' " +
						"or tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").getResultList();		
		if (t.size() > 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public void ubicarFacturaWeb(){
		entityManager.clear();
		
		List<Transaccion> t = (ArrayList)entityManager
		.createQuery("select t from Transaccion t " +
				"where t.establecimiento.codigounico='"+this.establecimientoHome.getInstance().getCodigounico()+"' " +
				"and t.numfactura = '"+this.transaccionHome.getInstance().getNumfactura()+"' " +
				" and (t.tarjeta.cedulatarjetahabiente <> '"+tarjetaHome.getInstance().getCedulatarjetahabiente()+"' " +
						"or tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"')").getResultList();
		
		if (t.size() > 0) {
			facesMessages.addToControl("numfactura", "Este numero de factura ya se registro para este establecimiento o para esta tarjeta");
		}
		
		this.baucher1 = this.transaccionHome.getInstance().getNumfactura();
	}
	
	BigDecimal totalizar = BigDecimal.ZERO;
	
	public BigDecimal getTotalizar(){
			
		BigDecimal val = BigDecimal.ZERO;
		
		if (valor1!=null){
			try{
			val = new BigDecimal(valor1);
			}catch(Exception e){}
		}else{
			return BigDecimal.ZERO;
		}
		
		if (valor2!=null){
			try{
			val = val.add(new BigDecimal(valor2));
			}catch(Exception e){}
		}

		if (valor3!=null){
			try{
			val = val.add(new BigDecimal(valor3));
			}catch(Exception e){}
		}
		
		this.totalizar = val;
		return this.totalizar;
	}
	
	public void setTotalizar(BigDecimal totalizar){
		this.totalizar = totalizar;
	}
	
	public void inicializar(){
		transaccionHome.wire();
		if(!transaccionHome.isManaged())
			transaccionHome.getInstance().setFechatx(new Date());
		llenarEstablcimiento(); 
	}
	
	
	
	public void reportarError(String error){
		String sql= "";
		
		Promotor p = entityManager.find(Promotor.class, tarjetaHome.getInstance().getPromotor().getDocumento());
		
		
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		String fecha = sdf.format(transaccionHome.getInstance().getFechatx());
		
		
		
		sql = "insert into public.transaccionerror (numerotarjeta,codigounico,fechatx, tipotx, " +
				"numfactura,fechamod,usuariomod,asesor,promotor,digitador,error) " +
				"values ('"+transaccionHome.getInstance().getTarjeta().getNumerotarjeta()+"'," +
						"'"+transaccionHome.getInstance().getEstablecimiento().getCodigounico()+"'," +
						"'"+fecha+"'," +
						"'"+transaccionHome.getInstance().getTipotx()+"'," +
						"'"+transaccionHome.getInstance().getNumfactura()+"'," +
						"now(),'"+identity.getUsername()+"','"+p.getAsesor().getDocumento()+"'," +
								"'"+p.getDocumento()+"'," +
										"'"+identity.getUsername()+"','"+error+"')";
		entityManager.createNativeQuery(sql).executeUpdate();
		entityManager.flush();
		entityManager.clear();
	}
	
	
	
	/**
	 *	Procesa el registro de una transaccion.
	 *	 
	 */
	 
	public String guardarTransaccion()
	{
		
		Integer numerotransaccion = 0;
		
		int t = 0;//variable de control
		
		//Pais del establecimiento de la Tx
		String pais = establecimientoHome.getInstance().getPais().getCodigopais();
		
		//valida que exista un nro de TC
		if(tarjetaHome.getInstance().getTarjetahabiente()==null){
			facesMessages.addToControl("nametarjeta", "No hay una tarjeta valida asignado a la transaccion");
			t++;			
		}else{
			//valida que la tarjeta cuente con cedula
			if( tarjetaHome.getInstance().getCedulatarjetahabiente()==null ||
				tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals("")){
				facesMessages.addToControl("nametarjeta",
						"No hay una cedula asignada al tarjetahabiente de esta tarjeta");
				this.reportarError("No hay una cedula asignada al tarjetahabiente");
				t++;				
			}
		}		
		
		//valida que el promotor tenga el  % de comision para viajero si la Tx es por Colombia 
		if(pais.equals("CO")){			
			String queryString = "select pr.comisionviajero from Promotor pr where pr.documento = '" +
					tarjetaHome.getInstance().getPromotor().getDocumento()+"'";
			
			BigDecimal comisionViajero = 
				(BigDecimal) entityManager.createQuery(queryString).getSingleResult();
			
			if( comisionViajero == null || comisionViajero.doubleValue() == 0 ){
				facesMessages.addToControl("nametarjeta", "El promotor de esta tarjeta no tiene " +
						"COMISION VIAJERO asignada. Valide los datos del promotor.");
				return null;
			}			
		}		
		
		//valida que el establecimiento este bien creado y tenga codigo unico
		if(establecimientoHome.getInstance().getCodigounico()==null){
			facesMessages.addToControl("name", "No hay un establecimiento asignado a la transaccion");
			//throw new Exception();
			t++;
		}
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		String sqlviaje = "";
		//valida que la fecha de la TX este dentro de las fechas del viaje inicio y fin
		sqlviaje = "select v.consecutivo from viaje v, " +
				"tarjetaviaje tv where tv.consecutivoviaje = v.consecutivo and " +
				"tv.numerotarjeta = '"+this.tarjetaHome.getInstance().getNumerotarjeta()+"' " +
				"and ((now() between v.fechainicio and v.fechafin) or ('" + 
				sdf.format(transaccionHome.getInstance().getFechatx())+ "'" +
						" between v.fechainicio and v.fechafin)) and tv.estado = 1";
		
		
		ArrayList a = (ArrayList) entityManager.createNativeQuery(sqlviaje).getResultList();		
		
		if (a.size()<=0){
			facesMessages.addToControl("nametarjeta", "La transaccion no se " +
					"puede registrar.  El viaje tiene fecha fin inferior a HOY, " +
					"no tiene viaje asignado o se encuentra bloqueada.");			
			this.reportarError("La transaccion no se puede registrar.  El viaje tiene fecha fin inferior a HOY, no tiene viaje asignado o se encuentra bloqueada.");
			t++;
			return null;
		}		
		//uso la variable de control t
		if(t>0){			
			
		}
		
		//Aca comienza a liquidar la Tx
		
		//Establesco parametros de la Tx
		transaccionHome.getInstance().setEstablecimiento(establecimientoHome.getInstance());
		transaccionHome.getInstance().setTarjeta(tarjetaHome.getInstance());
		//stem.out.println(">>>>>>>>>>>>>>>>>>>>>>>> " + establecimientoHome.getInstance().getPais().getCodigopais( ));
		
		
		String fechatx = sdf.format(transaccionHome.getInstance().getFechatx());
		
		//busca la tasa de dolar general para el pais
		List<Tasadolar> listTasaDolar = entityManager.createQuery("select td from Tasadolar td " +
				"where td.id.codigopais = '" + pais + "' and td.id.fecha = '" + fechatx + "'")
				.getResultList();
		
		//busca si tiene dolar negociado para el promotor en el pais del establecimiento
		List<Promotortasa> listPromoDolar = entityManager.createQuery("select pt from Promotortasa pt " +
				"where pt.id.codigopais = '" + pais + "' and pt.id.fecha <= '" + fechatx + "' and" +
						" ('" + fechatx + "' <= pt.fechafin or pt.fechafin is null) and " +
				"pt.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"'" +
						" and pt.id.fecha = (select max(pt.id.fecha) from Promotortasa pt " +
				"where pt.id.codigopais = '" + pais + "' and " +
				"pt.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"')")
				.getResultList();
		
		//recive los valores de la Tx de los 3 voucher si los hay para la Tx
		//si el comercio es colombiano graba pesos sino graba dolares
		BigDecimal valorTX = BigDecimal.ZERO;
		
		if (valor1!=null){
			try{
			valorTX = new BigDecimal(valor1);
			if(valorTX.longValue()<=0){
				facesMessages.add("Valor no valido para esta transaccion");
				return null;
			}
			}catch(Exception e){}
		}else{
			return null;
		}
		
		if (valor2!=null){
			try{
			valorTX = valorTX.add(new BigDecimal(valor2));
			}catch(Exception e){}
		}

		if (valor3!=null){
			try{
			valorTX = valorTX.add(new BigDecimal(valor3));
			}catch(Exception e){}
		}
		
		Float tasa = 0F;
		//establece la tasa del dolar
		if (listTasaDolar.size() > 0 || listPromoDolar.size()>0) {
			if(listPromoDolar.size()>0){
				Promotortasa tdp = (Promotortasa) entityManager.createQuery("select pt from Promotortasa pt " +
				"where pt.id.codigopais = '" + pais + "' and pt.id.fecha <= '"+fechatx+"' and" +
						" ('"+fechatx+"' <= pt.fechafin or pt.fechafin is null) and " +
				"pt.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"'" +
						" and pt.id.fecha = (select max(pt.id.fecha) from Promotortasa pt " +
				"where pt.id.codigopais = '" + pais + "' and " +
				"pt.id.documento = '"+tarjetaHome.getInstance().getPromotor().getDocumento()+"')")
						.getSingleResult();
				tasa = tdp.getTasa().floatValue();
			}else{
				if(listTasaDolar.size()>0){
					Tasadolar td = (Tasadolar) entityManager.createQuery("select td from Tasadolar td " +
							"where td.id.codigopais = '" + pais + "' and td.id.fecha = '"+fechatx+"'")
							.getSingleResult();
					tasa = td.getTasa().floatValue();
				}
			}
			
			//liquida la TX a pesos o dolares dependiendo del pais
			if (pais.contentEquals("CO")) {
				//liquida de Pesos a Dolar
				transaccionHome.getInstance().setValortxpesos( valorTX );
				float dolares = Math.round(((100*transaccionHome.getInstance().getValortxpesos().floatValue()/tasa))/100);
				System.out.println("Dolares 1 " + Math.round(transaccionHome.getInstance().getValortxpesos().floatValue()/tasa));
				transaccionHome.getInstance().setValortxdolares(new BigDecimal(dolares));
				
			}else{//liquida de Dolar a Pesos				
				transaccionHome.getInstance().setValortxdolares( valorTX );
				float pesos = Math.round( ( (100*transaccionHome.getInstance().getValortxdolares().floatValue()*tasa))/100);
				transaccionHome.getInstance().setValortxpesos(new BigDecimal(pesos));
			}
			
			BigDecimal comision = new BigDecimal(0);//comision de la TX
			BigDecimal pcomision = new BigDecimal(0);//porcentaje cobrado en la TX
			
			Promotor promo = entityManager.find(Promotor.class, tarjetaHome.getInstance().getPromotor().getDocumento());
						
			//Se liquida la comision de la Tx para el promotor
			if ( pais.contentEquals("CO")&& transaccionHome.getInstance().getTipotx().contentEquals("V"))
			{
				/* Se calcula de la transaccion para el promotor */
				pcomision = promo.getComisionviajero();
					//busca si el promotor tiene porcentaje negociado para la franquicia
					List<Promotorfranquicia> pfs = entityManager.createQuery("select pfs from " +
							"Promotorfranquicia pfs where pfs.id.documento = '"+promo.getDocumento()+"' and " +
							"pfs.id.codfranquicia = '"+tarjetaHome.getInstance().getFranquicia().getCodfranquicia()+"'").getResultList();
					if(pfs.size()>0){
						pcomision = pfs.get(0).getPorcentaje();
					}else{						
						//busca si tiene porcentaje para la franquica para todos
						Franquicia f = entityManager.find(Franquicia.class, tarjetaHome.getInstance().getFranquicia().getCodfranquicia());
						System.out.println("Porcentaje " + f.getPorcentaje());
						
						if(f.getPorcentaje().floatValue()>0){
							pcomision = f.getPorcentaje();
							System.out.println("Establece Porcentaje para Colombia " + pcomision);
						}//fin del if interno
					}//fin del else pfs				
				
					comision = transaccionHome.getInstance().getValortxpesos()
					.multiply((new BigDecimal(100)).subtract(pcomision)) 
					.divide(new BigDecimal(100));
				
					transaccionHome.getInstance().setPorcentajecomision(pcomision);				
			}else{// !pais.contentEquals("CO")&& transaccionHome.getInstance().getTipotx().contentEquals("V")
				
				//revisa si tiene porcentaje negociado por franquicia para el promo
				List<Promotorfranquicia> pfs = entityManager.createQuery("select pfs from " +
						"Promotorfranquicia pfs where pfs.id.documento = '"+promo.getDocumento()+"' and " +
						"pfs.id.codfranquicia = '"+tarjetaHome.getInstance().getFranquicia().getCodfranquicia()+"'").getResultList();
					if(pfs.size()>0 ){
						pcomision = pfs.get(0).getPorcentaje();
					}else{					
						/* Comision de la Franquicia de la Tarjeta para todos caso Amex*/				
						Franquicia f = entityManager.find(Franquicia.class, tarjetaHome.getInstance().getFranquicia().getCodfranquicia());
						System.out.println("Porcentaje Franquicia " + f.getPorcentaje());
						
						if(f.getPorcentaje().floatValue()>0 && pais.equals("CO")){							
							pcomision = new BigDecimal( f.getPorcentaje().floatValue() ); 
							System.out.println("Establece Porcentaje Franquicia " + pcomision);
						}else{// f.getPorcentaje().floatValue()>0
					
							/* Comision del Pais para el Promotor */
							List<Promotorcomisiontx> pctxs = entityManager.createQuery("select c from Promotorcomisiontx c " +
									"where (('"+fechatx+"' >= c.id.fechainicio " +
									"and '"+fechatx+"' <= c.fechafin ) or c.fechafin is null) and c.pais ='"+pais+"' and " +
									" c.id.documento = '"+promo.getDocumento()+"' " +
									" order by fechainicio asc").getResultList();				
							if (pctxs.size()>0){
								Promotorcomisiontx pctx = (Promotorcomisiontx)pctxs.get(0);
								pcomision = pctx.getPorcentaje();
								System.out.println("Establece Porcentaje Pais Negociado Cliente " + pcomision);
							}else{//pctxs.size()>0	
								
								/* Comision del Pais para Todos */
								List<Porcentajecomisiontx> resultList = entityManager.createQuery("select c from Porcentajecomisiontx c " +
										"where (('"+fechatx+"' >= c.id.fechainicio " +
										"and '"+fechatx+"' <= c.fechafin ) or c.fechafin is null) and c.pais ='"+pais+"'" +
										" order by fechainicio asc").getResultList();
								if (resultList.size()>0){
									Porcentajecomisiontx pctx = (Porcentajecomisiontx)resultList.get(0);									
									transaccionHome.getInstance().setPorcentajecomision(pctx.getPorcentaje());															
									pcomision = pctx.getPorcentaje();
									System.out.println("Establece Porcentaje Pais " + pcomision);
								}//fin del ultimo if
							}//fin del else pctxs.size()>0				
						}//fin del else f.getPorcentaje().floatValue()>0
					}//pfs.size()>0 fin del else
					
				// establezco el porcentaje de comision de la TX
				transaccionHome.getInstance().setPorcentajecomision(pcomision);
				
				//calculo el valor de la comision de la TX
				comision = 
					transaccionHome.getInstance().getValortxpesos()
					.multiply((new BigDecimal(100)).subtract(pcomision))
					.divide(new BigDecimal(100));
				System.out.println(">>COMISION LIQUIDADA TX >> " + comision.floatValue());
			}
			//porcentaje para el establecimiento
			BigDecimal porcentajeestablecimiento = BigDecimal.ZERO;
			if(establecimientoHome.getInstance().getPorcentaje()!=null) 
				porcentajeestablecimiento = establecimientoHome.getInstance().getPorcentaje();
			//porcentaje del asesor tabla asesor			
			BigDecimal porcentajeasesor = BigDecimal.ZERO;			
			if(promo.getAsesor().getComision()!=null) 
				porcentajeasesor = promo.getAsesor().getComision();
			//porcentaje si tiene arrastrador
			BigDecimal porcentajearrastrador = BigDecimal.ZERO;			
			if(promo.getArrastrador()!=null)
				if(promo.getComisionarrastrador()!=null){
					porcentajearrastrador = promo.getComisionarrastrador();
					
				}
			
			//se liquidan la comsiones de establecimiento, asesor, arrastrador
			BigDecimal establecimientocomision = transaccionHome.getInstance().getValortxpesos()
			.multiply(porcentajeestablecimiento)
			.divide(new BigDecimal(100));
			
			BigDecimal asesorcomision = transaccionHome.getInstance().getValortxpesos()
			.multiply(porcentajeasesor)
			.divide(new BigDecimal(100));
			
			BigDecimal arrastradorcomision = transaccionHome.getInstance().getValortxpesos()
			.multiply(porcentajearrastrador)
			.divide(new BigDecimal(100));
			
			//se establecen en el bean de sesion la comisiones para promotor(comision),
			//establecimiento, asesor, arrastrador
			transaccionHome.getInstance().setValorcomision(comision);						
			transaccionHome.getInstance().setEstablecimientocomision(establecimientocomision);			
			transaccionHome.getInstance().setAsesorcomision(asesorcomision);
			transaccionHome.getInstance().setArrastradorcomision(arrastradorcomision);
			
			//valida que la tarjeta cuente con un viaje actual a la fecha de la Tx
			List<Object> resultList = entityManager.createQuery("select v from Viaje v, Tarjetaviaje tv " +
					"where tv.tarjeta.numerotarjeta = '"+tarjetaHome.getInstance().getNumerotarjeta()+"' and tv.viaje.consecutivo = v.consecutivo " +
							" and '"+sdf.format(transaccionHome.getInstance().getFechatx())+"' between v.fechainicio and v.fechafin " +
							"" +
							"order by fechafin desc").getResultList();
			Viaje viajeactual = new Viaje();
			if(resultList.size()>0){
				viajeactual = (Viaje) resultList.get(0);
			}else{
				facesMessages.add("No hay un viaje asociado para esta tarjeta");
				this.reportarError("No hay un viaje asociado para esta tarjeta");
				return null;
			}
			
			if(transaccionHome.getInstance().getTipotx().contentEquals("V")){
				if(viajeactual.getCupoinicialviajero() == null 
						|| viajeactual.getCupoviajero() == null){
					facesMessages.add("No hay un cupo Viajero asignado a esta tarjeta");
					this.reportarError("No hay un cupo Viajero asignado a esta tarjeta");
					return null;
				}else{
					if(	viajeactual.getCupoviajero().equals(0) || 
					viajeactual.getCupoinicialviajero().equals(0)){
						facesMessages.add("El cupo viajero es cero, verifique la informacion");
						this.reportarError("El cupo viajero es cero, verifique la informacion");
						return null;
					}else{
						/* Se cambio el procesamiento de actualizacion de saldos por 
						 * un disparador a nivel de la base de datos
						 */
						viajeactual.setCupoviajero((Integer)(viajeactual.getCupoviajero()-transaccionHome.getInstance().getValortxdolares().intValue()));
					}
					/* Colocar comprobacion de cupo de viajero */
				}
			}else{
				if(viajeactual.getCupoinicialinternet() == null || viajeactual.getCupointernet() == null){
					facesMessages.add("No hay un cupo de Internet asignado a esta tarjeta");
					this.reportarError("No hay un cupo de Internet asignado a esta tarjeta");
					return null;
				}else{
					if(viajeactual.getCupoinicialinternet().equals(0) || viajeactual.getCupointernet().equals(0) ){
						facesMessages.add("El cupo viajero es cero, verifique informacion");
						this.reportarError("El cupo viajero es cero, verifique informacion");
						return null;
					}else{
						/* Se cambio el procesamiento de actualizacion de saldos por 
						 * un disparador a nivel de la base de datos
						 */
						viajeactual.setCupointernet((Integer)(viajeactual.getCupointernet()-transaccionHome.getInstance().getValortxdolares().intValue()));
					}
				}
			}
			
			//se redunda en la tabla transaccion el promotor, asesor 
			if(promo.getArrastrador()!=null){
				transaccionHome.getInstance().setArrastrador(promo.getArrastrador().getDocumento());
			}
			transaccionHome.getInstance().setAsesor(promo.getAsesor().getDocumento());
			transaccionHome.getInstance().setPromotor(promo.getDocumento());
			transaccionHome.getInstance().setDigitador(identity.getUsername());
			
			BigInteger query = (BigInteger)entityManager
			.createNativeQuery("select nextval('transaccion_consecutivo_seq')").getSingleResult();
			transaccionHome.getInstance().setConsecutivo(query.intValue());
			
			numerotransaccion = query.intValue();
			transaccionHome.getInstance().setFechamod(new Date());
			transaccionHome.getInstance().setUsuariomod(identity.getUsername());
			Expressions expressions = new Expressions();
			ValueExpression mensaje;
			//String vrTx = String.format("%d",transaccionHome.getInstance().getValortxpesos().toString());
			transaccionHome.setCreatedMessage(
					expressions.createValueExpression("La transaccion de la tarjeta ************" + tarjetafin + 
							" " + this.tarjetahabiente + " por $ " + transaccionHome.getInstance().getValortxpesos().toString() + " se ha registrado correctamente" +
							""));

			transaccionHome.persist();
			//entityManager.persist(transaccionHome.getInstance());
			entityManager.flush();
			entityManager.clear();
			
			if(this.getTransaccionvoz()){
				autovozHome.getInstance().setNumtransaccion(transaccionHome.getInstance().getConsecutivo());
				//autovozHome.update();
				entityManager.merge(autovozHome.getInstance());
				this.setTransaccionvoz(false);
			}			
					
			
			if(!baucher1.contentEquals("")){
				Baucher b1 = new Baucher();
				b1.setTransaccion(transaccionHome.getInstance());
				b1.setValor(new BigDecimal(valor1));
				b1.setId(new BaucherId(transaccionHome.getInstance().getConsecutivo(), this.baucher1));
				entityManager.persist(b1);
			}
			
			if(!baucher2.contentEquals("")){
				Baucher b2 = new Baucher();
				b2.setTransaccion(transaccionHome.getInstance());
				b2.setValor(new BigDecimal(valor2));
				b2.setId(new BaucherId(transaccionHome.getInstance().getConsecutivo(), this.baucher2));
				entityManager.persist(b2);
			}
			if(!baucher3.contentEquals("")){
				Baucher b3 = new Baucher();
				b3.setTransaccion(transaccionHome.getInstance());
				b3.setValor(new BigDecimal(valor3));
				b3.setId(new BaucherId(transaccionHome.getInstance().getConsecutivo(), this.baucher3));
				entityManager.persist(b3);
			}
			entityManager.flush();
			entityManager.clear();
			
			List<Gravamenestablecimiento> lg = entityManager.createQuery("select g from " +
					"Gravamenestablecimiento g where " +
					"g.id.codigounico = '"+establecimientoHome.getInstance().getCodigounico()+"'").getResultList();
			
			for(int i = 0; i < lg.size(); i ++){
				Gravamenestablecimiento g = lg.get(i);
				BigDecimal porcentaje = BigDecimal.ZERO;
				if (g.getId().getGravamen().contentEquals("0")){
					List<BigDecimal> porcenFranqList = entityManager.createQuery("select fe.porcentaje " +
							"from Franquiciaestablecimiento fe " +
							"where fe.id.franquicia = '"+tarjetaHome.getInstance().getFranquicia().getCodfranquicia()+"' and " +
							"fe.id.establecimiento = '"+establecimientoHome.getInstance().getCodigounico()+"'").getResultList();
					if(porcenFranqList.size()>0){
						porcentaje = porcenFranqList.get(0);
					}
				}else{
					porcentaje = g.getPorcentaje();
				}
				
				Deducciones d = new Deducciones();
				DeduccionesId did = new DeduccionesId();
				
				d.setDescripcion(g.getGravamen().getNombre());
				
				did.setConsecutivo(transaccionHome.getInstance().getConsecutivo());
				did.setTipo(g.getGravamen().getCodigo());
				
				BigDecimal valor = BigDecimal.ZERO;
				
				valor = transaccionHome.getInstance().getValortxpesos();
				
				System.out.println("Establecimiento " + establecimientoHome.getInstance().getCodigounico());
				
				if(establecimientoHome.getInstance().getIva()!=null)
					if(establecimientoHome.getInstance().getIva()){
						if(!g.getIva()){
							valor = new BigDecimal(valor.doubleValue()/(1.16));
					}
				}
				
				valor = valor.multiply(porcentaje).divide(new BigDecimal("100"));
					
				
				d.setId(did);
				d.setTransaccion(transaccionHome.getInstance());
				d.setValor(valor);
				
				entityManager.persist(d);
				
				entityManager.flush();
				//System.out.println(nov);
			}
			
			
			/* Codigo para descontar el valor de la transaccion del cupo total del
			 * viaje.
			 */	
			
			//ViajeHome viajeHome = null;
			//viajeHome.setViajeConsecutivo(viajeactual.getConsecutivo());
						
			entityManager.clear();
			entityManager.merge(viajeactual);
			entityManager.flush();
			
			if(establecimientoHome.getInstance().getFacturar()!=null){
				if(establecimientoHome.getInstance().getFacturar()){
					AdministrarFactura.generarDetallefactura(transaccionHome.getInstance().getConsecutivo());
					Reporteador.generarFactura(numerotransaccion);
				}				
			}	
			
			transaccionHome.clearInstance();
			
			tarjetahabiente = "";
			sugestion = "";
			tarjetafin = "";
			
			transaccionHome.getInstance().setFechatx(new Date());
			
			this.baucher1="";
			this.baucher2="";
			this.baucher3="";
			this.valor1="";
			this.valor2="";
			this.valor3="";				
			
			return "persisted";
			
		}else{
			facesMessages.addToControl("fechatx", "No hay una tasa de dolar asociada para esta fecha");
			this.reportarError("No hay una tasa de dolar asociada para esta fecha");
			return null;
		}
	//}catch(Exception e){
	//	facesMessages.add("El sistema ha intentado ejecutar una operacion que puede provocar errores, verifique los mensajes de error generados ");
	//	facesMessages.add(e.getMessage());
	//	return null;
	//}		
		
	}// fin del metodo guardarTransaccion
	
	
	
	private Boolean transaccionvoz = false;
	
	
	public Boolean getTransaccionvoz() {
		return transaccionvoz;
	}

	public void setTransaccionvoz(Boolean transaccionvoz) {
		this.transaccionvoz = transaccionvoz;
	}

	
	public void generarTransaccion(int consecutivo){
		//AutovozHome a = new AutovozHome();
		autovozHome.setAutovozConsecutivo(consecutivo);
		
		establecimientoHome.setEstablecimientoCodigounico(autovozHome.getInstance().getEstablecimiento().getCodigounico());
		
		tarjetaHome.setTarjetaNumerotarjeta(autovozHome.getInstance().getTarjeta().getNumerotarjeta());
		
		transaccionHome.getInstance().setFechatx(autovozHome.getInstance().getFechatx());
		
		transaccionHome.getInstance().setTipotx("I");
		
		transaccionHome.getInstance().setValortxpesos(autovozHome.getInstance().getValor());
		
		this.setBaucher1(autovozHome.getInstance().getNumautorizacion());
		
		this.setValor1(autovozHome.getInstance().getValor().toString());
		
		
		String ntarjeta = autovozHome.getInstance().getTarjeta().getNumerotarjeta();
		this.setTarjetafin(ntarjeta.substring(ntarjeta.length()-4, ntarjeta.length()));
		
		this.setSugestion(establecimientoHome.getInstance().getNombreestable());
		
		this.setTarjetahabiente(tarjetaHome.getInstance().getTarjetahabiente());
		
		transaccionvoz = true;		
	}//fin del metodo guardarTransaccion
	
	
	public void cargarTransaccion(int consecutivo){
		
		System.out.println("Num transaccion " + consecutivo );
		
		transaccionHome.setTransaccionConsecutivo(consecutivo);
		establecimientoHome.setEstablecimientoCodigounico(transaccionHome.getInstance().getEstablecimiento().getCodigounico());
		tarjetaHome.setTarjetaNumerotarjeta(transaccionHome.getInstance().getTarjeta().getNumerotarjeta());
		this.tarjetahabiente = tarjetaHome.getInstance().getTarjetahabiente();
		this.sugestion = establecimientoHome.getInstance().getNombreestable();
		String ntarjeta = tarjetaHome.getInstance().getNumerotarjeta();
		tarjetafin = ntarjeta.substring(ntarjeta.length()-4);
		
		List<Baucher> b = entityManager.createQuery("select b from Baucher b " +
				"where b.id.consecutivo = "+consecutivo+"").getResultList();
		
		if(b.size()>0){
			System.out.println("Cantidad " + b.size());
			if(b.get(0)!=null){	
				baucher1 = b.get(0).getId().getNumautorizacion();
				valor1 = b.get(0).getValor().toString();
			}
			if(b.size()>1)
			if(b.get(1)!=null){	
				baucher2 = b.get(1).getId().getNumautorizacion();
				valor2 = b.get(1).getValor().toString();
				if(b.size()>2)
					if(b.get(2)!=null){	
						baucher3 = b.get(2).getId().getNumautorizacion();
						valor3 = b.get(2).getValor().toString();
					}
			}
			
			String promotorTx = transaccionHome.getInstance().getPromotor();
			if( promotorTx != null){
				Promotor pr = entityManager.find(Promotor.class, promotorTx);
				this.nombrePromotor = pr.getPersonal().getNombre()+" "+pr.getPersonal().getApellido();
			}
		}
		
		
		
	}
	
	
	public void actualizarTransaccion()
	{
		//no se implementa el metodo se deben eliminar las Tx que deban ser
		//actualizadas.
		
	}
	
	@End
	public String eliminarTransaccion()
	{		
		//proceso eliminar Tx
		entityManager.createQuery("delete from Baucher b " +
			"where b.id.consecutivo = " + transaccionHome.getInstance().getConsecutivo()+"")
			.executeUpdate();			
		
		List<Tarjetaviaje> ltv = entityManager.createQuery("select t from Tarjetaviaje t " +
				"where t.id.numerotarjeta = '"+transaccionHome.getInstance().getTarjeta().getNumerotarjeta()+"'").getResultList();
		/*
		if (ltv.size()>0){
			Viaje v = entityManager.find(Viaje.class, ltv.get(0).getViaje().getConsecutivo());
			if (transaccionHome.getInstance().getTipotx().contentEquals("V")){
				v.setCupoviajero(v.getCupoviajero()+transaccionHome.getInstance().getValortxdolares().intValue());
			}else if (transaccionHome.getInstance().getTipotx().contentEquals("I")){
				v.setCupointernet(v.getCupoviajero()+transaccionHome.getInstance().getValortxdolares().intValue());
			}
		}
		*/		
		entityManager.createNativeQuery("delete from public.autovoz " +
			"where numtransaccion = "+transaccionHome.getInstance()
			.getConsecutivo()+"").executeUpdate();
		
		//auditoria del usuario
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy");
		DecimalFormat dec = new DecimalFormat("###,###.##");		
		AdministrarUsuario.auditarUsuario(21, "Elimino Transaccion de la tarjeta: " +
				transaccionHome.getInstance().getTarjeta().getNumerotarjeta() +
				" de la fecha: " + sdf.format(transaccionHome.getInstance().getFechatx()) +
				" por valor de: " + dec.format( transaccionHome.getInstance().getValortxpesos()) +
				" factura nro: " + transaccionHome.getInstance().getNumfactura() +
				" establecimiento: " + transaccionHome.getInstance().getEstablecimiento().getCodigounico()  +
				" del promotor: " + transaccionHome.getInstance().getPromotor());
		//
		entityManager.flush();
		transaccionHome.remove();
		
		Expressions expressions = new Expressions();
		transaccionHome.setDeletedMessage(
				expressions.createValueExpression("La transaccion de la tarjeta ************" + tarjetafin + " se ha eliminado"));
		
		entityManager.clear();		
		return "removed";
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
	
	/* Requerimiento de diversos baucher por transaccion
	 */
	
	public String baucher1="";
	public String baucher2="";
	public String baucher3="";
	
	public String valor1="";
	public String valor2="";
	public String valor3="";
	
	public Boolean fraccion = false; 

	public String getBaucher1() {
		return baucher1;
	}

	public void setBaucher1(String baucher1) {
		this.baucher1 = baucher1;
	}

	public String getBaucher2() {
		return baucher2;
	}

	public void setBaucher2(String baucher2) {
		this.baucher2 = baucher2;
	}

	public String getBaucher3() {
		return baucher3;
	}

	public void setBaucher3(String baucher3) {
		this.baucher3 = baucher3;
	}

	public String getValor1() {
		return valor1;
	}

	public void setValor1(String valor1) {
		this.valor1 = valor1;
	}

	public String getValor2() {
		return valor2;
	}

	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

	public String getValor3() {
		return valor3;
	}

	public void setValor3(String valor3) {
		this.valor3 = valor3;
	}

	public Boolean getFraccion() {
		return fraccion;
	}

	public void setFraccion(Boolean fraccion) {
		this.fraccion = fraccion;
	}
	
	String asesorTarjeta = "";	
	
	public String getAsesorTarjeta() 
	{		
		return asesorTarjeta;		
	}
	
	public void ubicarAsesor( String nroTc)
	{				
		System.out.println("Numero TC>> " + nroTc);
		
		if( nroTc != null ){
			asesorTarjeta = (String) entityManager.createNativeQuery( "SELECT" + 
                    " public.personal.nombre" +
                    " FROM public.tarjeta" +
                    " INNER JOIN public.promotor ON (public.tarjeta.documento = public.promotor.documento)" +
                    " INNER JOIN public.personal ON (public.promotor.asesor = public.personal.documento)" +
                    " WHERE  public.tarjeta.numerotarjeta = '" + nroTc + "'").getSingleResult();			
		}
	}

	public void setAsesorTarjeta(String asesorTarjeta) {
		this.asesorTarjeta = asesorTarjeta;
	}
	
	
	@Deprecated
	/**
	 * Parche para las transacciones Provincial que pasen
	 * por establecimientos de Colombia solo cambia el porcentaje de la comision
	 * @param Consecutivo de la Tx
	 */
	public int patchProvincialColombia( BigInteger conse){

		int rows = 0;
		rows = entityManager.createNativeQuery(
		"UPDATE public.transaccion " +
		"SET porcentajecomision = 12,  valorcomision = " +
		"round(  (transaccion.valortxdolares * 1600 ) * (100 - 12)/100  ) "+
		"FROM "+
		  "public.transaccion AS tx "+
		  "INNER JOIN public.tarjeta ON (tx.numerotarjeta = public.tarjeta.numerotarjeta) " +
		  "INNER JOIN public.promotor ON (public.tarjeta.documento = public.promotor.documento) " +
		  "INNER JOIN public.establecimiento ON (tx.codigounico = public.establecimiento.codigounico) " +
		"WHERE " +  
		  "tx.consecutivo = '" + conse +"' AND " +   
		  //"tx.tipotx = 'V' AND " + 
		  "tarjeta.bancoemisor = 'PRO' AND "+
		  "tx.consecutivo = public.transaccion.consecutivo").executeUpdate();
		return rows;		
	}//fin del parche
	
	
	@Deprecated
	/**
	 * Parche para las transacciones Provincial que pasen
	 * por establciemientos de otros paises 
	 * @param Consecutivo de la Tx
	 */
	public int patchProvincialOtrosPaises( BigInteger conse){
		
		int rows = 0;
		rows = entityManager.createNativeQuery(
				"UPDATE public.transaccion " + 
				"SET " +    
				
				  "valortxpesos = round( transaccion.valortxdolares * 1600 ), " + 
				  "porcentajecomision = 12, " +                          
				  "valorcomision = round(  (transaccion.valortxdolares * 1600 ) * (100 - 12)/100  ) " + 
				"FROM " + 
				  "public.transaccion AS tx " + 
				  "INNER JOIN public.tarjeta ON (tx.numerotarjeta = public.tarjeta.numerotarjeta) " +
				  "INNER JOIN public.promotor ON (public.tarjeta.documento = public.promotor.documento) " +
				  "INNER JOIN public.establecimiento ON (tx.codigounico = public.establecimiento.codigounico) " +
				"WHERE " +  
				  //"tx.fechatx = CURRENT_DATE AND " +   
				  "tarjeta.bancoemisor = 'PRO'	AND " +
				  "transaccion.consecutivo = '" + conse + "' AND " +
				  "tx.consecutivo = public.transaccion.consecutivo" ).executeUpdate();
		return rows;
		
	}//fin del parche
	
	
	@Deprecated
	/**
	 * Parche para las transacciones Provincial que pasen
	 * por el establecimiento Deposito De Drogas Continental
	 * para un promotor especificos
	 * @param Consecutivo de la Tx
	 */
	public int patchProvincialPromotor( BigInteger conse){

		int rows = 0;
		rows = entityManager.createNativeQuery(
				"UPDATE public.transaccion " + 
				"SET " +    
				  "valortxpesos = round( transaccion.valortxdolares * 1600 ), " + 
				  "porcentajecomision = 11, " +                          
				  "valorcomision = round(  (transaccion.valortxdolares * 1600 ) * (100 - 11)/100  ) " + 
				"FROM " + 
				  "public.transaccion AS tx " + 
				  "INNER JOIN public.tarjeta ON (tx.numerotarjeta = public.tarjeta.numerotarjeta) " +
				  "INNER JOIN public.promotor ON (public.tarjeta.documento = public.promotor.documento) " +
				  "INNER JOIN public.establecimiento ON (tx.codigounico = public.establecimiento.codigounico) " +
				"WHERE " +  
				  //"tx.fechatx = CURRENT_DATE AND " +   
				  "tarjeta.bancoemisor = 'PRO'	AND " +
				  "transaccion.consecutivo = '" + conse + "' AND " +
		"tx.consecutivo = public.transaccion.consecutivo" ).executeUpdate();
		return rows;
	}//fin del parche
	
	
}// fin de la clase AdministrarTransaccion
