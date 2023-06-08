package RoadSigns;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Class        RoadSignsGUI.java
 * Description  A class representing the GUI used in a road sign quiz 
 *              application.
 * Project      RoadSignsGUI Quiz
 * Platform     PC, Windows 11, jdk 19.0.2, NetBeans 17
 * Date         6/7/2023
 * @author	<i>Artem Grichanichenko</i>
 * @version 	1.0.0
 * @see     	javax.swing.JFrame
 * @see         java.awt.Toolkit 
 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
public class RoadSignsGUI extends javax.swing.JFrame 
{
    // File paths for the students and signs text files
    private String studentsFileName = "Src/RoadSigns/Students.txt";
    private String signsFileName = "Src/RoadSigns/Signs.txt"; 
    
    // List to store student names
    private ArrayList<RoadSign> signsList = new ArrayList<RoadSign>(); 
    
    // List to store signs
    private ArrayList<String> studentsList = new ArrayList<String>(); 
    
    // Array to keep track of signs used
    private boolean[] signsUsed;   
    
    // Array to store numbers
    private int[] numbers;
    
    // Index of the current sign
    private int currentIndex;  
    
    // Counter for correct answers    
    private int countCorrect = 0;  
    
    // Total number of signs
    private int numberOfSigns = 0;  
    
    // Total number of students
    private int numberOfStudents = 0; 
    
    // Total number of questions
    private int numberOfQuestions = 0;  
    
