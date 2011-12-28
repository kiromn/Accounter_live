package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ManageSalesTaxItemListGrid;

public class SalesTaxItemsView extends BaseListView<ClientTAXItem> {
	private List<ClientTAXItem> listOfTaxItems;

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewVatItemAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addaNewTaxItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages.taxItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageSalesTaxItemListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfTaxItems = getCompany().getActiveTaxItems();
		filterList(true);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXItem item : listOfTaxItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}

	}

	@Override
	public void onSuccess(PaginationList<ClientTAXItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	@Override
	protected String getViewTitle() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = messages.manageSalesItems();
		else
			constant = messages.salesTaxItems();
		return constant;
	}

}
