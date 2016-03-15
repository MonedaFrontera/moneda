package org.domain.moneda.bussiness;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Cuentapromotor;
import org.domain.moneda.entity.CuentapromotorId;
import org.domain.moneda.entity.Dolartoday;
import org.domain.moneda.entity.Gastos;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Promotorcomisiontx;
import org.domain.moneda.entity.PromotorcomisiontxId;
import org.domain.moneda.entity.Promotortasa;
import org.domain.moneda.entity.PromotortasaId;
import org.domain.moneda.entity.Saldo;
import org.domain.moneda.entity.Tasadolar;
import org.domain.moneda.entity.Usuario;
import org.domain.moneda.session.CuentapromotorHome;
import org.domain.moneda.session.PaisHome;
import org.domain.moneda.session.PersonalHome;
import org.domain.moneda.session.PromotorHome;
import org.domain.moneda.session.PromotorcomisiontxHome;
import org.domain.moneda.session.PromotortasaHome;
import org.domain.moneda.session.SaldoHome;
import org.domain.moneda.util.CargarObjetos;
import org.domain.moneda.util.EnviarMailAdjunto;
import org.domain.moneda.util.Reporteador;
import org.domain.moneda.util.UtilidadesBD;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("AdministrarPromotor")
@Scope(ScopeType.CONVERSATION)
public class AdministrarPromotor {
	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;

	@In
	StatusMessages statusMessages;

	@In(create = true)
	@Out(required = false)
	private PersonalHome personalHome;

	@In(create = true)
	@Out(required = false)
	private PromotorHome promotorHome;

	@In(create = true)
	@Out
	private AdministrarUsuario AdministrarUsuario;

	public void administrarPromotor() {
		// implement your business logic here
		log.info("AdministrarPromotor.administrarPromotor() action called");
		statusMessages.add("administrarPromotor");
	}

	// add additional action methods

	List<Object> balancesst = new ArrayList<Object>();
	List<Object> balances = new ArrayList<Object>();
	List<Gastos> gastosLista = new ArrayList<Gastos>();
	List<Object> balanceanalisis = new ArrayList<Object>();

	Object balancestotal = new Object();
	Object gastostotal = new Object();

	Object balancesfecha = new Object();
	Object gastosfecha = new Object();

	@In
	Identity identity;

	BigDecimal saldoini = BigDecimal.ZERO;
	Float totalDolaresViaje = 0f;
	BigDecimal sicad1 = BigDecimal.ZERO;
	
	private Date fechaSaldoPromotor;
	

	public Object getGastostotal() {
		return gastostotal;
	}

	public void setGastostotal(Object gastostotal) {
		this.gastostotal = gastostotal;
	}

	public List<Gastos> getGastosLista() {
		return gastosLista;
	}

	public void setGastosLista(List<Gastos> gastosLista) {
		this.gastosLista = gastosLista;
	}

	public float getTotalDolaresViaje() {
		return totalDolaresViaje;
	}

	public void setTotalDolaresViaje(float totalDolaresViaje) {
		this.totalDolaresViaje = totalDolaresViaje;
	}

	public BigDecimal getSaldoini() {
		return saldoini;
	}

	public void setSaldoini(BigDecimal saldoini) {
		this.saldoini = saldoini;
	}

	String mensajeSaldo = "No se registra en el sistema ningun saldo inicial";

	public String getMensajeSaldo() {
		return mensajeSaldo;
	}

	public void setMensajeSaldo(String mensajeSaldo) {
		this.mensajeSaldo = mensajeSaldo;
	}

	public BigDecimal getSicad1() {
		return sicad1;
	}

	public void setSicad1(BigDecimal sicad1) {
		this.sicad1 = sicad1;
	}
	
		public Date getFechaSaldoPromotor() {
		return fechaSaldoPromotor;
	}

	public void setFechaSaldoPromotor(Date fechaSaldoPromotor) {
		this.fechaSaldoPromotor = fechaSaldoPromotor;
	}

