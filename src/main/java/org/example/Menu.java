package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.example.entity.Student;
import org.example.exception.InvalidDataException;
import org.example.file.StudentFileReader;
import org.example.list.CustomLinkedList;
import org.example.report.StudentReport;
import org.example.sorting.QuickSort;
import org.example.sorting.SelectionSort;
import org.example.sorting.SortAlgorithm;
import org.example.util.InputUtils;
import org.example.util.Validator;

public class Menu {

    private static final String DEFAULT_FILE = "data/students.txt";
    private static final String TABLE_LINE =
            "+" + "-".repeat(5) + "+" + "-".repeat(9) + "+" + "-".repeat(7) + "+" + "-".repeat(38) + "+";

    private final InputUtils input;
    private final StudentManager manager = new StudentManager();
    private final StudentFileReader fileReader = new StudentFileReader();
    private final StudentReport report = new StudentReport();
    private final List<SortAlgorithm<Student>> algorithms = List.of(new SelectionSort<>(), new QuickSort<>());

    public Menu(InputUtils input) {
        this.input = input;
    }

    public void start() {
        int choice;
        do {
            printMenu();
            choice = input.askNumber("> ", 0, 7);
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    loadFromFile();
                    break;
                case 3:
                    showAll();
                    break;
                case 4:
                    findStudent();
                    break;
                case 5:
                    removeStudent();
                    break;
                case 6:
                    showReport();
                    break;
                case 7:
                    sortStudents();
                    break;
                case 0:
                    System.out.println("Работа программы завершена.");
                    break;
            }
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("---------- Студенты ----------");
        System.out.println("[1] Добавить студента");
        System.out.println("[2] Загрузить студентов из файла");
        System.out.println("[3] Показать список");
        System.out.println("[4] Найти по номеру зачётки");
        System.out.println("[5] Удалить по номеру зачётки");
        System.out.println("[6] Отчёт по успеваемости (Stream API)");
        System.out.println("[7] Сортировка");
        System.out.println("[0] Выход");
    }

    private void addStudent() {
        System.out.println("Новый студент");
        String name = readName();
        double grade = readGrade("Средний балл (от 2 до 5): ");
        String recordBook = readRecordBook();
        while (manager.indexOf(recordBook) != -1) {
            System.out.println("Ошибка: студент с зачёткой " + recordBook + " уже есть в списке");
            recordBook = readRecordBook();
        }

        Student student = new Student(name, grade, recordBook);
        manager.add(student);
        System.out.println("Добавлен: " + student);
    }

