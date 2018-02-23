package comPortWriter;


/*
 * This file defined the general pop up messages
 * showed via the Control Panel window
 */
import javax.swing.JOptionPane;

public class ShowUtils {

	public static void warningMessage(String errMsg){
		JOptionPane.showMessageDialog(null, errMsg, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noOpenPortWarning(){
		JOptionPane.showMessageDialog(null, "No Available Port!!!", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noInputStringWarning(){
		JOptionPane.showMessageDialog(null, "No Input String!!!", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void wrongInputStringFormat(){
		JOptionPane.showMessageDialog(null, "Wrong Input String format", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void portInUseWarning(int portNum){
		JOptionPane.showMessageDialog(null, "COM" + portNum + "Already In Use!!!", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void dataPacketNumUnmatch(int dataPacketNum, int PortNum){
		JOptionPane.showMessageDialog(null, "Port and Data packet number non-match", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void openPortfailed(int PortNum){
		JOptionPane.showMessageDialog(null, "Open the COM" + PortNum + "failed", "Error", JOptionPane.WARNING_MESSAGE);
	}
	
}