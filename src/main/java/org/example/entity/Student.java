package org.example.entity;

public class Student {

    private final String fullName;
    private final double averageGrade;
    private final String recordBookNumber;

    public Student(String fullName, double averageGrade, String recordBookNumber) {
        this.fullName = fullName;
        this.averageGrade = averageGrade;
        this.recordBookNumber = recordBookNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public String toString() {
        return fullName + ", зачётка " + recordBookNumber + ", средний балл " + String.format("%.2f", averageGrade);
    }
}
