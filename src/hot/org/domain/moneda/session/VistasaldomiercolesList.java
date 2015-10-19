package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldomiercolesList")
public class VistasaldomiercolesList extends EntityQuery<Vistasaldomiercoles> {

	private static final String EJBQL = "select vistasaldomiercoles from Vistasaldomiercoles vistasaldomiercoles";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldomiercoles.id.docupromo) like lower(concat(#{vistasaldomiercolesList.vistasaldomiercoles.id.docupromo},'%'))",
			"lower(vistasaldomiercoles.id.nombrepromo) like lower(concat(#{vistasaldomiercolesList.vistasaldomiercoles.id.nombrepromo},'%'))",
			"lower(vistasaldomiercoles.id.asesora) like lower(concat(#{vistasaldomiercolesList.vistasaldomiercoles.id.asesora},'%'))", };

	private Vistasaldomiercoles vistasaldomiercoles;

	public VistasaldomiercolesList() {
		vistasaldomiercoles = new Vistasaldomiercoles();
		vistasaldomiercoles.setId(new VistasaldomiercolesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldomiercoles getVistasaldomiercoles() {
		return vistasaldomiercoles;
	}
}
