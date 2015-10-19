package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldojuevesList")
public class VistasaldojuevesList extends EntityQuery<Vistasaldojueves> {

	private static final String EJBQL = "select vistasaldojueves from Vistasaldojueves vistasaldojueves";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldojueves.id.docupromo) like lower(concat(#{vistasaldojuevesList.vistasaldojueves.id.docupromo},'%'))",
			"lower(vistasaldojueves.id.nombrepromo) like lower(concat(#{vistasaldojuevesList.vistasaldojueves.id.nombrepromo},'%'))",
			"lower(vistasaldojueves.id.asesora) like lower(concat(#{vistasaldojuevesList.vistasaldojueves.id.asesora},'%'))", };

	private Vistasaldojueves vistasaldojueves;

	public VistasaldojuevesList() {
		vistasaldojueves = new Vistasaldojueves();
		vistasaldojueves.setId(new VistasaldojuevesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldojueves getVistasaldojueves() {
		return vistasaldojueves;
	}
}
