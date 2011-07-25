package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListDetailServerReport;

@SuppressWarnings("unchecked")
public class ReverseChargeListDetailReport extends
		AbstractReportView<ReverseChargeListDetail> {

	private String vatAgency;

	public ReverseChargeListDetailReport() {
		this.serverReport = new ReverseChargeListDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(ReverseChargeListDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getTransactionId());

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		ReverseChargeList transactionDetails = (ReverseChargeList) data;
		Accounter.createReportService().getReverseChargeListDetailReport(
				transactionDetails.getName(), start.getTime(), end.getTime(),
				this);
		this.vatAgency = transactionDetails.getName();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getTime())),
				Integer.parseInt(String.valueOf(endDate.getTime())), 145, "",
				"", vatAgency);

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(ReverseChargeListDetail obj1, ReverseChargeListDetail obj2,
			int col) {
		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return obj1.getNumber().compareTo(obj2.getNumber());
		case 3:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase()
					.compareTo(obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getSalesPrice(),
					obj2.getSalesPrice());
		}
		return 0;
	}

	public void exportToCsv() {

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getTime())),
				Integer.parseInt(String.valueOf(endDate.getTime())), 145, "",
				"", vatAgency);
	}

}
