package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("personalList")
public class PersonalList extends EntityQuery<Personal> {
 
	private static final String EJBQL = "select personal from Personal personal";

	private static final String[] RESTRICTIONS = {
			"lower(personal.documento) like lower(concat(#{personalList.personal.documento},'%'))",
			"lower(personal.tipodocumento) like lower(concat(#{personalList.personal.tipodocumento},'%'))",
			"lower(personal.nombre) like lower(concat('%',concat(#{personalList.personal.nombre},'%')))",
			"lower(personal.apellido) like lower(concat('%',concat(#{personalList.personal.apellido},'%')))",
			"lower(personal.celular) like lower(concat(#{personalList.personal.celular},'%'))",
			"lower(personal.telefono) like lower(concat(#{personalList.personal.telefono},'%'))",
			"lower(personal.pinbb) like lower(concat(#{personalList.personal.pinbb},'%'))",
			"lower(personal.direccion) like lower(concat(#{personalList.personal.direccion},'%'))",
			"lower(personal.correo) like lower(concat(#{personalList.personal.correo},'%'))",
			"lower(personal.correoalternativo) like lower(concat(#{personalList.personal.correoalternativo},'%'))", };

	private Personal personal = new Personal();

	public PersonalList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	
	public void setPersonal(Personal personal) {
		this.personal = personal;
	}



	public Personal getPersonal() {
		return personal;
	}
}
