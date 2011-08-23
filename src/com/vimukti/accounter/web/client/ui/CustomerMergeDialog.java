package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomerMergeDialog extends BaseDialog<ClientCustomer> implements
		AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private CustomerCombo customerCombo;
	private CustomerCombo customerCombo1;
	private TextItem customerIDTextItem;
	private TextItem customerIDTextItem1;

	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;
	private CheckboxItem status;
	private CheckboxItem status1;

	private ClientCustomer clientCustomer;
	private ClientCustomer clientCustomer1;

	public CustomerMergeDialog(String title, String descript) {
		super(title, descript);
		setWidth("650px");
		okbtn.setText("Merge");
		createControls();
		center();
		clientCustomer1 = null;
		clientCustomer = null;
	}

	private void createControls() {
		form = new DynamicForm();
		form1 = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");
		form1.setHeight("100%");
		form1.setWidth("100%");
		VerticalPanel layout = new VerticalPanel();
		VerticalPanel layout1 = new VerticalPanel();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		customerCombo = createCustomerCombo();
		customerCombo1 = createCustomerCombo1();

		customerIDTextItem = new TextItem("CustomerID");
		customerIDTextItem.setHelpInformation(true);
		customerIDTextItem.setDisabled(true);

		customerIDTextItem1 = new TextItem("CustomerID");
		customerIDTextItem1.setHelpInformation(true);
		customerIDTextItem1.setDisabled(true);

		status = new CheckboxItem("Active");
		status.setValue(false);

		status.setHelpInformation(true);

		status1 = new CheckboxItem("Active");
		status1.setValue(false);

		balanceTextItem = new TextItem("Balance");
		balanceTextItem.setHelpInformation(true);
		balanceTextItem.setDisabled(true);

		balanceTextItem1 = new TextItem("Balance");
		balanceTextItem1.setHelpInformation(true);
		balanceTextItem1.setDisabled(true);

		form.setItems(customerCombo, customerIDTextItem, status,
				balanceTextItem);
		form1.setItems(customerCombo1, customerIDTextItem1, status1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);
	}

	private CustomerCombo createCustomerCombo1() {
		customerCombo1 = new CustomerCombo("Customer To", false);
		customerCombo1.setHelpInformation(true);
		customerCombo1.setRequired(true);
		customerCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						clientCustomer1 = selectItem;
						customerSelected1(selectItem);

					}

				});

		return customerCombo1;
	}

	private CustomerCombo createCustomerCombo() {
		customerCombo = new CustomerCombo("Customer From", false);
		customerCombo.setHelpInformation(true);
		customerCombo.setRequired(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						clientCustomer = selectItem;
						customerSelected(selectItem);

					}

				});

		return customerCombo;
	}

	private void customerSelected(ClientCustomer selectItem) {

		customerIDTextItem.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue(selectItem.isActive());

	}

	private void customerSelected1(ClientCustomer selectItem) {
		customerIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
		status1.setValue(selectItem.isActive());
	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (clientCustomer1.getID() == clientCustomer.getID()) {
			result.addError(clientCustomer,
					"Could not move customer because two customers are same.");
			return result;
		}
		result = form.validate();
		result = form1.validate();
		if (clientCustomer1.getID() == clientCustomer.getID()) {
			result.addError(clientCustomer, "Same");
		}
		return result;

	}

	@Override
	protected boolean onOK() {

		if (clientCustomer1.getID() == clientCustomer.getID()) {
			return false;
		}
		Accounter.createHomeService().mergeCustomer(clientCustomer,
				clientCustomer1, this);

		return true;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

}
