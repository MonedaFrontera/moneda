package org.domain.moneda.bussiness;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Banco;
import org.domain.moneda.entity.Establecimiento;
import org.domain.moneda.entity.Establecimientoprecio;
import org.domain.moneda.entity.EstablecimientoprecioId;
import org.domain.moneda.entity.Franquicia;
import org.domain.moneda.entity.Pais;
import org.domain.moneda.entity.Porcentajecomisiontxparam;
import org.domain.moneda.entity.Porcentcomisiontxparampromo;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tasabolivarnegociado;
import org.domain.moneda.entity.Tasadebolivaroficina;
import org.domain.moneda.entity.TasadebolivaroficinaId;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.TasadolarId;
import org.domain.moneda.entity.Tasadolarparametro;
import org.domain.moneda.entity.Tasadolarpromotorparametro;
import org.domain.moneda.entity.Tasaeuroparametro;
import org.domain.moneda.entity.Tasaeuropromotorparametro;
import org.domain.moneda.session.BancoHome;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.EstablecimientoprecioHome;
import org.domain.moneda.session.FranquiciaHome;
import org.domain.moneda.session.PaisHome;
import org.domain.moneda.session.PorcentajecomisiontxparamHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TasabolivarnegociadoHome;
import org.domain.moneda.session.TasadebolivaroficinaHome;
import org.domain.moneda.session.TasadolarHome;
import org.domain.moneda.session.TasadolarparametroHome;
import org.domain.moneda.session.TasadolarpromotorparametroHome;
import org.domain.moneda.session.TasaeuroparametroHome;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.EnviarMailAlertas;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("AdministrarTasa")
@Scope(ScopeType.CONVERSATION)
public class AdministrarTasa
{
    @Logger private Log log;

    @In StatusMessages statusMessages; 

    public void administrarTasa()
    {
    	// implement your business logic here
        log.info("AdministrarTasa.administrarTasa() action called");
        statusMessages.add("administrarTasa");
    } 
    
    @In private FacesMessages facesMessages;
    
    @In
	private EntityManager entityManager;
    
    @In(create=true) @Out 
    TasadolarHome tasadolarHome;
    
    @In(create=true) @Out 
    TasadebolivaroficinaHome tasadebolivaroficinaHome;
    
    @In(create=true) @Out 
    TasabolivarnegociadoHome tasabolivarnegociadoHome;
    
    List<Tasadebolivaroficina> listaTasadebolivaroficina = new ArrayList<Tasadebolivaroficina>();
    
    List<Tasadolar> listaTasadolar = new ArrayList<Tasadolar>();
    
    //Coleccion de dolar y euro para paginas List
    private List<Tasadolarparametro> tasaDolarList = new ArrayList<Tasadolarparametro>();
    private List<Tasaeuroparametro> tasaEuroList = new ArrayList<Tasaeuroparametro>();
    
    List<Tasabolivarnegociado> listaTasabolivarnegociado = new ArrayList<Tasabolivarnegociado>();
    
    @In(create=true) @Out 
	private PromotorHome promotorHome;
    
    @In(create=true) @Out
    AdministrarUsuario AdministrarUsuario;
    
	
	/************************************************
     * Entidades a ingresar en la clase de negocio	*
     ************************************************/
    
    @In(create=true) @Out 
    PaisHome paisHome;
 
    @In(create=true) @Out 
    EstablecimientoHome establecimientoHome;
    
    @In(create=true) @Out 
    FranquiciaHome franquiciaHome;
    
    @In(create=true) @Out 
    BancoHome bancoHome;
    
    @In(create=true) @Out 
    TasadolarpromotorparametroHome tasadolarpromotorparametroHome;
    
    @In(create=true) @Out 
    TasaeuroparametroHome  tasaeuroparametroHome;
   
    @In(create=true) @Out 
    TasadolarparametroHome tasadolarparametroHome;
   
    @In(create=true) @Out 
    EstablecimientoprecioHome establecimientoprecioHome;
   
    @In(create=true) @Out 
    PorcentajecomisiontxparamHome porcentajecomisiontxparamHome;
	
    private Boolean managedTasa = true;
    
    List<String> listaString = new ArrayList<String>();
    
    //Campos del formulario
    private Pais paisTemp;
    private Promotor promoTemp;
    private Establecimiento estaTemp;	  
    private Franquicia frqTemp;
    private Banco bancoTemp;
    private String tipoCupoTemp = "V";
    private Date fechaIniTemp;
    private Date fechaFinTemp;
    
    private BigDecimal tasaDolarTemp;
    private BigDecimal tasaDolarNegTemp;
    private BigDecimal tasaDolarTacTemp;
    private BigDecimal tasaDolarOfTemp;
    
    private BigDecimal tasaEuroTemp;
    private BigDecimal tasaEuroNegTemp;
    private BigDecimal tasaEuroTacTemp;
    private BigDecimal tasaEuroOfTemp;
    
    private BigDecimal porcentCt;
    private BigDecimal porcentOfi;

    private BigDecimal paridadEstTemp;
    private BigDecimal paridadClienteTemp;
    
    private String codMoneda;
    
	private Tasaeuropromotorparametro tsEuroPromo;
	private Tasaeuroparametro tsEuroParam;
	private Tasadolarpromotorparametro tsDolarPromo;		
	private Tasadolarparametro tsDolarParam;
	
	private Porcentcomisiontxparampromo porcentajePromo;
	private Porcentajecomisiontxparam porcentajeGlob;
	
	private String pathBandera;
	
	Date fecha = null;
	
	private boolean formValido;
	
	private String tipoMoneda;
    
	@In(create = true)
	@Out
	private EnviarMailAlertas enviarMailAlertas;
    
    public List<Tasadebolivaroficina> getListaTasadebolivaroficina() {
		return listaTasadebolivaroficina;
	}

	public void setListaTasadebolivaroficina(
			List<Tasadebolivaroficina> listaTasadebolivaroficina) {
		this.listaTasadebolivaroficina = listaTasadebolivaroficina;
	}
	
	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public TasaeuroparametroHome getTasaeuroparametroHome() {
		return tasaeuroparametroHome;
	}

	public void setTasaeuroparametroHome(TasaeuroparametroHome tasaeuroparametroHome) {
		this.tasaeuroparametroHome = tasaeuroparametroHome;
	}

	public TasadolarparametroHome getTasadolarparametroHome() {
		return tasadolarparametroHome;
	}

	public void setTasadolarparametroHome(
			TasadolarparametroHome tasadolarparametroHome) {
		this.tasadolarparametroHome = tasadolarparametroHome;
	}

	public EstablecimientoprecioHome getEstablecimientoprecioHome() {
		return establecimientoprecioHome;
	}

	public void setEstablecimientoprecioHome(
			EstablecimientoprecioHome establecimientoprecioHome) {
		this.establecimientoprecioHome = establecimientoprecioHome;
	}

	public PorcentajecomisiontxparamHome getPorcentajecomisiontxparamHome() {
		return porcentajecomisiontxparamHome;
	}

	public void setPorcentajecomisiontxparamHome(
			PorcentajecomisiontxparamHome porcentajecomisiontxparamHome) {
		this.porcentajecomisiontxparamHome = porcentajecomisiontxparamHome;
	}
	
	
	public Boolean getManagedTasa() {
		return managedTasa;
	}

	public void setManagedTasa(Boolean managedTasa) {
		this.managedTasa = managedTasa;
	}

	public void buscarTasaBolivarOficina(){
		String sql = "select t from Tasadebolivaroficina t where 1=1 ";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		if(this.fecha != null)
			sql = sql + " and t.id.fecha = '"+sdf.format(fecha)+"'";
		
		
		if(!this.tipo.contentEquals("")){
			sql = sql + " and t.id.tipo = '"+this.tipo+"'";
		}
    	listaTasadebolivaroficina = entityManager.createQuery(sql).setMaxResults(30).getResultList();
    }
    
    // add additional action methods
    public void guardarTasaDolar(){
    	
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
    	tasadolarHome.getInstance().getId().setCodigopais(tasadolarHome.getInstance().getPais().getCodigopais());
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		tasadolarHome.setCreatedMessage(
				expressions.createValueExpression("Se ha registrado la tasa de dolar para el " + sdf.format(tasadolarHome.getInstance().getId().getFecha()) + " de forma exitosa"));
		tasadolarHome.persist();		
		
    }
    
    
    
    public void editarTasadolar(Date fecha, String codpais){
    	
    	log.info("Edicion Tasa Dolar");
    	llenarPromotores() ;
    	
    	Tasadolar td = (Tasadolar)entityManager
		.createQuery("select t from Tasadolar t where t.id.fecha = '"+fecha+"' and t.id.codigopais = '"+codpais+"'")
		.getSingleResult();
    	
    	tasadolarHome.setTasadolarId(new TasadolarId(codpais, fecha));
    	
    	pais = td.getPais();
    }
    
    
    
