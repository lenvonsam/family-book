package org.family.book.service

import com.alibaba.fastjson.TypeReference
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.AVRelation
import com.avos.avoscloud.AVUser
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.HashMap

@Service
open class AvosService {
	private val log = LoggerFactory.getLogger(AvosService::class.java)
//	private var currentUser: AVUser? = AVUser::class.objectInstance

	@Autowired
	lateinit var commonService: CommonService

	fun updateUser(valueMap: Map<String, Any>, userId: String): Map<String, Any> {
//		var currentUser = AVUser.getCurrentUser()
//		log.info(">>>>$currentUser")
		var result = HashMap<String, Any>()
		try {
			var currentUser = AVUser.getCurrentUser()
			println(currentUser?.objectId)
			if (currentUser?.objectId.equals(userId))
				return simpleSaveOrUpdateResponse(currentUser!!, valueMap)
			else {
				result.put("returnCode", -1)
				result.put("errMsg", "非法登录用户")
				return result
			}
		} catch(e: Exception) {
			log.error("用户保存异常:>>", e)
			result.put("returnCode", -1)
			result.put("errMsg", "用户未登录")
			return result
		}

	}

	fun updateObject(objectName: String, objectValueMap: Map<String, Any>, objectId: String): Map<String, Any> {
		return simpleSaveOrUpdateResponse(AVObject.createWithoutData(objectName, objectId), objectValueMap)
		//FIXME 暂时保留 dfd.promise 2017.2.27 探索
//		var dfd = deferred<String,Exception>()
//		obj.saveInBackground(object : SaveCallback() {
//			override fun done(e: AVException?) {
//				println("exception-----------------")
//				println(e)
//				if (e == null) {
//					println("resolve=====")
//					dfd.resolve("")
//				} else {
//					println("reject")
//					dfd.reject(e)
//				}
//			}
//		})
//		return dfd.promise
	}

	fun saveObject(objectName: String, objectValueMap: Map<String, Any>): Map<String, Any> {
		return simpleSaveOrUpdateResponse(AVObject(objectName), objectValueMap)
	}

	private fun simpleSaveOrUpdateResponse(obj: AVObject, params: Map<String, Any>): Map<String, Any> {
		var result = HashMap<String, Any>()
		params.keys.map { k -> obj.put(k, params.get(k)) }
		try {
			obj.save()
			result.put("returnCode", 0)
		} catch(e: Exception) {
			log.error("简单保存失败:>>.", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//用户手机登录获取验证码
	fun userRegisterSMSCode(phone: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			AVOSCloud.requestSMSCode(phone)
			result.put("returnCode", 0)
		} catch(e: AVException) {
			log.error("获取手机登录验证码异常:", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//用手机号登录
	fun signUpOrLoginByMobilePhone(phone: String, code: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var currentUser = AVUser.signUpOrLoginByMobilePhone(phone, code)
			result.put("returnCode", 0)
			result.put("user", currentUser)
		} catch(e: Exception) {
			log.error("手机号登录异常:", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//手机号和密码登录 仅用于测试
	fun loginByMobilePhoneNumber(phone: String, pwd: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		var currentUser = AVUser.loginByMobilePhoneNumber(phone, pwd)
		result.put("returnCode", 0)
		result.put("user", currentUser)
		return result
	}

	//创建家庭
	fun createFamily(name: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var familyObj = AVObject("Family")
			var currentUser = AVUser.getCurrentUser()
			var familyMemberMap = AVObject("FamilyMemberMap")

			familyObj.put("name", name)
			familyObj.put("creator", currentUser.mobilePhoneNumber)
			familyObj.save()
			familyMemberMap.put("member",currentUser)
			familyMemberMap.put("family",familyObj)
			familyMemberMap.save()

//			var relations: AVRelation<AVObject> = familyObj.getRelation("members")
//			relations.add(currentUser)
//			familyObj.save()
			result.put("returnCode", 0)
		} catch(e: Exception) {
			log.error("创建家庭异常:>>>", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//加入家庭
	fun joinFamily(familyId: String, addMember: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var familyObject = AVObject.createWithoutData("FamilyMemberMap", familyId)

			var addUser = AVObject.createWithoutData("_User", addMember)
			var releations: AVRelation<AVObject> = familyObject.getRelation("members")
			releations.add(addUser)
			familyObject.save()
			result.put("returnCode", 0)
		} catch(e: Exception) {
			log.error("加入家庭异常:>>>", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	@JsonIgnore
	fun queryFamilyMember(familyId: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var avQuery:AVQuery<AVObject> = AVQuery("FamilyMemberMap")
			avQuery.whereEqualTo("family",AVObject.createWithoutData("Family",familyId))
			result.put("returnCode", 0)
			println(avQuery.first.toString())
			println(avQuery.first.toJSONObject())
			println(avQuery.find().toList())
			var l:String = avQuery.first.toJSONObject().toString()
			result.put("members",avQuery.first.toJSONObject())
//			result.put("members", query.find())
		} catch(e: Exception) {
			log.error("查询家庭成员异常:>>", e)
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}
}