package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseClosedOrderAction extends Action {

	public PurchaseClosedOrderAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public ImageResource getBigImage() {
		// Nothing to do
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	// // Nothing to do
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				PurchaseClosedOrderReport report = new PurchaseClosedOrderReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, PurchaseClosedOrderAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
			}
		});

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		return "purchase-closed";
	}

	@Override
	public String getText() {
		return messages.purchaseClosedOrder();
	}

}
