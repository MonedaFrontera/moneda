package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentajeparametrospromotorList")
public class PorcentajeparametrospromotorList extends
		EntityQuery<Porcentajeparametrospromotor> {

	private static final String EJBQL = "select porcentajeparametrospromotor from Porcentajeparametrospromotor porcentajeparametrospromotor";

	private static final String[] RESTRICTIONS = {
			"lower(porcentajeparametrospromotor.id.codbanco) like lower(concat(#{porcentajeparametrospromotorList.porcentajeparametrospromotor.id.codbanco},'%'))",
			"lower(porcentajeparametrospromotor.id.codfranquicia) like lower(concat(#{porcentajeparametrospromotorList.porcentajeparametrospromotor.id.codfranquicia},'%'))",
			"lower(porcentajeparametrospromotor.id.codpais) like lower(concat(#{porcentajeparametrospromotorList.porcentajeparametrospromotor.id.codpais},'%'))",
			"lower(porcentajeparametrospromotor.id.documento) like lower(concat(#{porcentajeparametrospromotorList.porcentajeparametrospromotor.id.documento},'%'))", };

	private Porcentajeparametrospromotor porcentajeparametrospromotor;

	public PorcentajeparametrospromotorList() {
		porcentajeparametrospromotor = new Porcentajeparametrospromotor();
		porcentajeparametrospromotor
				.setId(new PorcentajeparametrospromotorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentajeparametrospromotor getPorcentajeparametrospromotor() {
		return porcentajeparametrospromotor;
	}
}
