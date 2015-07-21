import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.io.FileFilter;
import java.io.File;
import java.net.URISyntaxException;

import java.util.Random;

import static java.lang.System.out;
import java.util.*;

public class MainFrame extends JFrame
{
  private final int VOWEL_PRICE = 250;                    //vowel price
  private final int NUM_WHEEL_SPACES = 24;                //num of spaces
  private final int MAX = 24;                             //random generator
  
  private JButton buy;                                    //Three buttons
  private JButton spin;
  private JButton solve;
  
  private JButton[] consonants = new JButton[21];         //Button arrays
  private JButton[] vowels = new JButton[5];
  private String[] cons ={"B", "C", "D", "F", "G", "H", "J", 
                          "K", "L", "M", "N", "P", "Q", "R", 
                          "S", "T", "V", "W", "X", "Y", "Z"};
  private String[] vows ={"A", "E", "I", "O", "U"};       //Array of letters
  
  private TitledBorder consonantBorder;           //Titled borders for
  private TitledBorder vowelBorder;               //consonants and vowels
  
  private JLabel label;                           //This will be the puzzle
  private JLabel imageIcon;                       //This is the wheel image
  private StephenListener actionListener;         //Make the actionlistener
  
  private int[] usedconsonants = new int[21];     //Holds indeces for the used
  private int[] usedvowels = new int[5];          //consonants and vowels
  
  private int usedCbound = -1;                    //This is the number of 
  private int usedVbound = -1;                    //used vowels and consonants
  private int instances = 0;                      //Keeps track of the amount
  private int current = 0;                        //of a certain letter in the
                                                  //puzzle.
  public Player[] playerarr;                      //Array of players
  public WheelSpace[] wheelSpaces;                //Array of wheelSpaces
  
  public String seed, player, puzzle, title, message, dashed;
  public String[] allPlayers;                     //Array of Playernames
  
  public int randomNumber;                        //Used for rand generator
  public Random rand;
                                          //Sets the difference colored borders
  public Border blackline = BorderFactory.createLineBorder(Color.black);
  public Border redline = BorderFactory.createLineBorder(Color.red);
  
  public File[] fileList;                         //File array
  
  //Checks if a given string is an integer or not, returns the boolean
  public boolean isInteger(String seedstring)
  {
    try 
    {
      Integer.parseInt(seedstring);
    }
    catch(NumberFormatException e)
    {
      return false;
    }
    return true;
  }
  
  //This splits a given string into the different players by an arbitrary
  //amount of whitespace.
  public String[] splitPlayers(String playerstring)
  { 
    if (playerstring.contains(" "))
      return playerstring.split("\\s+"); 
    else
    {
      String[] player = new String[1];
      player[0] = playerstring;
      return player;
    }
  }
  
  //This takes the array of player names and returns a JPanel corresponding to
  //these players
  public JPanel setUpPlayers(String[] allPlayers)
  {
    playerarr = new Player[allPlayers.length];
    JPanel playerlayout = new JPanel(new GridLayout(1,100));
    
    for (int i = 0; i < allPlayers.length; i++)
    {
      playerarr[i] = new Player(allPlayers[i]);
      playerarr[i].PlayLabel.setHorizontalTextPosition(SwingConstants.CENTER);
      playerlayout.add(playerarr[i].PlayPanel);
    }
    
    return playerlayout;
  }
  
