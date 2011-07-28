package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

@SuppressWarnings("unchecked")
public class VendorBillListDialog extends AbstractBaseDialog {

	private VendorBillView view;
	// private CustomersMessages customerConstants = GWT
	// .create(CustomersMessages.class);
	private AccounterConstants financeConstants = GWT
			.create(AccounterConstants.class);
	private DialogGrid grid;
	private List<PurchaseOrdersAndItemReceiptsList> list;
	public ClientVendor preVendor;

	public VendorBillListDialog(VendorBillView view,
			List<PurchaseOrdersAndItemReceiptsList> list) {
		super(view);
		this.view = view;
		this.list = list;
		// setTitle("");
		setText(Accounter.constants().purchaseOrderList());
		createControls();
		setWidth("600");
		setQuoteList(list);
		show();
		center();
	}

	private void createControls() {
		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.constants().selectPurchaseOrder()
				+ Accounter.constants().selectDocument());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(Accounter.constants().Date(), Accounter.constants()
				.no(), Accounter.constants().type(), UIUtils.getVendorString(
				Accounter.constants().supplierName(), Accounter.constants()
						.vendorName()), Accounter.constants().total(),
				Accounter.constants().remainingTotal());
		grid.setCellsWidth(60, 20, 90, -1, 60, 95);
		grid.setView(this);
		grid.init();

		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler<PurchaseOrdersAndItemReceiptsList>() {

			@Override
			public void OnCellDoubleClick(
					PurchaseOrdersAndItemReceiptsList core, int column) {

				setRecord(core);
				// try {
				// ClientEstimate record = (ClientEstimate) core;
				//
				// String estimateId = record.getID();
				// selectedEstimate = getEstimate(estimateId);
				//
				// if (invoiceView != null && selectedEstimate != null)
				// invoiceView.selectedQuote(selectedEstimate);
				//
				// removeFromParent();
				//
				// } catch (Exception e) {
				// Accounter.showError("Error Loading Quote...");
				// }

			}
		});

		// getGridData();
		// setQuoteList(estimates);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		AccounterButton helpButton = new AccounterButton(
				financeConstants.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.constants().sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		helpButton.enabledButton();

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);

		AccounterButton okButton = new AccounterButton(financeConstants.ok());
		okButton.setWidth("100px");
		okButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) grid
						.getSelection();
				setRecord(record);

				// try {
				// ClientEstimate selectedEstimate = (ClientEstimate) grid
				// .getSelection();
				// if (invoiceView != null && selectedEstimate != null)
				// invoiceView.selectedQuote(selectedEstimate);
				// removeFromParent();
				//
				// } catch (Exception e) {
				// Accounter.showError("Error Loading Quote...");
				// }

			}

		});
		okButtonLayout.add(okButton);

		okButton.enabledButton();

		AccounterButton cancelButton = new AccounterButton(
				financeConstants.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);
		cancelButton.enabledButton();
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setWidth("100%");
		// buttonLayout.add(helpButtonLayout);
		buttonLayout.add(okButtonLayout);
		buttonLayout.setCellHorizontalAlignment(okButtonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);

		mainLayout.add(buttonLayout);
		mainLayout.setSize("100%", "100%");
		add(mainLayout);

	}

	protected void setRecord(PurchaseOrdersAndItemReceiptsList record) {

		if (record != null) {
			if (record.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
				getPurchaseOrder(record);

			} else {
				getItemReceipt(record);
			}
		}

		removeFromParent();

	}

	private void getItemReceipt(PurchaseOrdersAndItemReceiptsList record) {
		AsyncCallback<ClientItemReceipt> callback = new AsyncCallback<ClientItemReceipt>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(Accounter.constants()
							.errorLoadingItemReceipt());
				}

			}

			@Override
			public void onSuccess(ClientItemReceipt result) {
				if (result != null)
					view.selectedItemReceipt(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.ITEMRECEIPT,
				record.getTransactionId(), callback);

	}

	private void getPurchaseOrder(PurchaseOrdersAndItemReceiptsList record) {
		AsyncCallback<ClientPurchaseOrder> callback = new AsyncCallback<ClientPurchaseOrder>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(Accounter.constants()
							.errorLoadingPurchaseOrder());
				}

			}

			@Override
			public void onSuccess(ClientPurchaseOrder result) {
				if (result != null)
					view.selectedPurchaseOrder(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
				record.getTransactionId(), callback);

	}

	public void setQuoteList(List<PurchaseOrdersAndItemReceiptsList> result) {
		if (result == null)
			return;
		this.list = result;
		grid.removeAllRecords();
		if (list.size() > 0) {
			for (PurchaseOrdersAndItemReceiptsList rec : list) {
				grid.addData(rec);
			}
		} else
			grid.addEmptyMessage(Accounter.constants().norecordstoshow());
	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) obj;
		if (record != null) {
			switch (index) {
			case 0:
				return UIUtils.dateFormat(record.getDate());
			case 1:
				return record.getTransactionNumber();
			case 2:
				return Utility.getTransactionName(record.getType());
			case 3:
				return record.getVendorName();
			case 4:
				return DataUtils.getAmountAsString(record.getTotal());
			case 5:
				return DataUtils.getAmountAsString(record.getRemainingTotal());
			}
		}
		return null;

	}

	public void setFocus() {
		// cancelBtn.setFocus(true);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void processupdateView(IAccounterCore core, int command) {
	}
}