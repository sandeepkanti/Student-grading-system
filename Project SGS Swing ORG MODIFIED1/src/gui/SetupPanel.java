package gui;

import models.Student;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SetupPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField facultyField;
    private JTextField programmeField;
    private JSpinner studentCountSpinner;
    private JSpinner subjectCountSpinner;
    private ArrayList<JTextField> subjectFields;
    private ArrayList<JSpinner> creditSpinners;
    private JPanel subjectsPanel;
    
    public SetupPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        createBasicInfoPanel();
        createSubjectsPanel();
        createButtonPanel();
    }
    
    private void createBasicInfoPanel() {
        JPanel basicInfoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        facultyField = new JTextField();
        programmeField = new JTextField();
        studentCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        subjectCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        
        basicInfoPanel.add(new JLabel("Faculty:"));
        basicInfoPanel.add(facultyField);
        basicInfoPanel.add(new JLabel("Programme:"));
        basicInfoPanel.add(programmeField);
        basicInfoPanel.add(new JLabel("Number of Students:"));
        basicInfoPanel.add(studentCountSpinner);
        basicInfoPanel.add(new JLabel("Number of Subjects:"));
        basicInfoPanel.add(subjectCountSpinner);
        
        subjectCountSpinner.addChangeListener(e -> updateSubjectsPanel());
        
        add(basicInfoPanel, BorderLayout.NORTH);
    }
    
    private void createSubjectsPanel() {
        subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.Y_AXIS));
        subjectFields = new ArrayList<>();
        creditSpinners = new ArrayList<>();
        
        JScrollPane scrollPane = new JScrollPane(subjectsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        updateSubjectsPanel();
    }
    
    private void updateSubjectsPanel() {
        subjectsPanel.removeAll();
        subjectFields.clear();
        creditSpinners.clear();
        
        int count = (Integer) subjectCountSpinner.getValue();
        
        for (int i = 0; i < count; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel("Subject " + (i + 1) + ":"));
            
            JTextField subjectField = new JTextField(20);
            subjectFields.add(subjectField);
            row.add(subjectField);
            
            row.add(new JLabel("Credits:"));
            JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
            creditSpinners.add(creditSpinner);
            row.add(creditSpinner);
            
            subjectsPanel.add(row);
        }
        
        revalidate();
        repaint();
    }
    
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> mainFrame.switchToPanel("setup"));
        buttonPanel.add(previousButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (validateInput()) {
                proceedToStudentInput();
            }
        });
        buttonPanel.add(nextButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private boolean validateInput() {
        if (facultyField.getText().trim().isEmpty()) {
            showError("Please enter faculty name");
            return false;
        }
        if (programmeField.getText().trim().isEmpty()) {
            showError("Please enter programme name");
            return false;
        }
        
        for (int i = 0; i < subjectFields.size(); i++) {
            if (subjectFields.get(i).getText().trim().isEmpty()) {
                showError("Please enter name for Subject " + (i + 1));
                return false;
            }
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void proceedToStudentInput() {
        Student.setFacultyAndProgramme(facultyField.getText(), programmeField.getText());
        
        String[] subjects = subjectFields.stream()
            .map(JTextField::getText)
            .toArray(String[]::new);
            
        int[] credits = creditSpinners.stream()
            .mapToInt(spinner -> (Integer) spinner.getValue())
            .toArray();
            
        mainFrame.initializeStudentPanel(
            (Integer) studentCountSpinner.getValue(),
            subjects.length,
            subjects,
            credits
        );
        
        mainFrame.switchToPanel("studentInput");
    }
}
