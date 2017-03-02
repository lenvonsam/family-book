package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
class FamilyUserMap : BaseModel() {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Int = 0

	@JsonIgnore
	@ManyToOne
	lateinit var user: User

	@JsonIgnore
	@ManyToOne
	lateinit var family: Family

}