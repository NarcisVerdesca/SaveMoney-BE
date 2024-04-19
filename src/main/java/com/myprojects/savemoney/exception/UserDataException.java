package com.myprojects.savemoney.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDataException extends Exception{

    public UserDataException() {

    }

    public UserDataException(String s) {
        super(s);
    }

    public UserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String  passwordDoesNotRespectRegexException() {
        return "Password does not respect regex !";
    }

    public static String  invalidPasswordException() {
        return "Invalid Password!";
    }

    public static  String emailInvalidOrExist(){
        return "Invalid or existing email";
    }

    public static String userMustHaveAbilities(){
        return "The ability field cannot be empty";
    }

    public static String somethingGoesWrong() {
        return "Something goes wrong!";
    }

}
