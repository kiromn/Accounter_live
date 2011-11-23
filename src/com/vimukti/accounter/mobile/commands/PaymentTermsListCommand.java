package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class PaymentTermsListCommand extends NewAbstractCommand {
	private static final String PAYMENT_TERMS = "PaymentTerms";

	@Override
	public String getId() {
		return null;
	}

	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<PaymentTerms>(PAYMENT_TERMS, null, 10) {
			@Override
			protected Record createRecord(PaymentTerms value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getDescription());
				record.add("", value.getDueDays());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getMessages().paymentTerm()));
			}

			@Override
			protected boolean filter(PaymentTerms e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				List<PaymentTerms> list = new ArrayList<PaymentTerms>();
				Set<PaymentTerms> terms = context.getCompany()
						.getPaymentTerms();
				for (PaymentTerms a : terms) {
					list.add(a);
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().paymentTermList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(PaymentTerms value) {
				return getMessages().update() + " " + value.getName();
			}
		});
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

}
