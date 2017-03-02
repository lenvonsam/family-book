package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
class Classify(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int, var name: String) : BaseModel() {

	@JsonIgnore
	@ManyToOne
	lateinit var family: Family
}