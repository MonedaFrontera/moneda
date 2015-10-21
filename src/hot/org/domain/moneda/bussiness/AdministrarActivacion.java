package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import gnu.trove.benchmark.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.domain.moneda.entity.Actestado;
import org.domain.moneda.entity.Activacion;
import org.domain.moneda.entity.Activagestor;
import org.domain.moneda.entity.ActivagestorId;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Banco;
import org.domain.moneda.entity.Cita;
import org.domain.moneda.entity.CitaId;
import org.domain.moneda.entity.Cne;
import org.domain.moneda.entity.Estadoactivacion;
import org.domain.moneda.entity.EstadoactivacionId;
import org.domain.moneda.entity.Gestor;
import org.domain.moneda.entity.Observacion;
import org.domain.moneda.entity.ObservacionId;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.session.ActivacionHome;
import org.domain.moneda.session.ActivacionList;
import org.domain.moneda.session.ActivagestorHome;
import org.domain.moneda.session.EstadoactivacionHome;
import org.domain.moneda.session.GestorHome;
import org.domain.moneda.session.ObservacionHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.TarjetaList;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.EnviarMailAlertas;
import org.domain.moneda.util.ExpresionesRegulares;
import org.domain.moneda.util.Reporteador;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Scope(CONVERSATION)
@Name("AdministrarActivacion")
public class AdministrarActivacion {
	@Logger
	private Log log;
	
	@In  
	StatusMessages statusMessages;

	@In 
	private FacesMessages facesMessages;

	@In
	private EntityManager entityManager;

	@In(create = true)
	private Reporteador Reporteador;

	@In
	Identity identity;

	private String observacionReg;

	@In(create = true)
	@Out
	private AdministrarUsuario AdministrarUsuario;
	
	//Esta variable guarda el destino para la interfaz de usuario
	private String destinoAnio;

	public void administrarActivacion() {
		// implement your business logic here
		log.info("AdministrarActivacion.administrarActivacion() action called");
		statusMessages.add("administrarActivacion");
	}

	public String getObservacionReg() {
		return observacionReg;
	}

	public void setObservacionReg(String observacionReg) {
		this.observacionReg = observacionReg;
	}

	public String guardarActivacion() {
		Date hoy = new Date();
		BigInteger query = (BigInteger) entityManager.createNativeQuery(
				"select nextval('activacion_consecutivo_seq')")
				.getSingleResult();
		activacionHome.getInstance().setConsecutivo(query.intValue());

		activacionHome.getInstance().setPromotor(promotorHome.getInstance());

		if (activacionHome.getInstance().getFechareg() != null) {
			System.out.println("fecha reg"
					+ activacionHome.getInstance().getFechareg());
			activacionHome.getInstance().setFechareg(
					activacionHome.getInstance().getFechareg());
		} else {
			activacionHome.getInstance().setFechareg(hoy);
		}

		activacionHome.getInstance().setUsuarioreg(identity.getUsername());
		activacionHome.getInstance().setUsuariomod(identity.getUsername());
		activacionHome.getInstance().setFechamod(hoy);

		//if (!activacionHome.getInstance().getCitaasignada())
		activacionHome.getInstance().setFechacita(null);

		 activacionHome.getInstance().setAno(activacionHome.getInstance().getAno());
		//activacionHome.getInstance().setAno(114);// se establece que el viaje es
		// del proximo anio

		Credentials user = new Credentials();
		System.out.println("Usuario: " + user.getUsername());

		activacionHome.persist();
   
/*		if (activacionHome.getInstance().getCitaasignada()) {
			Cita c = new Cita();
			CitaId cid = new CitaId();
			cid.setConsecutivo(activacionHome.getInstance().getConsecutivo());
			cid.setFecha(activacionHome.getInstance().getFechacita());
			c.setActivacion(activacionHome.getInstance());
			c.setId(cid);
			entityManager.persist(c);
		}
*/
		if (gestorHome.getInstance().getDocumento() != null) {
			Activagestor ag = new Activagestor();
			ActivagestorId agid = new ActivagestorId(query.intValue(),
					gestorHome.getInstance().getDocumento(), activagestorHome
							.getInstance().getId().getFecha());
			ag.setId(agid);
			ag.setActivacion(activacionHome.getInstance());
			ag.setGestor(gestorHome.getInstance());
			entityManager.persist(ag);
			System.out.println("Se agrega en la tabla activagestor");
		}
	
		if (this.getObservacionReg() != null && !this.getObservacionReg().equals("")) {
			ObservacionId obsId = new ObservacionId();
			obsId.setConsecutivo(activacionHome.getInstance().getConsecutivo());
			obsId.setFecha(activacionHome.getInstance().getFechareg());

			Observacion obs = new Observacion();
			obs.setActivacion(activacionHome.getInstance());
			obs.setId(obsId);
			obs.setObservacion(this.getObservacionReg());

			entityManager.persist(obs);
		}

		Estadoactivacion e = new Estadoactivacion();
		EstadoactivacionId eid = new EstadoactivacionId();

		eid.setConsecutivo(activacionHome.getInstance().getConsecutivo());
		eid.setEstado(activacionHome.getInstance().getActestado()
				.getCodestado());
		if (activacionHome.getInstance().getActestado().getCodestado()
				.contentEquals("EG")) {
			eid.setFecha(activagestorHome.getInstance().getId().getFecha());
		} else {
			if (activacionHome.getInstance().getActestado().getCodestado()
					.contentEquals("AC")) {
				eid.setFecha(activacionHome.getInstance().getFechaact());
			} else {
				if (activacionHome.getInstance().getFechareg() != null
						&& activacionHome.getInstance().getActestado()
								.getCodestado().contentEquals("RE")) {
					eid.setFecha(activacionHome.getInstance().getFechareg());
				} else {
					eid.setFecha(hoy);
				}
			}

		}
		e.setId(eid);
		e.setActivacion(activacionHome.getInstance());
		e.setActestado(activacionHome.getInstance().getActestado());

		entityManager.persist(e);

		entityManager.flush();

		AdministrarTarjeta.setNombre("");
		this.setNombre("");
		promotorHome.clearInstance();
		gestorHome.clearInstance();
		this.estado = entityManager.find(Actestado.class, "RE");
		return "persisted";
	}

