package org.domain.moneda.bussiness;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Activacion;
import org.domain.moneda.entity.Administrativo;
import org.domain.moneda.entity.Arrastrador;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Cargo;
import org.domain.moneda.entity.Gestor;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.entity.Usuario;
import org.domain.moneda.entity.Usuariorol;
import org.domain.moneda.entity.UsuariorolId;
import org.domain.moneda.session.AdministrativoHome;
import org.domain.moneda.session.AsesorHome;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.PersonalList;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TarjetaList;
import org.domain.moneda.session.UsuarioHome;
import org.domain.moneda.util.ExpresionesRegulares;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarPersonal")
@Scope(ScopeType.CONVERSATION)
public class AdministrarPersonal {
	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;

	@In
	StatusMessages statusMessages;
	
	//
	private String ultimoAsesor;
	private Date utlimaTx;

	public void administrarPersonal() {
		// implement your business logic here
		log.info("AdministrarPersonal.administrarPersonal() action called");
		statusMessages.add("administrarPersonal");
	}

	// add additional action methods

	/*
	 * Se realiza la inclusion de los elementos que participan en el proceso
	 */
	@In
	Identity identity;

	@In(create = true)
	@Out(required = false)
	private PersonalHome personalHome;

	@In(required = false)
	@Out(required = false)
	private AsesorHome asesorHome;

	@In(required = false)
	@Out(required = false)
	private PromotorHome promotorHome;

	@In(required = false)
	@Out(required = false)
	private AdministrativoHome administrativoHome;

	@In(required = false)
	@Out(required = false)
	private UsuarioHome usuarioHome;

	public void validarDocumento(String documento) {
		List<String> doc = entityManager.createNativeQuery(
				"select documento from personal "
						+ "where trim(personal.documento) = '"
						+ documento.trim() + "'").getResultList();
		if (doc.size() > 0) {
			facesMessages.addToControl("documento",
					"Este numero de cedula ya se encuentra registrado");
			return;
		}else{
			this.buscarTarjetahabienteCne(documento);			
		}
	}
	
	
	/**
	 * Busca la cedula en la base de datos de cne y retorna este nombre.
	 * @param cedula
	 */
	public void buscarTarjetahabienteCne( String cedula)
	{
		StringBuilder nombreBuilder = new StringBuilder("");
		if( !cedula.equals("") && !cedula.equals(" ")  && !cedula.equals("  ") &&
				cedula!= null )
		{
			try
			{	
				List< String >tarjetahabiente =  entityManager.createNativeQuery( "SELECT "+
			               " cne.primer_nombre, cne.segundo_nombre, cne.primer_apellido, " +
			               "cne.segundo_apellido FROM  cne  WHERE cne.cedula = '" + cedula.trim() + "'").getResultList();							
				
				if( !tarjetahabiente.isEmpty() ){
					Object tupla[] = tarjetahabiente.toArray();
					Object fileRe[] = (Object[]) tupla[ tupla.length -1 ];
					String nombre = fileRe[0] + " " + fileRe[1];
					String apellido = fileRe[2] + " " + fileRe[3];	
					
					this.nombreMayuscula(nombre);
					this.apellidoMayuscula(apellido);
				}//fin del if					
			}catch(Exception e){	
				System.out.println("Se genero una Excepcion al cargar los datos de la " +
						"cedula con el viaje y el CNE");
				e.printStackTrace();
			}			
		}	
	}
	
	

	// Pasa a mayuscula la primera letra del nombre - elimina espacios
	public void nombreMayuscula(String nombre) {
		String nombreTemp = ExpresionesRegulares.eliminarEspacios(nombre, true);
		personalHome.getInstance().setNombre(nombreTemp);
	}

	// Pasa a mayuscula la primera letra del apellido - elimina espacios
	public void apellidoMayuscula(String apellido) {
		String apellidoTemp = ExpresionesRegulares.eliminarEspacios(apellido,
				true);
		personalHome.getInstance().setApellido(apellidoTemp);
	}

	public void direccionMayuscula(String direccion) {
		String dirTemp = ExpresionesRegulares.eliminarEspacios(direccion, true);
		personalHome.getInstance().setDireccion(dirTemp);

	}

	public void solodigitosCelular(String num) {
		String telTemp = ExpresionesRegulares.getDigitsOnly(num);
		personalHome.getInstance().setCelular(telTemp);
	}
	
	

