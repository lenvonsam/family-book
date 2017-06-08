package org.family.book.service

import org.family.book.model.AccountBook
import org.family.book.repository.AccountBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.HashMap

@Service
class AccountBookService {

	@Autowired
	lateinit  private var abRepo: AccountBookRepository

	fun save(ab: AccountBook): Boolean {
		abRepo.save(ab)
		println("new ab.id>>>${ab.id}")
		return if (ab.id > 0) true else false
	}

	fun findOne(id: Int): AccountBook {
		return abRepo.findOne(id)
	}

	fun delObj(ab: AccountBook) {
		abRepo.delete(ab)
	}

	fun getAccountBookListPg(userId: Int, familyId: Int, currentPage: Int, type: String = "", word: String = ""): Map<String, Any> {
		var result = HashMap<String, Any>()
		var pageSize: Int = 10
		var totalCount: Int
		var list: List<AccountBook>?
		if (type != "" && word != "") {
			totalCount = abRepo.findListCountByWordAndType(familyId, userId, word, type)
			list = abRepo.findListByTypeAndWordPg(familyId, userId, type, word, currentPage, pageSize)
		} else if (word != "") {
			totalCount = abRepo.findListCountByWord(familyId, userId, word)
			list = abRepo.findListPgByWord(familyId, userId, word, currentPage, pageSize)
		} else if (type != "") {
			totalCount = abRepo.findListCountByType(familyId, userId, type)
			list = abRepo.findListByTypePg(familyId, userId, type, currentPage, pageSize)
		} else {
			totalCount = abRepo.findListCount(familyId, userId)
			list = abRepo.findListPg(familyId, userId, currentPage, pageSize)
		}
		val totalPage = if (totalCount.mod(pageSize) > 0) totalCount / pageSize + 1 else totalCount / pageSize
		result.put("returnCode", 0)
		result.put("totalPage", totalPage)
		result.put("abList", list)
		return result
	}

	fun findMonthRecords(time: String, familyid: Int, userid: Int) = abRepo.findMonthRecords(time, familyid, userid)

	fun sumMonthRecords(time: String, familyid: Int, userid: Int, type: String) = abRepo.sumMonthRecords(time, familyid, userid, type)

	fun getYearData(familyId: Int, yearDesc: String) = abRepo.getYearData(familyId, yearDesc)

	fun getYearDataTotal(familyId: Int, yearDesc: String) = abRepo.getYearDataTotal(familyId, yearDesc)

	fun getMonthClassifies(type: String, familyid: Int, beginDate: String, endDate: String) = abRepo.getMonthClassifies(type, familyid, beginDate, endDate)
}
