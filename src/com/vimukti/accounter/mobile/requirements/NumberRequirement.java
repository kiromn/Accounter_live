package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class NumberRequirement extends SingleRequirement<String> {

	public NumberRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue(0.0d);
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContext(Context context) {
		return context.getString();
	}

}
