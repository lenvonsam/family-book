package org.family.book.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL

@Service
class HttpService {
	private val log = LoggerFactory.getLogger(HttpService::class.java)

	@Throws(Exception::class)
	fun sendGetRequest(url: String, charset: String = "utf-8"): String {
		log.info("get url:>>>$url")
		var resultBuffer = StringBuffer()
		var httpURLConnection = URL(url).openConnection() as HttpURLConnection
		httpURLConnection.setRequestProperty("Accept-Charset", charset)
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
		if (httpURLConnection.responseCode >= 300) throw Exception("HTTP Request is not success, Response code is ${httpURLConnection.responseCode}")
		val inputStream = httpURLConnection.inputStream
		val inputStreamReader = InputStreamReader(inputStream, charset)
		val reader = BufferedReader(inputStreamReader)
		reader.use { r ->
			var temp = r.readLine()
			if (temp != null) resultBuffer.append(temp)
		}
		reader.close()
		inputStreamReader.close()
		inputStream.close()
		return resultBuffer.toString()
	}

	@Throws(Exception::class)
	fun sendPostRequest(reqBody: String, url: String, charset: String = "utf-8"): String {
		log.info("post params:>>$reqBody;url:>>$url")
		var bufferResult = StringBuffer()
		val conn = URL(url).openConnection()
//		conn.setRequestProperty("Accept-Charset", charset)
		conn.setRequestProperty("accept", "*/*")
		conn.setRequestProperty("connection", "Keep-Alive")
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true)
		conn.setDoInput(true)

		val out = PrintWriter(OutputStreamWriter(conn.outputStream, charset))
		//发送请求
		out.print(reqBody)
		//flush 输出流缓冲
		out.flush()
		var inStream = BufferedReader(InputStreamReader(conn.inputStream, charset))
		inStream.use { r ->
			var temp = r.readLine()
			if (temp != null) bufferResult.append(temp)
		}
		out.close()
		inStream.close()

		return bufferResult.toString()
	}
}