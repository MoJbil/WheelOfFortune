import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;

public class DialogWindow extends JDialog
{
  //Declare the member variables, including the text field, button, and a bool
  //indicating if the puzzle is solved
  JTextField nameField;
  JButton okButton;
  boolean solved = false;
  
  public DialogWindow(JFrame mainFrame, String title, String message)
  {
    //Call the default constructor
    super(mainFrame, title, true);
    
    //Set up the JPanels and the Layout
    JPanel MessagePan = new JPanel(new BorderLayout());
    JPanel TextBoxPan = new JPanel(new BorderLayout());
    JPanel ButtonPan = new JPanel(new FlowLayout());
    setLayout(new BorderLayout());
    
    //Set the namefield and button, as well as an inside action listener.
    nameField = new JTextField(40);
    okButton = new JButton(" OK ");
    okButton.addActionListener(new ActionListener()
                               {
                                 //checks the conditions for when the window
                                 //should go away
                                 public void actionPerformed(ActionEvent e)
                                 {
                                   if (e.getSource() == okButton && 
                                       !nameField.getText().isEmpty())
                                     setVisible(false);
                                   else
                                     setVisible(true);
                                 }
                               });
    
    //Add to the three pans.
    MessagePan.add(new JLabel(message), BorderLayout.NORTH);
    TextBoxPan.add(nameField);
    ButtonPan.add(okButton);
    
    //Add the three above pans to the main layout.
    add(MessagePan, BorderLayout.NORTH);
    add(TextBoxPan, BorderLayout.CENTER);
    add(ButtonPan, BorderLayout.SOUTH);
  
    //Pack, make sure to do nothing on close, and set it visible.
    pack();
    setDefaultCloseOperation(mainFrame.DO_NOTHING_ON_CLOSE);
    setVisible(true);
  }
  
  //This constructor does the same as above except is used with the 
  //solve the puzzle button.
  public DialogWindow(String title, String message, String pu)
  { 
    final String p = pu;
    
    this.setModal(true);
    this.setTitle(title);
    
    JPanel MessagePan = new JPanel(new BorderLayout());
    JPanel TextBoxPan = new JPanel(new BorderLayout());
    JPanel ButtonPan = new JPanel(new FlowLayout());
    setLayout(new BorderLayout());
    
    nameField = new JTextField(40);
    okButton = new JButton(" OK ");
    okButton.addActionListener(new ActionListener()
                               {
                                 public void actionPerformed(ActionEvent e)
                                 {
                                   String field = nameField.getText().toLowerCase();
                                   String puz = p.toLowerCase();
                                   
                                   if (field.equals(puz) &&
                                       e.getSource() == okButton)
                                   {
                                     solved = true;
                                   }
                                
                                   if (e.getSource() == okButton && 
                                       !nameField.getText().isEmpty())
                                     setVisible(false);
                                   else
                                     setVisible(true);
                                 }
                               });
    
    MessagePan.add(new JLabel(message), BorderLayout.NORTH);
    TextBoxPan.add(nameField);
    ButtonPan.add(okButton);
    
    add(MessagePan, BorderLayout.NORTH);
    add(TextBoxPan, BorderLayout.CENTER);
    add(ButtonPan, BorderLayout.SOUTH);
  
    setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
    
    pack();
    setVisible(true);
  }
  
  public String getText()
  {
    return (nameField.getText());
  }
}