	public void balanceTarjetas(String documento) {

		if (fecha == null) {
			fecha = new Date();
		}

		personalHome.setPersonalDocumento(documento);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");

		Saldo s = null;

		String fechaini = sdf.format(fecha);
		List<Saldo> ss = entityManager.createQuery(
				"SELECT " + "s FROM Saldo s WHERE " + "s.id.documento = '"
						+ documento + "' and "
						+ "s.id.fecha = (Select max(s.id.fecha) from "
						+ "Saldo s where s.id.fecha<'" + sdf.format(fecha)
						+ "' " + "and s.id.documento = '" + documento + "'"
						+ ")").getResultList();
		saldoini = BigDecimal.ZERO;
		if (ss.size() > 0) {
			s = ss.get(0);
			fechaini = sdf.format(s.getId().getFecha());
			saldoini = s.getSaldo();
			mensajeSaldo = "Saldo inicial a partir de " + fechaini;
		} else {
			mensajeSaldo = "No se registra en el sistema ningun saldo inicial";
		}

		gastosLista = entityManager.createQuery("SELECT g FROM Gastos g " +
			      "WHERE g.fecha = '"+sdf.format(fecha)+"' and " +
			          "g.personal.documento = '"+documento+"'").getResultList();  
			  
			  gastostotal = entityManager.createQuery("SELECT sum(case when g.tipogasto.tipo = '2' then g.valor else 0 end) as tdebito, " +
			      "sum(case when g.tipogasto.tipo = '1' then g.valor else 0 end) as tcredito," +
			      "sum(t.valorbolivar) as tbolivar FROM Gastos g left outer join g.transferencia t " +
			        "WHERE g.fecha = '"+sdf.format(fecha)+"' and " +
			          "g.personal.documento = '"+documento+"' ").getSingleResult();
			  
			  gastosfecha = entityManager.createQuery("SELECT sum(case when g.tipogasto.tipo = '2' then g.valor else 0 end) as tdebito, " +
			        "sum(case when g.tipogasto.tipo = '1' then g.valor else 0 end) as tcredito," +
			        "sum(t.valorbolivar) as tbolivar FROM Gastos g left outer join g.transferencia t " +
			          "WHERE g.fecha < '"+sdf.format(this.fecha)+"' " +
			          "and (g.fecha > '"+fechaini+"' or " +
			        "'"+fechaini+"' =  '"+sdf.format(this.fecha)+"') " +
			              "and " +
			            "g.personal.documento = '"+documento+"' ").getSingleResult();

		
		System.out.println("Inicio de Consulta>>");
		balances = entityManager.createNativeQuery("SELECT " +
				  "tarjeta.numerotarjeta, " +  //0
				  "banco.nombrebanco, " +  //1
				  "franquicia.nombrefranquicia, " +  //2
				  "viaje.fechainicio, " +  //3
				  "viaje.fechafin, " +  //4 
				  "viaje.cupoinicialviajero, " +  //5
				  "viaje.cupoinicialinternet, " +  //6
				  "viaje.cupoviajero, " +  //7
				  "viaje.cupointernet, " +  //8
				  "tarjeta.tarjetahabiente, " +  //9 
				  "depositos.bolivares, " +  //10
				  "depositos.pesos as depositopesos, " +  //11
				  "transacciones.pesos, " +  //12
				  "transacciones.dolares, " +  //13
				  "transacciones.comision, " +  //14
				  "viaje.cupoviajero + viaje.cupointernet as totalcuporestante, " +  //15
				  "depositos.preciobolivar " +//16
				"FROM " +
				  "tarjeta " +
				  "LEFT OUTER JOIN tarjetaviaje ON (tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta) " +
				  "LEFT OUTER JOIN viaje ON (tarjetaviaje.consecutivoviaje = viaje.consecutivo) " +
				  "INNER JOIN banco ON (tarjeta.bancoemisor = banco.codbanco) " +
				  "INNER JOIN franquicia ON (tarjeta.franquicia = franquicia.codfranquicia) " +
				  "LEFT JOIN (SELECT  " +
				    "depositostarjeta.numerotarjeta, " +
				    "depositostarjeta.promotor, " +
				    "depositostarjeta.preciobolivar, "+
				    "sum(depositostarjeta.valordeposito) AS bolivares, " +
				    "sum(depositostarjeta.depositopesos) AS pesos " +
				    "FROM " +
				    "depositostarjeta, viaje, tarjeta, tarjetaviaje " +
				    "WHERE viaje.consecutivo = tarjetaviaje.consecutivoviaje and tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
				    "tarjeta.numerotarjeta = depositostarjeta.numerotarjeta and fecha = '"+sdf.format(this.fecha)+"' " + 
				    " and tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin " +
				    "GROUP BY " +
				    "depositostarjeta.numerotarjeta, " +
				    "depositostarjeta.promotor, " +
				    "depositostarjeta.preciobolivar ) depositos " + 
				  "ON (depositos.numerotarjeta =  " +
				  "tarjeta.numerotarjeta and depositos.promotor = '" + documento + "') " +
				"LEFT JOIN (SELECT  " +
				  "transaccion.numerotarjeta, " +
				  "sum(transaccion.valortxpesos) AS pesos, " +
				  "sum(transaccion.valortxdolares) AS dolares, " +
				  "sum(transaccion.valorcomision) AS comision," +
				  "transaccion.promotor " +
				"FROM " +
				  "transaccion, viaje, tarjeta, tarjetaviaje " +
				  "WHERE viaje.consecutivo = tarjetaviaje.consecutivoviaje and tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
				  "tarjeta.numerotarjeta = transaccion.numerotarjeta and fechatx = '"+sdf.format(this.fecha)+"' " +
				      "and tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin " + 
				"GROUP BY " +
				  "transaccion.numerotarjeta," +
				  "transaccion.promotor) transacciones " + 
				  "ON (transacciones.numerotarjeta =  " +
				  "tarjeta.numerotarjeta and transacciones.promotor = '" + documento + "')" + 
				          "where tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin and " +
				          "(transacciones.promotor = '" + documento + "' or " +
				              "depositos.promotor = '" + documento + "') " +
				          "and (transacciones.pesos > 0 or " +
				          "depositos.pesos > 0)").getResultList();

		//consulta para totales
	      balancestotal = entityManager.createNativeQuery("SELECT SUM(bolivares) as depobolivares, " +
	          "SUM(depositopesos) as depopesos, SUM(pesos) as transacpesos, " +
	          "SUM(dolares) as transacdolares, SUM(comision) as transacomision" +
	          " FROM (SELECT " +
	            "tarjeta.numerotarjeta, " +  //0
	            "banco.nombrebanco, " +  //1
	            "franquicia.nombrefranquicia, " +  //2
	            "viaje.fechainicio, " +  //3
	            "viaje.fechafin, " +  //4 
	            "viaje.cupoinicialviajero, " +  //5
	            "viaje.cupoinicialinternet, " +  //6
	            "viaje.cupoviajero, " +  //7
	            "viaje.cupointernet, " +  //8
	            "tarjeta.tarjetahabiente, " +  //9 
	            "depositos.bolivares, " +  //10
	            "depositos.pesos as depositopesos, " +  //11
	            "transacciones.pesos, " +  //12
	            "transacciones.dolares, " +  //13
	            "transacciones.comision, " +  //14
	            "sum(viaje.cupoviajero + viaje.cupointernet) as totalcuporestante " +  //15
	          "FROM " +
	            "tarjeta " +
	            "LEFT OUTER JOIN tarjetaviaje ON (tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta) " +
	            "LEFT OUTER JOIN viaje ON (tarjetaviaje.consecutivoviaje = viaje.consecutivo) " +
	            "INNER JOIN banco ON (tarjeta.bancoemisor = banco.codbanco) " +
	            "INNER JOIN franquicia ON (tarjeta.franquicia = franquicia.codfranquicia) " +
	             "LEFT JOIN (SELECT  " +
	             "depositostarjeta.promotor," +
	            "depositostarjeta.numerotarjeta, " +
	            "sum(depositostarjeta.valordeposito) AS bolivares, " +
	            "sum(depositostarjeta.depositopesos) AS pesos " +
	          "FROM " +
	            "depositostarjeta, viaje, tarjeta, tarjetaviaje " +
	            "WHERE viaje.consecutivo = tarjetaviaje.consecutivoviaje and tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
	  "tarjeta.numerotarjeta = depositostarjeta.numerotarjeta and depositostarjeta.fecha = '"+sdf.format(this.fecha)+"' " + 
	  "and tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin " + 
	          "GROUP BY " +
	            "depositostarjeta.numerotarjeta," +
	            "depositostarjeta.promotor) depositos " + 
	            "ON (depositos.numerotarjeta =  " +
	            "tarjeta.numerotarjeta and depositos.promotor = '" + documento + "') " +
	          "LEFT JOIN (SELECT  " +
	            "transaccion.numerotarjeta, transaccion.promotor, " +
	            "sum(transaccion.valortxpesos) AS pesos, " +
	            "sum(transaccion.valortxdolares) AS dolares, " +
	            "sum(transaccion.valorcomision) AS comision " +
	          "FROM " +
	            "transaccion, viaje, tarjeta, tarjetaviaje  " +
	            "WHERE viaje.consecutivo = tarjetaviaje.consecutivoviaje and tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
	            "tarjeta.numerotarjeta = transaccion.numerotarjeta and fechatx = '"+sdf.format(this.fecha)+"' " +
	                "and tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin " + 
	          "GROUP BY " +
	            "transaccion.numerotarjeta, transaccion.promotor) transacciones " + 
	            "ON (transacciones.numerotarjeta =  " +
	            "tarjeta.numerotarjeta and transacciones.promotor = '" + documento + "') " + 
	            
	                    "where tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin and " +
	                    "(transacciones.promotor = '" + documento + "' or " +
	              "depositos.promotor = '" + documento + "') " +
	              " group by " +
	             "tarjeta.numerotarjeta, " +  //0
	            "banco.nombrebanco, " +  //1 
	            "franquicia.nombrefranquicia, " +  //2
	            "viaje.fechainicio, " +  //3
	            "viaje.fechafin, " +  //4 
	            "viaje.cupoinicialviajero, " +  //5
	            "viaje.cupoinicialinternet, " +  //6
	            "viaje.cupoviajero, " +  //7
	            "viaje.cupointernet, " +  //8
	            "tarjeta.tarjetahabiente, " +  //9 
	            "depositos.bolivares, " +  //10
	            "depositos.pesos, " +  //11
	            "transacciones.pesos, " +  //12
	            "transacciones.dolares, " +  //13
	            "transacciones.comision  ) tot").getSingleResult();
	      
	      
	      balancesfecha = entityManager.createNativeQuery("SELECT SUM(bolivares) as depobolivares, " +
	              "SUM(depositopesos) as depopesos, SUM(pesos) as transacpesos, " +
	              "SUM(dolares) as transacdolares, SUM(comision) as transacomision" +
	              " FROM (SELECT " +
	                "tarjeta.numerotarjeta, " +  //0
	                "banco.nombrebanco, " +  //1
	                "franquicia.nombrefranquicia, " +  //2
	                "viaje.fechainicio, " +  //3
	                "viaje.fechafin, " +  //4 
	                "viaje.cupoinicialviajero, " +  //5
	                "viaje.cupoinicialinternet, " +  //6
	                "viaje.cupoviajero, " +  //7
	                "viaje.cupointernet, " +  //8
	                "tarjeta.tarjetahabiente, " +  //9 
	                "depositos.bolivares, " +  //10
	                "depositos.pesos as depositopesos, " +  //11
	                "transacciones.pesos, " +  //12
	                "transacciones.dolares, " +  //13
	                "transacciones.comision, " +  //14
	                "viaje.cupoviajero + viaje.cupointernet as totalcuporestante " +  //15
	              "FROM " +
	                "tarjeta " +
	                "LEFT OUTER JOIN tarjetaviaje ON (tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta) " +
	                "LEFT OUTER JOIN viaje ON (tarjetaviaje.consecutivoviaje = viaje.consecutivo) " +
	                "INNER JOIN banco ON (tarjeta.bancoemisor = banco.codbanco) " +
	                "INNER JOIN franquicia ON (tarjeta.franquicia = franquicia.codfranquicia) " +
	                 "LEFT JOIN (SELECT  " +
	                 "depositostarjeta.promotor, " +
	                "depositostarjeta.numerotarjeta, " +
	                "sum(depositostarjeta.valordeposito) AS bolivares, " +
	                "sum(depositostarjeta.depositopesos) AS pesos " +
	              "FROM " +
	                "depositostarjeta, viaje, tarjeta, tarjetaviaje " +
	                "WHERE tarjeta.numerotarjeta = depositostarjeta.numerotarjeta and viaje.consecutivo = tarjetaviaje.consecutivoviaje and tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
	                "tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin and " +
	                "depositostarjeta.fecha < '"+sdf.format(this.fecha)+"' " +
	                    "and (depositostarjeta.fecha > '"+fechaini+"' or " +
	                        "'"+fechaini+"' =  '"+sdf.format(this.fecha)+"') " + 
	              "GROUP BY " +
	                "depositostarjeta.numerotarjeta," +
	                "depositostarjeta.promotor) depositos " + 
	                "ON (depositos.numerotarjeta =  " +
	                "tarjeta.numerotarjeta and depositos.promotor = '" + documento + "') " +
	              "LEFT JOIN (SELECT  " +
	                "transaccion.numerotarjeta, transaccion.promotor, " +
	                "sum(transaccion.valortxpesos) AS pesos, " +
	                "sum(transaccion.valortxdolares) AS dolares, " +
	                "sum(transaccion.valorcomision) AS comision " +
	              "FROM " +
	                "transaccion, viaje, tarjeta, tarjetaviaje " +
	                "WHERE tarjeta.numerotarjeta = transaccion.numerotarjeta and viaje.consecutivo = tarjetaviaje.consecutivoviaje and " +
	                "tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta and " +
	                "tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin and fechatx < '"+sdf.format(this.fecha)+"' " + 
	                "and (fechatx > '"+fechaini+"' or " +
	                "'"+fechaini+"' =  '"+sdf.format(this.fecha)+"')" + 
	              "GROUP BY " +
	                "transaccion.numerotarjeta, transaccion.promotor) transacciones " + 
	                "ON (transacciones.numerotarjeta =  " +
	                "tarjeta.numerotarjeta and transacciones.promotor = '" + documento + "')" + 
	                        "where tarjeta.fechainicio = viaje.fechainicio and tarjeta.fechafin = viaje.fechafin " +
	                        "and (transacciones.promotor = '" + documento + "' or " +
	                  "depositos.promotor = '" + documento + "') ) tot").getSingleResult();
	      
		
		AdministrarUsuario.auditarUsuario(17, "Consulto balance de promotor "
				+ documento + " en la fecha " + sdf.format(this.fecha) + "");

	}

