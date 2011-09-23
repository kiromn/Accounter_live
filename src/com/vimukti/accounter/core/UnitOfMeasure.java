package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class UnitOfMeasure extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -952703729336678057L;
	/**
	 * Type of the UnitOfMeasure
	 */
	int type;

	/**
	 * The name of the UnitOfMeasure
	 */
	String name;
	String abbreviation;

	/**
	 * @return the version
	 */
	public UnitOfMeasure() {

	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
