package com.app.userVerify.service


import java.util.concurrent.atomic.AtomicInteger
import com.app.userVerify.dao.UserRepo
import com.app.userVerify.dao.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.app.userVerify.Passwords
import com.app.userVerify.UserDetails
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Configurable

@Service

public class UserVerifyService(var userRepo:UserRepo) {

	val counter = AtomicInteger()
    val MIN_PASSWORD_LEN = 4
	
	fun addUser(user: UserDetails):ServiceRetVals {
		var newUser = userRepo.findByName(user.name)
		if (newUser.isEmpty() == false)
			return ServiceRetVals.USER_EXIST
		if (checkPassword(user.password)) {
			userRepo.save(User(counter.incrementAndGet(), user.name, user.password))
			return ServiceRetVals.OK
		} else {
			return ServiceRetVals.INVALID_PASSWORD
		}

	}
	
    fun verifyUser(name:String, password: String):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == password)
			return ServiceRetVals.OK
		return ServiceRetVals.WRONG_PASSWORD
	}
	
	fun updatePassword(name:String, passwords:Passwords):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == passwords.oldPassword) {
			if (checkPassword(passwords.newPassword)) {
				var newUser = User(user[0].id, user[0].name, passwords.newPassword)
				userRepo.save(newUser)
				return ServiceRetVals.OK
			} else {
				return ServiceRetVals.INVALID_PASSWORD
			}
			
		}
		return ServiceRetVals.WRONG_PASSWORD
	}
	
	
	fun delUser(name:String ,password: String):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == password) {
			userRepo.deleteById(user[0].id)
			return ServiceRetVals.OK
		}
		return ServiceRetVals.WRONG_PASSWORD
	}
	
	fun checkPassword(password: String):Boolean {
		if (password.length < MIN_PASSWORD_LEN)
			return false
		else
			return true
	}
}