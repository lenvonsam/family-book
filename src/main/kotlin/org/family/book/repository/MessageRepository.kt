package org.family.book.repository

import org.family.book.model.Message
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, Int> {
	@Query(value = "select * from message where type=?1 and sender=?2 and receiver=?3 and family_id=?4 order by unread desc, update_at desc", nativeQuery = true)
	fun isExist(type: Int, sender: Int, receiver: Int, familyid: Int): Message?

	@Query(value = "select * from v_message where receiver=?1 order by unread desc, update_at desc", nativeQuery = true)
	fun findMsgList(receiver: Int): List<Array<Any>>

	@Query(value = "select count(*) from v_message where receiver=?1 and unread=1", nativeQuery = true)
	fun unreadCount(receiver: Int): Int
}