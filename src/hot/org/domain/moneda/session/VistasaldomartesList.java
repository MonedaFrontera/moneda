package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldomartesList")
public class VistasaldomartesList extends EntityQuery<Vistasaldomartes> {

	private static final String EJBQL = "select vistasaldomartes from Vistasaldomartes vistasaldomartes";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldomartes.id.docupromo) like lower(concat(#{vistasaldomartesList.vistasaldomartes.id.docupromo},'%'))",
			"lower(vistasaldomartes.id.nombrepromo) like lower(concat(#{vistasaldomartesList.vistasaldomartes.id.nombrepromo},'%'))",
			"lower(vistasaldomartes.id.asesora) like lower(concat(#{vistasaldomartesList.vistasaldomartes.id.asesora},'%'))", };

	private Vistasaldomartes vistasaldomartes;

	public VistasaldomartesList() {
		vistasaldomartes = new Vistasaldomartes();
		vistasaldomartes.setId(new VistasaldomartesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldomartes getVistasaldomartes() {
		return vistasaldomartes;
	}
}
