package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UsersActivityListView extends BaseView {

	private Label titleItem;
	private DateField fromdate, toDate;
	private VerticalPanel mainPanel;
	private DynamicForm dateForm, buttonForm;
	private UsersActivityList activityList;
	private Button updateButton, customizeButton;
	private long value;
	private ActivityCustomizationDialog customizationDialog;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		customizationDialog = new ActivityCustomizationDialog(
				messages.customize());
		titleItem = new Label(messages.usersActivityLogTitle());
		titleItem.setStyleName(messages.labelTitle());

		fromdate = new DateField(messages.fromDate());
		toDate = new DateField(messages.endDate());
		fromdate.setEnteredDate(new ClientFinanceDate());
		toDate.setEnteredDate(new ClientFinanceDate());

		customizeButton = new Button(messages.customize());
		customizeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customiseActivityLog();
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshActivityList();
			}
		});

		buttonForm = new DynamicForm();
		buttonForm.setNumCols(2);

		dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setFields(fromdate, toDate);

		activityList = new UsersActivityList(fromdate.getValue(),
				toDate.getValue(), value);
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(activityList);
		pager.setStyleName("pager-alignment");
		activityList.addColumnSortHandler(new Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				int columnIndex = activityList
						.getColumnIndex((Column<ClientActivity, ?>) column);
				boolean isAscending = event.isSortAscending();
				activityList.sortRowData(columnIndex, isAscending);

			}
		});

		HorizontalPanel panel = new HorizontalPanel();
		panel.add(dateForm);
		panel.add(customizeButton);
		panel.add(updateButton);
		panel.setCellHorizontalAlignment(customizeButton, ALIGN_RIGHT);
		panel.setCellHorizontalAlignment(updateButton, ALIGN_RIGHT);
		panel.setWidth("100%");
		panel.addStyleName("user_activity_dateform");

		mainPanel.add(titleItem);
		mainPanel.add(panel);
		mainPanel.add(activityList);
		activityList.setStyleName("user_activity_log");
		mainPanel.add(pager);
		add(mainPanel);
	}

	protected void customiseActivityLog() {
		customizationDialog.show();
		customizationDialog.showRelativeTo(customizeButton);
		customizationDialog.setCallback(new ActionCallback<Long>() {

			@Override
			public void actionResult(Long result) {
				value = result;
				refreshActivityByCustomiseValues(value);
				customizationDialog.setCustomiseValue(value);
			}
		});

	}

	private void refreshActivityByCustomiseValues(long value) {
		ClientFinanceDate startDate = fromdate.getEnteredDate();
		ClientFinanceDate endDate = toDate.getEnteredDate();
		activityList.setFromDate(startDate);
		activityList.setEndDate(endDate);
		activityList.setCustomiseValue(value);
		activityList.setVisibleRangeAndClearData(new Range(0, 50), true);
	}

	private void refreshActivityList() {
		ClientFinanceDate startDate = fromdate.getEnteredDate();
		ClientFinanceDate endDate = toDate.getEnteredDate();
		activityList.setFromDate(startDate);
		activityList.setEndDate(endDate);
		activityList.setVisibleRangeAndClearData(new Range(0, 50), true);
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return "Users Activity";
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
