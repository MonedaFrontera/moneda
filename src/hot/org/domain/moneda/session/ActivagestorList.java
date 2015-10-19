package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("activagestorList")
public class ActivagestorList extends EntityQuery<Activagestor> {

	private static final String EJBQL = "select activagestor from Activagestor activagestor";

	private static final String[] RESTRICTIONS = {
			"lower(activagestor.id.documento) like lower(concat(#{activagestorList.activagestor.id.documento},'%'))",
			"lower(activagestor.observacion) like lower(concat(#{activagestorList.activagestor.observacion},'%'))", };

	private Activagestor activagestor;

	public ActivagestorList() {
		activagestor = new Activagestor();
		activagestor.setId(new ActivagestorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Activagestor getActivagestor() {
		return activagestor;
	}
}
