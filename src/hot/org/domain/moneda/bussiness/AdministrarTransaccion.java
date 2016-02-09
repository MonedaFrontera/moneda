package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.domain.moneda.entity.Establecimientoprecio;
import org.domain.moneda.entity.EstablecimientoprecioId;
import org.domain.moneda.entity.Franquicia;
import org.domain.moneda.entity.Gravamenestablecimiento;
import org.domain.moneda.entity.Pais;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Porcentajecomisiontx;
import org.domain.moneda.entity.Porcentajecomisiontxparam;
import org.domain.moneda.entity.Porcentcomisiontxparampromo;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Promotorcomisiontx;
import org.domain.moneda.entity.Promotorfranquicia;
import org.domain.moneda.entity.Promotortasa;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tarjetaviaje;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.Tasadolarparametro;
import org.domain.moneda.entity.Tasadolarpromotorparametro;
import org.domain.moneda.entity.Tasaeuroparametro;
import org.domain.moneda.entity.Tasaeuropromotorparametro;
import org.domain.moneda.entity.Transaccion;
import org.domain.moneda.entity.Viaje;
import org.domain.moneda.session.AutovozHome;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TransaccionHome;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.Reporteador;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
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
	
	private String nombrePromotorActual;

	
	public AdministrarTransaccion  () {
		
	}
	
	public String getNombrePromotor() {
		return nombrePromotor;
	}


	public void setNombrePromotor(String nombrePromotor) {
		this.nombrePromotor = nombrePromotor;
	}
	
	public String getNombrePromotorActual() {
		return nombrePromotorActual;
	}


	public void setNombrePromotorActual(String nombrePromotorActual) {
		this.nombrePromotorActual= nombrePromotorActual;
	}
	
	
	public void ubicarPromotor(){
		System.out.println("Entra por primera vez: +++++++"+transaccionHome.isManaged());;
		Personal pr = CargarObjetos.ubicarPersonal(this.nombrePromotor);
		System.out.println("Nombre " + this.nombrePromotor
				+"++++ is managed "+transaccionHome.isManaged());
		//System.out.println("Doc " + pr.getDocumento());
		
		
		if(pr!=null){
			
			transaccionHome.getInstance().setPromotor(pr.getDocumento());
		}
				
		System.out.println("-------------------"+transaccionHome.getInstance().getPromotor()+
				"tarjeta:"+transaccionHome.getInstance().getTarjeta().getNumerotarjeta()+"****"+
				"num factura: "+transaccionHome.getInstance().getNumfactura()+
				"tipo tx:  "+transaccionHome.getInstance().getTipotx()+
				" estableciomiento: "+transaccionHome.getInstance().getEstablecimiento().getNombreestable()+
				"********* is maganed "+transaccionHome.isManaged());
	}
	
	/**
	 * Nueva version del metodo autocompletar. Comparacion basada en 
	 * expresiones regulares.
	 * @param nom
	 * @return
	 */
	public List<String> autocompletarPromotor(Object nom)
	{
		System.out.println(" is managed autocompletar: "+transaccionHome.isManaged());
		
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
	 * Ej: El nombre "Manuel Ricardo Perez Santos" se puede buscar 
	 * --> "Manuel Santos" ó "Manuel Perez"  ó "Manu Per"; cualquiera 
	 * de estos patrones puden usarse para busacar un nombre; o el que el 
	 * usuario elija guardando el orden de las palabras.
	 * 
	 * * http://docs.oracle.com/javase/tutorial/essential/regex/intro.html
	 * 
	 * @param suggest 
	 * @return List<String> nombres encontrados
	 */
	public List<String> autocompletarHabiente(Object suggest) {
		if (tarjetafin!=null){
			long t1 = System.currentTimeMillis();//me permite medir el tiempor de la busqueda
			if (tarjetafin.length() == 4){
				//llenarHabiente();
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
		
//		for( String s: resultList)
//			System.out.println( ">>" +s );
		listaHabiente = resultList;
	}
	
	
	public void verificarTarjeta(){
		entityManager.clear();
		//Valida que exista tarjeta(s) con ese final de numero
		List<Tarjeta> t = (ArrayList)entityManager
		.createQuery("select t from Tarjeta t where t.numerotarjeta like '%"+this.tarjetafin+"'").getResultList();
		
		//Si el resultado del arregrlo es de un solo elemento se establece esa tarjeta
		if (t.size() == 1) {
			this.tarjetahabiente = t.get(0).getTarjetahabiente();
			Tarjeta ta = t.get(0);		
			tarjetaHome.setTarjetaNumerotarjeta(ta.getNumerotarjeta());
			tarjetaHome.setInstance(ta);
		}
		//Se crea la coleccion con tarjetabiente con ese final de nro de tarjeta
		llenarHabiente();
	}
	
	
	public void ubicarEstablecimiento()
	{
		entityManager.clear();
		
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable) = " +
				"trim('"+this.sugestion+"')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 
			
			String factura = null;
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
//		llenarEstablcimiento();
	}
	
	
	
	public void ubicarFactura(){
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
	 *	Procesa el registro de una transaccion Version 1.
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
					//Reporteador.generarFactura(numerotransaccion);
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
	
	
	
	/**
	 *	Procesa el registro de una transaccion Version 2.
	 *	Nuevo modelo de liquidacion de Transacciones 2015
	 *	 
	 */
	public String guardarTransaccionFinal(){
		
		//Campos de la tabla Transaccion
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		Integer numerotransaccion = 0;
		int vControl = 0;//variable de control
		
		Pais pais = establecimientoHome.getInstance().getPais();
		//Se trae el pormotor desde la base para evitar excepciones tipo  org.hibernate.LazyInitializationException
		Promotor promotor =  entityManager.find( Promotor.class, tarjetaHome.getInstance().getPromotor().getDocumento());
		Establecimiento establecimiento = establecimientoHome.getInstance();
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		
		//Variables del TransaccionHome
		transaccionHome.getInstance().setEstablecimiento(establecimientoHome.getInstance());
		transaccionHome.getInstance().setTarjeta(tarjetaHome.getInstance());

		
		
		//Validacion de datos de la transaccion
		
		//valida que exista un nro de TC
		if(tarjetaHome.getInstance().getTarjetahabiente()==null){
			facesMessages.addToControl("nametarjeta", "No hay una tarjeta valida asignado a la transaccion");
			vControl++;			
		}else{
			//valida que la tarjeta cuente con cedula
			if( tarjetaHome.getInstance().getCedulatarjetahabiente()==null ||
				tarjetaHome.getInstance().getCedulatarjetahabiente().contentEquals("")){
				facesMessages.addToControl("nametarjeta",
						"No hay una cedula asignada al tarjetahabiente de esta tarjeta");
				this.reportarError("No hay una cedula asignada al tarjetahabiente");
				vControl++;				
			}
		}		
		
		//valida que el promotor tenga el  % de comision para viajero si la Tx es por Colombia 
		if(pais.getCodigopais().equals("CO")){			
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
			vControl++;
		}
		
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
			vControl++;
			return null;
		}
		
		//recive los valores de la Tx de los 3 voucher si los hay para la Tx
		//si el comercio es colombiano graba pesos sino graba dolares o euros
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
		
		
		//Busca la tasa de Dolar o el Euro segun la moneda correspondiente al pais del
		//establecimiento para la transaccion teniendo encuenta 
		//los niveles de precedencia contenidos en el cuadro 
		// http://www.monedafrontera.com:8080/tech/precedencias.jsp
		
		
		Float tasaDolar = 0f;
		Float tasaEuro = 0f;
		String moneda = null;
		
		moneda = transaccionHome.getInstance().getEstablecimiento().
									getPais().getPaisiso().getCodigomoneda();
		if( moneda != null ){
			if( moneda.equals("EUR") ){
				//Busca si tiene EURO negociado para el promotor
				//Esta primera busqueda es de inspeccion para identificar si el promotor tiene tasa negociada de Euro
				String queryString = " select t from Tasaeuropromotorparametro t where " +
						"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
						"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
						"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
						"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) ";
				
				List< Tasaeuropromotorparametro > listEPromo  = 
									entityManager.createQuery(queryString).getResultList();
				
				if( !listEPromo.isEmpty() ){//Se hace la busqueda del Euro
					System.out.println(" 1. Ingrese al if de NEGOCIADO DEL EURO	.....");
					tasaEuro = this.getTasaEuroPromotor();						
					// si hay tasa negociada para el pais actual pero no coincide ningun parametro se
					// direcciona a busaqueda de tasa global
					if( tasaEuro == 0f ){
						System.out.println(" 2. Ingrese al if EURO GLOBAL DESDE EL NEGOCIADO DEL EURO	.....");
						tasaEuro = this.getTasaEuroGlobal();}		
					
					// En caso de no haber tasa de Euro Global se emite un mensaje en la interfaz
					// Esto sucede por que el establecimiento no tiene tasa de euro grabada
					if (tasaEuro == 0f) {
						System.out.println("3. TASA EURO 0 MANEJANDO ERROR	.....");
						facesMessages.addToControl("fechatx", "No hay una tasa de Euro asociada " +
								"para esta fecha y/o para este establecimiento");
						this.reportarError("No hay una tasa de Euro asociada para esta fecha");
						
						return null; 
					}
				}else{//Se hace la busqueda para el Euro Global
					System.out.println("4. BUSCANDO EURO GLOBAL");
					
					tasaEuro = this.getTasaEuroGlobal();
					
					// En caso de no tener ninguna tasa de Euro global para este dia 
					// se muestra un mensaje al usuario
					if (tasaEuro == 0f) {
						System.out.println("5. TASA EURO NO ENCONTRADA	.....");
						facesMessages.addToControl("fechatx", "No hay una tasa de Euro asociada " +
								"para esta fecha y/o para este establecimiento");
						this.reportarError("No hay una tasa de Euro asociada para esta fecha");
						
						return null;
					}	 
				}
			//fin del if Euro
			}else{
				// Busca la tasa del DOLAR para el caso del dolar
				//Busca si tiene Dolar negociado para el promotor
				//Esta primera busqueda es de inspeccion para identificar si el promotor tiene tasa negociada de Euro
				String queryString = " select t from Tasadolarpromotorparametro t where " +
						"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
						"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
						"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
						"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) ";
				
				List< Tasadolarpromotorparametro > listEPromo  = 
									entityManager.createQuery(queryString).getResultList();
				
				if( !listEPromo.isEmpty() ){//Se hace la busqueda del Dolar
					System.out.println(" 1. Ingrese al if de NEGOCIADO DEL DOLAR.....");
					tasaDolar = this.getTasaDolarPromotor();					
					// si hay tasa negociada para el pais actual pero no coincide ningun parametro se
					// direcciona a busaqueda de tasa Dolar global
					if( tasaDolar == 0f  ){ 
						if(pais.getCodigopais().equals("CO")){//Tasa de dolar para Colombia
							System.out.println("2. BUSQUEDA DE TASA DOLAR PARA COLOMBIA	.....");
							Tasadolar td = (Tasadolar) entityManager.createQuery("select td from Tasadolar td " +
									"where td.id.codigopais = '" + pais.getCodigopais() + "' and " +
									" td.id.fecha = '"+ transaccionHome.getInstance().getFechatx()+"'")
									.getSingleResult();
							tasaDolar = td.getTasa().floatValue();							
						}else{
							System.out.println("3. BUSQUEDA DE TASA DOLAR GLOBAL OTROS PAISES.....");
							tasaDolar = this.getTasaDolarGlobal();
						}	
					}	
					// En caso de no haber tasa de Dolar Global se emite un mensaje en la interfaz
					// Esto sucede por que el establecimiento no tiene tasa de Dolar grabada
					if (tasaDolar == 0f) {
						System.out.println("4. TASA DOLAR 0 MANEJANDO ERROR.....");
						facesMessages.addToControl("fechatx", "No hay una tasa de Dolar asociada " +
								"para esta fecha y/o para este establecimiento");
						this.reportarError("No hay una tasa de Dolar asociada para esta fecha");
						
						return null; 
					}
				}else{//Se hace la busqueda para el Dolar Global
					System.out.println("5. BUSQUEDA EN TASA DE DOLAR GLOBAL EN ELSE.....");
					if(pais.getCodigopais().equals("CO")){//Tasa de dolar para Colombia
						System.out.println("6. BUSQUEDA EN TASA DE DOLAR PARA COLOMBIA.....");
						Tasadolar td = (Tasadolar) entityManager.createQuery("select td from Tasadolar td " +
								"where td.id.codigopais = '" + pais.getCodigopais() + "' and " +
								" td.id.fecha = '"+ sdf.format(transaccionHome.getInstance().getFechatx())+"'")
								.getSingleResult();
						tasaDolar = td.getTasa().floatValue();
					}else{
						System.out.println("7. BUSQUEDA EN TASA DE DOLAR GLOBAL.....");
						if( tasaDolar == 0f ){ tasaDolar = this.getTasaDolarGlobal();}	
					}
					if( tasaDolar == 0f ){
						System.out.println("8. TASA DOLAR 0 MANEJANDO ERROR.....");
						facesMessages.addToControl("fechatx", "No hay una tasa de dolar asociada para esta fecha");
						this.reportarError("No hay una tasa de dolar asociada para esta fecha");
						return null;	
					}
				}
			}
			System.out.println("TASA DE MONEDA ASIGNADA: " +
									( moneda.equals("EUR") ? tasaEuro : tasaDolar));
		}//fin if establecer tasa Dolar -  Euro
		
		//LIQUIDA la TX a pesos, de Euros a dolares dependiendo de la moneda del pais		
		System.out.println("LIQUIDANDO LA TRANSACCION");
		
		
		//TX en EUROS
		if( moneda.equals("EUR")){
			System.out.println("DE EUROS A PESOS..");
			//liquida de Euros a Pesos y de Euros a Dolar
			transaccionHome.getInstance().setValortxeuros( valorTX );
			float pesos = Math.round( ((100*transaccionHome.getInstance().getValortxeuros().floatValue() * tasaEuro ))/100);
			transaccionHome.getInstance().setValortxpesos(new BigDecimal(pesos));
			
			System.out.println("Pesos: " + pesos);
			System.out.println("DE EUROS A DOLARES...");
			
				// se debe obtener la paridad para el establecimiento actual
				// para liquidar de Euros a Dolares			
			EstablecimientoprecioId estprecioId = null;
			try {
				estprecioId = new EstablecimientoprecioId ( 
								establecimientoHome.getInstance().getCodigounico(), 
								sdf.parse(sdf.format(transaccionHome.getInstance().getFechatx())) );
				
			} catch (ParseException e) {
				e.printStackTrace(); 
			} 
			Establecimientoprecio establecimientoPrecio = 
							entityManager.find( Establecimientoprecio.class, estprecioId);	
			float paridad = 0f;
			if(establecimientoPrecio != null){
				paridad = establecimientoPrecio.getParidadCliente().floatValue();
			}else{
				facesMessages.addToControl("fechatx", "No existe la paridad para este establecimiento debe grabarla primero");
				return null;
				
			}
						
			float dolarFromEuro =Math.round( (100.0 * transaccionHome.getInstance().getValortxeuros().floatValue()*paridad)) / 100.0f;
			transaccionHome.getInstance().setValortxdolares(new BigDecimal(Float.toString(dolarFromEuro)));
			
			System.out.println("Paridad: " + paridad); 
			System.out.println("Dolares:" + dolarFromEuro);
		}
		
		//TX en PESOS
		if (pais.getCodigopais().contentEquals("CO") ) {
			System.out.println("DE PESOS A DOLARES (Caso Colombia)....");
			//liquida de Pesos a Dolar
			transaccionHome.getInstance().setValortxpesos( valorTX );

			//se debe cambiar, por valor con decimales
			float dolares = this.round(
					new Float(((transaccionHome.getInstance().getValortxpesos().floatValue()/tasaDolar))),2);
			
			System.out.println("Dolares>>>: " + (transaccionHome.getInstance().getValortxpesos().floatValue()/tasaDolar));

			transaccionHome.getInstance().setValortxdolares(new BigDecimal(dolares));
		}
		
		//TX en DOLARES
		if( !moneda.equals("EUR") && !pais.getCodigopais().contentEquals("CO") ){
			System.out.println("DE DOLARES A PESOS... ");
			transaccionHome.getInstance().setValortxdolares( valorTX );
			float pesos = Math.round( ( (100*transaccionHome.getInstance().getValortxdolares().floatValue()*tasaDolar))/100);
			System.out.println("Pesos: " + pesos);
			transaccionHome.getInstance().setValortxpesos(new BigDecimal(pesos));
		}
		
		//Busca el % de comision de la transaccion con base en los parametros de la Tx y 
		//los niveles de precedencia contenidos en el cuadro 
		// http://www.monedafrontera.com:8080/tech/precedencias.jsp
		Integer comision = null;//comision de la TX
		Float pcomision = 0f;//porcentaje cobrado en la TX	
		// Busca el porcentaje de comision si hay negociado
		//Esta primera busqueda es de inspeccion para identificar si el promotor tiene tasa negociada de Euro
		String queryStringComi = " select t from Porcentcomisiontxparampromo t where " +
				"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
				"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
				"t.fechainicio >= '" + transaccionHome.getInstance().getFechatx() + "' and " +
				"(t.fechafin <= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) ";		
		List< Porcentcomisiontxparampromo > listComiPromo  = 
							entityManager.createQuery(queryStringComi).getResultList();		
		if( !listComiPromo.isEmpty() ){
			pcomision = this.getPorcentajePromotor();
			if( pcomision == 0f  ){ 
				if(pais.getCodigopais().equals("CO")){//si la Tx es por Colombia se busca el % de viajero del promo
					pcomision  = promotor.getComisionviajero().floatValue();
				}else{
					pcomision = this.getPorcentajeGlobal();
				}						
			}	
			if( pcomision == 0f ){
				facesMessages.addToControl("fechatx", "No hay un porcentaje de comision asociada para esta fecha");
				this.reportarError("No hay un porcentaje de comision asociada para esta fecha");
				return null;	
			}
		}else{
			if(pais.getCodigopais().equals("CO")){//si la Tx es por Colombia se busca el % de viajero del promo
				pcomision  = promotor.getComisionviajero().floatValue();
			}else{
				pcomision = this.getPorcentajeGlobal();
			}
			if( pcomision == 0f ){
				facesMessages.addToControl("fechatx", "No hay un porcentaje de comision asociada para esta fecha");
				this.reportarError("No hay un porcentaje de comision asociada para esta fecha");
				return null;	
			}
		}
		System.out.println("PORCENTAJE DE COMISION ASIGNADA: " + pcomision);
		transaccionHome.getInstance().setPorcentajecomision(new BigDecimal(pcomision));
		System.out.println("Porcentaje Grabado:"+transaccionHome.getInstance().getPorcentajecomision());		
		comision = 
			Math.round( transaccionHome.getInstance().getValortxpesos().floatValue()*(100 - pcomision)/100 );	
		System.out.println(">>COMISION LIQUIDADA TX >> " + comision);
		
		// Porcentaje para el establecimiento
		BigDecimal porcentajeestablecimiento = BigDecimal.ZERO;
		EstablecimientoprecioId idEst = null;
		try {
			idEst = new EstablecimientoprecioId( 
					establecimientoHome.getInstance().getCodigounico(), sdf.parse(sdf.format(this.transaccionHome.getInstance().getFechatx())) );
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		System.out.println("Codigo Unico:" + idEst.getCodigounico());
		Establecimientoprecio estPrecio = entityManager.find(Establecimientoprecio.class, idEst);
		
		// Si no se ha grabado los parametros del establecimiento reenvia un mensaje a TransaccionEdit
		if (estPrecio == null ) {
			facesMessages.addToControl("fechatx", "No hay un porcentaje de comision asociada para esta fecha en el establecimiento");
			this.reportarError("No hay un porcentaje de comision asociada para esta fecha");
			return null;	
		}
		
		//Se establece el valor de la paridad para la tabla de Transaccion
		if( moneda.equals("EUR")){
			System.out.println("Estableciendo Paridad: " + estPrecio.getParidadCliente());
			transaccionHome.getInstance().setParidad(estPrecio.getParidadCliente());
		}		
		
		System.out.println(estPrecio.getPorcentajeoficina());
		if(estPrecio.getPorcentajeoficina()!=null) 
			porcentajeestablecimiento = estPrecio.getPorcentajeoficina();
		//porcentaje del asesor tabla asesor			
		BigDecimal porcentajeasesor = BigDecimal.ZERO;			
		if(promotor.getAsesor().getComision()!=null) 
			porcentajeasesor = promotor.getAsesor().getComision();
		//porcentaje si tiene arrastrador
		BigDecimal porcentajearrastrador = BigDecimal.ZERO;			
		if(promotor.getArrastrador()!=null)
			if(promotor.getComisionarrastrador()!=null){
				porcentajearrastrador = promotor.getComisionarrastrador();
		}
		
		// Se liquidan la comsiones de establecimiento, asesor, arrastrador
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
		transaccionHome.getInstance().setValorcomision(new BigDecimal(comision));						
		transaccionHome.getInstance().setEstablecimientocomision(establecimientocomision);			
		transaccionHome.getInstance().setAsesorcomision(asesorcomision);
		transaccionHome.getInstance().setArrastradorcomision(arrastradorcomision);
		
		//CONTROLES DE VALIDACION DE VIAJE
		
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
		if(promotor.getArrastrador()!=null){ 
			transaccionHome.getInstance().setArrastrador(promotor.getArrastrador().getDocumento());
		}
		transaccionHome.getInstance().setAsesor(promotor.getAsesor().getDocumento());
		transaccionHome.getInstance().setPromotor(promotor.getDocumento());
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
		
	}
	
	
	
	private Float getTasaEuroPromotor(){
		
		System.out.println("BUSCANDO TASA EURO PROMOTOR");
		
		Float tasaEuro = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Tasaeuropromotorparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Tasaeuropromotorparametro > listTasaEPromo = null ;
		List< Tasaeuropromotorparametro > listTasaEPromoFinal = null ;
					
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			listTasaEPromo = entityManager.createQuery(queryStringEuro)
					.getResultList();
			tasaEuro = (!listTasaEPromo.isEmpty()) ? listTasaEPromo.get(0)
					.getTasaeuro().floatValue() : 0f;
			System.out.println("TASA ENCONTRADA>>" + tasaEuro);

			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ((!listTasaEPromo.isEmpty() && i > flagC)|| (!listTasaEPromo.isEmpty() && i == 0 ) ) {
				flag = true;
				listTasaEPromoFinal = listTasaEPromo;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Tasaeuropromotorparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal	
		
		if( listTasaEPromoFinal != null ){ 			
			if( !listTasaEPromoFinal.isEmpty() ){
				tasaEuro = listTasaEPromoFinal.get(0).getTasaeuro().floatValue();
			}
		}else{
			tasaEuro = 0f;
		}				
		return tasaEuro;
		
	}	
	
	
	private Float getTasaEuroGlobal(){
		System.out.println("BUSCANDO TASA EURO GLOBAL");
		
		Float tasaEuro = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Tasaeuroparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Tasaeuroparametro > listTasaEuro = null ;
		List< Tasaeuroparametro > listTasaEuroFinal = null ;
					  
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			
			listTasaEuro = entityManager.createQuery(queryStringEuro).getResultList();
			//Determina el tipo de euro a asignar si es cliente TAC o normal	
			//---este if se debe eliminar es de inspeccion----
			System.out.println("Lista Vacia: " + listTasaEuro.isEmpty());
			if( !listTasaEuro.isEmpty() ){
				String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
				Promotor p = (Promotor) 
					entityManager.createQuery("from Promotor p where p.documento ='" + documento +"'").getResultList().get(0);
				System.out.println(">>>>>>>>>PROMOTOR:" + p.getTac());
				if( p.getTac() || tarjetaHome.getInstance().getTac() ){
					System.out.println("SI ES cliente TAC");
					tasaEuro = listTasaEuro.get(0).getTasaeuroTac().floatValue();
					
				}else{
					tasaEuro = listTasaEuro.get(0).getTasaeuro().floatValue();
				}
			}else{
				tasaEuro = 0f;
			}
			
			System.out.println("TASA ENCONTRADA>>" + tasaEuro);
			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ( (!listTasaEuro.isEmpty() && i > flagC)|| (!listTasaEuro.isEmpty() && i == 0 ) ) {
				
				flag = true;
				listTasaEuroFinal = listTasaEuro;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Tasaeuroparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal
		
		//Evalua si es un Promotor TAC
		//Determina el tipo de euro a asignar si es cliente TAC o normal
		if( listTasaEuroFinal != null ){
			System.out.println("Valida si es cliente TAC");			
			String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
			Promotor p = (Promotor) 
				entityManager.createQuery("from Promotor p where p.documento ='" + documento +"'").getResultList().get(0);			
			if( p.getTac() ){
				System.out.println("SI ES cliente TAC");
				tasaEuro = listTasaEuroFinal.get(0).getTasaeuroTac().floatValue();				
			}else{
				tasaEuro = listTasaEuroFinal.get(0).getTasaeuro().floatValue();
			}			
		}else{
			facesMessages.addToControl("fechatx", "No hay una tasa de Euro asociada para esta fecha");
			this.reportarError("No hay una tasa de dolar asociada para esta fecha");
			return tasaEuro;			
		}		
		return tasaEuro;
	}

	
	private Float getTasaDolarPromotor(){
		
		System.out.println("BUSCANDO TASA DOLAR PROMOTOR");
		
		Float tasaDolar = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Tasadolarpromotorparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Tasadolarpromotorparametro > listTasaDPromo = null ;
		List< Tasadolarpromotorparametro > listTasaDPromoFinal = null ;
					
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			listTasaDPromo = entityManager.createQuery(queryStringEuro)
					.getResultList();
			tasaDolar = (!listTasaDPromo.isEmpty()) ? listTasaDPromo.get(0)
						.getTasadolar().floatValue() : 0f;
			System.out.println("TASA ENCONTRADA>>" + tasaDolar);

			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ((!listTasaDPromo.isEmpty() && i > flagC)|| (!listTasaDPromo.isEmpty() && i == 0 ) ) {
				flag = true;
				listTasaDPromoFinal = listTasaDPromo;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Tasadolarpromotorparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal	
		
		if( listTasaDPromoFinal != null ){ 			
			if( !listTasaDPromoFinal.isEmpty() ){
				System.out.println("Asignando tasa de dolar:");
				tasaDolar = listTasaDPromoFinal.get(0).getTasadolar().floatValue();
			}
		}else{
			tasaDolar = 0f;
		}	
		return tasaDolar;
	}
	
	
	
	private Float getTasaDolarGlobal(){
		
		System.out.println("BUSCANDO TASA DOLAR GLOBAL");
		
		Float tasaDolar = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Tasadolarparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Tasadolarparametro > listTasaEuro = null ;
		List< Tasadolarparametro > listTasaEuroFinal = null ;
					  
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			
			listTasaEuro = entityManager.createQuery(queryStringEuro).getResultList();
			//Determina el tipo de euro a asignar si es cliente TAC o normal	
			//---este if se debe eliminar es de inspeccion----
			System.out.println("Lista Vacia: " + listTasaEuro.isEmpty());
			if( !listTasaEuro.isEmpty() ){
				String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
				Promotor p = (Promotor) 
					entityManager.createQuery("from Promotor p where p.documento ='" + documento +"'").getResultList().get(0);
				System.out.println(">>>>>>>>>PROMOTOR:" + p.getTac());
				if( p.getTac() || tarjetaHome.getInstance().getTac() ){
					System.out.println("SI ES cliente TAC");
					tasaDolar = listTasaEuro.get(0).getTasadolarTac().floatValue();
					
				}else{
					tasaDolar = listTasaEuro.get(0).getTasadolar().floatValue();
				}
			}else{
				tasaDolar = 0f;
			}
			
			System.out.println("TASA ENCONTRADA>>" + tasaDolar);
			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ( (!listTasaEuro.isEmpty() && i > flagC)|| (!listTasaEuro.isEmpty() && i == 0 ) ) {				
				flag = true;
				listTasaEuroFinal = listTasaEuro;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Tasadolarparametro t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio <= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin >= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal
		
		//Evalua si es un Promotor TAC
		//Determina el tipo de euro a asignar si es cliente TAC o normal
		if( listTasaEuroFinal != null ){
			System.out.println("Valida si es cliente TAC");			
			String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
			Promotor p = (Promotor) 
				entityManager.createQuery("from Promotor p where p.documento ='" + documento +"'").getResultList().get(0);			
			if( p.getTac() ){
				System.out.println("SI ES cliente TAC");
				tasaDolar = listTasaEuroFinal.get(0).getTasadolarTac().floatValue();				
			}else{
				tasaDolar = listTasaEuroFinal.get(0).getTasadolar().floatValue();
			}			
		}else{
			facesMessages.addToControl("fechatx", "No hay una tasa de Dolar asociada para esta fecha");
			this.reportarError("No hay una tasa de dolar asociada para esta fecha");
			return tasaDolar;			
		}		
		return tasaDolar;
	}
	
	
	
	private Float getPorcentajeGlobal(){
		
		System.out.println("BUSCANDO % COMISION GLOBAL");
		
		Float pComision = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Porcentajecomisiontxparam t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio >= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin <= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Porcentajecomisiontxparam > listPcomision = null ;
		List< Porcentajecomisiontxparam > listPcomisionFinal = null ;
					
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			
			listPcomision = entityManager.createQuery(queryStringEuro).getResultList();
			//Determina el tipo de euro a asignar si es cliente TAC o normal	
			//---este if se debe eliminar es de inspeccion----
			System.out.println("Lista Vacia: " + listPcomision.isEmpty());
			if( !listPcomision.isEmpty() ){
				String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
				Promotor p = (Promotor) entityManager.createQuery("select p from Promotor p where p.documento = '" +
						documento + "'").getSingleResult();
				if( p.getTac() ){
					System.out.println("SI ES cliente TAC");
					pComision = listPcomision.get(0).getPorcentaje().floatValue();
					
				}else{
					pComision = listPcomision.get(0).getPorcentaje().floatValue();
				}
			}else{
				pComision = 0f;
			}
			
			System.out.println("PORCENTAJE ENCONTRADO>>" + pComision);
			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ( (!listPcomision.isEmpty() && i > flagC)|| (!listPcomision.isEmpty() && i == 0 ) ) {
				
				flag = true;
				listPcomisionFinal = listPcomision;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Porcentajecomisiontxparam t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.fechainicio >= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin <= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal
		
		//Evalua si es un Promotor TAC
		//Determina el tipo de euro a asignar si es cliente TAC o normal
		if( listPcomisionFinal != null ){
			System.out.println("Valida si es cliente TAC");			
			String documento = tarjetaHome.getInstance().getPromotor().getDocumento();
			Promotor p = (Promotor) entityManager.createQuery("select p from Promotor p where p.documento = '" +
					documento + "'").getSingleResult();			
			if( p.getTac() ){
				System.out.println("SI ES cliente TAC");
				pComision = listPcomisionFinal.get(0).getPorcentaje().floatValue();				
			}else{
				pComision = listPcomisionFinal.get(0).getPorcentaje().floatValue();
			}			
		}else{
			facesMessages.addToControl("fechatx", "No hay Porcentaje de comsion para esta fecha");
			this.reportarError("No hay Porcentaje de comsion para esta fecha");
			return null;			
		}		
		return pComision;
	}

	
	private Float getPorcentajePromotor(){
		
		System.out.println("BUSCANDO % COMISION PROMOTOR");
		
		Float porcentajePromo = 0f;
		
		Pais pais = establecimientoHome.getInstance().getPais();
		Promotor promotor = tarjetaHome.getInstance().getPromotor();
		Establecimiento establecimiento = establecimientoHome.getInstance();
		//se usaron mejor los codigos para las consultas 
		String franquicia = tarjetaHome.getInstance().getFranquicia().getCodfranquicia();
		String banco = tarjetaHome.getInstance().getBanco().getCodbanco() ;
		
		String queryStringEuro = "select t from Porcentcomisiontxparampromo t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio >= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin <= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
					 
		String est = " and t.establecimiento.codigounico = '" + establecimiento.getCodigounico() + "'  ";
		String frq = " and t.franquicia.codfranquicia = '" + franquicia + "'  " ;
		String bnc = " and t.banco.codbanco = '" + banco + "' ";
					
		List< Porcentcomisiontxparampromo > listPorcentPromo = null ;
		List< Porcentcomisiontxparampromo > listPorcentromoFinal = null ;
					
		String binario = "";
		String binarioFinal = "";
		boolean flag = false;
		int flagC = 0;
							
		int baseImpresion = 3;//base de variables para consulta dinamica
		for( int i = 0 ; i < 8 ; i++ ){
			String builder = "";
			//Construyendo el binario
			binario = Integer.toString( i, 2);			
			for( int var = 0; var < baseImpresion - binario.length() ; var++ ){
				builder += "0";
			}			
			binarioFinal = builder.toString() + binario;// binario a evaluar						
			for (int ctr = 0; ctr < binarioFinal.length(); ctr++) {// itero los digitos del numero actual
				Character c = binarioFinal.charAt(ctr);
				switch (ctr) {
				case 0:
					if (c.toString().equals("1")) {
						queryStringEuro += est;
					} else {
						queryStringEuro += " and t.establecimiento.codigounico = null ";
					}
					break;
				case 1:
					if (c.toString().equals("1")) {
						queryStringEuro += frq;
					} else {
						queryStringEuro += " and t.franquicia.codfranquicia = null  ";
					}
					break;
				case 2:
					if (c.toString().equals("1")) {
						queryStringEuro += bnc;
					} else {
						queryStringEuro += " and t.banco.codbanco = null ";
					}
					break;
				}
			}
			System.out.println("Nivel de Precedencia:" + i);
			listPorcentPromo = entityManager.createQuery(queryStringEuro)
					.getResultList();
			porcentajePromo = (!listPorcentPromo.isEmpty() ) ? listPorcentPromo.get(0).getPorcentaje().floatValue() : 0f;
			System.out.println("PORCENTAJE ENCONTRADO>>" + porcentajePromo);

			// Aca Evalua cual tasa es la que se debe utilizar con base
			// en los niveles de precedencia.
			if ((!listPorcentPromo.isEmpty() && i > flagC)|| (!listPorcentPromo.isEmpty() && i == 0 ) ) {
				flag = true;
				listPorcentromoFinal = listPorcentPromo;
				flagC = i;
			}
			//Reinicio variables para el ciclo
			builder = null;
				//reinicio de consulta base.
			queryStringEuro = "select t from Porcentcomisiontxparampromo t where " +
					"t.pais.codigopais = '" + pais.getCodigopais() + "' and " +
					"t.promotor.documento = '" + promotor.getDocumento() + "' and " +
					"t.fechainicio >= '" + transaccionHome.getInstance().getFechatx() + "' and " +
					"(t.fechafin <= '" + transaccionHome.getInstance().getFechatx() + "' or t.fechafin is null ) and " +
					"t.tipocupo = '" + transaccionHome.getInstance().getTipotx() + "' " ;
		}// fin del for principal	
		
		if( listPorcentromoFinal != null ){ 			
			if( !listPorcentromoFinal.isEmpty() ){
				System.out.println("Asignando porcentaje negociado:");
				porcentajePromo = listPorcentromoFinal.get(0).getPorcentaje().floatValue();
			}
		}else{
			porcentajePromo = 0f;
		}	
		return porcentajePromo;
	}
	
	
	
	
	
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
	}
	
	
	
		

	public void cargarTransaccion(int consecutivo){

		System.out.println("Num transaccion " + consecutivo );
		
	
		this.nombrePromotor="";
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
				this.nombrePromotorActual = pr.getPersonal().getNombre()+" "+pr.getPersonal().getApellido();
			}
		}
		
		System.out.println(" is managed autocompletar2: "+transaccionHome.isManaged());
		
	}
	
	
	public String actualizarTransaccion()
	{
		entityManager.clear();
		
		System.out.println("entra a actualizar");
		System.out.println("actualiza:*******"+this.transaccionHome.getInstance().getPromotor());

		//actualiza el promotor en la tabla tarjeta
		String sql = "update public.transaccion " +
				"set promotor = '"+transaccionHome.getInstance().getPromotor()+"' where " +
				" consecutivo = '"+transaccionHome.getInstance().getConsecutivo()+"'";
		entityManager.createNativeQuery(sql).executeUpdate();
		entityManager.clear();
		entityManager.flush();
		
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy");
		DecimalFormat dec = new DecimalFormat("###,###.##");
		AdministrarUsuario.auditarUsuario(39, "Se cambió el promotor:"+this.nombrePromotor+
										" por el promotor:"+this.nombrePromotorActual +" en la transacción:"+transaccionHome.getInstance().getConsecutivo());
		
		facesMessages.add("Se actualizó el promotor de la transacción correctamente");
		
		
	   return "updated";
		
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
	
	
	public float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	
}// fin de la clase AdministrarTransaccion






