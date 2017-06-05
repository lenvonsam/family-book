package org.family.book.service

import org.family.book.model.Family
import org.family.book.model.FamilyUserMap
import org.family.book.repository.FamilyRepository
import org.family.book.repository.FamilyUserMapRepository
import org.family.book.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap

@Service
class FamilyService {
	@Autowired
	lateinit var familyRepo: FamilyRepository

	@Autowired
	lateinit var userRepo: UserRepository

	@Autowired
	lateinit var familyUserMapRepo: FamilyUserMapRepository

	//创建家庭
	fun createFamily(name: String, creatorId: Int): Map<String, Any> {
		var result = HashMap<String, Any>()
		try {
			var currentUser = userRepo.findOne(creatorId)
			var family = Family(name, currentUser.phone)
			familyRepo.save(family)
			println("family id:>>>${family.id}")
			if (family.id!! > 0) {
				// 建立家庭和用户关联关系
				var familyUserMap = FamilyUserMap()
				familyUserMap.user = currentUser
				familyUserMap.family = family
				familyUserMapRepo.save(familyUserMap)
			}
			result.put("returnCode", 0)
			result.put("family", family)
			result.put("errMsg", "数据库保存成功")
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	//家庭列表
	fun familyByCreator(creator: String): Map<String, Any> {
		var result = HashMap<String, Any>()
		result.put("returnCode", 0)
		result.put("list", familyUserMapRepo.listByUser(creator))
		return result
	}

	fun findByOne(id: Int): Family = familyRepo.findOne(id)

	// 更新选择家庭
	@Transactional
	@Throws(Exception::class)
	fun updateFamilyChoosed(userid: Int, familyid: Int) {
		familyUserMapRepo.resetFamilyChoosed(userid)
		familyUserMapRepo.updateFamilyChoose(userid, familyid)
	}
}