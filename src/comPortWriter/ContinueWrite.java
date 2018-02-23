package comPortWriter;

import java.io.*;
import gnu.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import comPortWriter.ComPortDataPackage;
import comPortWriter.Tuple;
import cmdReader.*;

public class ContinueWrite extends Thread implements SerialPortEventListener {
	
	public static CommPortIdentifier portId;  // Serial Port management class
    public static Enumeration<?> portList;    // Enumeration of available port
    public static boolean cmdReady = false;   //
    public OutputStream outputStream=null;    // outPutStream of a port, default as null
    public SerialPort serialPort = null;      // reference of a port
   
    int writerId;   // Current writer's ID
    private static BlockingQueue<Tuple<String, ComPortDataPackage>> msgQueue = new LinkedBlockingQueue<Tuple<String, ComPortDataPackage>>();   
    public static ContinueWrite writer[]=new ContinueWrite[40];  //index 0 inactive

	@Override
	public void serialEvent(SerialPortEvent arg0) {
	}
   
	//Initialize the writer ID of each thread
    public ContinueWrite(int Id) {
    	this.writerId = Id;   	
	}
    
    //Produce the number of and put them into the container, should be covered by a input interface or command line interface
    public static int fillQueue(String portInfo, int channel, int data)
    {
    	  ComPortDataPackage datapacket = new ComPortDataPackage(channel, data);
		  Tuple<String, ComPortDataPackage> t = new Tuple<String, ComPortDataPackage>(portInfo, datapacket);
		  msgQueue.add(t);
		  return 0;
    }
   
    /*
     * Open a COM port
     */
    public int startComPort() {
        
    	//Get the List of open COM port
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM"+Integer.toString(this.writerId))) {
                    try {
                        this.serialPort = (SerialPort) portId.open("COM"+Integer.toString(this.writerId), 2000);  
                    } catch (PortInUseException e) {
                        ShowUtils.portInUseWarning(this.writerId);
                        return 2;
                    } finally {
                    	System.out.println("Device Type£º--->" + portId.getPortType());
                        System.out.println("Device Name£º---->" + "COM"+Integer.toString(this.writerId));
					}

                    //Open output stream
                    try {
                        this.outputStream = this.serialPort.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    
                    try {
                        this.serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    
                    this.serialPort.notifyOnDataAvailable(true);

                    try {
                        this.serialPort.setSerialPortParams(9600,
							                                SerialPort.DATABITS_8, 
							                                SerialPort.STOPBITS_1,
							                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return 1;
                }
            }
        }
        return 0;
    }
    
    /*
     * static method to close a serial port
     * */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }
    
	@Override
    public void run() {
    	
       System.out.println("------------Thread running--------------");
       Tuple<String, ComPortDataPackage> temp_msg;
       
       try {
			this.outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        while (true) {
        	/*
        	 * In multi-Thread msgQueue.element()
        	 * In single Thread MsgQueue.remove()
        	 */
            if (msgQueue.size() > 0 ) {
				try {
					 temp_msg = msgQueue.element();
					} 
				catch(Exception NoSuchElementException){
						return;
					}	  
			
				//System.out.println(temp_msg.first);
				
				if(temp_msg.first.contains("COM"+Integer.toString(this.writerId))){		
					
					msgQueue.remove();
					//assert(this.outputStream == null);
					
			        if(this.outputStream != null){
                    	System.out.println("**************************************");
			        	//System.out.println(temp_msg.first+"////////////////////////////");
			        	temp_msg.second.writeThroughOutputStream(this.outputStream);
			        	try {
							this.outputStream.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
			        }
				}
              } // !--if (msgQueue.size() > 0 )
            //else
            	//ContinueWrite.cmdReady = false;
           } //--end While
        
        //Do some cleaning work
        /*try {
			this.outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 	
        ContinueWrite.closePort(this.serialPort);*/        
    }
	
	
    public static void main(String[] args) throws InterruptedException{
    	
    	/*
    	 * Handling the discrete writer's ID and thread id when 
    	 * their ids are not continuous.
    	 */
    	Queue<Integer>PortAssign=new LinkedBlockingQueue<Integer>();
    	Queue<Integer>PortStore=new LinkedBlockingQueue<Integer>();
    	
    	
    	
    	/*PortAssign.add(1);
    	PortAssign.add(2);
    	PortAssign.add(3);
    	PortAssign.add(4);
    	PortAssign.add(5);
    	PortAssign.add(6);*/
    	PortAssign.add(16);
    	/*PortAssign.add(27);
    	PortAssign.add(28);
    	PortAssign.add(29);
    	
    	
    	/*
    	 * Initialize the COM port instants: ContinueWrite,
    	 * Each COM port instance corresponding to one thread for management.
    	 * Open the COM Port through startComport()
    	 * Boot up running threads for each Writer.
    	 */
    	int i=0;
    	
    	while(PortAssign.size()!=0)
    	{
    	   i=PortAssign.remove();
    	   PortStore.add(i);
    	   ContinueWrite.writer[i] = new ContinueWrite(i);
    	   ContinueWrite.writer[i].startComPort();
    	   ContinueWrite.writer[i].start();
    	   //ContinueWrite.writer[i].join();
    	}
    	
    	/* 
    	 * Read in the commands, 6
    	 * send the commands to the COM port
    	 */
    	ReadInCmd.send();
    	
    	//Close the COM Port
    	while(PortStore.size() != 0)
    	{
    		i=PortStore.remove();
    	   ContinueWrite.closePort(ContinueWrite.writer[i].serialPort);
    	}
    	return;
    }
}