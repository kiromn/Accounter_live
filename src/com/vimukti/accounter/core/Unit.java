package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

public class Unit extends CreatableObject implements IAccounterServerCore,
		Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Measurement measurement;
	private String type;
	private double factor;
	private boolean isDefault;

	public Unit() {
	}

	public Unit(String type, double factor) {
		setType(type);
		setFactor(factor);
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public Unit clone() {
		Unit unitClone = (Unit) this.clone();
		return unitClone;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) {
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		this.setCompany(measurement.getCompany());
		return super.onSave(session);
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
