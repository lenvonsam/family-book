package org.family.book.repository

import org.family.book.model.Feedback
import org.springframework.data.repository.CrudRepository

interface FeedbackRepository : CrudRepository<Feedback, Int>{
}