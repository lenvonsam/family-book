package org.family.book.service

import com.avos.avoscloud.AVObject
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.springframework.stereotype.Service

@Service
open class AvosService {
	
	fun saveObject(objectName:String,objectValueMap:Map<String,Any>):Promise<Any,Exception> {
//	var testObject = AVObject("TestObject");
//		testObject.put("words", "Hello World!");
//		testObject.save()
		val dfd = deferred<Any,Exception>()
		var newObject = AVObject(objectName);
		objectValueMap.mapKeys { key ->
//			val keyVal:Any?= objectValueMap.get(key)
			println(key)
		 }
		return dfd.promise
	}
}