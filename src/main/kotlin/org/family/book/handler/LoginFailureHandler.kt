package org.family.book.handler

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class LoginFailureHandler : AuthenticationFailureHandler {

	@Throws(Exception::class)
	override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
		response?.sendRedirect(request?.contextPath + "/backend/login")
	}
}