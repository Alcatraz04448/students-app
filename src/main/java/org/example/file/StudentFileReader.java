package org.example.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.example.entity.Student;
import org.example.exception.InvalidDataException;
import org.example.list.CustomLinkedList;
import org.example.util.Validator;

public class StudentFileReader {

    private static final String SEPARATOR = ";";

    /*
     * Формат строки: ФИО;средний балл;номер зачётки
     * Некорректные строки не загружаются, описание ошибки добавляется в errors.
     */
    public List<Student> read(String fileName, List<String> errors) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(fileName);
        }
        if (file.isDirectory()) {
            throw new IOException(fileName + " - это папка, а не файл");
        }

        List<Student> students = new CustomLinkedList<>();
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // символ-заменитель появляется, если байты не удалось прочитать как UTF-8
                if (line.indexOf('�') >= 0) {
                    throw new IOException("файл должен быть сохранён в кодировке UTF-8");
                }
                if (reader.getLineNumber() == 1 && line.startsWith("﻿")) {
                    line = line.substring(1);
                }
                if (line.isBlank()) {
                    continue;
                }

                try {
                    students.add(parseStudent(line));
                } catch (InvalidDataException e) {
                    errors.add("строка " + reader.getLineNumber() + ": " + e.getMessage());
                }
            }
        }
        return students;
    }

    private Student parseStudent(String line) throws InvalidDataException {
        String[] fields = line.split(SEPARATOR);
        if (fields.length != 3) {
            throw new InvalidDataException("должно быть 3 поля через ';', а найдено " + fields.length);
        }
        String name = Validator.validateName(fields[0]);
        double grade = Validator.validateGrade(fields[1]);
        String recordBook = Validator.validateRecordBook(fields[2]);
        return new Student(name, grade, recordBook);
    }
}
