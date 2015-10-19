package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("administrativoList")
public class AdministrativoList extends EntityQuery<Administrativo> {

	private static final String EJBQL = "select administrativo from Administrativo administrativo";

	private static final String[] RESTRICTIONS = {
			"lower(administrativo.documento) like lower(concat(#{administrativoList.administrativo.documento},'%'))",
			"lower(administrativo.cargo) like lower(concat(#{administrativoList.administrativo.cargo},'%'))", };

	private Administrativo administrativo = new Administrativo();

	public AdministrativoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Administrativo getAdministrativo() {
		return administrativo;
	}
}
