package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.Vendor;

public class CashPurchaseMigrator extends TransactionMigrator<CashPurchase> {

	@Override
	public JSONObject migrate(CashPurchase obj, MigratorContext context)
			throws JSONException {
		JSONObject cashPurchase = super.migrate(obj, context);
		Vendor vendor = obj.getVendor();
		cashPurchase.put("phone", obj.getPhone());
		if (vendor != null) {
			cashPurchase.put("payee", context.get("Vendor", vendor.getID()));
		}
		Contact contact = obj.getContact();
		if (contact != null) {
			cashPurchase.put("contact", contact.getID());
		}
		Address vendorAddress = obj.getVendorAddress();
		if (vendorAddress != null) {
			JSONObject addressJSON = new JSONObject();
			addressJSON.put("street", vendorAddress.getStreet());
			addressJSON.put("city", vendorAddress.getCity());
			addressJSON.put("stateOrProvince",
					vendorAddress.getStateOrProvinence());
			addressJSON.put("zipOrPostalCode",
					vendorAddress.getZipOrPostalCode());
			addressJSON.put("country", vendorAddress.getCountryOrRegion());
			cashPurchase.put("billTo", vendorAddress);
		}
		cashPurchase.put(
				"paymentMethod",
				context.getPickListContext().get("PaymentMethod",
						obj.getPaymentMethod()));
		cashPurchase.put("account",
				context.get("Account", obj.getPayFrom().getID()));
		cashPurchase.put("deliveryDate", obj.getDeliveryDate()
				.getAsDateObject());

		{
			List<PurchaseOrder> purchaseOrders = obj.getPurchaseOrders();
			JSONArray array = new JSONArray();
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				JSONObject quoteJson = new JSONObject();
				quoteJson.put("purchaseOrder",
						context.get("PurchaseOrder", purchaseOrder.getID()));
				array.put(quoteJson);
			}
			cashPurchase.put("purchaseOrders", array);
		}
		cashPurchase.put("memo", obj.getMemo());
		return cashPurchase;
	}

}