    public void actualizarTasaDolar(){
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	tasadolarHome.getInstance().getId().setCodigopais(tasadolarHome.getInstance().getPais().getCodigopais());
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		tasadolarHome.setCreatedMessage(
				expressions.createValueExpression("Se ha actualizado la tasa de dolar para el " + sdf.format(tasadolarHome.getInstance().getId().getFecha()) + " de forma exitosa"));
		tasadolarHome.update();
		
		if (pais.getCodigopais().contentEquals("CO")){
				
		entityManager.createQuery("update Transaccion t " +
				"set " +
		    	"t.valortxdolares = round(t.valortxpesos/"+tasadolarHome
		    	.getInstance().getTasa()+")" +
		    	
		    	" where t.fechatx = '"+sdf.format(tasadolarHome
		    	    .getInstance().getId().getFecha())+"' and " +
		    	    		"t.establecimiento.codigounico in " +
		    	    		"(select e.codigounico from Establecimiento e where e.pais.codigopais = 'CO')").executeUpdate();
		}else{
			
			entityManager.createNativeQuery("UPDATE public.transaccion " + 
					"SET  " + 
					"porcentajecomision = case when public.transaccion.porcentajecomision is null then " + 
					"public.porcentajecomisiontx.porcentaje else " + 
					"public.transaccion.porcentajecomision end, " + 
					"valortxpesos = round(public.transaccion.valortxdolares*"+tasadolarHome
		    	    .getInstance().getTasa()+"), " + 
					"valorcomision = round(public.transaccion.valortxdolares*"+tasadolarHome
		    	    .getInstance().getTasa()+"*" +
					"(100-case when public.transaccion.porcentajecomision is null then " + 
					"public.porcentajecomisiontx.porcentaje else " + 
					"public.transaccion.porcentajecomision end)/100), " + 
					"asesorcomision = round(public.transaccion.valortxdolares*"+tasadolarHome
		    	    .getInstance().getTasa()+"*public.asesor.comision/100), " + 
					"arrastradorcomision = round(public.transaccion.valortxdolares*"+tasadolarHome
		    	    .getInstance().getTasa()+"*public.promotor.comisionarrastrador/100), " + 
					"establecimientocomision = round(public.transaccion.valortxdolares*"+tasadolarHome
		    	    .getInstance().getTasa()+"*public.establecimiento.porcentaje/100) " + 
					"FROM " + 
					"public.establecimiento " + 
					"  INNER JOIN public.transaccion as t ON (public.establecimiento.codigounico = t.codigounico) " + 
					"  INNER JOIN public.asesor ON (t.asesor = public.asesor.documento) " + 
					"  INNER JOIN public.promotor ON (t.promotor = public.promotor.documento) " + 
					"  INNER JOIN public.pais ON (public.establecimiento.codpais = public.pais.codigopais) " + 
					"  INNER JOIN public.porcentajecomisiontx ON (public.pais.codigopais = public.porcentajecomisiontx.codpais) " + 
					"WHERE " + 
					"  public.transaccion.fechatx = '"+sdf.format(tasadolarHome
		    	    .getInstance().getId().getFecha())+"' AND  " + 
					"  public.establecimiento.codpais = '"+pais.getCodigopais()+"' and " + 
					"  public.transaccion.consecutivo = t.consecutivo AND  " + 
			"  public.porcentajecomisiontx.fechafin IS NULL").executeUpdate();
			
			entityManager.createNativeQuery("delete from deducciones " + 
					"where consecutivo  " +
					"in (SELECT public.transaccion.consecutivo " +
					"FROM " +
					"public.establecimiento " +
					"INNER JOIN public.transaccion ON (public.establecimiento.codigounico = public.transaccion.codigounico) " +
					"INNER JOIN public.asesor ON (public.transaccion.asesor = public.asesor.documento) " +
					"INNER JOIN public.promotor ON (public.transaccion.promotor = public.promotor.documento) " +
					"INNER JOIN public.pais ON (public.establecimiento.codpais = public.pais.codigopais) " +
					"INNER JOIN public.porcentajecomisiontx ON (public.pais.codigopais = public.porcentajecomisiontx.codpais) " +
					"WHERE " +
					"public.transaccion.fechatx = '"+sdf.format(tasadolarHome
		    	    .getInstance().getId().getFecha())+"' AND " + 
					"public.establecimiento.codpais = '"+pais.getCodigopais()+"' and " +
					"public.porcentajecomisiontx.fechafin IS NULL)").executeUpdate();
			
			entityManager.createNativeQuery("insert into public.deducciones " +
					"(consecutivo, descripcion, valor, tipo) " +
					"select g.consecutivo, g.nombre, g.valor, g.codgravamen from " +
					"(SELECT  " +
					"public.transaccion.consecutivo consecutivo, " +
					"public.gravamenestablecimiento.gravamen codgravamen, " +
					"round(public.transaccion.valortxpesos* " +
					"public.gravamenestablecimiento.porcentaje/100) valor, " +
					"public.gravamen.nombre nombre " +
					"FROM " +
					"public.establecimiento " +
					"INNER JOIN public.transaccion ON (public.establecimiento.codigounico = public.transaccion.codigounico) " +
					"INNER JOIN public.gravamenestablecimiento ON (public.establecimiento.codigounico = public.gravamenestablecimiento.codigounico) " +
					"INNER JOIN public.gravamen ON (public.gravamenestablecimiento.gravamen = public.gravamen.codigo) " +
					"WHERE public.gravamenestablecimiento.gravamen <> '0' " +
					"union " +
					"SELECT " + 
					"public.transaccion.consecutivo consecutivo, " +
					"public.gravamen.codigo codgravamen, " +
					"round(public.franquiciaestablecimiento.porcentaje* " +
					"public.transaccion.valortxpesos/100) valor, " +
					"public.gravamen.nombre nombre " +
					"FROM " +
					"public.tarjeta " +
					"INNER JOIN public.transaccion ON (public.tarjeta.numerotarjeta = public.transaccion.numerotarjeta) " +
					"INNER JOIN public.franquicia ON (public.tarjeta.franquicia = public.franquicia.codfranquicia) " +
					"INNER JOIN public.establecimiento ON (public.transaccion.codigounico = public.establecimiento.codigounico) " +
					"INNER JOIN public.franquiciaestablecimiento ON (public.franquicia.codfranquicia = public.franquiciaestablecimiento.franquicia) " +
					"AND (public.establecimiento.codigounico = public.franquiciaestablecimiento.establecimiento), " +
					"public.gravamen " +
					"WHERE " +
					"public.gravamen.codigo = '0') g " +
					"where g.consecutivo " + 
					"in (SELECT public.transaccion.consecutivo " +
					"FROM " +
					"public.establecimiento " +
					"INNER JOIN public.transaccion ON (public.establecimiento.codigounico = public.transaccion.codigounico) " +
					"INNER JOIN public.asesor ON (public.transaccion.asesor = public.asesor.documento) " +
					"INNER JOIN public.promotor ON (public.transaccion.promotor = public.promotor.documento) " +
					"INNER JOIN public.pais ON (public.establecimiento.codpais = public.pais.codigopais) " +
					"INNER JOIN public.porcentajecomisiontx ON (public.pais.codigopais = public.porcentajecomisiontx.codpais) " +
					"WHERE " +
					"public.transaccion.fechatx = '"+sdf.format(tasadolarHome
		    	    .getInstance().getId().getFecha())+"' AND " + 
					"public.establecimiento.codpais = '"+pais.getCodigopais()+"' and " +
					"public.porcentajecomisiontx.fechafin IS NULL)").executeUpdate();			
		}		
		/*
		entityManager.createQuery("update Transaccion t " +
				"set " +
		    	"t.valortxpesos = round(t.valortxdolares*"+tasadolarHome
		    	.getInstance().getTasa()+")" +
		    	
		    	" where t.fechatx = '"+sdf.format(tasadolarHome
		    	    .getInstance().getId().getFecha())+"' and " +
		    	    		"t.establecimiento.codigounico in " +
		    	    		"(select e.codigounico from Establecimiento e where e.pais.codigopais <> 'CO')").executeUpdate();
		*/
		AdministrarUsuario.auditarUsuario(26, "Actualizo la Tasa de Dolar de: " + pais.getNombre() +
						" para el dia: " + sdf.format(tasadolarHome.getInstance().getId().getFecha()) +
						" valor tasa: " + tasadolarHome.getInstance().getTasa() );
		
    }//fin del metodo actualizarTasaDolar
    
    
    public String guardarTasaBolivarOficina(){
    	try{
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		tasadebolivaroficinaHome.setCreatedMessage(
				expressions.createValueExpression("Se ha registrado la tasa de bolivar de oficina para el " + tasadebolivaroficinaHome.getInstance().getId().getFecha() + " de forma exitosa"));
		
    	tasadebolivaroficinaHome.persist();
    	
    	if(tasadebolivaroficinaHome.getInstance().getId().getTipo().contentEquals("D")){
	    	entityManager.createQuery("update Depositostarjeta d " +
	    	"set d.preciobolivar = " + tasadebolivaroficinaHome
	    	.getInstance().getPreciobolivar() + ", " +
	    	"d.depositopesos = round(d.valordeposito*"+tasadebolivaroficinaHome
	    	.getInstance().getPreciobolivar()+")" +
	    	" where d.fecha = '"+sdf.format(tasadebolivaroficinaHome
	    	    .getInstance().getId().getFecha())+"' and " +
	    	    " d.tipodebolivar = 'OFI' ").executeUpdate();
    	}
    	return "persisted";
    	}catch(Exception e){
    		facesMessages.add("Se ha presentado un error al intentar registrar la tasa de bolivar, verifique la informacion");
    		return null;
    	}
    	
    	
    	
    }
    
    public void limpiarTasadolar(){
    	this.setBancoTemp(null);
    	this.setFrqTemp(null);
    	this.setEstaTemp(null);
    	this.setNomEstable("");
    	this.setFechaIniTemp(null);
    	this.setPaisTemp(null);
    	
    	
    	this.setTasaDolarList(null);
    	this.setTasaEuroList(null);
    	
    	this.pais = null;
    	this.fecha = null;
    	this.listaTasadolar =null;
    	
    	entityManager.flush();
    	
    	
    	
    }
    

    
    

	public void editarTasabolivaroficina(Date fecha, String tipo) {

		log.info("Edicion Tasa Dolar Bolivar");

		Tasadebolivaroficina td = (Tasadebolivaroficina) entityManager
				.createQuery(
						"select t from Tasadebolivaroficina t where t.id.fecha = '"
								+ fecha + "' and t.id.tipo = '" + tipo + "'")
				.getSingleResult();

		tasadebolivaroficinaHome
				.setTasadebolivaroficinaId(new TasadebolivaroficinaId(fecha,
						tipo));

	}


public void editarTasabolivarnegociado(Date fecha, String tipo, String documento){
	
	log.info("Edicion Tasa Dolar Bolivar");
	
	
	Tasabolivarnegociado td = (Tasabolivarnegociado)entityManager
	.createQuery("select t from Tasabolivarnegociado t where t.id.fecha = '"+fecha+"' and t.id.tipo = '"+tipo+"' " +
			"and t.id.documento='"+documento+"'")
	.getSingleResult();
	log.info("1");
	if (td!=null)
	tasabolivarnegociadoHome.setTasabolivarnegociadoId(td.getId());
	
	log.info("2");
	log.info(tasabolivarnegociadoHome.getInstance().getId().getDocumento());
	log.info("3");
	
}
    
    public void guardarTasaNegociada(){
    	//try{
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		tasabolivarnegociadoHome.setCreatedMessage(
				expressions.createValueExpression("Se ha registrado la tasa de bolivar negociada para " + promotorHome.getInstance().getPersonal().getNombre() + " " +
						promotorHome.getInstance().getPersonal().getApellido() + 
		//				" para el " + sdf.format(tasadebolivaroficinaHome.getInstance().getId().getFecha()) + " " +
						"de forma exitosa"));
		tasabolivarnegociadoHome.getInstance().getId().setDocumento(promotorHome.getPromotorDocumento());
		tasabolivarnegociadoHome.getInstance().setPromotor(promotorHome.getInstance());
		
		tasabolivarnegociadoHome.persist();
    	 
    	if(tasabolivarnegociadoHome.getInstance().getId().getTipo().contentEquals("D")){
    		/*
    		String sql = "update (select d from Depositostarjeta d " +
	    	" where " +
    	    " d.tarjeta.promotor.documento = '"+promotorHome.getPromotorDocumento()+"' and "+
	    	" d.fecha = '"+sdf.format(tasabolivarnegociadoHome
	    	    .getInstance().getId().getFecha())+"' and " +
	    	    " d.tipodep = 'NEG') d "  +
	        "set d.preciobolivar = " + tasabolivarnegociadoHome
	    	    	.getInstance().getPreciobolivar() + ", " +
	    	    	"d.depositopesos = round(d.valordeposito*"+tasabolivarnegociadoHome
	    	    	.getInstance().getPreciobolivar()+")";

    		log.info(" ***************** SQL " + sql);
    		
    		/*
	    	    +
	    	    "and " +
	    	    " d.tarjeta.promotor.documento = '"+promotorHome.getPromotorDocumento()+"'";*/
    		
	    	//entityManager.createQuery(sql).executeUpdate();
    		
    		String sql = " update depositostarjeta " +
    		" set preciobolivar = " + tasabolivarnegociadoHome
	    	.getInstance().getPreciobolivar() + ", " +
	    	"depositopesos = round(valordeposito*"+tasabolivarnegociadoHome
	    	.getInstance().getPreciobolivar()+") " +
    				" where " +
    		" fecha = '"+sdf.format(tasabolivarnegociadoHome
    	    	    .getInstance().getId().getFecha())+"' and " +
    	    	    " tipodebolivar = 'NEG' and numerotarjeta in ( select t.numerotarjeta from " +
    	    	    " tarjeta t where " +
    	    	    
    	    	    " t.documento = '" + promotorHome.getPromotorDocumento() + "')";
    		
    		log.info(" ***************** SQL " + sql);
    		entityManager.createNativeQuery(sql).executeUpdate();
    	}
    	
    	tasabolivarnegociadoHome.clearInstance();
    	promotorHome.clearInstance();
    	this.nombre = "";
    	/*
    	}catch(Exception e){
    		facesMessages.add("Se ha presentado un error al intentar registrar la tasa de bolivar, verifique la informacion");
    	}
    	
    	*/
    	
    }
    
    
    public void actualizarTasaBolivarOficina(){
    	try{
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	Expressions expressions = new Expressions();
		ValueExpression mensaje;
		tasadebolivaroficinaHome.setUpdatedMessage(
				expressions.createValueExpression("Se ha actualizado la tasa de bolivar de oficina para el " + tasadebolivaroficinaHome.getInstance().getId().getFecha() + " de forma exitosa"));
		
    	tasadebolivaroficinaHome.update();
    	
    	if(tasadebolivaroficinaHome.getInstance().getId().getTipo().contentEquals("D")){
	    	entityManager.createQuery("update Depositostarjeta d " +
	    	"set d.preciobolivar = " + tasadebolivaroficinaHome
	    	.getInstance().getPreciobolivar() + ", " +
	    	"d.depositopesos = round(d.valordeposito*"+tasadebolivaroficinaHome
	    	.getInstance().getPreciobolivar()+")" +
	    	" where d.fecha = '"+sdf.format(tasadebolivaroficinaHome
	    	    .getInstance().getId().getFecha())+"' and " +
	    	    " d.tipodep = 'OFI' ").executeUpdate();
    	}
    	}catch(Exception e){
    		facesMessages.add("Se ha presentado un error al intentar registrar la tasa de bolivar, verifique la informacion");
    	}
    }
    
