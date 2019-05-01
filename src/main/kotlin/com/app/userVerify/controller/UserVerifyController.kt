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
import com.app.userVerify.service.UserVerifyService
import com.app.userVerify.service.ServiceRetVals
import com.app.userVerify.dao.UserRepo

@RestController
class UserVerifyController(@Autowired var userRepo:UserRepo) {
	
	
	var service:UserVerifyService = UserVerifyService(userRepo)
	
	@PostMapping("/User")
	@ResponseBody
	fun addUser(@RequestBody user: UserDetails):ResponseEntity<Void> {
		var retVal =service.addUser(user) 
		if (retVal == ServiceRetVals.USER_EXIST) {
			return ResponseEntity<Void>(HttpStatus.CONFLICT)
		} else if (retVal == ServiceRetVals.INVALID_PASSWORD) {
			return ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE)
		} else {
			return ResponseEntity<Void>(HttpStatus.OK)
		}
		
	}
	
	@PostMapping("/User/login/{name}")
    fun loginUser(@PathVariable name:String, @RequestParam(value = "password") password: String):ResponseEntity<Void> {
		var retVal = service.verifyUser(name, password) 
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND) 
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		}
		return ResponseEntity<Void>(HttpStatus.OK)
	}
	
	@PutMapping("/User/{name}")
	fun updatePassword(@PathVariable name:String, @RequestBody passwords:Passwords):ResponseEntity<Void> {
		var retVal = service.updatePassword(name, passwords)
		
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND)
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		} else if (retVal == ServiceRetVals.INVALID_PASSWORD) {
			return ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE)
		} else {
			return ResponseEntity<Void>(HttpStatus.OK)
		}
	}
	
	
	@DeleteMapping("/User/{name}")
	fun delUser(@PathVariable name:String ,@RequestParam(value = "password") password: String):ResponseEntity<Void> {
		var retVal = service.delUser(name, password)
		
		if (retVal == ServiceRetVals.USER_DOES_NOT_EXIST) {
			return ResponseEntity<Void>(HttpStatus.NOT_FOUND) 
		} else if (retVal == ServiceRetVals.WRONG_PASSWORD) {
			return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
		}
		return ResponseEntity<Void>(HttpStatus.OK)
	}
	


}