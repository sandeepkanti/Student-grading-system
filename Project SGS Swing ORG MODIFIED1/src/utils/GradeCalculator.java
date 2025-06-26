package utils;

public class GradeCalculator {
    public static double calculateSGPA(double[] marks, int[] credits) {
        double totalGradePoints = 0;
        int totalCredits = 0;

        for (int i = 0; i < marks.length; i++) {
            totalGradePoints += getGradePoints(marks[i]) * credits[i];
            totalCredits += credits[i];
        }

        return totalGradePoints / totalCredits;
    }

    public static int getGradePoints(double marks) {
        if (marks >= 90) return 10;
        if (marks >= 80) return 9;
        if (marks >= 70) return 8;
        if (marks >= 60) return 7;
        if (marks >= 50) return 6;
        if (marks >= 40) return 5;
        return 0;
    }

    public static String getLetterGrade(double marks) {
        if (marks >= 90) return "O";
        if (marks >= 80) return "A+";
        if (marks >= 70) return "A";
        if (marks >= 60) return "B+";
        if (marks >= 50) return "B";
        if (marks >= 40) return "C";
        return "F";
    }
}