	BigDecimal tasa = BigDecimal.ZERO;
	BigDecimal tasabolivar = BigDecimal.ZERO;

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public BigDecimal getTasabolivar() {
		return tasabolivar;
	}

	public void setTasabolivar(BigDecimal tasabolivar) {
		this.tasabolivar = tasabolivar;
	}

	public void revisarPrestamo(String documento) {

		if (fecha == null) {
			fecha = new Date();
		}

		personalHome.setPersonalDocumento(documento);
		promotorHome.setPromotorDocumento(documento);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");

		Saldo s = null;

		List<Tasadolar> tds = entityManager.createQuery(
				"select td from Tasadolar td "
						+ "where td.id.codigopais = 'CO' and td.id.fecha = '"
						+ sdf.format(fecha) + "'").getResultList();
		tasa = BigDecimal.ZERO;
		if (tds.size() > 0) {
			tasa = (tds.get(0)).getTasa();
		}

		List<BigDecimal> tasabolivares = (List<BigDecimal>) entityManager
				.createQuery(
						"select t.preciobolivar from Tasadebolivaroficina t "
								+ "where t.id.fecha = '" + sdf.format(fecha)
								+ "' and t.id.tipo='D'").getResultList();
		if (tasabolivares.size() > 0) {
			this.tasabolivar = tasabolivares.get(0);
		}

		String sql = "SELECT tarjeta.tarjetahabiente, " + "banco.nombrebanco, "
				+ "viaje.cupoviajero, " + "viaje.cupoviajero*"
				+ tasa.floatValue()
				+ ", "
				+ "Max(transaccion.fechatx), "
				+ "date(max(transaccion.fechatx)) <  date((CURRENT_DATE - INTERVAL '5 DAY')) as ultimaTx "
				+ "FROM "
				+ "public.banco banco, "
				+ "public.promotor promotor, "
				+ "public.tarjeta tarjeta, "
				+ "public.tarjetaviaje tarjetaviaje,"
				+ "public.transaccion transaccion, "
				+ "public.viaje viaje "
				+ "WHERE "
				+ "tarjeta.numerotarjeta = tarjetaviaje.numerotarjeta AND "
				+ "tarjetaviaje.consecutivoviaje = viaje.consecutivo AND "
				+ "tarjeta.documento = promotor.documento AND "
				+ "tarjeta.bancoemisor = banco.codbanco AND "
				+ "tarjeta.numerotarjeta = transaccion.numerotarjeta AND "
				+ "((viaje.cupoviajero>=40) AND (promotor.documento='"
				+ documento
				+ "') AND "
				+ "(viaje.fechafin>=CURRENT_DATE) AND (tarjetaviaje.estado=1)) "
				+ "GROUP BY "
				+ "tarjeta.tarjetahabiente, "
				+ "banco.nombrebanco, "
				+ "viaje.cupoviajero "
				+ "ORDER BY "
				+ "tarjeta.tarjetahabiente";
		balanceanalisis = entityManager.createNativeQuery(sql).getResultList();

		List<Object[]> datos = entityManager.createNativeQuery(sql)
				.getResultList();

		BigDecimal x = null;
		for (int i = 0; i < datos.size(); i++) {
			x = (BigDecimal) datos.get(i)[2];
			this.totalDolaresViaje += x.floatValue();
		}

		/** Consulta que trae el valor del SICAD1 */
		String queryString = "select dolartoday.usdsicad1 from dolartoday WHERE dolartoday.epoch IN ( "
				+ "select max( dolartodayfecha.epoch) FROM ( select date(to_timestamp(dolartoday.epoch /1000 )) as fecha, "
				+ "dolartoday.epoch from dolartoday where date(to_timestamp(dolartoday.epoch /1000 ))= current_date  "
				+ "group by dolartoday.epoch  )as dolartodayfecha)";

		sicad1 = (BigDecimal) entityManager.createNativeQuery(queryString)
				.getSingleResult();
		BigDecimal saldoTemp = (BigDecimal) entityManager.createNativeQuery(
				"select saldofecha(CURRENT_DATE, '" + documento + "' )")
				.getSingleResult();
		this.setSaldoinforme(saldoTemp);

		System.out.println(saldoinforme);
	}

