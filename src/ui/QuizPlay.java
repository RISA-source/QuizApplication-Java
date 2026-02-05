package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import logic.QuizEngine;
import models.Question;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * QuizPlay - Main quiz gameplay interface with 5-round system
 */
public class QuizPlay extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Quiz Engine
    private QuizEngine quizEngine;
    private String difficulty;
    
    // GUI Components
    private JLabel lblRound;
    private JLabel lblQuestion;
    private JLabel lblDifficulty;
    private JTextArea questionTextArea;
    private JRadioButton optionA;
    private JRadioButton optionB;
    private JRadioButton optionC;
    private JRadioButton optionD;
    private ButtonGroup optionsGroup;
    private JButton btnNext;
    private JButton btnFinishRound;
    
    // Colors for visual feedback
    private Color defaultColor = Color.LIGHT_GRAY;
    private Color selectedColor = new Color(135, 206, 250); // Sky blue

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    QuizPlay frame = new QuizPlay("Beginner");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame with selected difficulty.
     */
    public QuizPlay(String difficulty) {
        this.difficulty = difficulty;
        this.quizEngine = new QuizEngine(difficulty);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(27, 22, 680, 520);
        contentPane.add(panel);
        panel.setLayout(null);
        
        // Round label
        lblRound = new JLabel("Round: 1 of 5");
        lblRound.setHorizontalAlignment(SwingConstants.CENTER);
        lblRound.setFont(new Font("Courier New", Font.BOLD, 20));
        lblRound.setBounds(230, 11, 220, 32);
        panel.add(lblRound);
        
        // Question number label
        lblQuestion = new JLabel("Question: 1 of 5");
        lblQuestion.setHorizontalAlignment(SwingConstants.LEFT);
        lblQuestion.setFont(new Font("Courier New", Font.BOLD, 16));
        lblQuestion.setBounds(30, 54, 250, 25);
        panel.add(lblQuestion);
        
        // Difficulty label
        lblDifficulty = new JLabel("Difficulty: " + difficulty);
        lblDifficulty.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDifficulty.setFont(new Font("Courier New", Font.BOLD, 16));
        lblDifficulty.setBounds(400, 54, 250, 25);
        panel.add(lblDifficulty);
        
        // Question text area
        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBackground(Color.WHITE);
        JScrollPane questionScroll = new JScrollPane(questionTextArea);
        questionScroll.setBounds(30, 90, 620, 90);
        panel.add(questionScroll);
        
        // Options label
        JLabel lblOptions = new JLabel("Select your answer:");
        lblOptions.setFont(new Font("Courier New", Font.BOLD, 14));
        lblOptions.setBounds(30, 190, 250, 20);
        panel.add(lblOptions);
        
        // Create button group for options
        optionsGroup = new ButtonGroup();
        
        // Option A
        optionA = new JRadioButton("A. Option A");
        optionA.setFont(new Font("Courier New", Font.PLAIN, 13));
        optionA.setBackground(defaultColor);
        optionA.setBounds(30, 220, 300, 50);
        optionA.addActionListener(e -> updateOptionColors());
        optionsGroup.add(optionA);
        panel.add(optionA);
        
        // Option B
        optionB = new JRadioButton("B. Option B");
        optionB.setFont(new Font("Courier New", Font.PLAIN, 13));
        optionB.setBackground(defaultColor);
        optionB.setBounds(350, 220, 300, 50);
        optionB.addActionListener(e -> updateOptionColors());
        optionsGroup.add(optionB);
        panel.add(optionB);
        
        // Option C
        optionC = new JRadioButton("C. Option C");
        optionC.setFont(new Font("Courier New", Font.PLAIN, 13));
        optionC.setBackground(defaultColor);
        optionC.setBounds(30, 280, 300, 50);
        optionC.addActionListener(e -> updateOptionColors());
        optionsGroup.add(optionC);
        panel.add(optionC);
        
        // Option D
        optionD = new JRadioButton("D. Option D");
        optionD.setFont(new Font("Courier New", Font.PLAIN, 13));
        optionD.setBackground(defaultColor);
        optionD.setBounds(350, 280, 300, 50);
        optionD.addActionListener(e -> updateOptionColors());
        optionsGroup.add(optionD);
        panel.add(optionD);
        
        // Progress info
        JLabel lblProgress = new JLabel("Answer all questions to complete the round");
        lblProgress.setFont(new Font("Courier New", Font.ITALIC, 11));
        lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
        lblProgress.setBounds(30, 340, 620, 20);
        panel.add(lblProgress);
        
        // Next Question button
        btnNext = new JButton("Next Question");
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleNext();
            }
        });
        btnNext.setFont(new Font("Courier New", Font.BOLD, 13));
        btnNext.setBackground(new Color(144, 238, 144)); // Light green
        btnNext.setBounds(380, 380, 180, 45);
        panel.add(btnNext);
        
        // Finish Round button (hidden initially)
        btnFinishRound = new JButton("Finish Round");
        btnFinishRound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFinishRound();
            }
        });
        btnFinishRound.setFont(new Font("Courier New", Font.BOLD, 13));
        btnFinishRound.setBackground(new Color(255, 215, 0)); // Gold
        btnFinishRound.setBounds(380, 380, 180, 45);
        btnFinishRound.setVisible(false);
        panel.add(btnFinishRound);
        
        // Previous Question button
        JButton btnPrevious = new JButton("Previous");
        btnPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handlePrevious();
            }
        });
        btnPrevious.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnPrevious.setBackground(Color.LIGHT_GRAY);
        btnPrevious.setBounds(270, 380, 100, 45);
        panel.add(btnPrevious);
        
        // Exit/Quit button
        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    QuizPlay.this,
                    "Are you sure you want to quit?\nYour progress will be lost!",
                    "Quit Quiz",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    UserDashboard ud = new UserDashboard();
                    ud.setVisible(true);
                    dispose();
                }
            }
        });
        btnQuit.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnQuit.setForeground(Color.WHITE);
        btnQuit.setBackground(Color.RED);
        btnQuit.setBounds(120, 380, 100, 45);
        panel.add(btnQuit);
        
        // Start the game
        if (quizEngine.startGame()) {
            displayCurrentQuestion();
        } else {
            JOptionPane.showMessageDialog(this,
                "Not enough questions available for " + difficulty + " level!\n" +
                "Need at least 25 questions (5 rounds × 5 questions).",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            UserDashboard ud = new UserDashboard();
            ud.setVisible(true);
            dispose();
        }
    }
    
    /**
     * Display the current question
     */
    private void displayCurrentQuestion() {
        Question q = quizEngine.getCurrentQuestion();
        
        if (q == null) {
            return;
        }
        
        // Update labels
        lblRound.setText("Round: " + quizEngine.getCurrentRoundNumber() + " of " + quizEngine.getTotalRounds());
        lblQuestion.setText("Question: " + quizEngine.getCurrentQuestionNumber() + " of " + quizEngine.getQuestionsPerRound());
        
        // Update question text
        questionTextArea.setText(q.getQuestionText());
        
        // Update options
        optionA.setText("A. " + q.getOptionA());
        optionB.setText("B. " + q.getOptionB());
        optionC.setText("C. " + q.getOptionC());
        optionD.setText("D. " + q.getOptionD());
        
        // Clear selection and reset colors
        optionsGroup.clearSelection();
        resetOptionColors();
        
        // If this question was already answered, show previous answer
        Character previousAnswer = quizEngine.getCurrentAnswer();
        if (previousAnswer != null) {
            switch (previousAnswer) {
                case 'A': optionA.setSelected(true); break;
                case 'B': optionB.setSelected(true); break;
                case 'C': optionC.setSelected(true); break;
                case 'D': optionD.setSelected(true); break;
            }
            updateOptionColors();
        }
        
        // Show appropriate button
        if (quizEngine.getCurrentQuestionNumber() == quizEngine.getQuestionsPerRound()) {
            btnNext.setVisible(false);
            btnFinishRound.setVisible(true);
        } else {
            btnNext.setVisible(true);
            btnFinishRound.setVisible(false);
        }
    }
    
    /**
     * Update option colors based on selection
     */
    private void updateOptionColors() {
        optionA.setBackground(optionA.isSelected() ? selectedColor : defaultColor);
        optionB.setBackground(optionB.isSelected() ? selectedColor : defaultColor);
        optionC.setBackground(optionC.isSelected() ? selectedColor : defaultColor);
        optionD.setBackground(optionD.isSelected() ? selectedColor : defaultColor);
    }
    
    /**
     * Reset all option colors to default
     */
    private void resetOptionColors() {
        optionA.setBackground(defaultColor);
        optionB.setBackground(defaultColor);
        optionC.setBackground(defaultColor);
        optionD.setBackground(defaultColor);
    }
    
    /**
     * Get the selected option
     */
    private Character getSelectedOption() {
        if (optionA.isSelected()) return 'A';
        if (optionB.isSelected()) return 'B';
        if (optionC.isSelected()) return 'C';
        if (optionD.isSelected()) return 'D';
        return null;
    }
    
    /**
     * Handle Next button click
     */
    private void handleNext() {
        Character selected = getSelectedOption();
        
        if (selected == null) {
            int choice = JOptionPane.showConfirmDialog(this,
                "You haven't selected an answer.\nSkip this question?",
                "No Answer Selected",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        } else {
            quizEngine.submitAnswer(selected);
        }
        
        // Move to next question
        if (quizEngine.nextQuestion()) {
            displayCurrentQuestion();
        }
    }
    
    /**
     * Handle Previous button click
     */
    private void handlePrevious() {
        // Save current answer first
        Character selected = getSelectedOption();
        if (selected != null) {
            quizEngine.submitAnswer(selected);
        }
        
        // Move to previous question
        if (quizEngine.previousQuestion()) {
            displayCurrentQuestion();
        }
    }
    
    /**
     * Handle Finish Round button click
     */
    private void handleFinishRound() {
        // Save last answer
        Character selected = getSelectedOption();
        if (selected != null) {
            quizEngine.submitAnswer(selected);
        }
        
        // Get round results
        QuizEngine.RoundResult result = quizEngine.finishRound();
        
        // Show round score
        JOptionPane.showMessageDialog(this,
            String.format(
                "Round %d Complete!\n\n" +
                "Score: %d/%d\n" +
                "Percentage: %.1f%%\n\n" +
                "Great job!",
                result.roundNumber,
                result.score,
                result.totalQuestions,
                result.percentage
            ),
            "Round " + result.roundNumber + " Results",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Check if more rounds
        if (quizEngine.nextRound()) {
            // Continue to next round
            displayCurrentQuestion();
        } else {
            // All rounds done - show final results
            showFinalResults();
        }
    }
    
    /**
     * Show final game results after all 5 rounds
     */
    private void showFinalResults() {
        QuizEngine.GameResult gameResult = quizEngine.finishGame();
        
        if (gameResult == null) {
            JOptionPane.showMessageDialog(this,
                "Error saving game results!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Build results message
        StringBuilder message = new StringBuilder();
        message.append("🎉 GAME COMPLETE! 🎉\n\n");
        message.append("Round Scores:\n");
        
        for (int i = 0; i < gameResult.roundScores.size(); i++) {
            message.append(String.format("  Round %d: %d/%d\n",
                i + 1,
                gameResult.roundScores.get(i),
                quizEngine.getQuestionsPerRound()
            ));
        }
        
        message.append("\n━━━━━━━━━━━━━━━━━━━━━━\n");
        message.append(String.format("Total Score: %d/%d\n",
            gameResult.totalScore,
            gameResult.totalQuestions
        ));
        message.append(String.format("Average per Round: %.1f/%d\n",
            gameResult.averageScore,
            quizEngine.getQuestionsPerRound()
        ));
        message.append(String.format("Overall Percentage: %.1f%%\n",
            gameResult.overallPercentage
        ));
        message.append("━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        // Performance message
        if (gameResult.overallPercentage >= 90) {
            message.append("OUTSTANDING!");
        } else if (gameResult.overallPercentage >= 75) {
            message.append("EXCELLENT!");
        } else if (gameResult.overallPercentage >= 60) {
            message.append("GOOD JOB!");
        } else {
            message.append("KEEP PRACTICING!");
        }
        
        message.append("\n\nYour score has been saved!");
        
        JOptionPane.showMessageDialog(this,
            message.toString(),
            "Final Results - " + difficulty,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Return to dashboard
        UserDashboard ud = new UserDashboard();
        ud.setVisible(true);
        dispose();
    }
}