    // Counter for questions
    private int count = 0, maxQuestions = 10; 
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Constructor  RoadSignsGUI()-default constructor
     * Description  Create an instance of the GUI form, set the default
     *              JButton to be submitJButton, set icon image, center form,
     *              read signs and students from external file.
     * Date         6/7/2023
     * @author      <i>Artem Grichanichenko</i>
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public RoadSignsGUI()
    {
        // Initialize the components of the GUI
        initComponents();
        
        // Set the default button for the root pane
        this.getRootPane().setDefaultButton(submitJButton)
                ;
        // Set the image icon for the JFrame
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/Images/smallSigns.png"));
        
        // Center the form on the screen
        setLocationRelativeTo(null);
        
        // Read student information from the external file
        readStudents(studentsFileName);
        
        // Read signs information from the external file
        readSignsFromFile(signsFileName);
        
        // Fill the combo box with the available signs
        fillComboBox(signsList);
        
        // Display students on the GUI
        displayStudents();
        
        // Disable the "Next" button
        nextJButton.setEnabled(false);
        
        // Disable the "Play" button
        playJButton.setEnabled(false);
        
        // Disable the "Submit" button
        submitJButton.setEnabled(false);
        
        // Disable "Print" button
        printJMenuItem.setEnabled(false);
        
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        readStudents
    * Description   The code defines a method called readStudents that 
    * takes a fileName parameter. It clears a list of students and removes them 
    * from a JList, then attempts to read student information from a file using 
    * a Scanner. If the file is not found, it displays a warning message and
    * prompts the user to choose a file using a JFileChooser.
    * If the user selects a file, it updates the fileName variable and
    * recursively calls the readSignsFromFile method. If the user cancels or an
    * exception occurs, it displays a warning message indicating that the file
    * could not be read.
    * Date          5/7/2023
    * @param       fileName String
    * @see         java.io.File
    * @see         java.util.Scanner
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void readStudents(String fileName)
    {
        studentJList.removeAll();
        studentsList.clear();
        try
        {
            Scanner input = new Scanner(new File(fileName));
            while(input.hasNextLine())
            {
                studentsList.add(input.nextLine());
            }
            input.close();
        }
        catch(FileNotFoundException exp)
        {
            // Show a warning message
            JOptionPane.showMessageDialog(null, studentsFileName + " does not exist", 
                    "File Input Error", JOptionPane.WARNING_MESSAGE);
            // Create a file chooser
            JFileChooser chooser = new JFileChooser("src/RoadSigns");
            // Set the file filter to only show .txt files
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Txt Files", "txt");
            // Apply the file filter to the file chooser
            chooser.setFileFilter(filter);
            // Show the file chooser and get the user's choice
            int choice = chooser.showOpenDialog(null);
            // If the user chooses a file
            if (choice == JFileChooser.APPROVE_OPTION)
            {
                // Get the selected file
                File chosenFile = chooser.getSelectedFile();
                // Update the fileName variable with the chosen file's name
                fileName = "src/RoadSigns/" + chosenFile.getName();
                // Recursively call the readSignsFromFile method
                readSignsFromFile(fileName);
            }
            // If the user cancels
            else
            {
                // Exit the program
                System.exit(0);
            }
        }
        // If any other exception occurs
        catch(Exception exp)
        {
            // Show a warning message
            JOptionPane.showMessageDialog(null, "Unable to read file",
                    "File Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        displayStudents
    * Description   defines a method called displayStudents that updates the
    * GUI's student list with the names of students stored in the students
    * ArrayList. It selects the previously selected student if available or
    * defaults to the first student in the list.
    * could not be read.
    * Date          5/7/2023
    * @author       <i>Artem Grichanichenko</i>
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void displayStudents()
    {
        int location = studentJList.getSelectedIndex();
        // Create a local array of String (names of students)
        String[] studentList = new String[studentsList.size()];
        
        for (int k = 0; k < studentsList.size(); k++)
        {
            studentList[k] = studentsList.get(k);
        }
            studentJList.setListData(studentList);
        
        if (location < 0)
            studentJList.setSelectedIndex(0);
        
        else
            studentJList.setSelectedIndex(location);
}

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       readSignsFromFile
     * Description  Reads signs from a file Signs.txt and fill signJComboBox.  
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       signsFileName String
     * @see         java.io.File
     * @see         java.util.Scanner
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public void readSignsFromFile(String signsFileName)
    {
        numberOfSigns = 0; signJComboBox.removeAllItems(); signsList.clear();
        try
        {
            File file = new File(signsFileName);
            Scanner fileScanner = new Scanner(file);
            //read from file and count how mant here are to size the array
            while(fileScanner.hasNextLine())
            {
                signsList.add(new RoadSign(fileScanner.nextLine()));
                numberOfSigns++;
            }
            maxQuestions = numberOfSigns;
            questionsJTextField.setToolTipText
        ("Enter an integer in the range [1, " + maxQuestions + "].");
            fileScanner.close();
            // create parallel arrays
            
            signsUsed = new boolean[numberOfSigns];
            numbers = new int[numberOfSigns];
            for(int i = 0 ; i < numberOfSigns; i++)
            {
                signsUsed[i]= false;
                numbers[i] = i; 
            }
        }
        catch(FileNotFoundException exp)
        {
            // Show a warning message if the file is not found
            JOptionPane.showMessageDialog(null, signsFileName + " does not exist", 
                    "File Input Error", JOptionPane.WARNING_MESSAGE);

            // Create a file chooser
            JFileChooser chooser = new JFileChooser("src/Images");

            // Set the file filter to only show .txt files
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Txt Files", "txt");
            chooser.setFileFilter(filter);

            // Show the file chooser and get the user's choice
            int choice = chooser.showOpenDialog(null);

            // If the user chooses a file
            if (choice == JFileChooser.APPROVE_OPTION)
            {
                // Get the selected file
                File chosenFile = chooser.getSelectedFile();
                // Update the fileName variable with the chosen file's name
                signsFileName = "src/Images/" + chosenFile.getName();

                // Recursively call the readSignsFromFile method with the new fileName
                readSignsFromFile(signsFileName);
            }
            // If the user cancels
            else
            {
                // Exit the program
                System.exit(0);
            }
        }
        catch(Exception exp)
        {
            // Show a warning message if an exception occurs while reading the file
            JOptionPane.showMessageDialog(null, "Unable to read file",
                    "File Input Error", JOptionPane.WARNING_MESSAGE);

        }
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       fillComboBox
     * Description  Fill signsJComboBox with sign descriptions
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param        String[]
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @SuppressWarnings("unchecked")
    private void fillComboBox(ArrayList<RoadSign> road)
    {
        signJComboBox.removeAllItems();
       for (RoadSign sign : road)
       {
           signJComboBox.addItem(sign.name);
       }
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       displayArtist
     * Description  Choose a random and unused sign and display it in the 
     *              signJLabel.
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void displaySigns()
    {
       // Generate a unique random number
       currentIndex = getUniqueRandomNumber();
       // create the path for that sign
       String roadSign = signJComboBox.getItemAt(currentIndex);
       // String country = signs[currenIndex];
       String countryPath = "src/Images/" + roadSign + ".png";
       // Set the icon of the signJLabel to display the road sign image
       signJLabel.setIcon(new ImageIcon(countryPath));
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       getUniqueRandomNumber
     * Description  Return an unused random number by a blind repetition of
     *              random generation and checking for unused sign
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @return      random int
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private int getUniqueRandomNumber()
    {
        // Create a new Random object
        Random generator = new Random();
        // Initialize the randomNumber variable to 0
        int randomNumber = 0;
        // Generate a random number until an unused sign is found
        do
        {
            // Generate a random number within the range of the signs array length
            randomNumber = generator.nextInt(signsList.size());
        }
        while(signsUsed[randomNumber]);
        // Mark the randomly chosen sign as used
        signsUsed[randomNumber] = true;
        // Return the randomly chosen and unused sign number
        return randomNumber;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleJLabel = new javax.swing.JLabel();
        signJLabel = new javax.swing.JLabel();
        questionsJLabel = new javax.swing.JLabel();
        questionsJTextField = new javax.swing.JTextField();
        selectJPanel = new javax.swing.JPanel();
        descriptionJLabel = new javax.swing.JLabel();
        signJComboBox = new javax.swing.JComboBox<>();
        controlJPanel = new javax.swing.JPanel();
        submitJButton = new javax.swing.JButton();
        nextJButton = new javax.swing.JButton();
        playJButton = new javax.swing.JButton();
        resultJTextField = new javax.swing.JTextField();
        studentJScrollPane = new javax.swing.JScrollPane();
        studentJList = new javax.swing.JList<>();
        quizJMenuBar = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        newJMenuItem = new javax.swing.JMenuItem();
        printJMenuItem = new javax.swing.JMenuItem();
        printGUIJMenuItem = new javax.swing.JMenuItem();
        fielJSeparator = new javax.swing.JPopupMenu.Separator();
        exitJMenuItem = new javax.swing.JMenuItem();
        helpJMenu = new javax.swing.JMenu();
        aboutJMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Road Signs Quiz");
        setResizable(false);

        titleJLabel.setFont(new java.awt.Font("High Tower Text", 2, 40)); // NOI18N
        titleJLabel.setForeground(new java.awt.Color(255, 0, 0));
        titleJLabel.setText("Road Sign Quiz");

        signJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Bicycle Traffic Warning.png"))); // NOI18N

        questionsJLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        questionsJLabel.setText("Questions:");

        questionsJTextField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        questionsJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        questionsJTextField.setToolTipText("Enter an integer in the range [1,16]");
        questionsJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                questionsJTextFieldActionPerformed(evt);
            }
        });
        questionsJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                questionsJTextFieldKeyTyped(evt);
            }
        });

        selectJPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        descriptionJLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        descriptionJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionJLabel.setText("Select Sign Description:");
        descriptionJLabel.setToolTipText("Select Sign Description to match road sign");
        selectJPanel.add(descriptionJLabel);

        signJComboBox.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        signJComboBox.setToolTipText("Select sign description to match road sign");
        selectJPanel.add(signJComboBox);

        controlJPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 3));

        submitJButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        submitJButton.setMnemonic('S');
        submitJButton.setText("Submit");
        submitJButton.setToolTipText("Click to see if you were correct");
        submitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(submitJButton);

        nextJButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nextJButton.setMnemonic('N');
        nextJButton.setText("Next Sign");
        nextJButton.setToolTipText("Click to see next sign");
        nextJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(nextJButton);

        playJButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        playJButton.setMnemonic('P');
        playJButton.setText("Play Again");
        playJButton.setToolTipText("Click to play agian");
        playJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(playJButton);

        resultJTextField.setEditable(false);
        resultJTextField.setFont(new java.awt.Font("High Tower Text", 2, 30)); // NOI18N
        resultJTextField.setForeground(new java.awt.Color(255, 0, 0));
        resultJTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        studentJList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Students", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("High Tower Text", 2, 18), new java.awt.Color(255, 0, 0))); // NOI18N
        studentJList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        studentJScrollPane.setViewportView(studentJList);

        fileJMenu.setText("File");
        fileJMenu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newJMenuItem.setMnemonic('n');
        newJMenuItem.setText("New");
        newJMenuItem.setToolTipText("Open a new set of road signs");
        newJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(newJMenuItem);

        printJMenuItem.setMnemonic('P');
        printJMenuItem.setText("Print");
        printJMenuItem.setToolTipText("Print result");
        printJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(printJMenuItem);

