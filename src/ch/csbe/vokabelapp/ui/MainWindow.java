package ch.csbe.vokabelapp.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Julian
 * This is a singleton class.
 *
 */
public class MainWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 9036218158327012609L;

    private static MainWindow instance;

    /**
     * Singleton getter for this class
     *
     * @return The instance of this class
     */
    public static MainWindow getInstance() { // getter for the instance, creates instance if none exists yet or
        // returns the instance if available
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    private JButton startButton;
    private JButton choosebutton;
    private JPanel gridPanel;
    private static String path;
    private JLabel chosenFilePath = new JLabel("chosen File path: " + path);
    private static ArrayList<String> words = new ArrayList<String>();
    private static ArrayList<String> wordsMinusExtension = new ArrayList<String>();
    private int shuffleCounter = 0;

    private MainWindow() {
        // Initialization of the window
        this.setTitle("Vokabel App MainMenu");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialization of the start button.
        this.startButton = new JButton("Start");
        this.startButton.addActionListener(this);
        this.add(this.startButton, BorderLayout.PAGE_END);

        // Shows the welcome message and main menu text.
        JLabel textLabel = new JLabel(
                "<html>Welcome to Vocable App <br>first press chose to select a filepath <br>Press start to start</html>");
        textLabel.setHorizontalAlignment(JLabel.CENTER);

        // Initialization of the choose button.
        this.choosebutton = new JButton("choose");
        this.choosebutton.addActionListener(this);

        // Initialization of the container for the Choose button and the textLabel.
        this.gridPanel = new JPanel(new GridLayout(0, 1));
        gridPanel.add(textLabel, BorderLayout.CENTER);
        gridPanel.add(choosebutton, BorderLayout.SOUTH);

        this.add(gridPanel, BorderLayout.CENTER);

    }

    /**
     * Is responsible for the handling when the start button is pressed. switches as
     * soon as a file path that only contains valid files is selected into the
     * QuestionWindow. Also detects if start is clicked again, to reshuffle the
     * words to ensure that the words are are always in different order. Also resets
     * the score of the QuestionWindow
     *
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == this.startButton) {

                // Is responsible to reshuffle the words if the set is played again.
                if (shuffleCounter >= 1) {
                    shuffleWords();
                }
                shuffleCounter++; // Increases the shuffleCounter every time the start button is pressed.
                boolean isValidFiles = checkValidFiles();
                if (!isValidFiles) {
                    // Shows a pop up that there are invalid files in the selected directory.
                    JOptionPane.showMessageDialog(this,
                            "Invalid Data inside choosen directory or empty directory. Please choose another directory.",
                            "Ivalid or empty directory found", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                QuestionWindow.getInstance().resetScore();
                QuestionWindow.getInstance().setVisible(true);
                MainWindow.getInstance().setVisible(false);
            }
            /*
             * If there is an exception a pop up is shown that says that there are invalid
             * files in the selected directory.
             */
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Data inside choosen directory or empty directory. Please choose another directory.",
                    "Ivalid or empty directory found", JOptionPane.WARNING_MESSAGE);
        }

        // Handling for the choose button.
        if (event.getSource() == this.choosebutton) {
            handleFileChooser();

        }
    }

    /**
     * Function to get the currently selected path.
     *
     * @return Currently selected path.
     */
    public static String getPath() {
        return path;
    }

    /**
     * Function to get the current getWords ArrayList.
     *
     * @return words ArrayList.
     */
    public static ArrayList<String> getWords() {
        return words;
    }

    /**
     * Function to get the current getWordsMinusExtension ArrayList.
     *
     * @return wordsMinusExtension ArrayList.
     */
    public static ArrayList<String> getWordsMinusExtension() {
        return wordsMinusExtension;
    }

    /**
     * Is responsible for shuffling the words, so that they always appear in a
     * randomized order.
     */
    private void shuffleWords() {
        words.clear();
        // Creates a file object to store the files from path into a file array.
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        for (File file : files) {
            words.add(file.getName());
        }
        /*
         * Shuffles the words in the words arraylist and then adds them to the
         * wordsMinusExtension ArrayList in the order of the shuffled words list.
         */
        Collections.shuffle(words);
        wordsMinusExtension.clear();
        for (int i = 0; i < words.size(); i++) {
            String tempWord = words.get(i);
            int dotIndex = tempWord.lastIndexOf(".");
            String addWord = tempWord.substring(0, dotIndex);
            wordsMinusExtension.add(addWord);
        }
        System.out.println("Words Shuffled");

    }

    /**
     * Is responsible for checking the files in the selected path.
     *
     * @return true if all file extensions are valid / false if there are files in
     *         the * folder that do not end in png, jpg or txt. folder that do not
     *         end in png, jpg or txt.
     */
    private boolean checkValidFiles() {
        // Creates a file object to store the files from path into a file array
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        /*
         * Goes through the array and checks all of the elements for their endings. If
         * an ending is not png, jpg or txt it stops and returns false.
         */
        for (File file : files) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            System.out.println("FileExtension: " + fileExtension);
            if (!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("txt")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Is responsible for handling the chooseButton. Creates a JFileChooser which is
     * in Directories_Only mode, then empties the words and wordsMinusExtension
     * ArrayLists and refills them afterwards.
     */
    private void handleFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        words.clear();
        wordsMinusExtension.clear();
        int option = fileChooser.showOpenDialog(this);

        /*
         * As soon as a path has been selected it will be stored in the path variable as
         * an absolutepath. It is then placed in the window with the chosenFilePath
         * variable and the window will be refreshed afterwards. The ArrayList words is
         * then filled with the filenames from the directory at the same time the
         * ArrayList wordsMinusExtension will be filled with the names of the file
         * without the extension
         */
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            this.chosenFilePath.setHorizontalAlignment(JLabel.CENTER);
            path = selectedDirectory.getAbsolutePath();
            chosenFilePath.setText("Selected Path: " + path);
            gridPanel.add(this.chosenFilePath, BorderLayout.NORTH);
            gridPanel.revalidate();
            gridPanel.repaint();
            // Creates a file object to add the files from path to the words list.
            File filePath = new File(path);
            File[] files = filePath.listFiles();
            for (File file : files) {
                words.add(file.getName());
            }
            System.out.println("original words Liste" + words);
            Collections.shuffle(words);
            System.out.println("shuffled words Liste" + words);
            for (int i = 0; i < words.size(); i++) {
                String tempWord = words.get(i);
                int dotIndex = tempWord.lastIndexOf(".");
                String addWord = tempWord.substring(0, dotIndex);
                wordsMinusExtension.add(addWord);

            }

        }
    }

}
