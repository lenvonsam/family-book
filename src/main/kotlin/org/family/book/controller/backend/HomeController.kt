package org.family.book.controller.backend

import org.family.book.controller.BasicController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("backend")
class HomeController: BasicController() {
	
	//后台主页
	@GetMapping("")
	fun homeIndex() = "backend/index"
}