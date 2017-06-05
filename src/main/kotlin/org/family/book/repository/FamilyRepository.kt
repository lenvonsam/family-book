package org.family.book.repository

import org.family.book.model.Family
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query

interface FamilyRepository : CrudRepository<Family, Int> {

//	@Query(nativeQuery=true,value="select * from family where creator")
//	fun listByCreator(creator: String);


	@Query(nativeQuery = true, value = "select * from family where is_show = true and name = ?1")
	fun isExistName(name: String): Family?

	fun findByName(name: String): Family?
}