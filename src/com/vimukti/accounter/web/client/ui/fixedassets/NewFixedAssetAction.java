package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewFixedAssetAction extends Action {

	private NewFixedAssetView view;

	public NewFixedAssetAction(String text) {
		super(text);
		this.catagory = Accounter.constants().fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newFixedAsset();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new NewFixedAssetView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewFixedAssetAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/New Fixed Asset.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newFixedAsset";
	}

}
