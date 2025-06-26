package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SetupPanel setupPanel;
    private StudentInputPanel studentInputPanel;
    
    public MainFrame() {
        setTitle("Student Grading System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        setupPanel = new SetupPanel(this);
        studentInputPanel = new StudentInputPanel(this);
        
        mainPanel.add(setupPanel, "setup");
        mainPanel.add(studentInputPanel, "studentInput");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "setup");
    }
    
    public void switchToPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void initializeStudentPanel(int studentCount, int subjectCount, String[] subjects, int[] credits) {
        studentInputPanel.initialize(studentCount, subjectCount, subjects, credits);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
