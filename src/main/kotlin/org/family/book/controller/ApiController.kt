package org.family.book.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap
import com.alibaba.fastjson.JSONObject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVUser

//所有api接口
@RestController
@RequestMapping("api")
class ApiController : BasicController() {

	@GetMapping("signGetCode")
	fun signGetCode(phone: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		avosService.userRegisterSMSCode(phone).success {
			result.put("returnCode", 0)
		} fail { e ->
			result.put("returnCode", -1);
			result.put("errMsg", e.message as String)
		}
		return result
	}

	@PostMapping("registerByMobile")
	fun registerByMobile(phone: String, code: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		println("api phone:>>$phone;code:>>$code")
		avosService.signUpOrLoginByMobilePhone(phone, code).success {
			result.put("returnCode", 0)
		}.fail { e ->
			result.put("returnCode", -1)
			result.put("errMsg", e.message as String)
		}
		return result
	}


	@PostMapping("mobileLogin")
	fun loginByMobile(phone: String, code: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		println("api phone:>>$phone;code:>>$code")
		avosService.signUpOrLoginByMobilePhone(phone, code).success {
			result.put("returnCode", 0)
			result.put("user", AVUser.getCurrentUser())
		}.fail { e ->
			result.put("returnCode", -1)
			result.put("errMsg", e.message as String)
		}
		return result
	}

	@PostMapping("updateUser")
	fun updateUser(userId: String, nickname: String?): Map<String, Any> {
		var result = HashMap<String, Any>()
		var params = HashMap<String, Any>()
		println("userid:>>.$userId;nickname:>>>$nickname")
		if (nickname != null) params.put("nickname", nickname)
		avosService.updateObject("_User", params, userId).success {
			result.put("returnCode", 0)
		} fail { e ->
			result.put("returnCode", -1)
			result.put("errMsg", e.message as String)
		}
		return result
	}
}