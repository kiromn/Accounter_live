package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class CustomerAndSalesSettingsMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject customerAndSalesSetting = new JSONObject();

		customerAndSalesSetting.put(
				"itemType",
				context.getPickListContext().get(
						"ItemType",
						getItemTypeString(obj.isSellServices(),
								obj.isSellProducts())));
		customerAndSalesSetting.put("inventoryTracking",
				obj.isInventoryEnabled());
		customerAndSalesSetting.put("haveMultipleWarehouses",
				obj.iswareHouseEnabled());
		customerAndSalesSetting.put("enableInventoryUnits",
				obj.isUnitsEnabled());
		customerAndSalesSetting.put(
				"inventoryScheme",
				context.getPickListContext()
						.get("InventoryScheme",
								getInventorySchemeString(obj
										.getActiveInventoryScheme())));
		customerAndSalesSetting.put("trackDiscount", obj.isTrackDiscounts());
		customerAndSalesSetting.put("discountInTransactions", obj
				.isDiscountPerDetailLine() ? "OnePerDetailLine"
				: "OnePerTransaction");
		// DiscountAccount is not found
		customerAndSalesSetting.put("useDelayedCharges",
				obj.isDelayedchargesEnabled());
		return customerAndSalesSetting;
	}

	private String getInventorySchemeString(int value) {
		switch (value) {
		case 1:
			return "FirstInFirstOut";
		case 2:
			return "LastInFirstOut";
		default:
			return "Average";
		}
	}

	private String getItemTypeString(Boolean service, Boolean product) {
		if (service && !product) {
			return "Sales";
		}
		if (product && !service) {
			return "Product";
		}
		return "BothsalesAndProduct";
	}
}