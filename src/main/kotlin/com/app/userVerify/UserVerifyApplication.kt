package com.app.userVerify

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ImportResource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan



@ImportResource("classpath*:applicationContext.xml")
@SpringBootApplication
@EnableJpaRepositories("com.app.userVerify")
@EntityScan("com.app.userVerify")
/**
 * UserVerifyApplication - Application caller for the spring boot API
 * @author      Oren Gur Arie
 */
open class UserVerifyApplication

	
fun main(args: Array<String>) {
	//Start the application
	SpringApplication.run(UserVerifyApplication::class.java, *args)
}