	BigDecimal saldoinforme = BigDecimal.ZERO;

	public BigDecimal getSaldoinforme() {
		return saldoinforme;
	}

	public void setSaldoinforme(BigDecimal saldoinforme) {
		this.saldoinforme = saldoinforme;
	}

	Object balancesprestamo = new Object();

	public Object getBalancesprestamo() {
		return balancesprestamo;
	}

	public void setBalancesprestamo(Object balancesprestamo) {
		this.balancesprestamo = balancesprestamo;
	}

	public List<Object> getBalances() {
		return balances;
	}

	public void setBalances(List<Object> balances) {
		this.balances = balances;
	}

	public List<Object> getBalancesst() {
		return balancesst;
	}

	public void setBalancesst(List<Object> balancesst) {
		this.balancesst = balancesst;
	}

	public Object getBalancestotal() {
		return balancestotal;
	}

	public void setBalancestotal(Object balancestotal) {
		this.balancestotal = balancestotal;
	}

	public Object getBalancesfecha() {
		return balancesfecha;
	}

	public void setBalancesfecha(Object balancesfecha) {
		this.balancesfecha = balancesfecha;
	}

	BigDecimal saldoinicial = BigDecimal.ZERO;

	public BigDecimal getSaldoinicial() {
		return saldoinicial;
	}