    @In Identity identity;    
    private List<String> lista = new ArrayList<String>();
    
       
    public List<String> getLista() {
		return lista;
	}

	public void setLista(List<String> lista) {
		this.lista = lista;
	}

	public void llenarPromotores(){
		entityManager.clear();
		String sql = "";
		
		if(identity.hasRole("Asesor")){
			sql = " where p.asesor.documento = '"+identity.getUsername()+"'";
		}
		List<String> resultList = entityManager.createQuery("select p.personal.nombre||' '||p.personal.apellido " +
				"from Promotor p "+ sql).getResultList();
		lista = resultList;
	}
        
    /*public List<String> autocompletar(Object nombre) {
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

	}*/
    
    /**
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
		log.info(">>>Tiempo total de la busqueda: " + t2 + "ms");		
		return result;
	}    private String nombre = "";


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	@In(create=true)
	private CargarObjetos CargarObjetos;
	
	private String tipo = "";
    
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void ubicarPromotor(){
		Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
		
		if(pr!=null){
			log.info("Promotor " + pr.getDocumento());
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			
			//tarjetaHome.getInstance().setPromotor(pr);
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
	
	public List<Tasabolivarnegociado> getListaTasabolivarnegociado() {
		return listaTasabolivarnegociado;
	}

	public void setListaTasabolivarnegociado(
			List<Tasabolivarnegociado> listaTasabolivarnegociado) {
		this.listaTasabolivarnegociado = listaTasabolivarnegociado;
	}

	public void buscarTasaBolivarnegociado(){
		String sql = "select t from Tasabolivarnegociado t where 1=1 ";
		
		log.info("*-*-*-*-*-*-*-* Tasa Bolivar Negociado " + this.nombre);
		
		log.info("*-*-*-*-*-*-*-* Tasa Bolivar Negociado " + promotorHome.getInstance().getDocumento());
		
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
		
		if(this.nombre != null && pr != null){
			sql = sql + " and t.id.documento = '"+pr.getDocumento()+"'";
		}
		
		if(this.fecha != null){
			sql = sql + " and to_char(t.id.fecha,'dd/mm/yyyy') = '"+sdf.format(fecha)+"'";
		}
		
		if(this.tipo!=null){
			sql = sql + " and t.id.tipo = '"+this.tipo+"'";
		}
    	listaTasabolivarnegociado = entityManager.createQuery(sql).setMaxResults(30).getResultList();
    }
	
	//Metodos del nuevo modelo de Dolar

	public List<Tasadolarparametro> getTasaDolarList() {
		return tasaDolarList;
	}

	public void setTasaDolarList(List<Tasadolarparametro> tasaDolarList) {
		this.tasaDolarList = tasaDolarList;
	}
	
	public List<Tasaeuroparametro> getTasaEuroList() {
		return tasaEuroList;
	}

	public void setTasaEuroList(List<Tasaeuroparametro> tasaEuroList) {
		this.tasaEuroList = tasaEuroList;
	}

	//Metodo de busqueda
	public String tasaDolarBuscar(){
		
		log.info("Buscando Tasa Global");
		log.info("Limpiando cache...");
		this.setTasaDolarList(null);
		this.setTasaEuroList(null);
		
	
		if( this.getPaisTemp() == null || this.getPaisTemp().equals("") ){
			facesMessages.addToControl("paisSel",
					"Se debe seleccionar un pais");
				return null;
		}			
		// Valida que se haya seleccionado la fecha
		if( this.getFechaIniTemp() == null){
			facesMessages.addToControl("fechainicio",	
			"Se debe seleccionar una fecha para esta tasa");
			return null;
			
		}			
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String pais = this.paisTemp.getPaisiso().getCodigomoneda();
		
		if( pais.equals("EUR")){
			log.info("Buscando Euros...");
			//Busca la tasa de EURO Global de la consulta del componente List
			try{
				String queryString = 
					"select tsp from Tasaeuroparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio = '"+ sdf.format(this.getFechaIniTemp()) +"' and tsp.pais.codigopais = '" + 
					this.paisTemp.getCodigopais() +"'";
				//Establecimiento
				if( this.getNomEstable() != null && !this.getNomEstable().equals("") ){
					queryString += " and tsp.establecimiento.nombreestable =  '" + this.getNomEstable() + "'";
				}
				//Franquicia 
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";					
				}
				//Banco
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" +
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
					
				}
				this.setTasaEuroList(entityManager.createQuery(queryString).getResultList());
				return "Busqueda Exitosa";
			}catch( NoResultException e ){
				e.printStackTrace();
			}	
			return "Busqueda Finalizada";
		}else{
			log.info("Buscando Tasa Dolar...");
			//Busca la tasa de DOLAR Global de la consulta del componente List
			try{
				String queryString = 
					"select tsp from Tasadolarparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio = '"+ sdf.format(this.getFechaIniTemp()) +"' and tsp.pais.codigopais = '" + 
					this.paisTemp.getCodigopais() +"'";
				//Establecimiento
				if( this.getNomEstable() != null && !this.getNomEstable().equals("")){
					queryString += " and tsp.establecimiento.nombreestable =  '" + this.getNomEstable() + "'";
				}
				//Franquicia 
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
				}
				//Banco
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" +
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco =  null";
					
				}
				this.setTasaDolarList( entityManager.createQuery(queryString).getResultList() );
				return "Busqueda Exitosa";
		} catch (Exception e) {
				e.printStackTrace();
		}
			
		return "Busqueda Finalizada";
		}//fin del else
		
			
    }
	
	
	public List<Tasadolar> getListaTasadolar() {
		return listaTasadolar;
	}

	
	public void setListaTasadolar(List<Tasadolar> listaTasadolar) {
		this.listaTasadolar = listaTasadolar;
	}
	
	public void establecerBotones(){
		System.out.println("Ejecutando boton");
	}
	
	/**
	 * Metodo llamado desde el List para editar un registro de 
	 * TasadolarpromotorparametroList
	 * 
	 * @param consecutivo
	 * @param moneda
	 * @throws ParseException
	 */
	public void editarTasaGlobal(Integer consecutivo, String moneda) 
													throws ParseException{	
		log.info("Edicion Tasa Global..");
		this.setManagedTasa(false); 
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	//1. Determinar la moneda Euros o Dolares a Editar
    	//2. Obtener las entidades: a. Tasaeuroparametro o Tasadolarparametro 
    	//						    b. Establecimientoprecio 
    	// 						    c. Porcentajecomisiontxparam
    	if(moneda.equals("EUR")){  
    		//a.
    		Tasaeuroparametro tsp = entityManager.find(Tasaeuroparametro.class, consecutivo);
    		//b.
    		Establecimientoprecio estPrecio = 
    			entityManager.find(Establecimientoprecio.class, 
    							   new EstablecimientoprecioId( tsp.getEstablecimiento().getCodigounico(), 
    									   						sdf.parse(sdf.format( tsp.getFechainicio()))));
    		String queryString = "select p from Porcentajecomisiontxparam p where " +
    							 "p.fechainicio = '" + sdf.format(tsp.getFechainicio()) + "'  " +
    							 "and p.pais.codigopais = '" + tsp.getPais().getCodigopais() + "' " +
    							 "and p.establecimiento.codigounico = '" + tsp.getEstablecimiento().getCodigounico()+ "' " +
    							 "and p.franquicia.codfranquicia " + (tsp.getFranquicia() == null ? " is null " : "'"+ tsp.getFranquicia().getCodfranquicia() +"'" ) + 
    							 "and p.banco.codbanco " + (tsp.getBanco() == null ? " is null" : "'" + tsp.getBanco().getCodbanco() +"'") ;
    		//c.
    		System.out.println("Paridad: "+estPrecio.getParidadCliente());
    		Porcentajecomisiontxparam porcentaje = 
    						(Porcentajecomisiontxparam) entityManager.createQuery(queryString).getSingleResult();
    		//3. Establezco los metodos Setter de los campos del formulario
    		this.setPaisTemp(tsp.getPais());
    		this.setEstaTemp(tsp.getEstablecimiento());
    		this.setNomEstable(this.getEstaTemp().getNombreestable());
    		this.setFrqTemp(tsp.getFranquicia());
    		this.setBancoTemp(tsp.getBanco());
    		this.setTipoCupoTemp(tsp.getTipocupo());
    		this.setFechaIniTemp(tsp.getFechainicio());
    		this.setTasaEuroTemp(tsp.getTasaeuro());
    		this.setTasaEuroTacTemp(tsp.getTasaeuroTac());
    		this.setTasaEuroOfTemp(estPrecio.getDolaroficina());//El campo es dolar pero graba datos de Euro tambien
    		this.setPorcentCt(porcentaje.getPorcentaje());
    		this.setPorcentOfi(estPrecio.getPorcentajeoficina());
    		this.setParidadClienteTemp(estPrecio.getParidadCliente());
    		//Auditoria
    		AdministrarUsuario.auditarUsuario(41, "Consulto Tasa Euro para el Establecimiento: " +this.getEstaTemp().getNombreestable() +
    				" En la fecha: " + sdf.format(tsp.getFechainicio()));
    		
    	}else{//Caso para Dolar
    		//a.
    		Tasadolarparametro tsp = entityManager.find(Tasadolarparametro.class, consecutivo);
    		//b.
    		Establecimientoprecio estPrecio = 
    			entityManager.find(Establecimientoprecio.class, 
    							   new EstablecimientoprecioId( tsp.getEstablecimiento().getCodigounico(), 
    									   						sdf.parse(sdf.format( tsp.getFechainicio()))));
    		String queryString = "select p from Porcentajecomisiontxparam p where " +
    							 "p.fechainicio = '" + sdf.format(tsp.getFechainicio()) + "'  " +
    							 "and p.pais.codigopais = '" + tsp.getPais().getCodigopais() + "' " +
    							 "and p.establecimiento.codigounico = '" + tsp.getEstablecimiento().getCodigounico()+ "' " +
    							 "and p.franquicia.codfranquicia " + (tsp.getFranquicia() == null ? " is null " : "'"+ tsp.getFranquicia().getCodfranquicia() +"'" ) + 
    							 "and p.banco.codbanco " + (tsp.getBanco() == null ? " is null" : "'" + tsp.getBanco().getCodbanco() +"'") ;
    		//c.
    		Porcentajecomisiontxparam porcentaje = 
    						(Porcentajecomisiontxparam) entityManager.createQuery(queryString).getSingleResult();
    		
    		//3. Establezco los metodos Setter de los campos del formulario
    		this.setPaisTemp(tsp.getPais());
    		this.setEstaTemp(tsp.getEstablecimiento());
    		this.setNomEstable(this.getEstaTemp().getNombreestable());
    		this.setFrqTemp(tsp.getFranquicia());
    		this.setBancoTemp(tsp.getBanco());
    		this.setTipoCupoTemp(tsp.getTipocupo());
    		this.setFechaIniTemp(tsp.getFechainicio());
    		this.setTasaDolarTemp(tsp.getTasadolar());
    		this.setTasaDolarTacTemp(tsp.getTasadolarTac());
    		this.setTasaDolarOfTemp(estPrecio.getDolaroficina());//El campo es dolar pero graba datos de Euro tambien
    		this.setPorcentCt(porcentaje.getPorcentaje());
    		this.setPorcentOfi(estPrecio.getPorcentajeoficina());
    		this.setParidadClienteTemp(estPrecio.getParidadCliente());
    		//Auditoria
    		AdministrarUsuario.auditarUsuario(41, "Consulto Tasa de Dolar para el Establecimiento: " +this.getEstaTemp().getNombreestable() +
    				" En la fecha: " + sdf.format(tsp.getFechainicio()));
    	}
    }
	
    
    public void actualizarTasasGlobal(){
    	//1.Crea los objetos del contexto actual
    	
    	//2.Update a los daots
    	
    	//3.Auditoria
    	
    	//5.Set null a todos los objetos
    	this.setManagedTasa(true);
    }
	
    
    
