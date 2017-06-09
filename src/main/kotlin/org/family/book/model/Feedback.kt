package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Feedback(var title: String, var content: String, var contact: String) : BaseModel() {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	val id: Int = -1

	@ManyToOne
	lateinit var user: User
}