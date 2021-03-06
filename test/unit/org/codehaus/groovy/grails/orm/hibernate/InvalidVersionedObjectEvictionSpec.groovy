package org.codehaus.groovy.grails.orm.hibernate

import grails.persistence.Entity

import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException

import spock.lang.FailsWith
import spock.lang.Issue

class InvalidVersionedObjectEvictionSpec extends GormSpec {

	@Issue('GRAILS-8937')
	@FailsWith(HibernateOptimisticLockingFailureException)
	void "test invalid versioned object in session"() {
		when:"An invalid version object is evicted and then a corrected version saved"
			def foo = new Foo(name:'valid')
			foo.save(failOnError:true, flush:true)

			def bar = new Bar(name:'valid')
			bar.save(failOnError:true, flush:true)

			assert foo.version == 0

			bar.name = 'valid2'
			foo.name = 'invalid'
			bar.save(failOnError:true, flush:true)

			foo.version = 0
			foo.name = 'valid2'

		then:"The last saved is successful"
			foo.merge(failOnError:true, flush:true)
	}

	@Override
	List getDomainClasses() {
		[Bar, Foo]
	}
}

@Entity
class Bar {
	String name
}

@Entity
class Foo {

	String name

	static constraints = {
		name(validator:{ it != 'invalid' })
	}
}