	public void buscarTasadolar(){
		String sql = "select t from Tasadolar t where 1=1 ";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		if(this.fecha != null){
			sql = sql + " and t.id.fecha = '"+sdf.format(fecha)+"'";
		}
		
		if(this.pais!=null){
			sql = sql + " and t.id.codigopais = '"+this.pais.getCodigopais()+"'";
		}
		
		sql = sql + " order by t.id.fecha desc ";
    	listaTasadolar = entityManager.createQuery(sql).setMaxResults(30).getResultList();
    }
	
	Pais pais = null;

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}
	
	public void limpiarTasaBolivarnegociado(){
		this.tipo=null;
		this.fecha=null;
		this.nombre=null;
		this.listaTasabolivarnegociado = null;
	}
	
	
   //Getter and Setter variables del formulario
   
    public Pais getPaisTemp() {
		return paisTemp;
	}

	public void setPaisTemp(Pais paisTemp) {
		this.paisTemp = paisTemp;
	}

	public Promotor getPromoTemp() {
		return promoTemp;
	}

	public void setPromoTemp(Promotor promoTemp) {
		this.promoTemp = promoTemp;
	}

	public Franquicia getFrqTemp() {
		return frqTemp;
	}

	public void setFrqTemp(Franquicia frqTemp) {
		this.frqTemp = frqTemp;
	}

	public Banco getBancoTemp() {
		return bancoTemp;
	}

	public void setBancoTemp(Banco bancoTemp) {
		this.bancoTemp = bancoTemp;
	}

	public String getTipoCupoTemp() {
		return tipoCupoTemp;
	}

	public void setTipoCupoTemp(String tipoCupoTemp) {
		this.tipoCupoTemp = tipoCupoTemp;
	}

	public Date getFechaIniTemp() {
		return fechaIniTemp;
	}

	public void setFechaIniTemp(Date fechaIniTemp) {
		this.fechaIniTemp = fechaIniTemp;
	}

	public Date getFechaFinTemp() {
		return fechaFinTemp;
	}

	public void setFechaFinTemp(Date fechaFinTemp) {
		this.fechaFinTemp = fechaFinTemp;
	}

	public BigDecimal getTasaDolarTemp() {
		return tasaDolarTemp;
	}

	public void setTasaDolarTemp(BigDecimal tasaDolarTemp) {
		this.tasaDolarTemp = tasaDolarTemp;
	}

	public BigDecimal getTasaDolarNegTemp() {
		return tasaDolarNegTemp;
	}

	public void setTasaDolarNegTemp(BigDecimal tasaDolarNegTemp) {
		this.tasaDolarNegTemp = tasaDolarNegTemp;
	}

	public BigDecimal getTasaDolarTacTemp() {
		return tasaDolarTacTemp;
	}

	public void setTasaDolarTacTemp(BigDecimal tasaDolarTacTemp) {
		this.tasaDolarTacTemp = tasaDolarTacTemp;
	}

	public BigDecimal getTasaDolarOfTemp() {
		return tasaDolarOfTemp;
	}

	public void setTasaDolarOfTemp(BigDecimal tasaDolarOfTemp) {
		this.tasaDolarOfTemp = tasaDolarOfTemp;
	}

	public BigDecimal getTasaEuroTemp() {
		return tasaEuroTemp;
	}

	public void setTasaEuroTemp(BigDecimal tasaEuroTemp) {
		this.tasaEuroTemp = tasaEuroTemp;
	}

	public BigDecimal getTasaEuroNegTemp() {
		return tasaEuroNegTemp;
	}

	public void setTasaEuroNegTemp(BigDecimal tasaEuroNegTemp) {
		this.tasaEuroNegTemp = tasaEuroNegTemp;
	}

	public BigDecimal getTasaEuroTacTemp() {
		return tasaEuroTacTemp;
	}

	public void setTasaEuroTacTemp(BigDecimal tasaEuroTacTemp) {
		this.tasaEuroTacTemp = tasaEuroTacTemp;
	}

	public BigDecimal getTasaEuroOfTemp() {
		return tasaEuroOfTemp;
	}

	public void setTasaEuroOfTemp(BigDecimal tasaEuroOfTemp) {
		this.tasaEuroOfTemp = tasaEuroOfTemp;
	}

	public BigDecimal getPorcentCt() {
		return porcentCt;
	}

	public void setPorcentCt(BigDecimal porcentCt) {
		this.porcentCt = porcentCt;
	}

	public BigDecimal getPorcentOfi() {
		return porcentOfi;
	}

	public void setPorcentOfi(BigDecimal porcentOfi) {
		this.porcentOfi = porcentOfi;
	}

	public BigDecimal getParidadEstTemp() {
		return paridadEstTemp;
	}

	public void setParidadEstTemp(BigDecimal paridadEstTemp) {
		this.paridadEstTemp = paridadEstTemp;
	}

	public BigDecimal getParidadClienteTemp() {
		return paridadClienteTemp;
	}

	public void setParidadClienteTemp(BigDecimal paridadClienteTemp) {
		this.paridadClienteTemp = paridadClienteTemp;
	}
	
	public String getCodMoneda() {
		return codMoneda;
	}

	public void setCodMoneda(String codMoneda) {
		this.codMoneda = codMoneda;
	}

	public Establecimiento getEstaTemp() {
		return estaTemp;
	}

	public void setEstaTemp(Establecimiento estaTemp) {
		this.estaTemp = estaTemp;
	}
	
	
	public String getPathBandera() {
		return pathBandera;
	}

	public void setPathBandera(String pathBandera) {
		this.pathBandera = pathBandera;
	}
	
	public boolean isFormValido() {
		return formValido;
	}

	public void setFormValido(boolean formValido) {
		this.formValido = formValido;
	}

	//Metodos de servicio para la Vista		
	public void ubicarPromotorTasa(){
		Promotor pr = CargarObjetos.ubicarPromotor(this.getNombrePromo());
		if(pr!=null){
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			this.setPromoTemp(pr);
		}
	}
	
	private String nombrePromo = "";

	public String getNombrePromo() {
		return nombrePromo;
	}

	public void setNombrePromo(String nombre) {
		this.nombrePromo = nombre;
	}
	
	public void llenarPromotoresNombre(){
		entityManager.clear();
		String sql = "";
		List<String> resultList = entityManager.createQuery("select " +
				"p.personal.nombre||' '||p.personal.apellido from Promotor p "+ 
				sql).getResultList();
		lista = resultList;
	}		
	
    public List<String> autocompletarPromotor(Object nom) {
    	llenarPromotoresNombre(); 							// Metodo que carga la informacion de los nombres de las personas
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
		log.info(">>>Tiempo total de la busqueda: " + t2 + "ms");		
	
		return result;
	}
    
    
    private String nomEstable = "";
    
    public String getNomEstable() {
		return nomEstable;
	}

	public void setNomEstable(String nomEstable) {
		this.nomEstable = nomEstable;
	}
	
	
	public void ubicarEstablecimientoTasa()
	{
		
		entityManager.clear();			
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable) = " +
				"trim('"+ this.getNomEstable() +"')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 				
				establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
			this.setEstaTemp(es);
		}		
	}
	
	public void llenarEstablcimiento(){
		entityManager.clear();
	
		List<String> resultList = entityManager.createQuery(" select e.nombreestable from Establecimiento e " +  
				"where e.pais.codigopais = '" + 
				this.getPaisTemp().getCodigopais()+"'" ).getResultList();
	    this.setLista( resultList );
	}
	
	public List<String> autocompletarEstablecimientoTasas(Object nomEst) {
		
		long t1 = System.currentTimeMillis();

		llenarEstablcimiento();
		String nombre = (String) nomEst;
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
		log.info(">>>Tiempo total de la busqueda: " + t2 + "ms");	
	return result;
	}
	
	public void listarComercios( String codPaisT){
		this.llenarEstablcimiento();
		Pais paisT = entityManager.find(Pais.class, codPaisT);
		log.info("Estableciendo codigo moneda.....");
		
		this.setCodMoneda(paisT.getPaisiso().getCodigomoneda().trim());
		this.setPathBandera( "img/flags" + 
								paisT.getPaisiso().getPathBanderaGui());
		
		log.info("PATH BANDERA: " + this.getPathBandera());
	}
	
	
	//Metodos de persistencia
	
	/**
	 * Graba los parametros de liquidacion de las transacciones como
	 * Tasa Dolar, Tasa Euro, porcentajes de comision y paridad de monedas
	 * @return 
	 */
	public String guardarTasaDolarParam(){			
		
		// Valida que se haya elegido un pais
		if( this.getPaisTemp() == null || this.getPaisTemp().equals("") ){
			facesMessages.addToControl("paisSel",
					"Se debe seleccionar un pais");
				return null;
		}			
		// Valida que se haya seleccionado la fecha
		if( this.getFechaIniTemp() == null){
			facesMessages.addToControl("fechainicio",	
			"Se debe seleccionar una fecha para esta tasa");
			return null;
			
		}
		// Valida que se haya ingresado las tasas y porcentajes para estableicimiento
		if( this.getPaisTemp().getPaisiso().getCodigomoneda().equals("EUR")){
			if( this.getTasaEuroOfTemp() == null && this.getTasaEuroNegTemp() == null ){
				facesMessages.addToControl("tasaeurorOf",	
				"Se debe ingresar la tasa de euro para la oficina");
				return null;
			}
			if(this.getTasaEuroTacTemp() == null && this.getTasaEuroNegTemp() == null){
				facesMessages.addToControl("tasaeuroTac",	
				"Se debe ingresar la tasa de euro para clientes TAC");
				return null;
			}					
			if(this.getTasaEuroTemp() == null && this.getTasaEuroNegTemp() == null ){					
				facesMessages.addToControl("tasaEuro",	
				"Se debe ingresar la tasa de euro para clientes");
				return null;
			}	
			//Valida el porcentaje de oficna y cliente 
			if( this.getPorcentCt() == null && this.getTasaEuroNegTemp() == null ){
				facesMessages.addToControl("porcentClient",	
				"Se debe ingresar la tasa de porcentaje  para cliente");
				return null;
			}
			if( this.getPorcentOfi()== null  && this.getTasaEuroNegTemp() == null){
				facesMessages.addToControl("porcentOf",	
				"Se debe ingresar la tasa de porcentaje para oficina");
				return null;
			}
		}else{
			if( this.getTasaDolarOfTemp() == null && this.getTasaDolarNegTemp() == null ){
				facesMessages.addToControl("tasadolarOf",	
				"Se debe ingresar la tasa de dolar para la oficina");
				return null;
			}
			if(this.getTasaDolarTacTemp() == null && this.getTasaDolarNegTemp() == null ){
				facesMessages.addToControl("tasadolarTac",	
				"Se debe ingresar la tasa de dolar para clientes TAC");
				return null;
			}					
			if(this.getTasaDolarTemp() == null && this.getTasaDolarNegTemp() == null ){					
				facesMessages.addToControl("tasadolar",	
				"Se debe ingresar la tasa de dolar para clientes");
				return null;
			}
			//Valida el porcentaje de oficna y cliente 
			if( this.getPorcentCt() == null && this.getTasaDolarNegTemp() == null ){
				facesMessages.addToControl("porcentClient",	
				"Se debe ingresar la tasa de porcentaje  para cliente");
				return null;
			}
			if( this.getPorcentOfi()== null && this.getTasaDolarNegTemp() == null ){
				facesMessages.addToControl("porcentOf",	
				"Se debe ingresar la tasa de porcentaje para oficina");
				return null;
			}
		}			
		
		// Valida que se haya seleccionado un establecimiento
		if((this.getTasaDolarOfTemp() != null || this.getTasaEuroOfTemp() != null) &&
				this.getEstaTemp() == null ){
			facesMessages.addToControl("name",	
				"Se debe seleccionar el establecimiento");
				return null;
		}
		// Valida los parametros de euro negociado para promotor
		if( this.getPromoTemp() != null && 
				this.getPaisTemp().getPaisiso().getCodigomoneda().equals("EUR") ){
			if( this.getPorcentCt() == null){
				facesMessages.addToControl("porcentClient",	
				"Se debe ingresar la tasa de porcentaje  para cliente");
				return null;					
			}
			if(this.getTasaEuroNegTemp() == null){
				facesMessages.addToControl("tasaeurorNeg",	
				"Se debe ingresar la tasa de euro  para cliente");
				return null;
				
			}
		}
		// Valida los parametros de dolar negociado para promotor
		if( this.getPromoTemp() != null && 
				this.getPaisTemp().getPaisiso().getCodigomoneda().equals("USD") ){
			if( this.getPorcentCt() == null){
				facesMessages.addToControl("porcentClient",	
				"Se debe ingresar la tasa de porcentaje  para cliente");
				return null;					
			}
			if(this.getTasaDolarNegTemp() == null){
				facesMessages.addToControl("tasadolarNeg",	
				"Se debe ingresar la tasa de euro  para cliente");
				return null;
				
			}
		}
		
		// Proceso de persistencia de las tasas
		
		Boolean euro = false;	    // Variable de control Moneda
		Boolean negociado = false;  // Variable de control tipo de Tasa
		Boolean estbto = false;		// Variable de control 
		String mensaje= null;
		
		//1. Determina si se graba dolar o euros
		if( this.getCodMoneda().equals("EUR")){
			euro = true;
		}
		
		//2. Determina si es tasa negociada para cliente o global
		if( this.getPromoTemp() != null ){
			negociado = true;
		}
		
		//3. Variable para parametros del comercio
		if( this.getEstaTemp() != null && this.getPromoTemp() == null ){
			estbto = true;
		}		
		
		log.info("EURO: " + euro);
		log.info("NEGOCIADO: " + negociado);

		//3. Proceso de persistencia negociado o global con base en el paso 2
		// Primero. Identifica que no exista esa tasa para esa misma fecha y parametros
		// Segundo. Cierra las Tasas anteriores vigentes
		// Tercero. Realiza el proceso de persistencia
		if( negociado ){
			if( euro ){
				log.info("EURO NEGOCIADO PROMOTOR");
				if (this.buscarEuroActualPromotor()){							
					facesMessages.addToControl("name",
						"Ya se encuentra grabada la tasa para "
								+ "esta fecha y promotor");
					return null;
				}
				
				this.cerrarTasaEuroPromotor();
				this.cerrarComisionNegociada();
				
				if( this.getPorcentCt() != null ){
					this.grabarComisionNegociada();
				}
				mensaje = this.grabarTasaEuroPromotor(); 
			}else{
				log.info("DOLAR NEGOCIADO PROMOTOR");
				if( this.buscarDolarActualPromotor()){
					facesMessages.addToControl("name",
							"Ya se encuentra grabada la tasa para "
									+ "esta fecha y promotor");
						return null;
				}
				
				this.cerrarTasaDolarPromotor();
				this.cerrarComisionNegociada();
				
				if( this.getPorcentCt() != null ){
					this.grabarComisionNegociada();
				}
				mensaje = this.grabarTasaDolarPromotor(); 
			}
		}else{
			if( euro ){
				
				log.info("EURO GENERAL");					
				if(buscarEuroActualGlobal()){
					facesMessages.addToControl("name", "Ya se encuentra grabada la tasa para " +
							"esta fecha y establecimiento");
					return null;
				}				
				this.cerrarTasaEuroGlobal();
				this.cerrarComisionGlobal();
				
				if( this.getPorcentCt() != null ){
					this.grabarComisionGlobal();
				}
				mensaje = this.grabarTasaEuroGlobal();				
			}else{
				log.info("DOLAR GENERAL");
				if( this.buscarDolarActualGlobal()){
					facesMessages.addToControl("name",
							"Ya se encuentra grabada la tasa para "
									+ "esta fecha y establecimiento");
						return null;
				}					
				this.cerrarTasaDolarGlobal();
				this.cerrarComisionGlobal();
				
				if( this.getPorcentCt() != null ){
					this.grabarComisionGlobal();
				}
				mensaje = this.grabarTasaDolarGlobal();
			}
		}			
		// Si es global Instancio el objeto Establecimiento precio
		if( estbto ){
			// Cierra los parametros de %, € o $ para el establecimiento
			if( this.getPorcentOfi() != null ){
				this.cerrarParametrosComercio();
				mensaje = this.grabarPrecioComercio( euro );
				log.info("DESPUES DE GRABAR EL PRECIO DEL ESTABLECIMIENTO");
			}
		}
		
		// Correo de notificaciones para asesoras
		if(!negociado ){
			if( euro ){
				
				//Consulta las tasas y porcentajes para promotores que no 
				//se han cerrado y cuyo valor sea menor que la tasa actual Global
				List<Object[]> te=this.consultarTasasEuroPorcentajesPromotor();
								
				List<String> documentoAsesor= entityManager.createQuery("SELECT DISTINCT t.promotor.asesor.documento "+
																		"FROM Tasaeuropromotorparametro t "+
																		"WHERE t.fechafin is null AND "+
																		"t.tasaeuro<"+ this.getTasaEuroTemp() + " AND " + 
																		"t.pais.codigopais = '" + this.getPaisTemp().getCodigopais() + "'").getResultList();
				for(String asesor: documentoAsesor){
					List<Object[]> tasaTemp=new ArrayList<Object[]>();
					for(int i=0; i<te.size(); i++){						
						if(te.get(i)[8].equals(asesor)){
							tasaTemp.add(te.get(i));							
						}	
					}
					
					Asesor ases=entityManager.find(Asesor.class, asesor);
					//enviar correo
					if(tasaTemp.size()>0){
					this.enviarMailAlertas.enviarEmailAlertaTasaPromotor("euro", ases, tasaTemp, this.getTasaEuroTemp(), this.getPorcentCt());
					}
					tasaTemp=null;					
				}
			}else{
				// busca en tasa dolar global
				//Consulta las tasas y porcentajes para promotores que no se han cerrado y cuyo valor sea menor que la tasa actual Global
				List<Object[]> te=this.consultarTasasDolarPorcentajesPromotor();
				List<String> documentoAsesor= entityManager.createQuery("SELECT DISTINCT t.promotor.asesor.documento "+
																		"FROM Tasadolarpromotorparametro t "+
																		"WHERE t.fechafin is null AND "+
																		"t.tasadolar<"+ this.getTasaDolarTemp() + "AND " +
																		"t.pais.codigopais = '" + this.getPaisTemp().getCodigopais() + "'").getResultList();
				for(String asesor: documentoAsesor){
					List<Object[]> tasaTemp=new ArrayList<Object[]>();
					for(int i=0; i<te.size(); i++){						
						if(te.get(i)[8].equals(asesor)){
							tasaTemp.add(te.get(i));							
						}	
					}
					
					Asesor ases=entityManager.find(Asesor.class, asesor);
					//enviar correo
					if(tasaTemp.size()>0){
					this.enviarMailAlertas.enviarEmailAlertaTasaPromotor("dolar", ases, tasaTemp, this.getTasaDolarTemp(), this.getPorcentCt());
					}
					tasaTemp=null;					
				}				
			}
		}
		//5. Proceso de auditoria
		return mensaje;
	}
	
	
	private List<Object[]> consultarTasasEuroPorcentajesPromotor(){
		
		List<Object[]> te=null;
		
		//Busca las tasas negociadas que esten por debajo de la nueva tasa global
		String sql="SELECT tasaeuropromotorparametro.consecutivo, "+
					"public.personal.nombre ||' '|| public.personal.apellido, "+
					"public.establecimiento.nombreestable, "+
					"public.banco.nombrebanco, "+
					"public.franquicia.nombrefranquicia, "+
					"public.tasaeuropromotorparametro.fechainicio, "+
					"public.tasaeuropromotorparametro.tasaeuro, "+
					"public.porcentcomisiontxparampromo.porcentaje, "+
					"public.promotor.asesor, "+
					"pais1.nombre, "+
					"public.porcentcomisiontxparampromo.consecutivo as idporcentaje "+
					"FROM "+
					"public.tasaeuropromotorparametro "+
					"LEFT OUTER JOIN public.banco ON (public.tasaeuropromotorparametro.codbanco = public.banco.codbanco) "+
					"LEFT OUTER JOIN public.franquicia ON (public.tasaeuropromotorparametro.codfranquicia = public.franquicia.codfranquicia) "+
					"LEFT OUTER JOIN public.establecimiento ON (public.tasaeuropromotorparametro.codigounico = public.establecimiento.codigounico) "+
					"INNER JOIN public.promotor ON (public.tasaeuropromotorparametro.documento = public.promotor.documento) "+
					"INNER JOIN public.personal ON (public.promotor.documento = public.personal.documento) "+
					"INNER JOIN public.pais pais1 ON (public.tasaeuropromotorparametro.codpais = pais1.codigopais), "+
					"public.porcentcomisiontxparampromo "+
					"WHERE "+
					"public.porcentcomisiontxparampromo.codpais =   public.tasaeuropromotorparametro.codpais AND "+
					"public.porcentcomisiontxparampromo.documento =   public.tasaeuropromotorparametro.documento AND "+
					"(public.porcentcomisiontxparampromo.codigounico = public.tasaeuropromotorparametro.codigounico OR "+
					"(public.porcentcomisiontxparampromo.codigounico IS NULL AND public.tasaeuropromotorparametro.codigounico IS NULL) ) AND "+
					"(public.porcentcomisiontxparampromo.codfranquicia = public.tasaeuropromotorparametro.codfranquicia OR "+
					"(public.porcentcomisiontxparampromo.codfranquicia IS NULL AND public.tasaeuropromotorparametro.codfranquicia IS NULL )) AND "+
					"(public.porcentcomisiontxparampromo.codbanco = public.tasaeuropromotorparametro.codbanco OR "+
					"(public.porcentcomisiontxparampromo.codbanco IS NULL AND public.tasaeuropromotorparametro.codbanco IS NULL)) AND "+
					"(public.porcentcomisiontxparampromo.tipocupo = public.tasaeuropromotorparametro.tipocupo OR "+
					"(public.porcentcomisiontxparampromo.tipocupo IS NULL AND public.tasaeuropromotorparametro.tipocupo IS NULL) )AND "+
					"public.porcentcomisiontxparampromo.fechainicio =  public.tasaeuropromotorparametro.fechainicio AND "+
					" public.tasaeuropromotorparametro.fechafin is NULL AND "+
					"(public.tasaeuropromotorparametro.tasaeuro<"+this.getTasaEuroTemp()+
					" OR  public.porcentcomisiontxparampromo.porcentaje<"+this.getPorcentCt()+") AND " +
					"  public.tasaeuropromotorparametro.codpais='"+this.getPaisTemp().getCodigopais()+"' "+
					" GROUP BY "+
					"tasaeuropromotorparametro.consecutivo, "+
					"public.personal.nombre, "+
					"public.personal.apellido, "+
					"public.establecimiento.nombreestable, "+
					"public.banco.nombrebanco, "+
					"public.franquicia.nombrefranquicia, "+
					"public.tasaeuropromotorparametro.fechainicio, "+
					"public.tasaeuropromotorparametro.tasaeuro, "+
					"public.porcentcomisiontxparampromo.porcentaje, "+
					"public.promotor.asesor, "+
					"pais1.nombre, "+
					"public.porcentcomisiontxparampromo.consecutivo"; 
		
		te = entityManager.createNativeQuery(sql).getResultList();
		
		return te;
		
	}
	
	
	private List<Object[]> consultarTasasDolarPorcentajesPromotor(){
		
		List<Object[]> te=null;
		
		//Busca las tasas negociadas que esten por debajo de la nueva tasa global
		String sql="SELECT tasadolarpromotorparametro.consecutivo, "+
					"public.personal.nombre ||' '|| public.personal.apellido, "+
					"public.establecimiento.nombreestable, "+
					"public.banco.nombrebanco, "+
					"public.franquicia.nombrefranquicia, "+
					"public.tasadolarpromotorparametro.fechainicio, "+
					"public.tasadolarpromotorparametro.tasadolar, "+
					"public.porcentcomisiontxparampromo.porcentaje, "+
					"public.promotor.asesor, "+
					"pais1.nombre, "+
					"public.porcentcomisiontxparampromo.consecutivo as idporcentaje "+
					"FROM "+
					"public.tasadolarpromotorparametro "+
					"LEFT OUTER JOIN public.banco ON (public.tasadolarpromotorparametro.codbanco = public.banco.codbanco) "+
					"LEFT OUTER JOIN public.franquicia ON (public.tasadolarpromotorparametro.codfranquicia = public.franquicia.codfranquicia) "+
					"LEFT OUTER JOIN public.establecimiento ON (public.tasadolarpromotorparametro.codigounico = public.establecimiento.codigounico) "+
					"INNER JOIN public.promotor ON (public.tasadolarpromotorparametro.documento = public.promotor.documento) "+
					"INNER JOIN public.personal ON (public.promotor.documento = public.personal.documento) "+
					"INNER JOIN public.pais pais1 ON (public.tasadolarpromotorparametro.codpais = pais1.codigopais), "+
					"public.porcentcomisiontxparampromo "+
					"WHERE "+
					"public.porcentcomisiontxparampromo.codpais =   public.tasadolarpromotorparametro.codpais AND "+
					"public.porcentcomisiontxparampromo.documento =   public.tasadolarpromotorparametro.documento AND "+
					"(public.porcentcomisiontxparampromo.codigounico = public.tasadolarpromotorparametro.codigounico OR "+
					"(public.porcentcomisiontxparampromo.codigounico IS NULL AND public.tasadolarpromotorparametro.codigounico IS NULL) ) AND "+
					"(public.porcentcomisiontxparampromo.codfranquicia = public.tasadolarpromotorparametro.codfranquicia OR "+
					"(public.porcentcomisiontxparampromo.codfranquicia IS NULL AND public.tasadolarpromotorparametro.codfranquicia IS NULL )) AND "+
					"(public.porcentcomisiontxparampromo.codbanco = public.tasadolarpromotorparametro.codbanco OR "+
					"(public.porcentcomisiontxparampromo.codbanco IS NULL AND public.tasadolarpromotorparametro.codbanco IS NULL)) AND "+
					"(public.porcentcomisiontxparampromo.tipocupo = public.tasadolarpromotorparametro.tipocupo OR "+
					"(public.porcentcomisiontxparampromo.tipocupo IS NULL AND public.tasadolarpromotorparametro.tipocupo IS NULL) )AND "+
					"public.porcentcomisiontxparampromo.fechainicio =  public.tasadolarpromotorparametro.fechainicio AND "+
					" public.tasadolarpromotorparametro.fechafin is NULL AND "+
					"(public.tasadolarpromotorparametro.tasadolar<"+this.getTasaDolarTemp()+
					" OR  public.porcentcomisiontxparampromo.porcentaje<"+this.getPorcentCt()+") AND "+
					" public.tasadolarpromotorparametro.codpais='"+this.getPaisTemp().getCodigopais()+"' "+
					" GROUP BY "+
					"tasadolarpromotorparametro.consecutivo, "+
					"public.personal.nombre, "+
					"public.personal.apellido, "+
					"public.establecimiento.nombreestable, "+
					"public.banco.nombrebanco, "+
					"public.franquicia.nombrefranquicia, "+
					"public.tasadolarpromotorparametro.fechainicio, "+
					"public.tasadolarpromotorparametro.tasadolar, "+
					"public.porcentcomisiontxparampromo.porcentaje, "+
					"public.promotor.asesor, "+
					"pais1.nombre, "+
					"public.porcentcomisiontxparampromo.consecutivo"; 
		
		te = entityManager.createNativeQuery(sql).getResultList();
		
		return te;
		
	}
	
	public void validarCamposFormulario(){
		// Se implementa proceso de validacion del formulario 
		// con alerta de interfaz.
	}
	
	///////////////////////////////
	// Metodos para Grabar Tasas //
	///////////////////////////////
	
	/**
	 * Persiste Tasa EURO PROMOTOR		
	 * @return
	 */
	private String grabarTasaEuroPromotor(){
		
		log.info("");
		log.info("GRABANDO TASA EURO PROMOTOR");
		log.info("");
		
		this.tsEuroPromo = new Tasaeuropromotorparametro();
		this.tsEuroPromo.setPais(this.getPaisTemp());
		this.tsEuroPromo.setPromotor(this.getPromoTemp());
		this.tsEuroPromo.setEstablecimiento(this.getEstaTemp());
		this.tsEuroPromo.setFranquicia(this.getFrqTemp());
		this.tsEuroPromo.setBanco(this.getBancoTemp());
		this.tsEuroPromo.setFechainicio(this.getFechaIniTemp());
		this.tsEuroPromo.setFechafin(this.getFechaFinTemp());
		this.tsEuroPromo.setTasaeuro( this.getTasaEuroNegTemp() );		
		this.tsEuroPromo.setTipocupo(this.getTipoCupoTemp());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('tasaeuropromo_consecutivo_seq')").getSingleResult();			
		this.tsEuroPromo.setConsecutivo(conse.intValue());
		
		entityManager.persist(tsEuroPromo);			
		entityManager.flush();
		entityManager.clear();
		
		statusMessages.add("Se ha registrado tasa de dolar para el pais " + 
					this.getPaisTemp().getNombre() + " ");
		tsEuroPromo.setPais(null);
		tsEuroPromo.setPromotor(null);
		tsEuroPromo.setEstablecimiento(null);
		tsEuroPromo.setFranquicia(null);
		tsEuroPromo.setBanco(null);
		tsEuroPromo.setTipocupo(null);
		tsEuroPromo.setFechainicio(null);
		tsEuroPromo.setFechafin(null);
		tsEuroPromo.setTasaeuro(null);
		
		return "persisted";
	}
	
	/**
	 * Persiste Tasa EURO GLOBAL		
	 * @return
	 */
	private String grabarTasaEuroGlobal(){
		
		log.info("");
		log.info("GRABANDO TASA EURO GENERAL");
		log.info("");
		
		this.tsEuroParam = new Tasaeuroparametro();
		this.tsEuroParam.setPais(this.getPaisTemp());
		this.tsEuroParam.setEstablecimiento(this.getEstaTemp());
		this.tsEuroParam.setFranquicia(this.getFrqTemp());
		this.tsEuroParam.setBanco(this.getBancoTemp());
		this.tsEuroParam.setFechainicio(this.getFechaIniTemp());
		this.tsEuroParam.setFechafin(this.getFechaFinTemp());
		this.tsEuroParam.setTasaeuro(this.getTasaEuroTemp());
		this.tsEuroParam.setTasaeuroTac(this.getTasaEuroTacTemp());
		this.tsEuroParam.setTipocupo(this.getTipoCupoTemp());
		this.tsEuroParam.setFechamod(new Date());
		this.tsEuroParam.setUsuariomod(identity.getUsername());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('tasaeuroparam_consecutivo_seq')").getSingleResult();			
		this.tsEuroParam.setConsecutivo(conse.intValue());
		
		entityManager.persist(tsEuroParam);			
		entityManager.flush();
		entityManager.clear();
	
		
		statusMessages.add("Se ha registrado tasa de dolar para el pais " + 
					this.getPaisTemp().getNombre() + " ");
		tsEuroParam.setPais(null);
		tsEuroParam.setEstablecimiento(null);
		tsEuroParam.setFranquicia(null);
		tsEuroParam.setBanco(null);
		tsEuroParam.setTipocupo(null);
		tsEuroParam.setFechainicio(null);
		tsEuroParam.setFechafin(null);
		tsEuroParam.setTasaeuro(null);
		
		return "persisted";
	}
	
	/**
	 * Persiste Tasa DOLAR PROMOTOR		
	 * @return
	 */
	private String grabarTasaDolarPromotor(){	
		
		log.info("");
		log.info("GRABANDO TASA DOLAR PROMOTOR");
		log.info("");
		
		this.tsDolarPromo = new Tasadolarpromotorparametro();
		this.tsDolarPromo.setPais(this.getPaisTemp());
		this.tsDolarPromo.setPromotor(this.getPromoTemp());
		this.tsDolarPromo.setEstablecimiento(this.getEstaTemp());
		this.tsDolarPromo.setFranquicia(this.getFrqTemp());
		this.tsDolarPromo.setBanco(this.getBancoTemp());
		this.tsDolarPromo.setFechainicio(this.getFechaIniTemp());
		this.tsDolarPromo.setFechafin(this.getFechaFinTemp());
		this.tsDolarPromo.setTasadolar( this.getTasaDolarNegTemp());
		this.tsDolarPromo.setTipocupo(this.getTipoCupoTemp());
		this.tsDolarPromo.setFechamod(new Date());
		this.tsDolarPromo.setUsuariomod(identity.getUsername());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('tasadolarpromoparam_consecutivo_seq')").getSingleResult();			
		this.tsDolarPromo.setConsecutivo(conse.intValue());
		
		entityManager.persist(tsDolarPromo);			
		entityManager.flush();
		entityManager.clear();
		
		statusMessages.add("Se ha registrado tasa de dolar para el pais " + 
					this.getPaisTemp().getNombre() + " ");
		tsDolarPromo.setPais(null);
		tsDolarPromo.setPromotor(null);
		tsDolarPromo.setEstablecimiento(null);
		tsDolarPromo.setFranquicia(null);
		tsDolarPromo.setBanco(null);
		tsDolarPromo.setTipocupo(null);
		tsDolarPromo.setFechainicio(null);
		tsDolarPromo.setFechafin(null);
		tsDolarPromo.setTasadolar(null);
		
		return "persisted";
	}
	
	
	/**
	 * Persiste Tasa DOLAR GLOBAL		
	 * @return
	 */
	private String grabarTasaDolarGlobal(){
		
		this.tsDolarParam = new Tasadolarparametro();
		this.tsDolarParam.setPais(this.getPaisTemp());
		this.tsDolarParam.setEstablecimiento(this.getEstaTemp());
		this.tsDolarParam.setFranquicia(this.getFrqTemp());
		this.tsDolarParam.setBanco(this.getBancoTemp());
		this.tsDolarParam.setFechainicio(this.getFechaIniTemp());
		this.tsDolarParam.setFechafin(this.getFechaFinTemp());
		this.tsDolarParam.setTasadolar(this.getTasaDolarTemp());
		this.tsDolarParam.setTasadolarTac(this.getTasaDolarTacTemp());
		this.tsDolarParam.setTipocupo(this.getTipoCupoTemp());
		this.tsDolarParam.setFechamod(new Date());
		this.tsDolarParam.setUsuariomod(identity.getUsername());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('tasadolarparam_consecutivo_seq')").getSingleResult();			
		this.tsDolarParam.setConsecutivo(conse.intValue());
		
		entityManager.persist(tsDolarParam);			
		entityManager.flush();
		entityManager.clear();
		
		statusMessages.add("Se ha registrado tasa de dolar para el pais " + 
					this.getPaisTemp().getNombre() + " ");
		tsDolarParam.setPais(null);
		tsDolarParam.setEstablecimiento(null);
		tsDolarParam.setFranquicia(null);
		tsDolarParam.setBanco(null);
		tsDolarParam.setTipocupo(null);
		tsDolarParam.setFechainicio(null);
		tsDolarParam.setFechafin(null);
		this.tsDolarParam.setTasadolar(null);
		this.tsDolarParam.setTasadolarTac(null);
		
		return "persisted";
	}
	
	
	/**
	 * 
	 */
	private void grabarComisionNegociada(){
		log.info("");
		log.info("GRABANDO PORCENTAJE PARA EL PROMOTOR ");
		log.info("");
		
		this.porcentajePromo = new Porcentcomisiontxparampromo();
		this.porcentajePromo.setPais(this.getPaisTemp());
		this.porcentajePromo.setPromotor(this.getPromoTemp());
		this.porcentajePromo.setEstablecimiento(this.getEstaTemp());
		this.porcentajePromo.setFranquicia(this.getFrqTemp());
		this.porcentajePromo.setBanco(this.getBancoTemp());
		this.porcentajePromo.setFechainicio(this.getFechaIniTemp());
		this.porcentajePromo.setFechafin(this.getFechaFinTemp());
		this.porcentajePromo.setPorcentaje(this.getPorcentCt());
		this.porcentajePromo.setTipocupo(this.getTipoCupoTemp());
		this.porcentajePromo.setFechamod(new Date());
		this.porcentajePromo.setUsuariomod(identity.getUsername());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('porcentpromo_consecutivo_seq')").getSingleResult();			
		this.porcentajePromo.setConsecutivo(conse.intValue());
		
		entityManager.persist(porcentajePromo);			
		entityManager.flush();
		entityManager.clear();
		
		porcentajePromo.setPais(null);
		porcentajePromo.setPromotor(null);
		porcentajePromo.setEstablecimiento(null);
		porcentajePromo.setFranquicia(null);
		porcentajePromo.setBanco(null);
		porcentajePromo.setTipocupo(null);
		porcentajePromo.setFechainicio(null);
		porcentajePromo.setFechafin(null);
		porcentajePromo.setPorcentaje(null);
		
	}
	
	/**
	 *		 
	 */
	private void grabarComisionGlobal(){
		
		log.info("");
		log.info("GRABANDO PORCENTAJE GLOBAL");
		log.info("");
		
		this.porcentajeGlob = new Porcentajecomisiontxparam();
		this.porcentajeGlob.setPais(this.getPaisTemp());
		this.porcentajeGlob.setEstablecimiento(this.getEstaTemp());
		this.porcentajeGlob.setFranquicia(this.getFrqTemp());
		this.porcentajeGlob.setBanco(this.getBancoTemp());
		this.porcentajeGlob.setFechainicio(this.getFechaIniTemp());
		this.porcentajeGlob.setFechafin(this.getFechaFinTemp());
		this.porcentajeGlob.setPorcentaje(this.getPorcentCt());
		this.porcentajeGlob.setTipocupo(this.getTipoCupoTemp());
		this.porcentajeGlob.setFechamod(new Date());
		this.porcentajeGlob.setUsuariomod(identity.getUsername());
		
		//Genera el consecutivo de la tabla (id del registro)
		BigInteger conse = (BigInteger)entityManager.createNativeQuery( 
						"select nextval('porcent_consecutivo_seq')").getSingleResult();			
		this.porcentajeGlob.setConsecutivo(conse.intValue());
		
		entityManager.persist(porcentajeGlob);			
		entityManager.flush();
		entityManager.clear();
		
		porcentajeGlob.setPais(null);
		porcentajeGlob.setEstablecimiento(null);
		porcentajeGlob.setFranquicia(null);
		porcentajeGlob.setBanco(null);
		porcentajeGlob.setTipocupo(null);
		porcentajeGlob.setFechainicio(null);
		porcentajeGlob.setFechafin(null);
		porcentajeGlob.setPorcentaje(null);
	}
	
	/**
	 * Persiste los valores de liquidacion de las transaccion 
	 * para el ESTABLECIMIENTO
	 * @param moneda
	 * @return
	 */
	public String grabarPrecioComercio(Boolean moneda){
		
		log.info("");
		log.info("GRABANDO PORCENTAJE Y DOLAR DEL COMERCIO");
		log.info("");

		EstablecimientoprecioId idEst;
		idEst = new  EstablecimientoprecioId( 
				this.getEstaTemp().getCodigounico(), this.fechaIniTemp );
		
		//Valida que ya no este grabado los parametros 
		Establecimientoprecio est = entityManager.find(Establecimientoprecio.class, idEst);
		
		if( est == null){
			est = new Establecimientoprecio(idEst, this.getEstaTemp());
			if( moneda ){//si es euro
				est.setDolaroficina( this.getTasaEuroOfTemp());
			}else{
				est.setDolaroficina(this.getTasaDolarOfTemp());
			}
			
			est.setParidad(this.getParidadEstTemp());
			est.setParidadCliente(this.getParidadClienteTemp());
			est.setPorcentajeoficina(this.getPorcentOfi());
			est.setFechamod(new Date());
			est.setUsuariomod(identity.getUsername());
			
			entityManager.persist(est);			
			entityManager.flush();
			entityManager.clear();
			
			return "persisted";
		}else{
			facesMessages.addToControl("nameestable",
			"Ya estan guardados los parametros para este establecimiento para esta fecha");
			return "Ya se tiene grabada una tasa para este pais en la fecha indicada";
			
		}
	}
	
	//////////////////////////////////////////
	// Metodos para Cerrar Tasas Anteriores //
	//////////////////////////////////////////
	
	/**
	 * Cierra Tasa Euro Global
	 * 
	 */
	public void cerrarTasaEuroGlobal(){
		try {
			log.info("Cerrando Tasa Euro Global");
			
			Tasaeuroparametro tEuroGlobal = null;
			try{
				String queryString = 
					"select tsp from Tasaeuroparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
					
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				tEuroGlobal = (Tasaeuroparametro) 
										entityManager.createQuery(queryString).getSingleResult();			
			}catch( NoResultException e ){
				//Por implementar...
			}
			
			if( tEuroGlobal != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
				tEuroGlobal.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(tEuroGlobal);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cierra Tasa Euro Promotor
	 * 
	 */
	public void cerrarTasaEuroPromotor(){
		try {
			log.info("Cerrando Tasa Euro Promotor");
			
			Tasaeuropromotorparametro tEuroPromotor = null;
			try{
				String queryString = 
					"select tsp from Tasaeuropromotorparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getPromoTemp() != null ){
					queryString += " and tsp.promotor.documento = '" + this.getPromoTemp().getDocumento() + "'";
				}else{
					queryString += " and tsp.promotor.documento =  null";
				}
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				tEuroPromotor = (Tasaeuropromotorparametro) 
										entityManager.createQuery(queryString).getSingleResult();				
			}catch( NoResultException e ){
				//Por implementar..
			}
			
			if( tEuroPromotor != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        tEuroPromotor.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(tEuroPromotor);
				entityManager.flush();
				entityManager.clear();
				log.info("EURO PORMOTOR CERRADA");
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Cierra Tasa Dolar Global
	 * 
	 */
	public void cerrarTasaDolarGlobal(){
		try {
			log.info("Cerrando Tasa Dolar Global");
			
			Tasadolarparametro tDolarGlobal = null;
			try{
				String queryString = 
					"select tsp from Tasadolarparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
					
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				tDolarGlobal = (Tasadolarparametro) 
										entityManager.createQuery(queryString).getSingleResult();					
			}catch( NoResultException e ){
				//Por implementar...
			}
			
			if( tDolarGlobal != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        tDolarGlobal.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(tDolarGlobal);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Cierra Tasa Dolar Promotor
	 * 
	 */
	public void cerrarTasaDolarPromotor(){
		try {
			log.info("Cerrando Tasa Dolar Promotor");
			
			Tasadolarpromotorparametro tDolarPr = null;
			try{
				String queryString = 
					"select tsp from Tasadolarpromotorparametro tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getPromoTemp() != null ){
					queryString += " and tsp.promotor.documento = '" + this.getPromoTemp().getDocumento() + "'";
				}else{
					queryString += " and tsp.promotor.documento =  null";
				}
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				tDolarPr = (Tasadolarpromotorparametro) 
										entityManager.createQuery(queryString).getSingleResult();			
			}catch( NoResultException e ){
				//Por implementar..
			}
			
			if( tDolarPr != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        tDolarPr.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(tDolarPr);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cierra Tasa Comision Negociada
	 * 
	 */
	public void cerrarComisionNegociada(){
		try {
			log.info("");
			log.info("Cerrando Comision Negociada");
			
			Porcentcomisiontxparampromo comiPromo = null;
			try{
				String queryString = 
					"select tsp from Porcentcomisiontxparampromo tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getPromoTemp() != null ){
					queryString += " and tsp.promotor.documento = '" + this.getPromoTemp().getDocumento() + "'";
				}else{
					queryString += " and tsp.promotor.documento =  null";
				}
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				comiPromo = (Porcentcomisiontxparampromo) 
										entityManager.createQuery(queryString).getSingleResult();					
			}catch( NoResultException e ){
				//Por implementar..
			}
			
			if( comiPromo != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        comiPromo.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(comiPromo);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Cierra Tasa Comision Global
	 * 
	 */
	public void cerrarComisionGlobal(){
		try {
			log.info("");
			log.info("Cerrando Porcentaje Global");
			
			Porcentajecomisiontxparam porcentajeGlob = null;
			try{
				
				String queryString = 
					"select tsp from Porcentajecomisiontxparam tsp where tsp.fechainicio != null and " +
					"tsp.fechainicio < current_date and tsp.fechafin is null and " +
					"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
				
				if( this.getEstaTemp() != null ){
					queryString += " and tsp.establecimiento.codigounico= '" +
										this.getEstaTemp().getCodigounico() + "'";
				}else{
					queryString += " and tsp.establecimiento.codigounico = null";
				}
				if( this.getFrqTemp() != null ){
					queryString += " and tsp.franquicia.codfranquicia = '" +
										this.getFrqTemp().getCodfranquicia() + "'";
				}else{
					queryString += " and tsp.franquicia.codfranquicia = null";
					
				}
				if( this.getBancoTemp() != null ){
					queryString += " and tsp.banco.codbanco = '" + 
										this.getBancoTemp().getCodbanco() + "'";
				}else{
					queryString += " and tsp.banco.codbanco = null";
				}
				porcentajeGlob = (Porcentajecomisiontxparam) 
										entityManager.createQuery(queryString).getSingleResult();						
			}catch( NoResultException e ){
				//Por implementar...
			}
			
			if( porcentajeGlob != null ){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        porcentajeGlob.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(porcentajeGlob);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Cierra los parametros del establecimiento (los precios de 
	 * OFICINA)
	 */
	public void cerrarParametrosComercio(){
		try {
			log.info("");
			log.info("CerrandoParametros Establecimiento");
			
			Establecimientoprecio  estPrecio = null;
			try{
				String queryString = "select est from Establecimientoprecio est where " + 
					" est.id.fechainicio = ( select max( es.id.fechainicio ) from Establecimientoprecio es) " +
					"and est.id.codigounico ='" + this.getEstaTemp().getCodigounico() +"'" ;
				estPrecio =  (Establecimientoprecio) 
								entityManager.createQuery(queryString).getSingleResult();				
			}catch( NoResultException e ){
				//Por implementar...
			}				
			if( estPrecio != null ){
				
				log.info( estPrecio != null );
				log.info("ESTABLECIMIENTO: " + 
						estPrecio.getEstablecimiento().getNombreestable());
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fin;
				
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date()); 
		        calendar.add(Calendar.DAY_OF_YEAR, -1);//restamos 1 dia a la fecha actual
		        fin = sdf.parse(sdf.format( calendar.getTime()));
									
		        estPrecio.setFechafin(fin);//Establece la fecha fin
				entityManager.merge(estPrecio);
				entityManager.flush();
				entityManager.clear();
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/////////////////////////////////////////////////
	// Metodos para Buscar Tasas de la misma Fecha //
	/////////////////////////////////////////////////
	
	public boolean buscarEuroActualGlobal(){	
		
		log.info("");
		log.info("Buscando Tasa Euro Global para esta fecha");
		
		Tasaeuroparametro tEuroGlobal = null;
		try{
			//HQL
			String queryString = 
				"select tsp from Tasaeuroparametro tsp where tsp.fechainicio != null and " +
				"tsp.fechainicio = '"+ this.getFechaIniTemp() +"' and tsp.fechafin is null and " +
				"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
			
			if( this.getEstaTemp() != null ){
				queryString += " and tsp.establecimiento.codigounico= '" +
									this.getEstaTemp().getCodigounico() + "'";
			}else{
				queryString += " and tsp.establecimiento.codigounico = null";
			}
			if( this.getFrqTemp() != null ){
				queryString += " and tsp.franquicia.codfranquicia = '" +
									this.getFrqTemp().getCodfranquicia() + "'";
			}else{
				queryString += " and tsp.franquicia.codfranquicia = null";
				
			}
			if( this.getBancoTemp() != null ){
				queryString += " and tsp.banco.codbanco = '" + 
									this.getBancoTemp().getCodbanco() + "'";
			}else{
				queryString += " and tsp.banco.codbanco = null";
			}
			tEuroGlobal = (Tasaeuroparametro) 
									entityManager.createQuery(queryString).getSingleResult();
					
		}catch( NoResultException e ){
			//Por implementar...
		}			
		if( tEuroGlobal != null ){
			return true;
		}
		return false;
	}
	
	
	public boolean buscarEuroActualPromotor(){		
		
		log.info("");
		log.info("Buscando Tasa Euro Promotor para esta fecha");
		
		Tasaeuropromotorparametro tEuroPromotor = null;
		try{
			String queryString = 
				"select tsp from Tasaeuropromotorparametro tsp where tsp.fechainicio != null and " +
				"tsp.fechainicio = '"+ this.getFechaIniTemp() +"' and tsp.fechafin is null and " +
				"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
			
			if( this.getPromoTemp() != null ){
				queryString += " and tsp.promotor.documento = '" + this.getPromoTemp().getDocumento() + "'";
			}else{
				queryString += " and tsp.promotor.documento =  null";
			}
			if( this.getEstaTemp() != null ){
				queryString += " and tsp.establecimiento.codigounico= '" +
									this.getEstaTemp().getCodigounico() + "'";
			}else{
				queryString += " and tsp.establecimiento.codigounico = null";
			}
			if( this.getFrqTemp() != null ){
				queryString += " and tsp.franquicia.codfranquicia = '" +
									this.getFrqTemp().getCodfranquicia() + "'";
			}else{
				queryString += " and tsp.franquicia.codfranquicia = null";
				
			}
			if( this.getBancoTemp() != null ){
				queryString += " and tsp.banco.codbanco = '" + 
									this.getBancoTemp().getCodbanco() + "'";
			}else{
				queryString += " and tsp.banco.codbanco = null";
			}
			tEuroPromotor = (Tasaeuropromotorparametro) 
									entityManager.createQuery(queryString).getSingleResult();
					
		}catch( NoResultException e ){
			//Por implementar...
		}			
		if( tEuroPromotor != null ){
			return true;
		}
		return false;
	}
	
	
	
	public boolean buscarDolarActualGlobal(){	
		
		log.info("");
		log.info("Buscando Tasa Dolar Global para esta fecha");
		
		Tasadolarparametro tDolarGlobal = null;
		try{
			//HQL
			String queryString = 
				"select tsp from Tasadolarparametro tsp where tsp.fechainicio != null and " +
				"tsp.fechainicio = '"+ this.getFechaIniTemp() +"' and tsp.fechafin is null and " +
				"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
			
			if( this.getEstaTemp() != null ){
				queryString += " and tsp.establecimiento.codigounico= '" +
									this.getEstaTemp().getCodigounico() + "'";
			}else{
				queryString += " and tsp.establecimiento.codigounico = null";
			}
			if( this.getFrqTemp() != null ){
				queryString += " and tsp.franquicia.codfranquicia = '" +
									this.getFrqTemp().getCodfranquicia() + "'";
			}else{
				queryString += " and tsp.franquicia.codfranquicia = null";
				
			}
			if( this.getBancoTemp() != null ){
				queryString += " and tsp.banco.codbanco = '" + 
									this.getBancoTemp().getCodbanco() + "'";
			}else{
				queryString += " and tsp.banco.codbanco = null";
			}
			tDolarGlobal = (Tasadolarparametro) 
									entityManager.createQuery(queryString).getSingleResult();
					
		}catch( NoResultException e ){
			//Por implementar...
		}			
		if( tDolarGlobal != null ){
			return true;
		}
		return false;
	}
	
	
	
	public boolean buscarDolarActualPromotor(){		
		
		log.info("");
		log.info("Buscando Tasa Dolar Promotor para esta fecha");
		
		Tasadolarpromotorparametro tDolarPromo = null;
		try{
			String queryString = 
				"select tsp from Tasadolarpromotorparametro tsp where tsp.fechainicio != null and " +
				"tsp.fechainicio = '"+ this.getFechaIniTemp() +"' and tsp.fechafin is null and " +
				"tsp.tipocupo = '" + this.getTipoCupoTemp() + "'";
			
			if( this.getPromoTemp() != null ){
				queryString += " and tsp.promotor.documento = '" + this.getPromoTemp().getDocumento() + "'";
			}else{
				queryString += " and tsp.promotor.documento =  null";
			}
			if( this.getEstaTemp() != null ){
				queryString += " and tsp.establecimiento.codigounico= '" +
									this.getEstaTemp().getCodigounico() + "'";
			}else{
				queryString += " and tsp.establecimiento.codigounico = null";
			}
			if( this.getFrqTemp() != null ){
				queryString += " and tsp.franquicia.codfranquicia = '" +
									this.getFrqTemp().getCodfranquicia() + "'";
			}else{
				queryString += " and tsp.franquicia.codfranquicia = null";
				
			}
			if( this.getBancoTemp() != null ){
				queryString += " and tsp.banco.codbanco = '" + 
									this.getBancoTemp().getCodbanco() + "'";
			}else{
				queryString += " and tsp.banco.codbanco = null";
			}
			tDolarPromo = (Tasadolarpromotorparametro) 
									entityManager.createQuery(queryString).getSingleResult();
					
		}catch( NoResultException e ){
			//Por implementar...
		}			
		if( tDolarPromo != null ){
			return true;
		}
		return false;
	}

	public void redirect(){
		System.out.println("ya");
	}
    
    
}
