package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author SaiPrasad N
 * 
 */
public class CreateQuoteCommand extends AbstractTransactionCommand {

	private static final String PHONE = "phone";
	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String EXPIRATION_DATE = "expirationDate";
	private static final String PAYMENT_TERMS = "paymentTerms";

	private ClientEstimate estimate;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().payeeNumber(Global.get().Customer()), false,
				true, new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						get(PHONE).setValue(value.getPhoneNo());

						get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								get(CONTACT).setValue(contact);
								break;
							}
						}

						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateQuoteCommand.this.get(CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), CURRENCY_FACTOR) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateQuoteCommand.this.get(
						CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});
		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				false, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected double getCurrencyFactor() {
				return CreateQuoteCommand.this.getCurrencyFactor();
			}

			@Override
			protected Payee getPayee() {
				return (Customer) CreateQuoteCommand.this.get(CUSTOMER).getValue();
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnterName(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contacts(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phone(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new DateRequirement(EXPIRATION_DATE, getMessages()
				.pleaseEnter(getMessages().expirationDate()), getMessages()
				.expirationDate(), true, false) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& !context.getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Customer customer = get(CUSTOMER).getValue();
		estimate.setCustomer(customer.getID());
		estimate.setType(ClientTransaction.TYPE_ESTIMATE);
		ClientFinanceDate date = get(DATE).getValue();
		estimate.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		estimate.setNumber(number);

		Contact contact = get(CONTACT).getValue();
		if (contact != null)
			estimate.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		estimate.setAddress(billTo);

		String phone = get(PHONE).getValue();
		estimate.setPhone(phone);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			estimate.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		estimate.setTransactionItems(items);

		PaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		estimate.setPaymentTerm(paymentTerm.getID());

		ClientFinanceDate d = get(DATE).getValue();
		estimate.setDate(d.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		estimate.setDeliveryDate(deliveryDate.getDate());
		ClientFinanceDate expiryDdate = get(EXPIRATION_DATE).getValue();
		estimate.setExpirationDate(expiryDdate.getDate());
		estimate.setCurrencyFactor(1);
		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);
		double taxTotal = updateTotals(context, estimate, true);
		estimate.setTaxTotal(taxTotal);
		estimate.setCurrency(customer.getCurrency().getID());
		estimate.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(estimate, context);

		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().quote()) : "Quote updating..";
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().credit()) : "Credit updating..";
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().charge()) : "Charge updating..";
		}

		return "";
	}

	@Override
	protected String getDetailsMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().quote()) : getMessages().readyToUpdate(
					getMessages().estimate());
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().credit()) : getMessages().readyToUpdate(
					getMessages().credits());
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().charge()) : getMessages().readyToUpdate(
					getMessages().Charges());
		}

		return "";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get("expirationDate").setDefaultValue(new ClientFinanceDate());

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().quote()) : getMessages().updateSuccessfully(
					getMessages().quote());
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().credit()) : getMessages().updateSuccessfully(
					getMessages().credit());
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().charge()) : getMessages().updateSuccessfully(
					getMessages().charge());
		}
		return "";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		int estimateType = ClientEstimate.QUOTES;
		String commandString = context.getCommandString().toLowerCase();
		if (commandString.contains("charge")) {
			estimateType = ClientEstimate.CHARGES;
		} else if (commandString.contains("credit")) {
			estimateType = ClientEstimate.CREDITS;
		}
		if (commandString.contains("quote")) {
			if (!context.getPreferences().isDoyouwantEstimates()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		} else {
			if (!context.getPreferences().isDelayedchargesEnabled()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		}

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				if (estimateType == ClientEstimate.QUOTES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().estimate()));
					return "quotesList";
				} else if (estimateType == ClientEstimate.CREDITS) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().credits()));
					return "creditsList";
				} else if (estimateType == ClientEstimate.CHARGES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "chargesList";
				}
			}

			estimate = getTransaction(string, AccounterCoreType.ESTIMATE,
					context);
			if (estimate == null) {
				if (estimateType == ClientEstimate.QUOTES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().estimate()));
					return "quotesList " + string;
				} else if (estimateType == ClientEstimate.CREDITS) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().credits()));
					return "creditsList " + string;
				} else if (estimateType == ClientEstimate.CHARGES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "chargesList " + string;
				}
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			estimate = new ClientEstimate();
			estimate.setEstimateType(estimateType);
		}
		setTransaction(estimate);
		return null;
	}

	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(estimate.getCurrencyFactor());
		get(DATE).setValue(estimate.getDate());
		get(NUMBER).setValue(estimate.getNumber());
		get(ITEMS).setValue(estimate.getTransactionItems());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(estimate.getCustomer(),
						AccounterCoreType.CUSTOMER));
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							estimate.getTransactionItems(), context));

		}
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(estimate.getDeliveryDate()));
		get(EXPIRATION_DATE).setValue(
				new ClientFinanceDate(estimate.getExpirationDate()));
		get(CONTACT).setValue(toServerContact(estimate.getContact()));
		get(BILL_TO).setValue(estimate.getAddress());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(estimate.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(MEMO).setValue(estimate.getMemo());
		get(IS_VAT_INCLUSIVE).setValue(estimate.isAmountsIncludeVAT());
		get(PHONE).setValue(estimate.getPhone());
	}

	@Override
	protected Payee getPayee() {
		return (Customer) CreateQuoteCommand.this.get(CUSTOMER).getValue();
	}

}