package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistatxdiasemanaList")
public class VistatxdiasemanaList extends EntityQuery<Vistatxdiasemana> {

	private static final String EJBQL = "select vistatxdiasemana from Vistatxdiasemana vistatxdiasemana";

	private static final String[] RESTRICTIONS = {
			"lower(vistatxdiasemana.id.promotor) like lower(concat(#{vistatxdiasemanaList.vistatxdiasemana.id.promotor},'%'))",
			"lower(vistatxdiasemana.id.asesor) like lower(concat(#{vistatxdiasemanaList.vistatxdiasemana.id.asesor},'%'))", };

	private Vistatxdiasemana vistatxdiasemana;

	public VistatxdiasemanaList() {
		vistatxdiasemana = new Vistatxdiasemana();
		vistatxdiasemana.setId(new VistatxdiasemanaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistatxdiasemana getVistatxdiasemana() {
		return vistatxdiasemana;
	}
}
