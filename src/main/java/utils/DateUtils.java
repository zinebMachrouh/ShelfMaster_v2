package main.java.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static boolean handleDate(String date) {
        //String regex = "^(0[1-9]|1[0-2])/(0[1-9]|1\\d|2\\d|3[01])/\\d{4}$";
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        /*if (date.matches(regex)) {
            Scanner scanner = new Scanner(date).useDelimiter("/");

            int month = scanner.nextInt();
            int day = scanner.nextInt();
            int year = scanner.nextInt();

            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);

            if (year < currentYear) {
                return false;
            } else {
                if(month == 2 && day > 28 && !isLeapYear){
                    return false;
                } else if (month == 2 && day > 29 && isLeapYear) {
                    return false;
                } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }*/
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            if (parsedDate.isAfter(LocalDate.now()) ) {
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static Object toDateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static LocalDate fromDateString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

}
