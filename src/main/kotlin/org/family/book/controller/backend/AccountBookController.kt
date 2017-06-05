package org.family.book.controller.backend

import org.family.book.controller.BasicController
import org.family.book.model.AccountBook
import org.family.book.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.ui.Model

@Controller
@RequestMapping("backend/accountBook")
class AccountBookController : BasicController() {

	private val log = LoggerFactory.getLogger(AccountBookController::class.java)

	@GetMapping("")
	public fun index() = "backend/accountbook/index"

	@GetMapping("edit/{id}")
	fun eidt(@PathVariable("id") id: Int, m: Model): String {
		m.addAttribute("accountBook", accountBookService.findOne(id))
		return "backend/accountbook/edit"
	}

	@GetMapping("new")
	public fun pageNew(): String = "backend/accountbook/new"

	@PostMapping("create")
	public fun pageCreate(ab: AccountBook, recordDate: String): String {
		//设置账户相关信息
		val user = req.session.getAttribute("currentUser") as User
		ab.currentFamily = user.choosedFamily!!
		ab.currentUser = user
		ab.owerName = user.nickname
		//string to timeStamp
		var rDate = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(recordDate)
		ab.recordTime = Timestamp(rDate.time)
		if (accountBookService.save(ab)) {
			log.info("保存成功")
			return "redirect:../"
		} else {
			log.info("保存失败")
			return "redirect:../new"
		}
	}

}