	public void setSaldoinicial(BigDecimal saldoinicial) {
		this.saldoinicial = saldoinicial;
	}

	Date fecha = null;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Object getGastosfecha() {
		return gastosfecha;
	}

	public void setGastosfecha(Object gastosfecha) {
		this.gastosfecha = gastosfecha;
	}

	public List<Object> getBalanceanalisis() {
		return balanceanalisis;
	}

	public void setBalanceanalisis(List<Object> balanceanalisis) {
		this.balanceanalisis = balanceanalisis;
	}

	/**
	 * Codificacion para adicionar la cuenta del promotor
	 * 
	 */

	@In(create = true)
	@Out
	private CuentapromotorHome cuentapromotorHome;

	private String nombre = "";

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@In(create = true)
	private CargarObjetos CargarObjetos;

	public void verificarDocumento(String documento) {
		System.out.println("Revisando " + documento);
		Promotor pr = entityManager.find(Promotor.class, documento);
		if (pr != null) {
			facesMessages
					.addToControl("documento",
							"Este numero de documento ya se encuentra registrado para un Promotor");
		}
	}

	public void ubicarPromotor() {
		Personal pr = CargarObjetos.ubicarPersonal(this.nombre);
		System.out.println("Nombre " + this.nombre);
		// System.out.println("Doc " + pr.getDocumento());
		if (pr != null) {
			personalHome.setPersonalDocumento(pr.getDocumento());
			personalHome.setInstance(pr);
			promotorHome.setPromotorDocumento(pr.getDocumento());
			cuentapromotorHome.getInstance().setPromotor(
					promotorHome.getInstance());
		}

		/*
		 * entityManager.clear(); List<Promotor> p = (ArrayList)entityManager
		 * .createQuery(
		 * "select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"
		 * +this.nombre+"'").getResultList();
		 * 
		 * if (p.size() > 0) { Promotor pr = (Promotor)entityManager
		 * .createQuery(
		 * "select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"
		 * +this.nombre+"'") .getSingleResult();
		 * 
		 * promotorHome.setPromotorDocumento(pr.getDocumento());
		 * promotorHome.setInstance(pr);
		 * 
		 * tarjetaHome.getInstance().setPromotor(pr); }
		 */

	}

	public void guardarCuenta() {
		CuentapromotorId cid = new CuentapromotorId();

		cid.setDocumento(personalHome.getInstance().getDocumento());
		cid.setNumcuenta(cuentapromotorHome.getInstance().getId()
				.getNumcuenta());

		Cuentapromotor cp = entityManager.find(Cuentapromotor.class, cid);

		if (cp == null) {

			Promotor pr = entityManager.find(Promotor.class, personalHome
					.getInstance().getDocumento());

			cuentapromotorHome.getInstance().setPromotor(pr);
			cuentapromotorHome.getInstance().setId(cid);

			cuentapromotorHome.persist();
			entityManager.clear();
			this.nombre = "";

		} else {
			this.facesMessages.addToControl("numcuenta",
					"Esta cuenta ya se encuentra asociada a este promotor");
			return;
		}

	}

	@In(create = true)
	@Out
	private SaldoHome saldoHome;

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

