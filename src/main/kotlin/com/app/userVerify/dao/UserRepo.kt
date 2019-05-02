package com.app.userVerify.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * UserRepo- Database definition (based on H2 database)
 * Actual DAO layer of the API
 * @author      Oren Gur Arie
 */
@Repository 
public interface UserRepo:CrudRepository<User, Int> {

	/**
	 * findByName - find all users in the data base with a given name
	 * @Param name user name to be searched
	 */
	fun findByName(name:String):List<User>
	
	
}  