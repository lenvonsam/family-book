package org.family.book.repository

import org.family.book.model.FamilyUserMap
import org.springframework.data.repository.CrudRepository

interface FamilyUserMapRepository : CrudRepository<FamilyUserMap, Int> {

}