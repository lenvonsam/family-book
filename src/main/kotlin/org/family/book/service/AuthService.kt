package org.family.book.service

import org.family.book.repository.AdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.ArrayList
import org.family.book.repository.UserRepository

@Service
class AuthService : UserDetailsService {

	@Autowired
	lateinit private var userRepo: UserRepository

	@Throws(Exception::class)
	override fun loadUserByUsername(username: String?): User {
		var user = userRepo.findByPhoneAndEnable(username!!, 1)
		return User(user?.phone, user?.password, ArrayList<GrantedAuthority>())
	}


}