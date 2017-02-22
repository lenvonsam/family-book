package org.family.book

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

@Configuration
open class ResourceConfig {

	public fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/js").addResourceLocations("classpath:/static/js")
			registry.addResourceHandler("/css").addResourceLocations("classpath:/static/css")
			registry.addResourceHandler("/fonts").addResourceLocations("classpath:/static/fonts")
			registry.addResourceHandler("/img").addResourceLocations("classpath:/static/img")
			registry.addResourceHandler("/i").addResourceLocations("classpath:/static/i")
		}
	}

}