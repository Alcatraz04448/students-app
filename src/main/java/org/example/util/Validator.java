package org.example.util;

import java.util.regex.Pattern;
import org.example.exception.InvalidDataException;

public final class Validator {

    public static final double MIN_GRADE = 2.0;
    public static final double MAX_GRADE = 5.0;
    private static final int MAX_NAME_LENGTH = 60;

    // слова из букв, между ними пробел или дефис: "Иванов Иван", "Салтыков-Щедрин Михаил"
    private static final Pattern NAME = Pattern.compile("[А-Яа-яЁёA-Za-z]+([ -][А-Яа-яЁёA-Za-z]+)*");
    private static final Pattern GRADE = Pattern.compile("\\d+([.,]\\d+)?");
    private static final Pattern RECORD_BOOK = Pattern.compile("\\d{6}");

    private Validator() {
    }

    public static String validateName(String text) throws InvalidDataException {
        String name = text.trim().replaceAll("\\s+", " ");
        if (name.isEmpty()) {
            throw new InvalidDataException("ФИО не заполнено");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidDataException("ФИО длиннее " + MAX_NAME_LENGTH + " символов");
        }
        if (!NAME.matcher(name).matches()) {
            throw new InvalidDataException("в ФИО допустимы только буквы, пробелы и дефис");
        }
        return name;
    }

    public static double validateGrade(String text) throws InvalidDataException {
        String value = text.trim();
        if (!GRADE.matcher(value).matches()) {
            throw new InvalidDataException("средний балл должен быть числом, например 4.5");
        }
        double grade = Double.parseDouble(value.replace(',', '.'));
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new InvalidDataException("средний балл должен быть в диапазоне от 2 до 5");
        }
        return grade;
    }

    public static String validateRecordBook(String text) throws InvalidDataException {
        String value = text.trim();
        if (!RECORD_BOOK.matcher(value).matches()) {
            throw new InvalidDataException("номер зачётки - это 6 цифр, например 210345");
        }
        return value;
    }
}
