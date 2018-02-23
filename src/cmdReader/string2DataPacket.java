package cmdReader;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import comPortWriter.Tuple;

public class string2DataPacket {
	
	/* 
	 * For selected COM port, for example COM1, COM2, the selected steering 20, 18, the steering position 1500, 1600
	 * The data Format is "20,18   1500,1600" which in between there is one or more Space blanks
	 * Return a general Queue contains Tuple pairs like (20, 1500) (18, 1600)
	 * 15,12,17;12 1500,1700,1800;1600
	 * 
	 * COM1: 15 1500
	 *       12 1700
	 *       17 1800
	 *       
	 * COM2: 12 1600
	 * 
	 * Storing Format: [[15,1500], [12,1700], [17,1800]], [[12,1600]]
	 * 
	 * Be careful with the Queue.clear(), it will clear all the data ever entered, 
	 * even the previous Queue has been added to another container
	 * 
	 * */
	
	public static Queue<Queue<Tuple<Integer,Integer>>> parserString(String InputSring)
	{
		Queue<Queue<Tuple<Integer, Integer>>> result= new LinkedBlockingQueue<Queue<Tuple<Integer, Integer>>>();
		Queue<Tuple<Integer, Integer>> cmdQueue = new LinkedBlockingQueue<Tuple<Integer,Integer>>();
		
		String InputStringPartition[] = InputSring.split("\\s+");  //15,12,17;12 | 1500,1700,1800;1600
		String steeringNum[] = InputStringPartition[0].split(";");  //15,12,17 | 12
		String steeringPos[] = InputStringPartition[1].split(";");  //1500,1700,1800 |1600
		
		for(int i=0;i<steeringNum.length;i++)
		{
			cmdQueue =new LinkedBlockingQueue<Tuple<Integer,Integer>>();
			
			String steeringNumEach[] = steeringNum[i].split(",");  //15,12,17
			String steeringPosEach[] = steeringPos[i].split(",");  //1500,1700,1800
			
		    if(steeringNumEach.length != steeringPosEach.length)
		    	System.out.println("Unmatched length of steering num To position");
		    	
			
			for(int j = 0; j < steeringNumEach.length; j++)
				cmdQueue.add(new Tuple<Integer,Integer>(Integer.valueOf(steeringNumEach[j]), Integer.valueOf(steeringPosEach[j])));	
				
			result.add(cmdQueue);
		}		  
		return result;
	}
}
