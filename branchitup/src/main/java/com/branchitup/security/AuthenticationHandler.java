package com.branchitup.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.system.Constants;

public class AuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler{
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
	throws IOException, ServletException {
		String url = (String) request.getSession().getAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL);
		// System.out.println("------> login to " + url);
		if (url != null) {
			this.setDefaultTargetUrl(url);
		} else {
			this.setDefaultTargetUrl("/");
		}
		// I think you should call for the principal before super???
		Principal principal = request.getUserPrincipal();
		super.onAuthenticationSuccess(request, response, authentication);
		Transaction transaction = null;
		if (principal != null) {
			try {
				Session session = this.sessionFactory.openSession();
				transaction = session.beginTransaction();
				UserAccount user = StatelessDAO.findUserAccountByEmail(session, principal.getName()); 
				if (user != null) {
					user.setLoginCount(user.getLoginCount() + 1);
					user.setLastLogIn(new Date());
					request.getSession().setAttribute(UserAccount.class.getSimpleName(), user);
					request.getSession().setMaxInactiveInterval(14400); // 4 hours
//					logger.trace("--->postSuccessAuthHandler: put user account in session " + user);
					transaction.commit();
					session.flush();
				}
			} 
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				if(transaction != null){
					transaction.rollback();
				}
			}
		}
	}
}
