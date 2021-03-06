package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.VendorPrePayment;

public class VendorPrepaymentMigrator extends
		TransactionMigrator<VendorPrePayment> {
	@Override
	public JSONObject migrate(VendorPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		Address addr = obj.getAddress();
		if (addr != null) {
			JSONObject jsonAddr = new JSONObject();
			jsonAddr.put("street", addr.getStreet());
			jsonAddr.put("city", addr.getCity());
			jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddr.put("country", addr.getCountryOrRegion());
			jsonObject.put("address", jsonAddr);
		}
		// payFrom
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			jsonObject.put("payFrom", context.get("Account", payFrom.getID()));
		}
		// amount
		jsonObject.put("amount", obj.getTotal());
		// payTo
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		// Payment Method
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "Cash");
		}
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return jsonObject;
	}
}
