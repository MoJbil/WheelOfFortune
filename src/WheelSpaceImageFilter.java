import javax.swing.JFileChooser;
import java.io.FileFilter;
import java.io.File;

  //Helper inner class to filter images used for wheel spaces,
  //based on specifically expected filename format.
  public class WheelSpaceImageFilter implements FileFilter
  {
    final String IMAGE_EXTENSION = "jpg";
    final int NUM_WHEEL_SPACES = 24;
    
    String prefix;  //The prefix of the filename we're looking
                    //for - what comes before the first underscore

    WheelSpaceImageFilter(int inPref)
    {
      //Sets the prefix member to string version of space number
      prefix = new Integer(inPref).toString();
    }

    //Test whether the file provided should be accepted by our
    //file filter. In the FileFilter interface.
    public boolean accept(File fname)
    {
      boolean isAccepted = false;

      //Accepted if matched "<prefix>_<...>.jpg" where IMAGE_EXTENSION
      //is assumed to be "jpg" for this example
      if (fname.getName().startsWith(prefix + "_") &&
          fname.getName().endsWith("." + IMAGE_EXTENSION))
      {
        isAccepted = true;
      }

      return (isAccepted);
    }

    //Parses the provided filename to determine the dollar value
    //associated with this wheel space image's filename.
    public static int getSpaceValue(File fname)
    {
      boolean bool = true;
      int i;
      
      String filename = fname.getName();
      String[] parts1 = filename.split("_");
      String[] parts2 = parts1[1].split("\\.");
      
      try { Integer.parseInt(parts2[0]); }
      catch(NumberFormatException e) { bool = false; }
      
      if (bool)
        i = Integer.parseInt(parts2[0]);
      else if (parts2[0].equals("bankrupt"))
        i = 24;
      else
        i = 25;
      
      return i;
    }
  }