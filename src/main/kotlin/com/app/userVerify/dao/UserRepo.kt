package com.app.userVerify.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository 
public interface UserRepo:CrudRepository<User, Int> {
	fun findByName(name:String):List<User>
	
	
}  