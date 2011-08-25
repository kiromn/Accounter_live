package com.vimukti.accounter.servlets;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditNotePDFTemplete;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class GeneratePDFservlet extends BaseServlet {
	public ITemplate template;
	public Converter converter;

	private StringBuilder outPutString;
	private String fileName;
	private int transactionType;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		ServletOutputStream sos = null;
		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}
			String footerImg = ("FinanceDir" + File.separator + "footer-print-img.jpg");

			String style = ("FinanceDir" + File.separator + "FinancePrint.css");

			getTempleteObjByRequest(request, footerImg, style, companyName);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:
				String output = outPutString.toString().replaceAll(
						"</html><html>", "");
				java.io.InputStream inputStream = new ByteArrayInputStream(
						output.getBytes());
				InputStreamReader reader = new InputStreamReader(inputStream);
				converter.generatePdfDocuments(fileName, sos, reader);
				break;
			// for credit note
			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
				String creditOutput = outPutString.toString().replaceAll(
						"</html>", "");
				creditOutput = creditOutput.toString().replaceAll("<html>", "");

				creditOutput = "<html>" + creditOutput + "</html>";

				java.io.InputStream inputStr = new ByteArrayInputStream(
						creditOutput.toString().getBytes());
				InputStreamReader creditReader = new InputStreamReader(inputStr);
				converter.generatePdfDocuments(fileName, sos, creditReader);
				break;

			default:
				// for generating pdf document for reports
				converter.generatePdfReports(template, sos);
				break;
			}

			System.err.println("Pdf created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void getTempleteObjByRequest(HttpServletRequest request,
			String footerImg, String style, String companyName)
			throws Exception, IOException, AccounterException {
		Session session = null;
		fileName = "";
		outPutString = new StringBuilder();
		transactionType = 0;
		try {

			String companyID = getCookie(request, COMPANY_COOKIE);
			session = HibernateUtil.openSession(Server.COMPANY + companyID);

			FinanceTool financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);

			Company company = financetool.getCompany();
			int companyType = company.getAccountingType();

			CompanyPreferenceThreadLocal.set(financetool
					.getClientCompanyPreferences());

			String objectId = request.getParameter("objectId");

			String multipleId = request.getParameter("multipleIds");
			String[] ids = null;
			if (multipleId != null) {
				ids = multipleId.split(",");
			}
			String brandingThemeId = request.getParameter("brandingThemeId");

			// this is used to print multiple pdf documents at a time
			if (multipleId != null) {
				transactionType = Integer
						.parseInt(request.getParameter("type"));
				BrandingTheme brandingTheme = (BrandingTheme) financetool
						.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
								Long.parseLong(brandingThemeId));
				converter = new Converter(
						getPageSizeType(brandingTheme.getPageSizeType()));

				for (int i = 0; i < ids.length; i++) {

					if (transactionType == Transaction.TYPE_INVOICE) {
						Invoice invoice = (Invoice) financetool
								.getServerObjectForid(
										AccounterCoreType.INVOICE,
										Long.parseLong(ids[i]));

						// template = new InvoiceTemplete(invoice,
						// brandingTheme, footerImg, style);

						InvoicePDFTemplete invoiceHtmlTemplete = new InvoicePDFTemplete(
								invoice, brandingTheme, company);

						fileName = invoiceHtmlTemplete.getFileName();

						outPutString = outPutString.append(invoiceHtmlTemplete
								.generatePDF());

					}
					if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						CustomerCreditMemo memo = (CustomerCreditMemo) financetool
								.getServerObjectForid(
										AccounterCoreType.CUSTOMERCREDITMEMO,
										Long.parseLong(ids[i]));

						CreditNotePDFTemplete creditNotePDFTemplete = new CreditNotePDFTemplete(
								memo, brandingTheme, company);

						fileName = creditNotePDFTemplete.getFileName();

						outPutString = outPutString
								.append(creditNotePDFTemplete
										.generateCreditMemoPDF());

					}

				}

			} else if (objectId != null) {
				transactionType = Integer
						.parseInt(request.getParameter("type"));
				BrandingTheme brandingTheme = (BrandingTheme) financetool
						.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
								Long.parseLong(brandingThemeId));
				converter = new Converter(
						getPageSizeType(brandingTheme.getPageSizeType()));

				// for printing individual pdf documents
				if (transactionType == Transaction.TYPE_INVOICE) {
					Invoice invoice = (Invoice) financetool
							.getServerObjectForid(AccounterCoreType.INVOICE,
									Long.parseLong(objectId));

					// template = new InvoiceTemplete(invoice,
					// brandingTheme, footerImg, style);

					InvoicePDFTemplete invoiceHtmlTemplete = new InvoicePDFTemplete(
							invoice, brandingTheme, company);

					fileName = invoiceHtmlTemplete.getFileName();

					outPutString = outPutString.append(invoiceHtmlTemplete
							.generatePDF());

				} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
					// for Credit Note
					CustomerCreditMemo memo = (CustomerCreditMemo) financetool
							.getServerObjectForid(
									AccounterCoreType.CUSTOMERCREDITMEMO,
									Long.parseLong(objectId));

					CreditNotePDFTemplete creditNotePDFTemplete = new CreditNotePDFTemplete(
							memo, brandingTheme, company);

					fileName = creditNotePDFTemplete.getFileName();

					outPutString = outPutString.append(creditNotePDFTemplete
							.generateCreditMemoPDF());

				}

			}
			// for Reports
			else {
				transactionType = 0;

				converter = new Converter();
				template = getReportTemplate(request, financetool, footerImg,
						style, companyType);
				fileName = template.getFileName();
			}

		} finally {
			session.close();
		}

	}

	private ITemplate getReportTemplate(HttpServletRequest request,
			FinanceTool financeTool, String footerImg, String style,
			int companyType) throws IOException {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		ReportsGenerator generator;
		if (status == null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF,
					companyType);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, status,
					companyType);
		}

		String gridTemplate = generator.generate(financeTool,
				ReportsGenerator.GENERATIONTYPEPDF);

		ReportTemplate template = new ReportTemplate(reportType, new String[] {
				gridTemplate, footerImg, style, dateRangeHtml });
		/*  */

		return template;
	}

	private Dimension getPageSizeType(int pageSizeType) {
		switch (pageSizeType) {
		case 2:

			return PD4Constants.LETTER;

		default:
			return PD4Constants.A4;
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;
		generatePDF(request, response, companyName);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
