
package cmdReader;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import comPortWriter.ContinueWrite;
import comPortWriter.Tuple;

class StringArrayCut{
	
	/* 
	 * For each line of command,
	 * connect the later part of the string 
	 * then return the string contain the steering number and data information
	 * */
	public static String dataPartcut(String[]Input)
	{
		String result=Input[1] +" "+Input[2];		
		return result;
	}
	
	/* 
	 * For each line of command, 
	 * process the string and store the port information into a queue
	 * */
	public static Queue<String> portPart2Queue(String PortInput)
	{
		Queue<String> result = new LinkedBlockingQueue<String>();
		
		String []availablePortTemp = PortInput.split(";");
		
		for(int i=0;i<availablePortTemp.length;i++)
			result.add(availablePortTemp[i]);
		
		return result;
	}
}
public class ReadInCmd {

	public static void send() {
		
		/*
		 * Only need to input, number the same as selected port, 
	     *  such as COM1, COM2 is selected, 
	     *  then the input stream should be:
	     *  {COM1;COM2;...    Channel1, Channel2, ...     Steering Number1, Steering Number2, ....}
	     *  
	     *  All the data to be displayed in format: 
	     *  {COM1: Channel 1, Data 1}
	     *  {COM2: Channel 2, Data 2}
	     *  .........
	     */
		
		Queue<String> cmdArray = CmdReader.cmdFileReader("ComPortCMD9.txt"); //file read in string array
		
		if(cmdArray.isEmpty()){
			System.out.println("No available Commands");
			return;
			}
		
		String Currentstring=null;
		String[] CurrentstringData=null;
		String dataInputtString=null;
		Scanner sc = new Scanner(System.in);
		String ctx = new String("");
		
		// Process the cmdFile line by line
		while(!cmdArray.isEmpty())
		{
			/*while(true)
			{
				ctx = sc.nextLine();
				if(ctx.equals("6"))  // Enter "6" then continue to next line
					break;
			}*/
			
			System.out.println("Press \"ENTER\" to continue...");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			
			Currentstring = cmdArray.remove();
		    CurrentstringData = Currentstring.split("\\s+");
		    Queue<String>tempPortQueue = StringArrayCut.portPart2Queue(CurrentstringData[0]);
		   
		    dataInputtString = StringArrayCut.dataPartcut(CurrentstringData);
			Queue<Queue<Tuple<Integer,Integer>>> currInput = string2DataPacket.parserString(dataInputtString);
		    	
		    Queue<Tuple<Integer,Integer>>tempQueue;
		    Tuple<Integer, Integer> tempTuple;
		    String portInfo="";
		    
		    while(!tempPortQueue.isEmpty())
		    {
		    	portInfo = tempPortQueue.remove();
		    	
				if(!currInput.isEmpty())
					tempQueue = currInput.remove();
				else
					break;
				
				/*if(portInfo.length() != currInput.size())
				{
					System.out.println("Unmatched num of port To the tempQueue");
					return;
				}*/
				
				while(!tempQueue.isEmpty())
				{
					tempTuple = tempQueue.remove();
					//System.out.println("************************************************");
		    		System.out.println(portInfo+": "+ tempTuple.first+"," + tempTuple.second);
		    		ContinueWrite.fillQueue(portInfo, tempTuple.first, tempTuple.second);
				}	
		     } 
		    //ContinueWrite.cmdReady = true;
		}
	}

	/*public static void main(String[] args) {
		send();
	}*/
}
