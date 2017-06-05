package org.family.book.service

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.AlgorithmParameters
import java.security.Security
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class WxMinService {

	private val log = LoggerFactory.getLogger(WxMinService::class.java)

	// 常量设定
	private final val fbAppid: String = "wx9ac7225a5b2d14dd"
	private final val fbSecret: String = "895d7adbee8ed87c4bb735b7cafb6076"
	private var aesInitalized = false
	private final val BLOCK_SIZE = 32


	// 注入
	@Autowired
	private lateinit var httpService: HttpService

	fun getSessionKey(code: String) = httpService.sendGetRequest("https://api.weixin.qq.com/sns/jscode2session?appid=$fbAppid&secret=$fbSecret&js_code=$code&grant_type=authorization_code")

	fun getOpenid() = ""

	@Throws(Exception::class)
	fun wxaesDecrypt(content: ByteArray, keyByte: ByteArray, ivByte: ByteArray): ByteArray {
		if (!aesInitalized) {
			Security.addProvider(BouncyCastleProvider())
			aesInitalized = true
		}
		val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
		val sKeySpec = SecretKeySpec(keyByte, "AES")
		val ivParams = AlgorithmParameters.getInstance("AES")
		ivParams.init(IvParameterSpec(ivByte))
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, ivParams)
		return cipher.doFinal(content)
	}

	fun WxPKCS7Encode(count: Int): ByteArray {
		var amountToPad = BLOCK_SIZE - count.mod(BLOCK_SIZE)
		if (amountToPad == 0) {
			amountToPad = BLOCK_SIZE
		}
		val padChr = chr(amountToPad)
		var tmp: String = ""
		var index: Int = 0
		while (index < amountToPad) {
			tmp += padChr
			index++
		}
		return tmp.toByteArray(Charsets.UTF_8)
	}

	fun WxPKCS7Decode(decrypted: ByteArray): ByteArray {
		var pad: Int = decrypted[decrypted.size - 1].toInt()
		if (pad < 1 || pad > 32) {
			pad = 0
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.size - pad)
	}

	private fun chr(a: Int): Char = (a and 0xFF).toChar()
}