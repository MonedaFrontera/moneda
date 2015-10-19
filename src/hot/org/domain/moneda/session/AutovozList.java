package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

import java.util.Arrays;
import java.util.List;

@Name("autovozList")
public class AutovozList extends EntityQuery<Autovoz> {
	
	@In(create = true)
	EstablecimientoHome establecimientoHome;
	
	private static final String EJBQL = "select autovoz from Autovoz autovoz";

	private static final String[] RESTRICTIONS = {
			"lower(autovoz.tipotx) like lower(concat(#{autovozList.autovoz.tipotx},'%'))",
			"lower(tarjeta.promotor.asesor.documento) like lower(concat(#{autovozList.asesor.documento},'%'))",
			"lower(tarjeta.promotor.documento) like lower(concat(#{autovozList.promotor.documento},'%'))",
			"lower(tarjeta.numerotarjeta) like lower(concat('%',#{autovozList.numtarjeta}))",
			"autovoz.fechatx >= #{autovozList.autovoz.fechatx}",
			"autovoz.fechatx <= #{autovozList.autovoz.fechatx}",
			"(autovoz.numtransaccion is null or true = #{autovozList.estado})",
			"lower(autovoz.establecimiento.codigounico) like lower(concat(#{autovozList.establecimiento.codigounico},'%'))",
			"lower(autovoz.numautorizacion) like lower(concat(#{autovozList.autovoz.numautorizacion},'%'))", };

	private Autovoz autovoz = new Autovoz();

	private Asesor asesor = new Asesor();
	
	private Promotor promotor = new Promotor();
	
	private Boolean estado = false;
	
	private String numtarjeta = null;
	
	private Establecimiento establecimiento = new Establecimiento();
	
	@In Identity identity;
	
	/****   Probar intentando obtener un objeto de una clase externa que sea
	 * tipo session
	 */
	
	public AutovozList() {
		setEjbql(EJBQL);
		List x = Arrays.asList(RESTRICTIONS);
		System.out.println("Estado " + estado);
		if(!estado){
			autovoz.setNumtransaccion(999999);
			//x.add("autovoz.numtransaccion is null");
		}else{
			autovoz.setNumtransaccion(0);
		}
		//x.add("")
		setRestrictionExpressionStrings(x);
		System.out.println(this.getEjbql());
		setMaxResults(25);
	}

	public Autovoz getAutovoz() {
		return autovoz;
	}
	
	
	public Asesor getAsesor() { 
		//asesor = AdministrarVariable.getAsesor();
		if (identity.hasRole("Asesor")){
			asesor.setDocumento(identity.getUsername());
		}else{
			//asesor.setDocumento("%");;
			return null;
		}
		return asesor;
	}

	public Promotor getPromotor() {
		return promotor;
	}

	public void setPromotor(Promotor promotor) {
		this.promotor = promotor;
	}

	public Establecimiento getEstablecimiento() {
		establecimiento = establecimientoHome.getInstance();
		return establecimiento;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	
	public void cambiarEstado(Boolean estado) {
		System.out.println("Aca " + estado);
		this.estado = estado;
	}

	public String getNumtarjeta() {
		return numtarjeta;
	}

	public void setNumtarjeta(String numtarjeta) {
		this.numtarjeta = numtarjeta;
	}
	
	
	
	
	

}
