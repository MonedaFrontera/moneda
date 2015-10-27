package org.domain.moneda.bussiness;

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
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.persistence.EntityManager;

import org.domain.moneda.entity.Cuenta;
import org.domain.moneda.entity.Gastos;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tasabolivarnegociado;
import org.domain.moneda.entity.TasabolivarnegociadoId;
import org.domain.moneda.entity.Tasadebolivaroficina;
import org.domain.moneda.entity.Tipogasto;
import org.domain.moneda.entity.Transferencia;
import org.domain.moneda.session.CuentaHome;
import org.domain.moneda.session.EnviosList;
import org.domain.moneda.session.GastosHome;
import org.domain.moneda.session.GastosList;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TransferenciaHome;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarGasto")
@Scope(ScopeType.CONVERSATION)
public class AdministrarGasto
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In Identity identity;
    
    @In
    private FacesMessages facesMessages;
    
    @In
	private EntityManager entityManager;
    
    @In(create=true) @Out 
	private GastosHome gastosHome;
    
    @In(create=true) @Out 
    private CuentaHome cuentaHome;
    
    @In(create=true) @Out 
	private PersonalHome personalHome;
    
    @In(create=true)
	private CargarObjetos CargarObjetos;
    
    List<String> lista = new ArrayList<String>();   

    @In(create=true) @Out 
    AdministrarUsuario AdministrarUsuario;

    public void administrarGasto()
    {
        // implement your business logic here
        log.info("AdministrarGasto.administrarGasto() action called");
        statusMessages.add("administrarGasto");
    }
    
    //variable que almacena el nombre del promotor
    public String nombre = "";
    
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	public String registrar(){
		try{
			BigInteger query = (BigInteger)entityManager
			.createNativeQuery("select nextval('gastos_consecutivo_seq')").getSingleResult();
	    	gastosHome.getInstance().setConsecutivo(query.intValue());
	    	Expressions expressions = new Expressions();
	    	
	    	gastosHome.setCreatedMessage(
	    			expressions.createValueExpression("Se ha registrado " +
	    					"un gasto a nombre de " +this.nombre+ " por un " +
	    							"valor de " + gastosHome.getInstance().getValor()));
			
			gastosHome.persist();
			gastosHome.clearInstance();
			
			personalHome.clearInstance();
			
			this.nombre = "";
			
			return "persisted";
		}catch(Exception e){
			facesMessages.add("El sistema ha intentado ejecutar una operacion que puede provocar errores, verifique los mensajes de error generados ");
			facesMessages.add(e.getMessage());
			return null;
		}
		
	}	
	
	
	@In(create=true) @Out 
	private GastosList gastosList;
	
	public void ubicarPromotorList()
	{    	
    	Personal pr = CargarObjetos.ubicarPersonal(this.nombre);
    	
		if(pr!=null){
			personalHome.setPersonalDocumento(pr.getDocumento());
			personalHome.setInstance(pr);
			gastosList.setNombre(pr.getNombre()+ " "+pr.getApellido());
			//enviosList.setPromotor(pr);
		}else{
			gastosList.setNombre(null);
		}	
		
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
    
    
	/**
	 * Nueva version del metodo autocompletar. Comparacion basada en 
	 * expresiones regulares.
	 * @param nom
	 * @return
	 */
	public List<String> autocompletar(Object nom)
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
	
	
/*	public List<String> autocompletar(Object nombre) {
		llenarPromotores(); 		// Metodo que carga la informacion de los nombres de las personas
		String pref = (String) nombre;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = lista.iterator();
		while (iterator.hasNext()) {			
			String elem =  iterator.next();
			if ( (elem != null && elem.toLowerCase().contains(pref.toLowerCase()))
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
		
	}
*/	
	
	
	public void ubicarPromotor(){
		Personal pr = CargarObjetos.ubicarPersonal(this.nombre);
		System.out.println("Nombre " + this.nombre);
		//System.out.println("Doc " + pr.getDocumento());
		if(pr!=null){
			personalHome.setPersonalDocumento(pr.getDocumento());
			personalHome.setInstance(pr);
			
			gastosHome.getInstance().setPersonal(personalHome.getInstance());
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
    // add additional action methods
	
	
	public void inicializar(){
		
		System.out.println("Otra vez");
		if(transferenciaHome.getInstance().getPreciobolivar()==null){
		transferenciaHome.wire();
		gastosHome.wire();
		//transferenciaHome.getInstance().setTipobolivar("OFI");
		if(!transferenciaHome.isManaged()){
			if(transferenciaHome.getInstance().getTipobolivar()==null)
			transferenciaHome.getInstance().setTipobolivar("NEG");
			if(gastosHome.getInstance().getFecha()==null)
			gastosHome.getInstance().setFecha(new Date());
		}
		traerPrecioBolivar();
		}
		//llenar(); 
	}
	
	private String cuenta = "";	
	
	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
		System.out.println("Cuenta Establecida>>" + this.cuenta);
	}
				
	//ubicarCuenta()	
	public void ubicarCuenta(){
		Cuenta ct = CargarObjetos.ubicarCuentas(this.cuenta);
		System.out.println("Nombre " + this.cuenta);
		//System.out.println("Doc " + pr.getDocumento());
		if(ct!=null){
			//personalHome.setPersonalDocumento(pr.getDocumento());
			//personalHome.setInstance(pr);
			
			cuentaHome.setCuentaNumcuenta(ct.getNumcuenta());
			cuentaHome.setInstance(ct);
			
			
			//gastosHome.getInstance().set.setPersonal(personalHome.getInstance());
		}				
	}
	
	//autocompletarCuenta()
	public List<String> autocompletarCuenta(Object nombreCta) {
		llenarCuentas(); 		// Metodo que carga la informacion de los nombres de las cuentas
		String pref = (String) nombreCta;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = lista.iterator();
		while (iterator.hasNext()) {			
			String elem =  iterator.next();
			if ( (elem != null && elem.toLowerCase().contains(pref.toLowerCase()))
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;

	}
	
	
	public void llenarCuentas(){
		entityManager.clear();
		String sql = "";		
		List<String> resultList = 
			entityManager.createQuery("select c.nombre from Cuenta c ").getResultList();
		lista = resultList;
	}	
	
	
	/* ***  Metodos para el proceso de Transferencias **/
	
	@In(create=true) @Out 
	private TransferenciaHome transferenciaHome;
	
	public String registrarTransferencia(){
		
		try {
			
			Tasabolivarnegociado tasabolivarnegociado = new Tasabolivarnegociado();
			
			TasabolivarnegociadoId tId = new TasabolivarnegociadoId(gastosHome.getInstance().getFecha(), 
					gastosHome.getInstance().getPersonal().getDocumento(), "T");
			tasabolivarnegociado.setId(tId);
			
			tasabolivarnegociado.setPreciobolivar(transferenciaHome.getInstance().getPreciobolivar());
			tasabolivarnegociado.setPreciocompra(transferenciaHome.getInstance().getPreciocompra());
			tasabolivarnegociado.setPrecioasesor(transferenciaHome.getInstance().getPrecioasesor());
			
			tasabolivarnegociado.setPromotor(personalHome.getInstance().getPromotor());
			
			entityManager.persist(tasabolivarnegociado);
			
			
			//entityManager.flush();
			
			Tipogasto tg = entityManager.find(Tipogasto.class, "TR");
			gastosHome.getInstance().setTipogasto(tg);
			/*
			gastosHome.getInstance().setValor((transferenciaHome.getInstance().getValorbolivar().longValue()
					*transferenciaHome.getInstance().getPreciobolivar().longValue()));
					*/
			BigInteger query = (BigInteger)entityManager
			.createNativeQuery("select nextval('gastos_consecutivo_seq')").getSingleResult();
	    	gastosHome.getInstance().setConsecutivo(query.intValue());
	    	Expressions expressions = new Expressions();
	    	
	    	gastosHome.setCreatedMessage(
	    			expressions.createValueExpression("Se ha registrado " +
	    					"un gasto a nombre de " +this.nombre+ " por un " +
	    							"valor de " + gastosHome.getInstance().getValor()));
			
			//gastosHome.persist();
			
			entityManager.persist(gastosHome.getInstance());
			entityManager.flush();
	    	entityManager.clear();
			
			Gastos g = entityManager.find(Gastos.class, gastosHome.getInstance().getConsecutivo());
			
			Transferencia t = new Transferencia();
			
			//t.setConsecutivo(g.getConsecutivo());
			t.setCuenta(transferenciaHome.getInstance().getCuenta());
			t.setGastos(g);
			t.setPreciobolivar(transferenciaHome.getInstance().getPreciobolivar());
			t.setTipobolivar(transferenciaHome.getInstance().getTipobolivar());
			t.setValorbolivar(transferenciaHome.getInstance().getValorbolivar());
			t.setPrecioasesor(transferenciaHome.getInstance().getPrecioasesor());
			t.setPreciocompra(transferenciaHome.getInstance().getPreciocompra());
			t.setCuentapromotor(transferenciaHome.getInstance().getCuentapromotor());
			t.setUsuariomod(this.identity.getUsername());
			t.setFechamod(new Date());
			
			entityManager.persist(t);
			entityManager.flush();
	    	entityManager.clear();
			
			//transferenciaHome.persist();
			
	    	facesMessages.add("Se ha registrado una transferencia bancaria a nombre de " + this.nombre + " por un valor " +
	    			"de B$ " + transferenciaHome.getInstance().getValorbolivar());
	    	
	    	gastosHome.clearInstance();
	    	transferenciaHome.clearInstance();
	    	personalHome.clearInstance();
	    	
	    	this.nombre = "";
	    	
			return "persisted";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		
		
		
	}

	
	public String tipoconv = "PAB";
	
	
	
	public String getTipoconv() {
		return tipoconv;
	}

	public void setTipoconv(String tipoconv) {
		this.tipoconv = tipoconv;
	}

	public void traerPrecioBolivar(){
		MathContext mc = new MathContext(0);
		System.out.println("Precio " + transferenciaHome.getInstance().getTipobolivar());
		try{
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		if(transferenciaHome.getInstance().getTipobolivar().contentEquals("NEG")){
			List<Tasabolivarnegociado> ltbn = (List<Tasabolivarnegociado>) 
			entityManager.createQuery("select tbn from Tasabolivarnegociado tbn where " +
				" tbn.id.documento = '" + personalHome.getInstance().getDocumento() + "' and " +
				" tbn.id.fecha = (select max(tbn.id.fecha) from Tasabolivarnegociado tbn " +
				" where " +
				" tbn.id.documento = '" + personalHome.getInstance().getDocumento() + "' and " +
				" to_char(tbn.id.fecha,'dd/MM/yyyy') = '" + sdf.format(gastosHome.getInstance().getFecha())  + "' " +
				" and tbn.id.tipo = 'T') " +
				" and tbn.id.tipo = 'T' order by tbn.id.fecha desc").getResultList();
			if(ltbn.size()>0){
				Tasabolivarnegociado tbn = ltbn.get(0);
				transferenciaHome.getInstance().setPreciobolivar(tbn.getPreciobolivar());
				transferenciaHome.getInstance().setPrecioasesor(tbn.getPrecioasesor());
				transferenciaHome.getInstance().setPreciocompra(tbn.getPreciocompra());
			if(this.tipoconv.contentEquals("PAB")&&gastosHome.getInstance().getValor()!=null){
					
					//System.out.println(this.tipoconv);
					
					transferenciaHome.getInstance().setValorbolivar(
							
							(new BigDecimal(gastosHome.getInstance().getValor().longValue()/
									transferenciaHome.getInstance().getPreciobolivar().floatValue()))
							
							);
					}else if(this.tipoconv.contentEquals("BAP")&&transferenciaHome.getInstance().getValorbolivar()!=null){
						gastosHome.getInstance().setValor(
								transferenciaHome.getInstance().getValorbolivar()
								.multiply(transferenciaHome.getInstance().getPreciobolivar()).longValue());
					}
				System.out.println("**********" + transferenciaHome.getInstance().getValorbolivar());
			}else{
				transferenciaHome.getInstance().setPreciobolivar(null);
				facesMessages.addToControl("preciobolivar", "No existe bolivar negociado para transferencia para este promotor " +
						"en esta fecha");
			}
			 
		}else if(transferenciaHome.getInstance().getTipobolivar().contentEquals("OFI")){
			List<Tasadebolivaroficina> ltbo = (List<Tasadebolivaroficina>) entityManager.createQuery("select tbo from Tasadebolivaroficina tbo where " +
				" tbo.id.fecha = (select max(tbo.id.fecha) from Tasadebolivaroficina tbo " +
				" where " +
				" to_char(tbo.id.fecha,'dd/MM/yyyy') = '" + sdf.format(gastosHome.getInstance().getFecha())  + "' " +
				" and tbo.id.tipo = 'T') " +
				" and tbo.id.tipo = 'T' order by tbo.id.fecha desc").getResultList();
			if(ltbo.size()>0){
				Tasadebolivaroficina tbo = ltbo.get(0);
				transferenciaHome.getInstance().setPreciobolivar(tbo.getPreciobolivar());
				transferenciaHome.getInstance().setPrecioasesor(null);
				transferenciaHome.getInstance().setPreciocompra(null);
				//transferenciaHome.getInstance().getGastos().setValor()
				
				if(this.tipoconv.contentEquals("PAB")&&gastosHome.getInstance().getValor()!=null){
					
					//System.out.println(this.tipoconv);
					transferenciaHome.getInstance().setValorbolivar(
							
							new BigDecimal(gastosHome.getInstance().getValor().longValue()/
							transferenciaHome.getInstance().getPreciobolivar().floatValue())
							
							
							);
					}else if(this.tipoconv.contentEquals("BAP")&&transferenciaHome.getInstance().getValorbolivar()!=null){
						gastosHome.getInstance().setValor(
								transferenciaHome.getInstance().getValorbolivar()
								.multiply(transferenciaHome.getInstance().getPreciobolivar()).longValue());
					}
			}else{
				transferenciaHome.getInstance().setPreciobolivar(null);
				facesMessages.addToControl("preciobolivar", "No existe bolivar de oficina para transferencia para esta fecha");
			}
		}
		}catch(Exception e){
			facesMessages.add("Se ha generado un error, verifique la informacion: " + e.toString());
		}
		
	}
	
	
	public void cambiarTasa(BigDecimal t){
		System.out.println("Cambiar Tasa");
		transferenciaHome.getInstance().setPreciobolivar(t);
		System.out.println("Cambiar Tasa2");
		traerTotalBolivar();
		System.out.println("Cambiar Tasa3");
	}
	
	public void cambiarPreciocompra(BigDecimal t){
		System.out.println("Cambiar Tasa");
		transferenciaHome.getInstance().setPreciocompra(t);
		
	}
	
	public void cambiarPrecioasesor(BigDecimal t){
		System.out.println("Cambiar Tasa");
		transferenciaHome.getInstance().setPrecioasesor(t);
	
	}
	
	public void traerTotalBolivar(){
		System.out.println("NEG Total Bolivar " + transferenciaHome.getInstance().getPreciobolivar());
		MathContext mc = new MathContext(0);
		System.out.println("Precio " + transferenciaHome.getInstance().getTipobolivar());
		try{
			
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("P1");
		
		if(this.tipoconv.contentEquals("PAB")&&gastosHome.getInstance().getValor()!=null){
			
			//System.out.println(this.tipoconv);
			System.out.println("P2 PAB: "+(new BigDecimal(gastosHome.getInstance().getValor().longValue()/
					transferenciaHome.getInstance().getPreciobolivar().floatValue())));
			transferenciaHome.getInstance().setValorbolivar(
					
					(new BigDecimal(gastosHome.getInstance().getValor().longValue()/
							transferenciaHome.getInstance().getPreciobolivar().floatValue())));
			}else if(this.tipoconv.contentEquals("BAP")&&transferenciaHome.getInstance().getValorbolivar()!=null){
				long pesos = 
					transferenciaHome.getInstance().getValorbolivar()
					.multiply(transferenciaHome.getInstance().getPreciobolivar()).longValue();
				
				gastosHome.getInstance().setValor(
						transferenciaHome.getInstance().getValorbolivar()
						.multiply(transferenciaHome.getInstance().getPreciobolivar()).longValue());
				System.out.println("P3 BAP: " + pesos);
				}
		
		System.out.println("P4");
		}catch(Exception e){
			facesMessages.add("Se ha generado un error, verifique la informacion: " + e.toString());
		}
		
	}
	
	List<Gastos> listarGastos = new ArrayList<Gastos>();
	
	List<Transferencia> listarTransferencias = new ArrayList<Transferencia>();

	public List<Gastos> getListarGastos() {
		return listarGastos;
	}

	public void setListarGastos(List<Gastos> listarGastos) {
		this.listarGastos = listarGastos;
	}

	public List<Transferencia> getListarTransferencias() {
		return listarTransferencias;
	}

	public void setListarTransferencias(List<Transferencia> listarTransferencias) {
		this.listarTransferencias = listarTransferencias;
	}
	
	BigDecimal totalgastos = BigDecimal.ZERO;
	
	public BigDecimal getTotalgastos() {
		return totalgastos;
	}

	public void setTotalgastos(BigDecimal totalgastos) {
		this.totalgastos = totalgastos;
	}

	public void buscarGastos(){ 
		
		String sqlsuma = "select sum(g.valor) from Gastos g where 1 = 1 ";
		
		String sql = "select g from Gastos g where 1 = 1 ";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		System.out.println("Documento " + this.personalHome.getInstance().getDocumento());
		if(this.nombre != null && !this.nombre.contentEquals("")){
			sql = sql + " and trim(g.personal.nombre)||' '||trim(g.personal.apellido)='"+this.nombre+"'";
			sqlsuma = sqlsuma + " and trim(g.personal.nombre)||' '||trim(g.personal.apellido)='"+this.nombre+"'";
		}
		
		System.out.println("Fecha Fin "+this.fechafin);
		
		if(this.gastosHome.getInstance().getFecha() != null){
			if(this.fechafin==null){
			sql = sql + " and g.fecha = '"+sdf.format(this.gastosHome.getInstance().getFecha())+"'";
			sqlsuma = sqlsuma + " and g.fecha = '"+sdf.format(this.gastosHome.getInstance().getFecha())+"'";
			}else{
				sql = sql + " and g.fecha >= '"+sdf.format(this.gastosHome.getInstance().getFecha())+"'";
				sqlsuma = sqlsuma + " and g.fecha >= '"+sdf.format(this.gastosHome.getInstance().getFecha())+"'";
			}
				
		}
		
		if(this.fechafin!=null){
			sql = sql + " and g.fecha <= '"+sdf.format(this.fechafin)+"'";
			sqlsuma = sqlsuma + " and g.fecha <= '"+sdf.format(this.fechafin)+"'";
		}	
		
		
		if(this.gastosHome.getInstance().getTipogasto()!=null){
			sql = sql + " and g.tipogasto.codtipogasto = '"+this.gastosHome.getInstance().getTipogasto().getCodtipogasto()+"'";
			sqlsuma = sqlsuma + " and g.tipogasto.codtipogasto = '"+this.gastosHome.getInstance().getTipogasto().getCodtipogasto()+"'";
		}
		this.listarGastos = entityManager.createQuery(sql).setMaxResults(100).getResultList();
		
		Long t = (Long) entityManager.createQuery(sqlsuma).getSingleResult();
		if (t!=null)
			this.totalgastos = new BigDecimal(t);
		//this.listarGastos = null;
	}
	
	
	private Date fechainicio;
	private Date fechafin = null;
	
	
	
	
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

	EntityQuery<Transferencia> qtransferencias = new EntityQuery<Transferencia>();
	
	
	
	public EntityQuery<Transferencia> getQtransferencias() {
		return qtransferencias;
	}

	public void setQtransferencias(EntityQuery<Transferencia> qtransferencias) {
		this.qtransferencias = qtransferencias;
	}
	
	public void limpiarTransferencia(){
		this.qtransferencias = null;
		this.nombre = "";
		this.gastosList.getGastos().setFecha(null);
	}

	/**
	 * Busca las transferecias de la interfaz TransferenciaList
	 */
	public void buscarTransferencias(){
		System.out.println("METODO BUSCAR***");
		String sql = "select transferencia from Transferencia transferencia where 1 = 1 ";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		System.out.println(sql);
		
		if (identity.hasRole("Asesor")){
			sql += " and transferencia.gastos.personal.promotor.asesor.documento ='"+this.identity.getUsername()+"' ";
			
		}
		System.out.println(sql);
		if(this.nombre != null && !this.nombre.contentEquals("")){
			sql = sql + " and trim(transferencia.gastos.personal.nombre)||' '||trim(transferencia.gastos.personal.apellido)='"+this.nombre+"'";
		}
		System.out.println(sql);
		if(this.gastosList.getGastos().getFecha() != null){
			sql = sql + " and transferencia.gastos.fecha = '"+sdf.format(this.gastosList.getGastos().getFecha())+"'";
		}		
		System.out.println(sql);
		if( this.cuenta != null && !this.cuenta.equals("")){
			sql+= " and transferencia.cuenta.nombre = '" +
					this.cuentaHome.getInstance().getNombre() +"'";
		}
		System.out.println(sql);
	
		System.out.println("Ejecutando query:");
		
		this.listarTransferencias = entityManager.createQuery(sql).setMaxResults(30).getResultList();
				
		qtransferencias.setEjbql(sql);
		
		if(qtransferencias.getResultCount()<25){
			qtransferencias.setFirstResult(0);
		}		
		
		qtransferencias.setMaxResults(80);
	
		//this.listarTransferencias = null;
	}
	
	public String eliminarGastos() 
	{		
				
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat nft = NumberFormat.getInstance();
		String tipoMovimiento = this.gastosHome.getInstance().
					getTipogasto().getTipo().equals("1")? "Credito" : "Debito";	
		
		Integer oper = (gastosHome.getInstance().getTipogasto().getCodtipogasto().equals("TR") )
								? 23 : 20;		//20 Eliminar Gasto, 23 Eliminar Deposito
		
		String enunciado = (oper == 20) ? "Elimino gasto: " : "Elimino: ";
		
		AdministrarUsuario.auditarUsuario( oper,  enunciado +  
				this.gastosHome.getInstance().getTipogasto().getDescripcion() +
				" | Promotor: " + this.gastosHome.getInstance().getPersonal().getDocumento() +
				" fecha: " + sdf.format( this.gastosHome.getInstance().getFecha() ) +
				" valor: " + nft.format( this.gastosHome.getInstance().getValor() )+// se formateo el valor
				" de tipo: " +  tipoMovimiento );
		
		gastosHome.remove();
		//entityManager.flush();				
		entityManager.clear();
		
		return "removed";
	}
}


