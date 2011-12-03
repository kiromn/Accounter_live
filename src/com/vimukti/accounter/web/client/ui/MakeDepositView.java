package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.CashBackAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.MakeDepositAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.MakeDepositTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class MakeDepositView extends
		AbstractBankTransactionView<ClientMakeDeposit> {
	DateItem date;

	DynamicForm depoForm;
	TextAreaItem memoText;

	AmountField cashBackAmountText;
	TAXCodeCombo taxCodeSelect;
	TextItem cashBackMemoText;
	DynamicForm memoForm, totForm;
	DynamicForm form1, form2;
	private AmountField amtText;
	private MakeDepositTransactionTable gridView;

	protected ClientTransactionMakeDeposit currentRecord;
	protected boolean isClose;

	private int TYPE_FINANCIAL_ACCOUNT = 1;
	private int TYPE_VENDOR = 2;
	private int TYPE_CUSTOMER = 3;

	private MakeDepositAccountCombo depositInSelect, depositFromSelect;
	private CashBackAccountsCombo cashBackAccountSelect;
	private OtherAccountsCombo financeAccountSelect;
	private CustomerCombo customerSelect;
	private CustomCombo<ClientVendor> vendorSelect;

	private ClientAccount selectedDepositInAccount, selectedDepositFromAccount;
	private ClientAccount selectedCashBackAccount;

	private String selectedItemId;

	// private ClientAccount selectedAccount;

	protected ClientCustomer customer;

	protected ClientVendor vendor;

	protected Long nextTransactionNumber;

	protected Long TransactionNumber;

	private Double calculatedTotal;

	private boolean isListEmpty;

	protected boolean isSelected;
	// private MakeDeposit transactionObject;

	private List<ClientAccount> listOfAccounts;

	private HorizontalPanel bot1Panel;

	private ArrayList<DynamicForm> listforms;

	private AddButton addButton;

	// private VerticalPanel botRightPanel;

	private boolean locationTrackingEnabled;

	private String transactionNo;

	public MakeDepositView() {
		super(ClientTransaction.TYPE_MAKE_DEPOSIT);
		calculatedTotal = 0D;
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	private void setTransactionNumberToMakeDepositObject() {
		AccounterAsyncCallback<String> getTransactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {

			}

			public void onResultSuccess(String result) {

				if (result != null)
					setTransactionNumber(result);
				else
					onFailure(new Exception());

			}

		};

		Accounter.createHomeService().getNextTransactionNumber(
				ClientTransaction.TYPE_MAKE_DEPOSIT,
				getTransactionNumberCallback);
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNo = transactionNumber;
	}

	private void addTransactionMakeDepositsToGrid(
			List<ClientTransactionMakeDeposit> transactionMakeDepositList) {

		ClientCompany company = getCompany();

		ClientTransactionMakeDeposit records[] = new ClientTransactionMakeDeposit[transactionMakeDepositList
				.size()];

		Iterator<ClientTransactionMakeDeposit> it = transactionMakeDepositList
				.iterator();
		int i = 0;

		while (it.hasNext()) {
			ClientTransactionMakeDeposit entry = it.next();
			// For avoiding the deletion of transaction items before clicking on
			// the edit button
			entry.setIsNewEntry(false);
			records[i] = new ClientTransactionMakeDeposit();
			// records[i].setAttribute(ATTR_CHECK, true);
			records[i].setDate(entry.getDate());
			if (entry.getNumber() != null)
				records[i].setNumber(entry.getNumber());
			if (entry.getPaymentMethod() != null)
				records[i].setPaymentMethod(entry.getPaymentMethod());

			if (entry.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT) {
				records[i].setType(TYPE_FINANCIAL_ACCOUNT);
				records[i].setAccount(entry.getAccount());
			} else if (entry.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR) {
				if (entry.getVendor() != 0) {
					records[i].setType(TYPE_VENDOR);
					records[i].setVendor(entry.getVendor());
				}
			} else {
				if (entry.getCustomer() != 0) {
					records[i].setType(TYPE_CUSTOMER);

					records[i].setCustomer(entry.getCustomer());
				}

			}

			if (entry.getReference() != null)
				records[i].setReference(entry.getReference());

			records[i].setAmount(entry.getAmount());
			// records[i++].setEnabled(false);

		}
	}

	protected void validateCashBackAmount() {

		String amount = cashBackAmountText.getAmount().toString();
		if (amount.substring(0, 1)
				.equals("" + UIUtils.getCurrencySymbol() + "")) {
			amount = amount.substring(1);
		}
		try {
			Double cashBackAmount = Double.parseDouble(amount);
			if (DecimalUtil.isEquals(cashBackAmount, 0.00)) {

				if (DecimalUtil.isLessThan(cashBackAmount, 0.00)
						|| DecimalUtil.isGreaterThan(cashBackAmount,
								calculatedTotal)) {
					Accounter.showError(messages.cashBackAmountErrorMsg());
					cashBackAmount = 0.00;
					// cashBackAmountText.setValue("$0.00");
					cashBackAmountText.setAmount(0.00);
				} /*
				 * else if (cashBackAmount > 1000000000000.00) { SC.say(
				 * "Cash-back Amount should not exceed +UIUtils.getCurrencySymbol() +"
				 * 1,000,000,000,000.00" ); cashBackAmount = 0.00;
				 * cashBackAmountText.setValue(""+UIUtils.getCurrencySymbol()
				 * +"0.00"); }
				 */

			}

			Double diff = calculatedTotal.doubleValue()
					- cashBackAmount.doubleValue();
			// cashBackAmountText.setValue(UIUtils.format(cashBackAmount));
			cashBackAmountText.setAmount(cashBackAmount);
			// totText.setValue(UIUtils.format(diff));
			// totText.setAmount(getAmountInTransactionCurrency(diff));
		} catch (Exception e) {
			Accounter.showError(messages.enterValidAmount());
			// cashBackAmountText.setValue("$0.00");
			cashBackAmountText.setAmount(0.00);

		}

	}

	private void addTracsactionMakeDepositsToGrid() {

		gridView.addLoadingImagePanel();

		AccounterAsyncCallback<List<ClientTransactionMakeDeposit>> callback = new AccounterAsyncCallback<List<ClientTransactionMakeDeposit>>() {

			public void onException(AccounterException caught) {
				Accounter.showError(messages.makeDepostTransationsListFailed());
				gridView.removeAllRecords();
			}

			public void onResultSuccess(
					List<ClientTransactionMakeDeposit> result) {
				if (result == null) {
					onFailure(null);
					return;
				}

				if (result.size() != 0) {
					gridView.removeAllRecords();
					gridView.setRecords(result);

				} else if (!isInViewMode()) {
					gridView.removeAllRecords();
					Accounter.showError(messages.noDepositsToShow());
					// gridView.addEmptyMessage("No records to show");
				}

			}

		};

		Accounter.createHomeService().getTransactionMakeDeposits(callback);

	}

	// public void initVendorCombo() {
	// List<ClientVendor> result = getCompany().getActiveVendors();
	// if (result != null) {
	// allVendors = result;
	// vendorSelect.initCombo(result);
	//
	// }
	//
	// }
	//
	// private void initCustomerCombo() {
	// List<ClientCustomer> result = getCompany().getActiveCustomers();
	// if (result != null) {
	// allCustomers = result;
	// customerSelect.initCombo(result);
	// }
	// }

	public void getDepositInAccounts() {
		listOfAccounts = depositInSelect.getAccounts();
		depositInSelect.initCombo(listOfAccounts);
		depositFromSelect.initCombo(listOfAccounts);
	}

	// protected boolean validateForm() {
	// boolean flag = true;
	// // if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) !=
	// // 0.00
	// // && selectedCashBackAccount == null) {
	// if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00)
	// && selectedCashBackAccount == null) {
	//
	// flag = false;
	// Accounter.showError(messages
	// .cashBackAccountShouldBeSelected());
	//
	// }
	//
	// return checkTotalAmount() && checkLastRecord() && flag
	// && depoForm.validate(false);
	// }

	private boolean checkTotalAmount() {

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) ==
		// 0.00)
		if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00))
			return true;

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) >
		// calculatedTotal) {
		if (DecimalUtil.isGreaterThan(cashBackAmountText.getAmount(),
				calculatedTotal)) {
			Accounter.showError(messages
					.cashBackAmountShouldnotBeGreaterthanDepositedAmount());
			return false;
		}
		return true;
	}

	private boolean checkLastRecord() {

		Object records[] = gridView.getRecords().toArray();
		ClientTransactionMakeDeposit rec = (ClientTransactionMakeDeposit) records[records.length - 1];
		// FIXME-- check the condition,there is no possiblity of type/account to
		// be '0'
		if (rec.getType() == 0 || (rec.getAccount() == 0)) {
			Accounter.showError(messages.pleaseChooseAnAccount());
			return false;
		}
		return true;
	}

	public void initListGrid() {
		gridView = new MakeDepositTransactionTable(this) {

			@Override
			protected void updateNonEditableItems() {
				MakeDepositView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return MakeDepositView.this.isInViewMode();
			}
		};
		// gridView.setHeight("250px");
		gridView.setDisabled(isInViewMode());
		gridView.getElement().getStyle().setMarginTop(10, Unit.PX);
	}

	protected void validateAmountField(
			ClientTransactionMakeDeposit selectedRecord) {

		String amount = selectedRecord.getAmount() + "";
		// if (amount.substring(0, 1).equals("$")) {
		// amount = amount.substring(1);
		// }
		try {

			Double enteredAmount = DataUtils.getAmountStringAsDouble(amount);

			if (DecimalUtil.isLessThan(enteredAmount, -1000000000000.00)
					|| DecimalUtil.isGreaterThan(enteredAmount,
							1000000000000.00)) {
				Accounter.showError(messages.amountExceedsTheLimit());
				enteredAmount = 0.00;
			} else
				selectedRecord.setAmount(enteredAmount);
		} catch (Exception e) {
			Accounter.showError(messages.invalidAmount());
			selectedRecord.setAmount(0.00);
		}

	}

	protected void updateTotalAmount() {

		calculatedTotal = 0D;
		for (ClientTransactionMakeDeposit rec : gridView.getRecords()) {

			ClientTransactionMakeDeposit record = (ClientTransactionMakeDeposit) rec;
			// FIXME--need to implement
			// if (record.getAttributeAsBoolean(ATTR_CHECK)) {
			// calculatedTotal += UIUtils.unFormat(
			// record.getAttribute(ATTR_AMOUNT)).doubleValue();
			// }

		}
		// totAmtText.setValue(UIUtils.format(calculatedTotal));
		// totText.setValue(UIUtils.format((calculatedTotal - (UIUtils
		// .unFormat(UIUtils.toStr(cashBackAmountText.getValue()))))));
		// totText.setAmount(getAmountInTransactionCurrency(calculatedTotal
		// - getAmountInBaseCurrency(cashBackAmountText.getAmount())));

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	private List<ClientTransactionMakeDeposit> getTransactionMakeDepositsList() {
		List<ClientTransactionMakeDeposit> transactionMakeDepositsList = new ArrayList<ClientTransactionMakeDeposit>();
		ClientTransactionMakeDeposit entry;
		// FIXME--need to check
		// for (IsSerializable rec : gridView.getRecords()) {
		// if (record.getAttributeAsBoolean(ATTR_CHECK)) {
		// entry = new ClientTransactionMakeDeposit();
		//
		// // setting date
		// entry.setDate(record.getAttributeAsDate(ATTR_DATE).getTime());
		//
		// // Setting cash account,number and payment method for old
		// // entries
		// if (record.getAttribute("cashAccountId") != null) {
		//
		// // setting isNewEntry for old entries .
		// entry.setIsNewEntry(false);
		//
		// entry.setCashAccount(FinanceApplication.getCompany()
		// .getAccount(
		// UIUtils.toLong(record
		// .getAttribute("cashAccountId")))
		// .getID());
		//
		// // setting DepositedTransactionId
		// entry.setDepositedTransaction(Long.parseLong(record
		// .getAttribute("transactionId")));
		// try {
		// Long number = UIUtils.toLong(record
		// .getAttribute(ATTR_NO));
		// entry.setNumber(number);
		// } catch (Exception e) {
		// }
		//
		// if (record.getAttributeAsString(ATTR_PAYMENT_METHOD) != null) {
		// entry.setPaymentMethod(record
		// .getAttributeAsString(ATTR_PAYMENT_METHOD));
		//
		// }
		//
		// else {
		// // setting isNewEntry for new entries .
		// entry.setIsNewEntry(true);
		// }
		//
		// // Setting Type and account or vendor or customer
		// String selectedType = record
		// .getAttributeAsString(ATTR_TYPE);
		// String account = record.getAttributeAsString(ATTR_ACCOUNT);
		// if (selectedType.equalsIgnoreCase("Financial Account")) {
		//
		// entry
		// .setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
		//
		// for (ClientAccount temp : allAccounts) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setAccount(temp.getID());
		// break;
		// }
		// }
		//
		// } else if (selectedType.equalsIgnoreCase("Vendor")) {
		// entry.setType(ClientTransactionMakeDeposit.TYPE_VENDOR);
		//
		// for (ClientVendor temp : allVendors) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setVendor(temp.getID());
		// break;
		// }
		// }
		//
		// } else {
		// entry
		// .setType(ClientTransactionMakeDeposit.TYPE_CUSTOMER);
		// for (ClientCustomer temp : allCustomers) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setCustomer(temp.getID());
		// break;
		// }
		// }
		//
		// }
		//
		// // Setting Reference
		// String ref = record.getAttributeAsString(ATTR_REFERENCE);
		// if (ref != null)
		// entry.setReference(ref);
		//
		// // Setting amount
		// // String amount = record.getAttribute(ATTR_AMOUNT);
		// entry.setTotal(UIUtils.unFormat(record
		// .getAttribute(ATTR_AMOUNT)));
		//
		// transactionMakeDepositsList.add(entry);
		//
		// }
		//
		// }
		//
		// }
		return transactionMakeDepositsList;
	}

	private List<ClientTransactionMakeDeposit> getAllSelectedRecords(
			ClientMakeDeposit makeDeposit) {
		List<ClientTransactionMakeDeposit> selectedRecords = gridView
				.getRecords();

		for (ClientTransactionMakeDeposit rec : selectedRecords) {
			rec.setID(0);
			rec.setMakeDeposit(makeDeposit);
		}

		return selectedRecords;
	}

	protected void resetForms() {

		// depoForm.resetValues();
		// memoForm.resetValues();
		gridView.removeAllRecords();
		addTracsactionMakeDepositsToGrid();
		// totForm.resetValues();
		// form1.resetValues();
		// form2.resetValues();

	}

	public String getSelectedItemId() {
		return selectedItemId;
	}

	public void setSelectedItemId(String selectedItemId) {
		this.selectedItemId = selectedItemId;
	}

	@Override
	public void init() {
		super.init();
		TYPE_FINANCIAL_ACCOUNT = ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT;
		TYPE_VENDOR = ClientTransactionMakeDeposit.TYPE_VENDOR;
		TYPE_CUSTOMER = ClientTransactionMakeDeposit.TYPE_CUSTOMER;

		// createControls();
		// setSize("100%", "100%");
		// setOverflow(Overflow.AUTO);
	}

	@Override
	public void initData() {
		super.initData();
		getDepositInAccounts();

		if (isInViewMode()) {
			depositInSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn()));
			this.selectedDepositInAccount = getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn());
			depositFromSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositFrom()));
			this.selectedDepositFromAccount = getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositFrom());
		}

		initFianancialAccounts();
		initCashBackAccounts();

		setTransactionNumberToMakeDepositObject();
		// addTracsactionMakeDepositsToGrid();
		// initVendorCombo();
		// initCustomerCombo();

	}

	private void initCashBackAccounts() {
		accountsList = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {

			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
				accountsList.add(account);
			}

		}
		cashBackAccountSelect.initCombo(accountsList);

	}

	@Override
	protected void initMemoAndReference() {

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientMakeDeposit());
		} else {
			selectedDepositFromAccount = getCompany().getAccount(
					transaction.getDepositFrom());
			selectedDepositInAccount = getCompany().getAccount(
					transaction.getDepositIn());
			if (currencyWidget != null) {
				setCurrency(transaction.getCurrency() != 0 ? getCurrency(transaction
						.getCurrency()) : getCompany().getPrimaryCurrency());
				checkForCurrencyType();
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}

			date.setValue(transaction.getDate());
			memoText.setValue(transaction.getMemo());
			transactionNumber.setValue(transaction.getNumber());
			this.transactionItems = transaction.getTransactionItems();
			cashBackAmountText.setValue(DataUtils.getAmountAsString(transaction
					.getCashBackAmount()));
			cashBackMemoText.setValue(transaction.getCashBackMemo());
			cashBackAccountSelect.setValue(transaction.getCashBackAccount());
			// totText
			// .setValue(DataUtils
			// .getAmountAsString(getAmountInTransactionCurrency(transaction
			// .getTotal())));
			if (transaction.getCurrency() == selectedDepositFromAccount
					.getCurrency()) {
				amtText.setAmount(transaction.getTotal());
			} else {
				amtText.setAmount(transaction.getTotal());
			}
			amtText.setCurrency(getCurrency(selectedDepositFromAccount
					.getCurrency()));
			gridView.setRecords(transaction.getTransactionMakeDeposit());
			initAccounterClass();
			// gridView.setCanEdit(false);
			updateTotals();
		}
		initTransactionNumber();
		// FIXME--need to implement this feature
		// gridView.setEnableMenu(false);

		// gridView.canDelete(true);
		// FIMXE--need to add this type
		// gridView.setEditEvent();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		// if (isMultiCurrencyEnabled()) {
		// modifyForeignCurrencyTotalWidget();
		// foreignCurrencyamountLabel
		// .setAmount(getAmountInTransactionCurrency(transaction
		// .getTotal()));
		// }
		super.initTransactionViewData();

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label lab = new Label(messages.makeDeposit());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName("lable-title");
		lab.setStyleName(messages.labelTitle());
		// lab.setHeight("50px");
		date = UIUtils.date(messages.date(), this);

		// set the transactionDate while creation
		setTransactionDate(date.getValue());
		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (date != null) {
					setTransactionDate(date);
				}
			}
		});
		locationCombo = createLocationCombo();
		// date.setWidth(100);
		transactionNumber = createTransactionNumberItem();
		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(date, transactionNumber);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);
		currencyWidget = createCurrencyFactorWidget();

		depositInSelect = new MakeDepositAccountCombo(messages.depositIn());
		depositInSelect.setHelpInformation(true);
		depositInSelect.setRequired(true);
		// depositInSelect.setWidth(100);
		depositInSelect.setPopupWidth("450px");
		depositInSelect.setDisabled(isInViewMode());
		depositInSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositInAccount = selectItem;
						checkForCurrencyType();

					}

				});

		depositFromSelect = new MakeDepositAccountCombo(messages.depositFrom());
		depositFromSelect.setHelpInformation(true);
		depositFromSelect.setRequired(true);
		depositFromSelect.setWidth(100);
		depositFromSelect.setPopupWidth("450px");
		depositFromSelect.setDisabled(isInViewMode());
		depositFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositFromAccount = selectItem;
						checkForCurrencyType();
					}

				});

		vendorSelect = new VendorCombo("");

		financeAccountSelect = new OtherAccountsCombo("");

		customerSelect = new CustomerCombo("");

		vendorSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;

					}

				});
		financeAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
					}
				});

		customerSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					public void selectedComboBoxItem(ClientCustomer selectItem) {
						// selectedCustomer = selectItem;

					}
				});
		amtText = new AmountField(messages.amount(), this, getBaseCurrency());
		amtText.setDisabled(isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotals();
			}
		});
		memoText = new TextAreaItem(messages.memo());
		memoText.setMemo(true, this);
		memoText.setHelpInformation(true);
		memoText.setWidth(100);

		memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoText);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		depoForm = new DynamicForm();
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(messages.deposit());
		depoForm.setFields(depositFromSelect, depositInSelect, amtText);
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			depoForm.setFields(classListCombo);
		}

		// Label lab1 = new Label(FinanceApplication.constants()
		// .paymentsReceived());

		addButton = new AddButton(this);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientTransactionMakeDeposit deposit = new ClientTransactionMakeDeposit();
				deposit.setIsNewEntry(true);
				deposit.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
				// deposit.set
				gridView.add(deposit);
			}
		});

		cashBackAccountSelect = new CashBackAccountsCombo(
				messages.cashBackAccount());
		cashBackAccountSelect.setHelpInformation(true);
		cashBackAccountSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.CASH_BACK_ACCOUNTS_COMBO));
		// cashBackAccountSelect.setRequired(true);
		cashBackAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedCashBackAccount = selectItem;

					}

				});

		cashBackMemoText = new TextItem(messages.cashBackMemo());
		cashBackMemoText.setHelpInformation(true);
		cashBackMemoText.setWidth(100);

		form1 = new DynamicForm();
		form1.setFields(cashBackMemoText, cashBackAccountSelect);
		// form1.getCellFormatter().setWidth(0, 0, "180px");
		form1.setWidth("70%");

		cashBackAmountText = new AmountField(messages.cashBackAmount(), this,
				getBaseCurrency());
		cashBackAmountText.setHelpInformation(true);
		cashBackAmountText.setWidth(100);
		cashBackAmountText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		cashBackAmountText.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null)
					validateCashBackAmount();
			}
		});

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());
		foreignCurrencyamountLabel = createTransactionTotalNonEditableLabel(getBaseCurrency());

		// transactionTotalBaseCurrencyText.setWidth("100px");
		transactionTotalBaseCurrencyText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + "0.00");
		transactionTotalBaseCurrencyText.setDisabled(true);
		((Label) transactionTotalBaseCurrencyText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		form2 = new DynamicForm();

		form2.setFields(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			form2.setFields(foreignCurrencyamountLabel);
		}

		form2.addStyleName("textbold");
		// form2.setWidth("50%");
		form2.getElement().getStyle().setMarginTop(10, Unit.PX);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.addStyleName("fields-panel");
		topHLay.add(depoForm);

		DynamicForm dynamicForm = new DynamicForm();

		locationCombo.addStyleName("locationCombo");

		if (locationTrackingEnabled) {
			dynamicForm.setFields(locationCombo);
		}

		VerticalPanel currencyPanel = new VerticalPanel();

		currencyPanel.add(dynamicForm);
		currencyPanel.add(currencyWidget);
		topHLay.add(currencyPanel);
		topHLay.setCellHorizontalAlignment(currencyPanel,
				HasAlignment.ALIGN_RIGHT);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_LEFT);
		// panel.add(addButton);
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		addButton.setEnabled(!isInViewMode());

		HorizontalPanel botHLay = new HorizontalPanel();
		botHLay.setWidth("100%");
		botHLay.add(memoForm);
		botHLay.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		botHLay.add(form2);
		botHLay.setCellHorizontalAlignment(form2, ALIGN_RIGHT);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_LEFT);
		vPanel.setWidth("100%");
		vPanel.add(panel);
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.add(botHLay);

		if (isInViewMode()) {
			date.setValue(transaction.getDate());
			depositInSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn()));
			memoText.setDisabled(true);
			if (((ClientMakeDeposit) transaction).getMemo() != null)
				memoText.setValue(((ClientMakeDeposit) transaction).getMemo());
			cashBackAccountSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getCashBackAccount()));
			if (((ClientMakeDeposit) transaction).getCashBackMemo() != null)
				cashBackMemoText.setValue(((ClientMakeDeposit) transaction)
						.getCashBackMemo());
			// totAmtText
			// .setValue(UIUtils.format(((MakeDeposit) transactionObject)
			// .getTotalAmount()));
			// totAmtText.setAmount(((ClientMakeDeposit) transactionObject)
			// .getTotal());
			// totText.setValue(UIUtils.format(((MakeDeposit) transactionObject)
			// .getTotal()));
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			// cashBackAmountText.setValue(UIUtils
			// .format(((MakeDeposit) transactionObject)
			// .getCashBackAmount()));
			cashBackAmountText.setAmount(transaction.getCashBackAmount());
			addTransactionMakeDepositsToGrid(transaction
					.getTransactionMakeDeposit());

			date.setDisabled(true);
			depositInSelect.setDisabled(true);
			cashBackAccountSelect.setDisabled(true);
			cashBackAmountText.setDisabled(true);
			cashBackMemoText.setDisabled(true);

		}

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		// mainVLay.add(lab1);
		// mainVLay.add(addButton);

		// mainVLay.add(gridView);

		mainVLay.add(vPanel);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// }

		this.add(mainVLay);

		// setSize("700", "600");

		/* Adding dynamic forms in list */
		listforms.add(depoForm);
		listforms.add(memoForm);
		// listforms.add(totForm);
		// listforms.add(form1);
		listforms.add(form2);
		settabIndexes();

		if (isMultiCurrencyEnabled()) {
			modifyForeignCurrencyTotalWidget();
		}

	}

	protected void updateTotals() {
		Double amount = amtText.getAmount();
		transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(amount));
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.setAmount(amount);
		}
	}

	private void initFianancialAccounts() {
		financeAccountSelect.initCombo(getCompany().getActiveAccounts());
	}

	@Override
	public void setData(ClientMakeDeposit data) {

		super.setData(data);
		if (isInViewMode() && (!transaction.isMakeDeposit()))
			try {
				throw new Exception(messages.unabletoLoadTheRequiredDeposit());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public void updateNonEditableItems() {
		if (gridView == null)
			return;
		// totText.setAmount(getAmountInTransactionCurrency(gridView.getTotal()
		// - getAmountInBaseCurrency(cashBackAmountText.getAmount())));
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// Validations
		// 1. if(isBlackTransaction(gridView))
		// ERROR
		// else
		// gridView validation
		// 2. if(!isValidTransactionDate(transactionDate)) ERROR
		// 3. if(isInPreventPostingBeforeDate(transactionDate)) ERROR
		// 4. depoForm validation
		// 5. if(!validMakeDepositCombo(selectedDepositInAccount, gridAcconts))
		// ERROR

		result.add(depoForm.validate());

		// if ((gridView == null)
		// || (gridView != null && gridView.getRecords().isEmpty())) {
		// result.addError(gridView, messages.blankTransaction());
		// } else {
		// ClientAccount selectedValue = depositInSelect.getSelectedValue();
		// long depositIn = selectedValue != null ? selectedValue.getID() : 0;
		// if(selectedValue1==null )
		// result.addError(selectedValue, "please select deposit in account");

		// result.add(gridView.validateGrid(depositIn));
		// }

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDateItem,
		// messages.invalidateTransactionDate());
		// }
		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		// result.add(depoForm.validate());

		// if
		// (AccounterValidator.isNegativeAmount(cashBackAmountText.getAmount()))
		// {
		// result.addError(cashBackAmountText,
		// messages.invalidNegativeAmount());
		// } else if (!AccounterValidator.isValidMakeDeposit_CashBackAmount(
		// cashBackAmountText.getAmount().doubleValue(),
		// totText.getAmount())) {
		// result.addError(cashBackAmountText, messages
		// .makeDepositCashBackAmount());
		// }

		// if (!AccounterValidator.validate_MakeDeposit_accountcombo(
		// selectedDepositInAccount, gridView)) {
		// result.addError(gridView, messages
		// .makedepositAccountValidation());
		// }
		return result;

	}

	public static MakeDepositView getInstance() {
		return new MakeDepositView();
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.depositInSelect.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	public void onEdit() {
		// if (transactionObject.canEdit) {
		// Accounter.showWarning(AccounterWarningType.MAKEDEPOSIT_EDITING,
		// AccounterType.WARNING, new ErrorDialogHandler() {
		//
		// @Override
		// public boolean onYesClick()
		// throws InvalidEntryException {
		// voidTransaction();
		// return true;
		// }
		//
		// private void voidTransaction() {
		// AccounterAsyncCallback<Boolean> callback = new
		// AccounterAsyncCallback<Boolean>() {
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter
		// .showError("Failed to void Make Deposit");
		//
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// if (result) {
		// enableFormItems();
		// } else
		//
		// onFailure(new Exception());
		// }
		//
		// };
		// makeDepositEdited = (ClientMakeDeposit) transactionObject;
		// if (makeDepositEdited != null) {
		// AccounterCoreType type = UIUtils
		// .getAccounterCoreType(makeDepositEdited
		// .getType());
		// rpcDoSerivce.voidTransaction(type,
		// makeDepositEdited.id, callback);
		// }
		//
		// }
		//
		// @Override
		// public boolean onNoClick() throws InvalidEntryException {
		//
		// return true;
		// }
		//
		// @Override
		// public boolean onCancelClick()
		// throws InvalidEntryException {
		//
		// return true;
		// }
		// });
		// }
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		depositInSelect.setDisabled(isInViewMode());
		depositFromSelect.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		addButton.setEnabled(!isInViewMode());
		gridView.setDisabled(isInViewMode());
		cashBackMemoText.setDisabled(isInViewMode());
		cashBackAmountText.setDisabled(isInViewMode());
		cashBackAccountSelect.setDisabled(isInViewMode());
		memoText.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());

		// For deleting the transctionItems after we edit
		for (ClientTransactionMakeDeposit ctmd : transaction
				.getTransactionMakeDeposit())
			ctmd.setIsNewEntry(true);
		// transactionObject = null;
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());

		if (isMultiCurrencyEnabled()) {
			currencyWidget.setDisabled(isInViewMode());
		}
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// NOTHING TO DO.
	}

	public void resetFormView() {

		// form1.getCellFormatter().setWidth(0, 1, "200px");
		// form1.getCellFormatter().setWidth(0, 1, "200px");
		// form2.getCellFormatter().setWidth(0, 1, "200px");
		// form2.getCellFormatter().setWidth(0, 1, "200px");
	}

	@Override
	protected String getViewTitle() {
		return messages.makeDeposit();
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting date
		if (date != null) {
			transaction.setDate(date.getValue().getDate());
		}
		// Setting Deposit in
		if (depositInSelect.getSelectedValue() != null)
			transaction.setDepositIn(selectedDepositInAccount.getID());

		if (depositFromSelect.getSelectedValue() != null)
			transaction.setDepositFrom(selectedDepositFromAccount.getID());
		// Setting Memo
		if (memoText.getValue() != null)
			transaction.setMemo(UIUtils.toStr(memoText.getValue()));

		if (transactionNumber.getValue() != null)
			transaction.setNumber(transactionNumber.getValue());

		transaction.setTotal(amtText.getAmount());

		// setting transaction make deposits list
		List<ClientTransactionMakeDeposit> listOfTrannsactionMakeDeposits = getAllSelectedRecords(transaction);
		isListEmpty = false;
		if (listOfTrannsactionMakeDeposits.size() == 0)
			isListEmpty = true;
		else
			transaction
					.setTransactionMakeDeposit(listOfTrannsactionMakeDeposits);

		// Setting Cash back account
		transaction
				.setCashBackAccount(selectedCashBackAccount != null ? selectedCashBackAccount
						.getID() : 0);
		if (cashBackMemoText.getValue() != null)
			transaction.setCashBackMemo(cashBackMemoText.getValue().toString());

		// Setting Cash back amount
		transaction.setCashBackAmount(cashBackAmountText.getAmount());
		// Setting Total amount

		// Setting Total
		transaction.setTotal(amtText.getAmount());

		// Setting Transaction type
		transaction.setType(ClientTransaction.TYPE_MAKE_DEPOSIT);

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		super.saveAndUpdateView();

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	private void settabIndexes() {
		depositInSelect.setTabIndex(1);
		date.setTabIndex(2);
		transactionNumber.setTabIndex(3);
		memoText.setTabIndex(4);
		addButton.setTabIndex(5);
		saveAndCloseButton.setTabIndex(6);
		saveAndNewButton.setTabIndex(7);
		cancelButton.setTabIndex(8);

	}

	private void checkForCurrencyType() {
		if (selectedDepositInAccount != null
				&& selectedDepositFromAccount != null) {

			long toCurrencyID = selectedDepositInAccount.getCurrency();
			long fromCurrencyID = selectedDepositFromAccount.getCurrency();

			ClientCurrency toCurrency = getCompany().getCurrency(toCurrencyID);
			ClientCurrency fromCurrency = getCompany().getCurrency(
					fromCurrencyID);

			if (selectedDepositInAccount == selectedDepositFromAccount) {
				Accounter.showError(messages
						.dipositAccountAndTransferAccountShouldBeDiff());
				depositInSelect.setComboItem(null);
			}
			if (toCurrency != getBaseCurrency()
					&& fromCurrency != getBaseCurrency()) {
				Accounter.showError(messages
						.oneOfTheAccountCurrencyShouldBePrimaryCurrency());
				depositInSelect.setComboItem(null);
			} else {
				if (toCurrencyID != fromCurrencyID) {
					if (toCurrencyID != getBaseCurrency().getID()) {
						currencyWidget.setSelectedCurrency(toCurrency);
						setCurrency(toCurrency);
					} else {
						currencyWidget.setSelectedCurrency(fromCurrency);
						setCurrency(fromCurrency);
					}
				} else {
					currencyWidget.setSelectedCurrency(toCurrency);
					setCurrency(toCurrency);
				}
				amtText.setCurrency(fromCurrency);
			}
			updateTotals();
		}
		modifyForeignCurrencyTotalWidget();
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		updateTotals();
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(currencyWidget.getSelectedCurrency()
							.getFormalName()));
		}
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
