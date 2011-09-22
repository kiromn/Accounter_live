package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.ServerMaintanance;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class MaintananceServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1163973484857078003L;
	private static final String MAINTANANCE_VIEW = "/WEB-INF/serverMaintain.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("CheckedValue",
				ServerConfiguration.isUnderMaintainance());
		dispatch(req, resp, MAINTANANCE_VIEW);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String paswdString = req.getParameter("password");
		if (paswdString == null
				|| !paswdString.equals(ServerConfiguration.getAdminPassword())) {
			req.setAttribute("message", "PassWord Wrong");
			dispatch(req, resp, MAINTANANCE_VIEW);
			return;
		}

		boolean isUndermaintanance = req.getParameter("option1") == null ? false
				: req.getParameter("option1").equals("on");

		ServerConfiguration.setUnderMaintainance(isUndermaintanance);

		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			ServerMaintanance maintanance = (ServerMaintanance) session.get(
					ServerMaintanance.class, 1l);
			if (maintanance == null) {
				maintanance = new ServerMaintanance();
			}
			maintanance.setUnderMaintanance(isUndermaintanance);
			session.saveOrUpdate(maintanance);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		if (ServerConfiguration.isUnderMaintainance())
			req.setAttribute("message", "Server will be under maintainence");
		else
			req.setAttribute("message",
					"Removed  Server from  under maintainence");
		req.setAttribute("CheckedValue",
				ServerConfiguration.isUnderMaintainance());
		dispatch(req, resp, MAINTANANCE_VIEW);
	}
}
