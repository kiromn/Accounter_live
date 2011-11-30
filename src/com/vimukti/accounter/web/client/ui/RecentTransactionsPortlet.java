package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class RecentTransactionsPortlet extends Portlet {
	private RecentTransactionHistoryGrid grid;
	private SelectCombo limitCombo;
	private List<String> limitList;

	public RecentTransactionsPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.recentTransactions(), messages
				.goToRecentTransactions());
	}

	@Override
	public void createBody() {
		limitCombo = new SelectCombo(messages.view());
		limitList = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			limitList.add(messages.lastOf((i + 1) * 5));
		}
		limitCombo.initCombo(limitList);
		limitCombo.setSelectedItem(0);
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(limitCombo);
		dynamicForm.addStyleName("list_combo_form");
		this.body.add(dynamicForm);
		limitCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						updateData();
					}
				});
		updateData();
	}

	private void updateData() {
		Accounter.createHomeService().getRecentTransactions(
				getLimit(limitCombo.getSelectedIndex()),
				new AsyncCallback<List<ClientActivity>>() {

					@Override
					public void onSuccess(List<ClientActivity> result) {
						if (grid != null) {
							body.remove(grid);
						}
						grid = new RecentTransactionHistoryGrid();
						grid.init();
						if (result != null && !(result.isEmpty())) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
						}
						body.add(grid);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	private int getLimit(int selectedIndex) {
		return (selectedIndex + 1) * 5;
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}