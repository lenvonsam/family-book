package org.family.book.repository

import org.family.book.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {

	//用于测试
	fun findByPhoneAndPassword(phone: String, password: String): User?

	fun findByPhoneAndEnable(phone: String, enable: Int): User?
}