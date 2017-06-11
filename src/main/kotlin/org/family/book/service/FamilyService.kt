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
import org.springframework.scheduling.annotation.EnableAsync

@Service
@EnableAsync
class FamilyService {
	@Autowired
	lateinit private var familyRepo: FamilyRepository

	@Autowired
	lateinit private var userRepo: UserRepository

	@Autowired
	lateinit private var classifyRepo: ClassifyRepository

	@Autowired
	lateinit private var familyUserMapRepo: FamilyUserMapRepository

	private var result = HashMap<String, Any>()

	//创建家庭
	@Transactional
	fun createFamily(name: String, creatorId: Int): Map<String, Any> {
		result = HashMap<String, Any>()
		try {
			var currentUser = userRepo.findOne(creatorId)
			var f = familyRepo.findByName(name)
			if (f == null) {
				var family = Family(name)
				family.creator = currentUser
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

	//加入家庭
	@Transactional
	fun joinFamily(familyId: Int, user: Int) {
		var u = userRepo.findOne(user)
		var f = familyRepo.findOne(familyId)
		var m = familyUserMapRepo.findByUserAndFamily(u, f)
		if (m == null) {
			var mapper = FamilyUserMap()
			mapper.user = u
			mapper.family = f
			familyUserMapRepo.save(mapper)
		}
	}

	// 更新选择家庭
	@Transactional
	@Throws(Exception::class)
	fun updateFamilyChoosed(userid: Int, familyid: Int) {
		familyUserMapRepo.resetFamilyChoosed(userid)
		familyUserMapRepo.updateFamilyChoose(userid, familyid)
	}

	// 更改家庭名称
	@Transactional
	fun updateFamily(familyid: Int, familyName: String): HashMap<String, Any> {
		var result = HashMap<String, Any>()
		val f = familyRepo.findOne(familyid)
		if (f.name != familyName) {
			var ft = familyRepo.findByName(familyName)
			if (ft == null) {
				f.name = familyName
				familyRepo.save(f)
				result.put("returnCode", 0)
			} else {
				result.put("returnCode", -1)
				result.put("errMsg", "账本名称已存在")
			}
		} else {
			result.put("returnCode", 0)
		}
		return result
	}

	@Transactional
	@Throws(Exception::class)
	fun delFmaily(id: Int) {
		var fmp = familyUserMapRepo.findOne(id)
		var f = familyRepo.findOne(fmp.family.id)
		f.isShow = false
		familyRepo.save(f)
		familyUserMapRepo.updateChooseByFamilyId(f.id!!)
	}

	fun searchFamily(userid: Int, searchVal: String) = familyRepo.searchFamily(userid, "%$searchVal%")

	fun findVfamilyById(id: Int, userid: Int): Any? {
		var f = familyRepo.findOne(id)
		var u = userRepo.findOne(userid)
		if (familyUserMapRepo.findByUserAndFamily(u, f) == null) {
			return familyRepo.findVfamilyById(id)
		} else {
			return null
		}
	}
}