import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.*;

public class Player 
{
  //Set up the two possible borders a player can have
  public Border blackline = BorderFactory.createLineBorder(Color.black);
  public Border redline = BorderFactory.createLineBorder(Color.red);
  
  String name;                              //Name of the player
  int money;                                //Amount of money the player has
  JPanel PlayPanel;                         //The panel of the player
  JLabel PlayLabel;                         //The label holding the money
  TitledBorder BlackBorder;                 //The black and red borders a
  TitledBorder RedBorder;                   //player can have.
  
  //Set up the name to the given input string, money defaults to 0, set up the
  //black and red border, and panel for the player.
  Player(String str)
  {
    name = str;
    money = 0;
    BlackBorder = BorderFactory.createTitledBorder(blackline, name);
    RedBorder = BorderFactory.createTitledBorder(redline, name);
    PlayPanel = makePanel();
  }
  
  //Makes the panel given the label and sets the border up to black by default.
  public JPanel makePanel()
  {
    PlayLabel = new JLabel(String.valueOf(this.money));
    JPanel playerpan = new JPanel(new FlowLayout());
    playerpan.add(PlayLabel);
    playerpan.setBorder(BlackBorder);
    
    return playerpan;
  }
}
