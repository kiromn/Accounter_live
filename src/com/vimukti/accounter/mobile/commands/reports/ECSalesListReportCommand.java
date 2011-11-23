package com.vimukti.accounter.mobile.commands.reports;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;

public class ECSalesListReportCommand extends
		AbstractReportCommand<ECSalesList> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(ECSalesList record) {
		Record salesRecord = new Record(record);
		salesRecord.add("", record.getName());
		salesRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		return salesRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ECSalesList> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(ECSalesList selection,
			CommandList commandList) {
		commandList.add("EC Sales List Detail");
	}

}