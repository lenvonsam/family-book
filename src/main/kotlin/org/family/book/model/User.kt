package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
class User(var nickname: String, var enable: Int = 1, @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int? = null) : BaseModel() {

	var gender: String = "男"
	var phone: String = ""
	var password: String = ""
	var openId: String = ""
	var avatarUrl: String = ""

	@JsonIgnore
	@ManyToOne
	var choosedFamily: Family? = null

}