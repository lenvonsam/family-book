package org.family.book.controller.backend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping

@Controller
@RequestMapping("/backend")
class HomeController {
	
	//后台主页
	@GetMapping("")
	fun homeIndex() = "backend/index"
}