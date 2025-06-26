package models;

import utils.GradeCalculator;
import utils.PDFGenerator;

public class Student {
    private String name;
    private String fatherName;
    private String usn;
    private double[] marks;
    private int[] creditsAssigned;
    private String[] subjects;
    private double sgpa;
    private static String facultyName;
    private static String programme;

    public Student(String name, String fatherName, String usn, int subjectsCount, String[] subjects, int[] credits) {
        this.name = name;
        this.fatherName = fatherName;
        this.usn = usn;
        this.marks = new double[subjectsCount];
        this.subjects = subjects;
        this.creditsAssigned = credits;
    }

    public void setMarks(int index, double mark) {
        marks[index] = mark;
    }

    public void calculatePerformance() {
        this.sgpa = GradeCalculator.calculateSGPA(marks, creditsAssigned);
    }

    public void generateAdmitCard() {
        PDFGenerator.generateResultSheet(this);
    }

    // Getters
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public String getUsn() { return usn; }
    public double[] getMarks() { return marks; }
    public int[] getCreditsAssigned() { return creditsAssigned; }
    public String[] getSubjects() { return subjects; }
    public double getSgpa() { return sgpa; }
    public static String getFacultyName() { return facultyName; }
    public static String getProgramme() { return programme; }

    // Static setter for faculty and programme
    public static void setFacultyAndProgramme(String faculty, String program) {
        facultyName = faculty;
        programme = program;
    }
}