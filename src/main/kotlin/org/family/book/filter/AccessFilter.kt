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

//@Component
open class AccessFilter: Filter {

	private val log = LoggerFactory.getLogger(AccessFilter::class.java)
	
	override fun init(filterConfig: FilterConfig) {
		throw UnsupportedOperationException()
		log.info(">>>>")
	}

	override fun destroy() {
		throw UnsupportedOperationException()
	}

	override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
		throw UnsupportedOperationException()
		log.info("accessFilter doFilter-----------------")
		val req:HttpServletRequest = request as HttpServletRequest
//		if (this.shouldPrintLog(req.getRequestURI())){
//		StringBuffer sb = new StringBuffer(
//				String.format("[%s] %s\nParameters: \n", req.getMethod().toUpperCase(), req.getRequestURI()));
//		Map<String, String[]> params = req.getParameterMap();
//		for (String key : params.keySet()) {
//			String[] pa = params.get(key);
//			String line = String.format("\t%s = %s\n", key, StringUtils.join(pa));
//			sb.append(line);
//		}
//		sb.append("\nSession Attributes: \n");
//		Enumeration<String> attrs = req.getSession().getAttributeNames();
//		while (attrs.hasMoreElements()) {
//			String key = attrs.nextElement();
//			String line = String.format("\t%s = %s\n", key, req.getSession().getAttribute(key));
//			sb.append(line);
//		}
//
//		log.info(sb.toString());
////		}
//		if(shouldPrintLog(req.requestURI)) {
//			val params:Map<String,Array<String>> = req.parameterMap
//			var strBuff = StringBuffer().append("[${req.method}]\nParameters:\n ${req.method.capitalize()}, ${req.requestURI}")
//			for(key in params.keys) {
//				strBuff.append("\t$key = ${StringUtils.join(params.get(key))}")
//			}
//			log.info(strBuff.toString())
//		}
		chain.doFilter(request, response);
	}

//	public fun shouldPrintLog(url: String) = !arrayOf("/js", "/img", "/css", "/lib", "/favicon.ico").contains(url)
}
