package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("promotorcomisiontxList")
public class PromotorcomisiontxList extends EntityQuery<Promotorcomisiontx> {

	private static final String EJBQL = "select promotorcomisiontx from Promotorcomisiontx promotorcomisiontx";

	private static final String[] RESTRICTIONS = {
			"lower(promotorcomisiontx.id.codpais) like lower(concat(#{promotorcomisiontxList.promotorcomisiontx.id.codpais},'%'))",
			"lower(promotorcomisiontx.id.documento) like lower(concat(#{promotorcomisiontxList.promotorcomisiontx.id.documento},'%'))", };

	private Promotorcomisiontx promotorcomisiontx;

	public PromotorcomisiontxList() {
		promotorcomisiontx = new Promotorcomisiontx();
		promotorcomisiontx.setId(new PromotorcomisiontxId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Promotorcomisiontx getPromotorcomisiontx() {
		return promotorcomisiontx;
	}
}
