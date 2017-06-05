package org.family.book.filter

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
open class AccessFilter : Filter {

	private val log = LoggerFactory.getLogger(AccessFilter::class.java)

	override fun init(filterConfig: FilterConfig) {
	}

	override fun destroy() {
	}

	@Throws(Exception::class)
	override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
		val req: HttpServletRequest = request as HttpServletRequest
//		log.info("req URI:>>" + req.requestURI)
		if (shouldPrintLog(req.requestURI)) {
			val params: Map<String, Array<String>> = req.parameterMap
			var strBuff = StringBuffer().append("[${req.method}]\nParameters:\n ${req.method.capitalize()}, ${req.requestURI}")
			for (key in params.keys) {
				strBuff.append("\t$key = ${StringUtils.join(params.get(key))}")
			}
			log.info(strBuff.toString())
		}
		chain.doFilter(request, response);
	}

	//FIXME 正则表达式
	private val x = "/^/"

	private fun shouldPrintLog(url: String): Boolean {
		arrayOf("/js", "/img", "/css", "/lib", "/favicon.ico","/fonts").map { item ->
			if (url.startsWith(item)) return false
		}
		return true
	}
}
