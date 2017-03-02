package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Family(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int, var name: String, var creator: String) : BaseModel() {
	
}