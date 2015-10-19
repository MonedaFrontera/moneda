package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("citaList")
public class CitaList extends EntityQuery<Cita> {

	private static final String EJBQL = "select cita from Cita cita";

	private static final String[] RESTRICTIONS = { "lower(cita.observacion) like lower(concat(#{citaList.cita.observacion},'%'))", };

	private Cita cita;

	public CitaList() {
		cita = new Cita();
		cita.setId(new CitaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cita getCita() {
		return cita;
	}
}
