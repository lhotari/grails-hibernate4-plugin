grails.project.work.dir = 'target'

forkConfig = false
grails.project.fork = [
	test:    forkConfig, // configure settings for the test-app JVM
	run:     forkConfig, // configure settings for the run-app JVM
	war:     forkConfig, // configure settings for the run-war JVM
	console: forkConfig, // configure settings for the Swing console JVM
	compile: forkConfig  // configure settings for compilation
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {

	inherits "global"
	log "warn"

	repositories {
		grailsCentral()
		mavenRepo "http://repo.grails.org/grails/core"
	}

	dependencies {
        String datastoreVersion = '2.0.0.BUILD-SNAPSHOT'

		compile "org.grails:grails-datastore-gorm-hibernate4:$datastoreVersion"

		runtime 'cglib:cglib:2.2.2'

		runtime 'org.jboss.logging:jboss-logging:3.1.0.GA', {
			excludes 'jboss-logmanager', 'log4j', 'slf4j-api'
		}

		test 'org.spockframework:spock-grails-support:0.7-groovy-2.0', {
			export = false
		}
	}

	plugins {
		build ':release:3.0.0', ':rest-client-builder:1.0.3', {
			export = false
		}

		test ':spock:0.7', {
			exclude 'spock-grails-support'
			export = false
		}

		test ':scaffolding:1.0.0', {
			export = false
		}
	}
}
