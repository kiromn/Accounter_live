package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorListGrid;

/**
 * 
 * @author venki.p modified by Rajesh.A
 * @modified Fernandez
 * 
 */
public class VendorListView extends BaseListView<PayeeList> {

	private VendorsMessages vendorConstants = GWT.create(VendorsMessages.class);
	private List<PayeeList> listOfPayees;

	public VendorListView() {
		super();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		super.deleteFailed(caught);

		Accounter.showInformation(FinanceApplication.getVendorsMessages()
				.youCantDelete());

	}

	@Override
	public Action getAddNewAction() {

		return VendorsActionFactory.getNewVendorAction();
	}

	@Override
	protected String getAddNewLabelString() {

		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.addaNewSupplier(), FinanceApplication.getVendorsMessages()
				.addANewVendor());
	}

	@Override
	protected String getListViewHeading() {

		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.supplierList(), FinanceApplication.getVendorsMessages()
				.vendorList());
	}

	// protected List<ClientPayee> getRecords() {
	//
	// List<ClientVendor> vendors = FinanceApplication.getCompany()
	// .getVendors();
	// List<ClientTaxAgency> taxAgencies = FinanceApplication.getCompany()
	// .gettaxAgencies();
	//
	// List<ClientVATAgency> vatAgencies = FinanceApplication.getCompany()
	// .getVatAgencies();
	//
	// List<ClientPayee> records = new ArrayList<ClientPayee>();
	// if (vendors != null)
	// records.addAll(vendors);
	//
	// if (FinanceApplication.getCompany().isUKAccounting()
	// && vatAgencies != null) {
	// records.addAll(vatAgencies);
	// } else if (!FinanceApplication.getCompany().isUKAccounting()
	// && taxAgencies != null) {
	// records.addAll(taxAgencies);
	// }
	//
	// return records;
	// }

	@Override
	protected void updateTotal(PayeeList t, boolean add) {

		if (add) {
			total += t.getBalance();
		} else
			total -= t.getBalance();
		totalLabel.setText(FinanceApplication.getVendorsMessages()
				.totalOutStandingBalance()

				+ DataUtils.getAmountAsString(total) + "");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {

		// grid.addFooterValue(FinanceApplication.getVendorsMessages().total(),
		// 8);
		// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) +
		// "",
		// 9);

		return null;
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_VENDOR, this);

	}

	@Override
	protected void initGrid() {
		grid = new VendorListGrid(false);
		grid.init();
		// listOfPayees = getRecords();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();
		for (PayeeList payee : listOfPayees) {
			if (isActive) {
				if (payee.isActive() == true)
					grid.addData(payee);

			} else if (payee.isActive() == false) {
				grid.addData(payee);

			}

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(List<PayeeList> result) {
		this.listOfPayees = result;
		super.onSuccess(result);
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}
