package org.family.book.handler

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import org.springframework.stereotype.Component

@Component
class LoginSuccessHandler: AuthenticationSuccessHandler {

	@Throws(Exception::class)
	override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
		response?.sendRedirect(request?.contextPath+"/backend")
	}
	
//	private String getRedirectUrl(HttpServletRequest request, String contextPath, Admin admin) {
//		String redirectUrl;
//		HttpSession session = request.getSession(false);
//		if (session.getAttribute("SPRING_SECURITY_SAVED_REQUEST") == null) {
//			redirectUrl = contextPath + "/backend/bill";
//		} else {
//			SavedRequest sRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
//			log.info(".>>>>>>>defaultSaveRequest:>>" + sRequest.getRedirectUrl());
//			redirectUrl = sRequest.getRedirectUrl();
//		}
//		return redirectUrl;
//	}
}