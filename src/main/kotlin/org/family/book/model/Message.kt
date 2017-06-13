package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class Message(var type: Int, var sender: Int, var receiver: Int, var status: String = "处理中", var unread: Int = 1) : BaseModel() {
	// type 0系统消息 1 申请加入家庭 2 回复申请
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	val id: Int? = null

	var familyId: Int = -1
}