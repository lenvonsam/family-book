package org.family.book.service

import org.family.book.model.Classify
import org.family.book.model.Family
import org.family.book.model.FamilyUserMap
import org.family.book.model.User
import org.family.book.repository.FamilyRepository
import org.family.book.repository.FamilyUserMapRepository
import org.family.book.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap
import org.family.book.repository.ClassifyRepository

@Service
class FamilyService {
	@Autowired
	lateinit var familyRepo: FamilyRepository

	@Autowired
	lateinit var userRepo: UserRepository

	@Autowired
	lateinit var classifyRepo: ClassifyRepository

	@Autowired
	lateinit var familyUserMapRepo: FamilyUserMapRepository

	private var result = HashMap<String, Any>()

	//创建家庭
	@Transactional
	fun createFamily(name: String, creatorId: Int): Map<String, Any> {
		result = HashMap<String, Any>()
		try {
			var currentUser = userRepo.findOne(creatorId)
			var f = familyRepo.isExistName(name)
			if (f == null) {
				var family = Family(name)
				familyRepo.save(family)
				println("family id:>>>${family.id}")
				if (family.id!! > 0) {
					// 建立家庭和用户关联关系
					var familyUserMap = FamilyUserMap()
					familyUserMap.user = currentUser
					familyUserMap.family = family
					familyUserMapRepo.save(familyUserMap)
					// 判断是否是第一次创建，如果第一次创建默认选中
					if (familyUserMap.id > 0) {
						if (familyUserMapRepo.listByUserId(currentUser.id).size == 1) {
							familyUserMapRepo.updateFamilyChoose(currentUser.id, family.id!!)
							currentUser.choosedFamily = family
							userRepo.save(currentUser)
						}
						// 异步创建账本分类
						autoCreateFamilyClassify(family, currentUser)
					}
				}
				result.put("returnCode", 0)
				result.put("family", family)
				result.put("familyUser", currentUser)
				result.put("errMsg", "数据库保存成功")
			} else {
				result.put("returnCode", -1)
				result.put("errMsg", "帐本名称不能重复")
			}
		} catch(e: Exception) {
			result.put("returnCode", -1)
			result.put("errMsg", e.message!!)
		}
		return result
	}

	@Async
	@Transactional
	fun autoCreateFamilyClassify(f: Family, u: User) {
		val payBasics = arrayOf("吃喝", "交通", "服饰", "日用品", "红包", "买菜", "孩子", "网购", "话费", "娱乐", "医疗", "化妆护肤", "其他")
		val receiveBasics = arrayOf("工资", "红包", "兼职", "投资", "奖金")
		payBasics.map { s ->
			var c = Classify(s, "支出")
			c.family = f
			c.user = u
			classifyRepo.save(c)
		}
		receiveBasics.map { r ->
			var c = Classify(r, "收入")
			c.family = f
			c.user = u
			classifyRepo.save(c)
		}
	}

	//家庭列表
	fun familyByCreator(creator: String): Map<String, Any> {
		result = HashMap<String, Any>()
		result.put("returnCode", 0)
		result.put("list", familyUserMapRepo.listByUser(creator))
		return result
	}

	// 家庭列表 openid
	fun familyByOpenId(userid: Int): Map<String, Any> {
		result = HashMap<String, Any>()
		result.put("returnCode", 0)
		result.put("list", familyUserMapRepo.listByUserId(userid))
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