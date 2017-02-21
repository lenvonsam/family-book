package org.family.book.interceptor

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class CsrfInterceptor: HandlerInterceptorAdapter() {

	override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
		println(">>>>preHandle")
		return super.preHandle(request, response, handler)
	}

	override fun postHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, modelAndView: ModelAndView?) {
		super.postHandle(request, response, handler, modelAndView)
	}
}