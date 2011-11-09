package com.vimukti.accounter.mobile.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.FinanceTool;

public class CommandUtils {

	public static Vendor getVendorByName(Company company, String vendorName) {
		Set<Vendor> vendors = company.getVendors();
		for (Vendor vendor : vendors) {
			if (vendor.getName().equals(vendorName)) {
				return vendor;
			}
		}
		return null;
	}

	public static IAccounterCore getClientObjectById(long id,
			AccounterCoreType type, long companyId) {
		try {
			return new FinanceTool().getManager().getObjectById(type, id,
					companyId);
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ClientItemGroup getItemGroupByName(Company company,
			String string) {
		Set<ItemGroup> itemGroups = company.getItemGroups();
		for (ItemGroup itemGroup : itemGroups) {
			if (itemGroup.getName().equals(string)) {
				return (ClientItemGroup) getClientObjectById(itemGroup.getID(),
						AccounterCoreType.ITEM_GROUP, company.getId());
			}
		}
		return null;
	}

	public static ClientFinanceDate getCurrentFiscalYearStartDate(
			ClientCompanyPreferences preferences) {
		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);
		return startDate;
	}

	public static ClientFinanceDate getCurrentFiscalYearEndDate(
			ClientCompanyPreferences preferences) {

		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());
		return endDate;
	}

	public static ClientAccount getAccountByName(Company company, String string) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(string)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static ClientAccount getAccountByNumber(Company company,
			long numberFromString) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getNumber().equals(numberFromString)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static List<String> getPaymentMethods() {
		List<String> paymentMethods = new ArrayList<String>();
		paymentMethods.add(Accounter.constants().cash());
		paymentMethods.add(Accounter.constants().check());
		paymentMethods.add(Accounter.constants().creditCard());
		return paymentMethods;
	}

	public static IAccounterServerCore getServerObjectById(long id,
			AccounterCoreType account) {
		return (IAccounterServerCore) new FinanceTool().getManager()
				.getServerObjectForid(account, id);
	}
}
