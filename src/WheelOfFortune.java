import javax.swing.JFrame;

public class WheelOfFortune 
{ 
  
  public static void main(String [] args)
  {
    //Make the main frame.
    MainFrame win;
    win = new MainFrame("Wheel of Fortune");
    
    //Pack the main frame, set it visible, exit on close.
    win.pack();
    win.setVisible(true);
    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}