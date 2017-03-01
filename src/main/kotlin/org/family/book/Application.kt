package org.family.book

import com.avos.avoscloud.AVOSCloud
import org.family.book.filter.AccessFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.web.context.request.RequestContextListener
import com.avos.avoscloud.AVUser

/**
@SpringBootApplication same as @Configuration @EnableAutoConfiguration @ComponentScan
 */
@SpringBootApplication
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
//@EnableWebMvc
open class Application : SpringBootServletInitializer {

	private val log = LoggerFactory.getLogger(Application::class.java)

	constructor()

	@Autowired
	lateinit var accessFilter: AccessFilter

	@Bean
	public fun fewFilterRegistration(): FilterRegistrationBean {
		val registration = FilterRegistrationBean()
		registration.addUrlPatterns("/*")
		registration.setFilter(accessFilter)
		registration.setOrder(Ordered.LOWEST_PRECEDENCE)
		return registration
	}

	override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
		return builder.sources(Application::class.java)
	}


}

fun main(args: Array<String>) {
	AVOSCloud.initialize("txQtqW5ROfawVxd8RgfAr3f0-gzGzoHsz", "6qmUf9FLjJEbUoXyeMB0TwhB", "EHG4S391TDinBFzXjjLtCtbs")
	SpringApplication.run(Application::class.java, *args)
}



