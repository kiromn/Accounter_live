package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {
	private static final String QUANITY = "Quantity";
	private static final String ITEM = "Item";
	private static final String UNITPTICE = "UnitPrice";
	private static final String DISCOUNT = "Discount";
	private static final String TAXCODE = "TaxCode";
	private static final String TAX = "Tax";
	private static final String DESCRIPTION = "Description";
	protected static final String IS_BILLABLE = "isBillable";
	protected static final String ITEM_CUSTOMER = "itemcustomer";

	public TransactionItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	public abstract boolean isSales();

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true,
				new ChangeListner<Item>() {

					@Override
					public void onSelection(Item item) {
						if (item != null) {
							if (isSales()) {
								get(UNITPTICE).setValue(

								item.getSalesPrice() / getCurrencyFactor());
							} else {
								get(UNITPTICE).setValue(

								item.getPurchasePrice() / getCurrencyFactor());
							}
							get(TAXCODE).setValue(
									get(TAXCODE).getValue() == null ? item
											.getTaxCode() : get(TAXCODE)
											.getValue());
							get(TAX).setValue(item.isTaxable());
						}

					}
				}, isSales()) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems(context);
			}

		});

		list.add(new AmountRequirement(QUANITY, getMessages().pleaseEnter(
				getMessages().quantity()), getMessages().quantity(), true, true));

		list.add(new CurrencyAmountRequirement(UNITPTICE, getMessages()
				.pleaseEnter(getMessages().unitPrice()), getMessages()
				.unitPrice(), false, true) {

			@Override
			protected String getFormalName() {
				return getCurrency().getFormalName();
			}
		});

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnter(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax() && isSales() ? false
						: getPreferences().isTrackPaidTax()
								&& getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> taxCodes = context.getCompany().getTaxCodes();
				List<TAXCode> clientcodes = new ArrayList<TAXCode>();
				for (TAXCode taxCode : taxCodes) {
					if (taxCode.isActive()) {
						clientcodes.add(taxCode);
					}
				}
				return clientcodes;
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(TAX, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax() && isSales() ? false
						: getPreferences().isTrackPaidTax()
								&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}
		});

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));
	}

	public abstract List<Item> getItems(Context context);

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Item clientItem = get(ITEM).getValue();
		obj.setItem(clientItem.getID());
		obj.getQuantity().setValue((Double) get(QUANITY).getValue());
		obj.setUnitPrice((Double) get(UNITPTICE).getValue());
		obj.setDiscount((Double) get(DISCOUNT).getValue());
		TAXCode taxCode = get(TAXCODE).getValue();
		if (taxCode != null) {
			obj.setTaxCode(taxCode.getID());
		}
		obj.setTaxable((Boolean) get(TAX).getValue());
		if (get(IS_BILLABLE) != null) {
			Boolean isBillable = get(IS_BILLABLE).getValue();
			obj.setIsBillable(isBillable);
			Customer customer = get(ITEM_CUSTOMER).getValue();
			if (customer != null) {
				obj.setCustomer(customer.getID());
			}
		}
		obj.setDescription((String) get(DESCRIPTION).getValue());
		double lt = obj.getQuantity().getValue() * obj.getUnitPrice();
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(QUANITY).setDefaultValue(obj.getQuantity().getValue());
		get(UNITPTICE).setValue(obj.getUnitPrice());

		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		get(TAXCODE).setValue(
				CommandUtils.getServerObjectById(obj.getTaxCode(),
						AccounterCoreType.TAX_CODE));
		get(TAX).setDefaultValue(obj.isTaxable());
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
		if (get(IS_BILLABLE) != null) {
			get(IS_BILLABLE).setDefaultValue(obj.isBillable());
			get(ITEM_CUSTOMER).setValue(
					CommandUtils.getServerObjectById(obj.getCustomer(),
							AccounterCoreType.CUSTOMER));
		}
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem clientTransactionItem = new ClientTransactionItem();
		clientTransactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		if (isSales() ? false : getPreferences().isTrackPaidTax())
			clientTransactionItem.setTaxable(true);
		clientTransactionItem.setIsBillable(false);
		ClientQuantity clientQuantity = new ClientQuantity();
		clientQuantity.setValue(1.0);
		clientTransactionItem.setQuantity(clientQuantity);
		clientTransactionItem.setDiscount(0.0);
		clientTransactionItem.setLineTotal(0d);
		return clientTransactionItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		ClientItem iAccounterCore = (ClientItem) CommandUtils
				.getClientObjectById(t.getItem(), AccounterCoreType.ITEM,
						getCompanyId());
		if (iAccounterCore != null) {
			record.add(getMessages().name(), iAccounterCore.getDisplayName());
		}
		record.add(getMessages().quantity(), t.getQuantity().getValue());
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		record.add(
				getMessages().unitPrice() + "(" + formalName + ")",
				Global.get().toCurrencyFormat(
						t.getUnitPrice() == null ? 0d : t.getUnitPrice()));
		if (getPreferences().isTrackTax() && isSales() ? false
				: getPreferences().isTrackPaidTax()) {
			if (getPreferences().isTaxPerDetailLine()) {
				record.add(getMessages().taxCode(),
						((ClientTAXCode) (CommandUtils.getClientObjectById(
								t.getTaxCode(), AccounterCoreType.TAX_CODE,
								getCompanyId()))).getDisplayName());
			} else {
				if (t.isTaxable()) {
					record.add(getMessages().taxable());
				} else {
					record.add(getMessages().taxExempt());
				}
			}
		}
		record.add(getMessages().totalPrice() + "(" + formalName + ")",
				t.getLineTotal());
		record.add(getMessages().description(), t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientTransactionItem> items = getValue();
		return items.isEmpty() ? getMessages().addOf(getMessages().items())
				: getMessages().addMore(getMessages().items());
	}

	@Override
	protected boolean contains(List<ClientTransactionItem> oldValues,
			ClientTransactionItem t) {
		for (ClientTransactionItem clientTransactionItem : oldValues) {
			if (clientTransactionItem.getItem() != 0
					&& clientTransactionItem.getItem() == t.getItem()) {
				return true;
			}
		}
		return false;
	}

	protected abstract double getCurrencyFactor();

	protected abstract Payee getPayee();

	protected Currency getCurrency() {
		return getPayee().getCurrency();
	}
}
