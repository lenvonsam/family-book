package org.family.book.controller

import com.alibaba.fastjson.JSONObject
import org.apache.commons.codec.binary.Base64
import org.family.book.model.Classify
import org.family.book.model.User
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.ArrayList
import java.util.HashMap

//所有api接口
@RestController
@RequestMapping("api")
class ApiController : BasicController() {

	private val log = LoggerFactory.getLogger(ApiController::class.java)

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
//		return avosService.loginByMobilePhoneNumber(phone, code)
		var result = HashMap<String, Any>()
		var user = userService.loginByPhoneAndPassword(phone, code)
		if (user == null) {
			result.put("returnCode", -1)
			result.put("errMsg", "用户名或密码错误")
		} else {
			result.put("returnCode", 0)
			result.put("user", user)
		}
		return result
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
	fun createFamily(name: String, userid: String): Map<String, Any> {
//		return avosService.createFamily(name)
		return familyService.createFamily(name, Integer.parseInt(userid))
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

	//获取账本分类列表
	@GetMapping("getAccountClassifyList")
	fun getAccountClassifyList(familyId: Int, userId: Int, type: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		result.put("returnCode", 0)
		val list = classifyService.listByFamilyUser(userService.findByOne(userId), familyService.findByOne(familyId), type)
		result.put("classifyList", list)
		val nameArray = ArrayList<String>()
		list.map { c ->
			nameArray.add(c.name)
		}
		result.put("names", nameArray)
		return result
	}

	//获取账本列表
	@GetMapping("getAccountbookList")
	fun getAccountbookList(familyId: Int, userId: Int, currentPage: Int): Map<String, Any> {
		var type = if (req.getParameter("type") == null) "" else req.getParameter("type")
		var word = if (req.getParameter("word") === null) "" else "%" + req.getParameter("word") + "%"
		return accountBookService.getAccountBookListPg(userId, familyId, currentPage, type, word)
	}

	//新增账本分类
	@PostMapping("createClassify")
	fun createClassify(classifyObj: Classify, userId: Int): Map<String, Any> {
		val currentUser = userService.findByOne(userId)
		val result = HashMap<String, Any>()
		if (classifyService.save(classifyObj, currentUser)) result.put("returnCode", 0) else result.put("returnCode", -1)
		return result
	}

	//账本分类列表
	@GetMapping("getClassifyList")
	fun getClassifyList(userId: Int, currentPage: Int): Map<String, Any> {
		var type = if (req.getParameter("type") == null) "" else req.getParameter("type")
		var word = if (req.getParameter("word") === null) "" else "%" + req.getParameter("word") + "%"
		println("search word:>>${word}")
		val user = userService.findByOne(userId)
		return classifyService.getClassifyListPg(user.id, user.choosedFamily?.id!!, currentPage, type, word)
	}

	//更新账本分类
	@PostMapping("updateClassify")
	fun updateClassify(id: Int, name: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		if (classifyService.update(id, name)) result.put("returnCode", 0) else result.put("returnCode", -1)
		return result
	}

	// 判断微信小程序用户是否之前登录过
	@GetMapping("wxUserLogin")
	fun wxUserLogin(code: String, encryptedData: String, iv: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		val sessionkey = wxMinService.getSessionKey(code)
		val sessionObj = JSONObject.parseObject(sessionkey)
		val openid = sessionObj.getString("openid")
		val currentUser = userService.findByOpenId(openid)
		val wxDecrypt = wxMinService.wxaesDecrypt(Base64.decodeBase64(encryptedData.replace(" ", "+")), Base64.decodeBase64(sessionObj.get("session_key").toString()), Base64.decodeBase64(iv.replace(" ", "+")));
		if (wxDecrypt.size > 0) {
			var resultResp = String(wxMinService.WxPKCS7Decode(wxDecrypt))
			var resultObj = JSONObject.parseObject(resultResp)
			if (currentUser == null) {
				log.info("=========== create new User ================")
				// 创建用户
				var newUser = User(resultObj.getString("nickName"))
				newUser.avatarUrl = resultObj.getString("avatarUrl")
				newUser.gender = if (resultObj.getString("gender").equals("1")) "男" else "女"
				newUser.openId = openid
				userService.save(newUser)
				if (newUser.id > 0) {
					result.put("userid", newUser.id)
				}
				result.put("needCreateFamily", 1)
			} else {
				// 更新用户
				log.info("========== update user =================")
				currentUser.avatarUrl = resultObj.getString("avatarUrl")
				currentUser.gender = if (resultObj.getString("gender").equals("1")) "男" else "女"
				currentUser.nickname = resultObj.getString("nickName")
				userService.save(currentUser)
				var f = currentUser.choosedFamily
				result.put("userid", currentUser.id)
				result.put("needCreateFamily", if (f == null) 1 else 0)
				result.put("familyName", f!!.name)
				result.put("familyId", f.id.toString())
			}
			result.put("returnCode", 0)
		} else {
			log.error("访问加密异常")
			result.put("returnCode", -1)
			result.put("errMsg", "非法的访问")
		}
		return result
	}

	// 获取根据用户openid获取其账本列表
	@GetMapping("wxMinFamilyList")
	fun wxMinFamilyList(userid: Int): Map<String, Any> = familyService.familyByOpenId(userid)

	// 更新选择账本
	@PostMapping("chooseFamily")
	fun chooseFamily(familyId: Int, userId: Int): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			familyService.updateFamilyChoosed(userId, familyId)
			var currentUsr = userService.findByOne(userId)
			val family = familyService.findByOne(familyId)
			currentUsr.choosedFamily = family
			userService.save(currentUsr)
			result.put("returnCode", 0)
			result.put("familyId", family.id.toString())
			result.put("familyName", family.name)
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

}