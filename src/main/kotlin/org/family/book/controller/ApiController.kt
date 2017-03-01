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
import org.springframework.scheduling.annotation.Async
import java.util.concurrent.Callable

//所有api接口
@RestController
@RequestMapping("api")
class ApiController : BasicController() {

	@GetMapping("signGetCode")
	fun signGetCode(phone: String): Map<String, Any> {
		return avosService.userRegisterSMSCode(phone)
	}

	@PostMapping("registerByMobile")
	fun registerByMobile(phone: String, code: String): Map<String, Any> {
		return avosService.signUpOrLoginByMobilePhone(phone, code)
	}


	@PostMapping("mobileLogin")
	fun loginByMobile(phone: String, code: String): Map<String, Any> {
		println("api phone:>>$phone;code:>>$code")
//		return avosService.signUpOrLoginByMobilePhone(phone, code)
		//用于测试
		return avosService.loginByMobilePhoneNumber(phone, code)
	}


	@PostMapping("updateUser")
	fun updateUser(userId: String, nickname: String?): Map<String, Any> {
		var params = HashMap<String, Any>()
		println("userid:>>.$userId;nickname:>>>$nickname")
		if (nickname != null) params.put("nickname", nickname)
		return avosService.updateUser(params, userId)
// FXIME 暂时保留Callable 2017.2.28 探索
//		return object : Callable<Map<String, Any>> {
//			@Throws(Exception::class)
//			override fun call(): Map<String, Any> {
//				avosService.updateObject("_User", params, userId).success {
//					println("success")
//					result.put("returnCode", 0)
//				} fail { e ->
//					println("error")
//					result.put("returnCode", -1)
//					result.put("errMsg", e.message as String)
//				}
//				Thread.sleep(1 * 1000L) // 暂停2秒
//				println("return-----------------")
//				return result
//			}
//		}
	}


	@PostMapping("createFamily")
	fun createFamily(name: String): Map<String, Any> {
		return avosService.createFamily(name)
	}

	@PostMapping("joinFamily")
	fun joinFamily(familyId: String, addMember: String): Map<String, Any> {
		return avosService.joinFamily(familyId, addMember)
	}

	//查询一个家庭里面的成员
	@GetMapping("queryFamilyMembers")
	fun queryFamilyMember(familyId: String): Map<String, Any> {
		return avosService.queryFamilyMember(familyId)
	}


}