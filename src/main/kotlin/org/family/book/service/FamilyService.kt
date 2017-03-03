package org.family.book.service

import org.family.book.repository.FamilyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.HashMap
import org.family.book.model.Family
import org.family.book.repository.UserRepository

@Service
class FamilyService {
	@Autowired
	lateinit var familyRepo: FamilyRepository

	@Autowired
	lateinit var userRepo: UserRepository

	//创建家庭
	fun createFamily(name: String, creatorId: Int): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var currentUser = userRepo.findOne(creatorId)
			var family = Family(name, currentUser.phone)
			familyRepo.save(family)
			result.put("returnCode", if (family.id > 0) 0 else -1)
			result.put("errMsg", if (family.id > 0) "" else "数据库保存失败")
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}
}