package com.app.userVerify.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ImportResource
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import com.app.userVerify.Passwords
import com.app.userVerify.UserDetails
import com.app.userVerify.LoginInput
import com.app.userVerify.service.UserVerifyService
import com.app.userVerify.service.ServiceRetVals
import com.app.userVerify.dao.UserRepo

@RestController
/**
 * UserVerifyController - Controller layer for the UserVerify API
 * @Param userRepo repository object
 * @author      Oren Gur Arie
 */
class UserVerifyController(@Autowired var userRepo:UserRepo) {
	
	//Service layer object
	var service:UserVerifyService = UserVerifyService(userRepo)
	

		/**
	 * addUser - adds a new user to the repository
	 * @param user - received details of the new user
	 * @return HTTP response code according to status

	 */
	@PostMapping("/User")
	@ResponseBody
	fun addUser(@RequestBody user: UserDetails):ResponseEntity<Void> {
		var retVal =service.addUser(user) 
		if (retVal == ServiceRetVals.USER_EXIST) {
			//Returns conflict when user already exist
			return ResponseEntity<Void>(HttpStatus.CONFLICT)
		} else if (retVal == ServiceRetVals.INVALID_PASSWORD) {
			//Returns NOT ACCEPTABLE for a weak password
			return ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE)
		} else {
			//Return OK
			return ResponseEntity<Void>(HttpStatus.OK)
		}
		
	}
	
	/**
	 * loginUser - user login to the system
	 * @param name - logged in user name
	 * @param loginInput - data received with the request (for now only password)
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	@PutMapping("/User/login/{name}")
    fun loginUser(@PathVariable name:String, @RequestBody loginInput: LoginInput):ResponseEntity<?> {
		var retVal = service.loginUser(name, loginInput) 
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			//No user with given name was found - return NOT_FOUND
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND) 
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			//Wrong password - return UNAUTHORIZED
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		}
		//Return OK
		return ResponseEntity<Int>(service.getUserId(name), HttpStatus.OK)
	}

	/**
	 * logoutUser - user logout to the system
	 * @param name - logged in user name
	 * @param logoutInput - data received with the request (for now only password)
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	@PutMapping("/User/logout/{name}")
    fun loginUser(@PathVariable name:String):ResponseEntity<Void> {
		var retVal = service.logoutUser(name, logoutInput) 
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			//No user with given name was found - return NOT_FOUND
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND) 
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			//Wrong password - return UNAUTHORIZED
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		}
		//Return OK
		return ResponseEntity<Void>(HttpStatus.OK)
	}

	
	/**
	 * updatePassword - update password for an existing user
	 * @param name - logged in user name
	 * @param passwords - data received with the request (old and new password)
	 * @return error Code according to result (wrong old password, wrong name, weak new password, ok)
	 */
	@PutMapping("/User/update/{name}")
	fun updatePassword(@PathVariable name:String, @RequestBody passwords:Passwords):ResponseEntity<Void> {
		var retVal = service.updatePassword(name, passwords)
		
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			//No user with given name was found - return NOT_FOUND
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND)
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			//Wrong password - return UNAUTHORIZED
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		} else if (retVal == ServiceRetVals.INVALID_PASSWORD) {
			//Returns NOT ACCEPTABLE for a weak password
			return ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE)
		} else {
			//Return OK
			return ResponseEntity<Void>(HttpStatus.OK)
		}
	}
	
	/**
	 * delUser - delete existing user from the repository
	 * @param name - logged in user name
	 * @param password - received password
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	@DeleteMapping("/User/{name}")
	fun delUser(@PathVariable name:String ,@RequestParam(value = "password") password: String):ResponseEntity<Void> {
		var retVal = service.delUser(name, password)
		
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			//No user with given name was found - return NOT_FOUND
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND) 
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			//Wrong password - return UNAUTHORIZED
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		}
		//Return OK
		return ResponseEntity<Void>(HttpStatus.OK)
	}
	


}