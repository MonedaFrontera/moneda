package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarparametrospromotorList")
public class TasadolarparametrospromotorList extends
		EntityQuery<Tasadolarparametrospromotor> {

	private static final String EJBQL = "select tasadolarparametrospromotor from Tasadolarparametrospromotor tasadolarparametrospromotor";

	private static final String[] RESTRICTIONS = {
			"lower(tasadolarparametrospromotor.id.codbanco) like lower(concat(#{tasadolarparametrospromotorList.tasadolarparametrospromotor.id.codbanco},'%'))",
			"lower(tasadolarparametrospromotor.id.codfranquicia) like lower(concat(#{tasadolarparametrospromotorList.tasadolarparametrospromotor.id.codfranquicia},'%'))",
			"lower(tasadolarparametrospromotor.id.codpais) like lower(concat(#{tasadolarparametrospromotorList.tasadolarparametrospromotor.id.codpais},'%'))",
			"lower(tasadolarparametrospromotor.id.documento) like lower(concat(#{tasadolarparametrospromotorList.tasadolarparametrospromotor.id.documento},'%'))", };

	private Tasadolarparametrospromotor tasadolarparametrospromotor;

	public TasadolarparametrospromotorList() {
		tasadolarparametrospromotor = new Tasadolarparametrospromotor();
		tasadolarparametrospromotor.setId(new TasadolarparametrospromotorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadolarparametrospromotor getTasadolarparametrospromotor() {
		return tasadolarparametrospromotor;
	}
}
