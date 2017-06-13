package org.family.book.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Classify( var name: String, var type: String) : BaseModel() {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	 val id: Int? = null

	@JsonIgnore
	@ManyToOne
	lateinit var family: Family

	@JsonIgnore
	@ManyToOne
	lateinit var user: User
}