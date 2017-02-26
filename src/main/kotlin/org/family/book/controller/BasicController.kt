package org.family.book.controller

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.family.book.service.AvosService

@Controller
open class BasicController {
	@Autowired
	lateinit var avosService: AvosService

}