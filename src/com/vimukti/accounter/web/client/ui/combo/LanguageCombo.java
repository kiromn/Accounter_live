package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.ui.Accounter;

public class LanguageCombo extends CustomCombo<ClientLanguage> {

	public LanguageCombo(String title) {
		super(title, false, 1);
		initComboData();
	}

	private void initComboData() {
		Accounter.createTranslateService().getLanguages(
				new AsyncCallback<List<ClientLanguage>>() {

					@Override
					public void onSuccess(List<ClientLanguage> result) {
						initCombo(result);
						setLanguage(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("");

					}
				});

	}

	protected void setLanguage(List<ClientLanguage> result) {
		Accounter.createTranslateService().getLocalLanguage(
				new AsyncCallback<ClientLanguage>() {

					@Override
					public void onSuccess(ClientLanguage result) {
						setComboItem(result);
						updateData(result);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});

	}

	protected void updateData(ClientLanguage result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getDisplayName(ClientLanguage object) {
		if (object.getLanguageName() != null) {
			return object.getLanguageName();
		}
		return null;
	}

	@Override
	protected String getColumnData(ClientLanguage object, int col) {
		if (object != null)
			if (object.getLanguageTooltip().equals(object.getLanguageName())) {
				return object.getLanguageName();
			} else {
				return messages.languageName(
						object.getLanguageName(), object.getLanguageTooltip());
			}
		return "";
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
