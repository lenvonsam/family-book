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
	fun findListCountByWordAndType(familyId: Int, userId: Int, word: String, type: String): Int

	@Query(value = "select * from account_book where current_family_id=?1 and current_user_id=?2 order by id desc  limit ?3,?4", nativeQuery = true)
	fun findListPg(familyId: Int, userId: Int, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "SELECT * FROM account_book where current_family_id=?1 and current_user_id=?2 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?3 order by id desc limit ?4,?5", nativeQuery = true)
	fun findListPgByWord(familyId: Int, userId: Int, word: String, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "select * from account_book where current_family_id=?1 and current_user_id=?2 and record_type=?3 order by id desc  limit ?4,?5", nativeQuery = true)
	fun findListByTypePg(familyId: Int, userId: Int, type: String, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "SELECT * FROM account_book where current_family_id=?1 and current_user_id=?2 and record_type=?3 and concat_ws(classify_name,ower_name,pay_type,price,record_time,record_type,remarks) like ?4 order by id desc limit ?5,?6", nativeQuery = true)
	fun findListByTypeAndWordPg(familyId: Int, userId: Int, type: String, word: String, currentPage: Int, pageSize: Int): List<AccountBook>

	@Query(value = "select * from account_book where date_format(record_time,'%Y-%m') = ?1 and current_family_id=?2 and  current_user_id=?3 order by record_time desc", nativeQuery = true)
	fun findMonthRecords(time: String, familyid: Int, userid: Int): List<AccountBook>

	@Query(value = "select sum(price) from account_book where date_format(record_time,'%Y-%m') = ?1 and current_family_id=?2 and  current_user_id=?3 and record_type=?4 order by record_time desc", nativeQuery = true)
	fun sumMonthRecords(time: String, familyid: Int, userid: Int, type: String): Float?

	@Query(value = "SELECT month(record_time) as month, sum(case when record_type ='支出' then price else 0 end) as 'pay', sum(case when record_type='收入' then price else 0 end) as 'receive' FROM account_book where year(record_time)=?2 and current_family_id=?1 group by month(record_time)", nativeQuery = true)
	fun getYearData(familyId: Int, yearDesc: String): List<Array<Any>>

	@Query(value = "SELECT sum(case when record_type ='支出' then price else 0 end) as 'pay', sum(case when record_type='收入' then price else 0 end) as 'receive' FROM account_book where year(record_time)=?2 and current_family_id=?1", nativeQuery = true)
	fun getYearDataTotal(familyId: Int, yearDesc: String): Any

	@Query(value = "select classify_name, sum(price) from account_book where record_type = ?1 and current_family_id=?2 and date_format(record_time,'%Y-%m-%d') between ?3 and ?4 group by classify_name", nativeQuery = true)
	fun getMonthClassifies(type: String, familyid: Int, beginDate: String, endDate: String):List<Array<Any>>
}