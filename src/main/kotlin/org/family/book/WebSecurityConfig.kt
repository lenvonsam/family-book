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
				.antMatchers("/backend", "/js/**","/css/**","/i/**","/img/**","/fonts/**").permitAll()
//				.antMatchers("/users/**").hasAuthority("ADMIN")
				.anyRequest().fullyAuthenticated()
				.and().csrf()
//				// disable csrf protection url
//				.requireCsrfProtectionMatcher(new AllExceptUrls("/bamboo-store/api/v1/pingxx/notify",
//						"http://jarvis.eurus.cn/projects/9/webhook","/api/v1/pingxx/notify"))
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


//	@Throws(Exception::class)
	override fun configure(auth: AuthenticationManagerBuilder?) {
//		auth.userDetailsService()
		auth?.inMemoryAuthentication()?.withUser("admin")?.password("admin")?.roles("USER")
	}


//	private static class AllExceptUrls implements RequestMatcher {
//		private static final String[] ALLOWED_METHODS = new String[] { "GET", "HEAD", "TRACE", "OPTIONS" };
//		private final String[] allowedUrls;
//
//		public AllExceptUrls(String... allowedUrls) {
//			this.allowedUrls = allowedUrls;
//		}
//
//		@Override
//		public boolean matches(HttpServletRequest request) {
//			String method = request.getMethod();
//			for (String allowedMethod : ALLOWED_METHODS) {
//				if (allowedMethod.equals(method)) {
//					return false;
//				}
//			}
//
//			String uri = request.getRequestURI();
//			for (String allowedUrl : allowedUrls) {
//				if (uri.equals(allowedUrl)) {
//					return false;
//				}
//			}
//			return true;
//		}
//
//	}


}