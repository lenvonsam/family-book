package org.family.book.interceptor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken

@Component
class CsrfInterceptor : HandlerInterceptorAdapter() {

	private val log = LoggerFactory.getLogger(CsrfInterceptor::class.java)

	override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
		println(">>>>preHandle")
		return super.preHandle(request, response, handler)
	}

	override fun postHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, modelAndView: ModelAndView?) {
		log.info("csrf post handle:>>>")
		log.info("csrf parametername:>>>" + (request?.getAttribute("_csrf") as CsrfToken).parameterName)
		modelAndView?.addObject("_csrf", (request?.getAttribute("_csrf") as CsrfToken).token);
		modelAndView?.addObject("_base", request?.contextPath);
		super.postHandle(request, response, handler, modelAndView)
	}
}