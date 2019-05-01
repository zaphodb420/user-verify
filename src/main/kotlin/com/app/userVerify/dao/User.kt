package com.app.userVerify.dao

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType

/**
 * User - Single entry structure in the database
 * @author      Oren Gur Arie
 */
@Entity
@Table(name = "Users")
public class User(
    @Id
    val id: Int = -1,
 
    @Column(name = "name")
    val name: String = "",
 
    @Column(name = "password")
    val password: String = "",

    @Column(name = "isLoggedIn")
    val isLoggedIn: Boolean = false    
)
