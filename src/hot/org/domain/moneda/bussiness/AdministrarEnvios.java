package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;
import static org.jboss.seam.ScopeType.SESSION;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Depositostarjeta;
import org.domain.moneda.entity.Envios;
import org.domain.moneda.entity.Gastos;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Tipogasto;
import org.domain.moneda.session.AsesorHome;
import org.domain.moneda.session.EnviosHome;
import org.domain.moneda.session.EnviosList;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarEnvios")
@Scope(CONVERSATION)
public class AdministrarEnvios
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In
	private EntityManager entityManager;
    
    @In private FacesMessages facesMessages;
    
    List<String> lista = new ArrayList<String>();
    
    String nombre = "";
    
    @In(create=true) @Out 
	private PromotorHome promotorHome;
    
    @In(create=true) @Out
    private EnviosHome enviosHome;
    
    @In(create=true)
	private AdministrarVariable AdministrarVariable;
    
   
    private Integer anioEnvio;
    
    private Boolean programados = true;
    
    private Boolean enviados = false;
    
    private String estado = "t";

    public void administrarEnvios()
    {
        // implement your business logic here
        log.info("AdministrarEnvios.administrarEnvios() action called");
        statusMessages.add("administrarEnvios");
    }    
    
    
    @In Identity identity;
    public void llenarPromotores(){
		entityManager.clear();
		String sql = "";
		
		System.out.println("Asesor "+AdministrarVariable.getAsesor().getDocumento());
		
		if(identity.hasRole("Asesor")){
			sql = " where p.asesor.documento = '"+identity.getUsername()+"'";
		}
		List<String> resultList = entityManager
		.createQuery("select p.personal.nombre||' '||p.personal.apellido from Promotor p " + sql)
		.getResultList();
		lista = resultList;
	}
        
    public List<String> autocompletar(Object nombre) {
		llenarPromotores(); 							// Metodo que carga la informacion de los nombres de las personas
		String pref = (String) nombre;
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
    
    @In(create=true)
	private CargarObjetos CargarObjetos;
    
    public void ubicarPromotor(){
    	
    	Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
		if(pr!=null){
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			
			enviosHome.getInstance().setPromotor(pr);
		}		
	}
    
    public Integer getAnio() {
		return anioEnvio;
	}


	public void setAnio(Integer anioEnvio) {
		
		this.anioEnvio = anioEnvio;
	}    
    
    @In(create=true) @Out 
	private EnviosList enviosList;
    
    public void ubicarPromotorList(){
		
		if(this.nombre != null && !this.nombre.contentEquals("")){
    	Promotor pr = CargarObjetos.ubicarPromotor(this.nombre);
    	
		if(pr!=null){
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			enviosList.setNombre(pr.getPersonal().getNombre()+ " "+pr.getPersonal().getApellido());
			System.out.println("Promotor List " + enviosList.getNombre());
			//enviosList.setPromotor(pr);
		}else{
			enviosList.setNombre(null);
		}
		}else{
			promotorHome.clearInstance();
		}
		
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	
	public void guardarEnvio(){
		if(enviosHome.getInstance().getPromotor()!=null){
		BigInteger query = (BigInteger)entityManager
		.createNativeQuery("select nextval('envios_consecutivo_seq')").getSingleResult();
		enviosHome.getInstance().setConsecutivo(query.intValue());
		if(identity.hasRole("Asesor")){
			AsesorHome as = new AsesorHome();
			as.setAsesorDocumento(identity.getUsername());
		
			enviosHome.getInstance().setAsesor(as.getInstance());
		
		}
		enviosHome.getInstance().setEnviado(false);
		enviosHome.getInstance().setRecibido(false);
		enviosHome.getInstance().setFecha(new Date());
		enviosHome.persist();
		promotorHome.clearInstance();
		
		}else{
			this.facesMessages.add("No hay un promotor asociado a este envio, por favor verifique");
			return;
		}
		
		this.nombre = "";
		
	}
	
	public void actualizarEnvio(){
		this.nombre = "";
		enviosHome.update();
		this.envioss = null;
		
	}
	
	@DataModel
	private List<Envios> enviosDataModel=new ArrayList<Envios>();

	public List<Envios> getEnviosDataModel() {
		return enviosDataModel;
	}

	public void setEnviosDataModel(List<Envios> enviosDataModel) {
		this.enviosDataModel = enviosDataModel;
	}
	
	
	public void buscarEnviosAsesora(){
		entityManager.clear();
		String sql = "select e from Envios e where 1=1 ";
		if(this.estado.contentEquals("p")){
			sql = sql + "and (e.enviado = false or e.enviado is null) ";
		}
		
		if(this.estado.contentEquals("e")){
			sql = sql + "and (e.enviado = true and e.nrocupon is null) ";
		}
		
		if(this.estado.contentEquals("m")){
			sql = sql + "and (e.enviado = true and e.nrocupon is not null and " +
					"(e.recibido <> true or e.recibido is null)) ";
		}
		
		if(this.estado.contentEquals("r")){
			sql = sql + "and (e.enviado = true and e.recibido = true) ";
		}
		
		this.enviosDataModel = entityManager.createQuery(sql).setMaxResults(70).getResultList();
	}
	
	//metodo de busqueda para la pagina EnviosProgramar.xhtml
	public void buscar(){
		entityManager.clear();
		
		String sql = "select e from Envios e where 1=1 ";
		if(this.estado.contentEquals("p")){
			sql = sql + "and (e.enviado = false or e.enviado is null) ";
		}
		
		if(this.estado.contentEquals("e")){
			sql = sql + "and (e.enviado = true and e.nrocupon is null) ";
		}
		
		if(this.estado.contentEquals("m")){
			sql = sql + "and (e.enviado = true and e.nrocupon is not null and " +
					"(e.recibido <> true or e.recibido is null)) ";
		}
		
		if(this.estado.contentEquals("r")){
			sql = sql + "and (e.enviado = true and e.recibido = true) ";
		}
		
		
		/*
		if(this.programados){
			sql = sql + "and (e.enviado = false or e.enviado is null) ";
		}
		
		if(this.enviados){
			sql = sql + "and e.enviado = true and e.nrocupon is null ";
		}
		*/
		
		if(this.envios.getEnvia()!=null && !this.envios.getEnvia().contentEquals("")){
			sql = sql + "and lower(e.envia) like lower('%" + this.envios.getEnvia() + "%') ";
		}
		
		if(this.envios.getRecibe()!=null && !this.envios.getRecibe().contentEquals("")){
			sql = sql + "and lower(e.recibe) like lower('%" + this.envios.getRecibe() + "%') ";
		}
		
		if(this.envios.getCiudad()!=null && !this.envios.getCiudad().contentEquals("")){
			sql = sql + "and lower (e.ciudad) like lower('%" + this.envios.getCiudad() + "%') ";
		}
		
		if(this.envios.getOficina()!=null && !this.envios.getOficina().contentEquals("")){
			sql = sql + "and lower (e.oficina) like lower('%" + this.envios.getOficina() + "%') ";
		}
		
		if(this.envios.getNrocupon()!=null && !this.envios.getNrocupon().contentEquals("")){
			sql = sql + "and e.nrocupon = '" + this.envios.getNrocupon() + "' ";
		}
		
		if(this.envios.getFecha()!=null){
			sql = sql + "and e.fecha = '" + this.envios.getFecha() + "' ";
		}
		
		if(this.envios.getFechaenvio()!=null){
			sql = sql + "and e.fechaenvio = '" + this.envios.getFechaenvio() + "' ";
		}
		
		this.enviosDataModel = entityManager.createQuery(sql).setMaxResults(70).getResultList();		
	}
	
	
	public void programarEnvios(List<Envios> datamodel){
		System.out.println("Prueba ");
		for(int i=0;i<datamodel.size();i++){
			Envios e = datamodel.get(i);
			System.out.println("Prueba "+e.getEnviado());
			if(e.getEnviado()!=null)
			if(e.getEnviado()){
				e.setFechaenvio(new Date());
				entityManager.clear();
				entityManager.merge(e);
				entityManager.flush();				
				/*
				BigInteger query = (BigInteger)entityManager
				.createNativeQuery("select nextval('gastos_consecutivo_seq')").getSingleResult();
		    	Gastos g = new Gastos();
		    	
		    	g.setConsecutivo(query.intValue());
		    	g.setFecha(e.getFechaenvio());
		    	
		    	g.setPersonal(e.getPromotor().getPersonal());
		    	g.setValor(new Long(25000));
		    	
		    	Tipogasto tg = entityManager.find(Tipogasto.class, "MR");
		    	
				g.setTipogasto(tg);
				
				entityManager.persist(g);
				entityManager.flush();
				*/				
			}			
		}
		entityManager.clear();
	}
	
	
	public void programarCupones(List<Envios> datamodel){
		System.out.println("Prueba ");
		for(int i=0;i<datamodel.size();i++){
			Envios e = datamodel.get(i);
			System.out.println("Prueba Cupon"+e.getNrocupon());
			System.out.println("Prueba Fecha "+e.getFechamrw());
			if(e.getEnviado()!=null)
			if(e.getFechamrw()!=null && e.getNrocupon()!=null && !e.getNrocupon().contentEquals("")){
				System.out.println("Prueba "+e.getNrocupon());
				entityManager.clear();
				entityManager.merge(e);
				entityManager.flush();
			}			
		}
		entityManager.clear();
	}
	
	//traza de revision
	public void registrarFecha(Envios e){
		System.out.println("Fecha " + e.getFechamrw());
		System.out.println("Cupon " + e.getNrocupon());
	}
	
	public void marcarEntrega(int numenvio){
		entityManager.createNativeQuery("update public.envios set recibido = true " +
				"where consecutivo = "+numenvio+"").executeUpdate();
		entityManager.flush();
		entityManager.clear();
		
	}
	
	private Envios envios = new Envios();

	public Envios getEnvios() {
		return envios;
	}


	public void setEnvios(Envios envios) {
		this.envios = envios;
	}


	public Boolean getProgramados() {
		return programados;
	}


	public void setProgramados(Boolean programados) {
		this.programados = programados;
	}


	public Boolean getEnviados() {
		return enviados;
	}


	public void setEnviados(Boolean enviados) {
		this.enviados = enviados;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	public boolean limpiar(){
		this.nombre = "";
	    enviosList.setPromotor(null);
		return false;
	}
	
	
	public void reset(){
		this.envios = new Envios();
		this.envioss = new EntityQuery<Envios>();
		this.estado = "t";
	    	
	}
	
	
	EntityQuery<Envios> envioss = new EntityQuery<Envios>();
	
	
	public EntityQuery<Envios> getEnvioss() {
		return envioss;
	}

	public void setEnvioss(EntityQuery<Envios> envioss) {
		this.envioss = envioss;
	}
    //metodo de busqueda para EnviosList.xhtml
	public void busqueda(){
		
		System.out.println("Prueba>>>>>>");
		
		//String sql = "select envios from Envios envios where 1=1 and EXTRACT (YEAR FROM envios.fecha) = EXTRACT (YEAR FROM CURRENT_DATE)  ";
		String sql = "select envios from Envios envios where 1=1   ";
		
		
		if(this.estado.contentEquals("p")){
			sql = sql + "and (envios.enviado = false or envios.enviado is null) ";
		}
		
		if(this.estado.contentEquals("e")){
			sql = sql + "and (envios.enviado = true and envios.nrocupon is null) ";
		}
		
		if(this.estado.contentEquals("m")){
			sql = sql + "and (envios.enviado = true and envios.nrocupon is not null and " +
					"(envios.recibido <> true or envios.recibido is null)) ";
		}
		
		if(this.estado.contentEquals("r")){
			sql = sql + "and (envios.enviado = true and envios.recibido = true) ";
		}
		
		//if(this.programados){
		//	sql = sql + "and (e.enviado = false or e.enviado is null) ";
		//}
		
		//if(this.enviados){
		//	sql = sql + "and e.enviado = true and e.nrocupon is null ";
		//}
		
		
		if(this.promotorHome.getInstance().getDocumento()!=null && 
				!this.promotorHome.getInstance().getDocumento().contentEquals("")){
			sql = sql + "and (envios.promotor.documento = '"+this.promotorHome.getInstance().getDocumento()+"') ";
		}
		
		sql = sql + "and (envios.promotor.asesor.documento = '"+this.identity.getUsername()+"') ";
		
		if(this.envios.getEnvia()!=null && !this.envios.getEnvia().contentEquals("")){
			sql = sql + "and lower(envios.envia) like lower('%" + this.envios.getEnvia() + "%') ";
		}
		
		if(this.envios.getRecibe()!=null && !this.envios.getRecibe().contentEquals("")){
			sql = sql + "and lower(envios.recibe) like lower('%" + this.envios.getRecibe() + "%') ";
		}
		
		if(this.envios.getCiudad()!=null && !this.envios.getCiudad().contentEquals("")){
			sql = sql + "and lower(envios.ciudad) like lower('%" + this.envios.getCiudad() + "%') ";
		}
		
		if(this.envios.getOficina()!=null && !this.envios.getOficina().contentEquals("")){
			sql = sql + "and lower(envios.oficina) like lower('%" + this.envios.getOficina() + "%') ";
		}
		
		if(this.envios.getNrocupon()!=null && !this.envios.getNrocupon().contentEquals("")){
			sql = sql + "and envios.nrocupon = '" + this.envios.getNrocupon() + "' ";
		}
		
		if(this.envios.getFecha()!=null){
			sql = sql + "and envios.fecha = '" + this.envios.getFecha() + "' ";
		}
		
		if(this.envios.getFechaenvio()!=null){
			sql = sql + "and envios.fechaenvio = '" + this.envios.getFechaenvio() + "' ";
		}		
		
		System.out.println(sql);
		
		envioss.setEjbql(sql);
		
		if(envioss.getResultCount()<25){
			envioss.setFirstResult(0);
		}
		
		envioss.setMaxResults(25);
	}
	
	public void iniciarEdicionEnvio(int consecutivo){
		enviosHome.setEnviosConsecutivo(consecutivo);
		
		Personal p = entityManager.find(Personal.class, enviosHome.getInstance().getPromotor().getDocumento());
		
		promotorHome.setPromotorDocumento(p.getDocumento());
		
		this.nombre = p.getNombre() + " " + p.getApellido();
	}
    
    
    // add additional action methods

}
