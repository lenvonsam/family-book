package org.family.book.service

import org.family.book.repository.AdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class AuthService : UserDetailsService {

	@Autowired
	lateinit var adminRepo: AdminRepository

	@Throws(Exception::class)
	override fun loadUserByUsername(username: String?): User {
		var admin = adminRepo.findByNameAndEnable(username!!, 1)
		return User(admin.name, admin.password, ArrayList<GrantedAuthority>())
	}


}