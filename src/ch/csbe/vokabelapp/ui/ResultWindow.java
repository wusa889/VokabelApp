package ch.csbe.vokabelapp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Julian
 * This is a singleton class.
 */
public class ResultWindow extends JFrame implements ActionListener {


    private static final long serialVersionUID = -7253575377715791428L;

    public static ResultWindow instance;

    /**
     * Singleton getter for this class
     *
     * @return The instance of the class.
     */
    public static ResultWindow getInstance() {
        if (instance == null) {
            instance = new ResultWindow();
        }

        return instance;
    }

    static int score = QuestionWindow.getScore();
    private static JLabel resultLabel = new JLabel();

    private JButton backToMainButton;

    private ResultWindow() {

        // Initialization of the window
        this.setTitle("Vokabel App ResultWindow");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialization of the resultLabel
        resultLabel.setText("<html> Maximum Score: " + String.valueOf(MainWindow.getWords().size() * 100) +" <br> Your score: " + String.valueOf(score) + "<br>" + result(score) + "</html>");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(resultLabel, BorderLayout.CENTER);

        // Initialization of the backToMainMenu button
        this.backToMainButton = new JButton("Back to Main Menu");
        this.backToMainButton.addActionListener(this);
        this.add(this.backToMainButton, BorderLayout.PAGE_END);

    }

    /**
     * Is responsible to get back to the MainWindow when backToMainButton is pressed.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == backToMainButton) {
            ResultWindow.getInstance().setVisible(false);
            MainWindow.getInstance().setVisible(true);
        }

    }


    /**
     * Provides the text for the window which will be displayed when it is called.
     * @param score is used to enable the calculation for the text
     * @return Text that is shown in the window.
     */
    private static String result(int score) {
        String resultText = "";
        // Takes the size of the Words list * 100 to calculate the maximum score.
        int maximumScore = MainWindow.getWords().size() * 100;
        // Calculates the percentage achieved using the maximum score.
        double percentScore = (double) score / maximumScore * 100.0;
        // 99.5% was chosen to exclude any rounding errors.
        if (percentScore >= 99.5) {
            resultText = "Perfect!";
        } else if (percentScore >= 75.0) {
            resultText = "Good job! If you practice a bit more it will be perfect!";
        } else if (percentScore >= 49.0) {
            resultText = "Well done but you should practice a bit more.";
        } else {
            resultText = "You have to practice more.";
        }
        System.out.println("percentScore " + percentScore);
        return resultText;
    }

    /**
     * Used to adjust the score variable and set the text of the window to the latest state.
     * @param value is used to set the new value for score.
     */
    public static void setScore(int value) {
        score = value;
        System.out.println("score is set to " + value);
        resultLabel.setText(
                "<html> Maximum Score: " + String.valueOf(MainWindow.getWords().size() * 100) +" <br> Your score: " + String.valueOf(score) + "<br>" + result(score) + "</html>");
        resultLabel.revalidate();
        resultLabel.repaint();
    }

}
