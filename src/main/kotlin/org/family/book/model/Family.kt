package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Family(var name: String, var creator: String,@Id  @GeneratedValue(strategy = GenerationType.AUTO) val id: Int? = null) : BaseModel() {

}