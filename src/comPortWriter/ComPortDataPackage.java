package comPortWriter;

import java.io.IOException;
import java.io.OutputStream;

/* This is the definition of the dataPackage of Controlling message
 * All the set() functions take the decimal value in integer as input.
 * All the get() functions return value is in byte format, which can be directly
 * sent to the serial port. 
 * All the startPoint has been set to 0xff. 
 * All the CMD currently is default as 0x20 for steering gear position adjustment.
 * */

import javax.xml.bind.DatatypeConverter;

/*
 * Data package format for controlling steering engines
 * NOTE: startPoint, CMD have already been set, for position adjust only.
 * the CH(Channel) and DataL/DataH information can be adjusted.
 */
public class ComPortDataPackage {
	
	private byte[] startPoint; //0xff
	private byte[] CMD;       // 0x20
	private byte[] CH;     // 0~31
	private byte[] DataL; // Data & 0xff
	private byte[] DataH; // Data >> 8
		
	// Construct from Channel and data
	public ComPortDataPackage(int channel, int data) {
		setChannel(channel);
		setData(data);
		getStartPoint();   //Also set the Start point
		getCMD();         //Also set the Command information
	}
	
	// Construct from dataInputstring
	public ComPortDataPackage(String dataInputString)
	{
		String [] arr = dataInputString.split("\\s+");
		getStartPoint();
		getCMD();
		setChannel(Integer.valueOf(arr[0]));
		setData(Integer.valueOf(arr[1]));
	}
	
	//Set Channel
	public int setChannel(int channel)
	{		
		// Controlling a COM port for 32 Stepping Motor
		if(0 <= channel && channel <= 31)
		{			
			if((Integer.toHexString(channel)).length()%2 == 0)
			{  
				this.CH = DatatypeConverter.parseHexBinary(Integer.toHexString(channel));
		    	System.out.println("Channel:" + Integer.toHexString(channel));
			}
			else
		    {
		    	String temp= "0" + Integer.toHexString(channel);
		    	this.CH =DatatypeConverter.parseHexBinary(temp);

		    	System.out.println("Channel:" + temp);
		    }
			return 0;
		}
		else
		{
			ShowUtils.warningMessage("Channel ID out of Bound!");
			return -1;
		}	
	}
	
	//Set Data
	public int setData(int Data)
	{
		/*
		 * For position parameter of stepping motor.
		 * DataLow and DataHigh are set in byte array format.
		 * 
		 * @param Data for input is the decimal integer of stepping motor.
		 * @return 0 if Data Low and Data High are correctly set.
		 * @return 1 if Data Low and Data High are out of range.
		 */
		if(500 <= Data && Data<= 2500)
		{
			// DataLow
			if((Integer.toHexString(Data&0xff)).length()%2 == 0)
			{
				this.DataL = DatatypeConverter.parseHexBinary(Integer.toHexString(Data&0xff));
		    	System.out.println("DataL:" + Integer.toHexString(Data&0xff));
			}	
			else
		    {
		    	String temp= "0" + Integer.toHexString(Data&0xff);
		    	this.DataL = DatatypeConverter.parseHexBinary(temp);
		    	System.out.println("DataL:" + temp);
		    }
			
            // DataHigh
			if((Integer.toHexString(Data>>8)).length()%2 == 0)
			{
				this.DataH= DatatypeConverter.parseHexBinary(Integer.toHexString(Data>>8));
		    	System.out.println("DataL:" + Integer.toHexString(Data>>8));
			}
		    else
		    {
		    	String temp= "0" + Integer.toHexString(Data>>8);
		    	this.DataH = DatatypeConverter.parseHexBinary(temp);
		    	System.out.println("DataH:" + temp);
		    }
		    return 0;
		}
		else 
		{
			ShowUtils.warningMessage("Control Pulse Width Out of Bound!!");
			return -1;
		}
	}
	
	/*
	 * Get Start Point
	 * @return the 0 1 byte array of the start point.
	 * NOTE: all of the data are transfered in hexadecimal format
	 */
	public byte[] getStartPoint()
	{
		int sp = 0xff;
		if((Integer.toHexString(sp)).length()%2 == 0)
	    	 this.startPoint = DatatypeConverter.parseHexBinary(Integer.toHexString(sp));
	    else
	    {
	    	String temp= "0" + Integer.toHexString(sp);
	    	this.startPoint = DatatypeConverter.parseHexBinary(temp);
	    }
		return this.startPoint;
	}
	
	/*
	 * Get Command
	 * @return the 0 1 byte array of the command.
	 * NOTE: all of the data are transfered in hexadecimal format
	 */
	public byte[] getCMD()
	{
		int command = 0x02;
		if((Integer.toHexString(command)).length()%2 == 0)
	    	this.CMD=DatatypeConverter.parseHexBinary(Integer.toHexString(command));
	    else
	    {
	    	String temp= "0" + Integer.toHexString(command);
	    	this.CMD = DatatypeConverter.parseHexBinary(temp);
	    }
		return this.CMD;
	}
	
	// Get Channel
	public byte[] getChannel()
	{
		return this.CH;
	}
	
	// Get Data Low
	public byte[] getDataL()
	{
		return this.DataL;
	}
	
	// Get Data High
	public byte[] getDataH()
	{
		return this.DataH;
	}
	
	/*
	 * Write the a control command through an output stream.
	 * @param the output stream type
	 * @return -1 if any part of the whole command writing goes wrong.
	 * @return 0 the whole command writing process goes correctly.
	 */
	public int writeThroughOutputStream(OutputStream os) 
	{
		try {
			os.write(this.getStartPoint());
		} catch (IOException e) {
			System.out.println("Write start point failed");
			e.printStackTrace();
			return -1;
		}
    	
		try {
			os.write(this.getCMD());
		} catch (IOException e) {
			System.out.println("Write command failed");
			e.printStackTrace();
			return -1;
		}
		
    	try {
			os.write(this.getChannel());
		} catch (IOException e) {
			System.out.println("Write Channel failed");
			e.printStackTrace();
			return -1;
		}
    	
    	try {
			os.write(this.getDataL());
		} catch (IOException e) {
			System.out.println("Write Data Low failed");
			e.printStackTrace();
			return -1;
		}
    	
    	try {
			os.write(this.getDataH());
		} catch (IOException e) {
			System.out.println("Write Data High failed");
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
}