  //This is the given code that sets up the wheel, checking the files.
  public void setUpWheel()
  {
    File myDir = null;
    int i;

    //Allocate array for number of spaces, which is set to a constant
    //for now as opposed to being able to change run-to-run
    wheelSpaces = new WheelSpace[NUM_WHEEL_SPACES];

    //Get a File object for the directory containing the images
    try
    {
      myDir = new File(getClass().getClassLoader().getResource("images").toURI());
    }
    catch (URISyntaxException uriExcep)
    {
      System.out.println("Caught a URI syntax exception");
      System.exit(4); //Just bail for simplicity in this project
    }

    //Loop from 1 to the number of spaces expected, so we can look
    //for files named <spaceNumber>_<value>.jpg.  Note: Space numbers
    //in image filenames are 1-based, NOT 0-based.
    for (i = 1; i <= NUM_WHEEL_SPACES; i++)
    {
      //Get a listing of files named appropriately for an image
      //for wheel space #i.  There should only be one, and this
      //will be checked below.
      fileList = myDir.listFiles(new WheelSpaceImageFilter(i));

      if (fileList.length == 1)
      {
        //System.out.println("Space: " + i + " img: " + fileList[0] +
        //        " val: " + WheelSpaceImageFilter.getSpaceValue(fileList[0]));
        //Index starts at 0, space numbers start at 1 -- hence the "- 1"
        wheelSpaces[i - 1] = new WheelSpace(
                              WheelSpaceImageFilter.getSpaceValue(fileList[0]),
                              new ImageIcon(fileList[0].toString()));
      }
      else
      {
        System.out.println("ERROR: Invalid number of images for space: " + i);
        System.out.println("       Expected 1, but found " + fileList.length);
      }
    }
  }
 
  public MainFrame(String inTitle)
  {
    super(inTitle);
    
    //Seed input----------------------------------------------------------------
    DialogWindow seedInput;
    title = "Seed Input";
    message = "Enter the random generator seed (int)";
    seedInput = new DialogWindow(this, title, message);
    
    while (!isInteger(seedInput.getText()))
      seedInput = new DialogWindow(this, title, message);
    //--------------------------------------------------------------------------
    
    //Player input--------------------------------------------------------------
    DialogWindow playerInput;
    title = "Player Name Input";
    message = "Enter space separated list of players";
    playerInput = new DialogWindow(this, title, message);
    //--------------------------------------------------------------------------
    
    //Puzzle input--------------------------------------------------------------
    DialogWindow puzzleInput;
    title = "Puzzle Input";
    message = "Ask a non-player to enter a puzzle";
    puzzleInput = new DialogWindow(this, title, message);
    //--------------------------------------------------------------------------
    
    this.seed = seedInput.getText();
    this.player = playerInput.getText();
    this.puzzle = puzzleInput.getText().trim().replaceAll("\\s+", " ");
    
    //Set up all the panels--------------------------------------------------
    
    JPanel All = new JPanel(new BorderLayout());
    
    JPanel Players = new JPanel(new FlowLayout());
    JPanel Wheel = new JPanel(new FlowLayout());
    JPanel Letters = new JPanel(new BorderLayout());
    
    JPanel Play = new JPanel(new GridLayout(3,1));
    JPanel Picture = new JPanel(new FlowLayout());
    
    JPanel VnC = new JPanel(new BorderLayout());
    JPanel Puzzle = new JPanel(new FlowLayout());
    
    JPanel Vowels = new JPanel(new GridLayout(3,2));
    JPanel Consonants = new JPanel(new GridLayout(3,5));
    
    setLayout(new FlowLayout());
    
    //-----------------------------------------------------------------------
   
    //Set up the players
    Players = setUpPlayers(splitPlayers(player));
    
    //Set up the wheel
    setUpWheel();
    
    //Start the random generator
    rand = new Random(Long.parseLong(this.seed));
    
    //Set the start imageIcon to the loseATurn wheel space.
    imageIcon = new JLabel(wheelSpaces[0].icon);
    Picture.add(imageIcon);
    
    //Declare the actionlistener
    actionListener = new StephenListener();
    
    //Set up the spin, vowel, and solve buttons------------------------------
    buy = new JButton("Buy a Vowel");
    spin = new JButton("Spin the Wheel");
    solve = new JButton("Solve the Puzzle");
    
    buy.addActionListener(actionListener);
    spin.addActionListener(actionListener);
    solve.addActionListener(actionListener);
    
    Play.add(buy);
    Play.add(spin);
    Play.add(solve);
    //-----------------------------------------------------------------------
    
    //Set the consonant and vowel buttons.-----------------------------------
    for(int i=0; i < 21; i++)
    {
      consonants[i] = new JButton(cons[i]); 
      Consonants.add(consonants[i]);
      consonants[i].addActionListener(actionListener); 
      consonants[i].setEnabled(false);
    }
    for(int i = 0; i < 5; i++)
    {
      vowels[i] = new JButton(vows[i]);
      Vowels.add(vowels[i]);
      vowels[i].addActionListener(actionListener);
      vowels[i].setEnabled(false);
    }
    //-----------------------------------------------------------------------
    
    //Create the titles for the consonant and vowel buttons------------------
    consonantBorder = BorderFactory.createTitledBorder("Consonants");
    consonantBorder.setTitleJustification(TitledBorder.LEFT);
    Consonants.setBorder(consonantBorder);
    
    vowelBorder = BorderFactory.createTitledBorder("Vowels");
    vowelBorder.setTitleJustification(TitledBorder.LEFT);
    Vowels.setBorder(vowelBorder);
    //-----------------------------------------------------------------------
    
    dashed = puzzle.replaceAll("[a-zA-Z]", "-");
    label = new JLabel(dashed.replace("", " ").trim());
    
    //Place the objects onto the screen--------------------------------------   
    Puzzle.add(label);
    
    VnC.add(Vowels, BorderLayout.WEST);
    VnC.add(Consonants, BorderLayout.EAST);
    
    Letters.add(VnC, BorderLayout.NORTH);
    Letters.add(Puzzle, BorderLayout.SOUTH);
    
    Wheel.add(Play);
    Wheel.add(Picture);
    
    All.add(Players, BorderLayout.NORTH);
    All.add(Wheel, BorderLayout.CENTER);
    All.add(Letters, BorderLayout.SOUTH);
    
    add(All);
    //-----------------------------------------------------------------------
    
    if (playerarr[current].money < 250)
      buy.setEnabled(false);
    
    playerarr[current].PlayPanel.setBorder(playerarr[current].RedBorder);
    
  }
  
