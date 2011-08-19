package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.user.client.ui.Composite;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends Composite {

	protected AccounterConstants accounterConstants = Accounter.constants();
	protected AccounterMessages accounterMessages = Accounter.messages();

	public AbstractSetupPage() {

	}

	protected abstract void createControls();

	protected abstract void onSave();

	protected abstract void onLoad();

	public void setPreferences(ClientCompanyPreferences preferences) {
		// TODO Auto-generated method stub

	}

	public boolean doShow() {
		// TODO Auto-generated method stub
		return false;
	}
}
