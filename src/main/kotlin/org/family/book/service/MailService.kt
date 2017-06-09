package org.family.book.service

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.scheduling.annotation.Async

@Service
@EnableAsync
class MailService(@Value("\${familybook.mail.user}") val fbuser: String, @Value("\${familybook.mail.pwd}") val fbpwd: String, @Value("\${familybook.mail.smtp}") val fbsmtp: String, @Value("\${familybook.mail.admin}") val samadmin: String) {

	@Async
	@Throws(Exception::class)
	fun fbsendTextMail(title: String, content: String) {
		val mail = HtmlEmail()
		mail.setAuthenticator(DefaultAuthenticator(fbuser, fbpwd));
		mail.hostName = fbsmtp
		mail.setCharset("utf-8")
		mail.setFrom(fbuser)
		mail.setSubject(title)
		mail.setContent(content, "text/html;charset=utf-8")
		mail.addTo(samadmin)
		mail.send()
	}
}