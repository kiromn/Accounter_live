package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountMergeDialog extends BaseDialog implements
		AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private OtherAccountsCombo accountCombo;
	private OtherAccountsCombo accountCombo1;
	private TextItem accountNumberTextItem;
	private TextItem accountNumberTextItem1;
	private TextItem name;
	private TextItem name1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;
	private ClientAccount fromAccount;
	private ClientAccount toAccount;

	public AccountMergeDialog(String title, String descript) {
		super(title, descript);
		setWidth("650px");
		okbtn.setText(messages.merge());
		createControls();
		center();
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
		accountCombo = createAccountCombo();
		accountCombo1 = createAccountCombo1();

		accountNumberTextItem = new TextItem(messages.payeeNumber(
				messages.Account()));
		accountNumberTextItem.setHelpInformation(true);

		accountNumberTextItem1 = new TextItem(messages.payeeNumber(
				messages.Account()));
		accountNumberTextItem1.setHelpInformation(true);

		name = new TextItem(messages.accountName());
		name.setHelpInformation(true);
		name1 = new TextItem(messages.accountName());
		name1.setHelpInformation(true);

		balanceTextItem = new TextItem(messages.balance());
		balanceTextItem.setHelpInformation(true);

		balanceTextItem1 = new TextItem(messages.balance());
		balanceTextItem1.setHelpInformation(true);

		form.setItems(accountCombo, accountNumberTextItem, name,
				balanceTextItem);

		form1.setItems(accountCombo1, accountNumberTextItem1, name1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);

	}

	private OtherAccountsCombo createAccountCombo1() {
		accountCombo1 = new OtherAccountsCombo(messages.payeeTo(
				messages.Account()), false);
		accountCombo1.setHelpInformation(true);
		accountCombo1.setRequired(true);
		accountCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						toAccount = selectItem;
						customerSelected1(selectItem);

					}

				});

		return accountCombo1;
	}

	private OtherAccountsCombo createAccountCombo() {
		accountCombo = new OtherAccountsCombo(messages.payeeFrom(
				messages.Account()), false);
		accountCombo.setHelpInformation(true);
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						fromAccount = selectItem;
						customerSelected(selectItem);

					}

				});

		return accountCombo;
	}

	private void customerSelected(ClientAccount selectItem) {

		accountNumberTextItem.setValue(String.valueOf(selectItem.getNumber()));
		balanceTextItem.setValue(DataUtils.getAmountAsStringInPrimaryCurrency(selectItem
				.getOpeningBalance()));

		name.setValue(selectItem.getName());

	}

	private void customerSelected1(ClientAccount selectItem) {
		accountNumberTextItem1.setValue(String.valueOf(selectItem.getNumber()));
		balanceTextItem1.setValue(DataUtils.getAmountAsStringInPrimaryCurrency(selectItem
				.getOpeningBalance()));

		name1.setValue(selectItem.getName());

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (toAccount != null && fromAccount != null) {
			if ((toAccount.getID() == fromAccount.getID())
					|| !(toAccount.getType() == fromAccount.getType())) {
				result.addError(fromAccount, messages
						.notMoveAccount());
				return result;
			}

			if ((toAccount.getID() == fromAccount.getID())
					|| !(toAccount.getType() == fromAccount.getType())) {
				result.addError(fromAccount, messages
						.notMoveAccount());
				return result;
			}
			result = form.validate();

			result = form1.validate();

			return result;

		}
		return result;
	}

	@Override
	protected boolean onOK() {

		if (fromAccount != null && toAccount != null) {
			if (fromAccount.getID() == toAccount.getID()) {
				Accounter.showError("Accounts must be different");
				return false;
			}
		}
		ClientCurrency currency1 = getCompany().getCurrency(
				fromAccount.getCurrency());
		ClientCurrency currency2 = getCompany().getCurrency(
				toAccount.getCurrency());

		if (currency1 != currency2) {
			Accounter
					.showError("Currencies of the both Accounts must be same ");
		} else if (fromAccount.getType() != toAccount.getType()) {
			Accounter.showError("Type of the both Accounts must be same ");
		} else {
			Accounter.createHomeService().mergeAccount(fromAccount, toAccount,
					this);
			return true;
		}
		return false;

	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		accountCombo.setFocus();

	}

}
