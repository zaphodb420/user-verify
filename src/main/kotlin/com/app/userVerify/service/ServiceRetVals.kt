package com.app.userVerify.service

/**
 * ServiceRetVals- Enum for the service layer return values
 * Mostly represents error codes
 * @author      Oren Gur Arie
 */
enum class ServiceRetVals{
    OK,
    USER_EXIST,
    USER_DOES_NOT_EXIST,
    WRONG_PASSWORD,
    INVALID_PASSWORD
}