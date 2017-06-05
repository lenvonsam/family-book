package org.family.book.repository

import org.family.book.model.Classify
import org.family.book.model.Family
import org.family.book.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ClassifyRepository : CrudRepository<Classify, Int> {
	fun findByUserAndFamilyAndType(user: User, family: Family, type: String): List<Classify>

	@Query(value = "select count(*) from classify where family_id=?1 and user_id=?2", nativeQuery = true)
	fun findListCount(familyId: Int, userId: Int): Int

	@Query(value = "select count(*) from classify where family_id=?1 and user_id=?2 and type=?3", nativeQuery = true)
	fun findListCountByType(familyId: Int, userId: Int, type: String): Int

	@Query(value = "SELECT count(*) FROM classify where family_id=?1 and user_id=?2 and concat_ws(name,update_at,type) like ?3", nativeQuery = true)
	fun findListCountByWord(familyId: Int, userId: Int, word: String): Int

	@Query(value = "SELECT count(*) FROM classify where family_id=?1 and user_id=?2 and type=?4 and concat_ws(name,update_at,type) like ?3", nativeQuery = true)
	fun findListCountByWordAndType(familyId: Int, userId: Int, word: String, type: String): Int

	@Query(value = "select * from classify where family_id=?1 and user_id=?2 order by id desc  limit ?3,?4", nativeQuery = true)
	fun findListPg(familyId: Int, userId: Int, currentPage: Int, pageSize: Int): List<Classify>

	@Query(value = "SELECT * FROM classify where family_id=?1 and user_id=?2 and concat_ws(name,update_at,type) like ?3 order by id desc limit ?4,?5", nativeQuery = true)
	fun findListPgByWord(familyId: Int, userId: Int, word: String, currentPage: Int, pageSize: Int): List<Classify>

	@Query(value = "select * from classify where family_id=?1 and user_id=?2 and type=?3 order by id desc  limit ?4,?5", nativeQuery = true)
	fun findListByTypePg(familyId: Int, userId: Int, type: String, currentPage: Int, pageSize: Int): List<Classify>

	@Query(value = "SELECT * FROM classify where family_id=?1 and user_id=?2 and type=?3 and concat_ws(name,update_at,type) like ?4 order by id desc limit ?5,?6", nativeQuery = true)
	fun findListByTypeAndWordPg(familyId: Int, userId: Int, type: String, word: String, currentPage: Int, pageSize: Int): List<Classify>
}