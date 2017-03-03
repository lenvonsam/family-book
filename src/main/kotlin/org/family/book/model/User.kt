package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(var phone: String, var password: String, var nickname: String) : BaseModel() {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Int = -1

}