package controlPanel;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comPortWriter.ContinueWrite;
import comPortWriter.ShowUtils;
import comPortWriter.Tuple;
import cmdReader.string2DataPacket;
import gnu.io.CommPortIdentifier;

/*
 * Serial Port ActionListener,
 * click once turn yellow, click twice return to previous color
 * */
class serialPortColor implements ActionListener{
	
	JButton botton;
	int counter = 0;
	ContinueWrite local_writer_group[]=null;
	
    public serialPortColor(JButton b) {
    	this.botton = b;
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
	
		int buttoNum=Integer.valueOf(this.botton.getName());   //Get the number of the button
		this.counter++;
		
		if(counter%2 != 0){
			this.botton.setBackground(Color.YELLOW);
			ContinueWrite.writer[buttoNum] = new ContinueWrite(buttoNum); // 1 - 13
			
			// If the port already in use, the open port operation will be undo, the color will not be set, otherwise open the port
			if(ContinueWrite.writer[buttoNum].startComPort() == 2){
				this.botton.setBackground(Color.GREEN);
				ShowUtils.openPortfailed(buttoNum);
				return;
			}
			ControlPanel.availablePort++;
			ControlPanel.activePort.add(buttoNum);
			ContinueWrite.writer[buttoNum].start();
		}   
		else{
			ContinueWrite.closePort(ContinueWrite.writer[buttoNum].serialPort);
			this.botton.setBackground(Color.GREEN);
			ContinueWrite.writer[buttoNum] = null;
		}
			
	}
}

public class ControlPanel extends JFrame {

    public static final int WIDTH = 830;
    public static final int HEIGHT = 360;
 
    private JTextArea dataView = new JTextArea();
    private JScrollPane scrollDataView = new JScrollPane(dataView);
     
    // COM port control panel
    private JPanel serialPortPanel = new JPanel();
    private JButton COM1 = new JButton("1");
    private JButton COM2 = new JButton("2");
    private JButton COM3 = new JButton("3");
    private JButton COM4 = new JButton("4");
    private JButton COM5 = new JButton("5");
    private JButton COM6 = new JButton("6");
    private JButton COM7 = new JButton("7");
    private JButton COM8 = new JButton("8");
    private JButton COM9 = new JButton("9");
    private JButton COM10 = new JButton("10");
    private JButton COM11 = new JButton("11");
    private JButton COM12 = new JButton("12");
    private JButton COM13 = new JButton("13");
    
    // operation control panel
    private JPanel operatePanel = new JPanel();
    private JTextField dataInput = new JTextField();
    private JButton sendData = new JButton("Send");
    private JButton Clear = new JButton("Clear");
    private JButton Stop = new JButton("Stop");
    public static int availablePort=0;
    public static Queue<Integer> activePort = new LinkedBlockingQueue<Integer>();
    
    // initial the multi-thread ContinueWrite writers
    public static JButton buttonGroup[] = new JButton[14];    // index 0 inactive
    
    public ControlPanel() {
        initView();
        initComponents();
        actionListener();
        initData();
    }
 
