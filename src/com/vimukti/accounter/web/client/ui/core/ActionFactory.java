package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.BankingHomeAction;
import com.vimukti.accounter.web.client.ui.banking.BuyChecksAndFormsAction;
import com.vimukti.accounter.web.client.ui.banking.ChartsOfAccountsAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.EnterPaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.ImportBankFilesAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MatchTrasactionsAction;
import com.vimukti.accounter.web.client.ui.banking.NewBankAccountAction;
import com.vimukti.accounter.web.client.ui.banking.NewPayeeAction;
import com.vimukti.accounter.web.client.ui.banking.PaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.PrintChecksAction;
import com.vimukti.accounter.web.client.ui.banking.ServicesOverviewAction;
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.TransferFundsAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.company.AddEditSalesTaxCodeAction;
import com.vimukti.accounter.web.client.ui.company.ChangePasswordAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CompanyInfoAction;
import com.vimukti.accounter.web.client.ui.company.CountryRegionListAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CustomerGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.company.FinanceLogAction;
import com.vimukti.accounter.web.client.ui.company.FormLayoutsListAction;
import com.vimukti.accounter.web.client.ui.company.IntegrateWithBusinessContactManagerAction;
import com.vimukti.accounter.web.client.ui.company.ItemGroupListAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.JournalEntriesAction;
import com.vimukti.accounter.web.client.ui.company.MakeActiveAction;
import com.vimukti.accounter.web.client.ui.company.MakeInActiveAction;
import com.vimukti.accounter.web.client.ui.company.ManageFiscalYearAction;
import com.vimukti.accounter.web.client.ui.company.ManageItemTaxAction;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.company.MergeCustomersAction;
import com.vimukti.accounter.web.client.ui.company.MergeFinancialAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeItemsAction;
import com.vimukti.accounter.web.client.ui.company.MergeVendorsAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewCashBasisJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewCompanyAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.company.NewbankAction;
import com.vimukti.accounter.web.client.ui.company.PaySalesTaxAction;
import com.vimukti.accounter.web.client.ui.company.PayTypeListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentTermListAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.company.PriceLevelListAction;
import com.vimukti.accounter.web.client.ui.company.PurchaseItemsAction;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListsAction;
import com.vimukti.accounter.web.client.ui.company.ShippingMethodListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingTermListAction;
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.VendorGroupListAction;
import com.vimukti.accounter.web.client.ui.customers.CreateStatementAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomersHomeAction;
import com.vimukti.accounter.web.client.ui.customers.InvoicesAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerItemAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.QuotesAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesPersonAction;
import com.vimukti.accounter.web.client.ui.customers.TaxDialogAction;
import com.vimukti.accounter.web.client.ui.fixedassets.DisposingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SellingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.BalanceSheetAction;
import com.vimukti.accounter.web.client.ui.reports.CashFlowStatementAction;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.reports.StatementReportAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.settings.AutomaticSequenceAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionBalancesAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionDateAction;
import com.vimukti.accounter.web.client.ui.settings.CopyThemeAction;
import com.vimukti.accounter.web.client.ui.settings.CustomThemeAction;
import com.vimukti.accounter.web.client.ui.settings.DeleteThemeAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InviteUserAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;
import com.vimukti.accounter.web.client.ui.settings.UsersAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseTransferAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseViewAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseListAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseTransferListAction;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;
import com.vimukti.accounter.web.client.ui.vat.CreateTaxesAction;
import com.vimukti.accounter.web.client.ui.vat.FileVatAction;
import com.vimukti.accounter.web.client.ui.vat.ManageTAXCodesListAction;
import com.vimukti.accounter.web.client.ui.vat.ManageVATGroupListAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayVATAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.VatGroupAction;
import com.vimukti.accounter.web.client.ui.vat.VatItemListAction;
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCheckAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorItemAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.PreviousClaimAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

public class ActionFactory {
	public static AccounterConstants actionsConstants = Accounter.constants();

