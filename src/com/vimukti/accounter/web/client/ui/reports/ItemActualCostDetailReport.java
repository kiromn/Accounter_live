package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ItemActualCostDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ItemActualCostDetailServerReport;

public class ItemActualCostDetailReport extends
		AbstractReportView<ItemActualCostDetail> {
	private long itemId;
	private long customerId;
	private long jobId;
	private boolean isActualcostDetail;

	public ItemActualCostDetailReport(boolean isActualcostDetail, long itemId, long customerId, long jobId) {
		this.itemId = itemId;
		this.customerId = customerId;
		this.jobId = jobId;
		this.isActualcostDetail = isActualcostDetail;
		this.serverReport = new ItemActualCostDetailServerReport(this,
				isActualcostDetail);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getItemActualCostDetail(start, end,
				itemId,customerId,jobId, isActualcostDetail, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ItemActualCostDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransationType(),
				record.getTransactionId());
	
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void print() {
	long status =0;
	if(isActualcostDetail)
	{
		status=1;
	}
	StringBuffer buffer = new StringBuffer();
	buffer.append(itemId);
	buffer.append(",");
	buffer.append(status);
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 187,String.valueOf(customerId),String.valueOf(jobId),
			 buffer.toString());
	}

	@Override
	public void exportToCsv() {
		long status =0;
		if(isActualcostDetail)
		{//cost- false-0,revenue-true-1
			status=1;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(itemId);
		buffer.append(",");
		buffer.append(status);
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 187,String.valueOf(customerId),String.valueOf(jobId),
				buffer.toString());
	}
}