    private void loadFromFile() {
        String fileName = input.ask("Файл [" + DEFAULT_FILE + "]: ");
        if (fileName.isEmpty()) {
            fileName = DEFAULT_FILE;
        }
        if (fileName.length() > 1 && fileName.startsWith("\"") && fileName.endsWith("\"")) {
            fileName = fileName.substring(1, fileName.length() - 1);
        }

        try {
            List<String> errors = new CustomLinkedList<>();
            List<Student> loaded = fileReader.read(fileName, errors);
            int added = 0;
            for (Student student : loaded) {
                if (manager.add(student)) {
                    added++;
                } else {
                    errors.add("зачётка " + student.getRecordBookNumber() + " уже есть в списке, запись пропущена");
                }
            }

            System.out.println("Добавлено студентов: " + added);
            if (!errors.isEmpty()) {
                System.out.println("Пропущено записей: " + errors.size());
                for (String error : errors) {
                    System.out.println("  * " + error);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден или к нему нет доступа: " + fileName);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + e.getMessage());
        }
    }

    private void showAll() {
        if (isListEmpty()) {
            return;
        }
        printTable(manager.getAll());
        System.out.println("Всего студентов: " + manager.size());
    }

    private void findStudent() {
        if (isListEmpty()) {
            return;
        }
        String recordBook = readRecordBook();
        int index = manager.indexOf(recordBook);
        if (index == -1) {
            System.out.println("Студент с зачёткой " + recordBook + " не найден");
            return;
        }
        Student student = manager.get(index);
        System.out.println("Найден (позиция в списке " + (index + 1) + "): " + student);
    }

    private void removeStudent() {
        if (isListEmpty()) {
            return;
        }
        String recordBook = readRecordBook();
        int index = manager.indexOf(recordBook);
        if (index == -1) {
            System.out.println("Студент с зачёткой " + recordBook + " не найден");
            return;
        }
        Student removed = manager.remove(index);
        System.out.println("Удалён: " + removed.getFullName() + ". Осталось студентов: " + manager.size());
    }

    private void showReport() {
        if (isListEmpty()) {
            return;
        }
        double minGrade = readGrade("Показать студентов со средним баллом от: ");
        List<String> lines = report.studentsWithGradeAtLeast(manager.getAll(), minGrade);

        if (lines.isEmpty()) {
            System.out.printf("Студентов со средним баллом от %.2f нет%n", minGrade);
        } else {
            System.out.printf("Студенты со средним баллом от %.2f (от лучшего к худшему):%n", minGrade);
            lines.forEach(line -> System.out.println("  " + line));
            System.out.println("Таких студентов: " + lines.size() + " из " + manager.size());
        }
        System.out.printf("Средний балл по всем студентам: %.2f%n", report.averageGrade(manager.getAll()));
        report.bestStudent(manager.getAll())
                .ifPresent(best -> System.out.println("Лучший студент: " + best.getFullName()));
    }

    private void sortStudents() {
        if (isListEmpty()) {
            return;
        }
        System.out.println("Алгоритм сортировки:");
        for (int i = 0; i < algorithms.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + algorithms.get(i).getTitle());
        }
        SortAlgorithm<Student> algorithm = algorithms.get(input.askNumber("Алгоритм: ", 1, algorithms.size()) - 1);

        System.out.println("Поле:  1) ФИО  2) средний балл  3) номер зачётки");
        int field = input.askNumber("Поле: ", 1, 3);
        Comparator<Student> comparator;
        if (field == 1) {
            // в Unicode буква Ё стоит отдельно от остального алфавита, поэтому сравниваем её как Е
            comparator = Comparator.comparing(student -> student.getFullName().replace('Ё', 'Е').replace('ё', 'е'));
        } else if (field == 2) {
            comparator = Comparator.comparingDouble(Student::getAverageGrade);
        } else {
            comparator = Comparator.comparing(Student::getRecordBookNumber);
        }

        System.out.println("Порядок:  1) по возрастанию  2) по убыванию");
        boolean ascending = input.askNumber("Порядок: ", 1, 2) == 1;

        algorithm.sort(manager.getAll(), comparator, ascending);
        System.out.println("Отсортировано: " + algorithm.getTitle());
        printTable(manager.getAll());
    }

    private void printTable(List<Student> students) {
        System.out.println(TABLE_LINE);
        System.out.printf("| %-3s | %-7s | %-5s | %-36s |%n", "№", "Зачётка", "Балл", "ФИО");
        System.out.println(TABLE_LINE);
        int number = 1;
        for (Student student : students) {
            System.out.printf("| %-3d | %-7s | %5.2f | %-36s |%n",
                    number++, student.getRecordBookNumber(), student.getAverageGrade(), student.getFullName());
        }
        System.out.println(TABLE_LINE);
    }

    private boolean isListEmpty() {
        if (manager.isEmpty()) {
            System.out.println("Список студентов пуст. Добавьте студентов через пункт 1 или 2.");
            return true;
        }
        return false;
    }

    private String readName() {
        while (true) {
            try {
                return Validator.validateName(input.ask("ФИО: "));
            } catch (InvalidDataException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private double readGrade(String prompt) {
        while (true) {
            try {
                return Validator.validateGrade(input.ask(prompt));
            } catch (InvalidDataException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private String readRecordBook() {
        while (true) {
            try {
                return Validator.validateRecordBook(input.ask("Номер зачётки: "));
            } catch (InvalidDataException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
