package org.family.book.repository

import org.family.book.model.Admin
import org.springframework.data.repository.CrudRepository

interface AdminRepository : CrudRepository<Admin, Int> {
	
	fun findByNameAndEnable(name: String, enable: Int): Admin
}