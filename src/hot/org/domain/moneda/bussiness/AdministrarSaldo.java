package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Saldo;
import org.domain.moneda.entity.SaldoId;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.SaldoHome;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;


@Name("AdministrarSaldo")
@Scope(CONVERSATION)
public class AdministrarSaldo {
	
	@In
	private EntityManager entityManager;
	
	@In(create=true) @Out 
    private SaldoHome saldoHome;
	
	@In(create = true)
	private CargarObjetos CargarObjetos;
	
	@In(create = true)
	@Out(required = false)
	private PersonalHome personalHome;
	
	@In(create = true)
	@Out(required = false)
	private PromotorHome promotorHome;
	
	@In
	Identity identity;
	@Logger
	private Log log;
	
	
	 @In 
	 private FacesMessages facesMessages;
	 @In 
	 StatusMessages statusMessages; 
	
	private String nombre;
	private String apellido;
	private String nombrePromotor;
	
	private Date fechaPK ;
	
	private EntityQuery<Saldo> saldoList = new EntityQuery<Saldo>();	
	private List<String> lista = new ArrayList<String>();
	
	public EntityQuery<Saldo> getSaldoList() {
		return saldoList;
	}

	public void setSaldoList(EntityQuery<Saldo> saldoList) {
		this.saldoList = saldoList;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public SaldoHome getSaldoHome() {
		return saldoHome;
	}
	
	public String getNombrePromotor() {
		return nombrePromotor;
	}

	public void setNombrePromotor(String nombrePromotor) {
		this.nombrePromotor = nombrePromotor;
	}

	public void setSaldoHome(SaldoHome saldoHome) {
		this.saldoHome = saldoHome;
	}
	
	public void setSaldoHomeInstance( Saldo saldo ){
		this.getSaldoHome().setInstance(saldo);
	}

	public List<String> getLista() {
		return lista;
	}

	public void setLista(List<String> lista) {
		this.lista = lista;
	}

	public Date getFechaPK() {
		return fechaPK;
	}

	public void setFechaPK(Date fechaPK) {
		this.fechaPK = fechaPK;
	}

	public void buscarSaldo(){
		try {
			entityManager.clear();
			log.info("Buscando saldo inicial..");
			String queryString =
				"select saldo from Saldo saldo where year(saldo.id.fecha) = year(current_date()) ";
						
			if ( this.getNombre() != null && !this.getNombre().equals("")  ) {
				queryString += " and lower(saldo.personal.nombre) like '%"
									+ this.getNombre().toLowerCase().trim() + "%'";
			}		
			if( this.getApellido() != null && !this.getApellido().equals("")   ){
				queryString += "and lower(saldo.personal.apellido) like '%" + 
						this.getApellido().toLowerCase().trim() +"%'";
			}				
			saldoList.setEjbql(queryString);
			System.out.println("EJBQL: " + saldoList.getEjbql()); 
			if(saldoList.getResultCount() < 25)
				saldoList.setFirstResult(0);
			saldoList.setMaxResults(25);
		}	
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void editarSaldo( SaldoId id){
		try {
			//Obtengo el objeto de la base
			Saldo saldo = entityManager.find(Saldo.class, id);
			this.setFechaPK(saldo.getId().getFecha());
			
			//establezco el objeto en el Home
			this.setSaldoHomeInstance(saldo);
			this.getSaldoHome().getInstance().setId(id);
			//estableciendo el promotor
			this.personalHome.setInstance(this.saldoHome.getInstance().getPersonal());
			this.setNombrePromotor(this.personalHome.getInstance().getNombre()+ " " + 
								this.personalHome.getInstance().getApellido());
			this.promotorHome.setInstance(entityManager.find(
					Promotor.class, this.personalHome.getInstance().getDocumento()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void llenarPromotores(){
		entityManager.clear();
		String sql = "";
		this.setLista( entityManager.createQuery("select p.personal.nombre||' '||p.personal.apellido " +
				"from Promotor p "+ sql).getResultList());
	}
	
	
	
	//Metodos de servicio para la Vista		
	public void ubicarPromotor(){
		Promotor pr = CargarObjetos.ubicarPromotor(this.getNombrePromotor());
		if(pr!=null){
			
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);
			
			
		}
	}
	
	
	
	/**
	 * Este metodo busca y autocompleta el nombre del tarjetahabiente, usando
	 * expresiones regulares. Esta forma de autocompletar permite buscar usando
	 * cualquier parte del nombre. Ej: El nombre "Manuel Ricardo Perez Avadia"
	 * se puede buscar --> "Manuel Avadia" ó "Manuel Perez" ó "Ma Pe";
	 * cualquiera de estos patrones puden usarse para busacar un nombre; o el
	 * que el usuario elija guardando el orden de las palabras.
	 * 
	 * @param nom
	 * @return List<String> nombres encontrados
	 */
	public List<String> autocompletar(Object nom) {
		llenarPromotores();// Metodo que carga la informacion de los nombres de
							// las personas
		String nombre = (String) nom;
		List<String> result = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(nombre.toLowerCase());
		StringBuilder bldr = new StringBuilder();// builder usado para formar el
												 // patron
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
		System.out.println("Patron: >" + bldr.toString());
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
	
	
	/**
	 * Guarda  saldo inicial para el promotor 
	 * @return
	 */
	public String guardarSaldoInicial(){
		//valida que no exista  saldo inicial para el periodo actual
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Saldo > saldoTemp = new ArrayList<Saldo>();
		String queryString = "select s from Saldo s where s.id.documento = '"+ 
								this.promotorHome.getInstance().getDocumento() + 
								"'  and year(saldo.id.fecha) = year(current_date())";
		System.out.println(queryString);
		
		log.info("Validando saldo inicial : " + this.promotorHome.getInstance().getDocumento() );
		
		saldoTemp = entityManager.createQuery(queryString).getResultList();
		if( !saldoTemp.isEmpty()){
			statusMessages.add("Ya se encuentra grabado un saldo inicial para este promotor en la fecha: " +
					sdf.format(saldoTemp.get(0).getId().getFecha()) + ", por valor de: " + saldoTemp.get(0).getSaldo());
			return null;
		}else{
			this.saldoHome.getInstance().setId(
					new SaldoId(this.promotorHome.getInstance().getDocumento(), 
							this.saldoHome.getInstance().getId().getFecha()
							));
			this.saldoHome.getInstance().setPersonal(
					entityManager.find(Personal.class, promotorHome.getInstance().getDocumento()));
			this.saldoHome.getInstance().setUsuariomod(this.identity.getUsername());
			this.saldoHome.getInstance().setFechamod(new Date());
			this.saldoHome.persist();
			entityManager.flush();
			
			return "persisted";
		}	
	}
	
	
	/**
	 * Actualiza el saldo inicial del promotor
	 * @return
	 */
	public String actualizarSaldo(){
		log.info("Actualizando saldo del promotor-------");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String nativeQuery = "update public.saldo set " +
				"fecha = '"+ sdf.format(this.saldoHome.getInstance().getId().getFecha()) +"', " + 
				"saldo = "+ this.saldoHome.getInstance().getSaldo() + ", " +
				"usuariomod = '" + this.identity.getUsername() + "', " + 
				"fechamod = '" + sdf.format(new Date()) +"' " +
				"where public.saldo.documento = '" + this.promotorHome.getInstance().getDocumento()+ "' and " +
				"public.saldo.fecha = '" + sdf.format(this.fechaPK) +"' " ;
		int reg = entityManager.createNativeQuery(nativeQuery).executeUpdate();
		if(reg == 1){
			log.info("Total de registros actualizado " + reg);
			this.setNombre("");
			this.setApellido("");
			entityManager.clear();
			return "updated";
		}else{
			statusMessages.add("No se puede actualizar este saldo, contacte al adminstrador del sistema o valide la informacion " +
					" que esta ingresando");
			return null;
		}
	}
	
}