	/**
	 * Cambia a mayuscula las letras contenidas en una direccion pin para
	 * BlackBerry Messenger.
	 * 
	 * @param PIN_BlackBerry_Messenger
	 */
	public void formatoPinBlackBerri(String pin) {
		String pinTemp = pin.toUpperCase().trim();
		personalHome.getInstance().setPinbb(pinTemp);
	}
	

	
	/**
	 * Valida que la direccion de correo electronico sea una direccion valida. Esta 
	 * revision se hace por medio expresiones regulares
	 * @param correo
	 */
	public void validarCorreoElecronicoPrincipal(String correo) 
	{
		String correoTemp = ExpresionesRegulares
		.eliminarEspacios(correo, false);//pasa a minuscula el correo y le aplica trim

		if(!correoTemp.equals("")){
			if (ExpresionesRegulares.validateEmail(correoTemp)) {
				personalHome.getInstance().setCorreo(correoTemp);
				}else {
					facesMessages
							.addToControl("correo",
									"Escriba una direccion de e-mail valida; Ejemplo:  usuario@dominio.com ");
				}
		}
	}
	
	
	public void validarCorreoElecronicoAlterno(String correo) {
		String correoTemp = ExpresionesRegulares
		.eliminarEspacios(correo, false);
		
		if(!correoTemp.equals("")){
			if (ExpresionesRegulares.validateEmail(correoTemp)) {
				personalHome.getInstance().setCorreoalternativo(correoTemp);
			} else {
				facesMessages
						.addToControl("correoalternativo",
								"Escriba una direccion de e-mail valida; Ejemplo:  usuario@dominio.com ");
			}					
		}			 
	}
	
	

	public void registrarPersonal() {
		log.info("Registrar Personal");
		if (personalHome.getInstance().getCargo().getCodcargo().contentEquals(
				"AS")) {
			registrarAsesor();
		} else if (personalHome.getInstance().getCargo().getCodcargo()
				.contentEquals("PR")) {
			registrarPromotor();
		} else if (personalHome.getInstance().getCargo().getCodcargo()
				.contentEquals("AR")) {
			registrarArrastrador();
		} else if (personalHome.getInstance().getCargo().getCodcargo()
				.contentEquals("GE")) {
			registrarGestor();
		} else {
			registrarAdministrativo();
		}
	}

	public void registrarPromotorA() {
		log.info("Registrar Promotor");
		Cargo c = entityManager.find(Cargo.class, "PR");
		personalHome.getInstance().setCargo(c);
		registrarPromotor();
	}

	public void registrarAsesor() {
		log.info("Registrar Asesor");
		entityManager.persist(personalHome.getInstance());
		entityManager.flush();
		entityManager.clear();
		Personal p = entityManager.find(Personal.class, personalHome
				.getInstance().getDocumento());
		Asesor a = new Asesor();
		a.setDocumento(p.getDocumento().trim());
		a.setPersonal(p);
		a.setComision(asesorHome.getInstance().getComision());		
		a.setTelefonoOficina(asesorHome.getInstance().getTelefonoOficina());
		a.setExtension(asesorHome.getInstance().getExtension());
		entityManager.persist(a);
		
		
		Usuario userAsesor = entityManager.find(Usuario.class, personalHome.getInstance().getDocumento());
		
		entityManager.flush();
		entityManager.clear();
		
		
		if(userAsesor == null ){
			usuarioHome.getInstance().setNombre(
					personalHome.getInstance().getNombre() + " "
							+ personalHome.getInstance().getApellido());
			usuarioHome.getInstance().setUsuario(p.getDocumento());
			usuarioHome.getInstance().setClave(usuarioHome.getInstance().getClave());
			entityManager.persist(usuarioHome.getInstance());
			entityManager.flush();
			entityManager.clear();

			Usuariorol ur = new Usuariorol(new UsuariorolId(a.getDocumento(), 1));

			entityManager.persist(ur);			
		}
		entityManager.flush();
		entityManager.clear();

	}

	public void registrarPromotor() {
		log.info("Registrar Promotor");	
		
			/*
			 * Registra la informacion de la persona en la base de datos
			 */
			entityManager.persist(personalHome.getInstance());
			entityManager.flush();
			entityManager.clear();
			/* Se crea el objeto Personal y se busca la informacion de la persona */

			Personal p = entityManager.find(Personal.class, personalHome
					.getInstance().getDocumento());
			/*
			 * Se crea el objeto Promotor para realizar la persistencia
			 */
			Promotor pr = new Promotor();
			pr.setDocumento(p.getDocumento().trim());
			pr.setPersonal(p);
			if (promotorHome.getInstance().getAsesor() != null) {
				pr.setAsesor(promotorHome.getInstance().getAsesor());
			} else {
				Asesor a = entityManager.find(Asesor.class, identity.getUsername());
				if (a != null) {
					pr.setAsesor(a);
				} else {
					this.facesMessages
							.add("El usuario actual no esta registrado como asesor");

					return;
				}
			}
			
			pr.setComisionviajero(promotorHome.getInstance().getComisionviajero());
			pr.setArrastrador(promotorHome.getInstance().getArrastrador());
			pr.setComisionarrastrador(promotorHome.getInstance()
					.getComisionarrastrador());
			/*
			 * Se realiza la persistencia del objeto Promotor
			 */
			entityManager.persist(pr);
			entityManager.flush();
			entityManager.clear();
		
	}
	

