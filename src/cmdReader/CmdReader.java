package cmdReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CmdReader {
		
	/*
	 * Read the command File into the queue array
	 * @param the name of the input Command File
	 * @return A Queue contains a sequence of command
	 */
	public static Queue<String> cmdFileReader(String inputCmdFile) {
        try {
        	
        	Queue<String> cmdArray=new LinkedBlockingQueue<String>();
	        FileReader reader = new FileReader(inputCmdFile);
	        BufferedReader br = new BufferedReader(reader);
	        String str = null;
	
	        while((str = br.readLine()) != null) {
	              cmdArray.add(str);
	        }
	        
	        br.close();
	        reader.close();
	        
	        return cmdArray;
        }
	  catch(FileNotFoundException e) {
	        	e.printStackTrace();
	        }
      catch(IOException e) {
	            e.printStackTrace();
	        }
		return null;
	}
}