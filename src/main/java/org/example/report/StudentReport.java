package org.example.report;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.entity.Student;
import org.example.list.CustomLinkedList;

public class StudentReport {

    // студенты с баллом не ниже minGrade, от лучшего к худшему, в виде "Фамилия И. О.   4,50"
    public List<String> studentsWithGradeAtLeast(List<Student> students, double minGrade) {
        return students.stream()
                .filter(student -> student.getAverageGrade() >= minGrade)
                .sorted(Comparator.comparingDouble(Student::getAverageGrade).reversed())
                .map(student -> String.format("%-24s %.2f", shortName(student.getFullName()), student.getAverageGrade()))
                .collect(Collectors.toCollection(CustomLinkedList::new));
    }

    public double averageGrade(List<Student> students) {
        return students.stream()
                .mapToDouble(Student::getAverageGrade)
                .average()
                .orElse(0);
    }

    public Optional<Student> bestStudent(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(Student::getAverageGrade));
    }

    // "Иванов Иван Петрович" -> "Иванов И. П."
    private String shortName(String fullName) {
        String[] parts = fullName.split(" ");
        if (parts.length == 1) {
            return fullName;
        }
        return Arrays.stream(parts, 1, parts.length)
                .map(part -> part.charAt(0) + ".")
                .collect(Collectors.joining(" ", parts[0] + " ", ""));
    }
}
