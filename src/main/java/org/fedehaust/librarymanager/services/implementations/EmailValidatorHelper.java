package org.fedehaust.librarymanager.services.implementations;

public class EmailValidatorHelper {

    public static boolean isInvalid(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return !email.matches(regex);
    }
}