		/*
		 * entityManager.clear(); List<Promotor> p = (ArrayList)entityManager
		 * .createQuery(
		 * "select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"
		 * +this.nombre+"'").getResultList();
		 * 
		 * if (p.size() > 0) { Promotor pr = (Promotor)entityManager
		 * .createQuery(
		 * "select p from Promotor p where trim(p.personal.nombre)||' '||trim(p.personal.apellido)='"
		 * +this.nombre+"'") .getSingleResult();
		 * 
		 * promotorHome.setPromotorDocumento(pr.getDocumento());
		 * promotorHome.setInstance(pr);
		 * 
		 * tarjetaHome.getInstance().setPromotor(pr); }
		 */

	}

	List<String> lista = new ArrayList<String>();

	public void llenarPromotores() {
		entityManager.clear();
		String sql = "";

		/*
		 * if(identity.hasRole("Asesor")){ sql =
		 * " where p.asesor.documento = '"+identity.getUsername()+"'"; }
		 */
		List<String> resultList = entityManager
				.createQuery(
						"select p.personal.nombre||' '||p.personal.apellido from Promotor p "
								+ "union select a.personal.nombre||' '||a.personal.apellido from Arrastrador a"
								+ sql).getResultList();
		lista = resultList;
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

	/*
	 * public List<String> autocompletar(Object nombre) { llenarPromotores(); //
	 * Metodo que carga la informacion de los nombres de las personas String
	 * pref = (String) nombre; ArrayList<String> result = new
	 * ArrayList<String>(); Iterator<String> iterator = lista.iterator(); while
	 * (iterator.hasNext()) { String elem = ((String) iterator.next()); if
	 * ((elem != null && elem.toLowerCase().contains(pref.toLowerCase())) ||
	 * "".equals(pref)) { result.add(elem); } } return result;
	 * 
	 * }
	 */

	@In(create = true)
	@Out
	private Reporteador Reporteador;

	@In(create = true)
	@Out
	private EnviarMailAdjunto EnviarMail;

	public String enviarBalance(Date param1, Object param2, Object param3,
			Object param4, Object param5, Object param5a, String nombre) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String nombrereporte = nombre;
		String path = "";
		String fecha = sdf.format(param1);
		Promotor promotor = personalHome.getInstance().getPromotor();
		String correoCliente = promotor.getPersonal().getCorreo();

		if (correoCliente != null & !correoCliente.contentEquals("")) {// and
																		// (&)
																		// logico
																		// booleano

			String documento = "/ReportesMoneda/" + nombrereporte + ".jasper";

			path = Reporteador.generarReportePDFElipsis(nombre,param1, param2, param3,
					param4, param5, param5a);
			EnviarMail.enviarReporteMail(promotor, path, fecha);
		} else {
			facesMessages
					.add("Lo siento :-( , el promotor no cuenta con email en la base de datos. "
							+ "Para enviar el informe, actualize los datos de su cliente.");
		}
		// graba en auditoria el envio por correo del balance
		this.AdministrarUsuario.auditarUsuario(25,
				"Envio balance por correo del promotor " + param2
						+ " de la fecha " + fecha + " a la cuenta "
						+ personalHome.getInstance().getCorreo());
		return path;
	}

	Date fechaIniReporte;
	Date fechaFinReporte;

	public Date getFechaIniReporte() {
		return fechaIniReporte;
	}

	public void setFechaIniReporte(Date fechaIniReporte) {
		this.fechaIniReporte = fechaIniReporte;
	}

	public Date getFechaFinReporte() {
		return fechaFinReporte;
	}

	public void setFechaFinReporte(Date fechaFinReporte) {
		this.fechaFinReporte = fechaFinReporte;
	}

	public String generarReporteFechas(String docupromo) {
		
		String path = "";
		Boolean wSaldo = false;
		String mensajeSaldo = "";
		Saldo saldoInicial = this.getSaldoIncial(docupromo);
		BigDecimal vrSaldoAnterior = BigDecimal.ZERO;
				
		//1. se determina la fecha del saldo inicial para el promotor en el periodo actual
		try {	
			
			if(saldoInicial != null){
				
				this.setFechaSaldoPromotor( saldoInicial.getId().getFecha() );
				//2. si la fecha inicio de la consulta es menor o 
				//igual a la fecha del saldo inicial la fecha inicial 
				//de la consulta se cambiara al dia siguiente del saldo inicial 
				if( this.getFechaIniReporte().before(this.getFechaSaldoPromotor()) || 
						this.getFechaIniReporte().equals(this.getFechaSaldoPromotor())){
					log.info("Cambiando la fecha inicio de la consulta");
					
					//Establezco la fecha inicio de la consulta a un dia despues de la 
					//fecha del saldo inicial
					this.cambiarFechaInicioConsulta( this.getFechaSaldoPromotor() );
					wSaldo = true ;
				}
			}else{
				mensajeSaldo =  "El promotor no tiene saldo inicial " +
						"registrado para este periodo, la informacion del reporte puede ser imprecisa.";
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("Fecha Inireporte antes del saldo: " + 
				this.getFechaIniReporte());
		
		if(wSaldo){
			vrSaldoAnterior = saldoInicial.getSaldo();
		}else{
			vrSaldoAnterior =this.saldoAnteriorPromotor(this.getFechaIniReporte(), 
					docupromo);
		}
		String reporte = "ExtractoGeneralPromotor";
		Usuario user = entityManager.find(Usuario.class, identity.getUsername());
		//Genera el reporte
		path = Reporteador.generarReportePDFElipsis(reporte,
													this.getFechaIniReporte(),
													docupromo, 
													vrSaldoAnterior, 
													this.getFechaFinReporte(), 
													user.getNombre(),
													mensajeSaldo);

		this.setFechaIniReporte(null);
		this.setFechaFinReporte(null);

		return path;
	}

	public BigDecimal saldoAnteriorPromotor(Date fecha, String documento) {

		BigDecimal saldoAnterior = BigDecimal.ZERO;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date fechaIniTemp;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(this.getFechaIniReporte());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			fechaIniTemp = calendar.getTime();
			
			//la funcion saldofecha (sql) me retorna el saldo a la fecha anterior ingresada
			String queryString = "select saldofecha( '"
					+ sdf.format(fechaIniTemp) + "' , '" + documento + "')";
			
			saldoAnterior = (BigDecimal) entityManager.createNativeQuery(
					queryString).getSingleResult();

			if (saldoAnterior == null) {
				saldoAnterior = BigDecimal.ZERO;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saldoAnterior;
	}
	
	
	/**
	 * Cambia la fecha inicial de la consulta a un dia despues de la 
	 * fecha del saldo inicial del promotor
	 * @param fechaSaldoIni
	 */
	private void cambiarFechaInicioConsulta(Date fechaSaldoIni){
		
		Date fechaIniTemp;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaSaldoIni);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		fechaIniTemp = calendar.getTime();
		
		log.info("Nueva fecha de consulta Inicial: " + sdf.format(fechaIniTemp) );
		this.setFechaIniReporte(fechaIniTemp);
		
	}
	
	/**
	 * Proporciona la fecha del saldo inicial del promotor para el periodo actual.
	 * Si no cuenta con un saldo inicial retorna null.
	 * @param docupromo
	 * @return
	 */
	private Saldo getSaldoIncial(String docupromo){
		
		Saldo saldoInicial = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String date = sdf.format(this.getFechaIniReporte());
		String queryString =
			"select saldo from Saldo saldo where year(saldo.id.fecha) = year(date('"+ date +"')) " +
			"and  saldo.id.documento = '" + docupromo + "'";
		try {
			saldoInicial = (Saldo) entityManager.createQuery(queryString)
												.getSingleResult() ;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("No se hallo saldo inicial para el promotor " + docupromo);
			return saldoInicial;
		}
		return saldoInicial;
	}

	
	public void enviarBalanceOutlook(Date param1, Object param2, Object param3,
			Object param4, Object param5, Object param5a, String nombre) {
		URI uri = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String nombrereporte = nombre;
		String path = "";
		String fecha = sdf.format(param1);
		Promotor promotor = personalHome.getInstance().getPromotor();
		String correoCliente = promotor.getPersonal().getCorreo();

		if (correoCliente != null & !correoCliente.contentEquals("")) {// and
																		// (&)
																		// logico
																		// booleano

			String documento = "ReportesMoneda\\" + nombrereporte + ".jasper";
			path = Reporteador.generarReportePDFElipsis(nombre,param1, param2, param3,
					param4, param5, param5a);

			String mensaje = "Hola "
					+ promotor.getPersonal().getNombre()
					+ "\nEstimado(a) cliente: "
					+ "\n\nAdjunto encontrara; el extracto de sus cuentas del "
					+ fecha
					+ "Moneda Frontera.\nNo responda a esta direccion de correo electronico.\n\nPara mas informacion "
					+ "contacte a su asesor.\nCordialmente,\n\n"
					+ promotor.getAsesor().getPersonal().getNombre() + " "
					+ promotor.getAsesor().getPersonal().getApellido();

			// String url =
			// "mailTo:"+ promotor.getPersonal().getCorreo() +","+
			// promotor.getAsesor().getPersonal().getCorreo() +
			// "?subject=" +"Extracto de Movimientos Diario <" + fecha +">"+
			// "&body=" + mensaje /*+ "+file&attachment=\\localhost\\" +
			// documento */;

			String url = "http://www.date4j.net";

			try {
				uri = new URI(url);
				UtilidadesBD util = new UtilidadesBD();
				util.openClietMail(url, uri);
			} catch (URISyntaxException ex) {
				System.out.println("CAGADA EN EL MAILTO");
				ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			facesMessages
					.add("Lo siento :-( , el promotor no cuenta con email en la base de datos. "
							+ "Para enviar el informe, actualize los datos de su cliente.");
		}
		// graba en auditoria el envio por correo del balance
		this.AdministrarUsuario.auditarUsuario(25,
				"Envio balance por correo del promotor " + param2
						+ " de la fecha " + fecha + " a la cuenta "
						+ personalHome.getInstance().getCorreo());
	}

	/**
	 * Actualizaciones de gestion de Porcentaje de Comision de Promotor
	 * 
	 */

	@In(create = true)
	@Out
	private PromotorcomisiontxHome promotorcomisiontxHome;

	@In(create = true)
	@Out
	private PromotortasaHome promotortasaHome;

	@In(create = true)
	@Out
	private PaisHome paisHome;

	public void iniciarTasapromotor(String documento) {
		Promotor p = entityManager.find(Promotor.class, documento);
		// promotorcomisiontxHome = new PromotorcomisiontxHome();
		promotortasaHome.getInstance().setPromotor(p);
		// promotorcomisiontxHome.getInstance().setId(new
		// PromotorcomisiontxId());
		promotortasaHome.getInstance().getId().setDocumento(p.getDocumento());
	}

	public String guardarTasapromotor() {

		PromotortasaId pctxid = new PromotortasaId();
		pctxid.setCodigopais(promotortasaHome.getInstance().getPais()
				.getCodigopais());
		pctxid.setDocumento(promotortasaHome.getInstance().getPromotor()
				.getDocumento());
		pctxid.setFecha(promotortasaHome.getInstance().getId().getFecha());

		Promotortasa promotortasabuscar = entityManager.find(
				Promotortasa.class, pctxid);

		Promotortasa pt = new Promotortasa();
		pt.setPromotor(promotortasaHome.getInstance().getPromotor());
		pt.setPais(promotortasaHome.getInstance().getPais());
		pt.setTasa(promotortasaHome.getInstance().getTasa());
		pt.setTasadolar(promotortasaHome.getInstance().getTasadolar());

		pt.setId(pctxid);

		if (promotortasabuscar == null) {
			entityManager.persist(pt);
			entityManager.flush();
			statusMessages.add("Se ha registrado para el promotor "
					+ personalHome.getInstance().getNombre() + " "
					+ personalHome.getInstance().getApellido() + " un tasa de "
					+ pt.getTasa() + " para el pais "
					+ pt.getPais().getNombre());
			promotortasaHome.getInstance().setPais(null);
			promotortasaHome.getInstance().getId().setCodigopais(null);
			promotortasaHome.getInstance().getId().setFecha(null);
			promotortasaHome.getInstance().setTasa(null);
			promotortasaHome.getInstance().setTasadolar(null);

			return "persisted";
		} else {
			entityManager.merge(pt);
			entityManager.flush();
			promotortasaHome.getInstance().setPais(null);
			promotortasaHome.getInstance().getId().setCodigopais(null);
			promotortasaHome.getInstance().getId().setFecha(null);
			promotortasaHome.getInstance().setTasa(null);
			promotortasaHome.getInstance().setTasadolar(null);
			statusMessages.add("Se ha actualizado para el promotor "
					+ personalHome.getInstance().getNombre() + " "
					+ personalHome.getInstance().getApellido() + " un tasa de "
					+ pt.getTasa() + " para el pais "
					+ pt.getPais().getNombre());

			return "updated";
		}
	}

	public void iniciarPromotorcomision(String documento) {
		// promotorcomisiontxHome.clearInstance();
		// promotorcomisiontxHome.
		Promotor p = entityManager.find(Promotor.class, documento);
		// promotorcomisiontxHome = new PromotorcomisiontxHome();
		promotorcomisiontxHome.getInstance().setPromotor(p);
		// promotorcomisiontxHome.getInstance().setId(new
		// PromotorcomisiontxId());
		promotorcomisiontxHome.getInstance().getId().setDocumento(
				p.getDocumento());

	}

	public String guardarPromotorcomision() {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");

		PromotorcomisiontxId pctxid = new PromotorcomisiontxId();
		pctxid.setCodpais(promotorcomisiontxHome.getInstance().getPais()
				.getCodigopais());
		pctxid.setDocumento(promotorcomisiontxHome.getInstance().getPromotor()
				.getDocumento());
		pctxid.setFechainicio(promotorcomisiontxHome.getInstance().getId()
				.getFechainicio());

		Promotorcomisiontx pctxbuscar = entityManager.find(
				Promotorcomisiontx.class, pctxid);

		Promotorcomisiontx pctx = new Promotorcomisiontx();
		pctx.setPromotor(promotorcomisiontxHome.getInstance().getPromotor());
		pctx.setPais(promotorcomisiontxHome.getInstance().getPais());
		pctx
				.setPorcentaje(promotorcomisiontxHome.getInstance()
						.getPorcentaje());
		pctx.setFechafin(promotorcomisiontxHome.getInstance().getFechafin());

		Promotor p = promotorcomisiontxHome.getInstance().getPromotor();
		promotorcomisiontxHome.getInstance().getId().setCodpais(
				promotorcomisiontxHome.getInstance().getPais().getCodigopais());
		promotorcomisiontxHome.getInstance().getId().setDocumento(
				promotorcomisiontxHome.getInstance().getPromotor()
						.getDocumento());

		pctx.setId(pctxid);

		if (pctxbuscar == null) {
			statusMessages.add("Se ha registrado para el promotor "
					+ personalHome.getInstance().getNombre() + " "
					+ personalHome.getInstance().getApellido()
					+ " un porcentaje de " + pctx.getPorcentaje()
					+ " para el pais " + pctx.getPais().getNombre());

			entityManager
					.createNativeQuery(
							"update promotorcomisiontx "
									+ "set fechafin = to_date('"
									+ sdf.format(pctxid.getFechainicio())
									+ "','dd/mm/yyyy') - 1 "
									+ "where "
									+ "fechafin is null and "
									+ "codpais = '"
									+ pctxid.getCodpais()
									+ "' and "
									+ "documento = '"
									+ pctxid.getDocumento()
									+ "' and "
									+ "fechainicio = (select max(ptx.fechainicio) from promotorcomisiontx ptx "
									+ "where ptx.codpais = '"
									+ pctxid.getCodpais() + "' and "
									+ "ptx.documento = '"
									+ pctxid.getDocumento() + "' and "
									+ "fechafin is null) ").executeUpdate();

			entityManager.flush();

			entityManager.persist(pctx);

			entityManager.flush();

			promotorcomisiontxHome.getInstance().setPais(null);
			promotorcomisiontxHome.getInstance().getId().setCodpais(null);
			promotorcomisiontxHome.getInstance().getId().setFechainicio(null);
			promotorcomisiontxHome.getInstance().setFechafin(null);
			promotorcomisiontxHome.getInstance().setPorcentaje(null);

			return "persisted";
		} else {

			entityManager.merge(pctx);

			entityManager.flush();

			promotorcomisiontxHome.getInstance().setPais(null);
			promotorcomisiontxHome.getInstance().getId().setCodpais(null);
			promotorcomisiontxHome.getInstance().getId().setFechainicio(null);
			promotorcomisiontxHome.getInstance().setFechafin(null);
			promotorcomisiontxHome.getInstance().setPorcentaje(null);

			promotorHome.getPromotorcomisiontxes().add(pctx);
			entityManager.flush();

			statusMessages.add("Se ha actualizado para el promotor "
					+ personalHome.getInstance().getNombre() + " "
					+ personalHome.getInstance().getApellido()
					+ " el porcentaje de " + pctx.getPorcentaje()
					+ " para el pais " + pctx.getPais().getNombre());

			return "updated";
		}
	}

	// Genera el pdf del análisis de clientes
	public void generarAnalisisRiesgoCliente() {

		// Estableciendo parametros para el informe en pdf
		String nombreReporte = "analisisderiesgo";
		BigDecimal trm = this.getTasa();
		BigDecimal bolivar = this.getTasabolivar();
		String documentoProm = this.promotorHome.getInstance().getDocumento();
		BigDecimal saldo = this.getSaldoinforme();
		Usuario user = 
			entityManager.find(Usuario.class, identity.getUsername());
		

		// Se genera el reporte
		Reporteador.generarReportePDFElipsis(nombreReporte,trm, bolivar, documentoProm, this
				.getSicad1(), user.getNombre(), saldo );
	}

	/**
	 *Calcula del total en pesos de los dolares de un promotor por el valor de
	 * la tasa del dolar.
	 */
	public float calcularTotalPesos() {
		float total = this.totalDolaresViaje * this.tasa.floatValue();
		return total;

	}

	/**
	 *Calcula del total a favor de un promotor, resultado del total en pesos
	 * menos la comision
	 */
	public float calcularTotalaFavor() {
		BigDecimal comision = BigDecimal.ZERO;
		if (this.promotorHome.getInstance().getComisionviajero() != null) {
			comision = this.promotorHome.getInstance().getComisionviajero();
		}

		float totalaFavor = (this.totalDolaresViaje * this.tasa.floatValue())
				- (this.totalDolaresViaje * this.tasa.floatValue()
						* comision.floatValue() / 100);
		return totalaFavor;
	}

	/**
	 *Calcula el valor en pesos de los depositos proyectados
	 * (TotalDolares*PrecioSicad1*PrecioBolovar)
	 */
	public float calcularDepositosProyectados() {
		float depositos = this.totalDolaresViaje * this.sicad1.floatValue()
				* this.tasabolivar.floatValue();
		return depositos;
	}

	/**
	 *Calcula el monto proyectado a favor del cliente despues de las
	 * transacciones
	 */
	public float calcularMontoProyectado() {
		float monto = this.calcularTotalaFavor()
				- this.calcularDepositosProyectados();
		return monto;
	}

	/**
	 *Calcula la capacidad de endeudamiento del cliente, es decir el 50% del
	 * saldo proyectado
	 */
	public float calcularCapacidadEndeudamiento() {
		return this.calcularMontoProyectado() * 50 / 100;
	}

	/**
	 *Calcula el Monto maximo que se le puede prestar al promotor
	 */
	public float calcularMontoMaximoPrestamo() {
		return this.calcularCapacidadEndeudamiento()
				+ this.saldoinforme.floatValue();
	}

}
