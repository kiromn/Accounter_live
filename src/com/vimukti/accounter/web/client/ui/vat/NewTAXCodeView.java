package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeView extends BaseView<ClientTAXCode> {

	private TextItem vatCodeTxt;
	private TextAreaItem description;
	private CheckboxItem isActive;
	private RadioGroupItem taxableGroupRadio;
	private VATItemCombo vatItemComboForPurchases;
	private VATItemCombo vatItemComboForSales;
	private DynamicForm vatNameForm;
	public long selectedVATPurchaseAcc;
	public long selectedVATSAlesAcc;
	private ClientTAXCode editableTAXCode;
	protected boolean isComboDisabled = false;
	private String vatCode;

	private ArrayList<DynamicForm> listforms;

	public NewTAXCodeView() {
		super();
	}

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
		editableTAXCode = (ClientTAXCode) this.data;
		createControls();
		setSize("100%", "");

	}

	@Override
	public void initData() {
		ClientTAXCode vat = (ClientTAXCode) getData();
		if (vat != null) {
			vatCodeTxt.setValue(vat.getName() != null ? vat.getName() : "");
			vatCode = vat.getName() != null ? vat.getName() : "";
			description.setValue(vat.getDescription() != null ? vat
					.getDescription() : "");
			isActive.setValue(vat.isActive());
			taxableGroupRadio.setValue(vat.isTaxable() ? Accounter.constants()
					.taxable() : Accounter.constants().taxExempt());

			if (getCompany().getTaxItem(vat.getTAXItemGrpForPurchases()) != null) {
				selectedVATPurchaseAcc = vat.getTAXItemGrpForPurchases();
				vatItemComboForPurchases.setComboItem(Accounter.getCompany()
						.getTaxItem(vat.getTAXItemGrpForPurchases()));
			} else
				vatItemComboForPurchases.setSelected("");

			if (getCompany().getTaxItem(vat.getTAXItemGrpForSales()) != null) {
				selectedVATSAlesAcc = vat.getTAXItemGrpForSales();
				vatItemComboForSales.setComboItem(Accounter.getCompany()
						.getTaxItem(vat.getTAXItemGrpForSales()));
			} else
				vatItemComboForSales.setSelected("");

			if (!vat.isTaxable()) {
				// vatItemComboForPurchases.setValue("");
				// vatItemComboForSales.setValue("");
				vatItemComboForPurchases.setDisabled(true);
				vatItemComboForSales.setDisabled(true);
				vatItemComboForSales.setRequired(false);
				vatItemComboForPurchases.setRequired(false);
			}
		}
	}

	private void createControls() {
		Label infoLabel = new Label(Accounter.constants().newVATCode());
		infoLabel.setStyleName(Accounter.constants().labelTitle());
		// infoLabel.setHeight("35px");
		listforms = new ArrayList<DynamicForm>();

		AccounterConstants vatMessages = Accounter.constants();
		vatCodeTxt = new TextItem(vatMessages.vatCode());
		vatCodeTxt.setHelpInformation(true);
		vatCodeTxt.setRequired(true);
		vatCodeTxt.setWidth(100);
		description = new TextAreaItem();
		description.setHelpInformation(true);
		description.setWidth(100);
		description.setTitle(Accounter.constants().description());

		isActive = new CheckboxItem(Accounter.constants().isActive());
		isActive.setValue((Boolean) true);

		taxableGroupRadio = new RadioGroupItem(Accounter.constants().tax());
		taxableGroupRadio.setWidth(100);
		taxableGroupRadio.setValues(getClickHandler(), Accounter.constants()
				.taxable(), Accounter.constants().taxExempt());
		taxableGroupRadio.setDefaultValue(Accounter.constants().taxable());

		vatItemComboForPurchases = new VATItemCombo(Accounter.constants()
				.vatItemForPurchases());
		vatItemComboForPurchases.setHelpInformation(true);
		vatItemComboForPurchases.initCombo(vatItemComboForPurchases
				.getPurchaseWithPrcntVATItems());
		vatItemComboForPurchases.setRequired(true);
		// vatItemComboForPurchases.setWidth(100);
		vatItemComboForPurchases
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {
					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATPurchaseAcc = selectItem.getID();
					}
				});

		vatItemComboForSales = new VATItemCombo(Accounter.constants()
				.vatItemForSales());
		vatItemComboForSales.setHelpInformation(true);
		vatItemComboForSales.initCombo(vatItemComboForSales
				.getSalesWithPrcntVATItems());
		vatItemComboForSales.setRequired(true);
		vatItemComboForSales
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATSAlesAcc = selectItem.getID();
					}
				});
		vatNameForm = new DynamicForm();
		vatNameForm.setWidth("80%");
		vatNameForm.getCellFormatter().setWidth(0, 0, "225px");
		vatNameForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		vatNameForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		vatNameForm.setFields(vatCodeTxt, description, taxableGroupRadio,
				isActive, vatItemComboForSales, vatItemComboForPurchases);

		if (editableTAXCode != null) {
			vatCodeTxt
					.setValue(editableTAXCode.getName() != null ? editableTAXCode
							.getName() : "");
			description
					.setValue(editableTAXCode.getDescription() != null ? editableTAXCode
							.getDescription() : "");
			isActive.setValue(editableTAXCode.isActive());
			taxableGroupRadio.setValue(editableTAXCode.isTaxable() ? Accounter
					.constants().taxable() : Accounter.constants().taxExempt());
			vatItemComboForPurchases.setValue(editableTAXCode
					.getTAXItemGrpForPurchases() != 0 ? Accounter
					.getCompany()
					.getTAXItemGroup(
							editableTAXCode.getTAXItemGrpForPurchases())
					.getName() : "");
			vatItemComboForSales.setValue(editableTAXCode
					.getTAXItemGrpForSales() != 0 ? Accounter.getCompany()
					.getTAXItemGroup(editableTAXCode.getTAXItemGrpForSales())
					.getName() : "");
		}

		VerticalPanel mainVPanel = new VerticalPanel();
		mainVPanel.setSpacing(25);
		mainVPanel.setWidth("100%");
		mainVPanel.add(infoLabel);
		mainVPanel.add(vatNameForm);

		if (UIUtils.isMSIEBrowser()) {
			vatNameForm.getCellFormatter().setWidth(0, 1, "270px");
			vatNameForm.setWidth("50%");
		}
		this.add(mainVPanel);

		/* Adding dynamic forms in list */
		listforms.add(vatNameForm);

	}

	private ClickHandler getClickHandler() {
		ClickHandler hanler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				vatCodeTxt.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForPurchases.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForSales.getMainWidget().removeStyleName(
						"highlightedFormItem");
				String taxbl = taxableGroupRadio.getValue().toString();
				taxableGroupRadio.setValue(taxbl);
				if (taxbl.equalsIgnoreCase(Accounter.constants().taxable())) {
					isComboDisabled = false;
					vatItemComboForPurchases.setDisabled(false);
					vatItemComboForSales.setDisabled(false);
					vatItemComboForPurchases.setRequired(true);
					vatItemComboForSales.setRequired(true);
				} else {
					isComboDisabled = true;
					// vatItemComboForSales.setValue("");
					// vatItemComboForPurchases.setValue("");
					vatItemComboForSales.setDisabled(true);
					vatItemComboForPurchases.setDisabled(true);
					vatItemComboForPurchases.setRequired(false);
					vatItemComboForSales.setRequired(false);
				}
			}
		};
		return hanler;
	}

	@Override
	public void saveAndUpdateView() {

		ClientTAXCode vatCode = getVATCode();

		saveOrUpdate(vatCode);

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		String exceptionMessage = exception.getMessage();
		addError(this, exception.getMessage());
		ClientTAXCode clientTAXCode = getVATCode();
		if (exceptionMessage.contains("name")) {
			clientTAXCode.setName(vatCode);

		}
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (editableVATCode == null) {
			// Accounter.showInformation(FinanceApplication.constants()
			// .newVATCodeCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.constants()
			// .VATCodeUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}
	}

	protected ClientTAXCode getVATCode() {
		ClientTAXCode vatCode;
		if (editableTAXCode != null) {
			vatCode = editableTAXCode;
		} else
			vatCode = new ClientTAXCode();

		vatCode.setName(vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "");
		vatCode.setDescription(description.getValue() != null ? description
				.getValue().toString() : "");
		vatCode.setActive((Boolean) isActive.getValue());
		if (taxableGroupRadio.getValue() != null) {
			if (taxableGroupRadio.getValue().toString()
					.equalsIgnoreCase("Taxable"))
				vatCode.setTaxable(true);
			else
				vatCode.setTaxable(false);
		} else
			vatCode.setTaxable(false);

		vatCode.setTAXItemGrpForPurchases(selectedVATPurchaseAcc);
		vatCode.setTAXItemGrpForSales(selectedVATSAlesAcc);

		return vatCode;
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		result.add(DynamicForm.validate(this.getForms().toArray(
				new DynamicForm[getForms().size()])));
		String name = vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "";
		if (!((editableTAXCode == null && Utility.isObjectExist(getCompany()
				.getTaxCodes(), name)) ? false : true)
				|| (editableTAXCode != null ? (editableTAXCode.getName()
						.equalsIgnoreCase(name) ? true
						: (Utility.isObjectExist(getCompany().getTaxCodes(),
								name) ? false : true)) : true)) {
			result.addError(vatCodeTxt, AccounterErrorType.ALREADYEXIST);
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
		this.vatCodeTxt.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {

		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.addComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.addComboItem((ClientTAXItem) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.removeComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.removeComboItem((ClientTAXItem) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.updateComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.updateComboItem((ClientTAXItem) core);
			break;

		}
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

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
		return UIUtils.getVendorString(Accounter.constants().vatCode(),
				Accounter.constants().taxCode());
	}
}
