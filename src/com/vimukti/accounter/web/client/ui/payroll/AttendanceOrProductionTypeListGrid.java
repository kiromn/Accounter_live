package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class AttendanceOrProductionTypeListGrid extends
		BaseListGrid<ClientAttendanceOrProductionType> {

	public AttendanceOrProductionTypeListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.name(), messages.type(),
				messages.period(), messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.name(), messages.type(),
				messages.period(), messages.delete() };
	}

	@Override
	protected void executeDelete(final ClientAttendanceOrProductionType object) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					deleteRecord(object);
				}
			}
		};
		rpcDoSerivce.delete(AccounterCoreType.ATTENDANCE_PRODUCTION_TYPE,
				object.getID(), callback);
	}

	@Override
	protected Object getColumnValue(ClientAttendanceOrProductionType obj,
			int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return ClientAttendanceOrProductionType.getTypeName(obj.getType());
		case 2:
			if (obj.getType() == ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
				obj.getUnit();
			}
			return obj.getPeriodType();

		case 3:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientAttendanceOrProductionType obj) {
		ReportsRPC.openTransactionView(
				IAccounterCore.ATTENDANCE_PRODUCTION_TYPE, obj.getID());
	}

	@Override
	protected void onClick(ClientAttendanceOrProductionType obj, int row,
			int col) {
		if (col == 3) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.type(),
				messages.period(), messages.delete() };
	}
}
