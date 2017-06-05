package org.family.book.handler

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.family.book.repository.UserRepository

@Component
class LoginSuccessHandler : AuthenticationSuccessHandler {

	@Autowired
	lateinit var userRepo: UserRepository

	@Throws(Exception::class)
	override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
		var user = userRepo.findByPhoneAndEnable(authentication!!.name, 1)
		request?.getSession()?.setAttribute("currentUser", user)
		response?.sendRedirect(request?.contextPath + "/backend")
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