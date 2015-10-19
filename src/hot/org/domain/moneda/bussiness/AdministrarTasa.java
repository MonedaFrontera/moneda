package org.domain.moneda.bussiness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Pais;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tasabolivarnegociado;
import org.domain.moneda.entity.TasabolivarnegociadoId;
import org.domain.moneda.entity.Tasadebolivaroficina;
import org.domain.moneda.entity.TasadebolivaroficinaId;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.TasadolarId;
import org.domain.moneda.entity.Transaccion;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TarjetaHome;
import org.domain.moneda.session.TasabolivarnegociadoHome;
import org.domain.moneda.session.TasadebolivaroficinaHome;
import org.domain.moneda.session.TasadolarHome;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.international.StatusMessages;

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
    
    List<Tasabolivarnegociado> listaTasabolivarnegociado = new ArrayList<Tasabolivarnegociado>();
    
    @In(create=true) @Out 
	private PromotorHome promotorHome;
    
    @In(create=true) @Out
    AdministrarUsuario AdministrarUsuario;
    
    public List<Tasadebolivaroficina> getListaTasadebolivaroficina() {
		return listaTasadebolivaroficina;
	}

	public void setListaTasadebolivaroficina(
			List<Tasadebolivaroficina> listaTasadebolivaroficina) {
		this.listaTasadebolivaroficina = listaTasadebolivaroficina;
	}
	
	Date fecha = null;
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void buscarTasaBolivarOficina(){
		String sql = "select t from Tasadebolivaroficina t where 1=1 ";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		if(this.fecha != null){
			sql = sql + " and t.id.fecha = '"+sdf.format(fecha)+"'";
		}
		
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
    	
    	System.out.println("Edicion Tasa Dolar");
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
    	this.pais = null;
    	this.fecha = null;
    	this.listaTasadolar =null;
    	
    }
    

    
    
public void editarTasabolivaroficina(Date fecha, String tipo){
    	
    	System.out.println("Edicion Tasa Dolar Bolivar");
    	
    	
    	Tasadebolivaroficina td = (Tasadebolivaroficina)entityManager
		.createQuery("select t from Tasadebolivaroficina t where t.id.fecha = '"+fecha+"' and t.id.tipo = '"+tipo+"'")
		.getSingleResult();
    	
    	tasadebolivaroficinaHome.setTasadebolivaroficinaId(new TasadebolivaroficinaId(fecha,tipo));
    	
    	
    	
    }


public void editarTasabolivarnegociado(Date fecha, String tipo, String documento){
	
	System.out.println("Edicion Tasa Dolar Bolivar");
	
	
	Tasabolivarnegociado td = (Tasabolivarnegociado)entityManager
	.createQuery("select t from Tasabolivarnegociado t where t.id.fecha = '"+fecha+"' and t.id.tipo = '"+tipo+"' " +
			"and t.id.documento='"+documento+"'")
	.getSingleResult();
	System.out.println("1");
	if (td!=null)
	tasabolivarnegociadoHome.setTasabolivarnegociadoId(td.getId());
	
	System.out.println("2");
	System.out.println(tasabolivarnegociadoHome.getInstance().getId().getDocumento());
	System.out.println("3");
	
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

    		System.out.println(" ***************** SQL " + sql);
    		
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
    		
    		System.out.println(" ***************** SQL " + sql);
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
    
    List<String> lista = new ArrayList<String>();
    
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
		System.out.println(">>>Tiempo total de la busqueda: " + t2 + "ms");		
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
			System.out.println("Promotor " + pr.getDocumento());
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
		
		System.out.println("*-*-*-*-*-*-*-* Tasa Bolivar Negociado " + this.nombre);
		
		System.out.println("*-*-*-*-*-*-*-* Tasa Bolivar Negociado " + promotorHome.getInstance().getDocumento());
		
		
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

	public List<Tasadolar> getListaTasadolar() {
		return listaTasadolar;
	}

	public void setListaTasadolar(List<Tasadolar> listaTasadolar) {
		this.listaTasadolar = listaTasadolar;
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
	
	
	
    
    
}
