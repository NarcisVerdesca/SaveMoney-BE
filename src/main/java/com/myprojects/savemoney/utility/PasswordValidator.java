package com.myprojects.savemoney.utility;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static final String PASSWORD_REGEX =
            //  "^(?=.*[0-9])"
            "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[!@#$%^&+=])"
                    + "(?=\\S+$).{8,}$";// no limit password and "!" added
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
