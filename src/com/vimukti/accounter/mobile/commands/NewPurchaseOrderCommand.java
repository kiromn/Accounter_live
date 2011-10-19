package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccountTransaction;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewPurchaseOrderCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";
	private static final String STATUS = "status";
	private static final String NAME = "name";
	private static final int CONTACTS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("vendor", false, true));
		list.add(new Requirement("status", false, true));
		list.add(new ObjectListRequirement("accounts", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("total", true, true));
			}
		});

		list.add(new ObjectListRequirement("items", false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("total", true, true));
			}
		});

		list.add(new Requirement("contact", true, false));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("billto", true, false));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("orderno", true, true));
		list.add(new Requirement("vendororderno", true, true));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("duedate", true, true));
		list.add(new Requirement("dispatchdate", true, true));
		list.add(new Requirement("receiveddate", true, true));

	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
			if (process.equals(ACCOUNTS_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		Result makeResult = context.makeResult();
		makeResult
				.add(" PurchaseOrder is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = createSupplierRequirement(context, list, VENDOR);
		if (result != null) {
			return result;
		}

		result = statusRequirement(context, list, STATUS);
		if (result != null) {
			return result;
		}
		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_EXPENSE
								&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
								&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						} else {
							return false;
						}
					}
				});
		if (result != null) {
			return result;
		}
		setDefaultsValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;

	}

	private void setDefaultsValues() {

		get("date").setDefaultValue(new Date());
		get("duedate").setDefaultValue(new Date());
		get("dispatchdate").setDefaultValue(new Date());
		get("receiveddate").setDefaultValue(new Date());

	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {

		ClientPurchaseOrder newPurchaseOrder = new ClientPurchaseOrder();

		ClientVendor vendor = get("vendor").getValue();

		newPurchaseOrder.setVendor(vendor.getID());

		newPurchaseOrder.setPhone((String) get("phone").getValue());

		newPurchaseOrder.setStatus((Integer) get(STATUS).getValue());

		newPurchaseOrder.setNumber((String) get(ORDER_NO).getValue());

		ClientPaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		newPurchaseOrder.setPaymentTerm(newPaymentTerms.getID());

		Date dueDate = get("duedate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(dueDate).getDate());

		Date receivedDate = get("receiveddate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(receivedDate).getDate());

		Date dispatchDate = get("dispatchdate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(dispatchDate).getDate());

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);
		newPurchaseOrder.setTransactionItems(items);
		
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		create(newPurchaseOrder, context);
	}

	/**
	 * Creating optional results
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions
	 * @param list
	 * @return
	 */
	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		Requirement vendrReq = get("vendor");
		ClientVendor vendor = (ClientVendor) vendrReq.getValue();

		Result result = contactRequirement(context, list, selection, vendor);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (getClientCompany().getPreferences().isDoProductShipMents()) {
			result = shippingMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = shippingTermsRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		result = dateOptionalRequirement(context, list, "duedate",
				"Enter date", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, "dispatchdate",
				"Enter dispatchdate", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, "receiveddate",
				"Enter receiveddate", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, "orderno",
				"Enter order Number");
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, "phone",
				"Enter order Number");
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection,
				"vendororderno", "Enter vendor order number");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);

		return makeResult;
	}

	/**
	 * Contact requirement checking
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @param vendor
	 * @return
	 */
	private Result contactRequirement(Context context, ResultList list,
			Object selection, Vendor vendor) {
		Object contactObj = context.getSelection(CONTACTS);
		Requirement contactReq = get("contact");
		Contact contact = (Contact) contactReq.getValue();
		if (selection == contact) {
			return contactList(context, vendor, contact);

		}
		if (contactObj != null) {
			contact = (Contact) contactObj;
			contactReq.setValue(contact);
		}

		Record contactRecord = new Record(contact);
		contactRecord.add("Name", "Customer Contact");
		contactRecord.add("Value", contact.getName());
		list.add(contactRecord);
		return null;
	}

	private Result contactList(Context context, Vendor vendor,
			Contact oldContact) {
		Set<Contact> contacts = vendor.getContacts();
		ResultList list = new ResultList(CONTACTS);
		int num = 0;
		if (oldContact != null) {
			// list.add(createContactRecord(oldContact));
			num++;
		}
		for (Contact contact : contacts) {
			if (contact != oldContact) {
				// list.add(createContactRecord(contact));
				num++;
			}
			if (num == CONTACTS_TO_SHOW) {
				break;
			}
		}

		Result result = context.makeResult();
		result.add("Select " + vendor.getName() + "'s Contact");
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Contact");
		result.add(commandList);

		return result;
	}

}
