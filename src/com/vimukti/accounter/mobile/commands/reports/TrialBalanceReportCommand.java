package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class TrialBalanceReportCommand extends
		NewAbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);

		trialRecord.add("Account Name", record.getAccountName());
		if (getCompany().getPreferences().getUseAccountNumbers())
			trialRecord.add("Account Number", record.getAccountNumber());
		else
			trialRecord.add("", "");
		trialRecord.add("Debit", record.getDebitAmount());
		trialRecord.add("Credit", record.getCreditAmount());
		return trialRecord;
	}

	@Override
	protected List<TrialBalance> getRecords() {
		List<TrialBalance> trialBalanceDetails = new ArrayList<TrialBalance>();
		try {
			new FinanceTool().getReportManager().getTrialBalance(
					getStartDate(), getEndDate(), getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
			trialBalanceDetails = new ArrayList<TrialBalance>();
		}
		return trialBalanceDetails;
	}

	@Override
	protected String addCommandOnRecordClick(TrialBalance selection) {
		return "Transaction Detail By Account ," + selection.getAccountNumber();
	}

	@Override
	protected String getEmptyString() {
		return "You don't have any trial balance report details";
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return "Select any report to view report details of that account";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return "Trial balance report details";
	}

	@Override
	public String getSuccessMessage() {
		return "Trial balance report command closed successfully";
	}
}