package com.vimukti.accounter.developer.api.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.tools.ant.filters.StringInputStream;

import com.vimukti.accounter.developer.api.core.AbstractTest;
import com.vimukti.accounter.developer.api.core.ApiResult;

public class CreateTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/crud";
		createCustomer();
	}

	private void createCustomer() throws Exception {
		String ss = "	<Customer><name>MMMMM</name><currencyFactor>1.0</currencyFactor></Customer>";
		doRequest(new StringInputStream(ss));
	}

	@SuppressWarnings("deprecation")
	private void doRequest(InputStream body) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String queryStr = getQueryString(map);
		PutMethod prepareMethod = preparePutMethod(path, queryStr);
		prepareMethod.setRequestBody(body);
		ApiResult result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}

}