  public class StephenListener implements ActionListener
  {
    boolean IsInString;
    boolean solved;
    
    public void actionPerformed(ActionEvent e)
    { 
      //Upon clicking the buy button, enable the vowels, disable the already
      //clicked vowels, and modify the price of the current player. Then
      //set everything else to disabled.
      if (e.getSource() == buy)
      {
        EnableDisable(vowels, true);
        DisableUsed(vowels, usedvowels, usedVbound);
        playerarr[current].money -= VOWEL_PRICE;
        buy.setEnabled(false);
        spin.setEnabled(false);
        solve.setEnabled(false);
      }
      //This will execute when the spin button is pressed
      else if (e.getSource() == spin)
      {
        //Change the image icon
        imageIcon.setIcon(wheelSpaces[nextNumber()].icon);
        
        //If the value is 25, it's the same as loseATurn, skip to the next
        //player.
        if (wheelSpaces[randomNumber].value == 25)
        {
          nextPlayer();
          if (usedVbound < 4 && playerarr[current].money >= 250)
            buy.setEnabled(true);
          else
            buy.setEnabled(false);
          
        }
        //If the value is 24, it corresponds to bankrupt, change the money
        //of the player to 0 and move on to the next player.
        else if (wheelSpaces[randomNumber].value == 24)
        {
          playerarr[current].money = 0;
          playerarr[current].PlayLabel.setText(String.valueOf(playerarr[current].money));
          nextPlayer();
          if (usedVbound < 4 && playerarr[current].money >= 250)
            buy.setEnabled(true);
          else
            buy.setEnabled(false);
          
        }
        //If the wheel space is neither of these, enable the consonants,
        //disable the used ones, and set the other buttons to disabled.
        else
        {
          EnableDisable(consonants, true);
          DisableUsed(consonants, usedconsonants, usedCbound);
          buy.setEnabled(false);
          spin.setEnabled(false);
          solve.setEnabled(false);
        }
      }
      //When solve is pressed, show the appropriate dialog window
      //and then the JOptionPane corresponding to whether or not the 
      //answer was correct.
      else if (e.getSource() == solve)
      {
        DialogWindow solvePuzzle;
        String title = "Solve Puzzle";
        String message = "Enter complete puzzle exactly as displayed";
        solvePuzzle = new DialogWindow(title, message, puzzle);
        solved = solvePuzzle.solved;
        if (solved)
        {
          JOptionPane.showMessageDialog(solvePuzzle, 
                                        playerarr[current].name + " wins $" +
                                        playerarr[current].money,
                                        "Game Over",
                                        JOptionPane.INFORMATION_MESSAGE);
          System.exit(1);
        }
        else
        {
          JOptionPane.showMessageDialog(solvePuzzle, 
                                        "Guess by " + playerarr[current].name + 
                                        " was incorrect",
                                        "Wrong Answer",
                                        JOptionPane.ERROR_MESSAGE);
          nextPlayer();
        }
      }
      //A letter or consonant was pressed, check if it was in the string,
      //update the puzzle at the bottom, and reset the rest of the buttons
      else
      {  
        for(int i = 0; i < 5; i++)
        {
          if (e.getSource() == vowels[i])
          {
            usedVbound++;
            usedvowels[usedVbound] = i; 
            EnableDisable(vowels, false);
            IsInString = checkIfInString(vows[i], puzzle);
            if (!IsInString) nextPlayer();
            instances = 0;
            break;
          }
        }
        for(int i = 0; i < 21; i++)
        {
          if (e.getSource() == consonants[i])
          {
            usedCbound++;
            usedconsonants[usedCbound] = i;
            EnableDisable(consonants, false);
            IsInString = checkIfInString(cons[i], puzzle);
            if (IsInString) 
            {
              playerarr[current].money += wheelSpaces[randomNumber].value*instances;
              instances = 0;
            }
            else
            {
              nextPlayer();
            }
            break;
          }
        }
        //Check if the player has enough money to buy a vowel, as well as if
        //there are still vowels or consonants to guess.
        if (usedVbound < 4 && playerarr[current].money >= VOWEL_PRICE)
          buy.setEnabled(true);
        if (usedCbound < 20)
          spin.setEnabled(true);
        
        solve.setEnabled(true);     
      }
      //Change players logic
      if (current != 0)
        playerarr[current-1].PlayPanel.setBorder(playerarr[current-1].BlackBorder);
      else
        playerarr[playerarr.length-1].PlayPanel.setBorder(playerarr[playerarr.length-1].BlackBorder);
      
      //Change borders and label logic
      playerarr[current].PlayPanel.setBorder(playerarr[current].RedBorder);
      label.setText(dashed.replace("", " ").trim());
      playerarr[current].PlayLabel.setText(String.valueOf(playerarr[current].money));
    }
    
