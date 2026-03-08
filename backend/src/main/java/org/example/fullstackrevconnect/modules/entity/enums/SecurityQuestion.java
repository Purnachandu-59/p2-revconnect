package org.example.fullstackrevconnect.modules.entity.enums;

public enum SecurityQuestion {

    PET_NAME("What is your pet's name?"),
    MOTHER_MAIDEN_NAME("What is your mother's maiden name?"),
    FIRST_SCHOOL("What was your first school name?"),
    FAVORITE_TEACHER("What is your favorite teacher's name?"),
    BIRTH_CITY("What city were you born in?");

    private final String question;

    SecurityQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}