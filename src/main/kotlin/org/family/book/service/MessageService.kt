package org.family.book.service

import org.family.book.model.Message
import org.family.book.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService {
	@Autowired
	lateinit private var msgRepo: MessageRepository

	fun save(msg: Message) = msgRepo.save(msg)

	fun isExist(type: Int, sender: Int, receiver: Int, familyid: Int) = msgRepo.isExist(type, sender, receiver, familyid)

	fun findMsgList(receiver: Int) = msgRepo.findMsgList(receiver)

	fun unreadCount(receiver: Int) = msgRepo.unreadCount(receiver)
}