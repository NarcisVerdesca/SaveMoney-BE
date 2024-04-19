package com.myprojects.savemoney.utility;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public  class TitleOrDescriptionValidator {

    private static final String TITLE_REGEX = "^(?=[\\S\\s]{1,255}$)[\\S\\s]*";

    private static final String DESCRIPTION_REGEX = "^(?=[\\S\\s]{1,5000}$)[\\S\\s]*";


    public static boolean isValidTitle(String title) {
        return Pattern.matches(TITLE_REGEX, title);
    }

    public static boolean isValidDescription(String description) {
        return Pattern.matches(DESCRIPTION_REGEX, description);
    }


}
