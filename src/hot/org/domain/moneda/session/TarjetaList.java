package org.domain.moneda.session;

import org.domain.moneda.bussiness.AdministrarVariable;
import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

import java.util.Arrays;
import java.util.Date;

@Name("tarjetaList")
public class TarjetaList extends EntityQuery<Tarjeta> {

	private static final String EJBQL = "select tarjeta from Tarjeta tarjeta";

	private static final String[] RESTRICTIONS = {
			"lower(concat(concat(promotor.personal.nombre,' '),promotor.personal.apellido)) like lower(concat(#{tarjetaList.nombre},'%'))",
			"lower(tarjeta.numerotarjeta) like lower(concat('%',concat(#{tarjetaList.tarjeta.numerotarjeta},'%')))",
			"lower(tarjeta.fechavencimiento) like lower(concat(#{tarjetaList.tarjeta.fechavencimiento},'%'))",
			"lower(tarjeta.codseguridad) like lower(concat(#{tarjetaList.tarjeta.codseguridad},'%'))",
			"lower(tarjeta.tarjetahabiente) like lower(concat('%',concat(#{tarjetaList.tarjeta.tarjetahabiente},'%')))",
			"promotor.asesor.documento like concat(#{tarjetaList.docasesor},'%')",
			"lower(tarjeta.direcciontarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.direcciontarjetahabiente},'%'))",
			"lower(tarjeta.cedulatarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.cedulatarjetahabiente},'%'))",
			"lower(tarjeta.telefonotarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.telefonotarjetahabiente},'%'))", };
	
	
	@In(create=true)
	private AdministrarVariable AdministrarVariable;
	
	@In Identity identity;
	
	private Promotor promotor = new Promotor();
	
	private Personal personal = new Personal();
	
	private Asesor asesor = new Asesor();
	
	private Tarjeta tarjeta = new Tarjeta();
	
	//private Tarjeta viaje = new Tarjeta();
	
	private String nombre = new String();
	
	private String docasesor = new String();
	
	private String docpromotor = new String();
	
	private String estado = "t";
	
	private String d = null;

	public TarjetaList() {
		setEjbql(EJBQL);
		
		System.out.println("Estado " + this.estado);
		
		if (this.getEstado().contentEquals("a")){
			String[] restricciones = {
					"lower(concat(concat(promotor.personal.nombre,' '),promotor.personal.apellido)) like lower(concat(#{tarjetaList.nombre},'%'))",
					"lower(tarjeta.numerotarjeta) like lower(concat('%',concat(#{tarjetaList.tarjeta.numerotarjeta},'%')))",
					"lower(tarjeta.fechavencimiento) like lower(concat(#{tarjetaList.tarjeta.fechavencimiento},'%'))",
					"lower(tarjeta.codseguridad) like lower(concat(#{tarjetaList.tarjeta.codseguridad},'%'))",
					"lower(tarjeta.tarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.tarjetahabiente},'%'))",
					"promotor.asesor.documento like concat(#{tarjetaList.docasesor},'%')",
					"lower(tarjeta.direcciontarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.direcciontarjetahabiente},'%'))",
					"lower(tarjeta.cedulatarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.cedulatarjetahabiente},'%'))",
					"lower(tarjeta.telefonotarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.telefonotarjetahabiente},'%'))",
					"#{tarjetaList.d} between tarjeta.viaje.fechainicio and tarjeta.viaje.fechafin"};
			setRestrictionExpressionStrings(Arrays.asList(restricciones));
		}else if (this.getEstado().contentEquals("b")){
			String[] restricciones = {
					"lower(concat(concat(promotor.personal.nombre,' '),promotor.personal.apellido)) like lower(concat(#{tarjetaList.nombre},'%'))",
					"lower(tarjeta.numerotarjeta) like lower(concat('%',concat(#{tarjetaList.tarjeta.numerotarjeta},'%')))",
					"lower(tarjeta.fechavencimiento) like lower(concat(#{tarjetaList.tarjeta.fechavencimiento},'%'))",
					"lower(tarjeta.codseguridad) like lower(concat(#{tarjetaList.tarjeta.codseguridad},'%'))",
					"lower(tarjeta.tarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.tarjetahabiente},'%'))",
					"promotor.asesor.documento like concat(#{tarjetaList.docasesor},'%')",
					"lower(tarjeta.direcciontarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.direcciontarjetahabiente},'%'))",
					"lower(tarjeta.cedulatarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.cedulatarjetahabiente},'%'))",
					"lower(tarjeta.telefonotarjetahabiente) like lower(concat(#{tarjetaList.tarjeta.telefonotarjetahabiente},'%'))",
					"", };
			setRestrictionExpressionStrings(Arrays.asList(restricciones));
		}else{
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		}
		
		//RESTRICTIONS.
		
		//if(identity.hasRole("Asesor")){
			//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		//}else{
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
		//System.out.println("SQL"+this.getEjbql());
		//}
		setMaxResults(25);
	}

	public Tarjeta getTarjeta() {
		return tarjeta;
	}
	
	public Promotor getPromotor() { 
		return promotor;
	}
	
	public Personal getPersonal() { 
		return personal;
	}
	
	public String getNombre() {
		//System.out.println("Asesor " + this.nombre);
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public void cambiarEstado(String estado) {
		System.out.println("Aca " + estado);
		this.estado = estado;
		//AdministrarEnvios.setEstado(estado);
	}
	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDocasesor() {
		if (identity.hasRole("Asesor")){
		if (this.nombre!=null&&!this.nombre.contentEquals(""))
			docasesor = null;
		else
			docasesor = identity.getUsername();
		}
		return docasesor;
	}

	

	public Asesor getAsesor() { 
		//asesor = AdministrarVariable.getAsesor();
		if (identity.hasRole("Asesor")){
			asesor.setDocumento(identity.getUsername());
		}
		
		//asesor.setDocumento(identity.getUsername());
		/*
		else{
			//asesor.setDocumento("%");;
			return null;
		}
		*/
		return asesor;
	}
	
	public String getDocpromotor() {
		if (identity.hasRole("Asesor")){
		if (this.nombre!=null)
			docasesor = identity.getUsername();
		else
			docasesor = null;
		}
		return docpromotor;
	}

	public String getD() {
		//Date dd = new Date();
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(new Date());
		
		
	}

	public void setD(String d) {
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		this.d = sdf.format(new Date());
	}
	
	
}
