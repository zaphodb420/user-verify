package com.app.userVerify.test

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.http.MediaType
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.context.annotation.ImportResource
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.junit.Before
import org.springframework.test.annotation.DirtiesContext



@RunWith(SpringRunner::class)
@SpringBootTest
@WebAppConfiguration
/**
 * UserVerifyApplicationTests - unit testing for the user verify API functionality
 * Covers Controller, Service and DAO functionality
 * @author      Oren Gur Arie
 */
class UserVerifyApplicationTests() {
	@Autowired
	lateinit private  var context:WebApplicationContext;
	 lateinit private var mockMvc: MockMvc


	/**
	 * setUp - Setting up the test environment
	 */
	@Before
	public fun setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	/**
	 * addUserAPI - Tests the AddUser functionality
	 */
	@Test
	fun addUserAPI() {
		val name:String = "joe"
		
		val password:String = "pass"
		val invalidPassword = "pas"
		val invalidPayload = mapOf(
                "password" to invalidPassword,
                "name" to name
        )
		
		//Try to add a user with unacceptable password
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(invalidPayload).toString()))
						.andExpect(status().isNotAcceptable)
		
		val payload = mapOf(
                "password" to password,
                "name" to name
        )
		
		//Try to add a user with correct parameters
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)

		//Try to add a user with existing name
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isConflict)
		}
	
	/**
	 * updatePasswordApi - Tests the UpdatePassword functionality
	 */
	@Test
	fun updatePasswordApi() {
		val name:String = "john"
		val wrongName:String = "john1"
		val oldPassword:String = "pass"
		val newPassword:String = "passpass"
		val invalidPassword:String = "pas"
		val payload = mapOf(
                "password" to oldPassword,
                "name" to name)
		
		val oldPasswordPayload = mapOf(
                "password" to oldPassword
        )

		val newPasswordPayload = mapOf(
                "password" to newPassword
        )

		//Add new user
		mockMvc.perform(MockMvcRequestBuilders
				.post("/User")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject(payload).toString()))
				.andExpect(status().isOk)
		
		val invalidPayload = mapOf(
							 "newPassword" to invalidPassword,
							 "oldPassword" to oldPassword)
		
		val wrongPayload = mapOf(
							"newPassword" to oldPassword,
							 "oldPassword" to newPassword)
		
		val payload2 = mapOf(
							 "newPassword" to newPassword,
							 "oldPassword" to oldPassword)
		
		//Try to update a password for non existing user
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/update/".plus(wrongName))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload2).toString()))
						.andExpect(status().isNotFound)
		
		//Try to update a password to an unacceptable password
		mockMvc.perform(MockMvcRequestBuilders
					.put("/User/update/".plus(name))
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(JSONObject(invalidPayload).toString()))
					.andExpect(status().isNotAcceptable)

		//Try to update a password with wrong current password value
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/update/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(wrongPayload).toString()))
						.andExpect(status().isUnauthorized)
		
		//Try to update a password with correct parametrs
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/update/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload2).toString()))
						.andExpect(status().isOk)
		
		//Try to login with old password
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(oldPasswordPayload).toString()))
						.andExpect(status().isUnauthorized)

		//Try to login with new password
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(newPasswordPayload).toString()))
						.andExpect(status().isOk)
	}
	

	/**
	 * delUserAPI - Tests the UpdatePassword functionality
	 */
	@Test
	fun delUserAPI() {
		val name:String = "joe1"
		val wrongName:String = "jack"
		
		val password:String = "pass"
		val wrongPassword = "pass2"

		val payload = mapOf(
                "password" to password,
                "name" to name
        )

		val passwordPayload = mapOf(
                "password" to password
        )
		
		//Add a new user
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)
		
		//Delete a user with wrong name
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(wrongName))
						.param("password", password))
						.andExpect(status().isNotFound)

		//Delete a user with wrong password
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(name))
						.param("password", wrongPassword))
						.andExpect(status().isUnauthorized)		
		
		//Access the user (which wasn't deleted yet)
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(passwordPayload).toString()))
						.andExpect(status().isOk)
		
		//Delete the user with correct parameters
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(name))
						.param("password", password))
						.andExpect(status().isOk)
		
		//Try to access the deleted user
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(passwordPayload).toString()))
						.andExpect(status().isNotFound)		
	}

	/**
	 * loginUserAPI - Tests the loginUser functionality
	 */		
	@Test
	fun loginUserAPI() {
		val name:String = "joe2"
		val wrongName:String = "jack1"
		val correctPassword:String = "pass"
		val wrongPassword:String = "passs"
		
		val payload = mapOf(
                "password" to correctPassword,
                "name" to name
        )

		val correctPasswordPayload = mapOf(
                "password" to correctPassword
        )

		val wrongPasswordPayload = mapOf(
                "password" to wrongPassword
        )

		//Add new user
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)

		//Try to login with wrong password
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(wrongPasswordPayload).toString()))
						.andExpect(status().isUnauthorized)

		//Try to login with wrong name
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(wrongName))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(correctPasswordPayload).toString()))
						.andExpect(status().isNotFound)
		
		//Login with correct parameters
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/login/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(correctPasswordPayload).toString()))
						.andExpect(status().isOk)
					
	}
}
