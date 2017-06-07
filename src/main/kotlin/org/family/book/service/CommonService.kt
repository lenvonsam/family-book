package org.family.book.service

import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap

@Service
class CommonService {
	fun mapToString(map: Map<String, Any>) = JSONObject.toJSONString(map)

	fun firstAndEndOfMonth(date: Date, format: String = "yyyy-MM-dd"): Map<String, String> {
		var result = HashMap<String, String>()
		val sdf = SimpleDateFormat(format)
		val d = Calendar.getInstance()
		d.time = date
		d.set(Calendar.DAY_OF_MONTH, 1)
		result.put("beginDate", sdf.format(d.getTime()))
		d.time = date
		d.add(Calendar.MONTH, 1)
		d.set(Calendar.DATE, 1)
		d.add(Calendar.DATE, -1)
		result.put("endDate", sdf.format(d.getTime()))
		return result
	}
}