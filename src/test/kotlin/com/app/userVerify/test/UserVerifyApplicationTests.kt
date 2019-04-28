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
//@WebMvcTest
@SpringBootTest
@WebAppConfiguration

class UserVerifyApplicationTests() {
	@Autowired
	lateinit private  var context:WebApplicationContext;
	
	 lateinit private var mockMvc: MockMvc
	
	@Before
	public fun setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	fun addUserAPI() {
		val name:String = "joe"
		
		val password:String = "pass"
		val invalidPassword = "pas"
		val invalidPayload = mapOf(
                "password" to invalidPassword,
                "name" to name
        )
		
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(invalidPayload).toString()))
						.andExpect(status().isNotAcceptable)
		
		val payload = mapOf(
                "password" to password,
                "name" to name
        )
		
		
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)

		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isConflict)
		}
	
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
		
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/".plus(wrongName))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload2).toString()))
						.andExpect(status().isNotFound)
		
		mockMvc.perform(MockMvcRequestBuilders
					.put("/User/".plus(name))
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(JSONObject(invalidPayload).toString()))
					.andExpect(status().isNotAcceptable)
				
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(wrongPayload).toString()))
						.andExpect(status().isUnauthorized)
		
		mockMvc.perform(MockMvcRequestBuilders
						.put("/User/".plus(name))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload2).toString()))
						.andExpect(status().isOk)
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", oldPassword))
						.andExpect(status().isUnauthorized)
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", newPassword))
						.andExpect(status().isOk)
		
	}
	

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
		
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)
		
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(wrongName))
						.param("password", password))
						.andExpect(status().isNotFound)

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(name))
						.param("password", wrongPassword))
						.andExpect(status().isUnauthorized)		
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", password))
						.andExpect(status().isOk)
		
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/User/".plus(name))
						.param("password", password))
						.andExpect(status().isOk)		
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", password))
						.andExpect(status().isNotFound)
		
	}

		
	@Test
	fun verifyUserAPI() {
		val name:String = "joe2"
		val wrongName:String = "jack1"
		val correctPassword:String = "pass"
		val wrongPassword:String = "passs"
		
		val payload = mapOf(
                "password" to correctPassword,
                "name" to name
        )
	
		mockMvc.perform(MockMvcRequestBuilders
						.post("/User")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(JSONObject(payload).toString()))
						.andExpect(status().isOk)

		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", wrongPassword))
						.andExpect(status().isUnauthorized)

		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(wrongName))
						.param("password", correctPassword))
						.andExpect(status().isNotFound)

		mockMvc.perform(MockMvcRequestBuilders
						.get("/User/".plus(name))
						.param("password", correctPassword))
						.andExpect(status().isOk)
					
	}
}
