package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;

public class TaxCodeTable extends AbstractDropDownTable<ClientTAXCode> {

	private ListFilter<ClientTAXCode> filter;
	private boolean isSales;

	public TaxCodeTable() {
		super(Accounter.getCompany().getTaxCodes());
	}

	public TaxCodeTable(ListFilter<ClientTAXCode> filter, boolean isSales) {
		super(getTaxCodes(filter));
		this.isSales = isSales;
		this.filter = filter;
	}

	private static List<ClientTAXCode> getTaxCodes(
			ListFilter<ClientTAXCode> filter) {
		if (filter != null) {
			ArrayList<ClientTAXCode> filteredList = Utility.filteredList(
					filter, Accounter.getCompany().getTaxCodes());
			return filteredList;
		} else {
			return Accounter.getCompany().getTaxCodes();
		}
	}

	@Override
	public void initColumns() {
		TextColumn<ClientTAXCode> column = new TextColumn<ClientTAXCode>() {

			@Override
			public String getValue(ClientTAXCode object) {
				// String displayName;
				// ClientTAXItemGroup vatGroup;
				// if (object != null) {
				// displayName = object.getName() != null ? object.getName()
				// : "";
				// if (isSales) {
				// vatGroup = ((ClientTAXItemGroup) Accounter
				// .getCompany()
				// .getTAXItemGroup(object.getTAXItemGrpForSales()));
				//
				// } else {
				// vatGroup = ((ClientTAXItemGroup) Accounter.getCompany()
				// .getTAXItemGroup(
				// object.getTAXItemGrpForPurchases()));
				// }
				//
				// if (vatGroup instanceof ClientTAXItem) {
				// // The selected one is VATItem,so get 'VATRate' from
				// // 'VATItem'
				// if (vatGroup != null)
				// displayName += " - "
				// + ((ClientTAXItem) vatGroup).getTaxRate();
				// } else {
				// // The selected one is VATGroup,so get 'GroupRate' from
				// // 'VATGroup'
				// if (vatGroup != null)
				// displayName += " - "
				// + ((ClientTAXGroup) vatGroup)
				// .getGroupRate();
				// }
				// if (vatGroup != null && vatGroup.isPercentage())
				// displayName += "%";
				// return displayName;
				// } else
				// return "";
				return getDisplayName(object);
			}
		};
		this.addColumn(column);
		this.setColumnWidth(column, "100px");
	}

	@Override
	protected boolean filter(ClientTAXCode t, String string) {
		return getDisplayName(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientTAXCode value) {
		return getDisplayName(value);
	}

	@Override
	protected ClientTAXCode getAddNewRow() {
		ClientTAXCode code = new ClientTAXCode();
		code.setName(messages.addaNewTaxCode());
		return code;
	}

	@Override
	public void addNewItem() {
		addNewItem("");

		/*
		 * else { TaxDialog dialog = new TaxDialog(); dialog.setCallback(new
		 * ActionCallback<ClientTAXCode>() {
		 * 
		 * @Override public void actionResult(ClientTAXCode result) {
		 * selectRow(result);
		 * 
		 * } }); dialog.show(); }
		 */

	}

	@Override
	public List<ClientTAXCode> getTotalRowsData() {
		return getTaxCodes(filter);
	}

	@Override
	protected Class<?> getType() {
		return ClientTAXCode.class;
	}

	@Override
	protected void addNewItem(String text) {
		if (Accounter.getCompany().getPreferences().isTrackTax()) {
			NewTAXCodeAction action = ActionFactory.getNewTAXCodeAction();
			action.setCallback(new ActionCallback<ClientTAXCode>() {

				@Override
				public void actionResult(ClientTAXCode result) {
					selectRow(result);
				}
			});
			action.setTaxCodeName(text);
			action.run(null, true);
		}
	}

	private String getDisplayName(ClientTAXCode code) {
		String name = code.getName();
		if (name == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(name);
		if (name.equals(messages.addaNewTaxCode())) {
			return result.toString();
		}
		result.append(" - ");
		ClientTAXItemGroup vatGroup;
		if (isSales) {
			vatGroup = ((ClientTAXItemGroup) Accounter.getCompany()
					.getTAXItemGroup(code.getTAXItemGrpForSales()));

		} else {
			vatGroup = ((ClientTAXItemGroup) Accounter.getCompany()
					.getTAXItemGroup(code.getTAXItemGrpForPurchases()));
		}

		if (vatGroup instanceof ClientTAXItem) {
			// The selected one is VATItem,so get 'VATRate' from
			// 'VATItem'
			if (vatGroup != null)
				result.append(((ClientTAXItem) vatGroup).getTaxRate());
		} else {
			// The selected one is VATGroup,so get 'GroupRate' from
			// 'VATGroup'
			if (vatGroup != null)
				result.append(((ClientTAXGroup) vatGroup).getGroupRate());
		}
		result.append("%");
		return result.toString();
	}
}
