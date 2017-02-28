package org.family.book.service

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.avos.avoscloud.SaveCallback

@Service
open class AvosService {
	private val log = LoggerFactory.getLogger(AvosService::class.java)

	@Autowired
	lateinit var commonService: CommonService

	fun updateObject(objectName: String, objectValueMap: Map<String, Any>, objectId: String): Promise<Any, Exception> {
		var dfd = deferred<Any, Exception>()
		var obj = AVObject.createWithoutData(objectName, objectId)
		objectValueMap.keys.map { key ->
			print(key)
			obj.put(key, objectValueMap.get(key))
		}
		obj.saveInBackground(object : SaveCallback() {
			override fun done(e: AVException?) {
				if (e == null) {
					println("resolve=====")
					dfd.resolve("")
				} else {
					println("reject")
					dfd.reject(e)
				}
			}
		})
		return dfd.promise
	}

	fun saveObject(objectName: String, objectValueMap: Map<String, Any>): Promise<Any, Exception> {
		val dfd = deferred<Any, Exception>()
		var newObject = AVObject(objectName);
		objectValueMap.keys.map { key ->
			newObject.put(key, objectValueMap.get(key))
		}
		newObject.saveInBackground(object : SaveCallback() {
			override fun done(e: AVException?) {
				if (e == null) {
					dfd.resolve("")
				} else {
					dfd.reject(e)
				}
			}
		})
		return dfd.promise
	}

	//用户手机登录获取验证码
	fun userRegisterSMSCode(phone: String): Promise<Any, Exception> {
		var dfd = deferred<Any, Exception>()
		try {
			AVOSCloud.requestSMSCode(phone)
			dfd.resolve("");
		} catch(e: AVException) {
			e.printStackTrace()
			log.error("获取手机登录验证码异常:", e)
			dfd.reject(e)
		}
		return dfd.promise
	}

	//用手机号登录
	fun signUpOrLoginByMobilePhone(phone: String, code: String): Promise<Any, Exception> {
		var dfd = deferred<Any, Exception>()
		try {
			AVUser.signUpOrLoginByMobilePhone(phone, code)
			dfd.resolve("")
		} catch(e: Exception) {
			log.error("手机号登录异常:", e)
			dfd.reject(e)
		}
		return dfd.promise
	}
}