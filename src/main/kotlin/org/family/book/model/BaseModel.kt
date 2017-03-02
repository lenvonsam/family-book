package org.family.book.model

import java.sql.Timestamp
import java.util.Date
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
open class BaseModel() {

	 lateinit var createAt: Timestamp
	 lateinit var updateAt: Timestamp

	@PrePersist
	fun setInitValue() {
		this.createAt = Timestamp(Date().getTime())
		this.updateAt = Timestamp(Date().getTime())
	}

	@PreUpdate
	fun setUpdateAt() {
		this.updateAt = Timestamp(Date().getTime())
	}
}