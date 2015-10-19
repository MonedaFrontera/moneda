package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("configList")
public class ConfigList extends EntityQuery<Config> {

	private static final String EJBQL = "select config from Config config";

	private static final String[] RESTRICTIONS = {};

	private Config config = new Config();

	public ConfigList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Config getConfig() {
		return config;
	}
}
