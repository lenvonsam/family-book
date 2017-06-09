package org.family.book.repository

import org.family.book.model.Family
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query

interface FamilyRepository : CrudRepository<Family, Int> {

//	@Query(nativeQuery=true,value="select * from family where creator")
//	fun listByCreator(creator: String);

	fun findByName(name: String): Family?

	@Query(nativeQuery = true, value = "SELECT * FROM v_family where creator_id <>?1 and concat_ws(' ',name,nickname) like ?2")
	fun searchFamily(userid: Int, searchVal: String): List<Array<Any>>

	@Query(nativeQuery = true, value = "select * from v_family where id = ?1")
	fun findVfamilyById(id: Int): Any
}