package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;

public class PurchaseOrderView extends
		AbstractVendorTransactionView<ClientPurchaseOrder> {

	private PaymentTermsCombo payTermsSelect;
	private ShippingTermsCombo shippingTermsCombo;
	private ShippingMethodsCombo shippingMethodsCombo;
	private AddressCombo shipToCombo;
	private Button addLinksButton;
	private TextItem linksText;
	private DynamicForm vendorForm;
	private DynamicForm termsForm;
	private ClientAddress shippingAddress;
	protected ClientAddress vendorAddress;
	protected ClientPaymentTerms paymentTerms;
	protected ClientShippingTerms shippingTerms;
	protected ClientShippingMethod shippingMethod;
	private TextAreaItem billtoAreaItem;
	private ShipToForm shipToAddress;
	private DateField dueDateItem;

	private long dueDate;
	private long despatchDate;

	private long deliveryDate;
	DynamicForm amountsForm;

	private ArrayList<DynamicForm> listforms;
	private TextItem purchaseOrderText;
	private HTML lab1;
	private List<String> listOfTypes;
	private String OPEN = Accounter.constants().open();
	private String COMPLETED = Accounter.constants().completed();
	private String CANCELLED = Accounter.constants().cancelled();
	private DateField despatchDateItem;
	AccounterConstants accounterConstants = Accounter.constants();
	private boolean locationTrackingEnabled;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private CurrencyWidget currencyWidget;

	public PurchaseOrderView() {
		super(ClientTransaction.TYPE_PURCHASE_ORDER);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .purchaseOrder()));

		lab1 = new HTML(Accounter.constants().purchaseOrder());
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");

		statusSelect = new SelectCombo(Accounter.constants().status());
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		statusSelect.initCombo(listOfTypes);
		statusSelect.setComboItem(OPEN);
		statusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (statusSelect.getSelectedValue() != null)
							statusSelect.setComboItem(selectItem);

					}
				});
		statusSelect.setRequired(true);
		statusSelect.setDisabled(isInViewMode());
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().orderNo());
		transactionNumber.setWidth(50);

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.addStyleName("date-number");
		dateNoForm.setFields(statusSelect, transactionDateItem);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("98%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);
		amountsForm = new DynamicForm();
		amountsForm.setWidth("100%");

		netAmount = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabelforPurchase();

		vatTotalNonEditableText = createVATTotalNonEditableLabelforPurchase();

		// vendorCombo =
		// createVendorComboItem(Accounter.constants().vendorName());

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		if (isTrackTax() && isTrackPaidTax()) {

			DynamicForm priceLevelForm = new DynamicForm();
			// priceLevelForm.setCellSpacing(4);
			// priceLevelForm.setWidth("70%");
			// priceLevelForm.setFields(priceLevelSelect);

			if (!isTaxPerDetailLine()) {
				taxCodeSelect = createTaxCodeSelectItem();
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect, vatinclusiveCheck);
				prodAndServiceHLay.add(form);

				// this.taxCode =
				// getTaxCodeForTransactionItems(this.transactionItems);
				// if (taxCode != null) {
				// this.taxCodeSelect
				// .setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
				// }
				// this.transactionTotalNonEditableText.setAmount(transaction
				// .getTotal());
			}
			amountsForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);

			amountsForm.setStyleName("boldtext");
			// forms.add(priceLevelForm);
			// prodAndServiceHLay.add(priceLevelForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm,
			// ALIGN_RIGHT);
			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
			// listforms.add(priceLevelForm);abstracttrans

		} else {

			salesTaxTextNonEditable = createSalesTaxNonEditableLabel();
			transactionTotalNonEditableText = createTransactionTotalNonEditableLabelforPurchase();
			paymentsNonEditableText = new AmountLabel(
					accounterConstants.payments());
			paymentsNonEditableText.setDisabled(true);
			paymentsNonEditableText.setDefaultValue(""
					+ UIUtils.getCurrencySymbol() + " 0.00");

			balanceDueNonEditableText = new AmountField(
					accounterConstants.balanceDue(), this);
			balanceDueNonEditableText.setDisabled(true);
			balanceDueNonEditableText.setDefaultValue(""
					+ UIUtils.getCurrencySymbol() + " 0.00");
			// prodAndServiceForm2.setFields(salesTaxTextNonEditable,
			// transactionTotalNonEditableText, ,
			// balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);

			amountsForm.setNumCols(4);
			amountsForm.addStyleName("boldtext");
			amountsForm.setFields(/* taxCodeSelect, salesTaxTextNonEditable, */
			disabletextbox, transactionTotalNonEditableText, disabletextbox
			/*
			 * paymentsNonEditableText, disabletextbox,
			 * balanceDueNonEditableText
			 */);

			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
		}
		prodAndServiceHLay.add(amountsForm);
		prodAndServiceHLay.setCellHorizontalAlignment(amountsForm, ALIGN_RIGHT);
		prodAndServiceHLay.setCellVerticalAlignment(amountsForm,
				HasVerticalAlignment.ALIGN_BOTTOM);

		vendorCombo = new VendorCombo(Global.get().Vendor(), true);
		vendorCombo.setRequired(true);
		vendorCombo.setHelpInformation(true);

		vendorCombo.setDisabled(isInViewMode());
		// vendorCombo.setShowDisabled(false);

		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setTitle(Accounter.constants().contact());

		// contactCombo.setWidth(100);
		// billToCombo = createVendorAddressComboItem();
		// billToCombo.setTitle(FinanceApplication.constants().billTo());
		billtoAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billtoAreaItem.setWidth("100%");
		billtoAreaItem.setDisabled(true);

		// shipToCombo = createShipToComboItem();
		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		shipToAddress.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		shipToAddress.addrArea.setDisabled(true);
		shipToAddress.businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						shippingAddress = shipToAddress.getAddress();
						if (shippingAddress != null)
							shipToAddress.setAddres(shippingAddress);
						else
							shipToAddress.addrArea.setValue("");
					}
				});

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setDisabled(false);

		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Global.get().Vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billtoAreaItem);
		vendorForm.getCellFormatter().setWidth(0, 0, "226px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");

		// formItems.add(billToCombo);

		purchaseOrderText = new TextItem(messages.vendorOrderNo(Global.get()
				.Vendor()));
		purchaseOrderText.setWidth(50);
		purchaseOrderText.setColSpan(1);
		purchaseOrderText.setDisabled(isInViewMode());

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(Accounter.constants().dueDate());
		dueDateItem.setToolTip(Accounter.messages().selectDateUntilDue(
				this.getAction().getViewName()));
		dueDateItem.setDisabled(isInViewMode());
		// dueDateItem.setWidth(100);
		if (isInViewMode()) {
			// setDueDate(((ClientEnterBill) transactionObject).getDueDate());
		} else {
			setDueDate(new ClientFinanceDate().getDate());
		}
		dueDateItem.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					ClientFinanceDate newDate = ((DateField) event.getSource())
							.getValue();
					setDueDate(newDate.getDate());
				} catch (Exception e) {
					Accounter.showError(Accounter.constants().invalidDueDate());
				}

			}

		});
		despatchDateItem = new DateField(Accounter.constants().despatchDate());
		despatchDateItem.setDisabled(isInViewMode());
		if (isInViewMode()) {
		} else
			setDespatchDate(new ClientFinanceDate().getDate());

		despatchDateItem.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					ClientFinanceDate newDate = ((DateField) event.getSource())
							.getValue();
					setDespatchDate(newDate.getDate());
				} catch (Exception e) {
					Accounter.showError(Accounter.constants()
							.invalidDispatchDate());
				}

			}

		});

		deliveryDateItem = createTransactionDeliveryDateItem();
		deliveryDateItem.setTitle(Accounter.constants().receivedDate());

		DynamicForm dateform = new DynamicForm();
		dateform.setWidth("100%");
		dateform.setNumCols(2);
		if (locationTrackingEnabled)
			dateform.setFields(locationCombo);
		dateform.setItems(dueDateItem, despatchDateItem, deliveryDateItem);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			dateform.setFields(classListCombo);
		}

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(transactionNumber, purchaseOrderText,
				payTermsSelect);
		if (getPreferences().isDoProductShipMents()) {
			termsForm.setFields(shippingTermsCombo, shippingMethodsCombo);
		}
		termsForm.getCellFormatter().setWidth(0, 0, "208px");
		dateform.getCellFormatter().setWidth(0, 0, "230px");

		// formItems.add(checkNo);
		// formItems.add(dueDateItem);
		// formItems.add(despatchDateItem);
		// formItems.add(deliveryDateItem);

		// Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				PurchaseOrderView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return PurchaseOrderView.this.isShowPriceWithVat();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});
		FlowPanel accountFlowPanel = new FlowPanel();
		accountsDisclosurePanel = new DisclosurePanel("Itemize by Account");
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");
		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				PurchaseOrderView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return PurchaseOrderView.this.isShowPriceWithVat();
			}

		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});
		currencyWidget = createCurrencyWidget();
		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel("Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);

		// refText = createRefereceText();
		// refText.setWidth(100);
		addLinksButton = new Button(Accounter.constants().addLinks());
		// FIXME--need to disable basing on the mode of the view being opened

		// addLinksButton.setEnabled(true);
		linksText = new TextItem();
		linksText.setWidth(100);
		linksText.setShowTitle(false);
		linksText.setDisabled(isInViewMode());
		// formItems.add(linksText);

		DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("80%");
		memoForm.setFields(memoTextAreaItem);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm linksform = new DynamicForm();
		linksform.setWidth("100%");
		linksform.setItems(linksText);
		HorizontalPanel linkspanel = new HorizontalPanel();
		linkspanel.setWidth("70%");
		linkspanel.add(addLinksButton);
		addLinksButton.setEnabled(isInViewMode());
		linkspanel.add(linksform);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);
		if (getPreferences().isDoProductShipMents()) {
			leftVLay.add(shipToAddress);
		}
		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("93%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
		}
		// rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		// rightVLay.setCellHorizontalAlignment(dateform, ALIGN_RIGHT);

		HorizontalPanel topHLay = new HorizontalPanel();
		// topHLay.setStyleName("toplayout");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "52%");
		topHLay.setCellWidth(rightVLay, "47%");
		// topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.add(memoForm);
		panel.add(prodAndServiceHLay);

		// panel.setHorizontalAlignment(ALIGN_RIGHT);

		VerticalPanel bottomLayout = new VerticalPanel();
		bottomLayout.setWidth("100%");

		bottomLayout.add(panel);
		panel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		panel.setCellHorizontalAlignment(prodAndServiceHLay,
				HasHorizontalAlignment.ALIGN_RIGHT);
		// bottomLayout.add(linkspanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		mainVLay.add(bottomLayout);

		if (UIUtils.isMSIEBrowser()) {
			// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
			// vendorForm.setWidth("75%");
			// // termsForm.getCellFormatter().setWidth(0, 1, "68%");
			// memoForm.getCellFormatter().setWidth(0, 1, "300px");
			// memoForm.setWidth("40%");
			// statusSelect.setWidth("150px");
		}
		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(dateform);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(linksform);
		settabIndexes();
	}

	private PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(Accounter
				.constants().paymentTerms());

		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						if (selectItem != null) {
							paymentTerms = selectItem;
							paymentTermsSelected(paymentTerms);
						}

					}

				});
		comboItem.setDisabled(isInViewMode());
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	private ShippingTermsCombo createShippingTermsCombo() {

		ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				Accounter.constants().shippingTerms());

		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						if (selectItem != null)
							shippingTerms = selectItem;
					}

				});

		shippingTermsCombo.setDisabled(isInViewMode());

		// formItems.add(shippingTermsCombo);

		return shippingTermsCombo;
	}

	protected ShippingMethodsCombo createShippingMethodCombo() {

		ShippingMethodsCombo shippingMethodsCombo = new ShippingMethodsCombo(
				Accounter.constants().shippingMethod());

		shippingMethodsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						if (selectItem != null)
							shippingMethod = selectItem;
					}

				});

		shippingMethodsCombo.setDisabled(isInViewMode());

		// formItems.add(shippingMethodsCombo);

		return shippingMethodsCombo;

	}

	protected AddressCombo createShipToComboItem() {

		AddressCombo shipToCombo = new AddressCombo(Accounter.constants()
				.shipTo());

		shipToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {
						shipToAddressSelected(selectItem);
					}

				});

		shipToCombo.setDisabled(isInViewMode());
		// shipToCombo.setShowDisabled(false);
		if (getPreferences().isDoProductShipMents()) {
			// formItems.add(shipToCombo);
		}
		return shipToCombo;

	}

	public AddressCombo createVendorAddressComboItem() {

		AddressCombo addressCombo = new AddressCombo(
				messages.vendorAddress(Global.get().Vendor()));

		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						if (selectItem != null)
							vendoraddressSelected(selectItem);
					}

				});

		addressCombo.setDisabled(isInViewMode());
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientPurchaseOrder());
			super.initTransactionViewData();
			initVendorAddressCombo();
			initShipToCombo();
			initPaymentTerms();
			initShippingTerms();
			initShippingMethod();
		} else {
			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
			}
			// taxCodeSelected(this.taxCode);
			ClientCompany company = getCompany();

			vendorAccountTransactionTable
					.setRecords(getAccountTransactionItems(transaction
							.getTransactionItems()));
			vendorItemTransactionTable
					.setRecords(getItemTransactionItems(transaction
							.getTransactionItems()));

			// String status;
			// if (purchaseOrderToBeEdited.getStatus() ==
			// ClientPurchaseOrder.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			// status = "Un-Applied";
			// else if (purchaseOrderToBeEdited.getStatus() ==
			// ClientPurchaseOrder.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
			// status = "Partially Applied";
			// else
			// status = "Applied";
			// lab1.setText(FinanceApplication.constants().purchaseOrder()
			// + " (" + status + ")");
			this.dueDate = transaction.getDueDate();
			this.despatchDate = transaction.getDespatchDate();
			this.deliveryDate = transaction.getDeliveryDate();

			this.transactionItems = transaction.getTransactionItems();
			initTransactionNumber();
			this.setVendor(company.getVendor(transaction.getVendor()));
			vendorCombo.setComboItem(vendor);
			// vendorSelected(company.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			phoneSelect.setDisabled(isInViewMode());
			// vendoraddressSelected(purchaseOrderToBeEdited.getVendorAddress());
			// shipToAddressSelected(purchaseOrderToBeEdited.getShippingAddress());

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getVendor() != null)
				addresses.addAll(getVendor().getAddress());
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(shippingAddress
						.getAddressTypes().get(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}
			shipToAddress.businessSelect.setDisabled(true);

			this.addressListOfVendor = getVendor().getAddress();

			if (billingAddress != null) {

				billtoAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billtoAreaItem.setValue("");
			if (isTrackTax() && isTrackPaidTax()) {

				if (!isTaxPerDetailLine()) {
					ClientTAXCode code = getTaxCodeForTransactionItems(this.transactionItems);
					this.taxCodeSelect.setComboItem(code);
					taxCode = code;
				}
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
				}
			}

			purchaseOrderText.setValue(transaction.getPurchaseOrderNumber());

			paymentTermsSelected(company.getPaymentTerms(transaction
					.getPaymentTerm()));
			shippingTermsSelected(company.getShippingTerms(transaction
					.getShippingTerms()));
			shippingMethodSelected(company.getShippingMethod(transaction
					.getShippingMethod()));
			dueDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDueDate()));
			despatchDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDespatchDate()));
			deliveryDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			memoTextAreaItem.setValue(transaction.getMemo());
			memoTextAreaItem.setDisabled(isInViewMode());
			// refText.setValue(purchaseOrderToBeEdited.getReference());
			int status = transaction.getStatus();
			switch (status) {
			case ClientTransaction.STATUS_OPEN:
				statusSelect.setComboItem(OPEN);
				break;
			case ClientTransaction.STATUS_COMPLETED:
				statusSelect.setComboItem(COMPLETED);
				break;
			case ClientTransaction.STATUS_CANCELLED:
				statusSelect.setComboItem(CANCELLED);
			default:
				break;
			}
			initAccounterClass();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
	}

	private void initDeliveryDate() {

		if (isInViewMode()) {
			ClientPurchaseOrder purchaseOrder = (ClientPurchaseOrder) transaction;
			deliveryDateItem.setEnteredDate(new ClientFinanceDate(purchaseOrder
					.getDeliveryDate()));

		}

	}

	private void initVendorAddressCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		billToCombo.setDisabled(isInViewMode());
		// billToCombo.setShowDisabled(false);

		if (isInViewMode() && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setComboItem(null);
			billToaddressSelected(clientAddress);
		}
	}

	private void initShipToCombo() {

		if (shipToCombo == null || addressListOfVendor == null)
			return;
		// ClientCompany company = FinanceApplication.getCompany();
		//
		// List<ClientAddress> listOfAddress = company.getAddresses();

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_SHIP_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}

		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		shipToCombo.initCombo(adressList);
		shipToCombo.setDisabled(isInViewMode());

		if (isInViewMode() && shippingAddress != null) {
			shipToCombo.setComboItem(shippingAddress);
			return;
		}
		if (clientAddress != null) {
			shipToCombo.setComboItem(clientAddress);
			shipToAddressSelected(clientAddress);

		} else {
			shipToCombo.setComboItem(null);
			shipToAddressSelected(clientAddress);
		}

	}

	private void initPaymentTerms() {

		payTermsSelect.initCombo(getCompany().getPaymentsTerms());

	}

	private void initShippingTerms() {

		shippingTermsCombo.initCombo(getCompany().getShippingTerms());

	}

	private void initShippingMethod() {

		List<ClientShippingMethod> result = getCompany().getShippingMethods();
		if (shippingMethodsCombo != null) {
			shippingMethodsCombo.initCombo(result);

		}

	}

	@Override
	protected void initMemoAndReference() {

		if (isInViewMode()) {

			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(salesOrder.getReference());

		}

	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalNonEditableText
				.setAmount(getAmountInTransactionCurrency(grandTotal));
		netAmount.setAmount(getAmountInTransactionCurrency(lineTotal));
		// vatTotalNonEditableText.setValue(vendorTransactionGrid.getVatTotal());
		if (getPreferences().isTrackPaidTax()) {
			vatTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal
							- lineTotal));
		}

	}

	@Override
	public void saveAndUpdateView() {

		super.saveAndUpdateView();

		saveOrUpdate((ClientPurchaseOrder) transaction);

		// if (isTrackTax()) {
		// netAmount.setAmount(transaction.getNetAmount());
		// vatTotalNonEditableText.setAmount(transaction.getTotal()
		// - transaction.getNetAmount());
		// transactionTotalNonEditableText.setAmount(transaction.getTotal());
		// }

	}

	protected void updateTransaction() {
		super.updateTransaction();
		if (getVendor() != null) {
			transaction.setVendor(getVendor().getID());
		}

		if (statusSelect.getSelectedValue().equals(OPEN))
			transaction.setStatus(ClientTransaction.STATUS_OPEN);
		else if (statusSelect.getSelectedValue().equals(COMPLETED))
			transaction.setStatus(ClientTransaction.STATUS_COMPLETED);
		else if (statusSelect.getSelectedValue().equals(CANCELLED))
			transaction.setStatus(ClientTransaction.STATUS_CANCELLED);

		if (contact != null)
			transaction.setContact(contact);
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAddress(shippingAddress);

		if (purchaseOrderText.getValue() != null)
			transaction.setPurchaseOrderNumber(purchaseOrderText.getValue()
					.toString());
		if (paymentTerms != null)
			transaction.setPaymentTerm(paymentTerms.getID());
		if (shippingTerms != null)
			transaction.setShippingTerms(shippingTerms.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (dueDateItem.getEnteredDate() != null) {
			transaction.setDueDate(dueDateItem.getEnteredDate().getDate());
		}
		if (despatchDateItem.getEnteredDate() != null) {
			transaction.setDespatchDate(despatchDateItem.getEnteredDate()
					.getDate());
		}
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate((deliveryDateItem.getEnteredDate()
					.getDate()));

		transaction.setMemo(getMemoTextAreaItem());
		if (transaction.getNetAmount() != 0)
			transaction
					.setNetAmount(vendorAccountTransactionTable.getLineTotal()
							+ vendorItemTransactionTable.getLineTotal());
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// transaction.setReference(getRefText());

		if (isTrackTax()) {
			if (vatinclusiveCheck != null)
				transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());

		}
		transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		this.setVendor(vendor);
		if (vendor == null)
			return;

		super.vendorSelected(vendor);
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billtoAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billtoAreaItem.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(vendor.getAddress());
		shipToAddress.setAddress(addresses);

		initVendorAddressCombo();
		initShipToCombo();

		ClientCompany company = getCompany();
		paymentTerms = company.getPaymentTerms(vendor.getPaymentTerms());
		shippingMethod = company.getShippingMethod(vendor.getShippingMethod());
		if (paymentTerms != null) {
			payTermsSelect.setComboItem(paymentTerms);
			paymentTermsSelected(paymentTerms);
		}

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrency(clientCurrency);
		}
		shippingMethodsCombo.setComboItem(shippingMethod);
		vendorCombo.setComboItem(vendor);
	}

	private void vendoraddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.vendorAddress = selectedAddress;
		billToCombo.setComboItem(selectedAddress);

	}

	protected void shipToAddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.shippingAddress = selectedAddress;
		shipToCombo.setComboItem(selectedAddress);

	}

	private void paymentTermsSelected(ClientPaymentTerms paymentTerms) {
		if (paymentTerms != null) {
			this.paymentTerms = paymentTerms;
			payTermsSelect.setComboItem(paymentTerms);
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(
					getTransactionDate(), paymentTerms);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}
	}

	private void shippingTermsSelected(ClientShippingTerms shippingTerms) {
		if (shippingTerms != null) {
			this.shippingTerms = shippingTerms;
			shippingTermsCombo.setComboItem(shippingTerms);
		}
	}

	private void shippingMethodSelected(ClientShippingMethod shippingMethod) {
		if (shippingMethod != null) {
			this.shippingMethod = shippingMethod;
			shippingMethodsCombo.setComboItem(shippingMethod);
		}
	}

	private void setDueDate(long date) {
		dueDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	private void setDespatchDate(long date) {
		despatchDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// TODO:: is it required to validate transaction date?yes
		// TODO:: do we require validation for dispatchdate,
		// receiveddate?dispatchDate is not required
		// 1. isValid transaction date?
		// 2. is in prevent posting before date?
		// 3. statusSelect valid? is valid received date?
		// 4. is valid due date?
		// 5. vendon form valid?
		// 6. is blank transaction?
		// 7. vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// accounterConstants.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}

		// TODO::: isvalid received date
		if (!AccounterValidator.isValidPurchaseOrderRecievedDate(
				deliveryDateItem.getDate(), transactionDate)) {
			result.addError(deliveryDateItem, Accounter.constants()
					.receivedDateShouldNotBeAfterTransactionDate());
		}

		if (!statusSelect.validate()) {
			result.addError(statusSelect, statusSelect.getTitle());
		}

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				dueDateItem.getDate(), transactionDateItem.getDate())) {
			result.addError(dueDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().dueDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}

		result.add(vendorForm.validate());

		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					accounterConstants.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}

		// if (getCompany().getAccountingType() !=
		// ClientCompany.ACCOUNTING_TYPE_UK
		// && getCompany().getPreferences().getDoYouPaySalesTax()) {
		// if (taxCodeSelect != null && !taxCodeSelect.validate()) {
		// result.addError(taxCodeSelect,
		// messages.pleaseEnter(taxCodeSelect.getTitle()));
		// }
		// }

		return result;
	}

	public void onEdit() {
		if (transaction.getStatus() == ClientTransaction.STATUS_COMPLETED)
			Accounter.showError("Completed purchase order can't be edited");
		else {
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

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		statusSelect.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		shipToAddress.businessSelect.setDisabled(isInViewMode());
		// shipToCombo.setDisabled(isEdit);
		ClientTransactionItem item = new ClientTransactionItem();
		if (!DecimalUtil.isEquals(item.getInvoiced(), 0)) {
			vendorCombo.setDisabled(isInViewMode());
		} else {
			vendorCombo.setDisabled(true);
			if (this.transaction.getVendorAddress() == null) {
				this.addressListOfVendor = vendor.getAddress();
				billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
				if (billingAddress != null) {
					billtoAreaItem.setValue(billingAddress.getAddress1() + "\n"
							+ billingAddress.getStreet() + "\n"
							+ billingAddress.getCity() + "\n"
							+ billingAddress.getStateOrProvinence() + "\n"
							+ billingAddress.getZipOrPostalCode() + "\n"
							+ billingAddress.getCountryOrRegion());

				} else
					billtoAreaItem.setValue("");
			}

			if (this.transaction.getPhone() == null
					|| this.transaction.getPhone().isEmpty()) {
				initPhones(vendor);
			}

			if (this.transaction.getContact() == null) {
				initContacts(vendor);
			}
		}

		// billToCombo.setDisabled(isEdit);
		purchaseOrderText.setDisabled(isInViewMode());
		deliveryDateItem.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());

		shippingTermsCombo.setDisabled(isInViewMode());
		shippingMethodsCombo.setDisabled(isInViewMode());

		dueDateItem.setDisabled(isInViewMode());
		despatchDateItem.setDisabled(isInViewMode());

		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		vendorCombo.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());

		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return null;
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().purchaseOrder();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
		vendorAccountTransactionTable.removeAllRecords();
		vendorItemTransactionTable.removeAllRecords();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billtoAreaItem.setTabIndex(4);
		statusSelect.setTabIndex(5);
		transactionDateItem.setTabIndex(6);
		transactionNumber.setTabIndex(7);
		purchaseOrderText.setTabIndex(8);
		payTermsSelect.setTabIndex(9);
		dueDateItem.setTabIndex(10);
		despatchDateItem.setTabIndex(11);
		deliveryDateItem.setTabIndex(12);
		memoTextAreaItem.setTabIndex(13);
		// menuButton.setTabIndex(14);
		saveAndCloseButton.setTabIndex(15);
		saveAndNewButton.setTabIndex(16);
		cancelButton.setTabIndex(17);
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		vendorItemTransactionTable.updateAmountsFromGUI();
		vendorAccountTransactionTable.updateAmountsFromGUI();
	}

}