    //Returns the next number in the random generator.
    public int nextNumber()
    {
      randomNumber = rand.nextInt(MAX);
      return randomNumber;
    }
    
    //Advances the game to the next player.
    public void nextPlayer()
    {
      current++;
      if (current == playerarr.length)
        current = 0;  
    }
    
    //Checks if the given string(letter) is in the phrase puzzle, returns
    //a boolean based on the input string.
    public boolean checkIfInString(String str, String phrase)
    {
      char charPressed = Character.toUpperCase(str.charAt(0));
      int index = phrase.toUpperCase().indexOf(charPressed);
      boolean contains = phrase.toUpperCase().contains(Character.toString(charPressed));
      
      if (contains)
      {
        instances++;
        
        char[] temp1 = dashed.toCharArray();
        char[] temp2 = phrase.toCharArray();
        temp1[index] = charPressed;
        temp2[index] = '-';
        dashed = String.valueOf(temp1);
        phrase = String.valueOf(temp2);
        
        checkIfInString(str, phrase);
      }
      
      return contains;
    }
  
    //Enables or disables the given button array based on the option the user
    //inputs: true for enabling, false for disabling
    public void EnableDisable(JButton[] buttons, boolean option)
    {
      
      if (buttons[0] == vowels[0])
      {
        for (int i = 0; i < 5; i++)
        {
          vowels[i].setEnabled(option);
        }
      }
      else
      {
        for (int i = 0; i < 21; i++)
        {
          consonants[i].setEnabled(option);
        }
      }
    }
  
    //Disables the used button array given the array of indeces and bound
    public void DisableUsed(JButton[] buttons, int[] intarray, int bound)
    {
      for(int i = 0; i <= bound; i++)
      {
        buttons[intarray[i]].setEnabled(false);
      }
    }
  }
}
