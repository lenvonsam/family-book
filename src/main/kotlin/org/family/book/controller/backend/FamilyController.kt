package org.family.book.controller.backend

import org.family.book.controller.BasicController
import org.family.book.model.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping

@Controller
@RequestMapping("backend/family")
class FamilyController : BasicController() {

	// 创建家庭
	@PostMapping("create")
	@ResponseBody
	fun familyCreate(name: String, req: HttpServletRequest): Map<String, Any> {
		var currentUser: User = req.session.getAttribute("currentUser") as User
		return familyService.createFamily(name, currentUser.id)
	}

	@GetMapping("list")
	@ResponseBody
	fun familyList(req: HttpServletRequest): Map<String, Any> {
		var currentUser = req.session.getAttribute("currentUser") as User
		return familyService.familyByCreator(currentUser.phone)
	}

	// 选择家庭
	@PostMapping("chooseUserFamily")
	@ResponseBody
	fun chooseUserFamily(familyId: Int, req: HttpServletRequest): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			val currentUser = req.session.getAttribute("currentUser") as User
			familyService.updateFamilyChoosed(currentUser.id, familyId)
			currentUser.choosedFamily = familyService.findByOne(familyId)
			userService.save(currentUser)
			req.session.setAttribute("currentUser", currentUser)
			result.put("returnCode", 0)
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}
}