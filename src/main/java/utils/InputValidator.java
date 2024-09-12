package main.java.utils;

import java.util.UUID;

public class InputValidator {
    public boolean validateId(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean handleISBN(String isbn) {
        return isbn.matches("^(97(8|9))?[0-9]{9}([0-9]|X)$");
    }

    public static boolean handleNumber(int number) {
        if (number < 0) {
            return false;
        }else {
            return true;
        }
    }

    public static boolean handleTitle(String title) {
        return title.matches("^[a-zA-Z0-9 !,.?-]+$");
    }

    public static boolean handleAuthor(String author) {
        return author.matches("^[a-zA-Z ]+$");
    }

    public static boolean validateNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

}
