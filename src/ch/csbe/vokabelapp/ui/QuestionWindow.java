package ch.csbe.vokabelapp.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author Julian
 * This is a singleton class.
 */
public class QuestionWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 3293374670142297068L;

    private static QuestionWindow instance;

    /**
     * Singleton getter for this class.
     *
     * @return The instance of this class.
     */
    public static QuestionWindow getInstance() {
        if (instance == null) {
            instance = new QuestionWindow();
        }
        return instance;
    }

    private JButton backButton;
    private JButton checkButton;
    private JTextField answerTextField;
    private JLabel questionLabel;
    private JLabel scoreLabel;
    private static int score;
    private int wordIndex = 0;
    private JPanel topContainer = new JPanel();
    private String path = MainWindow.getPath();

    private QuestionWindow() {

        System.out.println("question window path: " + path);

        // Initializing of the Window.
        Font questionFont = new Font("Arial", Font.BOLD, 40);
        this.setTitle("Vokabel App QuestionWindow");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Top Container
        this.topContainer.setLayout(new BorderLayout());
        this.add(topContainer, BorderLayout.PAGE_START);

        this.backButton = new JButton("←");
        this.backButton.setFont(backButton.getFont().deriveFont(Font.BOLD, 20));
        this.backButton.addActionListener(this);
        topContainer.add(backButton, BorderLayout.LINE_START);

        scoreLabel = new JLabel("score: ");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        topContainer.add(scoreLabel, BorderLayout.CENTER);

        // Center
        this.questionLabel = new JLabel();
        this.questionLabel.setHorizontalAlignment(JLabel.CENTER);
        this.questionLabel.setFont(questionFont);
        this.add(questionLabel, BorderLayout.CENTER);

        // Bottom Container
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BorderLayout());
        this.add(bottomContainer, BorderLayout.PAGE_END);

        this.answerTextField = new JTextField("please write your answer...");
        this.answerTextField.addActionListener(this);
        bottomContainer.add(this.answerTextField);

        this.checkButton = new JButton("Check");
        this.checkButton.addActionListener(this);
        bottomContainer.add(this.checkButton, BorderLayout.LINE_END);

    }

    /**
     * Ensures that when the backButton is clicked you are returned to the MainWindow.
     * Also sets the variables wordIndex and score back to 0.
     * Also repaints the scoreLabel, the topContainer and the questionLabel.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.backButton) {
            handleBackButton();
        }
        /**
         * Responsible for the handling when the checkButton is pressed.
         */
        if (event.getSource() == checkButton) {
            handleInput();
        }
        /**
         * Responsible for the handling when enter key is pressed in the answer field.
         */
        if (event.getSource() == this.answerTextField) {
            handleInput();
        }

    }

    /**
     * Checks if the currently selected file is an image
     *
     * @param filePath The file path of the file that has to be checked must be put here.
     * @return true if file ends with .png or .jpg / false if the file ends with something else
     */
    static boolean isImageFile(String filePath) {
        // Gets the file path in lower case and checks if the ending is .jpg or .png.
        String lowerCasePath = filePath.toLowerCase();
        return lowerCasePath.endsWith(".jpg") || lowerCasePath.endsWith(".png");
    }

    /**
     * Initializes a new FileReader that creates the hint from the word that is in
     * the txt file. The path is formed from the current word + the
     * MainWindow.getPath function. The reader reads the file character by
     * characters until it is finished. These characters are added to the hint variable character by
     * characters until the complete word is formed.
     *
     * @param index Used to tell the function which word to be at.
     * you are at.
     * @return The hint for the current word to solve.
     */
    private String getHint(int index) {
        // Puts hint in html mode.
        String hint = "<html>";
        String readerPath = MainWindow.getPath() + File.separator + MainWindow.getWords().get(index);

        try (FileReader reader = new FileReader(readerPath)) {
            int data;
            int characterCount = 0;  // Counts the read characters.

            while ((data = reader.read()) != -1) {
                char currentChar = (char) data;
                hint = hint + currentChar;
                characterCount++;

                // Adds a line break after every 47th character.
                if (characterCount - 47 == 0) {
                    hint = hint + "<br>";
                    characterCount = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Adds the closing html tag to the hint variable at the end to end the html mode.
        hint = hint + "</html>";
        return hint;
    }
    /**
     * Function used to get the current answer.
     *
     * @param index is used to get the current answer
     * @return the answer to the current query
     */
    private String getAnswer(int index) {
        String answer = MainWindow.getWordsMinusExtension().get(index);
        return answer;
    }

    /**
     * Function to get the current score of this class.
     *
     * @return Value of the score variable.
     */
    public static int getScore() {
        return score;
    }

    /**
     * Method to reset the score variable. Also resets the
     * scoreLabel and updates the questionLabel to the current
     * wordIndex
     */
    public void resetScore() {
        score = 0; // Score is set to 0.
        scoreLabel.setText("score: " + String.valueOf(score)); // Score Label is set to 0.
        this.answerTextField.setText(null); // Emptys the answer textfield.
        // checks if the current word is an image, if so questionlabel is updated to it.
        if (isImageFile(MainWindow.getWords().get(wordIndex))) {
            ImageIcon originalIcon = new ImageIcon(MainWindow.getPath() + "\\" + MainWindow.getWords().get(wordIndex));
            Image scaledImage = originalIcon.getImage().getScaledInstance(700, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            this.questionLabel.setIcon(scaledIcon);
            this.questionLabel.setText("");
            this.questionLabel.revalidate();
            this.questionLabel.repaint();
            // If the current hint is not an image the previous image gets deleted and the text hint is shown.
        } else {
            this.questionLabel.setIcon(null);
            this.questionLabel.setText(getHint(wordIndex));
            this.questionLabel.revalidate();
            this.questionLabel.repaint();
        }
        // Refreshes the QuestionWindow.
        revalidate();
        repaint();
        System.out.println("score is reset");
        System.out.println("MainWindow.getWords().get(wordIndex)" + MainWindow.getWords().get(wordIndex));
    }

    /**
     * Is responsible for handling the answer in the text field. If the answer in the
     * text field corresponds to the correct word the score is increased by 100 points
     * and the next word is called. If the answer was wrong, the score is subtracted by 50
     * points, the text field is cleared and the user has to enter the answer again.
     */
    private void handleInput() {
        String fieldValue = this.answerTextField.getText();
        System.out.println(getAnswer(wordIndex));

        // If the answer was the same as the searched word, score is increased by 100 and the score label is updated.
        if (fieldValue.equals(getAnswer(wordIndex))) {
            score += 100;
            this.answerTextField.setText(null);
            this.scoreLabel.setText("score: " + String.valueOf(score));
            this.topContainer.revalidate();
            this.topContainer.repaint();
            wordIndex++;

            /*
             * When the wordIndex has reached the same size as the number of words
             * in the words list, the score variable of the ResultWindow is set to the value of the score that has been reached.
             * Afterwards the programm switches to the ResultWindow
             */
            if (wordIndex == MainWindow.getWords().size()) {
                wordIndex = 0;
                ResultWindow.setScore(score);
                ResultWindow.getInstance().setVisible(true);
                QuestionWindow.getInstance().setVisible(false);
            }

            /*
             * Checks if the current hint is an image file. If this is so
             * a new image object is created and set to a maximum size of 500x700px.
             * Afterwards this is assigned to the questionLabel and the
             * questionLabel is updated.
             */
            if (isImageFile(MainWindow.getWords().get(wordIndex))) {
                ImageIcon originalIcon = new ImageIcon(
                        MainWindow.getPath() + "\\" + MainWindow.getWords().get(wordIndex));
                Image scaledImage = originalIcon.getImage().getScaledInstance(700, 500, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                this.questionLabel.setIcon(scaledIcon);
                this.questionLabel.setText("");
                this.questionLabel.revalidate();
                this.questionLabel.repaint();
            }

            /*
             * If the current hint is not an image file, the last image is removed from the
             * questionLabel and the hint is set as text. Then the questionLabel is updated.
             */
            else {
                this.questionLabel.setIcon(null);
                this.questionLabel.setText(getHint(wordIndex));
                this.questionLabel.revalidate();
                this.questionLabel.repaint();
            }

            /*
             * If the answer in the text field was wrong, 50 points are subtracted from the score, the answer
             * text field is cleared and the topContainer is updated.
             */
        } else if (fieldValue != getAnswer(wordIndex)) {
            score -= 50;
            this.answerTextField.setText(null);
            this.scoreLabel.setText("score: " + String.valueOf(score));
            this.topContainer.revalidate();
            this.topContainer.repaint();
        }
    }

    /**
     * Ensures that if the backButton is clicked the programm goes back to the MainWindow.
     * Also sets the variables wordIndex and score back to 0.
     * Also updates the scoreLabel, the topContainer and the questionLabel.
     */
    private void handleBackButton() {
        System.out.println("switch main Window");
        wordIndex = 0;
        score = 0;
        this.scoreLabel.setText("score: " + String.valueOf(score));
        this.topContainer.revalidate();
        this.topContainer.repaint();
        MainWindow.getInstance().setVisible(true);
        QuestionWindow.getInstance().setVisible(false);
    }
}
