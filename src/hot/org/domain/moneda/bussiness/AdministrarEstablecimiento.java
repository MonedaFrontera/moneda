package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Establecimiento;
import org.domain.moneda.entity.Franquicia;
import org.domain.moneda.entity.Pais;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Transaccion;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.EstablecimientoList;
import org.domain.moneda.session.PaisHome;
import org.domain.moneda.session.TarjetaList;
import org.domain.moneda.util.ExpresionesRegulares;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarEstablecimiento")
@Scope(CONVERSATION)
public class AdministrarEstablecimiento
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In
	private EntityManager entityManager;
    
    @In
    private FacesMessages facesMessages;
    
    List<String> lista = new ArrayList<String>();
    
    List<Object> transacciones = new ArrayList<Object>();
    
    Object totales = new Object();
       
	private String sugestion = "";
	
	@In(create=true) @Out 
	private EstablecimientoHome	establecimientoHome;
	
	@In(create=true) @Out 
	private PaisHome paisHome;
	
	@In(create=true)
	private EstablecimientoList	establecimientoList;
	
	Date fechainicio = null;
	Date fechafin = null;
	
	String numautorizacion = "";
	String numtarjeta = "";
	String tipotx = "";
	String numfactura = "";
	
	Franquicia franquicia = null;
	
    
    public void administrarEstablecimiento()
    {
        // implement your business logic here
        log.info("AdministrarEstablecimiento.administrarEstablecimiento() action called");
        statusMessages.add("administrarEstablecimiento");
    }
    
    public List<String> autocompletar(Object suggest) {
		llenar(); 							// Metodo que carga la informacion de los establecimientos
		String pref = (String) suggest;
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

    
	public void llenar(){
		entityManager.clear();
		List<String> resultList = 
			entityManager.createQuery("select establecimiento.nombreestable from " +
					"Establecimiento establecimiento").getResultList();
		lista = resultList;
	}
	
	public void ubicarEstablecimiento(){

		entityManager.clear();
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where e.nombreestable='"+this.sugestion+"'").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = (Establecimiento)entityManager
			.createQuery("select e from Establecimiento e where e.nombreestable='"+this.sugestion+"'")
			.getSingleResult();
			
			establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
		}
		
		
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

	public List<Object> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<Object> transacciones) {
		this.transacciones = transacciones;
	}
	
	public void buscar()
	{
		try{
			String sql = "";		
			String sqlcampos = "select t.tarjeta.numerotarjeta, t.fechatx, t.tipotx, t.valortxpesos," +
					"t.valortxdolares, case when td.tasadolar is null or td.tasadolar = td.tasa then " +
					"t.valortxpesos else (td.tasadolar*t.valortxdolares) end as valorcomp, t.numfactura ";
			
			if(this.numautorizacion.contentEquals("")){
				sql = "from Tarjeta tar, Transaccion t, Tasadolar td where 1 = 1 and " +
						"tar.numerotarjeta = t.tarjeta.numerotarjeta and td.id.fecha = t.fechatx and td.id.codigopais = t.establecimiento.pais.codigopais ";
			}else{
				sql = "from Tarjeta tar, Transaccion t, Baucher b, Tasadolar td where b.id.consecutivo = t.consecutivo and " +
						"tar.numerotarjeta = t.tarjeta.numerotarjeta and td.id.fecha = t.fechatx and td.id.codigopais = t.establecimiento.pais.codigopais ";
			}
			
			List< String > codigoUnico = entityManager.createNativeQuery( "select" + 
								" public.establecimiento.codigounico from public.establecimiento"  +   
								" where public.establecimiento.nombreestable = '" + this.sugestion.trim() +"'" ).getResultList();
								
								
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
			
			
			if(!this.sugestion.contentEquals("")){
				sql = sql + " and t.establecimiento.codigounico='"+ codigoUnico.get(0) +"'";
			}
			
			if(this.fechainicio!=null){
				String fechai = sdf.format(this.fechainicio);
				sql = sql + " and t.fechatx >= '" + fechai + "'";
			}
			
			if(this.fechafin!=null){
				String fechaf = sdf.format(this.fechafin);
				sql = sql + " and t.fechatx <= '" + fechaf + "'";
			}
			
			if(!this.numfactura.contentEquals("")){
				
				sql = sql + " and t.numfactura = '" + this.numfactura + "'";
			}
			
			if(this.franquicia != null){
				
				sql = sql + " and tar.franquicia.codfranquicia = '" + this.franquicia.getCodfranquicia() + "'";
			}
			
			if(!this.numtarjeta.contentEquals("")){
				
				sql = sql + " and t.tarjeta.numerotarjeta = '" + this.numtarjeta + "'";
			}
			if(this.tipotx!=null)
			if(!this.tipotx.contentEquals("")){
				
				sql = sql + " and t.tipotx = '" + this.tipotx + "'";
			}
			
			if(!this.numautorizacion.contentEquals("")){
				
				sql = sql + " and b.id.numautorizacion = '" + this.numautorizacion + "'";
			}
			
			this.transacciones = (ArrayList)entityManager
			.createQuery(sqlcampos+sql).setMaxResults(100).getResultList();
					
			sql = "select sum(t.valortxpesos), sum(t.valortxdolares), sum(case when td.tasadolar is null " +
					"or td.tasadolar = td.tasa then " +
					"t.valortxpesos else (td.tasadolar*t.valortxdolares) end) " + sql;
			
			this.totales = (Object)entityManager
			.createQuery(sql).setMaxResults(100).getSingleResult();
			
		}catch(Exception e){
			
			e.printStackTrace();			
		}		
	}//fin del metodo buscar
	
	
	public String getSugestion() {
		return sugestion;
	}


	public void setSugestion(String sugestion) {
		this.sugestion = sugestion;
	}

	public String getNumautorizacion() {
		return numautorizacion;
	}

	public void setNumautorizacion(String numautorizacion) {
		this.numautorizacion = numautorizacion;
	}

	public String getNumtarjeta() {
		return numtarjeta;
	}

	public void setNumtarjeta(String numtarjeta) {
		this.numtarjeta = numtarjeta;
	}

	public String getTipotx() {
		return tipotx;
	}

	public void setTipotx(String tipotx) {
		this.tipotx = tipotx;
	}

	public String getNumfactura() {
		return numfactura;
	}

	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
	}

	public Object getTotales() {
		return totales;
	}

	public void setTotales(Object totales) {
		this.totales = totales;
	}

	public Franquicia getFranquicia() {
		return franquicia;
	}

	public void setFranquicia(Franquicia franquicia) {
		this.franquicia = franquicia;
	}
	
	
	private Pais pais;	
	
	public Pais getPais() {		
		return pais;
		
	}

	public void setPais(Pais pais) {		
		this.pais = pais;		
	}
	
	/**	  
	 * 
	 * @see validarCodigoUnico
	 * 
	 */
	public void validarCodigoUnicoAtomatico()
	{	
		String codUnico = establecimientoHome.getInstance().getCodigounico().trim();
		String codFinal = codUnico; 
		System.out.println("metodo validarCodigo");
		
		try {	
			
			//confirma que el pais no sea Colombia
			if(  !pais.getCodigopais().equals("CO") )
			{
				//extraigo el codigo base del pais
				String codPaisBase =         		
					(String) entityManager.createNativeQuery(
							"SELECT substr( public.establecimiento.codigounico, 1,3) " +
							"FROM public.establecimiento " +
							"WHERE substr( public.establecimiento.codpais, 1,2) = '" 
							+ pais.getCodigopais().substring(0,2) + "'").getSingleResult();
				
				System.out.println("CODIGO BASE>>" + codPaisBase );
				
				if( codPaisBase != null ){
					//obtiene el ultimo codigo creado por pais y le suma 1
					BigDecimal conse = (BigDecimal) entityManager.createNativeQuery(
							"SELECT "+
							"to_number( max(substr( public.establecimiento.codigounico, 4,5)), '99G999D9S' ) + 1  "+
							"FROM "+
							"public.establecimiento " +
							"WHERE "+
							"substr(public.establecimiento.codpais, 1,2) ='" + 
							pais.getCodigopais().substring(0,2) + "'").getSingleResult();
					
					if( conse.intValue() < 10 )
					{
						String consedos = "0" + conse.intValue();
						codFinal = codUnico.substring(0, 3) + consedos;    			
					}else{
						codFinal = codUnico.substring(0, 3) + conse.intValue();
					}				
				}else{//sino existe ningun codigo creado para este pais
					codFinal = codUnico.substring(0, 3) + "01";				
				}			
				
			}else{
				//Valida que el codigo unico no exista en la BD
				String codigo = 
					(String) entityManager.createNativeQuery( 
							"SELECT public.establecimiento.codigounico " +
							"FROM public.establecimiento " +
							"WHERE public.establecimiento.codigounico = '"+ codUnico +
					"'").getSingleResult();
				
				facesMessages.addToControl("codigounico", 
						"El codigo unico de este establecimiento  \"" +
						codUnico  + "\" ya existe debe asignar otro codigo unico.");
				
			}//fin del else principal**
			
			//establezco el codigo unico en el componente
			establecimientoHome.getInstance().setCodigounico(
					codFinal.toUpperCase().trim() );			
		}catch(Exception e){    		
			System.out.println("Excepcion lanzada Imprimiendo pila:");
			e.printStackTrace();
		}   
	}//fin del metodo validarCodigoUnico	
	
    		
	/**
	 * <p>
	 * Revisa el codigo unico para los establecimientos. Los codigo unicos 
	 * se crean con base en la codificacion mundial de paises, el estandar 
	 * ISO A3. Moneda Frontera toma este codigo del pais, y le adiciona un 
	 * numero que va desde 01 hasta 99 por cada nuevo establecimiento que 
	 * se crea en este pais.
	 * <p>
	 * Si el establecimiento es Colombiano se registra el codigo unico
	 * asignado por Incocrdito.
	 * 
	 * 
	 */
	public void validarCodigoUnico()
	{		
		String codUnico = establecimientoHome.getInstance().getCodigounico().trim();
		String codFinal = codUnico;  
		String paisLocal = establecimientoHome.getInstance().getPais().getCodigopais();
				
		//si el codigo no es valido
		if( codUnico.length()<5)
		{
			facesMessages.addToControl("codigounico", 
			"El codigo ingresado no es valido");
			return;
		}    			
		
		if( codUnico.substring(3,5).equals("00") && !paisLocal.equals("CO") )
			facesMessages.addToControl("codigounico", 
					"Los digitos finales del establecimiento no deben ser 00, " +
			"cambielo por un valor entre 01 a 99");
		
		try {
			//Valida que el codigo unico no exista en la BD
			String codigo = 
				(String) entityManager.createNativeQuery( 
						"SELECT public.establecimiento.codigounico " +
						"FROM public.establecimiento " +
						"WHERE public.establecimiento.codigounico = '" 
						+ codUnico + "'").getSingleResult();        //camibar esto por jpaql
			
			
			if( codigo != null && !paisLocal.equals("CO"))
			{       
				
				String codPaisBase =         		
					(String) entityManager.createNativeQuery(
							"SELECT substr( public.establecimiento.codigounico, 1,3) " +
							"FROM public.establecimiento " +
							"WHERE substr( public.establecimiento.codigounico, 1,3) = '" 
							+ codUnico.substring(0, 3) + "'").getSingleResult();        		
				
				if( codPaisBase != null ){
					//obtiene el ultimo codigo creado por pais y le suma 1
					BigDecimal conse = (BigDecimal) entityManager.createNativeQuery(
							"SELECT "+
							"to_number( max(substr( public.establecimiento.codigounico, 4,5)), '99G999D9S' ) + 1  "+
							"FROM "+
							"public.establecimiento " +
							"WHERE "+
							"substr(public.establecimiento.codigounico, 1,3) ='" + 
							codUnico.substring(0, 3) + "'").getSingleResult();
					
					if( conse.intValue() < 10 )
					{
						String consedos = "0" + conse.intValue();
						codFinal = codUnico.substring(0, 3) + consedos;    			
					}else{
						codFinal = codUnico.substring(0, 3) + conse.intValue();
					}
					
					facesMessages.addToControl("codigounico", 
							"El codigo unico \""+ codUnico  + "\" ya existe, se asignara " +
							"el siguiente codigo: " + codFinal);
					
					//establezco el codigo unico en el componente
					establecimientoHome.getInstance().setCodigounico(
							codFinal.toUpperCase().trim() );        			
				}else{
					facesMessages.addToControl("codigounico", 
							"El codigo unico de este establecimiento  \"" +
							codUnico  + "\" ya existe debe asignar otro codigo unico.");        			
				}//fin del if interno        		
			}//fin del if externo
			
		}catch(Exception e){    		
			System.out.println("Excepcion lanzada Imprimiendo pila:");
			e.printStackTrace();
		}   
		
	}//fin del metodo validarCodigoUnico
	
	
	private EntityQuery<Establecimiento> establecimiento = new EntityQuery<Establecimiento>();	
	
	public EntityQuery<Establecimiento> getTarjetas() {
		return establecimiento;
	}

	public void setTarjetas(EntityQuery<Establecimiento> establecimiento) {
		this.establecimiento = establecimiento;
	}
	
	public void establecerEstablecimiento(String nombre) {
		String nombreTemp = ExpresionesRegulares.eliminarEspacios(nombre, true);
		this.establecimientoHome.getInstance().setNombreestable(nombreTemp);
	}

	public void buscarEstablecimientos()
	{
		entityManager.clear();
		
		String sql = "select establecimiento from Establecimiento establecimiento where 1=1";
		
	}//fin del metodo buscarEstablecimientos
	
	
	
}//fin de la clase AdministrarEstablecimiento