    private void initView() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);  // Close the program
        setResizable(false);           // Inactivate the resizable function
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint(); // Set the window in center
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        this.setLayout(null);        //setLayout to fix all components through using setbounds()
        setTitle("Serial Port Communication");
    }
 
    private void initComponents() {
   
    	/*JFrame 
    	 *      |--serialPortPanel {JPanel}
    	 *      |--scrollDataView  {JScrollPane}
    	 *      |--operatePanel    {JPanel}
    	 * */  
        // ¥Æø⁄…Ë÷√√Ê∞Â
        serialPortPanel.setBorder(BorderFactory.createTitledBorder("Serial Port Selection"));
        serialPortPanel.setBounds(5, 5, 810, 150);
        serialPortPanel.setLayout(null);
        add(serialPortPanel);
        
        COM1.setBounds(20, 20, 50, 100);
        COM2.setBounds(80, 20, 50, 100);
        COM3.setBounds(140, 20, 50, 100);
        COM4.setBounds(200, 20, 50, 100);
        COM5.setBounds(260, 20, 50, 100);
        COM6.setBounds(320, 20, 50, 100);
        COM7.setBounds(380, 20, 50, 100);
        COM8.setBounds(440, 20, 50, 100);
        COM9.setBounds(500, 20, 50, 100);
        COM10.setBounds(560, 20, 50, 100);
        COM11.setBounds(620, 20, 50, 100);
        COM12.setBounds(680, 20, 50, 100);
        COM13.setBounds(740, 20, 50, 100);
        
        COM1.setName("1");
        COM2.setName("2");
        COM3.setName("3");
        COM4.setName("4");
        COM5.setName("5");
        COM6.setName("6");
        COM7.setName("7");
        COM8.setName("8");
        COM9.setName("9");
        COM10.setName("10");
        COM11.setName("11");
        COM12.setName("12");
        COM13.setName("13");        
        
        buttonGroup[1] = COM1;
        buttonGroup[2] = COM2;
        buttonGroup[3] = COM3;
        buttonGroup[4] = COM4;
        buttonGroup[5] = COM5;
        buttonGroup[6] = COM6;
        buttonGroup[7] = COM7;
        buttonGroup[8] = COM8;
        buttonGroup[9] = COM9;
        buttonGroup[10] = COM10;
        buttonGroup[11] = COM11;
        buttonGroup[12] = COM12;
        buttonGroup[13] = COM13;

        COM1.setFocusable(false);
        COM2.setFocusable(false);
        COM3.setFocusable(false);
        COM4.setFocusable(false);
        COM5.setFocusable(false);
        COM6.setFocusable(false);
        COM7.setFocusable(false);
        COM8.setFocusable(false);
        COM9.setFocusable(false);
        COM10.setFocusable(false);
        COM11.setFocusable(false);
        COM12.setFocusable(false);
        COM13.setFocusable(false);
        
        serialPortPanel.add(COM1);
        serialPortPanel.add(COM2);
        serialPortPanel.add(COM3);
        serialPortPanel.add(COM4);
        serialPortPanel.add(COM5);
        serialPortPanel.add(COM6);
        serialPortPanel.add(COM7);
        serialPortPanel.add(COM8);
        serialPortPanel.add(COM9);
        serialPortPanel.add(COM10);
        serialPortPanel.add(COM11);
        serialPortPanel.add(COM12);
        serialPortPanel.add(COM13);
 
        // dataView --- scrollDataView {JScrollPane}
        // Show entered commands 
        dataView.setBorder(BorderFactory.createTitledBorder("Command Entered"));
        dataView.setBounds(20, 220, 400, 100);
        scrollDataView.setBounds(20, 220, 400, 100);
        dataView.setFocusable(false);
        add(scrollDataView);
        
        // Operation
        operatePanel.setBorder(BorderFactory.createTitledBorder("Operation"));
        operatePanel.setBounds(450, 220, 350, 100);
        operatePanel.setLayout(null);
        add(operatePanel);
 
        /*
         * operatePanel--dataInput        {JTextField} 
         *           |--serialPortOperate {JBotton}
         *           |--sendData          {JBotton}
         * */
        dataInput.setBounds(20, 25, 300, 30);
        operatePanel.add(dataInput);
 
        sendData.setFocusable(false);
        sendData.setBounds(130, 60, 90, 30);
        operatePanel.add(sendData);
        
        Clear.setBounds(20, 60, 90, 30);
        Clear.setFocusable(false);
        operatePanel.add(Clear);
        
        Stop.setBounds(235, 60, 90, 30);
        Stop.setFocusable(false);
        operatePanel.add(Stop);
    }
 
    /*
     * Find the available COM ports.
     * COM port available: Green
     * COM port unavailable: grey
     */
    private void initData() {
    	ContinueWrite.portList = CommPortIdentifier.getPortIdentifiers();
    	
    	while (ContinueWrite.portList.hasMoreElements()) {
    		ContinueWrite.portId = (CommPortIdentifier)ContinueWrite.portList.nextElement();
    	    
    		 if (ContinueWrite.portId.getPortType() == CommPortIdentifier.PORT_SERIAL) 
    			 ControlPanel.buttonGroup[Integer.valueOf(ContinueWrite.portId.getName().replaceAll("COM", ""))].setBackground(Color.GREEN);
    		 else {
				ShowUtils.warningMessage("Port: " + Integer.valueOf(ContinueWrite.portId.getName().replaceAll("COM", "")) + " is not serial COM port");
			}
    	}
    }
 
    // Add event listeners to operatePanel
    private void actionListener() {
        
    	// Send data bottom
        sendData.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData(e);
            }
        });
        
        // Clear the previous data record
        Clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dataView.setText("");	
			}
		});
    	
    	COM1.addActionListener(new serialPortColor(COM1));
    	COM2.addActionListener(new serialPortColor(COM2));
    	COM3.addActionListener(new serialPortColor(COM3));
    	COM4.addActionListener(new serialPortColor(COM4));
    	COM5.addActionListener(new serialPortColor(COM5));
    	COM6.addActionListener(new serialPortColor(COM6));
    	COM7.addActionListener(new serialPortColor(COM7));
    	COM8.addActionListener(new serialPortColor(COM8));
    	COM9.addActionListener(new serialPortColor(COM9));
    	COM10.addActionListener(new serialPortColor(COM10));
    	COM11.addActionListener(new serialPortColor(COM11));
    	COM12.addActionListener(new serialPortColor(COM12));
    	COM13.addActionListener(new serialPortColor(COM13));
    	
        Stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
    }
 
    /**
     * Send the data to COM port
     */
	private void sendData(java.awt.event.ActionEvent evt) {
		
        /*  
         *  Only need to input, number the same as selected port, 
         *  such as COM1, COM2 is selected, 
         *  then the input stream should be:
         *  {Channel1, Channel2, ...  Steering Number1, Steering Number2, ....}
         *  
         *  All the data to be displayed in format: 
         *  {Channel 1, Steering number 1, Data 1}
         *  {Channel 2, Steering number 2, Data 2}
         *  .........
         */
		String dataInputtString = dataInput.getText().toString();
		boolean flag = true;
		
		for(int i=1; i<=13; i++)
        	if(ContinueWrite.writer[i] == null)
        		flag = false;
		
		if(!flag){
			ShowUtils.noOpenPortWarning();			
			return;
		}
		
		if(dataInputtString.length() == 0){
			  ShowUtils.noInputStringWarning();
			  return;
		}
		
		if(dataInputtString.split("\\s+").length != 2 
		||dataInputtString.split("\\s+")[0].split(",").length != dataInputtString.split("\\s+")[1].split(",").length){
			ShowUtils.wrongInputStringFormat();
			return;			
		}
		
        Queue<Queue<Tuple<Integer,Integer>>> currInput = string2DataPacket.parserString(dataInputtString);
        //System.out.println("The size of data currInput:" + currInput.size());
        
        if(currInput.size() != availablePort){
        	ShowUtils.dataPacketNumUnmatch(currInput.size(),availablePort);
        	return;
        }
      
        Queue<Tuple<Integer,Integer>>tempQueue;
        Tuple<Integer, Integer> tempTuple;
        String portInfo="";
        
        while(!ControlPanel.activePort.isEmpty()){
        	//Get the number of current active ports
        	int i = ControlPanel.activePort.remove();
        	portInfo="COM"+Integer.toString(i);
        	
    		if(!currInput.isEmpty())
    			tempQueue = currInput.remove();
    		else
    			break;
    		
    		while(!tempQueue.isEmpty()){
    			tempTuple = tempQueue.remove();
        		System.out.println(portInfo+": "+ tempTuple.first+"," + tempTuple.second);
        		ContinueWrite.fillQueue(portInfo, tempTuple.first, tempTuple.second);
        		dataView.append(portInfo + ":" +  "channel:" + tempTuple.first + " data:" + tempTuple.second + "\r\n"); //display the sent command
    		}
         }		
      }
 
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlPanel().setVisible(true);
            }
        });
    }
}