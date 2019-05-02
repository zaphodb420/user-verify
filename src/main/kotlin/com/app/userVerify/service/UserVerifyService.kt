package com.app.userVerify.service


import java.util.concurrent.atomic.AtomicInteger
import com.app.userVerify.dao.UserRepo
import com.app.userVerify.dao.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.app.userVerify.Passwords
import com.app.userVerify.UserDetails
import com.app.userVerify.LoginInput
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Configurable

@Service
/**
 * UserVerifyService - Service layer for the UserVerify API
 * @Param userRepo repository object
 * @author      Oren Gur Arie
 */
public class UserVerifyService(var userRepo:UserRepo) {

	//Used for new user ID generation
	val counter = AtomicInteger()

	//Used for password quality verification
    val MIN_PASSWORD_LEN = 4

	/**
	 * addUser - adds a new user to the repository
	 * @param user - received details of the new user
	 * @return error Code according to result (weak password, user exists, ok)

	 */
	
	fun addUser(user: UserDetails):ServiceRetVals {
		var newUser = userRepo.findByName(user.name)
		if (newUser.isEmpty() == false)
			//There is already a user with the same name
			return ServiceRetVals.USER_EXIST
		if (checkPassword(user.password)) {
			userRepo.save(User(counter.incrementAndGet(), user.name, user.password, false))
			return ServiceRetVals.OK
		} else {
			//Password is not strong enough
			return ServiceRetVals.INVALID_PASSWORD
		}

	}

	/**
	 * loginUser - Change the state of a user to logged in
	 * @param name - logged in user name
	 * @param loginInput - data received with the request (for now only password)
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	
    fun loginUser(name:String, loginInput: LoginInput):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			//No user with that name
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == loginInput.password) {
			var newUser = User(user[0].id, user[0].name, user[0].password, true)
			userRepo.save(newUser)
			return ServiceRetVals.OK
		}
		//Wrong password received
		return ServiceRetVals.WRONG_PASSWORD
	}
	
		/**
	 * logoutUser - logout request for a given user
	 * @param name - logged in user name
	 * @param token - Token received with login request
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	
    fun logoutUser(name:String, token: String):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			//No user with that name
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].id == token.toInt()) {
			var newUser = User(user[0].id, user[0].name, user[0].password, false)
			userRepo.save(newUser)
			return ServiceRetVals.OK
		}
		//Wrong password received
		return ServiceRetVals.WRONG_PASSWORD
	}

	/**
	 * updatePassword - update password for an existing user
	 * @param name - logged in user name
	 * @param passwords - data received with the request (old and new password)
	 * @return error Code according to result (wrong old password, wrong name, weak new password, ok)
	 */

	fun updatePassword(name:String, passwords:Passwords):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			//User does not exist
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == passwords.oldPassword) {
			if (checkPassword(passwords.newPassword)) {
				var newUser = User(user[0].id, user[0].name, passwords.newPassword, user[0].isLoggedIn)
				userRepo.save(newUser)
				return ServiceRetVals.OK
			} else {
				//Weak password
				return ServiceRetVals.INVALID_PASSWORD
			}
			
		}
		//Mismatch between password and user name
		return ServiceRetVals.WRONG_PASSWORD
	}
	
	/**
	 * delUser - delete existing user from the repository
	 * @param name - logged in user name
	 * @param password - received password
	 * @return error Code according to result (wrong password, wrong name, ok)
	 */
	fun delUser(name:String ,password: String):ServiceRetVals {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			//User does not exist
			return ServiceRetVals.USER_DOES_NOT_EXIST
		if (user[0].password == password) {
			userRepo.deleteById(user[0].id)
			return ServiceRetVals.OK
		}
		//Mismatch between password and user name
		return ServiceRetVals.WRONG_PASSWORD
	}
	
	/**
	 * getUserId - returns UserID of a given name
	 * @param name - received name
	 * @return If user exists returns its ID otherwise return -1
	 */
	fun getUserId(name:String):Int {
		var user = userRepo.findByName(name)
		if (user.isEmpty() == true)
			return -1
		return user[0].id
	}

	/**
	 * checkPassword - tests the quality of a given password
	 * @param password - received password
	 * @return If password strong enough returns true otehrwise returns false
	 */
	private fun checkPassword(password: String):Boolean {
		//Force a minimum password length
		if (password.length < MIN_PASSWORD_LEN)
			return false
		else
			return true
	}
}
