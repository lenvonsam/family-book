package org.family.book.controller

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.family.book.service.AvosService
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.family.book.service.CommonService

@Controller
open class BasicController {
	@Autowired
	lateinit var avosService: AvosService

	@Autowired
	lateinit var resp: HttpServletResponse

	@Autowired
	lateinit var req: HttpServletRequest

}