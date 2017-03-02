package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Admin(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int, var name: String, var password: String, var enable: Int = 1) : BaseModel() {
}