	public void registrarArrastrador() {
		log.info("Registrar Arrastrador");
		/*
		 * Registra la informacion de la persona en la base de datos
		 */
		entityManager.persist(personalHome.getInstance());
		entityManager.flush();
		entityManager.clear();

		Personal p = entityManager.find(Personal.class, personalHome
				.getInstance().getDocumento());
		Arrastrador a = new Arrastrador();
		a.setDocumento(p.getDocumento().trim());
		a.setPersonal(p);
		entityManager.persist(a);
		entityManager.flush();
		entityManager.clear();
		
	}

	public void registrarGestor() {
		log.info("Registrar Gestor");
		/*
		 * Registra la informacion de la persona en la base de datos
		 */
		entityManager.persist(personalHome.getInstance());
		entityManager.flush();
		entityManager.clear();

		Personal p = entityManager.find(Personal.class, personalHome
				.getInstance().getDocumento());
		Gestor g = new Gestor();
		g.setDocumento(p.getDocumento().trim());
		g.setPersonal(p);
		entityManager.persist(g);
		entityManager.flush();
		entityManager.clear();
	}

	public void registrarAdministrativo() {
		log.info("Registrar Administrativo");
		/*
		 * Registra la informacion de la persona en la base de datos
		 */
		entityManager.persist(personalHome.getInstance());
		entityManager.flush();
		entityManager.clear();
		/*
		 * Se crea el objeto Personal y se busca la informacion de la persona
		 */
		Personal p = entityManager.find(Personal.class, personalHome
				.getInstance().getDocumento());
		/*
		 * Se crea el objeto Administrativo para realizar la persistencia
		 */
		Administrativo a = new Administrativo();
		a.setDocumento(p.getDocumento().trim());
		a.setPersonal(p);
		a.setCargo(administrativoHome.getInstance().getCargo());
		a.setComision(administrativoHome.getInstance().getComision());
		/*
		 * Se realiza la persistencia del objeto Administrativo
		 */
		entityManager.persist(a);
		entityManager.flush();
		entityManager.clear();
	}

	@In(create = true)
	@Out
	private AdministrarUsuario AdministrarUsuario;

	public void editarPersonal(String documento) {
		personalHome.setPersonalDocumento(documento);
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Promotor p = entityManager.find(Promotor.class, personalHome
				.getInstance().getDocumento());
		if (p != null) {
			promotorHome.setPromotorDocumento(p.getDocumento());
			AdministrarUsuario.auditarUsuario(6,
					"Consulto los datos personales del promotor "
							+ p.getDocumento() + " en la fecha "
							+ sdf.format(fecha) + "");
			this.buscarUltimoAsesor(p.getDocumento());
			this.ultimaTx(p.getDocumento());
			this.ultimaActualizacion(documento);

		} else {
			Asesor a = entityManager.find(Asesor.class, personalHome
					.getInstance().getDocumento());
			if (a != null) {
				asesorHome.setAsesorDocumento(a.getDocumento());
				AdministrarUsuario.auditarUsuario(7,
						"Consulto los datos personales del asesor "
								+ a.getDocumento() + " en la fecha "
								+ sdf.format(fecha) + "");

			} else {
				Administrativo ad = entityManager.find(Administrativo.class,
						personalHome.getInstance().getDocumento());
				if (ad != null) {
					administrativoHome.setAdministrativoDocumento(ad
							.getDocumento());
				}
			}
		}
	}

	public String actualizarPersonal() {
		
		personalHome.getInstance().setUltmactualizacion(new Date());
		entityManager.persist(personalHome.getInstance());
		personalHome.clearInstance();
		entityManager.flush();
		entityManager.clear();
		
		return "updated";
	}

