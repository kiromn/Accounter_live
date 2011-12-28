package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class RecurringButton extends ImageButton {

	private AbstractTransactionBaseView<?> view;

	public RecurringButton(String save) {
		super(save, Accounter.getFinanceImages().saveAndClose());
		this.addStyleName("saveAndClose-Btn");
	}

	public void setView(AbstractTransactionBaseView<?> view) {
		this.view = view;
		addHandler();
	}

	/**
	 * Creates new Instance
	 */
	public RecurringButton(AbstractTransactionBaseView<?> view) {

		super(messages.makeItRecurring(), Accounter
				.getFinanceImages().saveAndClose());
		this.view = view;
		this.addStyleName("saveAndClose-Btn");
		this.setTitle(messages.clickThisTo(this.getText(),
				view.getAction().getViewName()));
		addHandler();
	}

	private void addHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				ClientTransaction transaction = view.getTransactionObject();
				if (transaction.getRecurringTransaction() == 0) {
					// create new recurring for this transaction
					view.openRecurringDialog();
				} else {
					// open existing recurring transaction.
					Accounter.createGETService().getObjectById(
							AccounterCoreType.RECURRING_TRANSACTION,
							transaction.getRecurringTransaction(),
							new AsyncCallback<ClientRecurringTransaction>() {

								@Override
								public void onFailure(Throwable caught) {
									Accounter
											.showError(messages.Unabletoopenrecurringtransactio()
													+ caught);
								}

								@Override
								public void onSuccess(
										ClientRecurringTransaction result) {
									view.openRecurringDialog(result);
								}
							});
				}

			}
		});
	}
}
