package org.family.book.controller.backend

import org.family.book.controller.BasicController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.HashMap

@Controller
@RequestMapping("backend/classify")
class ClassifyController : BasicController() {

	@GetMapping("")
	fun classifyIndex(): String {
		var reqMap = HashMap<String, Any>()
		reqMap.put("returncode", "123")
		reqMap.put("xxx", "456")
		avosService.saveObject("test", reqMap).success {
			println("success==============$it")
		}.fail {
			println("error")
		}
		println("next")
		return "backend/classify/index"
	}

	@GetMapping("new")
	fun classifyNew() = "backend/classify/new"

	@GetMapping("/{id}")
	fun classifyEidt(@PathVariable("id") id:String,m:Model): String {
		println("classify id:>>$id")
//		m.addAttribute("classifyObj")
		return "backend/classify/edit"
	}
	
	

}