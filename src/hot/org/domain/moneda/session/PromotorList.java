package org.domain.moneda.session;

import org.domain.moneda.bussiness.AdministrarVariable;
import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

import java.util.Arrays;

@Name("promotorList")
public class PromotorList extends EntityQuery<Promotor> {

	private static final String EJBQL = "select promotor from Promotor promotor";

	private static final String[] RESTRICTIONS = {
			"lower(promotor.documento) like lower(concat(#{promotorList.promotor.documento},'%'))",
			"lower(personal.tipodocumento) like lower(concat(#{promotorList.personal.tipodocumento},'%'))",
/*			"lower(asesor.documento) like lower(concat(#{promotorList.asesor.documento},'%'))",*/
			"lower(asesor.documento) like lower(concat(#{promotorList.asesor.documento},'%'))",
			"lower(personal.nombre) like lower(concat(#{promotorList.personal.nombre},'%'))",
			"lower(personal.apellido) like lower(concat(#{promotorList.personal.apellido},'%'))",
			"lower(personal.celular) like lower(concat(#{promotorList.personal.celular},'%'))",
			"lower(personal.telefono) like lower(concat(#{promotorList.personal.telefono},'%'))",
			"lower(personal.pinbb) like lower(concat(#{promotorList.personal.pinbb},'%'))",
			"lower(personal.direccion) like lower(concat(#{promotorList.personal.direccion},'%'))",
			"lower(personal.correo) like lower(concat(#{promotorList.personal.correo},'%'))",
			"lower(personal.correoalternativo) like lower(concat(#{promotorList.personal.correoalternativo},'%'))",};
	
	@In(create=true)
	private AdministrarVariable AdministrarVariable;
	
	@In Identity identity;
	
	private Promotor promotor = new Promotor();
	
	private Personal personal = new Personal();
	
	private Asesor asesor = new Asesor();

	public PromotorList() {
		//asesor = AdministrarVariable.getAsesor();
		//asesor.setDocumento(identity.getUsername());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Promotor getPromotor() { 
		return promotor;
	}
	
	public Personal getPersonal() { 
		return personal;
	}
	
	public Asesor getAsesor() { 
		//asesor = AdministrarVariable.getAsesor();
		asesor.setDocumento(identity.getUsername());
		return asesor;
	}
}
