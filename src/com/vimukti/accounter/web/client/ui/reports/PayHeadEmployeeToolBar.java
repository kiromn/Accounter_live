package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.combo.EmployeeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class PayHeadEmployeeToolBar extends PayHeadReportToolBar {

	EmployeeCombo employeeCombo;

	public PayHeadEmployeeToolBar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		employeeCombo = new EmployeeCombo(messages.employee()) {
			ClientEmployee allEmployee = null;

			@Override
			public void initCombo(List<ClientEmployee> list) {
				if (allEmployee == null) {
					allEmployee = new ClientEmployee();
					allEmployee.setName(messages.all());
				}
				if (!list.contains(allEmployee)) {
					list.add(allEmployee);
				}
				super.initCombo(list);
			}
		};
		employeeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmployee>() {

					@Override
					public void selectedComboBoxItem(ClientEmployee selectItem) {
						setSelectedEmployee(selectItem);
					}
				});

		super.createControls();
	}

	protected void setSelectedEmployee(ClientEmployee selectItem) {
		changeDates(getStartDate(), getEndDate());
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(fromItem.getDate(), toItem.getDate());
	}

	protected com.vimukti.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return employeeCombo;
	}

	public ClientEmployee getEmployee() {
		return employeeCombo.getSelectedValue();
	}

	@Override
	protected void addFormItems() {
		addItems(employeeCombo, payheadCombo, dateRangeItemCombo, fromItem,
				toItem);
	}

	public void setEmployee(long employeeId) {
		employeeCombo.setSelectedEmployee(employeeId);
	}
}