	public String traerAsesor(String docasesor) {
		return (String) entityManager.createQuery(
				"select p.nombre || ' ' ||p.apellido from Personal p where "
						+ "p.documento = '" + docasesor + "'")
				.getSingleResult();
	}

	@In(create = true)
	@Out
	private PromotorHome promotorHome;

	@In(create = true)
	@Out
	private GestorHome gestorHome;

	@In(create = true)
	@Out
	private ActivacionHome activacionHome;

	@In(create = true)
	@Out
	private ObservacionHome observacionHome;

	@In(create = true)
	@Out
	private ActivagestorHome activagestorHome;

	@In(create = true)
	@Out
	private EstadoactivacionHome estadoactivacionHome;

	List<String> lista = new ArrayList<String>();
	
	private Integer anioViaje;
	
	
	public Integer getAnioViaje() {
		return anioViaje;
	}

	public void setAnioViaje(Integer anioViaje) {
		this.anioViaje = anioViaje;
	}

	public void llenarPromotores() {
		entityManager.clear();
		String sql = "";
		if (identity.hasRole("Asesor")) {
			sql = " where p.asesor.documento = '" + identity.getUsername()
					+ "'";
		}
		List<String> resultList = entityManager.createQuery(
				"select p.personal.nombre||' '||p.personal.apellido from Gestor p "
						+ sql).getResultList();
		lista = resultList;
	}

