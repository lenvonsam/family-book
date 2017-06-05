package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
class User(var phone: String, var password: String, var nickname: String, var enable: Int = 1) : BaseModel() {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Int = -1

	@JsonIgnore
	@ManyToOne
	lateinit var choosedFamily: Family;

}