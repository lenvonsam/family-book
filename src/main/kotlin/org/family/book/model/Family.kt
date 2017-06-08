package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
class Family(var name: String, @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int? = null, var isShow: Boolean = true) : BaseModel() {
	@JsonIgnore
	@ManyToOne
	lateinit var creator: User
}