	private static AccounterConstants messages = Accounter.constants();

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction(messages.labelTitle());
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction(Accounter.constants().inventoryItems());

	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction(Accounter.constants()
				.conversionBalance());
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction(Accounter.constants()
				.invoiceBranding());
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction(messages.newBrandThemeLabel());

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction(Accounter.constants().conversionDate());
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction(Accounter.constants()
				.automaticSequencing());
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction(messages.newBrandThemeLabel());
	}

	public static UsersAction getUsersAction() {
		return new UsersAction(messages.users());
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction(messages.inviteUser());
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction(messages.users());
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction(messages.copyTheme());
	}

	public static WareHouseViewAction getWareHouseViewAction() {
		return new WareHouseViewAction("Ware House");
	}

	public static WareHouseTransferAction getWareHouseTransferAction() {
		return new WareHouseTransferAction(messages.wareHouseTransfer());
	}

	public static WarehouseListAction getWarehouseListAction() {
		return new WarehouseListAction(messages.warehouseList());
	}

	public static WarehouseTransferListAction getWarehouseTransferListAction() {
		return new WarehouseTransferListAction(messages.warehouseTransferList());
	}

	// Banking action factory

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction(actionsConstants.bankingHome());
	}

	public static NewBankAccountAction getNewBankAccountAction() {
		return new NewBankAccountAction(actionsConstants.newBankCategory());
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction(actionsConstants.accountRegister());

	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction(actionsConstants.writeCheck());
	}

	public static WriteChecksAction getWriteChecksAction(
			ClientWriteCheck writeCheck, AsyncCallback<Object> callBackObject) {
		return new WriteChecksAction(actionsConstants.writeCheck(), writeCheck,
				callBackObject);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction(actionsConstants.makeDeposit());
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientMakeDeposit makeDeposit, AsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(actionsConstants.makeDeposit(),
				makeDeposit, callBackObject);
	}

	public static TransferFundsAction getTransferFundsAction() {
		return new TransferFundsAction(actionsConstants.transferFunds());
	}

	public static TransferFundsAction getTransferFundsAction(
			ClientTransferFund transferFund,
			AsyncCallback<Object> callBackObject) {
		return new TransferFundsAction(actionsConstants.transferFunds(),
				transferFund, callBackObject);
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction(actionsConstants.enterPayments());
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction(actionsConstants.syncOnlinePayees());
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction(actionsConstants.importBankFiles());
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction(actionsConstants.creditCardCharge());
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction(actionsConstants.printChecks());
	}

	public static ChartsOfAccountsAction getChartsOfAccountsAction() {
		return new ChartsOfAccountsAction(actionsConstants.chartOfAccounts());
	}

	public static PaymentsAction getPaymentsAction() {
		return new PaymentsAction(actionsConstants.payments());
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction(actionsConstants.matchTrasactions());
	}

	// ActionFactory

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction(actionsConstants.home());
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction(actionsConstants.makeActive());

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction(actionsConstants.makeInActive());

	}

	public static NewCompanyAction getNewCompanyAction() {
		return new NewCompanyAction(actionsConstants.newCompany());
	}

	public static CompanyInfoAction getCompanyInformationAction() {
		return new CompanyInfoAction(actionsConstants.companyinfo());
	}

	public static PreferencesAction getPreferencesAction() {
		return new PreferencesAction(actionsConstants.companyPreferences());
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction(actionsConstants
				.integrateWithBusinessContactManager());
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction(actionsConstants.newJournalEntry());
	}

	public static NewCashBasisJournalEntryAction getNewCashBasisJournalEntryAction() {
		return new NewCashBasisJournalEntryAction(actionsConstants
				.newCashBasisJournalEntry());
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction(actionsConstants.newAccount());
	}

	public static MergeCustomersAction getMergeCustomersAction() {
		return new MergeCustomersAction(actionsConstants.mergeCustomers());
	}

	public static MergeVendorsAction getMergeVendorsAction() {
		return new MergeVendorsAction(actionsConstants.mergeSuppliers());
	}

	public static MergeItemsAction getMergeItemsAction() {
		return new MergeItemsAction(actionsConstants.mergeItems());
	}

	public static MergeFinancialAccountsAction getMergeFinancialAccountsAction() {
		return new MergeFinancialAccountsAction(actionsConstants
				.mergeFinancialAccounts());
	}

	public static ManageSalesTaxGroupsAction getManageSalesTaxGroupsAction() {
		String text;
		if (Accounter.getUser().canDoInvoiceTransactions())
			text = actionsConstants.manageSalesGroups();
		else
			text = actionsConstants.salesTaxGroups();
		return new ManageSalesTaxGroupsAction(text);
	}

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = actionsConstants.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static ManageItemTaxAction getManageItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.manageItemTax());
	}

	public static PaySalesTaxAction getPaySalesTaxAction() {
		String constant = null;
		constant = actionsConstants.paySalesTax();
		return new PaySalesTaxAction(constant);
	}

	public static SalesTaxLiabilityAction getViewSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(actionsConstants.salesTaxLiability());
	}

	// public static NewTaxAgencyAction getNewTaxAgencyAction() {
	// return new NewTaxAgencyAction(actionsConstants.newTaxAgency());
	// }

	public static CustomerGroupListAction getCustomerGroupListAction() {
		return new CustomerGroupListAction(actionsConstants.customerGroupList());
	}

	public static VendorGroupListAction getVendorGroupListAction() {
		return new VendorGroupListAction(UIUtils.getVendorString(
				actionsConstants.supplierGroupList(), actionsConstants
						.vendorGroupList()));
	}

	public static PaymentTermListAction getPaymentTermListAction() {
		return new PaymentTermListAction(actionsConstants.paymentTermList());
	}

	public static ShippingMethodListAction getShippingMethodListAction() {
		return new ShippingMethodListAction(actionsConstants
				.shippingMethodList());
	}

	public static ShippingTermListAction getShippingTermListAction() {
		return new ShippingTermListAction(actionsConstants.shippingTermList());
	}

	public static PriceLevelListAction getPriceLevelListAction() {
		return new PriceLevelListAction(actionsConstants.priceLevelList());
	}

	public static ItemGroupListAction getItemGroupListAction() {
		return new ItemGroupListAction(actionsConstants.itemGroupList());
	}

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction(actionsConstants.creditRatingList());
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction(actionsConstants.countryRegionList());
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction(actionsConstants.formLayoutsList());
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction(actionsConstants.payTypeList());
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction(actionsConstants.manageFiscalYear());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction(actionsConstants
				.accounterCategoryList());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(actionsConstants
				.accounterCategoryList(), accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction(actionsConstants.salesPersons());
	}

	public static JournalEntriesAction getJournalEntriesAction() {
		return new JournalEntriesAction(actionsConstants.journalEntries());
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction(actionsConstants.newBank());
	}

	public static ManageItemTaxAction getNewItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.newItemTax());
	}

	public static NewItemAction getNewItemAction() {
		return new NewItemAction(actionsConstants.newItem());
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AsyncCallback<Object> callback) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", item, callback);
	// }

	public static NewPayeeAction getNewPayeeAction() {
		return new NewPayeeAction("", null);

	}

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction(Accounter.constants().depreciation());

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = Accounter.constants().newVATAgency();

		else
			flag = Accounter.constants().newTAXAgency();

		return new NewTAXAgencyAction(flag);
	}

	public static FinanceLogAction getFinanceLogAction() {
		return new FinanceLogAction(Accounter.constants().showLog());
	}

	public static ManageSalesTaxItemsAction getManageSalesTaxItemsAction() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = actionsConstants.manageSalesItems();
		else
			constant = actionsConstants.salesTaxItems();
		return new ManageSalesTaxItemsAction(constant);
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = actionsConstants.VATAdjustment();

		else
			flag = actionsConstants.taxAdjustment();
		return new AdjustTAXAction(flag);
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction(actionsConstants.changePassword());

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction(actionsConstants.userDetails());
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(actionsConstants.forgetPassword());
	//
	// }

	// Customers actions Factory

	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction(actionsConstants.newCustomer());
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer, AsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(actionsConstants.newCustomer(), customer,
				callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction(actionsConstants.customersHome());
	}

	public static AddEditSalesTaxCodeAction getAddEditSalesTaxCodeAction() {
		String constant = null;
		if (Accounter.getCompany().getAccountingType() == 1)
			constant = actionsConstants.newVATCode();
		else
			constant = actionsConstants.newTaxCode();
		return new AddEditSalesTaxCodeAction(constant);
	}

	public static NewQuoteAction getNewQuoteAction() {
		return new NewQuoteAction(actionsConstants.newQuote());
	}

	public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
			AsyncCallback<Object> callBackObject) {
		return new NewQuoteAction(actionsConstants.newQuote(), quote,
				callBackObject);
	}

	public static NewItemAction getNewCustomerItemAction() {
		return new NewCustomerItemAction(actionsConstants.newItem());
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AsyncCallback<Object> callBackObject) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/customers/new_item.png", item, callBackObject);
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction(actionsConstants.tax());
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction(actionsConstants.newInvoice());
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(actionsConstants.newInvoice(), invoice,
				callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction(actionsConstants.newCashSale());
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales, AsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(actionsConstants.newCashSale(), cashSales,
				callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction(actionsConstants.newCreditNotes());
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
			ClientCustomerCreditMemo creditMemo,
			AsyncCallback<Object> callBackObject) {
		return new NewCreditsAndRefundsAction(actionsConstants
				.newCreditsAndRefunds(), creditMemo, callBackObject);
	}

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction("Receive Payment");
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction("Receive Payment", receivePayment,
				callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction(actionsConstants.customerRefund());
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(actionsConstants.customerRefund(),
	// "/images/icons/customers/customer_refunds.png", customerRefund,
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction(actionsConstants.createStatement());
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction(actionsConstants.customers());
	}

	public static ItemsAction getItemsAction() {
		return new ItemsAction(actionsConstants.items(), Accounter.constants()
				.customer());
	}

	public static QuotesAction getQuotesAction() {
		return new QuotesAction(actionsConstants.quotes());
	}

	public static ReceivedPaymentsAction getReceivedPaymentsAction() {
		return new ReceivedPaymentsAction(actionsConstants.receivedPayments());
	}

	public static InvoicesAction getInvoicesAction(String viewType) {
		return new InvoicesAction(actionsConstants.invoices(), viewType);
	}

	public static CustomerRefundsAction getCustomerRefundsAction() {
		return new CustomerRefundsAction(actionsConstants.customerRefunds());
	}

	public static SalesOrderAction getSalesOrderAction() {
		return new SalesOrderAction(actionsConstants.newSalesOrder());
	}

	public static SalesOrderListAction getSalesOrderListAction() {
		return new SalesOrderListAction(actionsConstants.salesOrderList());
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction(actionsConstants.newSalesPerson());
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction(actionsConstants.salesPersons());
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction(actionsConstants.customerPrePayment());
	}

	public static BrandingThemeComboAction getBrandingThemeComboAction() {
		return new BrandingThemeComboAction(actionsConstants
				.brandingThemeCombo());
	}

	public static PaymentDialogAction getPaymentDialogAction() {
		return new PaymentDialogAction(actionsConstants.payments());
	}

	// Fixed Assests action factory

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction(actionsConstants.newFixedAsset());
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction(actionsConstants
				.sellingRegisteredItem());
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction(actionsConstants
				.disposingRegisteredItem());
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction(actionsConstants.pendingItemsList());

	}

	public static Action getRegisteredItemsListAction() {
		return new RegisteredItemsListAction(actionsConstants
				.registeredItemsList());
	}

	public static Action getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction(actionsConstants
				.soldDisposedItems());
	}

	public static Action getHistoryListAction() {
		return new HistoryListAction(actionsConstants.historyList());
	}

	// Purchase order action

	// public static PurchaseOrderAction getPurchaseOrderAction() {
	// return new PurchaseOrderAction("Purchase Order",
	// "/images/Purchase-order.png");
	// }

	// public static PurchaseOrderListAction getPurchaseOrderListAction() {
	// return new PurchaseOrderListAction("PurchaseOrder List",
	// "/images/Purchase-order-list.png");
	// }

	public static PurchaseOpenOrderAction getPurchaseOpenOrderListAction() {
		return new PurchaseOpenOrderAction("PurchaseOrder Report");
	}

	// reports action factory

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction(actionsConstants.reportsHome());
	}

	public static VendorsListAction getVendorListAction() {
		return new VendorsListAction(UIUtils.getVendorString(Accounter
				.constants().suppliersList(), Accounter.constants()
				.vendorsList()));
	}

	public static BalanceSheetAction getBalanceSheetAction() {
		return new BalanceSheetAction(Accounter.constants().balanceSheet());
	}

	public static CashFlowStatementAction getCashFlowStatementAction() {
		return new CashFlowStatementAction(Accounter.constants()
				.cashFlowReport());
	}

	public static TrialBalanceAction getTrialBalanceAction() {
		return new TrialBalanceAction(Accounter.constants().trialBalance());
	}

	public static TransactionDetailByAccountAction getTransactionDetailByAccountAction() {
		return new TransactionDetailByAccountAction(Accounter.constants()
				.transactionDetailByAccount());
	}

	public static GLReportAction getGlReportAction() {
		return new GLReportAction(Accounter.constants().generalLedgerReport());
	}

	public static SalesTaxLiabilityAction getSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(Accounter.constants()
				.salesTaxLiability());
	}

	public static TransactionDetailByTaxItemAction getTransactionDetailByTaxItemAction() {
		return new TransactionDetailByTaxItemAction(Accounter.constants()
				.transactionDetailByTaxItem());
	}

	// public static YtdProfitComparedToLastYearAction
	// getYtdProfitComparedToLastYearAction() {
	// return new YtdProfitComparedToLastYearAction(
	// "Ytd Profit Compared To Last Year","/images/icons/report/reports.png");
	// }

	public static ARAgingDetailAction getArAgingDetailAction() {
		return new ARAgingDetailAction(Accounter.constants().ARAgeingDetail());
	}

	public static CustomerTransactionHistoryAction getCustomerTransactionHistoryAction() {
		return new CustomerTransactionHistoryAction(Accounter.constants()
				.customerTransactionHistory());
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction(Accounter.constants()
				.mostProfitableCustomers());
	}

	public static SalesByCustomerSummaryAction getSalesByCustomerSummaryAction() {
		return new SalesByCustomerSummaryAction(Accounter.constants()
				.salesByCustomerSummary());
	}

	public static SalesByCustomerDetailAction getSalesByCustomerDetailAction() {
		return new SalesByCustomerDetailAction(Accounter.constants()
				.salesByCustomerDetail());
	}

	public static SalesByItemSummaryAction getSalesByItemSummmaryAction() {
		return new SalesByItemSummaryAction(Accounter.constants()
				.salesByItemSummary());
	}

	public static SalesByItemDetailAction getSalesByItemDetailAction() {
		return new SalesByItemDetailAction(Accounter.constants()
				.salesByItemDetail());
	}

	// public static YtdSalesComparedToLastYearAction
	// getYtdSalesComparedToLastYearAction() {
	// return new YtdSalesComparedToLastYearAction(
	// "Ytd Sales Compared To Last Year","/images/icons/report/reports.png");
	// }

	public static APAgingDetailAction getAorpAgingDetailAction() {
		return new APAgingDetailAction(Accounter.constants().APAgeingDetail());
	}

	public static VendorTransactionHistoryAction getVendorTransactionHistoryAction() {
		return new VendorTransactionHistoryAction(UIUtils.getVendorString(
				Accounter.constants().supplierTransactionHistory(), Accounter
						.constants().vendorTransactionHistory()));
	}

	// public static AmountsDueToVendorsAction getAmountsDueToVendorsAction() {
	// return new AmountsDueToVendorsAction("Amounts Due To"
	// + UIUtils.getVendorString("Suppliers", "Vendors"),
	// "/images/icons/report/reports.png");
	// }

	public static ProfitAndLossAction getProfitAndLossAction() {
		return new ProfitAndLossAction(actionsConstants.profitAndLoss());
	}

	public static SalesByItemSummaryAction getSalesByItemSummaryAction() {
		return new SalesByItemSummaryAction(Accounter.constants()
				.salesByItemSummary());
	}

	public static PurchaseByVendorSummaryAction getPurchaseByVendorSummaryAction() {
		return new PurchaseByVendorSummaryAction(UIUtils.getVendorString(
				Accounter.constants().purchaseBySupplierSummary(), Accounter
						.constants().purchaseByVendorSummary()));
	}

	public static PurchaseByVendorDetailsAction getPurchaseByVendorDetailAction() {
		return new PurchaseByVendorDetailsAction(UIUtils.getVendorString(
				Accounter.constants().purchaseBySupplierDetail(), Accounter
						.constants().purchaseByVendorDetail()));
	}

	public static PurchaseByItemSummaryAction getPurchaseByItemSummaryAction() {
		return new PurchaseByItemSummaryAction(Accounter.constants()
				.purchaseByItemSummary());
	}

	public static PurchaseByItemDetailsAction getPurchaseByItemAction() {
		return new PurchaseByItemDetailsAction(Accounter.constants()
				.purchaseByItemDetail());
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderAction() {
		return new PurchaseOpenOrderAction(Accounter.constants()
				.purchaseOrderReport());
	}

	public static PurchaseClosedOrderAction getPurchaseClosedOrderAction() {
		return new PurchaseClosedOrderAction(Accounter.constants()
				.purchaseClosedOrder());
	}

	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction(Accounter.constants()
				.salesOrderReport());
	}

	public static SalesClosedOrderAction getSalesCloseOrderAction() {
		return new SalesClosedOrderAction(Accounter.constants()
				.salesCloseOrder());
	}

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction(Accounter.constants().VATDetail());
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction(Accounter.constants()
				.priorVATReturns());
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction(Accounter.constants().VAT100());
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction(Accounter.constants()
				.uncategorisedVATAmounts());
	}

	public static VATItemSummaryReportAction getVATItemSummaryReportAction() {
		return new VATItemSummaryReportAction(Accounter.constants()
				.VATItemSummary());
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction(Accounter.constants().ecSalesList());
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction(Accounter.constants()
				.ecSalesListDetailReport());
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction(Accounter.constants()
				.reverseChargeList());
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction(Accounter.constants()
				.reverseChargeListDetailReport());
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction(Accounter.constants()
				.VATItemDetailReport());
	}

	public static ARAgingSummaryReportAction getArAgingSummaryReportAction() {
		return new ARAgingSummaryReportAction(Accounter.constants()
				.ARAgeingSummary());
	}

	public static Action getAorpAgingSummaryReportAction() {
		return new APAgingSummaryReportAction(Accounter.constants()
				.APAgeingSummary());
	}

	public static ExpenseReportAction getExpenseReportAction() {
		return new ExpenseReportAction(Accounter.constants().expenseReport());
	}

	public static DepositDetailAction getDetailReportAction() {
		return new DepositDetailAction(Accounter.constants().depositDetail());
	}

	public static CheckDetailReportAction getCheckDetailReport() {
		return new CheckDetailReportAction(Accounter.constants().checkDetail(),
				"");
	}

	public static Action getStatementReport() {
		return new StatementReportAction(Accounter.constants()
				.customerStatement());
	}

	// sales order action factory

	// public static SalesOrderAction getSalesOrderAction() {
	// return new SalesOrderAction("Sales Order",
	// "/images/Sales-order.png");
	// }

	// public static SalesOrderListAction getSalesOrderListAction() {
	// return new SalesOrderListAction("SalesOrder List",
	// "/images/Sales-order-list.png");
	// }

	// public static SalesOpenOrderAction getSalesOpenOrderAction() {
	// return new SalesOpenOrderAction("SalesOrder Report",
	// "/images/icons/report/reports.png");
	// }

	// vat action factory

	public static NewVatItemAction getNewVatItemAction() {
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = actionsConstants.newVATItem();
		else
			flag = actionsConstants.newTaxItem();
		return new NewVatItemAction(flag);
	}

	public static FileVatAction getFileVatAction() {
		return new FileVatAction(actionsConstants.fileVAT());
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction(actionsConstants.VATItemList());
	}

	public static VatGroupAction getVatGroupAction() {
		return new VatGroupAction(actionsConstants.newVATGroup());
	}

	public static Action getTAXCodeListAction() {
		return new ManageTAXCodesListAction(actionsConstants.VATCodeList());
	}

	public static Action getNewTAXCodeAction() {
		return new NewTAXCodeAction(actionsConstants.newVATCode());
	}

	public static Action getCreateTaxesAction() {
		return new CreateTaxesAction(actionsConstants.createTaxes());
	}

	// public static Action getManageVATCodeAction() {
	// return new ManageVATCodeAction("Manage VAT");
	// }

	// public static NewTAXAgencyAction getNewTAXAgencyAction() {
	// return new NewTAXAgencyAction(actionsConstants.newVATAgency());
	// }

	public static ManageVATGroupListAction getManageVATGroupListAction() {
		return new ManageVATGroupListAction(actionsConstants.VATGroupList());
	}

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(Accounter.constants().VATAdjustment());

	}

	public static PayVATAction getpayVATAction() {
		return new PayVATAction(Accounter.constants().payVAT());

	}

	public static Action getreceiveVATAction() {
		return new ReceiveVATAction(Accounter.constants().receiveVAT());
	}

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction(UIUtils
				.getVendorString(Accounter.constants().supplierhome(),
						Accounter.constants().vendorHome()));
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction(UIUtils.getVendorString(Accounter
				.constants().newSupplier(), Accounter.constants().newVendor()));
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AsyncCallback<Object> callback) {
		return new NewVendorAction(UIUtils.getVendorString(Accounter
				.constants().newSupplier(), Accounter.constants().newVendor()),
				vendor, callback);
	}

	public static PurchaseItemsAction getPurchaseItemsAction() {
		return new PurchaseItemsAction(actionsConstants.items(), UIUtils
				.getVendorString(Accounter.constants().supplier(), Accounter
						.constants().vendor()));
	}

	// public static NewItemAction getNewItemAction() {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", null);
	// }
	//
	// public static NewItemAction getNewItemAction(Item item,
	// AsyncCallback<Object> callback, AbstractBaseView view) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", view, item, callback);
	// }

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase());
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase, AsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase(),
				cashPurchase, callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction(UIUtils.getVendorString(Accounter
				.constants().supplierCredit(), Accounter.constants()
				.vendorCredit()));
	}

	public static NewItemAction getNewVendorItemAction() {
		return new NewVendorItemAction(actionsConstants.newItem());
	}

	public static NewCreditMemoAction getNewCreditMemoAction(
			ClientVendorCreditMemo vendorCreditMemo,
			AsyncCallback<Object> callBack) {
		return new NewCreditMemoAction(actionsConstants.newCreditMemo(),
				vendorCreditMemo, callBack);
	}

	public static NewCheckAction getNewCheckAction() {
		return new NewCheckAction(actionsConstants.newCheck());
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction(actionsConstants.enterBills());
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction(actionsConstants.payBills());
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction(actionsConstants.issuePayments());
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction(UIUtils.getVendorString(Accounter
				.constants().supplierPrePayment(), Accounter.constants()
				.vendorPrePayment()));
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction(actionsConstants.recordExpenses());
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction(actionsConstants.servicesOverview());
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction(
				actionsConstants.buyChecksAndForms(), "");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction(UIUtils.getVendorString(Accounter
				.constants().suppliers(), Accounter.constants().vendors()));
	}

	// public static Item getItemAction() {
	// return new ItemListAction(actionsConstants.items());
	// }

	public static BillsAction getBillsAction() {
		return new BillsAction(actionsConstants.billsAndItemReceipts());
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(actionsConstants.recordExpenses(), viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {

		return new VendorPaymentsListAction(UIUtils.getVendorString(Accounter
				.constants().supplierPayments(), Accounter.constants()
				.vendorPayments()));
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction(Accounter.constants().purchaseOrder());
	}

	public static PurchaseOrderListAction getPurchaseOrderListAction() {
		return new PurchaseOrderListAction(Accounter.constants()
				.purchaseOrderList());
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction(Accounter.constants().itemReceipt());
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction(Accounter.constants().cash());
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction(Accounter.constants().employee());
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction(Accounter.constants().creditCard());
	}

	public static AwaitingAuthorisationAction getAwaitingAuthorisationAction() {
		return new AwaitingAuthorisationAction(Accounter.constants()
				.awaitingAuthorisation());

	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction(Accounter.constants().previousClaims());

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(Accounter.constants().expenseClaims(),
				selectedTab);
	}

}
