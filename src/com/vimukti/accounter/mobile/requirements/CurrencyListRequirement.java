package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCurrency;

public abstract class CurrencyListRequirement extends
		ListRequirement<ClientCurrency> {

	public CurrencyListRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCurrency> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		if (getPreferences().isEnableMultiCurrency()) {
			return super.run(context, makeResult, list, actions);
		}
		return null;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().salesPerson());
	}

	@Override
	protected Record createRecord(ClientCurrency value) {
		Record record = new Record(value);
		record.add("Name", value.getFormalName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCurrency value) {
		return value != null ? value.getFormalName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("Add Currency");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().currency());
	}
}