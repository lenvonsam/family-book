package org.family.book.repository

import org.family.book.model.Family
import org.family.book.model.FamilyUserMap
import org.family.book.model.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface FamilyUserMapRepository : CrudRepository<FamilyUserMap, Int> {

	@Query(value = "select * from v_family_user where phone=?1 and is_show = true order by default_choosed desc", nativeQuery = true)
	fun listByUser(phone: String): List<Array<Any>>

	@Query(value = "select * from v_family_user where user_id=?1 and is_show = true order by default_choosed desc", nativeQuery = true)
	fun listByUserId(userid: Int): List<Array<Any>>

//	@Query(value = "select * from v_family_user where user_id=?1 and family_id=?2 and is_show = true order by default_choosed desc", nativeQuery = true)
//	fun listByUserAndFamilyId(userid: Int, familyid: Int): List<Array<Any>>

	fun findByUserAndFamily(user: User, family: Family): FamilyUserMap?

	@Modifying
	@Query(value = "update family_user_map set default_choosed = 0 where user_id=?1", nativeQuery = true)
	fun resetFamilyChoosed(userId: Int)

	@Modifying
	@Query(value = "update family_user_map set default_choosed = 1 where user_id=?1 and family_id=?2", nativeQuery = true)
	fun updateFamilyChoose(userId: Int, familyId: Int)

	@Modifying
	@Query(value = "update family_user_map set default_choosed = 0 where family_id = ?1", nativeQuery = true)
	fun updateChooseByFamilyId(fid: Int)
}