package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bannerList")
public class BannerList extends EntityQuery<Banner> {

	private static final String EJBQL = "select banner from Banner banner";

	private static final String[] RESTRICTIONS = {
			"lower(banner.imagen) like lower(concat(#{bannerList.banner.imagen},'%'))",
			"lower(banner.caption) like lower(concat(#{bannerList.banner.caption},'%'))",
			"lower(banner.clase) like lower(concat(#{bannerList.banner.clase},'%'))", };

	private Banner banner = new Banner();

	public BannerList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Banner getBanner() {
		return banner;
	}
}
