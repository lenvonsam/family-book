package org.family.book.filter

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

//@Component
class TestFilter: Filter {
	override fun init(filterConfig: FilterConfig) {
		throw UnsupportedOperationException()
		println(">>>>TestFilter")
	}

	override fun destroy() {
		throw UnsupportedOperationException()
	}

	override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
		throw UnsupportedOperationException()
	}
	
	
}