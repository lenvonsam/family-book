package org.family.book.interceptor

import org.slf4j.LoggerFactory
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CsrfInterceptor : HandlerInterceptorAdapter() {

	private val log = LoggerFactory.getLogger(CsrfInterceptor::class.java)

	override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
		var session = request!!.session
		log.info("csrf uri>>>>>" + request.requestURI + ";request contextpath:>>${request.contextPath}")
		if (!("${request.contextPath}/backend/login".equals(request.requestURI)) && session != null && request.isRequestedSessionIdValid && session.getAttribute("currentUser") == null) {
			log.info("validate user , then redirect to backlogin")
			response?.sendRedirect(request.contextPath + "/backend/login")
			return false
		}
		return true
	}

	override fun postHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, modelAndView: ModelAndView?) {
		log.info("csrf post handle:>>>")
		modelAndView?.addObject("_csrf", (request?.getAttribute("_csrf") as CsrfToken).token);
		modelAndView?.addObject("_base", request?.contextPath);
		modelAndView?.addObject("currentUser", request?.session?.getAttribute("currentUser"));
		super.postHandle(request, response, handler, modelAndView)
	}
}