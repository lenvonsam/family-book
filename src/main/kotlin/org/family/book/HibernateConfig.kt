package org.family.book

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean
import javax.persistence.EntityManagerFactory

@Configuration
class HibernateConfig {

	@Bean
	fun sessionFactory(emf: EntityManagerFactory): HibernateJpaSessionFactoryBean {
		val factory = HibernateJpaSessionFactoryBean();
		factory.setEntityManagerFactory(emf);
		return factory
	}
}