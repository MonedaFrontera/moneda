package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("salesHome")
public class SalesHome extends EntityHome<Sales> {

	public void setSalesId(SalesId id) {
		setId(id);
	}

	public SalesId getSalesId() {
		return (SalesId) getId();
	}

	public SalesHome() {
		setSalesId(new SalesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSalesId().getYear() == null)
			return false;
		if (getSalesId().getMonth() == null)
			return false;
		if (getSalesId().getQty() == null)
			return false;
		return true;
	}

	@Override
	protected Sales createInstance() {
		Sales sales = new Sales();
		sales.setId(new SalesId());
		return sales;
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

	public Sales getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