        printGUIJMenuItem.setMnemonic('r');
        printGUIJMenuItem.setText("Print Form");
        printGUIJMenuItem.setToolTipText("Print GUI");
        printGUIJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printGUIJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(printGUIJMenuItem);
        fileJMenu.add(fielJSeparator);

        exitJMenuItem.setMnemonic('x');
        exitJMenuItem.setText("Exit");
        exitJMenuItem.setToolTipText("Exit Form");
        exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(exitJMenuItem);

        quizJMenuBar.add(fileJMenu);

        helpJMenu.setText("Help");
        helpJMenu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        aboutJMenuItem.setMnemonic('A');
        aboutJMenuItem.setText("About");
        aboutJMenuItem.setToolTipText("Show About Form");
        aboutJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(aboutJMenuItem);

        quizJMenuBar.add(helpJMenu);

        setJMenuBar(quizJMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(titleJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(signJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(questionsJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(questionsJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(controlJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                            .addComponent(resultJTextField)
                            .addComponent(studentJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleJLabel)
                    .addComponent(questionsJLabel)
                    .addComponent(questionsJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(selectJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resultJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentJScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(controlJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(signJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       questionsJTextFieldKeyTyped
     * Description  Accept only digits, backspace and delete keys.
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt java.awt.event.KeyEvent
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void questionsJTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_questionsJTextFieldKeyTyped
        // Retrieve the key character from the event
        char c = evt.getKeyChar();
        // Check if the key character is not a digit, backspace, or delete
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || 
                (c == KeyEvent.VK_DELETE))) )
        {
            // Produce a beep sound using the toolkit
            getToolkit().beep();
            // Consume the event, preventing further processing
            evt.consume();
        }
    }//GEN-LAST:event_questionsJTextFieldKeyTyped
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       submitJButtonActionPerformed
     * Description  Event handler to check if the user's answer is correct. The
     *              correct answer is held in class instance variable 
     *              currentIndex.
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt ActionEvent
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void submitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitJButtonActionPerformed
        count++;
        if (signJComboBox.getSelectedIndex() == currentIndex)
        {
            countCorrect++;
            resultJTextField.setText("Correct! " + countCorrect + "/" + count);
        }
        else
        {
            resultJTextField.setText("Incorrect! " + countCorrect + "/" + count);
        }
        if (count == numberOfQuestions)
        {
            resultJTextField.setText(countCorrect + "/" + numberOfQuestions + " Correct!");
            nextJButton.setEnabled(false);
            submitJButton.setEnabled(false);
            playJButton.setEnabled(true);
            signJComboBox.setEnabled(false);
            printJMenuItem.setEnabled(true);
        }
        else
        {
            submitJButton.setEnabled(false);
            nextJButton.setEnabled(true);
            playJButton.setEnabled(false);
            printJMenuItem.setEnabled(false);
        }
    }//GEN-LAST:event_submitJButtonActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       nextJButtonActionPerformed
     * Description  Event handler to select next unused sign randomly by 
     *              calling the displayArtist() method.
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt ActionEvent
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void nextJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextJButtonActionPerformed
       // display next sign
       displaySigns();
       //reset GUI components to initial states
       resultJTextField.setText("");
       signJComboBox.setSelectedIndex(0);
       submitJButton.setEnabled(true);
       nextJButton.setEnabled(false);
    }//GEN-LAST:event_nextJButtonActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       playJButtonActionPerformed
     * Description  Event handler to start the game all over again.
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt ActionEvent
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void playJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playJButtonActionPerformed
        // Start game all over
        countCorrect = 0;
        count = 0;
        resultJTextField.setText("");
        numberOfSigns = signsList.size();
        submitJButton.setEnabled(false);
        nextJButton.setEnabled(false);
        playJButton.setEnabled(false);
        signJComboBox.setEnabled(true);
        newJMenuItem.setEnabled(true);
        questionsJTextField.setEnabled(true);
        questionsJTextField.setText("");
        questionsJTextField.requestFocus();
        printJMenuItem.setEnabled(false);
        studentJList.setEnabled(true);
        for(int i = 0; i < signsList.size(); i++)
        {
            signsUsed[i] = false;
            numbers[i] = 0;
        }
    }//GEN-LAST:event_playJButtonActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       aboutJMenuItemActionPerformed()
     * Description  Create an About form and show it. 
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt java.awt.event.ActionEvent
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void aboutJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutJMenuItemActionPerformed
        // show about form
        About aboutWindow = new About(this, true);
        aboutWindow.setVisible(true);
    }//GEN-LAST:event_aboutJMenuItemActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       exitJMenuItemActionPerformed()
     * Description  Close application. 
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt java.awt.event.ActionEvent
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void exitJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJMenuItemActionPerformed
        // exit application
        System.exit(0);
    }//GEN-LAST:event_exitJMenuItemActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       newJMenuItemActionPerformed
     * Description  Event handler to reset the form and start the game all
     *              over again. Calls the playJButtonActionPerformed
     * Date         6/7/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       evt ActionEvent
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void newJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newJMenuItemActionPerformed
        JFileChooser chooser = new JFileChooser("src/RoadSigns");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Txt Files", "txt");
        chooser.setFileFilter(filter);       int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            File chosenFile = chooser.getSelectedFile();
            signsFileName = "src/RoadSigns/" + chosenFile.getName();
            readSignsFromFile(signsFileName);
        }
    }//GEN-LAST:event_newJMenuItemActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       printJMenuItemActionPerformed()
     * Description  Print displayJTextArea.
     * @param       evt java.awt.event.ActionEvent
     * @author      <i>Artem Grichanichenko</i>
     * Date         6/7/2023
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void printGUIJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printGUIJMenuItemActionPerformed
        // print GUI
        PrintUtilities.printComponent(this);
    }//GEN-LAST:event_printGUIJMenuItemActionPerformed
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       questionsJTextFieldActionPerformed()
     * Description  The code tries to parse an integer value from a text field, 
     * checks if it is within a specific range, and performs actions based on the
     * result, including enabling or disabling certain components and displaying
     * error messages if necessary.
     * @param       evt java.awt.event.ActionEvent
     * @author      <i>Artem Grichanichenko</i>
     * Date         6/7/2023
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void questionsJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_questionsJTextFieldActionPerformed
        try
        {
            if(!Validation.isInteger(questionsJTextField.getText(),
                    1, maxQuestions))
            throw new NumberFormatException();
        // Set the numberOfQuestions variable to the parsed value
        numberOfQuestions = Integer.parseInt(questionsJTextField.getText());
        
        // Disable the questionsJTextField and enable the signJComboBox and submitJButton
        questionsJTextField.setEnabled(false);
        signJComboBox.setEnabled(true);
        studentJList.setEnabled(false);

        // Call the displaySigns() function to display the signs
        displaySigns();
        
        // Enable the submitJButton and disable the newJMenuItem
        submitJButton.setEnabled(true);
        newJMenuItem.setEnabled(false);
        printJMenuItem.setEnabled(false);
   
        }
        catch (NumberFormatException exp)
        {
            // Display a warning message dialog indicating the input error
        JOptionPane.showMessageDialog(null,
            "Input must be an integer in the range [1, " + maxQuestions + "].",
            "Input Error", JOptionPane.WARNING_MESSAGE);
        
        // Set the focus on the questionsJTextField and select its entire text
        questionsJTextField.requestFocus();
        questionsJTextField.selectAll();
        }
    }//GEN-LAST:event_questionsJTextFieldActionPerformed

    private void printJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printJMenuItemActionPerformed
        try
        {
        resultJTextField.print();
        }
        catch (PrinterException ex)
        {
            JOptionPane.showMessageDialog(null, "Cannot print",
                    "Print Error", JOptionPane.WARNING_MESSAGE);
        }
