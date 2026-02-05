package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import database.DBQuestion;
import models.Question;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * QuestionManagement - Admin panel for managing quiz questions
 * Features: Add, Edit, Delete questions with table view
 */
public class QuestionManagement extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable questionsTable;
    private DefaultTableModel tableModel;
    
    // Form components
    private JTextArea questionTextArea;
    private JTextField optionAField;
    private JTextField optionBField;
    private JTextField optionCField;
    private JTextField optionDField;
    private JComboBox<String> difficultyComboBox;
    private ButtonGroup correctAnswerGroup;
    private JRadioButton radioA;
    private JRadioButton radioB;
    private JRadioButton radioC;
    private JRadioButton radioD;
    
    private Question selectedQuestion = null; // For editing

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    QuestionManagement frame = new QuestionManagement();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public QuestionManagement() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        panel.setBounds(25, 20, 840, 570);
        contentPane.add(panel);
        panel.setLayout(null);
        
        // Title
        JLabel lblTitle = new JLabel("Question Management");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Courier New", Font.BOLD, 20));
        lblTitle.setBounds(250, 11, 340, 30);
        panel.add(lblTitle);
        
        // ========== QUESTIONS TABLE SECTION ==========
        
        JLabel lblAllQuestions = new JLabel("All Questions:");
        lblAllQuestions.setFont(new Font("Courier New", Font.BOLD, 14));
        lblAllQuestions.setBounds(20, 52, 200, 20);
        panel.add(lblAllQuestions);
        
        // Create table
        String[] columnNames = {"ID", "Question", "Difficulty", "Correct"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        questionsTable = new JTable(tableModel);
        questionsTable.setFont(new Font("Courier New", Font.PLAIN, 11));
        questionsTable.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 11));
        questionsTable.setRowHeight(22);
        
        // Set column widths
        questionsTable.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        questionsTable.getColumnModel().getColumn(1).setPreferredWidth(400);  // Question
        questionsTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Difficulty
        questionsTable.getColumnModel().getColumn(3).setPreferredWidth(60);   // Correct
        
        JScrollPane scrollPane = new JScrollPane(questionsTable);
        scrollPane.setBounds(20, 77, 800, 150);
        panel.add(scrollPane);
        
        // ========== ADD/EDIT FORM SECTION ==========
        
        JLabel lblFormTitle = new JLabel("Add / Edit Question");
        lblFormTitle.setFont(new Font("Courier New", Font.BOLD, 14));
        lblFormTitle.setBounds(20, 240, 250, 20);
        panel.add(lblFormTitle);
        
        // Question Text
        JLabel lblQuestion = new JLabel("Question:");
        lblQuestion.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblQuestion.setBounds(20, 268, 100, 20);
        panel.add(lblQuestion);
        
        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
        questionScrollPane.setBounds(20, 290, 800, 60);
        panel.add(questionScrollPane);
        
        // Options
        JLabel lblOptions = new JLabel("Options:");
        lblOptions.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblOptions.setBounds(20, 360, 100, 20);
        panel.add(lblOptions);
        
        // Option A
        JLabel lblOptA = new JLabel("A:");
        lblOptA.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblOptA.setBounds(20, 385, 20, 20);
        panel.add(lblOptA);
        
        optionAField = new JTextField();
        optionAField.setFont(new Font("Courier New", Font.PLAIN, 11));
        optionAField.setBounds(45, 385, 360, 25);
        panel.add(optionAField);
        
        radioA = new JRadioButton("");
        radioA.setBounds(410, 385, 25, 25);
        panel.add(radioA);
        
        // Option B
        JLabel lblOptB = new JLabel("B:");
        lblOptB.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblOptB.setBounds(20, 415, 20, 20);
        panel.add(lblOptB);
        
        optionBField = new JTextField();
        optionBField.setFont(new Font("Courier New", Font.PLAIN, 11));
        optionBField.setBounds(45, 415, 360, 25);
        panel.add(optionBField);
        
        radioB = new JRadioButton("");
        radioB.setBounds(410, 415, 25, 25);
        panel.add(radioB);
        
        // Option C
        JLabel lblOptC = new JLabel("C:");
        lblOptC.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblOptC.setBounds(20, 445, 20, 20);
        panel.add(lblOptC);
        
        optionCField = new JTextField();
        optionCField.setFont(new Font("Courier New", Font.PLAIN, 11));
        optionCField.setBounds(45, 445, 360, 25);
        panel.add(optionCField);
        
        radioC = new JRadioButton("");
        radioC.setBounds(410, 445, 25, 25);
        panel.add(radioC);
        
        // Option D
        JLabel lblOptD = new JLabel("D:");
        lblOptD.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblOptD.setBounds(20, 475, 20, 20);
        panel.add(lblOptD);
        
        optionDField = new JTextField();
        optionDField.setFont(new Font("Courier New", Font.PLAIN, 11));
        optionDField.setBounds(45, 475, 360, 25);
        panel.add(optionDField);
        
        radioD = new JRadioButton("");
        radioD.setBounds(410, 475, 25, 25);
        panel.add(radioD);
        
        // Group radio buttons
        correctAnswerGroup = new ButtonGroup();
        correctAnswerGroup.add(radioA);
        correctAnswerGroup.add(radioB);
        correctAnswerGroup.add(radioC);
        correctAnswerGroup.add(radioD);
        
        // Difficulty
        JLabel lblDifficulty = new JLabel("Difficulty:");
        lblDifficulty.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblDifficulty.setBounds(500, 385, 100, 20);
        panel.add(lblDifficulty);
        
        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Beginner", "Intermediate", "Advanced"}));
        difficultyComboBox.setFont(new Font("Courier New", Font.PLAIN, 11));
        difficultyComboBox.setBackground(Color.LIGHT_GRAY);
        difficultyComboBox.setBounds(500, 408, 150, 30);
        panel.add(difficultyComboBox);
        
        // ========== BUTTONS ==========
        
        // Add Button
        JButton btnAdd = new JButton("Add Question");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });
        btnAdd.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnAdd.setBackground(new Color(144, 238, 144)); // Light green
        btnAdd.setBounds(500, 450, 140, 35);
        panel.add(btnAdd);
        
        // Update Button
        JButton btnUpdate = new JButton("Update Question");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateQuestion();
            }
        });
        btnUpdate.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnUpdate.setBackground(new Color(135, 206, 250)); // Light blue
        btnUpdate.setBounds(650, 450, 150, 35);
        panel.add(btnUpdate);
        
        // Load to Edit Button
        JButton btnLoadEdit = new JButton("Load Selected");
        btnLoadEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSelectedQuestion();
            }
        });
        btnLoadEdit.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnLoadEdit.setBackground(Color.LIGHT_GRAY);
        btnLoadEdit.setBounds(500, 490, 140, 30);
        panel.add(btnLoadEdit);
        
        // Delete Button
        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteQuestion();
            }
        });
        btnDelete.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnDelete.setBackground(new Color(255, 99, 71)); // Tomato red
        btnDelete.setBounds(650, 490, 150, 30);
        panel.add(btnDelete);
        
        // Clear Form Button
        JButton btnClear = new JButton("Clear Form");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        btnClear.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnClear.setBackground(Color.LIGHT_GRAY);
        btnClear.setBounds(650, 525, 150, 30);
        panel.add(btnClear);
        
        // Refresh Table Button
        JButton btnRefresh = new JButton("Refresh Table");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadQuestions();
            }
        });
        btnRefresh.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnRefresh.setBackground(Color.LIGHT_GRAY);
        btnRefresh.setBounds(500, 525, 140, 30);
        panel.add(btnRefresh);
        
        // Back to Dashboard Button
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad = new AdminDashboard();
                ad.setVisible(true);
                dispose();
            }
        });
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnBack.setBackground(new Color(169, 169, 169));
        btnBack.setBounds(20, 525, 180, 35);
        panel.add(btnBack);
        
        // Load questions on startup
        loadQuestions();
    }
    
    /**
     * Load all questions into the table
     */
    private void loadQuestions() {
        tableModel.setRowCount(0);
        List<Question> questions = DBQuestion.getAllQuestions();
        
        for (Question q : questions) {
            Object[] rowData = {
                q.getQuestionId(),
                truncateText(q.getQuestionText(), 60),
                q.getDifficultyLevel(),
                q.getCorrectOption()
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * Truncate text for display in table
     */
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
    
    /**
     * Add a new question
     */
    private void addQuestion() {
        // Validate inputs
        if (!validateForm()) {
            return;
        }
        
        // Get values from form
        String questionText = questionTextArea.getText().trim();
        String optA = optionAField.getText().trim();
        String optB = optionBField.getText().trim();
        String optC = optionCField.getText().trim();
        String optD = optionDField.getText().trim();
        char correctAnswer = getSelectedCorrectAnswer();
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        
        // Create Question object
        Question newQuestion = new Question(questionText, optA, optB, optC, optD, correctAnswer, difficulty);
        
        // Add to database
        boolean success = DBQuestion.addQuestion(newQuestion);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Question added successfully!");
            clearForm();
            loadQuestions();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding question!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update existing question
     */
    private void updateQuestion() {
        if (selectedQuestion == null) {
            JOptionPane.showMessageDialog(this, "Please load a question first using 'Load Selected' button!");
            return;
        }
        
        // Validate inputs
        if (!validateForm()) {
            return;
        }
        
        // Get values from form
        String questionText = questionTextArea.getText().trim();
        String optA = optionAField.getText().trim();
        String optB = optionBField.getText().trim();
        String optC = optionCField.getText().trim();
        String optD = optionDField.getText().trim();
        char correctAnswer = getSelectedCorrectAnswer();
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        
        // Update Question object
        selectedQuestion.setQuestionText(questionText);
        selectedQuestion.setOptionA(optA);
        selectedQuestion.setOptionB(optB);
        selectedQuestion.setOptionC(optC);
        selectedQuestion.setOptionD(optD);
        selectedQuestion.setCorrectOption(correctAnswer);
        selectedQuestion.setDifficultyLevel(difficulty);
        
        // Update in database
        boolean success = DBQuestion.updateQuestion(selectedQuestion);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Question updated successfully!");
            clearForm();
            loadQuestions();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating question!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Load selected question from table into form for editing
     */
    private void loadSelectedQuestion() {
        int selectedRow = questionsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question from the table!");
            return;
        }
        
        // Get question ID from table
        int questionId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Fetch full question from database
        selectedQuestion = DBQuestion.getQuestionById(questionId);
        
        if (selectedQuestion != null) {
            // Populate form
            questionTextArea.setText(selectedQuestion.getQuestionText());
            optionAField.setText(selectedQuestion.getOptionA());
            optionBField.setText(selectedQuestion.getOptionB());
            optionCField.setText(selectedQuestion.getOptionC());
            optionDField.setText(selectedQuestion.getOptionD());
            
            // Set correct answer radio button
            switch (selectedQuestion.getCorrectOption()) {
                case 'A': radioA.setSelected(true); break;
                case 'B': radioB.setSelected(true); break;
                case 'C': radioC.setSelected(true); break;
                case 'D': radioD.setSelected(true); break;
            }
            
            // Set difficulty
            difficultyComboBox.setSelectedItem(selectedQuestion.getDifficultyLevel());
            
            JOptionPane.showMessageDialog(this, "Question loaded! You can now edit and click 'Update Question'.");
        } else {
            JOptionPane.showMessageDialog(this, "Error loading question!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete selected question
     */
    private void deleteQuestion() {
        int selectedRow = questionsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question from the table to delete!");
            return;
        }
        
        // Get question ID
        int questionId = (int) tableModel.getValueAt(selectedRow, 0);
        String questionPreview = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this question?\n\n" + questionPreview,
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean success = DBQuestion.deleteQuestion(questionId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Question deleted successfully!");
                clearForm();
                loadQuestions();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting question!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Clear the form
     */
    private void clearForm() {
        questionTextArea.setText("");
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        correctAnswerGroup.clearSelection();
        difficultyComboBox.setSelectedIndex(0);
        selectedQuestion = null;
    }
    
    /**
     * Validate form inputs
     */
    private boolean validateForm() {
        // Check question text
        if (questionTextArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the question text!");
            return false;
        }
        
        // Check options
        if (optionAField.getText().trim().isEmpty() || 
            optionBField.getText().trim().isEmpty() ||
            optionCField.getText().trim().isEmpty() ||
            optionDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all four options!");
            return false;
        }
        
        // Check correct answer selected
        if (correctAnswerGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select the correct answer!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Get selected correct answer
     */
    private char getSelectedCorrectAnswer() {
        if (radioA.isSelected()) return 'A';
        if (radioB.isSelected()) return 'B';
        if (radioC.isSelected()) return 'C';
        if (radioD.isSelected()) return 'D';
        return 'A'; // Default
    }
}