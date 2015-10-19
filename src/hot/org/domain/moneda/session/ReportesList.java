package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("reportesList")
public class ReportesList extends EntityQuery<Reportes> {

	private static final String EJBQL = "select reportes from Reportes reportes";

	private static final String[] RESTRICTIONS = {
			"lower(reportes.codreporte) like lower(concat(#{reportesList.reportes.codreporte},'%'))",
			"lower(reportes.descripcion) like lower(concat(#{reportesList.reportes.descripcion},'%'))", };

	private Reportes reportes = new Reportes();

	public ReportesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Reportes getReportes() {
		return reportes;
	}
}
