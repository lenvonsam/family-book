package org.family.book.controller

import org.family.book.service.AccountBookService
import org.family.book.service.AvosService
import org.family.book.service.ClassifyService
import org.family.book.service.FamilyService
import org.family.book.service.UserService
import org.family.book.service.WxMinService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
open class BasicController {
	@Autowired
	lateinit var avosService: AvosService

	@Autowired
	lateinit var resp: HttpServletResponse

	@Autowired
	lateinit var req: HttpServletRequest

	@Autowired
	lateinit var userService: UserService

	@Autowired
	lateinit var familyService: FamilyService

	@Autowired
	lateinit var classifyService: ClassifyService

	@Autowired
	lateinit var accountBookService: AccountBookService

	@Autowired
	lateinit var wxMinService: WxMinService

}