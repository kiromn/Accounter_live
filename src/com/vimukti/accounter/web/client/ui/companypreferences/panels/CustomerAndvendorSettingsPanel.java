package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class CustomerAndvendorSettingsPanel extends AbstractCompanyInfoPanel {
	private RadioGroupItem chargeTaxGroupItem, trackTimeGroupItem,
			managingBillsGroupItem, createEstimatesGroupItem,
			usingStatementsGroupItem;

	public CustomerAndvendorSettingsPanel() {
		super();
		createControls();
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();

		VerticalPanel chargeTaxPanel = new VerticalPanel();
		// DynamicForm trackTimeForm = new DynamicForm();
		VerticalPanel managingBillPanel = new VerticalPanel();
		VerticalPanel createEstimatesPanel = new VerticalPanel();
		VerticalPanel usingStatementsPanel = new VerticalPanel();

		DynamicForm chargeTaxForm = new DynamicForm();
		// DynamicForm trackTimeForm = new DynamicForm();
		DynamicForm managingBillForm = new DynamicForm();
		DynamicForm createEstimatesForm = new DynamicForm();
		DynamicForm usingStatementsForm = new DynamicForm();

		Label chargeTaxLabelItem = new Label(constants.doyouchargesalestax());
		// Label trackTimeLabelItem = new Label();
		Label managingBillLabelItem = new Label(constants.managingBills());
		Label createEstimatesLabelItem = new Label(constants
				.wanttoCreateEstimatesInAccounter());
		Label usingStatementsLabelItem = new Label(constants
				.doyouWantToUseStatements());

		// trackTimeLabelItem.setValue(constants.doyouwantTrackTime());

		chargeTaxGroupItem = new RadioGroupItem();
		trackTimeGroupItem = new RadioGroupItem();
		managingBillsGroupItem = new RadioGroupItem();
		createEstimatesGroupItem = new RadioGroupItem();
		usingStatementsGroupItem = new RadioGroupItem();

		chargeTaxGroupItem.setGroupName(constants.doyouchargesalestax());
		chargeTaxGroupItem.setValue(constants.yes(), constants.no());
		chargeTaxGroupItem.setVertical(false);

		trackTimeGroupItem.setGroupName(constants.doyouwantTrackTime());
		trackTimeGroupItem.setValue(constants.yes(), constants.no());
		trackTimeGroupItem.setVertical(false);

		managingBillsGroupItem.setGroupName(constants.managingBills());
		managingBillsGroupItem.setValue(constants.yes(), constants.no());
		managingBillsGroupItem.setVertical(false);

		createEstimatesGroupItem.setGroupName(constants
				.wanttoCreateEstimatesInAccounter());
		createEstimatesGroupItem.setValue(constants.yes(), constants.no());
		createEstimatesGroupItem.setVertical(false);

		usingStatementsGroupItem.setGroupName(constants
				.doyouWantToUseStatements());
		usingStatementsGroupItem.setValue(constants.yes(), constants.no());
		usingStatementsGroupItem.setVertical(false);

		chargeTaxForm.setFields(chargeTaxGroupItem);
		// trackTimeForm.setFields(trackTimeLabelItem, trackTimeGroupItem);
		managingBillForm.setFields(managingBillsGroupItem);
		createEstimatesForm.setFields(createEstimatesGroupItem);
		usingStatementsForm.setFields(usingStatementsGroupItem);

		chargeTaxPanel.add(chargeTaxLabelItem);
		chargeTaxLabelItem.addStyleName("header");
		chargeTaxPanel.add(chargeTaxForm);
		chargeTaxForm.addStyleName("fullSizePanel");
		chargeTaxPanel.setCellHorizontalAlignment(chargeTaxForm,
				HasAlignment.ALIGN_RIGHT);

		managingBillPanel.add(managingBillLabelItem);
		managingBillLabelItem.addStyleName("header");
		managingBillPanel.add(managingBillForm);
		managingBillForm.addStyleName("fullSizePanel");
		managingBillPanel.setCellHorizontalAlignment(managingBillForm,
				HasAlignment.ALIGN_RIGHT);

		createEstimatesLabelItem.addStyleName("header");
		createEstimatesPanel.add(createEstimatesLabelItem);
		createEstimatesPanel.add(createEstimatesForm);
		createEstimatesForm.addStyleName("fullSizePanel");
		createEstimatesPanel.setCellHorizontalAlignment(createEstimatesForm,
				HasAlignment.ALIGN_RIGHT);

		usingStatementsLabelItem.addStyleName("header");
		usingStatementsPanel.add(usingStatementsLabelItem);
		usingStatementsPanel.add(usingStatementsForm);
		usingStatementsForm.addStyleName("fullSizePanel");
		usingStatementsPanel.setCellHorizontalAlignment(usingStatementsForm,
				HasAlignment.ALIGN_RIGHT);

		mainPanel.add(chargeTaxPanel);
		mainPanel.add(managingBillPanel);
		mainPanel.add(createEstimatesPanel);
		mainPanel.add(usingStatementsPanel);

		chargeTaxPanel.addStyleName("companyInfoPanel");
		managingBillPanel.addStyleName("companyInfoPanel");
		createEstimatesPanel.addStyleName("companyInfoPanel");
		usingStatementsPanel.addStyleName("companyInfoPanel");

		mainPanel.addStyleName("fullSizePanel");
		mainPanel.setSpacing(8);
		add(mainPanel);
	}

	@Override
	public void onLoad() {
		if (companyPreferences.isDoYouChargesalesTax())
			chargeTaxGroupItem.setValue(constants.yes());
		else
			chargeTaxGroupItem.setValue(constants.no());

		if (companyPreferences.isDoYouKeepTrackOfTime())
			trackTimeGroupItem.setValue(constants.yes());
		else
			trackTimeGroupItem.setValue(constants.no());

		if (companyPreferences.isDoyouKeepTrackofBills())
			managingBillsGroupItem.setValue(constants.yes());
		else
			managingBillsGroupItem.setValue(constants.no());
		if (companyPreferences.isDoyouwantEstimates())
			createEstimatesGroupItem.setValue(constants.yes());
		else
			createEstimatesGroupItem.setValue(constants.no());
		if (companyPreferences.isDoyouwantstatements())
			usingStatementsGroupItem.setValue(constants.yes());
		else
			usingStatementsGroupItem.setValue(constants.no());

	}

	@Override
	public void onSave() {
		if (chargeTaxGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoYouPaySalesTax(true);
		else
			companyPreferences.setDoYouChargesalesTax(false);

		if (trackTimeGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoYouKeepTrackOfTime(true);
		else
			companyPreferences.setDoYouKeepTrackOfTime(false);

		if (managingBillsGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouKeepTrackofBills(true);
		else
			companyPreferences.setDoyouKeepTrackofBills(false);

		if (createEstimatesGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouwantEstimates(true);
		else
			companyPreferences.setDoyouwantEstimates(false);

		if (usingStatementsGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouwantstatements(true);
		else
			companyPreferences.setDoyouwantstatements(false);

	}
}
