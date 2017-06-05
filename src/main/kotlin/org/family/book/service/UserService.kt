package org.family.book.service

import org.family.book.model.User
import org.family.book.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

	@Autowired
	lateinit var userRepo: UserRepository

	fun loginByPhoneAndPassword(phone: String, password: String): User? {
		return userRepo.findByPhoneAndPassword(phone, password)
	}

	fun findByOne(id: Int): User = userRepo.findOne(id)

	fun save(u: User) {
		userRepo.save(u)
	}

	fun findByOpenId(openid: String): User? = userRepo.findByOpenId(openid)
}