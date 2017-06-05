package org.family.book

import org.family.book.handler.LoginFailureHandler
import org.family.book.handler.LoginSuccessHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest
import org.family.book.service.AuthService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {


	private val log = LoggerFactory.getLogger(WebSecurityConfig::class.java)

	@Autowired
	lateinit var loginSuccessHandler: LoginSuccessHandler

	@Autowired
	lateinit var loginFailureHandler: LoginFailureHandler

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http.authorizeRequests()
				.antMatchers("/api/**", "/error",  "/js/**", "/css/**", "/i/**", "/img/**", "/fonts/**").permitAll()
//				.antMatchers("/users/**").hasAuthority("ADMIN")
				.anyRequest().fullyAuthenticated()
				.and().csrf()
//				// disable csrf protection url
				.requireCsrfProtectionMatcher(AllExceptUrls(arrayOf("http://jarvis.eurus.cn/projects/9/webhook", "/api/v1/pingxx/notify")))
				.and()
				.formLogin()
				.loginPage("/backend/login")
				.usernameParameter("name").passwordParameter("password")
//				//登录成功
				.successHandler(loginSuccessHandler)
				//登录失败
				.failureHandler(loginFailureHandler)
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/backend/logout")
				.logoutSuccessUrl("/backend/login")
				.permitAll()
	}

	@Autowired
	lateinit var authService: AuthService

	@Throws(Exception::class)
	override fun configure(auth: AuthenticationManagerBuilder?) {
		auth?.userDetailsService(authService)
//		auth?.inMemoryAuthentication()?.withUser("admin")?.password("admin")?.roles("USER")
	}

	private open class AllExceptUrls : RequestMatcher {
		private val ALLOWED_METHODS = arrayOf("GET", "HEAD", "TRACE", "OPTIONS")
		private val allowedUrls: Array<String>

		constructor(allowedUrls: Array<String>) {
			this.allowedUrls = allowedUrls
		}

		override fun matches(request: HttpServletRequest): Boolean {
			var method = request.method
			ALLOWED_METHODS.map { mtd ->
				if (mtd.equals(method)) return false
			}
			var uri = request.requestURI
			println("uri:>>>>" + uri)
			this.allowedUrls.map { url ->
				if (uri.startsWith("/api")) {
					return false
				} else {
					if (url.equals(uri)) return false
				}
			}
			return true
		}
	}


}