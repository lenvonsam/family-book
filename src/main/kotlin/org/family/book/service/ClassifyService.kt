package org.family.book.service

import org.family.book.model.Classify
import org.family.book.model.Family
import org.family.book.model.User
import org.family.book.repository.ClassifyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.HashMap

@Service
class ClassifyService {

	@Autowired
	lateinit var classifyRepo: ClassifyRepository

	fun listByFamilyUser(user: User, family: Family, type: String): List<Classify> = classifyRepo.findByUserAndFamilyAndType(user, family, type)

	fun save(obj: Classify, currentUser: User): Boolean {
		obj.user = currentUser
		obj.family = currentUser.choosedFamily
		classifyRepo.save(obj)
		if (obj.id > 0) return true else return false
	}

	fun update(id: Int, name: String): Boolean {
		try {
			var obj = classifyRepo.findOne(id)
			obj.name = name
			classifyRepo.save(obj)
			return true
		} catch(e: Exception) {
			return false
		}
	}

	fun getClassifyListPg(userId: Int, familyId: Int, currentPage: Int, type: String = "", word: String = ""): Map<String, Any> {
		var result = HashMap<String, Any>()
		var pageSize: Int = 10
		var totalCount: Int
		var list: List<Classify>?
		if (type != "" && word != "") {
			totalCount = classifyRepo.findListCountByWordAndType(familyId, userId, word, type)
			list = classifyRepo.findListByTypeAndWordPg(familyId, userId, type, word, currentPage, pageSize)
		} else if (word != "") {
			totalCount = classifyRepo.findListCountByWord(familyId, userId, word)
			list = classifyRepo.findListPgByWord(familyId, userId, word, currentPage, pageSize)
		} else if (type != "") {
			totalCount = classifyRepo.findListCountByType(familyId, userId, type)
			list = classifyRepo.findListByTypePg(familyId, userId, type, currentPage, pageSize)
		} else {
			totalCount = classifyRepo.findListCount(familyId, userId)
			list = classifyRepo.findListPg(familyId, userId, currentPage, pageSize)
		}
		val totalPage = if (totalCount.mod(pageSize) > 0) totalCount / pageSize + 1 else totalCount / pageSize
		result.put("returnCode", 0)
		result.put("totalPage", totalPage)
		result.put("cList", list)
		return result
	}
}