//        JTextArea studentInfo = new JTextArea();
//        String studentsName = studentJList.getSelectedValue();
//        Student student = searchStudent(studentsName);
//        StringBuffer output = new StringBuffer(student.getName());
//        output.append("\n" + student.getName() + "\n" + student.getQuestions() +
//                "\n" + student.getCorrect());
//        try
//        {
//        studentInfo.print();        
//        }
//        catch (PrinterException ex)
//        {
//        JOptionPane.showMessageDialog(null, "Cannot print",
//                "Print Error", JOptionPane.WARNING_MESSAGE);
//        }
    }//GEN-LAST:event_printJMenuItemActionPerformed
//    public Student searchStudent(String studentsName)
//    {
//        for(int i = 0; i < studentsList.size(); i++)
//        {
//            if(!studentsList.get(i).getName().equalsIgnoreCase(studentsName)) {
//            }
//            else 
//            {
//                return studentsList.get(i);
//            }
//        }
//        return null;
//    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       main()
     * Description  Displays splash screen and the main RoadSign GUI form.
     * Date         5/22/2023 
     * @author      <i>Artem Grichanichenko</i>
     * @param       args are the command line strings
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void main(String args[])
    {
        // Show splash screen
//        Splash mySplash = new Splash(4000);     // duration = 4 seconds
//        mySplash.showSplash();                  // show splash screen
                
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() {
                new RoadSignsGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JPanel controlJPanel;
    private javax.swing.JLabel descriptionJLabel;
    private javax.swing.JMenuItem exitJMenuItem;
    private javax.swing.JPopupMenu.Separator fielJSeparator;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JMenuItem newJMenuItem;
    private javax.swing.JButton nextJButton;
    private javax.swing.JButton playJButton;
    private javax.swing.JMenuItem printGUIJMenuItem;
    private javax.swing.JMenuItem printJMenuItem;
    private javax.swing.JLabel questionsJLabel;
    private javax.swing.JTextField questionsJTextField;
    private javax.swing.JMenuBar quizJMenuBar;
    private javax.swing.JTextField resultJTextField;
    private javax.swing.JPanel selectJPanel;
    private javax.swing.JComboBox<String> signJComboBox;
    private javax.swing.JLabel signJLabel;
    private javax.swing.JList<String> studentJList;
    private javax.swing.JScrollPane studentJScrollPane;
    private javax.swing.JButton submitJButton;
    private javax.swing.JLabel titleJLabel;
    // End of variables declaration//GEN-END:variables
}
