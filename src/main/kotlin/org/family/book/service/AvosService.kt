package org.family.book.service

import com.avos.avoscloud.AVObject
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.springframework.stereotype.Service

@Service
open class AvosService {

	fun saveObject(objectName: String, objectValueMap: Map<String, Any>): Promise<Any, Exception> {
//	var testObject = AVObject("TestObject");
//		testObject.put("words", "Hello World!");
//		testObject.save()
		val dfd = deferred<Any, Exception>()
		var newObject = AVObject(objectName);
		objectValueMap.keys.map { key ->
			val keyVal = objectValueMap.get(key)
			println(key)
			println(keyVal)
		}
		var testObject = AVObject("TestObject");
		testObject.put("words", "Hello World!");
		testObject.save()
		println("testObject.oid:>>>" + testObject.objectId)
		if (testObject.objectId.length > 0) {
			dfd.resolve(testObject)
		} else {
			dfd.resolve("error")
		}


		return dfd.promise
	}
}