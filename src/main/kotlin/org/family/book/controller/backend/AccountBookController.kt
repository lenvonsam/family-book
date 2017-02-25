package org.family.book.controller.backend

import com.avos.avoscloud.AVObject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import nl.komponents.kovenant.*

@Controller
@RequestMapping("backend/accountBook")
class AccountBookController {
	@GetMapping("")
	public fun index() = "backend/accountbook/index"

	@GetMapping("new")
	public fun pageNew():String{
		
		return "backend/accountbook/new"
	}
}