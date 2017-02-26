package org.family.book.controller.backend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping

@Controller
@RequestMapping("/backend")
class LoginController {
	
	@GetMapping("login")
	fun backLogin() = "backend/login"
	
	@GetMapping("register")
	fun backRegister() = "backend/register"
	
	//TODO 忘记密码
	
}
