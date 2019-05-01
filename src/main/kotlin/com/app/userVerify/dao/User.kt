package com.app.userVerify.dao

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType


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
    val osLoggedIn: Boolean = false    
)
