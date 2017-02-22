package org.family.book

import org.family.book.interceptor.CsrfInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.beans.factory.annotation.Value

@Configuration
open class InterceptorConfig : WebMvcConfigurerAdapter() {

	private val log = LoggerFactory.getLogger(InterceptorConfig::class.java)

	@Autowired
	lateinit var csrfInterceptor: CsrfInterceptor

	override fun addInterceptors(registry: InterceptorRegistry) {

		registry.addInterceptor(csrfInterceptor).addPathPatterns("/**")
	}
}