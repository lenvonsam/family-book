package org.family.book.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import java.sql.Timestamp
import com.fasterxml.jackson.annotation.JsonIgnore


@Entity
class AccountBook(var price: Double, var content: String, var classifyName: String, var recordType: String, var owerName: String, var recordTime: Timestamp, var payType: String = "现金", var remarks: String = "") : BaseModel() {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	val id: Int = -1

	@JsonIgnore
	@ManyToOne
	lateinit var currentUser: User

	@JsonIgnore
	@ManyToOne
	lateinit var currentFamily: Family

}