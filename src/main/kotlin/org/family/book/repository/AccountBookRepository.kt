package org.family.book.repository

import org.springframework.data.repository.CrudRepository
import org.family.book.model.AccountBook
import org.springframework.data.jpa.repository.Query

interface AccountBookRepository : CrudRepository<AccountBook, Int> {

	@Query(value = "select count(*) from account_book where current_family_id=?1 and current_user_id=?2", nativeQuery = true)
	fun findListCount(familyId: Int, userId: Int): Int

	@Query(value = "select count(*) from account_book where current_family_id=?1 and current_user_id=?2 and record_type=?3", nativeQuery = true)
	fun findListCountByType(familyId: Int, userId: Int, type: String): Int

	@Query(value = "SELECT count(*) FROM account_book where current_family_id=?1 and current_user_id=?2 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?3", nativeQuery = true)
	fun findListCountByWord(familyId: Int, userId: Int, word: String): Int
	
	@Query(value = "SELECT count(*) FROM account_book where current_family_id=?1 and current_user_id=?2 and record_type=?4 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?3", nativeQuery = true)
	fun findListCountByWordAndType(familyId: Int, userId: Int, word: String,type:String):Int

	@Query(value = "select * from account_book where current_family_id=?1 and current_user_id=?2 order by id desc  limit ?3,?4", nativeQuery = true)
	fun findListPg(familyId: Int, userId: Int, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "SELECT * FROM account_book where current_family_id=?1 and current_user_id=?2 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?3 order by id desc limit ?4,?5", nativeQuery = true)
	fun findListPgByWord(familyId: Int, userId: Int, word: String, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "select * from account_book where current_family_id=?1 and current_user_id=?2 and record_type=?3 order by id desc  limit ?4,?5", nativeQuery = true)
	fun findListByTypePg(familyId: Int, userId: Int, type: String, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "SELECT * FROM account_book where current_family_id=?1 and current_user_id=?2 and record_type=?3 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?4 order by id desc limit ?5,?6", nativeQuery = true)
	fun findListByTypeAndWordPg(familyId: Int, userId: Int, type: String, word: String, currentPage: Int, pageSize: Int): List<AccountBook>
}