package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldolunesList")
public class VistasaldolunesList extends EntityQuery<Vistasaldolunes> {

	private static final String EJBQL = "select vistasaldolunes from Vistasaldolunes vistasaldolunes";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldolunes.id.docupromo) like lower(concat(#{vistasaldolunesList.vistasaldolunes.id.docupromo},'%'))",
			"lower(vistasaldolunes.id.nombrepromo) like lower(concat(#{vistasaldolunesList.vistasaldolunes.id.nombrepromo},'%'))",
			"lower(vistasaldolunes.id.asesora) like lower(concat(#{vistasaldolunesList.vistasaldolunes.id.asesora},'%'))", };

	private Vistasaldolunes vistasaldolunes;

	public VistasaldolunesList() {
		vistasaldolunes = new Vistasaldolunes();
		vistasaldolunes.setId(new VistasaldolunesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldolunes getVistasaldolunes() {
		return vistasaldolunes;
	}
}
