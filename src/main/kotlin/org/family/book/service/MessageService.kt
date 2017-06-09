package org.family.book.service

import org.family.book.model.Message
import org.family.book.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync

@Service
@EnableAsync
class MessageService {
	@Autowired
	lateinit private var msgRepo: MessageRepository

	fun save(msg: Message) = msgRepo.save(msg)

	fun isExist(type: Int, sender: Int, receiver: Int, familyid: Int) = msgRepo.isExist(type, sender, receiver, familyid)

	fun findMsgList(receiver: Int) = msgRepo.findMsgList(receiver)

	fun unreadCount(receiver: Int) = msgRepo.unreadCount(receiver)

	fun findOne(id: Int) = msgRepo.findOne(id)

	@Async
	@Throws(Exception::class)
	fun generatorApplyReceiveMsg(msg: Message) {
		print("msg.status>>>${msg.status}")
		var m = Message(2, msg.receiver, msg.sender, msg.status)
		m.familyId = msg.familyId
		save(m)
	}
}