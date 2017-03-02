package org.family.book.repository

import org.family.book.model.Family
import org.springframework.data.repository.CrudRepository

interface FamilyRepository : CrudRepository<Family, Int> {
}