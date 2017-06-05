package org.family.book.controller.backend

import org.family.book.controller.BasicController
import org.family.book.model.Classify
import org.family.book.model.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("backend/classify")
class ClassifyController : BasicController() {

	@GetMapping("")
	fun classifyIndex(): String {
		return "backend/classify/index"
	}

	@GetMapping("new")
	fun classifyNew() = "backend/classify/new"

	@GetMapping("/{id}")
	fun classifyEidt(@PathVariable("id") id: String): String {
		println("classify id:>>$id")
//		m.addAttribute("classifyObj")
		return "backend/classify/edit"
	}

	@PostMapping("create")
	fun classifyCreate(classifyObj: Classify): String {
		val user = req.session.getAttribute("currentUser") as User
		if (classifyService.save(classifyObj, user)) return "redirect:./" else return "redirect:./new"
	}


}