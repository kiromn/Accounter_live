package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.requirements.SingleRequirement;

public class BoxRequirement extends SingleRequirement<String> {

	public BoxRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContext(Context context) {
		return context.getString();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_BOX);
	}

}