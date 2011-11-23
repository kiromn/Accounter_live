package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class VendorGroupListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<VendorGroup>(getMessages().vendor()
				+ " " + getMessages().group(), "", 10) {

			@Override
			protected String onSelection(VendorGroup value) {
				return "update vendor group " + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(
						getMessages().vendor() + " " + getMessages().group());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(
						getMessages().vendor() + " " + getMessages().group());

			}

			@Override
			protected Record createRecord(VendorGroup value) {
				Record vendorGroupRec = new Record(value);
				vendorGroupRec.add(getMessages().name(), value.getName());
				return vendorGroupRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(
						getMessages().vendor() + " " + getMessages().group()));

			}

			@Override
			protected boolean filter(VendorGroup e, String name) {
				return false;

			}

			@Override
			protected List<VendorGroup> getLists(Context context) {
				return getVendorGroups(context);
			}

			@Override
			protected void setSelectCommands(CommandList commandList,
					VendorGroup value) {
				commandList.add(new UserCommand("update vendor group", value
						.getName()));
				commandList.add(new UserCommand("delete vendor group", value
						.getName()));
			}
		});

	}

	private List<VendorGroup> getVendorGroups(Context context) {
		Set<VendorGroup> vendorGroups = context.getCompany().getVendorGroups();
		List<VendorGroup> result = new ArrayList<VendorGroup>(vendorGroups);
		return result;
	}
}
