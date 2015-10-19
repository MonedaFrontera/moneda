package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("asesorList") 
public class AsesorList extends EntityQuery<Asesor> {

	private static final String EJBQL = "select asesor from Asesor asesor ";

	//se anadio la restriccion del cargo de asesor
	private static final String[] RESTRICTIONS = { "lower(asesor.documento) like lower(concat(#{asesorList.asesor.documento},'%'))",
												   "asesor.personal.cargo.codcargo = (#{'AS'})",};

	private Asesor asesor = new Asesor();

	public AsesorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		this.setOrder("asesor.personal.nombre");
	}

	public Asesor getAsesor() {
		return asesor;
	}
}
