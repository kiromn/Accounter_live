package com.vimukti.accounter.web.client.ui;

/*	 Modified by Rajesh.A, Murali.A
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.MeasurementCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesItemCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.FloatRangeValidator;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ItemView extends BaseView<ClientItem> {

	public static final int TYPE_SERVICE = 1;
	public static final int NON_INVENTORY_PART = 3;
	private int type;
	private TextItem nameText, skuText;
	private AmountField salesPriceText, stdCostText, purchasePriceTxt,
			openingBalTxt;
	private IntegerField vendItemNumText, weightText;
	// defaultSellPrice, defaultPurchasePrice;
	// , salesTaxRate, purcahseTaxRate;
	private TextAreaItem salesDescArea, purchaseDescArea;
	CheckboxItem /* isservice, */isellCheck, comCheck, activeCheck, ibuyCheck,
			itemTaxCheck;

	private ItemGroupCombo itemGroupCombo, commodityCode;
	private VendorCombo prefVendorCombo;
	private SalesItemCombo accountCombo;
	private PurchaseItemCombo expAccCombo;
	AccounterConstants accounterConstants = Accounter.constants();
	private TAXCodeCombo taxCode;
	List<ClientAccount> incomeAccounts;
	HashMap<String, ClientVendor> allvendors;
	HashMap<String, ClientAccount> allaccounts;
	HashMap<String, ClientItemGroup> allitemgroups;
	private DynamicForm itemForm;
	private DynamicForm stdCostForm;
	private DynamicForm itemInfoForm;
	private DynamicForm purchaseInfoForm;
	private DynamicForm salesInfoForm;
	private FloatRangeValidator floatRangeValidator;
	private IntegerRangeValidator integerRangeValidator;
	protected ClientAccount selectAccount, selectExpAccount,
			defaultIncomeAccount, defaultExpAccount;

	protected ClientItemGroup selectItemGroup;
	protected ClientVendor selectVendor;
	protected ClientTAXCode selectTaxCode;
	private ClientCompany company;
	private boolean isGeneratedFromCustomer;
	private ArrayList<DynamicForm> listforms;
	String name;
	private String itemName;

	private MeasurementCombo measurement;
	private WarehouseCombo wareHouse;

	public ItemView(int type, boolean isGeneratedFromCustomer) {

		super();
		this.type = type;
		this.company = getCompany();
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;

	}

	private void initTaxCodes() {
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			List<ClientTAXCode> result = getCompany().getActiveTaxCodes();
			if (result != null) {
				taxCode.initCombo(result);
				ClientTAXCode code = null;
				if (isInViewMode()) {
					code = getCompany().getTAXCode(data.getTaxCode());
				}
				if (code == null) {
					code = getCompany().getTAXCode(
							getPreferences().getDefaultTaxCode());
				}
				taxCode.setComboItem(code);
			}
		}

	}

	private ClientAccount getDefaultAccount(String defaultAccount) {
		List<ClientAccount> accountList = getCompany().getActiveAccounts();
		for (ClientAccount account : accountList) {
			if (account.getName().equalsIgnoreCase(defaultAccount)) {
				return account;
			}
		}
		return null;
	}

	private void createControls() {

		// setTitle(UIUtils.title("NEW ITEM"));
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .. .newServiceItem()));

		listforms = new ArrayList<DynamicForm>();

		Label lab1 = new Label(Accounter.constants().newProduct());
		lab1.setStyleName(Accounter.constants().labelTitle());

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lab1);

		// nameText = new TextItem(FinanceApplication.constants()
		// .itemName());
		nameText = new TextItem(this.type == TYPE_SERVICE ? Accounter
				.constants().serviceName() : Accounter.constants()
				.productName());
		nameText.setValue(itemName);
		nameText.setHelpInformation(true);
		nameText.setWidth(100);
		nameText.setRequired(true);
		nameText.setDisabled(isInViewMode());

		// isservice = new CheckboxItem(Accounter.constants().isService());
		// isservice.setValue(true);
		// isservice.setDisabled(true);
		// isservice.setDisabled(isInViewMode());

		floatRangeValidator = new FloatRangeValidator();
		floatRangeValidator.setMin(0);

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		skuText = new TextItem();// new FormFieldItem("UPC/SKU",
		skuText.setHelpInformation(true);
		skuText.setWidth(100);
		skuText.setTitle(Accounter.constants().upcsku());
		skuText.setDisabled(isInViewMode());

		weightText = new IntegerField(this, Accounter.constants().weight());
		weightText.setHelpInformation(true);
		weightText.setWidth(100);
		weightText.setDisabled(isInViewMode());
		weightText.setValidators(integerRangeValidator);
		commodityCode = new ItemGroupCombo(Accounter.constants()
				.commodityCode());
		commodityCode.setHelpInformation(true);
		itemForm = new DynamicForm();
		itemForm.setWidth("98%");
		itemForm.setStyleName("item-form-view");
		itemForm.setIsGroup(true);
		itemForm.setGroupTitle(Accounter.constants().item());
		if (isInViewMode()) {
			this.type = data.getType();
		}
		if (type == ClientItem.TYPE_SERVICE) {
			lab1.setText(Accounter.constants().newService());
			// if (getCompany().getAccountingType() == 1)
			itemForm.getCellFormatter().setWidth(0, 0, "42%");
			itemForm.setFields(nameText/* , isservice */);

			// itemForm.setFields(nameText, /* isservice, */skuText);
		} else {
			lab1.setText(Accounter.constants().newProduct());
			// if (getCompany().getAccountingType() == 1)
			itemForm.setFields(nameText, weightText);
			itemForm.getCellFormatter().setWidth(0, 0, "42%");
			// else
			// itemForm.setFields(nameText, skuText, weightText);
		}
		// itemForm.getCellFormatter().setWidth(0, 0, "185px");
		salesDescArea = new TextAreaItem();
		salesDescArea.setHelpInformation(true);
		salesDescArea.setWidth(100);
		salesDescArea.setTitle(Accounter.constants().salesDescription());

		salesDescArea.setDisabled(isInViewMode());

		salesDescArea.setToolTip(Accounter.messages().writeCommentsForThis(
				this.getAction().getViewName()).replace(
				Accounter.constants().comments(),
				Accounter.constants().salesDescription()));
		salesPriceText = new AmountField(Accounter.constants().salesPrice(),
				this);
		salesPriceText.setHelpInformation(true);
		salesPriceText.setWidth(100);
		salesPriceText.setDisabled(isInViewMode());
		// FIXME--needto implement this feature
		// salesPriceText.setValidators(floatRangeValidator);
		// salesPriceText.setValidateOnChange(true);

		accountCombo = new SalesItemCombo(Accounter.messages().incomeAccount(
				Global.get().Account()));
		accountCombo.setHelpInformation(true);
		accountCombo.setDisabled(isInViewMode());
		// accountCombo.setWidth(100);
		accountCombo.setPopupWidth("500px");
		accountCombo.setRequired(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectAccount = selectItem;
						if (selectAccount != null
								&& selectAccount != defaultIncomeAccount
								&& defaultIncomeAccount != null) {
							if (type == ClientItem.TYPE_SERVICE)
								AccounterValidator
										.defaultIncomeAccountServiceItem(
												selectAccount,
												defaultIncomeAccount);
							if (type == ClientItem.TYPE_NON_INVENTORY_PART)
								AccounterValidator
										.defaultIncomeAccountNonInventory(
												selectAccount,
												defaultIncomeAccount);
						}
					}
				});
		itemTaxCheck = new CheckboxItem(Accounter.constants().taxable());
		itemTaxCheck.setValue(true);
		itemTaxCheck.setDisabled(true);

		comCheck = new CheckboxItem(Accounter.constants().commissionItem());

		salesInfoForm = UIUtils.form(Accounter.constants().salesInformation());
		salesInfoForm.setWidth("98%");

		stdCostText = new AmountField(Accounter.constants().standardCost(),
				this);
		stdCostText.setHelpInformation(true);
		stdCostText.setWidth(100);
		stdCostText.setDisabled(isInViewMode());

		// FIXME--needto implement this feature
		// stdCostText.setValidators(floatRangeValidator);
		// stdCostText.setValidateOnChange(true);

		// stdCostForm = UIUtils.form(FinanceApplication.constants()
		// .standardcost());
		// stdCostForm.setFields(stdCostText);
		// stdCostForm.setWidth("95%");
		// stdCostForm.getCellFormatter().setWidth(0, 0, "165");
		// itemGroupCombo = new ItemGroupCombo(FinanceApplication
		// .constants().itemGroup());
		itemGroupCombo = new ItemGroupCombo(Accounter.constants().itemGroup());
		itemGroupCombo.setHelpInformation(true);
		itemGroupCombo.setDisabled(isInViewMode());
		// itemGroupCombo.setWidth(100);
		itemGroupCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
					public void selectedComboBoxItem(ClientItemGroup selectItem) {
						selectItemGroup = selectItem;
					}
				});
		taxCode = new TAXCodeCombo(Accounter.constants().vatCode(),
				isGeneratedFromCustomer);
		taxCode.setHelpInformation(true);
		taxCode.setRequired(true);
		taxCode.setDisabled(isInViewMode());
		// if (!FinanceApplication.getCompany().getpreferences()
		// .getDoYouPaySalesTax()) {
		// vatCode.setDisabled(true);
		// }

		taxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectTaxCode = selectItem;
					}
				});
		taxCode.setDefaultValue(Accounter.constants().ztozeroperc());
		activeCheck = new CheckboxItem(Accounter.constants().active());
		activeCheck.setValue(true);
		activeCheck.setDisabled(isInViewMode());
		purchaseDescArea = new TextAreaItem();
		purchaseDescArea.setHelpInformation(true);
		purchaseDescArea.setWidth(100);
		purchaseDescArea.setTitle(Accounter.constants().purchaseDescription());
		purchaseDescArea.setDisabled(isInViewMode());

		purchasePriceTxt = new AmountField(Accounter.constants()
				.purchasePrice(), this);
		purchasePriceTxt.setHelpInformation(true);
		purchasePriceTxt.setWidth(100);
		purchasePriceTxt.setDisabled(isInViewMode());
		// FIXME--needto implement this feature
		// purchasePriceTxt.setValidators(floatRangeValidator);
		// purchasePriceTxt.setValidateOnChange(true);

		expAccCombo = new PurchaseItemCombo(Accounter.messages()
				.expenseAccount(Global.get().Account()));
		expAccCombo.setHelpInformation(true);
		expAccCombo.setRequired(true);
		expAccCombo.setDisabled(isInViewMode());
		expAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectExpAccount = selectItem;
						if (selectExpAccount != null
								&& selectExpAccount != defaultExpAccount) {
							if (type == ClientItem.TYPE_SERVICE)
								AccounterValidator
										.defaultExpenseAccountServiceItem(
												selectExpAccount,
												defaultExpAccount);
							if (type == ClientItem.TYPE_NON_INVENTORY_PART)
								AccounterValidator
										.defaultExpenseAccountNonInventory(
												selectExpAccount,
												defaultExpAccount);
						}
					}
				});
		expAccCombo.setPopupWidth("500px");
		prefVendorCombo = new VendorCombo(messages.preferredVendor(Global.get()
				.Vendor()));
		prefVendorCombo.setHelpInformation(true);
		prefVendorCombo.setDisabled(isInViewMode());
		prefVendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectVendor = selectItem;
					}
				});

		vendItemNumText = new IntegerField(this,
				this.type != ClientItem.TYPE_SERVICE ? messages
						.vendorProductNo(Global.get().Vendor()) : messages
						.vendorServiceNo(Global.get().Vendor()));
		vendItemNumText.setHelpInformation(true);
		vendItemNumText.setWidth(100);
		vendItemNumText.setDisabled(isInViewMode());

		// isellCheck = new CheckboxItem(FinanceApplication
		// .constants().iSellThisItem());
		isellCheck = new CheckboxItem(
				this.type == ClientItem.TYPE_SERVICE ? Accounter.constants()
						.isellthisservice() : Accounter.constants()
						.isellthisproduct());
		isellCheck.setDisabled(isInViewMode());

		isellCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disableSalesFormItems(!event.getValue());
			}

		});

		// ibuyCheck = new
		// CheckboxItem(FinanceApplication.constants()
		// .iBuyThisItem());
		ibuyCheck = new CheckboxItem(
				this.type == ClientItem.TYPE_SERVICE ? Accounter.constants()
						.ibuythisservice() : Accounter.constants()
						.ibuythisproduct());
		// ibuyCheck.setDisabled(isGeneratedFromCustomer);

		ibuyCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disablePurchaseFormItems(!event.getValue());

			}

		});
		if (!isInViewMode()) {
			if (isGeneratedFromCustomer) {
				isellCheck.setValue(isGeneratedFromCustomer);
				disablePurchaseFormItems(isGeneratedFromCustomer);
			} else {
				ibuyCheck.setValue(isGeneratedFromCustomer);
				disableSalesFormItems(isGeneratedFromCustomer);
			}
		}

		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			salesInfoForm.setFields(isellCheck, salesDescArea, salesPriceText,
					accountCombo, comCheck, stdCostText);
		else
			salesInfoForm.setFields(/* isservice, */isellCheck, salesDescArea,
					salesPriceText, accountCombo, itemTaxCheck, comCheck,
					stdCostText);

		salesInfoForm.setStyleName("align-form");
		salesInfoForm.setStyleName("new_service_table");
		salesInfoForm.getCellFormatter().setWidth(0, 0, "42%");
		salesInfoForm.getCellFormatter().setWidth(3, 0, "25%");
		salesInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		itemInfoForm = UIUtils.form(Accounter.constants().itemInformation());
		itemInfoForm.getCellFormatter().setWidth(0, 0, "47%");
		itemInfoForm.setWidth("97%");
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			itemInfoForm.setFields(itemGroupCombo, taxCode, activeCheck);
		else
			itemInfoForm.setFields(itemGroupCombo, activeCheck);
		// itemInfoForm.getCellFormatter().setWidth(0, 0, "47%");
		purchaseInfoForm = UIUtils.form(Accounter.constants()
				.purchaseInformation());
		purchaseInfoForm.setNumCols(2);
		purchaseInfoForm.setStyleName("purchase_info_form");
		purchaseInfoForm.setWidth("97%");
		purchaseInfoForm
				.setFields(ibuyCheck, purchaseDescArea, purchasePriceTxt,
						expAccCombo, prefVendorCombo, vendItemNumText);
		purchaseInfoForm.getCellFormatter().setWidth(0, 0, "47%");
		purchaseInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		VerticalPanel salesVPanel = new VerticalPanel();
		salesVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		salesVPanel.setWidth("100%");
		HorizontalPanel itemHPanel = new HorizontalPanel();
		itemHPanel.setWidth("90%");

		itemHPanel.setCellHorizontalAlignment(itemForm, ALIGN_RIGHT);
		salesVPanel.setCellHorizontalAlignment(itemHPanel, ALIGN_RIGHT);

		// itemHPanel.add(itemForm);

		salesVPanel.add(salesInfoForm);
		// salesVPanel.add(stdCostForm);

		VerticalPanel purchzVPanel = new VerticalPanel();

		purchzVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		purchzVPanel.setWidth("100%");
		HorizontalPanel itemInfoPanel = new HorizontalPanel();

		itemInfoPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);
		// itemInfoPanel.add(itemInfoForm);

		purchzVPanel.add(purchaseInfoForm);

		HorizontalPanel topPanel1 = new HorizontalPanel();
		// topPanel1.setSpacing(15);
		// topPanel1.setStyleName("equal-columns");
		topPanel1.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel1.setWidth("100%");
		topPanel1.add(itemForm);
		topPanel1.setStyleName("service-item-group");
		topPanel1.setCellHorizontalAlignment(itemInfoPanel, ALIGN_RIGHT);
		topPanel1.add(itemInfoForm);
		topPanel1.setCellWidth(itemForm, "50%");
		topPanel1.setCellWidth(itemInfoForm, "50%");
		VerticalPanel topHLay = new VerticalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");

		HorizontalPanel topPanel2 = new HorizontalPanel();
		// topPanel2.setStyleName("equal-columns");
		VerticalPanel emptyPanel = new VerticalPanel();
		emptyPanel.setWidth("100%");

		topPanel2.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel2.setWidth("100%");
		topPanel2.add(salesVPanel);
		// topPanel2.add(emptyPanel);
		topPanel2.setCellHorizontalAlignment(purchzVPanel, ALIGN_LEFT);
		topPanel2.add(purchzVPanel);
		topPanel2.setCellWidth(salesVPanel, "50%");
		topPanel2.setCellWidth(purchzVPanel, "50%");
		topHLay.add(topPanel1);
		topHLay.add(topPanel2);
		// topHLay.setCellWidth(topPanel1, "50%");
		// topHLay.setCellWidth(topPanel2, "50%");

		VerticalPanel mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "100%");
		mainVLay.getElement().getStyle().setMarginBottom(15, Unit.PX);
		mainVLay.add(hPanel);
		mainVLay.add(topHLay);
		if (type == ClientItem.TYPE_INVENTORY_PART) {
			VerticalPanel stockPanel_1 = getStockPanel_1();
			VerticalPanel stockPanel_2 = getStockPanel_2();
			salesVPanel.add(stockPanel_1);
			purchzVPanel.add(stockPanel_2);
			purchzVPanel.setCellHorizontalAlignment(stockPanel_1, ALIGN_LEFT);
			purchzVPanel.setCellHorizontalAlignment(stockPanel_2, ALIGN_LEFT);

		}
		this.add(mainVLay);

		if (getData() != null) {
			nameText.setValue(data.getName());
			name = data.getName();
			System.out.println(name + Accounter.constants().beforesaving());
			stdCostText.setAmount(data.getStandardCost());
			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_US)
			// skuText.setValue(data.getUPCorSKU() != null ? data
			// .getUPCorSKU() : "");

			weightText.setValue(String.valueOf(data.getWeight()));

			isellCheck.setValue(data.isISellThisItem());
			if (data.getSalesDescription() != null)
				salesDescArea.setValue(data.getSalesDescription());
			salesPriceText.setAmount(data.getSalesPrice());

			ClientCompany company = getCompany();
			selectAccount = company.getAccount(data.getIncomeAccount());
			comCheck.setValue(data.isCommissionItem());

			selectItemGroup = company.getItemGroup(data.getItemGroup());

			activeCheck.setValue((data.isActive()));

			ibuyCheck.setValue(data.isIBuyThisItem());
			if (data.getPurchaseDescription() != null)
				purchaseDescArea.setValue(data.getPurchaseDescription());
			purchasePriceTxt.setAmount(data.getPurchasePrice());

			selectVendor = company.getVendor(data.getPreferredVendor());
			if (data.getVendorItemNumber() != null) {
				vendItemNumText.setValue(data.getVendorItemNumber());
			}
			if (getPreferences().isTrackTax()
					&& getPreferences().isTaxPerDetailLine()) {
				selectTaxCode = company.getTAXCode(data.getTaxCode());
			}
			itemTaxCheck.setValue(data.isTaxable());

		} else {
			setData(new ClientItem());
		}

		/* Adding dynamic forms in list */
		listforms.add(itemForm);
		listforms.add(salesInfoForm);

		listforms.add(stdCostForm);
		listforms.add(itemInfoForm);
		listforms.add(purchaseInfoForm);
		settabIndexes();

	}

	private VerticalPanel getStockPanel_2() {
		VerticalPanel measurementPanel = new VerticalPanel();
		measurement = new MeasurementCombo(Accounter.constants().measurement());
		measurement.setDisabled(isInViewMode());

		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(measurement);

		measurementPanel.add(dynamicForm);

		return measurementPanel;
	}

	private VerticalPanel getStockPanel_1() {

		VerticalPanel stockPanel = new VerticalPanel();
		DynamicForm stockForm = new DynamicForm();

		wareHouse = new WarehouseCombo(Accounter.constants().wareHouse());

		openingBalTxt = new AmountField(Accounter.constants().openingBalance(),
				this);
		openingBalTxt.setDisabled(isInViewMode());
		wareHouse.setDisabled(isInViewMode());
		// if (getPreferences().iswareHouseEnabled()) {
		stockForm.setFields(wareHouse);
		// } else {
		// stockForm.setFields(openingBalTxt);
		// }
		if (getPreferences().iswareHouseEnabled()) {
			stockPanel.add(stockForm);
		}
		listforms.add(stockForm);

		stockForm.setWidth("100%");
		stockPanel.setWidth("100%");
		return stockPanel;

	}

	@Override
	public void saveAndUpdateView() {
		updateItem();
		saveOrUpdate(data);

	}

	protected void disableSalesFormItems(Boolean isEdit) {
		salesDescArea.setDisabled(isEdit);
		salesPriceText.setDisabled(isEdit);
		accountCombo.setDisabled(isEdit);
		itemTaxCheck.setDisabled(isEdit);
		comCheck.setDisabled(isEdit);
	}

	/**
	 * call to save Entered data on this View
	 * 
	 * @param isSaveClose
	 *            if it is true, than it means it will close View, if false,than
	 *            reset values to new View
	 */
	protected void updateItem() {

		data.setActive(getBooleanValue(activeCheck));
		data.setType(type);
		if (nameText.getValue() != null)
			data.setName(nameText.getValue().toString());
		if (selectItemGroup != null)
			data.setItemGroup(selectItemGroup.getID());
		data.setStandardCost(stdCostText.getAmount());

		data.setUPCorSKU((String) skuText.getValue());

		if (type == ClientItem.TYPE_NON_INVENTORY_PART
				&& weightText.getNumber() != null)
			data.setWeight(UIUtils.toInt(weightText.getNumber()));

		data.setISellThisItem(getBooleanValue(isellCheck));
		data.setIBuyThisItem(getBooleanValue(ibuyCheck));

		if (getBooleanValue(isellCheck)) {
			if (salesDescArea.getValue() != null)
				data.setSalesDescription(salesDescArea.getValue().toString());
			data.setSalesPrice(salesPriceText.getAmount());
			if (selectAccount != null)
				data.setIncomeAccount(selectAccount.getID());
			data.setCommissionItem(getBooleanValue(comCheck));
		}

		if (getBooleanValue(ibuyCheck)) {

			data.setPurchaseDescription(getStringValue(purchaseDescArea));
			data.setPurchasePrice(purchasePriceTxt.getAmount());
			if (selectVendor != null)
				data.setPreferredVendor(selectVendor.getID());
			if (selectExpAccount != null)
				data.setExpenseAccount(selectExpAccount.getID());

			data.setVendorItemNumber(vendItemNumText.getValue().toString());
		}
		data.setTaxable(getBooleanValue(itemTaxCheck));
		if (type == ClientItem.TYPE_NON_INVENTORY_PART
				|| type == ClientItem.TYPE_SERVICE)
			data.setTaxCode(selectTaxCode != null ? selectTaxCode.getID() : 0);
		if (type == ClientItem.TYPE_INVENTORY_PART) {
			if (measurement.getSelectedValue() != null)
				data.setMeasurement(measurement.getSelectedValue().getId());
			if (wareHouse.getSelectedValue() != null)
				data.setWarehouse(wareHouse.getSelectedValue().getID());
			data.setMinStockAlertLevel(null);
			data.setMaxStockAlertLevel(null);
			data.setOpeningBalance(openingBalTxt.getAmount());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		String exceptionMessage = exception.getMessage();
		// addError(this,exceptionMessage);

		// BaseView.errordata
		// .setHTML(this.type != ClientItem.TYPE_SERVICE ?
		// "Duplication of Product name are not allowed..."
		// : "Duplication of Service name are not allowed...");
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this, this.type != ClientItem.TYPE_SERVICE ?
		// Accounter.constants()
		// .duplicationofProductnamearenotallowed3dots() : Accounter
		// .constants().duplicationofServicenamearenotallowed3dots());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateItem();
		if (exceptionMessage.contains(Accounter.constants().failed())) {
			data.setName(name);
			System.out.println(name + Accounter.constants().aftersaving());
		}

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			saveFailed(new AccounterException());
			return;
		} else {
			// if (takenItem == null)
			// Accounter.showInformation(FinanceApplication
			// .constants().itemCreatedSuccessfully());
			// else
			// Accounter.showInformation(FinanceApplication
			// .constants().itemupdatedSuccessfully());
			NewItemAction action = (NewItemAction) this.getAction();
			action.setType(type);
			super.saveSuccess(result);

		}

	}

	private boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;
	}

	private String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";
	}

	protected void disablePurchaseFormItems(Boolean isDisable) {

		purchasePriceTxt.setDisabled(isDisable);
		purchaseDescArea.setDisabled(isDisable);
		vendItemNumText.setDisabled(isDisable);
		expAccCombo.setDisabled(isDisable);
		prefVendorCombo.setDisabled(isDisable);

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		initAccountList();
		initVendorsList();
		initItemGroups();
		if (type == ClientItem.TYPE_INVENTORY_PART) {
			setStockPanelData();
		}
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			if (data != null) {
				if (getPreferences().isTaxPerDetailLine())
					initTaxCodes();
			}
		}
		super.initData();

	}

	private void setStockPanelData() {
		ClientMeasurement measurement;
		if (data.getMeasurement() != 0) {
			measurement = getCompany().getMeasurement(data.getMeasurement());
		} else {
			measurement = getCompany().getMeasurement(
					getCompany().getDefaultMeasurement());
		}
		this.measurement.setComboItem(measurement);
		ClientWarehouse wareHouse;
		if (data.getWarehouse() != 0) {
			wareHouse = getCompany().getWarehouse(data.getWarehouse());
		} else {
			wareHouse = getCompany().getWarehouse(
					getCompany().getDefaultWarehouse());
		}
		this.wareHouse.setComboItem(wareHouse);
		this.openingBalTxt.setAmount(data.getOpeningBalance());

	}

	private void initItemGroups() {
		List<ClientItemGroup> clientItemgroup = getCompany().getItemGroups();

		if (clientItemgroup != null) {
			itemGroupCombo.initCombo(clientItemgroup);
			if (isInViewMode()) {
				itemGroupCombo.setComboItem(getCompany().getItemGroup(
						data.getItemGroup()));
			}
		}
		if (isInViewMode()) {
			if (data.isISellThisItem()) {
				ibuyCheck.setDisabled(true);
				disablePurchaseFormItems(true);
			}

			if (data.isIBuyThisItem()) {
				isellCheck.setDisabled(true);
				disableSalesFormItems(true);
			}

		} else {
			disablePurchaseFormItems(isGeneratedFromCustomer);
			disableSalesFormItems(!isGeneratedFromCustomer);
		}
	}

	private void initVendorsList() {
		List<ClientVendor> clientVendor = getCompany().getActiveVendors();
		if (clientVendor != null) {
			prefVendorCombo.initCombo(clientVendor);
			if (!isInViewMode()) {
				prefVendorCombo.setComboItem(getCompany().getVendor(
						data.getPreferredVendor()));
				if (data.isIBuyThisItem() == false)
					prefVendorCombo.setDisabled(true);
				else
					prefVendorCombo.setDisabled(false);

			} else
				prefVendorCombo.setDisabled(true);
		}

	}

	private void initAccountList() {
		List<ClientAccount> listAccount = accountCombo.getFilterdAccounts();
		List<ClientAccount> listExpAccount = expAccCombo.getFilterdAccounts();
		if (listAccount != null) {
			accountCombo.initCombo(listAccount);
			expAccCombo.initCombo(listExpAccount);
			if (type == ClientItem.TYPE_SERVICE) {
				// if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				// defaultIncomeAccount = getDefaultAccount(company
				// .getUkServiceItemDefaultIncomeAccount());
				// defaultExpAccount = getDefaultAccount(company
				// .getUkServiceItemDefaultExpenseAccount());
				// } else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				defaultIncomeAccount = getDefaultAccount(Accounter.constants()
						.incomeandDistribution());
				defaultExpAccount = getDefaultAccount(Accounter.constants()
						.cashDiscountTaken());
				// }
				selectAccount = defaultIncomeAccount;
				accountCombo.setComboItem(defaultIncomeAccount);
				selectExpAccount = defaultExpAccount;
				expAccCombo.setComboItem(defaultExpAccount);
			}
			if (type == ClientItem.TYPE_NON_INVENTORY_PART) {
				// if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				// defaultIncomeAccount = getDefaultAccount(company
				// .getUkNonInventoryItemDefaultIncomeAccount());
				// defaultExpAccount = getDefaultAccount(company
				// .getUkNonInventoryItemDefaultExpenseAccount());
				// } else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				defaultIncomeAccount = getDefaultAccount(Accounter.constants()
						.incomeandDistribution());
				defaultExpAccount = getDefaultAccount(Accounter.constants()
						.cashDiscountTaken());

				// }
				selectAccount = defaultIncomeAccount;
				accountCombo.setComboItem(defaultIncomeAccount);
				selectExpAccount = defaultExpAccount;
				expAccCombo.setComboItem(defaultExpAccount);
			}
		}
		if (!isInViewMode()) {

			accountCombo.setComboItem(getCompany().getAccount(
					data.getIncomeAccount()));
			if (data.isISellThisItem() == false)
				accountCombo.setDisabled(true);
			selectExpAccount = getCompany()
					.getAccount(data.getExpenseAccount());
			expAccCombo.setComboItem(selectExpAccount);
			if (data.isIBuyThisItem() == false)
				expAccCombo.setDisabled(true);
			else
				expAccCombo.setDisabled(false);

		} else
			expAccCombo.setDisabled(true);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// check whether the item is already available or not
		// itemform valid?
		// isSell or isBuy checked?
		// salesinfoform or buyinfoform validation
		// positive sales price and positive purchase price check
		// valid income accont and valid expense account?

		String name = nameText.getValue().toString();

		ClientItem clientItem = company.getItemByName(name);
		if (clientItem != null
				&& !(this.getData().getID() == clientItem.getID())) {
			result.addError(nameText, Accounter.constants()
					.anItemAlreadyExistswiththisname());
			return result;
		}

		result.add(itemForm.validate());

		if (!AccounterValidator.isSellorBuyCheck(isellCheck, ibuyCheck)) {
			result.addError(isellCheck, accounterConstants.checkAnyone());
		}
		if (isellCheck.isChecked()) {
			result.add(salesInfoForm.validate());
		}
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			result.add(itemInfoForm.validate());
		}
		if (ibuyCheck.isChecked()) {
			result.add(purchaseInfoForm.validate());
		}

		if (isellCheck.isChecked()) {
			if (AccounterValidator.isNegativeAmount(salesPriceText.getAmount())) {
				result.addError(salesPriceText, accounterConstants
						.enterValidAmount());
			}
		}

		if (ibuyCheck.isChecked()) {
			if (AccounterValidator.isNegativeAmount(purchasePriceTxt
					.getAmount())) {
				result.addError(purchasePriceTxt, accounterConstants
						.enterValidAmount());
			}
		}

		if (selectAccount != null) {
			if (isellCheck.isChecked()) {
				if (!AccounterValidator.isValidIncomeAccount(this,
						selectAccount)) {
					result.addWarning(accountCombo,
							AccounterWarningType.different_IncomeAccountType);
				}
			}
		}
		if (selectExpAccount != null) {
			if (ibuyCheck.isChecked()) {
				if (!AccounterValidator
						.validate_ExpenseAccount(selectExpAccount)) {
					result.addWarning(expAccCombo,
							AccounterWarningType.different_ExpenseAccountType);
				}
			}
		}
		return result;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.nameText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {

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

		this.rpcDoSerivce.canEdit(AccounterCoreType.ITEM, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		nameText.setDisabled(isInViewMode());
		// isservice.setDisabled(isInViewMode());
		skuText.setDisabled(isInViewMode());
		weightText.setDisabled(isInViewMode());
		salesDescArea.setDisabled(isInViewMode());
		salesPriceText.setDisabled(isInViewMode());
		accountCombo.setDisabled(isInViewMode());
		stdCostText.setDisabled(isInViewMode());
		itemGroupCombo.setDisabled(isInViewMode());
		taxCode.setDisabled(isInViewMode());
		isellCheck.setDisabled(isInViewMode());
		ibuyCheck.setDisabled(isInViewMode());
		if (ibuyCheck.getValue()) {
			purchaseDescArea.setDisabled(isInViewMode());
			expAccCombo.setDisabled(isInViewMode());
			prefVendorCombo.setDisabled(isInViewMode());
			purchasePriceTxt.setDisabled(isInViewMode());
			vendItemNumText.setDisabled(isInViewMode());
		} else {
			purchaseDescArea.setDisabled(true);
			expAccCombo.setDisabled(true);
			prefVendorCombo.setDisabled(true);
			purchasePriceTxt.setDisabled(true);
			vendItemNumText.setDisabled(true);
		}
		if (isellCheck.getValue()) {
			salesDescArea.setDisabled(isInViewMode());
			accountCombo.setDisabled(isInViewMode());
			salesPriceText.setDisabled(isInViewMode());
		} else {
			salesDescArea.setDisabled(true);
			accountCombo.setDisabled(true);
			salesPriceText.setDisabled(true);
		}

		if (type == ClientItem.TYPE_INVENTORY_PART) {
			measurement.setDisabled(isInViewMode());
			wareHouse.setDisabled(isInViewMode());
			openingBalTxt.setDisabled(isInViewMode());
		}
		activeCheck.setDisabled(isInViewMode());

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
	protected String getViewTitle() {
		return Accounter.constants().item();
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	private void settabIndexes() {
		nameText.setTabIndex(1);
		// isservice.setTabIndex(2);
		weightText.setTabIndex(2);
		isellCheck.setTabIndex(3);
		salesDescArea.setTabIndex(4);
		salesPriceText.setTabIndex(5);
		accountCombo.setTabIndex(6);
		comCheck.setTabIndex(7);
		stdCostText.setTabIndex(8);
		itemGroupCombo.setTabIndex(9);
		taxCode.setTabIndex(10);
		activeCheck.setTabIndex(11);
		ibuyCheck.setTabIndex(12);
		purchaseDescArea.setTabIndex(13);
		purchasePriceTxt.setTabIndex(14);
		expAccCombo.setTabIndex(15);
		prefVendorCombo.setTabIndex(16);
		vendItemNumText.setTabIndex(17);
		saveAndCloseButton.setTabIndex(18);
		saveAndNewButton.setTabIndex(19);
		cancelButton.setTabIndex(20);
	}

}
