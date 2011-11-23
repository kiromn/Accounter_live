package com.vimukti.accounter.web.client.ui.grids;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class TAXAgencyListGrid extends BaseListGrid<PayeeList> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();

	public TAXAgencyListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable, true);
	}

	@Override
	protected String[] getColumns() {

		String[] colArray = new String[11];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Accounter.messages().active();
				break;
			case 1:
				colArray[index] = Accounter.messages().payeeName(
						Accounter.messages().taxAgency());
				break;
			case 2:
				colArray[index] = Accounter.messages().currentMonth();
				colsMap.put(2, getCurrentMonth());
				break;
			case 3:
				colArray[index] = getMonthAsString(getCurrentMonth() - 1);
				colsMap.put(3, getCurrentMonth() - 1);
				break;
			case 4:
				colArray[index] = getMonthAsString(getCurrentMonth() - 2);
				colsMap.put(4, getCurrentMonth() - 2);
				break;
			case 5:
				colArray[index] = getMonthAsString(getCurrentMonth() - 3);
				colsMap.put(5, getCurrentMonth() - 3);
				break;
			case 6:
				colArray[index] = getMonthAsString(getCurrentMonth() - 4);
				colsMap.put(6, getCurrentMonth() - 4);
				break;
			case 7:
				colArray[index] = getMonthAsString(getCurrentMonth() - 5);
				colsMap.put(6, getCurrentMonth() - 5);
				break;
			case 8:
				colArray[index] = Accounter.messages().yearToDate();
				break;
			case 9:
				colArray[index] = Accounter.messages().balance();
				break;
			case 10:
				colArray[index] = "";
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	private int getCurrentMonth() {
		return new ClientFinanceDate().getMonth();
	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 1:
			return Accounter.messages().jan();
		case 2:
			return Accounter.messages().feb();
		case 3:
			return Accounter.messages().mar();
		case 4:
			return Accounter.messages().apr();
		case 5:
			return Accounter.messages().may();
		case 6:
			return Accounter.messages().jun();
		case 7:
		case -5:
			return Accounter.messages().jul();
		case 8:
		case -4:
			return Accounter.messages().aug();
		case 9:
		case -3:
			return Accounter.messages().sept();
		case 10:
		case -2:
			return Accounter.messages().oct();
		case 11:
		case -1:
			return Accounter.messages().nov();
		case 12:
		case 0:
			return Accounter.messages().dec();

		}
		return "";

	}

	@Override
	protected Object getColumnValue(PayeeList payee, int col) {

		switch (col) {
		case 0:
			return payee.isActive();
		case 1:
			return payee.getPayeeName();
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					payee.getCurrentMonth(), payee.getCurrecny());
		case 3:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousMonth(), payee.getCurrecny());
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousSecondMonth(), payee.getCurrecny());

		case 5:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousThirdMonth(), payee.getCurrecny());

		case 6:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousFourthMonth(), payee.getCurrecny());

		case 7:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousFifthMonth(), payee.getCurrecny());

		case 8:
			return DataUtils.amountAsStringWithCurrency(payee.getYearToDate(),
					payee.getCurrecny());
		case 9:
			return DataUtils.amountAsStringWithCurrency(payee.getBalance(),
					payee.getCurrecny());
		case 10:
			updateTotal(payee, true);
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			break;
		}

		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 40;
		case 1:
			return -1;
		case 2:
			return 100;
		case 3:
			return 70;
		case 4:
			return 70;
		case 5:
			return 70;
		case 6:
			return 70;
		case 7:
			return 70;
		case 8:
			return 75;
		case 9:
			return 70;
		case 10:
			return 25;
		}

		return -1;
	}

	@Override
	protected void onClick(PayeeList payee, int row, int col) {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		switch (col) {
		case 10:
			showWarnDialog(payee);
			break;
		default:
			break;
		}
		// super.onClick(payee, row, col);
	}

	@Override
	public void onDoubleClick(PayeeList payee) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			AccounterAsyncCallback<ClientTAXAgency> callback = new AccounterAsyncCallback<ClientTAXAgency>() {

				public void onException(AccounterException caught) {
				}

				public void onResultSuccess(ClientTAXAgency result) {
					if (result != null) {
						ActionFactory.getNewTAXAgencyAction().run(
								(ClientTAXAgency) result, false);
					}
				}

			};
			Accounter.createGETService().getObjectById(
					AccounterCoreType.TAXAGENCY, payee.id, callback);
		}
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	protected void executeDelete(final PayeeList recordToBeDeleted) {

		AccounterAsyncCallback<ClientTAXAgency> callback = new AccounterAsyncCallback<ClientTAXAgency>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientTAXAgency result) {
				if (result != null) {
					deleteObject(result);
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.TAXAGENCY,
				recordToBeDeleted.id, callback);

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onValueChange(PayeeList obj, int col, Object value) {
	}

	protected void updateTotal(PayeeList supplier, boolean add) {

		if (add) {
			if (supplier.isActive())
				total += supplier.getBalance();
			else
				total += supplier.getBalance();
		} else
			total -= supplier.getBalance();

	}

	public Double getTotal() {
		return total;
	}

	public void setTotal() {
		this.total = 0.0D;

	}

	@Override
	protected int sort(PayeeList obj1, PayeeList obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getPayeeName().toLowerCase()
					.compareTo(obj2.getPayeeName().toLowerCase());
		case 2:
			Double currentMonth1 = obj1.getCurrentMonth();
			Double currentMonth2 = obj2.getCurrentMonth();
			return currentMonth1.compareTo(currentMonth2);
		case 3:
			Double previousMonth1 = obj1.getPreviousMonth();
			Double previousMonth2 = obj2.getPreviousMonth();
			return previousMonth1.compareTo(previousMonth2);
		case 4:
			Double previousSecondMonth1 = obj1.getPreviousSecondMonth();
			Double previousSecondMonth2 = obj2.getPreviousSecondMonth();
			return previousSecondMonth1.compareTo(previousSecondMonth2);
		case 5:
			Double previousThirdMonth1 = obj1.getPreviousThirdMonth();
			Double previousThirdMonth2 = obj2.getPreviousThirdMonth();
			return previousThirdMonth1.compareTo(previousThirdMonth2);
		case 6:
			Double previousFourthMonth1 = obj1.getPreviousFourthMonth();
			Double previousFourthMonth2 = obj2.getPreviousFourthMonth();
			return previousFourthMonth1.compareTo(previousFourthMonth2);
		case 7:
			Double previousFifthMonth1 = obj1.getPreviousFifthMonth();
			Double previousFifthMonth2 = obj2.getPreviousFifthMonth();
			return previousFifthMonth1.compareTo(previousFifthMonth2);

		case 8:
			Double yearToDate1 = obj1.getYearToDate();
			Double yearToDate2 = obj2.getYearToDate();
			return yearToDate1.compareTo(yearToDate2);
		case 9:
			Double bal1 = obj1.getBalance();
			Double bal2 = obj2.getBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.TAXAGENCY;
	}

	@Override
	public void addData(PayeeList obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(Global.get().Vendor()));
		} else {
			super.deleteFailed(caught);
		}
	}

}