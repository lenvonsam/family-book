package org.family.book.repository

import org.family.book.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository:CrudRepository<User,Int> {

}