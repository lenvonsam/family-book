package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import java.sql.Timestamp

@Entity
class User(var phone: String, var password: String, var nickname: String, @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int) : BaseModel() {
	
}