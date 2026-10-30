package org.example;

import java.util.List;
import org.example.entity.Student;
import org.example.list.CustomLinkedList;

public class StudentManager {

    private final CustomLinkedList<Student> students = new CustomLinkedList<>();

    // номер зачётки уникален, поэтому повторно такого студента не добавляем
    public boolean add(Student student) {
        if (indexOf(student.getRecordBookNumber()) != -1) {
            return false;
        }
        students.add(student);
        return true;
    }

    public int indexOf(String recordBookNumber) {
        int index = 0;
        for (Student student : students) {
            if (student.getRecordBookNumber().equals(recordBookNumber)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Student get(int index) {
        return students.get(index);
    }

    public Student remove(int index) {
        return students.remove(index);
    }

    public List<Student> getAll() {
        return students;
    }

    public int size() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }
}
