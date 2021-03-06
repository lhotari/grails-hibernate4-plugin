package org.codehaus.groovy.grails.orm.hibernate

/**
 * Test for GRAILS-3178.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
class CriteriaListDistinctTests extends AbstractGrailsHibernateTests {

	protected void onSetUp() {
		gcl.parseClass '''
import grails.persistence.*

@Entity
class PlantCategory {
	String name

	static hasMany = [plants:Plant]
}

@Entity
class Plant {
	boolean goesInPatch
	String name
}
'''
	}

	void testListDistinct() {
		def PlantCategory = ga.getDomainClass("PlantCategory").clazz
		def Plant = ga.getDomainClass("Plant").clazz

		assertNotNull PlantCategory.newInstance(name:"leafy")
		                           .addToPlants(goesInPatch:true, name:"lettuce")
		                           .addToPlants(goesInPatch:true, name:"cabbage")
		                           .save(flush:true)

		assertNotNull PlantCategory.newInstance(name:"orange")
		                           .addToPlants(goesInPatch:true, name:"carrots")
		                           .addToPlants(goesInPatch:true, name:"pumpkin")
		                           .save(flush:true)

		assertNotNull PlantCategory.newInstance(name:"grapes")
		                           .addToPlants(goesInPatch:false, name:"red")
		                           .addToPlants(goesInPatch:false, name:"white")
		                           .save(flush:true)

		session.clear()

		def categories = PlantCategory.createCriteria().listDistinct {
			plants {
				eq('goesInPatch', true)
			}
			order('name', 'asc')
		}

		assertNotNull categories
		assertEquals 2, categories.size()
		assertEquals "leafy", categories[0].name
		assertEquals "orange", categories[1].name
	}

	void testListDistinct2() {
		def PlantCategory = ga.getDomainClass("PlantCategory").clazz
		def Plant = ga.getDomainClass("Plant").clazz

		assertNotNull PlantCategory.newInstance(name:"leafy")
		                           .addToPlants(goesInPatch:true, name:"lettuce")
		                           .addToPlants(goesInPatch:true, name:"cabbage")
		                           .save(flush:true)

		assertNotNull PlantCategory.newInstance(name:"orange")
		                           .addToPlants(goesInPatch:true, name:"carrots")
		                           .addToPlants(goesInPatch:true, name:"pumpkin")
		                           .save(flush:true)

		assertNotNull PlantCategory.newInstance(name:"grapes")
		                           .addToPlants(goesInPatch:false, name:"red")
		                           .addToPlants(goesInPatch:true, name:"white")
		                           .save(flush:true)

		session.clear()

		def categories = PlantCategory.createCriteria().listDistinct {
			plants {
				eq('goesInPatch', true)
			}
			order('name', 'asc')
		}

		assertNotNull categories
		assertEquals 3, categories.size()
		assertEquals "grapes", categories[0].name
		assertEquals "leafy", categories[1].name
		assertEquals "orange", categories[2].name
	}
}