	public List<String> autocompletar(Object nombre) {
		if (lista.isEmpty())
			llenarPromotores(); // Metodo que carga la informacion de los
		// nombres de las personas
		String pref = (String) nombre;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = lista.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase()
					.contains(pref.toLowerCase()))
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;

	}

	@In(create = true)
	private CargarObjetos CargarObjetos;

	public void ubicarGestor() {
		Gestor pr = CargarObjetos.ubicarGestor(this.nombre);
		if (pr != null) {
			gestorHome.setGestorDocumento(pr.getDocumento());
			gestorHome.setInstance(pr);

			activacionHome.getInstance().setGestor(pr);
		}
	}

	public List<Actestado> estadosInicio() {
		return entityManager.createQuery(
				"select s from Actestado s where s.codestado "
						+ "in ('AC','EG','RE', 'RA', 'RC') order by s.descripcion asc").getResultList();
	}

	private String nombre = "";

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private String promotor;

	public String getPromotor() {
		return promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	EntityQuery<Activacion> activacions = new EntityQuery<Activacion>();

	@In(create = true)
	private ActivacionList activacionList;

	public EntityQuery<Activacion> getActivacions() {
		return activacions;
	}

	public void setActivacions(EntityQuery<Activacion> activacions) {
		this.activacions = activacions;
	}

	Banco banco;

	public Banco getBanco() {
		//
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	Asesor asesor;

	public Asesor getAsesor() {
		return asesor;
	}

	public void setAsesor(Asesor asesor) {
		this.asesor = asesor;
	}

	Boolean b = false;
	Boolean a = false;
	
	
	public Boolean getA() {
		return a;
	}

	public void setA(Boolean a) {
		this.a = a;
	}

	public Boolean getB() {
		return b;
	}

	public void setB(Boolean b) {
		this.b = b;
	}

	Actestado estado;

	public Actestado getEstado() {
		return estado;
	}

	public void setEstado(Actestado estado) {
		this.estado = estado;
	}
	
	
	public String getDestinoAnio() {
		return destinoAnio;
	}

	public void setDestinoAnio(String destinoAnio) {
		this.destinoAnio = destinoAnio;
	}

	Activacion x;
	public void buscar() {
		System.out.println("BUSCANDO..........");
		System.out.println(this.getAnioViaje() == null);
		System.out.println(this.getAnioViaje() != null);
	

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");

		String sql = "";
		
		sql = sql + "select activacion from Activacion activacion where 1 = 1 ";
		
		if( this.getAnioViaje() == null){
			System.out.println("ingrese al if de anio");
			this.setAnioViaje(115);
			sql +=  " and activacion.ano = " + this.anioViaje;
		}else{
			sql +=  " and activacion.ano = " + this.anioViaje;
			
		}

		if (asesor != null) {
			sql = sql + " and activacion.promotor.asesor.documento = '"
					+ this.asesor.getDocumento() + "'";
		}

		if (banco != null) {
			sql = sql + " and activacion.banco.codbanco = '"
					+ this.banco.getCodbanco() + "'";
		}

		if (promotorHome.getInstance().getDocumento() != null
				&& !promotorHome.getInstance().getDocumento().contentEquals("")) {
			sql = sql + " and activacion.promotor.documento = '"
					+ promotorHome.getInstance().getDocumento() + "'";
		}

		if (gestorHome.getInstance().getDocumento() != null
				&& !gestorHome.getInstance().getDocumento().contentEquals("")) {
			sql = sql + " and activacion.gestor.documento = '"
					+ gestorHome.getInstance().getDocumento() + "'";
		}

		if (this.estado != null) {
			sql = sql + " and activacion.actestado.codestado = '"
					+ this.estado.getCodestado() + "'";
		} else {
			if (!b) {
				this.estado = entityManager.find(Actestado.class, "RE");
				sql = sql + " and activacion.actestado.codestado = 'RE'";
				b = true;
			}
		}

		if (this.activacionList.getActivacion().getNombre() != null
				&& !this.activacionList.getActivacion().getNombre()
						.contentEquals("")) {
			sql = sql + " and lower(activacion.nombre) like lower('%"
					+ this.activacionList.getActivacion().getNombre() + "%') ";
		}

		if (this.activacionList.getActivacion().getCedula() != null
				&& !this.activacionList.getActivacion().getCedula()
						.contentEquals("")) {
			sql = sql + " and activacion.cedula = '"
					+ this.activacionList.getActivacion().getCedula() + "'";
		}

		if (identity.hasRole("Asesor")) {
			sql = sql + " and activacion.promotor.asesor.documento = '"
					+ identity.getUsername() + "'";
		}

		if (fechainiact != null) {
			if (fechafinact != null) {
				sql = sql + " and activacion.fechaact between '"
						+ sdf.format(fechainiact) + "' and '"
						+ sdf.format(fechafinact) + "' ";
			} else {
				sql = sql + " and activacion.fechaact = '"
						+ sdf.format(fechainiact) + "' ";
			}
		}

		if (fechainireg != null) {
			if (fechafinreg != null) {
				sql = sql + " and activacion.fechareg between '"
						+ sdf.format(fechainireg) + "' and '"
						+ sdf.format(fechafinreg) + "' ";
			} else {
				sql = sql + " and activacion.fechareg = '"
						+ sdf.format(fechainireg) + "' ";
			}
		}
		// adiciona condicion para la fecha del estado
		if (fechainiest != null && this.estado != null) {
			if (fechainiest != null) {
				sql = sql
						+ " and activacion.consecutivo in "
						+ "(select v.consecutivo from Vistaactivaciones v where v.estado = '"
						+ this.estado.getCodestado()
						+ "' and v.fecha between '" + sdf.format(fechainiest)
						+ "' and '" + sdf.format(fechafinest) + "') ";
			} else {
				sql = sql
						+ " and activacion.consecutivo in "
						+ "(select v.consecutivo from Vistaactivaciones v where v.estado = '"
						+ this.estado.getCodestado() + "' and v.fecha = '"
						+ sdf.format(fechainiest) + "') ";
			}
		}

		if (fechainicit != null) {
			if (fechafincit != null) {
				sql = sql + " and activacion.fechacita between '"
						+ sdf.format(fechainicit) + "' and '"
						+ sdf.format(fechafincit) + "' ";
			} else {
				sql = sql + " and activacion.fechacita = '"
						+ sdf.format(fechainicit) + "' ";
			}
		}

		activacions.setEjbql(sql);

		if (activacions.getResultCount() < 25) {
			activacions.setFirstResult(0);
		}

		activacions.setMaxResults(20);
		 
		
		//esta iteracion permite identificar errores 
		for( Activacion a: activacions.getResultList()){
			System.out.println("Documento Activcion: " + a.getCedula());
		}

	}

	public String generarReporte() {
		System.out.println("Ingrese a generar el reporte");

		String promotor = null;
		String gestor = null;
		String asesor = null;
		String banco = null;
		String estado = null;
		Boolean rusad = null;
		Object fechaInicio = null;
		Object fechaFin = null;
		Boolean documentos = true;

		System.out.println("");

		if (promotorHome.getInstance().getDocumento() != null
				&& !promotorHome.getInstance().getDocumento().contentEquals("")) {
			promotor = promotorHome.getInstance().getDocumento();
		}

		if (gestorHome.getInstance().getDocumento() != null
				&& !gestorHome.getInstance().getDocumento().contentEquals("")) {
			gestor = gestorHome.getInstance().getDocumento();
		}

		if (identity.hasRole("Asesor")) {
			asesor = identity.getUsername();
		}

		if (this.banco != null) {
			banco = this.getBanco().getCodbanco();
			System.out.println("BANCO ELEGIDO: " + banco);
		}

		if (this.estado != null) {
			estado = this.estado.getCodestado().trim();
		}

		if (fechainiest != null && fechafinest != null) {
			fechaInicio = fechainiest;
			fechaFin = fechafinest;
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				int anio = new Date().getYear();// cambiar por metodo de la
				// clase Calendar
				fechaInicio = sdf.parse((anio + 1900) + "-01-01");
				fechaFin = new Date();

			} catch (Exception e) {
				System.out.println("ALGO MALO PASO  :-( ");
				e.printStackTrace();
			}
		}

		return Reporteador.generarReporteActivaciones(promotor, gestor, asesor,
				banco, estado, rusad, fechaInicio, fechaFin, documentos,
				"ActivacionesConsulta");

	}

	public void generar() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");

		String sql = "";

		sql = sql
				+ "select "
				+ "activacion.promotor.asesor.personal.nombre || ' ' || activacion.promotor.asesor.personal.apellido||';'||"
				+ "coalesce(gestor.nombre,'') || ' ' || coalesce(gestor.apellido,'')||';'||"
				+ "activacion.promotor.personal.nombre || ' ' || activacion.promotor.personal.apellido||';'||"
				+ "activacion.nombre||';'||"
				+ "activacion.banco.nombrebanco||';'||"
				+ "coalesce(activacion.tipoviaje,'')||';'||"
				+ "activacion.cedula||';'||"
				+ "activacion.correo||';'||"
				+ "activacion.clave||';'||"
				+ "activacion.actestado.descripcion||';'||"
				+ "activacion.actestado.codestado||';'||"
				+ "activacion.consecutivo"
				+ " from Activacion activacion left outer join activacion.gestor.personal as gestor "
				+ " where 1 = 1 ";

		if (asesor != null) {
			sql = sql + " and activacion.promotor.asesor.documento = '"
					+ this.asesor.getDocumento() + "'";
		}

		if (banco != null) {
			sql = sql + " and activacion.banco.codbanco = '"
					+ this.banco.getCodbanco() + "'";
		}

		if (promotorHome.getInstance().getDocumento() != null
				&& !promotorHome.getInstance().getDocumento().contentEquals("")) {
			sql = sql + " and activacion.promotor.documento = '"
					+ promotorHome.getInstance().getDocumento() + "'";
		}

		if (gestorHome.getInstance().getDocumento() != null
				&& !gestorHome.getInstance().getDocumento().contentEquals("")) {
			sql = sql + " and activacion.gestor.documento = '"
					+ gestorHome.getInstance().getDocumento() + "'";
			System.out.println("Documento del gestor>>> "
					+ gestorHome.getInstance().getDocumento());
		}

		if (this.estado != null) {
			sql = sql + " and activacion.actestado.codestado = '"
					+ this.estado.getCodestado() + "'";
		}

		if (this.activacionList.getActivacion().getNombre() != null
				&& !this.activacionList.getActivacion().getNombre()
						.contentEquals("")) {
			sql = sql + " and lower(activacion.nombre) like lower('%"
					+ this.activacionList.getActivacion().getNombre() + "%') ";
		}

		if (this.activacionList.getActivacion().getCedula() != null
				&& !this.activacionList.getActivacion().getCedula()
						.contentEquals("")) {
			sql = sql + " and activacion.cedula = '"
					+ this.activacionList.getActivacion().getCedula() + "'";
		}

		if (identity.hasRole("Asesor")) {
			sql = sql + " and activacion.promotor.asesor.documento = '"
					+ identity.getUsername() + "'";
		}

		if (fechainiact != null) {
			if (fechafinact != null) {
				sql = sql + " and activacion.fechaact between '"
						+ sdf.format(fechainiact) + "' and '"
						+ sdf.format(fechafinact) + "' ";
			} else {
				sql = sql + " and activacion.fechaact = '"
						+ sdf.format(fechainiact) + "' ";
			}
		}

		if (fechainireg != null) {
			if (fechafinreg != null) {
				sql = sql + " and activacion.fechareg between '"
						+ sdf.format(fechainireg) + "' and '"
						+ sdf.format(fechafinreg) + "' ";
			} else {
				sql = sql + " and activacion.fechareg = '"
						+ sdf.format(fechainireg) + "' ";
			}
		}

		if (fechainiest != null && this.estado != null) {
			if (fechainiest != null) {
				sql = sql
						+ " and activacion.consecutivo in "
						+ "(select v.consecutivo from Vistaactivaciones v where v.estado = '"
						+ this.estado.getCodestado()
						+ "' and v.fecha between '" + sdf.format(fechainiest)
						+ "' and '" + sdf.format(fechafinest) + "') ";
			} else {
				sql = sql
						+ " and activacion.consecutivo in "
						+ "(select v.consecutivo from Vistaactivaciones v where v.estado = '"
						+ this.estado.getCodestado() + "' and v.fecha = '"
						+ sdf.format(fechainiest) + "') ";
			}
		}

		if (fechainicit != null) {
			if (fechafincit != null) {
				sql = sql + " and activacion.fechacita between '"
						+ sdf.format(fechainicit) + "' and '"
						+ sdf.format(fechafincit) + "' ";
			} else {
				sql = sql + " and activacion.fechacita = '"
						+ sdf.format(fechainicit) + "' ";
			}
		}
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		// response.setContentType(...as appropriate...);
		response.addHeader("Content-disposition",
				"attachment; filename=\"resultado.csv\"");
		System.out.println(">>>> " + sql);
		List<Object> deta = entityManager.createQuery(sql).getResultList();
		System.out.println("Tamanio Consulta>>>" + deta.size());
		try {
			ServletOutputStream os = response.getOutputStream();
			// Incluir registro 01 informacion de la empresa
			// os.println((String)a);
			// Incluir registro 02 detalle de los empleados de la empresa

			os.println("Asesor;Gestor;Promotor;Tarjetahabiente;Banco;Tipo Viaje;Cedula;Correo;Clave;Estado;Fecha");

			if (deta.size() > 0) {
				for (int i = 0; i < deta.size(); i++) {
					String ff = (String) deta.get(i);
					String[] f = ff.split(";");
					os.println(f[0] + ";" + f[1] + ";" + f[2] + ";" + f[3]
							+ ";" + f[4] + ";" + f[5] + ";" + f[6] + ";" + f[7]
							+ ";" + f[8] + ";" + f[9] + ";"
							+ this.traerFecha(new Integer(f[11]), f[10]));
				}
			}
			os.flush();
			os.close();
			FacesContext.getCurrentInstance().responseComplete(); // DONT FORGET
			// THIS ONE
		} catch (IOException ioe) {
			// ...
		}
	}

	String observacion = "";

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void guardarObservacion() {
		Observacion o = new Observacion();
		ObservacionId oid = new ObservacionId();

		oid.setConsecutivo(activacionHome.getInstance().getConsecutivo());
		oid.setFecha(new Date());

		o.setId(oid);
		o.setActivacion(activacionHome.getInstance());
		o.setObservacion(observacionHome.getInstance().getObservacion());

		entityManager.persist(o);
		observacionHome.getInstance().setObservacion("");

		facesMessages
				.add("Se registro la observacion de la activacion correctamente");
		entityManager.flush();
		observacionHome.getInstance().setObservacion("");
	}

	public void guardarGestor() {
		Activagestor ag = new Activagestor();
		ActivagestorId agid = new ActivagestorId(activacionHome.getInstance()
				.getConsecutivo(), gestorHome.getInstance().getDocumento(),
				activagestorHome.getInstance().getId().getFecha());
		ag.setId(agid);
		ag.setActivacion(activacionHome.getInstance());
		ag.setGestor(gestorHome.getInstance());
		entityManager.persist(ag);

		activacionHome.getInstance().setGestor(gestorHome.getInstance());
		Activacion a = activacionHome.getInstance();
		entityManager.merge(a);
		System.out.println("Se agrega en la tabla activagestor");
		entityManager.flush();

		AdministrarTarjeta.setNombre("");
		this.setNombre("");
		promotorHome.clearInstance();
		gestorHome.clearInstance();
	}

	public void cambiarPromotor() {
		System.out.println("Nuevo Promotor>> "
				+ promotorHome.getInstance().getDocumento());

		String docPromo = promotorHome.getInstance().getDocumento();

		int reg = (int) entityManager.createNativeQuery(
				" update public.activacion set promotor = '" + docPromo
						+ "' where" + " public.activacion.cedula = '"
						+ activacionHome.getInstance().getCedula() + "'")
				.executeUpdate();
		promotorHome.clearInstance();
		gestorHome.clearInstance();

	}

	public void guardarEstado() {
		System.out.print("Entregada al Gestor:");
		System.out.println(activacionHome.getInstance().getActestado().getCodestado().contentEquals("EG"));
		
		System.out.print("Proceso con Carpeta:");
		System.out.println(activacionHome.getInstance().getActestado().getCodestado().contentEquals("PC"));
		
		// Si es el estado proceso con carpeta
		if ( activacionHome.getInstance().getActestado().getCodestado().contentEquals("PC") ) {
			System.out.println("Ingrese al if de Proceso con carpeta");	
			
			ActivagestorId agid = new ActivagestorId();
			agid.setConsecutivo(activacionHome.getInstance().getConsecutivo());
			agid.setDocumento( gestorHome.getInstance().getDocumento());
			agid.setFecha( activagestorHome.getInstance().getId().getFecha() );
			
			Activagestor ag = new Activagestor();
			ag.setId(agid);
			ag.setActivacion(activacionHome.getInstance());
			ag.setGestor( gestorHome.getInstance() );
			ag.setObservacion(estadoactivacionHome.getInstance()
					.getObservacion());
						
			entityManager.persist(ag);
			entityManager.clear();
			
			System.out.println();
			System.out.println("Asignando Gestor Proceso con Carpeta");
			
			activacionHome.getInstance().setGestor(gestorHome.getInstance());
		}
		
		
		Estadoactivacion e = new Estadoactivacion();
		EstadoactivacionId eid = new EstadoactivacionId();

		if (!activacionHome.getInstance().getActestado().getCodestado()
				.contentEquals("EG")) {
			eid.setConsecutivo(activacionHome.getInstance().getConsecutivo());
			eid.setEstado(activacionHome.getInstance().getActestado()
					.getCodestado());

			if (estadoactivacionHome.getInstance().getId().getFecha() != null) {
				eid.setFecha(estadoactivacionHome.getInstance().getId()
						.getFecha());
			} else {
				// sino se establece la fecha para el estado actual
				System.out.println("Fecha Asignada>>> " + new Date());
			}// fin del else

			e.setId(eid);
			e.setActivacion(activacionHome.getInstance());
			e.setActestado(activacionHome.getInstance().getActestado());
			e.setObservacion(estadoactivacionHome.getInstance()
					.getObservacion());

			entityManager.persist(e);
			
		}
		// activacionHome.getInstance().setActestado(gestorHome.getInstance());

		Banco b = activacionHome.getInstance().getBanco();

		if (activacionHome.getInstance().getActestado().getCodestado().contentEquals("EG")  ) {
			System.out.println("Proceso con carpeta");	
			
			Activagestor ag = new Activagestor();
			ActivagestorId agid = new ActivagestorId(activacionHome
					.getInstance().getConsecutivo(), gestorHome.getInstance()
					.getDocumento(), activagestorHome.getInstance().getId()
					.getFecha());
			ag.setId(agid);
			ag.setActivacion(activacionHome.getInstance());
			ag.setGestor( gestorHome.getInstance() );
			ag.setObservacion(estadoactivacionHome.getInstance()
					.getObservacion());
			
			entityManager.clear();
			entityManager.persist(ag);
			System.out.println();
			System.out.println("Cambiar gestor>>>>>");
			activacionHome.getInstance().setGestor(gestorHome.getInstance());

		} else {// cuando el estado es activacion completa
			if (activacionHome.getInstance().getActestado().getCodestado()
					.contentEquals("AC")) {
				if (estadoactivacionHome.getInstance().getId().getFecha() != null) {
					activacionHome.getInstance().setFechaact(
							estadoactivacionHome.getInstance().getId()
									.getFecha());
					activacionHome.getInstance().setFechaInicioViaje(
							activacionHome.getInstance().getFechaInicioViaje());
					activacionHome.getInstance().setFechaFinViaje(
							activacionHome.getInstance().getFechaFinViaje());
				} else {
					activacionHome.getInstance().setFechaact(new Date());
				}
			}// fin del else interno
		}// fin del else externo
		
		
		

		Activacion a = activacionHome.getInstance();
		entityManager.merge(a);
		entityManager.flush();

		AdministrarTarjeta.setNombre("");
		this.setNombre("");
		promotorHome.clearInstance();
		gestorHome.clearInstance();

	}// fin del metodo guardarEstado

	@In(create = true)
	@Out
	private AdministrarTarjeta AdministrarTarjeta;

	public void editar(int consecutivo) {
		activacionHome.setActivacionConsecutivo(consecutivo);
		this.setDestinoAnio("Destino " + activacionHome.getInstance().getDestino());
		AdministrarTarjeta.setNombre(activacionHome.getInstance().getPromotor()
				.getPersonal().getNombre()
				+ " "
				+ activacionHome.getInstance().getPromotor().getPersonal()
						.getApellido());
		if (activacionHome.getInstance().getGestor() != null) {
			this.nombre = activacionHome.getInstance().getGestor()
					.getPersonal().getNombre()
					+ " "
					+ activacionHome.getInstance().getGestor().getPersonal()
							.getApellido();
		}

	}

	public List<Object> obsActivacion() {
		List<Object> lista = null;
		String sql = "select tipo, consecutivo, fecha, observacion, dato from public.vistaactobservacion where "
				+ "consecutivo = "
				+ activacionHome.getInstance().getConsecutivo()
				+ " order by "
				+ "fecha desc";

		lista = entityManager.createNativeQuery(sql).getResultList();

		return lista;
	}

	public void cancelar() {
		AdministrarTarjeta.setNombre("");
		this.setNombre("");
		promotorHome.clearInstance();
		gestorHome.clearInstance();
		activacionHome.clearInstance();
	}

	List agencias = new ArrayList<String>();

	public void llenarAgencias() {
		entityManager.clear();
		String sql = "";

		List<String> resultList = entityManager.createNativeQuery(
				"select a.nombre " + "from Agencia a ").getResultList();
		agencias = resultList;
	}

	public List<String> autocompletarAgencia(Object nombre) {
		if (agencias.isEmpty())
			llenarAgencias(); // Metodo que carga la informacion de los nombres
		// de las personas
		String pref = (String) nombre;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = agencias.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase()
					.contains(pref.toLowerCase()))
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}

	// se cambio el tipo del valor retorno a String para exporta
	// como fecha a excel
	public String traerFecha(Integer c, String e) {
		Date fecha = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("CONSECUTIVO DEL ERROR: " + c);
		
		List<Date> ls = entityManager.createQuery(
				"select max(a.id.fecha) "
						+ "from Estadoactivacion a where a.id.consecutivo = "
						+ c + " and " + "a.id.estado = '" + e + "'")
				.getResultList();

		if (!ls.isEmpty())
			fecha = ls.get(0);
		
		if (e.contentEquals("EG")) {
			List<Date> ls2 = entityManager.createQuery(
					"select max(ag.id.fecha) "
							+ "from Activagestor ag where ag.id.consecutivo = "
							+ c + "").getResultList();

			if (!ls2.isEmpty()) {
				if (ls2.get(0) != null && fecha != null) {
					if (fecha.getTime() < ls2.get(0).getTime()) {
						fecha = ls2.get(0);
					}
				} else {
					if (ls2.get(0) != null)
						fecha = ls2.get(0);
				}
			}// fin del segundo if
		}// fin del if externo

		return sdf.format(fecha);
	}

	Date fechainiact;
	Date fechafinact;
	Date fechainireg;
	Date fechafinreg;
	Date fechainiest;
	Date fechafinest;
	Date fechainicit;
	Date fechafincit;

	public Date getFechainiact() {
		return fechainiact;
	}

	public void setFechainiact(Date fechainiact) {
		this.fechainiact = fechainiact;
	}

	public Date getFechafinact() {
		return fechafinact;
	}

	public void setFechafinact(Date fechafinact) {
		this.fechafinact = fechafinact;
	}

	public Date getFechainireg() {
		return fechainireg;
	}

	public void setFechainireg(Date fechainireg) {
		this.fechainireg = fechainireg;
	}

	public Date getFechafinreg() {
		return fechafinreg;
	}

	public void setFechafinreg(Date fechafinreg) {
		this.fechafinreg = fechafinreg;
	}

	public Date getFechainiest() {
		return fechainiest;
	}

	public void setFechainiest(Date fechainiest) {
		this.fechainiest = fechainiest;
	}

	public Date getFechafinest() {
		return fechafinest;
	}

	public void setFechafinest(Date fechafinest) {
		this.fechafinest = fechafinest;
	}

	public Date getFechainicit() {
		return fechainicit;
	}

	public void setFechainicit(Date fechainicit) {
		this.fechainicit = fechainicit;
	}

	public Date getFechafincit() {
		return fechafincit;
	}

	public void setFechafincit(Date fechafincit) {
		this.fechafincit = fechafincit;
	}

	public void limpiar() {
		activacionList.setActivacion(null);
		activacionList.setActivacion(new Activacion());
		AdministrarTarjeta.setNombre("");
		this.setNombre("");
		this.setAsesor(null);
		this.setEstado(null);
		this.setBanco(null);
		this.fechafinact = null;
		this.fechafincit = null;
		this.fechafinest = null;
		this.fechafinreg = null;
		this.fechainiact = null;
		this.fechainicit = null;
		this.fechainiest = null;
		this.fechainireg = null;
		entityManager.flush();

	}

	public void nombreActivacionMayuscula(String nombre) {
		String nomTemp = ExpresionesRegulares.eliminarEspacios(nombre, true);
		activacionHome.getInstance().setNombre(nomTemp);
	}

	public void correoEspaciosBlanco(String correo) {
		String correoTemp = ExpresionesRegulares
				.eliminarEspacios(correo, false);
		
		if (!correoTemp.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
			facesMessages
					.addToControl("correo",
							"Escriba una direccion de e-mail valida; ejemplo: usuario@dominio.com");
			}else{
				activacionHome.getInstance().setCorreo(correoTemp);
			} 
	}

	//
	public void claveEspaciosBlanco(String clave) {
		
		activacionHome.getInstance().setClave(clave.trim().toLowerCase());
		
		//this.depurarClaveCadivi("");
	}

	// las claves no pueden contener caracter de letra "o"
	// se debe cambiar al caracter a cero "0"
	public String depurarClaveCadivi(String clave) {
		StringBuilder claveTemp = new StringBuilder();
		String temp = "";
		for (int i = 0; i < clave.length(); i++) {
			temp = clave.substring(i, i + 1);
			if (temp.equals("o"))
				temp = "0";
			claveTemp.append(temp);
		}
		return claveTemp.toString();
	}

	/**
	 * Verifica que no exista otra activacion grabada para el periodo actual
	 * y valida los destinos actuales de la activacion
	 * @param cedulavalida
	 * @param ano
	 */
	public void validarActivacion(String cedulavalida, Integer ano) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");

		String cedulaTemp = cedulavalida.trim();

		List<Activacion> actTempList = entityManager.createQuery(
				"select a from Activacion a where a.cedula = '"
					+ cedulaTemp + "' and a.ano = " + ano + "").getResultList();
		if (!actTempList.isEmpty()) {
			Activacion actTemp = actTempList.get(0);
			if( actTemp.getDestino() != null ){
				String codEstado = actTemp.getActestado().getCodestado();
				System.out.println("ESTADO ACTUAL>>>"+ codEstado);
				
				if( codEstado.equals("AC")){	
					//Revisa que la fecha actual no este en el rango de un viaje
					//vigente y ademas descarto rangos de viajes futuros
					Date fechaIni = actTemp.getFechaInicioViaje();
					Date fechaFin = actTemp.getFechaFinViaje();
					if( (!(fechaIni.before(new Date()) && fechaFin.after(new Date())) 
									&& fechaFin.before(new Date()))){
						//aca establezco el destino incrementando
						Integer nroDestino = actTemp.getDestino() + 1;
						activacionHome.getInstance().setDestino(nroDestino);
						System.out.println("Nro de Destino Actual:" + nroDestino);						
						//establezco el destino para la interfaz
						this.setDestinoAnio("Destino " + nroDestino);
						//establezco el nombre y la cedula para la activacion
						activacionHome.getInstance().setCedula( cedulaTemp );
						activacionHome.getInstance().setNombre( 
								buscarTarjetahabienteCne(activacionHome.getInstance().getCedula()) );
						System.out.println("CREO UN NUEVO VIAJE");
					}else{
						facesMessages
						.addToControl(
								"nrocedula",
								"Ya se tiene una ACTIVACION REGISTRADA para este tarjetahabiente vieje por consumir");
						System.out.println("NO CREO UN NUEVO VIAJE");
					}					
				}else{
					//Tiene en un proceso de activacion en curso
					facesMessages
					.addToControl(
							"nrocedula",
							"Tiene en un proceso de activacion en curso para este perido, valide la informacion.");
					System.out.println("NO CREO UN NUEVO VIAJE (estado != AC)");
				}					
			}			
		}else{
			//establezco el destino para la interfaz y para el Home
			Integer nroDestino = new Integer(1);
			activacionHome.getInstance().setDestino(nroDestino);
			this.setDestinoAnio("Destino " + nroDestino);
			activacionHome.getInstance().setCedula( cedulaTemp );
			activacionHome.getInstance().setNombre( 
					buscarTarjetahabienteCne(activacionHome.getInstance().getCedula()) );
		}		
	}// fin metodo validarActivacion
	
	
	/**
	 * Busca la cedula en la base de datos de cne y retorna este nombre.
	 * @param cedula
	 */
	public String buscarTarjetahabienteCne( String cedula)
	{
		StringBuilder nombreBuilder = new StringBuilder("");
		
		if( !cedula.equals("") && !cedula.equals(" ")  && !cedula.equals("  ") &&
				cedula!= null )
		{
			try
			{	
				List< String >tarjetahabiente =  entityManager.createNativeQuery( "SELECT "+
			               " cne.primer_nombre||' '||cne.segundo_nombre||' '||cne.primer_apellido||' '||" +
			               "cne.segundo_apellido FROM  cne  WHERE cne.cedula = '" + cedula.trim() + "'").getResultList();							
					
					if( !tarjetahabiente.isEmpty() ){					
						StringTokenizer token = new StringTokenizer( tarjetahabiente.get(0) );
						
						while( token.hasMoreTokens()){
							String palabra = token.nextToken();
							if( !palabra.equals("") || !palabra.equals(" "))
								nombreBuilder.append(palabra).append(" ");							
					}							
				}//fin del if					
			}catch(Exception e){	
				System.out.println("Se genero una Excepcion al cargar los datos de la " +
						"cedula con el viaje y el CNE");
				e.printStackTrace();
			}			
		}			
		return this.tarjetaHabienteAMayuscula( nombreBuilder.toString().trim() );
	}
	
	
	/* pasa a Mayuscula la primera letra del nombre del Tarjetahabiente 
	 * y elimina espacios en blanco, y establece el nombre
	 * en el componente
	 */ 
	public String tarjetaHabienteAMayuscula( String nombre )
	{		
		log.info("Edicion tarejtahabiente " + nombre);
		return  ExpresionesRegulares.eliminarEspacios(nombre, true);			
	}
	
	
	public void formatoLlaveMercantil(String llaveMer) {
		String llaveFinal = "";

		try {
			llaveFinal = ExpresionesRegulares.getDigitsOnly(llaveMer);
			activacionHome.getInstance().setLlave(auxFormatoLlaves(llaveFinal));
		} catch (Exception e) {
			activacionHome.getInstance().setLlave("");
			facesMessages.addToControl("llave", "Valor errado");
		}
	}

	public void formatoProvinet(String llaveProvi) {
		String llaveFinal = "";

		try {
			llaveFinal = ExpresionesRegulares.getDigitsOnly(llaveProvi);
			activacionHome.getInstance().setProvinet(
					auxFormatoLlaves(llaveFinal));
		} catch (Exception e) {
			activacionHome.getInstance().setProvinet("");
			facesMessages.addToControl("provinet", "El numero no es valido");
		}
	}

	// agrupa en conjuntos de 4 la llave
	public String auxFormatoLlaves(String llave) {
		StringBuilder llaveFinal = new StringBuilder();

		int control = llave.length() - 4;

		for (int i = 0; i < llave.length(); i += 4) {
			if (control > 0) {
				llaveFinal.append(llave.substring(i, llave.length() - control));
				llaveFinal.append("-");
			} else {
				llaveFinal.append(llave.substring(i, llave.length()));
			}
			control -= 4;
		}
		return llaveFinal.toString().trim();
	}// fin del metodo formatoLLaves

	/**
	 * actualiza la activacion
	 */
	public void actualizarActivacion() {
		log.info("Actualizacion del Viaje "
				+ activacionHome.getInstance().getCedula());

		activacionHome.getInstance().setActestado(
				activacionHome.getInstance().getActestado());
		activacionHome.getInstance().setPromotor(promotorHome.getInstance());

		entityManager.merge(activacionHome.getInstance());

		entityManager.flush();
		activacionHome.clearInstance();

	}

	private String nombrePromotor = "";

	public String getNombrePromotor() {
		return nombre;
	}

	public void setNombrePromotor(String nombre) {
		this.nombre = nombre;
	}

	public void ubicarPromotor() {

		Promotor pr = CargarObjetos.ubicarPromotor(this.nombrePromotor);

		if (pr != null) {
			promotorHome.setPromotorDocumento(pr.getDocumento());
			promotorHome.setInstance(pr);

			activacionHome.getInstance().setPromotor(pr); 
		}
	}
	
	/**
	 * Este metodo se encarga de enviar un mensaje de correo electronico
	 * informando al cliente y la asesora que la activacion actual no ingresa a
	 * CADIVI
	 */
	public void noIngresaCadiviCorreo(Integer consecutivo) 
	{
		System.out.println("ESTADO CADIVI>> "+ activacionHome.getInstance().getErroringresocadivi());
		
		if (activacionHome.getInstance().getErroringresocadivi() ) {
			
			EnviarMailAlertas alerta = new EnviarMailAlertas();
			
			Activacion act = activacionHome.getInstance();
			alerta.enviarEmailActivaciones(act);
			AdministrarUsuario.auditarUsuario(12,
					"Envio email al promotor para la activacion de cedula "
							+ act.getCedula() + " "
							+ "al no permitir el acceso a Cencoex");
		}
	}

	/**
	 * 
	 * 
	 */
	public void noIngresaBancaLinea(Integer consecutivo) 
	{	
		if (activacionHome.getInstance().getNoingresobancalinea()) {
			
			System.out.println("Enviando Correo al cliente y asesor.");
			
			EnviarMailAlertas alerta = new EnviarMailAlertas();
			
			Activacion act = activacionHome.getInstance();
			alerta.enviarEmailBancaLinea(act);
			AdministrarUsuario.auditarUsuario(33,
					"Envio email al promotor para la activacion de cedula "
							+ act.getCedula() + " "
							+ "al no permitir el acceso a la banca en linea");
		}
	}
	
	
	
	
}// fin de la clase AdministrarActivacion