	String password = "";
	String confirmacion = "";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}

	public String cambiarClave() {
		if (this.confirmacion.contentEquals(password)) {
			entityManager.createQuery(
					"update Usuario u set clave = '" + this.password + "' "
							+ "where usuario = '" + identity.getUsername()
							+ "'").executeUpdate();
			entityManager.flush();
			facesMessages
					.add("El cambio de clave se ha realizado de forma correcta");
			return "updated";
		} else {
			facesMessages
					.add("La clave y la confirmacion no coinciden, verifique la informacion ingresada");
			return null;
		}
	}

	EntityQuery<Personal> personal = new EntityQuery<Personal>();

	@In(create = true)
	private PersonalList personalList;

	public EntityQuery<Personal> getPersonal() {
		return personal;
	}

	public void setPersonal(EntityQuery<Personal> personal) {
		this.personal = personal;
	}

	/**
	 * fecha 06/02/14 se cambio el la busqueda en PersonalList, para evitar que
	 * usuarios, que no sean administrativos puedan ver datos de empleados.
	 */
	public void busqueda() {
		entityManager.clear();

		String sql = " select  personal from Personal personal where 1 = 1";

		if (personalList.getPersonal().getDocumento() != null
				&& !personalList.getPersonal().getDocumento().equals("")) {
			sql += " and replace(trim(personal.documento),' ','') = "
					+ "replace(trim('"
					+ personalList.getPersonal().getDocumento() + "' ),' ','')";
			System.out.println("Cedula: "
					+ personalList.getPersonal().getDocumento());
		}

		if (personalList.getPersonal().getNombre() != null
				&& !personalList.getPersonal().getNombre().equals("")) {
			sql += " and lower(trim(personal.nombre)) like "
					+ "lower( concat('%',concat((trim('"
					+ personalList.getPersonal().getNombre() + "')),'%' )))";
		}

		if (personalList.getPersonal().getApellido() != null
				&& !personalList.getPersonal().getApellido().equals("")) {
			sql += " and lower(trim(personal.apellido)) like "
					+ "lower( concat('%',concat((trim('"
					+ personalList.getPersonal().getApellido() + "')),'%' )))";
		}

		// si no tiene el Rol de Consultas Administrativas no permite
		// ver informacion de empleados de la empresa.
		if (!identity.hasRole("Consulta Administrativa")) {
			sql += " and personal.cargo.codcargo <> 'AS' and personal.cargo.codcargo <> 'AD' and "
					+ "personal.cargo.codcargo <> 'FA' and personal.cargo.codcargo <> 'AR'";
		}

		personal.setEjbql(sql);

		if (personal.getResultCount() < 25) {
			personal.setFirstResult(0);
		}

		personal.setMaxResults(25);
	}

	public Date getUtlimaTx() {
		return utlimaTx;
	}

	public void setUtlimaTx(Date utlimaTx) {
		this.utlimaTx = utlimaTx;
	}

	public String getUltimoAsesor() {
		return ultimoAsesor;
	}

	public void setUltimoAsesor(String ultimoAsesor) {
		this.ultimoAsesor = ultimoAsesor;
	}

	public void buscarUltimoAsesor(String documento) {
		try {
			List<String> as = entityManager
					.createNativeQuery(
							"SELECT public.personal.nombre ||' '||public.personal.apellido"
									+ " FROM public.transaccion INNER JOIN public.personal ON ("
									+ " public.transaccion.asesor = public.personal.documento) WHERE "
									+ " public.transaccion.promotor = '"
									+ documento
									+ "' AND public.transaccion.fechatx IN "
									+ " ( SELECT MAX(public.transaccion.fechatx) FROM public.transaccion"
									+ " WHERE public.transaccion.promotor = '"
									+ documento + "' )").getResultList();

			if (!as.isEmpty() && !as.get(0).equals("")) {
				this.setUltimoAsesor(ExpresionesRegulares.eliminarEspacios(as
						.get(0), true));
			} else {
				this.setUltimoAsesor("No ha tenido Asesor Asignado");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ultimaTx(String documento) {
		List<Date> fechaTx = entityManager.createQuery(
				"SELECT MAX(tx.fechatx) FROM "
						+ "Transaccion tx  WHERE tx.promotor = '" + documento
						+ "'").getResultList();
		if (!fechaTx.isEmpty()) {
			this.setUtlimaTx( fechaTx.get(0) );
		} else {
			this.setUtlimaTx(null);
		}
	}
	
	private Date actualizacion;
	
	public Date getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Date actualizacion) {
		this.actualizacion = actualizacion;
	}
	Personal p;

	public void ultimaActualizacion(String documento)
	{
		Date fecha = (Date) entityManager.createQuery(
				"select p.ultmactualizacion from Personal p where p.documento = '" + documento + "'")
				.getSingleResult();
		
		this.setActualizacion(fecha);
		
	}

	public void limpiar() {
		System.out.println("Entre a Limpiar");
		personalHome.clearInstance();
		personalList.setPersonal(null);
		personalList.setPersonal(new Personal());		
		entityManager.flush();
		entityManager.clear();

	}

}
