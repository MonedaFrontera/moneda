package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Saldo;
import org.domain.moneda.entity.SaldoId;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.SaldoHome;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
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
	
	private String nombre;
	private String apellido;
	private String nombrePromotor;
	
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

	public void buscarSaldo(){
		try {
			entityManager.clear();
			System.out.println("Ingrese a buscar...");
			String queryString =
				"select saldo from Saldo saldo where year(saldo.id.fecha) = year(current_date()) ";
			
			System.out.println("Algunos cambios-->");		

			System.out.println(this.getNombre() == null);	
			
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
			
			//establezco el objeto en el Home(
			this.setSaldoHomeInstance(saldo);
			this.getSaldoHome().getInstance().setId(id);
			//estableciendo el promotor
			this.personalHome.setInstance(this.saldoHome.getInstance().getPersonal());
			this.setNombrePromotor(this.personalHome.getInstance().getNombre()+ " " + 
								this.personalHome.getInstance().getApellido());
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
	
	
	
	public void ubicarPersonal() {
		Personal pr = CargarObjetos.ubicarPersonal(this.nombre);
		System.out.println("Nombre " + this.nombre);
		// System.out.println("Doc " + pr.getDocumento());
		if (pr != null) {
			personalHome.setPersonalDocumento(pr.getDocumento());
			personalHome.setInstance(pr);
			promotorHome.setPromotorDocumento(pr.getDocumento());
			saldoHome.getInstance().setPersonal(personalHome.getInstance());
			saldoHome.getInstance().getId().setDocumento(
					personalHome.getInstance().getDocumento());
			saldoHome.getInstance().setUsuariomod(identity.getUsername());
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
	
	
	
	public String guardarSaldoInicial(){
		
	
		
		return "persisted";
	}
	
	
	
	public String actualizarSaldo(){
		
		
		
		return "";
	}
	
	
	
	
}
