package gui;

import models.Student;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentInputPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField usnField;
    private ArrayList<JTextField> markFields;
    private String[] subjects;
    private int[] credits;
    private int totalStudents;
    private int currentStudent;
    private ArrayList<Student> students;
    private JLabel studentCountLabel;
    
    public StudentInputPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        students = new ArrayList<>();
    }
    
    public void initialize(int studentCount, int subjectCount, String[] subjects, int[] credits) {
        this.subjects = subjects;
        this.credits = credits;
        this.totalStudents = studentCount;
        this.currentStudent = 0;
        
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }
    
    private void setupUI() {
        // Student count indicator
        studentCountLabel = new JLabel("Student " + (currentStudent + 1) + " of " + totalStudents);
        studentCountLabel.setHorizontalAlignment(JLabel.CENTER);
        add(studentCountLabel, BorderLayout.NORTH);
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        nameField = new JTextField();
        fatherNameField = new JTextField();
        usnField = new JTextField();
        markFields = new ArrayList<>();
        
        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Father's Name:"));
        formPanel.add(fatherNameField);
        formPanel.add(new JLabel("USN:"));
        formPanel.add(usnField);
        
        // Add mark input fields
        for (int i = 0; i < subjects.length; i++) {
            formPanel.add(new JLabel(subjects[i] + " Marks:"));
            JTextField markField = new JTextField();
            markFields.add(markField);
            formPanel.add(markField);
        }
        
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> mainFrame.switchToPanel("setup"));
        buttonPanel.add(previousButton);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (validateInput()) {
                saveStudentData();
                if (currentStudent < totalStudents - 1) {
                    clearFields();
                    currentStudent++;
                    studentCountLabel.setText("Student " + (currentStudent + 1) + " of " + totalStudents);
                } else {
                    generateResults();
                }
            }
        });
        buttonPanel.add(submitButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Please enter student name");
            return false;
        }
        if (fatherNameField.getText().trim().isEmpty()) {
            showError("Please enter father's name");
            return false;
        }
        if (usnField.getText().trim().isEmpty()) {
            showError("Please enter USN");
            return false;
        }
        
        for (int i = 0; i < markFields.size(); i++) {
            try {
                double mark = Double.parseDouble(markFields.get(i).getText().trim());
                if (mark < 0 || mark > 100) {
                    showError("Marks for " + subjects[i] + " must be between 0 and 100");
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Please enter valid marks for " + subjects[i]);
                return false;
            }
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void saveStudentData() {
        Student student = new Student(
            nameField.getText(),
            fatherNameField.getText(),
            usnField.getText(),
            subjects.length,
            subjects,
            credits
        );
        
        for (int i = 0; i < markFields.size(); i++) {
            student.setMarks(i, Double.parseDouble(markFields.get(i).getText().trim()));
        }
        
        student.calculatePerformance();
        students.add(student);
    }
    
    private void clearFields() {
        nameField.setText("");
        fatherNameField.setText("");
        usnField.setText("");
        for (JTextField field : markFields) {
            field.setText("");
        }
    }
    
    private void generateResults() {
        for (Student student : students) {
            student.generateAdmitCard();
        }
        
        JOptionPane.showMessageDialog(this,
            "Result sheets have been generated for all students.",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
