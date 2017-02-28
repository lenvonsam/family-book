package org.family.book.service

import org.springframework.stereotype.Service
import com.alibaba.fastjson.JSONObject

@Service
class CommonService {
	fun mapToString(map:Map<String,Any>) = JSONObject.toJSONString(map)
}