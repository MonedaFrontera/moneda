package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bannerHome")
public class BannerHome extends EntityHome<Banner> {

	public void setBannerConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getBannerConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Banner createInstance() {
		Banner banner = new Banner();
		return banner;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Banner getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
