package com.vimukti.accounter.web.client.ui.customers;

import java.util.Set;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class VendorDetailsPanel extends VerticalPanel {
	ClientVendor selectedVendor;
	LabelItem name, email, currency, fax, vendorsince, webpageadress, notes,
			address;
	AmountLabel balance, openingBalance;
	Label heading, vendName;
	private ClientAddress payeeAddress;
	private Set<ClientAddress> addressListOfVendor;
	private DynamicForm leftform, rightform;
	protected static final AccounterMessages messages = Global.get().messages();

	public VendorDetailsPanel(ClientVendor clientVendor) {
		this.selectedVendor = clientVendor;
		createControls();
		if (clientVendor != null) {
			showVendorDetails(clientVendor);
		}

	}

	private void createControls() {

		name = new LabelItem(messages.name(), "name");
		name.setTitle(messages.name());

		email = new LabelItem(messages.email(), "email");
		email.setTitle(messages.email());

		balance = new AmountLabel(messages.balance());

		currency = new LabelItem(messages.currency(), "currency");
		currency.setTitle(messages.currency());

		fax = new LabelItem(messages.faxNumber(), "fax");
		fax.setTitle(messages.faxNumber());

		vendorsince = new LabelItem(messages.payeeSince(Global.get().Vendor()),
				"vendorsince");
		vendorsince.setTitle(messages.payeeSince(Global.get().Vendor()));

		webpageadress = new LabelItem(messages.webPageAddress(),
				"webpageadress");
		webpageadress.setTitle(messages.webPageAddress());

		openingBalance = new AmountLabel(messages.balanceAsOf());

		notes = new LabelItem(messages.notes(), "notes");
		notes.setTitle(messages.notes());

		address = new LabelItem(messages.address(), "address");
		address.setTitle(messages.address());

		leftform = new DynamicForm("leftform");
		rightform = new DynamicForm("rightform");

		leftform.add(name, balance, openingBalance, currency, vendorsince);

		rightform.add(email, fax, webpageadress, notes, address);
		rightform.addStyleName("customers_detail_rightpanel");

		StyledPanel hp = new StyledPanel("hp");
		StyledPanel headingPanel = new StyledPanel("headingPanel");
		headingPanel.addStyleName("customers_detail_panel");
		heading = new Label(messages.payeeDetails(Global.get().Vendors())
				+ " :");
		headingPanel.add(heading);
		vendName = new Label();
		vendName.setText(messages.noPayeeSelected(Global.get().Vendor()));
		headingPanel.add(heading);
		headingPanel.add(vendName);
		add(headingPanel);
		hp.add(leftform);
		hp.add(rightform);

		add(hp);
		headingPanel.setWidth("100%");
		hp.setWidth("100%");
		this.setWidth("100%");
		hp.getElement().getParentElement().addClassName("details-Panel");
	}

	protected void showVendorDetails(ClientVendor selectedVendor) {
		if (selectedVendor != null) {

			addressListOfVendor = selectedVendor.getAddress();
			vendName.setText(selectedVendor.getName());
			name.setValue(selectedVendor.getName());

			email.setValue(selectedVendor.getEmail());

			balance.setAmount(selectedVendor.getBalance());

			currency.setValue(Accounter.getCompany()
					.getCurrency(selectedVendor.getCurrency()).getFormalName());

			fax.setValue(selectedVendor.getFaxNo());

			long payeeSince = selectedVendor.getPayeeSince();
			ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
			if (payeeSince > 0) {
				clientFinanceDate = new ClientFinanceDate(payeeSince);
			}

			vendorsince.setValue(UIUtils
					.getDateByCompanyType(clientFinanceDate));

			webpageadress.setValue(selectedVendor.getWebPageAddress());

			openingBalance.setAmount(selectedVendor.getOpeningBalance());

			notes.setValue(selectedVendor.getMemo());
			notes.getMainWidget().getElement().getParentElement()
					.addClassName("customer-detail-notespanel");

			payeeAddress = getAddress(ClientAddress.TYPE_BILL_TO);
			if (payeeAddress != null) {

				address.setValue(getValidAddress(payeeAddress));

			} else
				address.setValue("");

		} else {
			name.setValue("");
			email.setValue("");
			balance.setAmount(0.00);
			currency.setValue("");
			fax.setValue("");
			vendorsince.setValue("");
			webpageadress.setValue("");
			openingBalance.setAmount(0.00);
			notes.setValue("");
			address.setValue("");
		}
	}

	public void setVendor(ClientVendor vendor) {
		this.selectedVendor = vendor;
	}

	public ClientVendor getVendor() {
		return selectedVendor;
	}

	public ClientAddress getAddress(int type) {
		for (ClientAddress address : addressListOfVendor) {
			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "," + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "," + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "," + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "," + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "," + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion() + ".";
		}
		return toToSet;
	}

}
