package org.family.book.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.family.book.repository.FeedbackRepository
import org.family.book.model.Feedback

@Service
class FeedbackService {
	@Autowired
	lateinit var feedbackRepo: FeedbackRepository

	fun save(fb: Feedback) = feedbackRepo.save(fb)
}