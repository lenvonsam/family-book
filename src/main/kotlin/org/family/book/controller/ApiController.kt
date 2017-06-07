package org.family.book.controller

import com.alibaba.fastjson.JSONObject
import org.apache.commons.codec.binary.Base64
import org.family.book.model.AccountBook
import org.family.book.model.Classify
import org.family.book.model.User
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.HashMap
import javax.servlet.http.HttpServletRequest

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

	//创建账本
	@PostMapping("createAccountBook")
	fun createAccountBook(userid: Int, ab: AccountBook, recordDate: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		log.info(ab.payType)
		try {
			val u = userService.findByOne(userid)
			ab.currentFamily = u.choosedFamily!!
			ab.currentUser = u
			ab.owerName = u.nickname
			val sdf = SimpleDateFormat("yyyy-MM-dd")
			val recordTime = Timestamp(sdf.parse(recordDate).time)
			ab.recordTime = recordTime
			accountBookService.save(ab)
			result.put("returnCode", 0)
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//更新账本
	@PostMapping("updateAccountBook")
	fun updateAccountBook(userid: Int, ab: AccountBook, recordDate: String, id: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		var originAb = accountBookService.findOne(Integer.parseInt(id))
		try {
			val u = userService.findByOne(userid)
			originAb.owerName = u.nickname
			val sdf = SimpleDateFormat("yyyy-MM-dd")
			val recordTime = Timestamp(sdf.parse(recordDate).time)
			originAb.recordTime = recordTime
			originAb.price = ab.price
			originAb.classifyName = ab.classifyName
			originAb.payType = ab.payType
			originAb.remarks = ab.remarks
			accountBookService.save(originAb)
			result.put("returnCode", 0)
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	// 删除单个记录
	@PostMapping("accountbookDel")
	fun accountbookDel(userid: Int, abid: Int): Map<String, Any> {
		var result = HashMap<String, Any>()
		//判断是不是当前用户操作
		val ab = accountBookService.findOne(abid)
		if (ab.currentUser.id == userid) {
			accountBookService.delObj(ab)
			result.put("returnCode", 0)
		} else {
			result.put("returnCode", -1)
			result.put("errMsg", "无效请求")
		}
		return result
	}

	// 获取一个月的账本情况
	@GetMapping("getMonthAbList")
	fun getMonthAbList(userid: Int, req: HttpServletRequest): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var fixTime: String = req.getParameter("recordTime")
			var u = userService.findByOne(userid)
			var f = u.choosedFamily!!
			var list: List<AccountBook> = accountBookService.findMonthRecords(fixTime, f.id!!, u.id)
			var dsdf = SimpleDateFormat("yyyy-MM-dd")
			var monthRecords = list.groupBy { t -> dsdf.format(t.recordTime) }
			var subMonthRecord = HashMap<String, Any>()
			monthRecords.keys.forEach { s ->
				var sl = monthRecords.get(s)
				var slpay = sl?.filter { sp -> sp.recordType == "支出" }?.sumByDouble { sprice -> sprice.price }
				var slReceiver = sl?.filter { sp -> sp.recordType == "收入" }?.sumByDouble { sprice -> sprice.price }
				var prMap = HashMap<String, Any>()
				prMap.put("pay", slpay!!)
				prMap.put("receiver", slReceiver!!)
				subMonthRecord.put(s, prMap)
			}
			result.put("returnCode", 0)
			val totalPay = accountBookService.sumMonthRecords(fixTime, f.id!!, u.id, "支出")
			val totalReceive = accountBookService.sumMonthRecords(fixTime, f.id!!, u.id, "收入")
			result.put("payMonthTotal", if(totalPay == null) 0 else totalPay)
			result.put("receiveMonthTotal", if(totalReceive == null) 0 else totalReceive)
			result.put("monthDate", "${fixTime.substring(6)}/${fixTime.substring(0, 4)}")
			result.put("monthRecords", monthRecords)
			result.put("dayRecords", subMonthRecord)
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}

		return result
	}

	//获取一年的线型报表信息
	@GetMapping("getYearLineData")
	fun getYearLineData(familyid: Int, year: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		val list = accountBookService.getYearData(familyid, year)
		var monthArray = Array(12, { i -> i + 1 })
		var payArray = Array(12, { 0.00 })
		var receArray = Array(12, { 0.00 })
		var balanceArray = Array(12, { 0.00 })
		list.map { arr ->
			var month: Int = Integer.parseInt(arr[0].toString())
			var pay = arr[1].toString().toDouble()
			var receive = arr[2].toString().toDouble()
			var monthIndex = monthArray.indexOf(month)
			payArray[monthIndex] = pay
			receArray[monthIndex] = receive
			balanceArray[monthIndex] = receive - pay
		}
		result.put("list", list)
		result.put("payArr", payArray)
		result.put("receiveArr", receArray)
		result.put("balanceArr", balanceArray)
		result.put("returnCode", 0)
		result.put("listTotal", accountBookService.getYearDataTotal(familyid, year))
		return result
	}
	
	// 获取固定日期的报表信息
	@GetMapping("getMonthPieData")
	fun getMonthPieData(familyid:Int, type:String):Map<String,Any> {
		var result = HashMap<String,Any>()
		var beginDate = ""
		var endDate = ""
		if (req.getParameter("monthDate") != null) {
			var sdf = SimpleDateFormat("yyyy-MM-dd")
			var days = commService.firstAndEndOfMonth(sdf.parse(req.getParameter("monthDate")))
			beginDate = days.get("beginDate").toString()
			endDate = days.get("endDate").toString()
		}
		if(req.getParameter("beginDate") !=null && req.getParameter("endDate") != null) {
			beginDate = req.getParameter("beginDate")
			endDate = req.getParameter("endDate")
		}
		val list = accountBookService.getMonthClassifies(type,familyid,beginDate,endDate)
		var mList:List<HashMap<String,Any>> = ArrayList<HashMap<String,Any>>()
		list.map { l ->
			var tem = HashMap<String,Any>()
			tem.put("name", l[0])
			tem.put("data", l[1])
//			mList.
		}
		
		return result
	}
}

//fun main(args: Array<String>) {
////	AVOSCloud.initialize("txQtqW5ROfawVxd8RgfAr3f0-gzGzoHsz", "6qmUf9FLjJEbUoXyeMB0TwhB", "EHG4S391TDinBFzXjjLtCtbs")
//	val d = Calendar.getInstance()
//	d.time = Date()
//	d.set(Calendar.DAY_OF_MONTH,1)
//	println(d.getTime())
//	d.time = Date()
//	d.add(Calendar.MONTH,1)
//	d.set(Calendar.DATE,1)
//	d.add(Calendar.DATE,-1)
//	println(d.getTime())
//}