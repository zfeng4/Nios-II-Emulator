package emulator;
/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.JFileChooser;
 import javax.swing.JTextArea;
 import javax.swing.filechooser.FileNameExtensionFilter;
 import java.awt.Color;
 import javax.swing.JTextField;

 public class GUI implements ActionListener {
   //this is the declaration of the main window
   public static JFrame mainWindow;

   //ERROR message
   public static String errorMessage="";
   public static String alertMessage="";
   public static boolean fileSelected;
   //these variables are for the command line
   public static JButton execute = new JButton("Execute");
   public static JTextArea commandLine = new JTextArea();
   public static JScrollPane commandLineScroll = new JScrollPane(commandLine);
   public static String executionCommand;

   //these variables are for the browse bar
   public static JTextArea browseBox = new JTextArea();
   public static JScrollPane browseBoxScroll = new JScrollPane(browseBox);
   public static String filePath="";

   //this will declare all of the text boxes
   public static JTextArea registerBox, codeBox, miscBox, memoryBox, errorBox;
   public static JScrollPane registerBoxScroll, codeScroll, miscBoxScroll, memoryBoxScroll, errorBoxScroll;

   //these variables are for the 4 hex displays;
   public static JTextArea hex1Segment0Display, hex1Segment1Display, hex1Segment2Display, hex1Segment3Display, hex1Segment4Display, hex1Segment5Display, hex1Segment6Display;
   public static JTextArea hex2Segment0Display, hex2Segment1Display, hex2Segment2Display, hex2Segment3Display, hex2Segment4Display, hex2Segment5Display, hex2Segment6Display;
   public static JTextArea hex3Segment0Display, hex3Segment1Display, hex3Segment2Display, hex3Segment3Display, hex3Segment4Display, hex3Segment5Display, hex3Segment6Display;
   public static JTextArea hex4Segment0Display, hex4Segment1Display, hex4Segment2Display, hex4Segment3Display, hex4Segment4Display, hex4Segment5Display, hex4Segment6Display;
   static Boolean[] hexDisplaySegment= new Boolean[28];
   //these variables determine the state of the switches
   public static int switch1State = 0, switch2State = 0, switch3State = 0, switch4State = 0;

   //these variables make up the actual switch
   public static JButton switch1TopButton, switch1BottomButton, switch2TopButton, switch2BottomButton, switch3TopButton, switch3BottomButton, switch4TopButton, switch4BottomButton;


   //debug lastline
   public static int lastLine=0;

   //exception used to view stackframe in logs
   static Exception p= new Exception();

   //increment var used to name files.
   static public int logNumber=0;

   //locations of I/O Devices in memory
   static public int MEMHEX=1000;

   static public int SWITCH1=1001;
   static public int SWITCH2=1002;
   static public int SWITCH3=1003;
   static public int SWITCH4=1004;



   public static void main(String[] args) {
     GUI N2EMU = new GUI();
	 processor.initialize();
	updateGui();
	threads processorStart= new threads("processorThread");
	   processorStart.start();
	   System.out.println("processor thread started");
	threads guiStart= new threads("guiThread");
	   guiStart.start();
	   System.out.println("GUI THREAD STARTED");

   }

   public GUI(){
     //this will open up a new window so that we can put stuff into it.
     mainWindow = new JFrame("N2EMU: A NIOS II Emulator");
     mainWindow.setSize(1300,720);
     mainWindow.setLayout(null);
     mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     for(int i=0; i<hexDisplaySegment.length; i++){
 		   hexDisplaySegment[i]=false;
 	   }
     //This sets the font for the text
     Font font = new Font("serif", Font.PLAIN, 20);

     //this will add the bar for file name
     browseBoxScroll.setBounds(10, 10, 730, 40);
     mainWindow.add(browseBoxScroll);

     //this is for the register box
     registerBox = new JTextArea();
     registerBox.setEditable(false);
     registerBox.setFont(font);
     registerBoxScroll = new JScrollPane(registerBox); //this makes the text box scrollable
     registerBoxScroll.setBounds(10, 55, 240, 285);
     mainWindow.add(registerBoxScroll);

     //this is for the code box
     codeBox = new JTextArea();
     codeBox.setEditable(false);
     codeBox.setFont(font);
     codeScroll = new JScrollPane(codeBox); //this makes the text box scrollable
     codeScroll.setBounds(260, 55, 480, 485);
     mainWindow.add(codeScroll);

     //this is for the misc box
     miscBox = new JTextArea();
     miscBox.setEditable(false);
     miscBox.setFont(font);
     miscBoxScroll= new JScrollPane(miscBox); //this makes the text box scrollable
     miscBoxScroll.setBounds(10, 350, 240, 190);
     mainWindow.add(miscBoxScroll);

     //this is for the memory box
     memoryBox = new JTextArea();
     memoryBox.setEditable(false);
     memoryBox.setFont(font);
     memoryBoxScroll = new JScrollPane(memoryBox); //this makes the text box scrollable
     memoryBoxScroll.setBounds(750, 125, 240, 415);
     mainWindow.add(memoryBoxScroll);

     //this is for where the errors will be displayed
     errorBox = new JTextArea();
     errorBox.setEditable(false);
     errorBoxScroll = new JScrollPane(errorBox); //this makes the text box scrollable
     errorBoxScroll.setBounds(10, 550, 460, 140);
     mainWindow.add(errorBoxScroll);

     //this will make the command line
     commandLineScroll.setBounds(480, 550, 380, 140);;
     mainWindow.add(commandLineScroll);
     //commandLine.setText("Please input one of the following commands:\n vm, rm, ram, ab, rb, rab, cmes, rmLogs,\n veryslow, slow, normal, fast, extreme");
    // commandLine.setEditable(true);



     //this makes the text box that says "Switches"
     JTextField switchesDisplayTextField = new JTextField("Switches\nMEM:1001-1004");

     switchesDisplayTextField.setBounds(1000, 250, 235, 45);
     switchesDisplayTextField.setFont(font);
     switchesDisplayTextField.setHorizontalAlignment(JTextField.CENTER);
     switchesDisplayTextField.setEditable(false);
     mainWindow.add(switchesDisplayTextField);

     //this makes the text box that says "Hex Display"
     JTextField hexDisplayTextField = new JTextField("Hex Display\nMEM:1000");
     hexDisplayTextField.setBounds(1000, 455, 235, 45);
     hexDisplayTextField.setFont(font);
     hexDisplayTextField.setHorizontalAlignment(JTextField.CENTER);
     hexDisplayTextField.setEditable(false);
     mainWindow.add(hexDisplayTextField);

     //this is to add all the buttons
     JButton run, stepUp, pause, reset, browse, load, writeToTextFile;


     //this is for the browse button
     browse = new JButton("Browse");
     browse.setBounds(770, 15 ,90, 30);
     mainWindow.add(browse);
     browse.addActionListener(this);

     //This is for the run button and adds the action listener
     run = new JButton("Run");
     run.setBounds(770, 60, 90, 30);
     mainWindow.add(run);
     run.addActionListener(this);

     //This is for the Step Up Button and adds the action listener
     stepUp = new JButton("Step Up");
     stepUp.setBounds(870, 60, 90, 30);
     mainWindow.add(stepUp);
     stepUp.addActionListener(this);

     //this is for the pause button and adds the action listener
     pause = new JButton("Pause");
     pause.setBounds(770, 90, 90, 30);
     mainWindow.add(pause);
     pause.addActionListener(this);

     //this is for the load button and adds the action listener
     load = new JButton("Load");
     load.setBounds(870, 15, 90, 30);
     mainWindow.add(load);
     load.addActionListener(this);

     //this is for the reset button and adds the action listener
     reset = new JButton("Reset");
     reset.setBounds(870, 90, 90, 30);
     mainWindow.add(reset);
     reset.addActionListener(this);

     //this is for the execute button and adds the action listener
     execute.setBounds(860, 580, 110, 30);
     mainWindow.add(execute);
     execute.addActionListener(this);

     //this is for the write to .txt button and adds the action listener
     writeToTextFile = new JButton("Write to .txt");
     writeToTextFile.setBounds(860, 620, 110, 30);
     mainWindow.add(writeToTextFile);
     writeToTextFile.addActionListener(this);



 //this is for the 4 switches
     //this is for switch 1
     switch1TopButton = new JButton();
     switch1TopButton.setBounds(1005, 310, 20, 20);
     switch1TopButton.setBackground(Color.red);
     switch1TopButton.setOpaque(true);
     switch1TopButton.setBorderPainted(false);
     mainWindow.add(switch1TopButton);
     switch1TopButton.addActionListener(this);

     switch1BottomButton = new JButton();
     switch1BottomButton.setBounds(1005, 330, 20, 20);
     switch1BottomButton.setBackground(Color.darkGray);
     switch1BottomButton.setOpaque(true);
     switch1BottomButton.setBorderPainted(false);
     mainWindow.add(switch1BottomButton);
     switch1BottomButton.addActionListener(this);

     switch1State=0;
     //this is for switch 2
     switch2TopButton = new JButton();
     switch2TopButton.setBounds(1073, 310, 20, 20);
     switch2TopButton.setBackground(Color.red);
     switch2TopButton.setOpaque(true);
     switch2TopButton.setBorderPainted(false);
     mainWindow.add(switch2TopButton);
     switch2TopButton.addActionListener(this);

     switch2BottomButton = new JButton();
     switch2BottomButton.setBounds(1073, 330, 20, 20);
     switch2BottomButton.setBackground(Color.darkGray);
     switch2BottomButton.setOpaque(true);
     switch2BottomButton.setBorderPainted(false);
     mainWindow.add(switch2BottomButton);
     switch2BottomButton.addActionListener(this);

     switch2State=0;

     //this is for switch 3
     switch3TopButton = new JButton();
     switch3TopButton.setBounds(1142, 310, 20, 20);
     switch3TopButton.setBackground(Color.red);
     switch3TopButton.setOpaque(true);
     switch3TopButton.setBorderPainted(false);
     mainWindow.add(switch3TopButton);
     switch3TopButton.addActionListener(this);

     switch3BottomButton = new JButton();
     switch3BottomButton.setBounds(1142, 330, 20, 20);
     switch3BottomButton.setBackground(Color.darkGray);
     switch3BottomButton.setOpaque(true);
     switch3BottomButton.setBorderPainted(false);
     mainWindow.add(switch3BottomButton);
     switch3BottomButton.addActionListener(this);

     switch3State=0;

     //this is for switch 4
     switch4TopButton = new JButton();
     switch4TopButton.setBounds(1210, 310, 20, 20);
     switch4TopButton.setBackground(Color.red);
     switch4TopButton.setOpaque(true);
     switch4TopButton.setBorderPainted(false);
     mainWindow.add(switch4TopButton);
     switch4TopButton.addActionListener(this);

     switch4BottomButton = new JButton();
     switch4BottomButton.setBounds(1210, 330, 20, 20);
     switch4BottomButton.setBackground(Color.darkGray);
     switch4BottomButton.setOpaque(true);
     switch4BottomButton.setBorderPainted(false);
     mainWindow.add(switch4BottomButton);
     switch4BottomButton.addActionListener(this);

     switch4State=0;

 //this is for hex display 1 all the way to the left
     //hex 1 segment 0
     hex1Segment0Display = new JTextArea();
     hex1Segment0Display.setBounds(1008, 525, 30, 3);
     hex1Segment0Display.setEditable(false);
     hex1Segment0Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment0Display);

     //hex 1 segment 1
     hex1Segment1Display = new JTextArea();
     hex1Segment1Display.setBounds(1038, 530, 3, 30);
     hex1Segment1Display.setEditable(false);
     hex1Segment1Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment1Display);

     //hex 1 segment 2
     hex1Segment2Display = new JTextArea();
     hex1Segment2Display.setBounds(1038, 568, 3, 30);
     hex1Segment2Display.setEditable(false);
     hex1Segment2Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment2Display);

     //hex 1 segment 3
     hex1Segment3Display = new JTextArea();
     hex1Segment3Display.setBounds(1008, 600, 30, 3);
     hex1Segment3Display.setEditable(false);
     hex1Segment3Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment3Display);

     //hex 1 segment 4
     hex1Segment4Display = new JTextArea();
     hex1Segment4Display.setBounds(1005, 568, 3, 30);
     hex1Segment4Display.setEditable(false);
     hex1Segment4Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment4Display);

     //hex 1 segment 5
     hex1Segment5Display = new JTextArea();
     hex1Segment5Display.setBounds(1005, 530, 3, 30);
     hex1Segment5Display.setEditable(false);
     hex1Segment5Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment5Display);

     //hex 1 segment 6
     hex1Segment6Display = new JTextArea();
     hex1Segment6Display.setBounds(1008, 562, 30, 3);
     hex1Segment6Display.setEditable(false);
     hex1Segment6Display.setBackground(Color.lightGray);
     mainWindow.add(hex1Segment6Display);

 //this is for hex display 2
     //hex 2 segment 0
     hex2Segment0Display = new JTextArea();
     hex2Segment0Display.setBounds(1071, 525, 30, 3);
     hex2Segment0Display.setEditable(false);
     hex2Segment0Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment0Display);

     //hex 2 segment 1
     hex2Segment1Display = new JTextArea();
     hex2Segment1Display.setBounds(1101, 530, 3, 30);
     hex2Segment1Display.setEditable(false);
     hex2Segment1Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment1Display);

     //hex 2 segment 2
     hex2Segment2Display = new JTextArea();
     hex2Segment2Display.setBounds(1101, 568, 3, 30);
     hex2Segment2Display.setEditable(false);
     hex2Segment2Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment2Display);

     //hex 2 segment 3
     hex2Segment3Display = new JTextArea();
     hex2Segment3Display.setBounds(1071, 600, 30, 3);
     hex2Segment3Display.setEditable(false);
     hex2Segment3Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment3Display);

     //hex 2 segment 4
     hex2Segment4Display = new JTextArea();
     hex2Segment4Display.setBounds(1068, 568, 3, 30);
     hex2Segment4Display.setEditable(false);
     hex2Segment4Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment4Display);

     //hex 2 segment 5
     hex2Segment5Display = new JTextArea();
     hex2Segment5Display.setBounds(1068, 530, 3, 30);
     hex2Segment5Display.setEditable(false);
     hex2Segment5Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment5Display);

     //hex 2 segment 6
     hex2Segment6Display = new JTextArea();
     hex2Segment6Display.setBounds(1071, 562, 30, 3);
     hex2Segment6Display.setEditable(false);
     hex2Segment6Display.setBackground(Color.lightGray);
     mainWindow.add(hex2Segment6Display);

 //this is for hex display 3
     //hex 3 segment 0
     hex3Segment0Display = new JTextArea();
     hex3Segment0Display.setBounds(1135, 525, 30, 3);
     hex3Segment0Display.setEditable(false);
     hex3Segment0Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment0Display);

     //hex 3 segment 1
     hex3Segment1Display = new JTextArea();
     hex3Segment1Display.setBounds(1165, 530, 3, 30);
     hex3Segment1Display.setEditable(false);
     hex3Segment1Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment1Display);

     //hex 3 segment 2
     hex3Segment2Display = new JTextArea();
     hex3Segment2Display.setBounds(1165, 568, 3, 30);
     hex3Segment2Display.setEditable(false);
     hex3Segment2Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment2Display);

     //hex 3 segment 3
     hex3Segment3Display = new JTextArea();
     hex3Segment3Display.setBounds(1135, 600, 30, 3);
     hex3Segment3Display.setEditable(false);
     hex3Segment3Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment3Display);

     //hex 3 segment 4
     hex3Segment4Display = new JTextArea();
     hex3Segment4Display.setBounds(1132, 568, 3, 30);
     hex3Segment4Display.setEditable(false);
     hex3Segment4Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment4Display);

     //hex 3 segment 5
     hex3Segment5Display = new JTextArea();
     hex3Segment5Display.setBounds(1132, 530, 3, 30);
     hex3Segment5Display.setEditable(false);
     hex3Segment5Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment5Display);

     //hex 3 segment 6
     hex3Segment6Display = new JTextArea();
     hex3Segment6Display.setBounds(1135, 562, 30, 3);
     hex3Segment6Display.setEditable(false);
     hex3Segment6Display.setBackground(Color.lightGray);
     mainWindow.add(hex3Segment6Display);

 //this is for hex display 4 all the way to the right
     //hex 4 segment 0
     hex4Segment0Display = new JTextArea();
     hex4Segment0Display.setBounds(1198, 525, 30, 3);
     hex4Segment0Display.setEditable(false);
     hex4Segment0Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment0Display);

     //hex 4 segment 1
     hex4Segment1Display = new JTextArea();
     hex4Segment1Display.setBounds(1228, 530, 3, 30);
     hex4Segment1Display.setEditable(false);
     hex4Segment1Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment1Display);

     //hex 4 segment 2
     hex4Segment2Display = new JTextArea();
     hex4Segment2Display.setBounds(1228, 568, 3, 30);
     hex4Segment2Display.setEditable(false);
     hex4Segment2Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment2Display);

     //hex 4 segment 3
     hex4Segment3Display = new JTextArea();
     hex4Segment3Display.setBounds(1198, 600, 30, 3);
     hex4Segment3Display.setEditable(false);
     hex4Segment3Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment3Display);

     //hex 4 segment 4
     hex4Segment4Display = new JTextArea();
     hex4Segment4Display.setBounds(1195, 568, 3, 30);
     hex4Segment4Display.setEditable(false);
     hex4Segment4Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment4Display);

     //hex 4 segment 5
     hex4Segment5Display = new JTextArea();
     hex4Segment5Display.setBounds(1195, 530, 3, 30);
     hex4Segment5Display.setEditable(false);
     hex4Segment5Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment5Display);

     //hex 4 segment 6
     hex4Segment6Display = new JTextArea();
     hex4Segment6Display.setBounds(1198, 562, 30, 3);
     hex4Segment6Display.setEditable(false);
     hex4Segment6Display.setBackground(Color.lightGray);
     mainWindow.add(hex4Segment6Display);


     mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this sets the program to terminate when the window is closed

     mainWindow.setVisible(true); //this sets everything to visible

   }


   public void actionPerformed(ActionEvent e) {
     if (e.getActionCommand().equals("Browse")) {
       System.out.println("Browse Button Has been clicked.");

       //this will select the file
       filePath = null;
       JFileChooser fileChooser = new JFileChooser();
       if(new File("testing/").isDirectory()==false){
 		  addMessage("testing/ directory not found, creating directory");
 		  new File("testing/").mkdirs();
 		  addMessage("testing/ created in program folder");

 	  }
       fileChooser.setCurrentDirectory(new File("testing/"));
       FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text"); //this sets the file chooser to only choose text files
       fileChooser.setFileFilter(filter);

       int result = fileChooser.showOpenDialog(new JFrame());
       if (result == fileChooser.APPROVE_OPTION) {
           File selectedFile = fileChooser.getSelectedFile();
           filePath = selectedFile.getAbsolutePath();
           browseBox.setText(filePath);
       }
     } else if (e.getActionCommand().equals("Run")) {
       System.out.println("Run button has been clicked.");
       processor.runP();
     } else if (e.getActionCommand().equals("Step Up")) {
       System.out.println("Step Up button has been clicked.");
       processor.stepUp();
     } else if (e.getActionCommand().equals("Pause")) {
       System.out.println("Pause button has been clicked.");
       processor.pause();
     } else if (e.getActionCommand().equals("Load")) {
       System.out.println("Load button has been clicked.");
       filePath = browseBox.getText();
       if (filePath.length() < 5) {
         browseBox.setText("Please input a .txt file");
       } else if (filePath.charAt(filePath.length()-1) == 't' && filePath.charAt(filePath.length()-2) == 'x' && filePath.charAt(filePath.length()-3) == 't') {
         browseBox.setText(filePath);
         System.out.println(filePath);
         loadFile.PushFileIntoString();

       } else {
         browseBox.setText("Please input a .txt file");
       }
     } else if (e.getActionCommand().equals("Reset")) {
       System.out.println("Reset button has been clicked.");
       processor.reset();
     } else if (e.getSource() == execute) {
       System.out.println("Execute button has been clicked.");
       executionCommand = commandLine.getText();
       System.out.println(executionCommand);
       BehavioralCore.exeCommand(executionCommand);
       commandLine.setText(null);
     } else if (e.getActionCommand().equals("Write to .txt")) {
       writeLog();
 //This is for switch 1
     } else if (e.getSource() == switch1TopButton || e.getSource() == switch1BottomButton) {
       if(processor.processorState!=2){
    	 if (switch1State == 0) {
         switch1TopButton.setBackground(Color.darkGray);
         switch1BottomButton.setBackground(Color.green);
         switch1State = 1;
         System.out.println("Switch 1 ON");

       } else if (switch1State == 1) {
         switch1TopButton.setBackground(Color.red);
         switch1BottomButton.setBackground(Color.darkGray);
         switch1State = 0;
         System.out.println("Switch 1 OFF");

       }
       }
 //This is for switch 2
     } else if (e.getSource() == switch2TopButton || e.getSource() == switch2BottomButton) {
         if(processor.processorState!=2){

    	 if (switch2State == 0) {
         switch2TopButton.setBackground(Color.darkGray);
         switch2BottomButton.setBackground(Color.green);
         switch2State = 1;
         System.out.println("Switch 2 ON");

       } else if (switch2State == 1) {
         switch2TopButton.setBackground(Color.red);
         switch2BottomButton.setBackground(Color.darkGray);
         switch2State = 0;
         System.out.println("Switch 2 OFF");

       }
         }
 //This is for switch 3
     }  else if (e.getSource() == switch3TopButton || e.getSource() == switch3BottomButton) {
         if(processor.processorState!=2){

    	 if (switch3State == 0) {
         switch3TopButton.setBackground(Color.darkGray);
         switch3BottomButton.setBackground(Color.green);
         switch3State = 1;
         System.out.println("Switch 3 ON");

       } else if (switch3State == 1) {
         switch3TopButton.setBackground(Color.red);
         switch3BottomButton.setBackground(Color.darkGray);
         switch3State = 0;
         System.out.println("Switch 3 OFF");

       }
         }
    	 //this is for switch 4
     } else if (e.getSource() == switch4TopButton || e.getSource() == switch4BottomButton) {
         if(processor.processorState!=2){

    	 if (switch4State == 0) {
         switch4TopButton.setBackground(Color.darkGray);
         switch4BottomButton.setBackground(Color.green);
         switch4State = 1;
         System.out.println("Switch 4 ON");

       } else if (switch4State == 1) {
         switch4TopButton.setBackground(Color.red);
         switch4BottomButton.setBackground(Color.darkGray);
         switch4State = 0;
         System.out.println("Switch 4 OFF");

       }
         }
     }
   }

   public static void writeLog(){
     String logName="";
	   logNumber=0;
	   if(new File("logs/").isDirectory()==false){
		   addMessage("logs/ directory not found, creating directory");
		   new File("logs/").mkdirs();
		   addMessage("logs/ created in program folder");
	   }

	   boolean exists= new File("logs/logFile"+logNumber+".txt").isFile();
	   while(exists){
       logNumber++;
		   exists= new File("logs/logFile"+logNumber+".txt").isFile();
		   logName="logFile"+logNumber+".txt";
	   }

	   logName="logFile"+logNumber+".txt";
	   try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("logs/"+logName)))){
       writer.write("N2EMU LOG FILE\n");
       writer.write("Registry:\n");
       for(int i=0; i<processor.registry.length; i++){
         writer.write("Register R"+i+": 0x"+Integer.toHexString(processor.registry[i])+"\n");
       }
       writer.write("Misc Info:\n");
       writer.write("Program Counter:"+processor.PC+"\nCurrent Line:"+processor.line+"\nProcessor State:"+processor.getProcessorState()+"\nReturn Address:"+processor.returnAddress+"\nBreak Lines:\n"+processor.getBreakLines());
       writer.write("Memory Viewed (0-15):\n");
       writer.write("0:Line:"+processor.memLines[0]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[0]])+"\n"+"1:Line:"+processor.memLines[1]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[1]])+"\n"+"2:Line:"+processor.memLines[2]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[2]])+"\n"+"3:Line:"+processor.memLines[3]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[3]])+"\n"+"4:Line:"+processor.memLines[4]+"   Value:"+Integer.toHexString(processor.memory[processor.memLines[4]])+"\n"+"5:Line:"+processor.memLines[5]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[5]])+"\n"+"6:Line:"+processor.memLines[6]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[6]])+"\n"+"7:Line:"+processor.memLines[7]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[7]])+"\n"+"8:Line:"+processor.memLines[8]+"   Value:"+Integer.toHexString(processor.memory[processor.memLines[8]])+"\n"+"9:Line:"+processor.memLines[9]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[9]])+"\n"+"10:Line:"+processor.memLines[10]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[10]])+"\n"+"11:Line:"+processor.memLines[11]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[11]])+"\n"+"12:Line:"+processor.memLines[12]+"   Value:"+Integer.toHexString(processor.memory[processor.memLines[12]])+"\n"+"13:Line:"+processor.memLines[13]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[13]])+"\n"+"14:Line:"+processor.memLines[14]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[14]])+"\n"+"15:Line:"+processor.memLines[15]+" Value:"+Integer.toHexString(processor.memory[processor.memLines[15]])+"\n");
       writer.write("Errors:\n"+getError() +"\n Messages:\n"+getMessage());
       writer.write("Original Code:\n");
       writer.write(loadFile.getTextFile()+"\n");
       writer.write("Stack Trace:\n");
       StringWriter outError = new StringWriter();
       p.printStackTrace(new PrintWriter(outError));
       String errorString = outError.toString();
       writer.write(errorString+"\n");
       writer.write("FULL MEMORY\n");
       for(int i=0; i<processor.memory.length; i++){
         writer.write("Line "+i+": 0x"+Integer.toHexString(processor.memory[i])+"\n");
       }
       writer.write("LOGFILE DONE\n");
       System.out.println(logName+" has been created in logs/ subdirectory.");

       addMessage(logName+" has been created in logs/ subdirectory");

	   }
	   catch(IOException e){
       GUI.addError("Log file not created");
		   e.printStackTrace();
	   }
   }

  public static String getFileName(){
    return filePath;
  }
  public static String getError(){
	  return errorMessage;
  }
  public static void addError(String errorM){
	  errorMessage=errorMessage+"Error at line "+processor.line+":"+errorM+"\n";
  }
  public static void addMessage(String message){
	  if(alertMessage.equals("All Messages Cleared\n")){
	  alertMessage="Message :"+message+"\n";
	  }
	  else{
	  alertMessage=alertMessage+"Message :"+message+"\n";
	  }
  }
  public static String getMessage(){
	  return alertMessage;
  }
  public static void clearMessages(){
	  errorMessage="";
	  alertMessage="All Messages Cleared\n";
  }

  public static void hexDisplayChecker(){
	  //This value is the last 4 values of the memory location, which is used for the I/O device.
	  String hexDisplayValue=new String();
	  if(Integer.toHexString(processor.memory[MEMHEX]).length()>4){
	 hexDisplayValue= Integer.toHexString(processor.memory[MEMHEX]).substring(Integer.toHexString(processor.memory[MEMHEX]).length()-4);
	 System.out.println("The value of the memory at the location of the 7-SEGMENT HEX DISPLAY is greater than what can be shown. Only the last 4 digits in HEX at Memory location: 1000 will be displayed");
	  }
	  else if(Integer.toHexString(processor.memory[MEMHEX]).length()==4){
			 hexDisplayValue= Integer.toHexString(processor.memory[MEMHEX]);

	  }
	  else if(Integer.toHexString(processor.memory[MEMHEX]).length()==3){
			 hexDisplayValue= "0" + Integer.toHexString(processor.memory[MEMHEX]);

	  }
	  else if(Integer.toHexString(processor.memory[MEMHEX]).length()==2){
			 hexDisplayValue= "00" + Integer.toHexString(processor.memory[MEMHEX]);

	  }
	  else if(Integer.toHexString(processor.memory[MEMHEX]).length()==1){
			 hexDisplayValue= "000"+ Integer.toHexString(processor.memory[MEMHEX]);

	  }
	  else if(Integer.toHexString(processor.memory[MEMHEX]).length()==0){
			 hexDisplayValue= "----";

	  }
	  sevenSegConverter(hexDisplayValue.charAt(0),1);
	  sevenSegConverter(hexDisplayValue.charAt(1),2);
	  sevenSegConverter(hexDisplayValue.charAt(2),3);
	  sevenSegConverter(hexDisplayValue.charAt(3),4);


  }
  public static void sevenSegConverter(char dispVal, int dispNum){
	  boolean segArray[]={false,false,false,false,false,false,false};
	  if(dispVal=='0'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=false;

	  }
	  else if(dispVal=='-'){
		  segArray[0]=false;
		  segArray[1]=false;
		  segArray[2]=false;
		  segArray[3]=false;
		  segArray[4]=false;
		  segArray[5]=false;
		  segArray[6]=true;
	  }
	  else if(dispVal=='1'){
		  segArray[0]=false;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=false;
		  segArray[4]=false;
		  segArray[5]=false;
		  segArray[6]=false;
	  }
	  else if(dispVal=='2'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=false;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=false;
		  segArray[6]=true;
	  }
	  else if(dispVal=='3'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=false;
		  segArray[5]=false;
		  segArray[6]=true;
	  }
	  else if(dispVal=='4'){
		  segArray[0]=false;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=false;
		  segArray[4]=false;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='5'){
		  segArray[0]=true;
		  segArray[1]=false;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=false;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='6'){
		  segArray[0]=true;
		  segArray[1]=false;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='7'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=false;
		  segArray[4]=false;
		  segArray[5]=false;
		  segArray[6]=false;
	  }
	  else if(dispVal=='8'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='9'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=false;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='A' || dispVal=='a'){
		  segArray[0]=true;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=false;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='B' || dispVal=='b'){
		  segArray[0]=false;
		  segArray[1]=false;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='C' || dispVal=='c'){
		  segArray[0]=true;
		  segArray[1]=false;
		  segArray[2]=false;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=false;
	  }
	  else if(dispVal=='D' || dispVal=='d'){
		  segArray[0]=false;
		  segArray[1]=true;
		  segArray[2]=true;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=false;
		  segArray[6]=true;
	  }
	  else if(dispVal=='E' || dispVal=='e'){
		  segArray[0]=true;
		  segArray[1]=false;
		  segArray[2]=false;
		  segArray[3]=true;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else if(dispVal=='F' || dispVal=='f'){
		  segArray[0]=true;
		  segArray[1]=false;
		  segArray[2]=false;
		  segArray[3]=false;
		  segArray[4]=true;
		  segArray[5]=true;
		  segArray[6]=true;
	  }
	  else{
		  segArray[0]=false;
		  segArray[1]=false;
		  segArray[2]=false;
		  segArray[3]=false;
		  segArray[4]=false;
		  segArray[5]=false;
		  segArray[6]=true;
	  }


	  if(dispNum==1){
		 hexDisplaySegment[0]=segArray[0];
		 hexDisplaySegment[1]=segArray[1];
		 hexDisplaySegment[2]=segArray[2];
		 hexDisplaySegment[3]=segArray[3];
		 hexDisplaySegment[4]=segArray[4];
		 hexDisplaySegment[5]=segArray[5];
		 hexDisplaySegment[6]=segArray[6];

	  }

	  if(dispNum==2){
		 hexDisplaySegment[7]=segArray[0];
		 hexDisplaySegment[8]=segArray[1];
		 hexDisplaySegment[9]=segArray[2];
		 hexDisplaySegment[10]=segArray[3];
		 hexDisplaySegment[11]=segArray[4];
		 hexDisplaySegment[12]=segArray[5];
		 hexDisplaySegment[13]=segArray[6];
	  }

	  if(dispNum==3){
		 hexDisplaySegment[14]=segArray[0];
		 hexDisplaySegment[15]=segArray[1];
		 hexDisplaySegment[16]=segArray[2];
		 hexDisplaySegment[17]=segArray[3];
		 hexDisplaySegment[18]=segArray[4];
		 hexDisplaySegment[19]=segArray[5];
		 hexDisplaySegment[20]=segArray[6];
	  }

	  if(dispNum==4){
		 hexDisplaySegment[21]=segArray[0];
		 hexDisplaySegment[22]=segArray[1];
		 hexDisplaySegment[23]=segArray[2];
		 hexDisplaySegment[24]=segArray[3];
		 hexDisplaySegment[25]=segArray[4];
		 hexDisplaySegment[26]=segArray[5];
		 hexDisplaySegment[27]=segArray[6];
	  }



  }


  public static void updateGui(){
	  registerBox.setText("            Register Box\nr0 = "+"0x"+Integer.toHexString(processor.registry[0])+"\nr1 = "+"0x"+Integer.toHexString(processor.registry[1])+"\nr2 = "+"0x"+Integer.toHexString(processor.registry[2])+"\nr3 = "+"0x"+Integer.toHexString(processor.registry[3])+"\nr4 = "+"0x"+Integer.toHexString(processor.registry[4])+"\nr5 = "+"0x"+Integer.toHexString(processor.registry[5])+"\nr6 = "+"0x"+Integer.toHexString(processor.registry[6])+"\nr7 = "+"0x"+Integer.toHexString(processor.registry[7])+"\nr8 = "+"0x"+Integer.toHexString(processor.registry[8])+"\nr9 = "+"0x"+Integer.toHexString(processor.registry[9])+"\nr10 = "+"0x"+Integer.toHexString(processor.registry[10])+"\nr11 = "+"0x"+Integer.toHexString(processor.registry[11])+"\nr12 = "+"0x"+Integer.toHexString(processor.registry[12])+"\nr13 = "+"0x"+Integer.toHexString(processor.registry[13])+"\nr14 = "+"0x"+Integer.toHexString(processor.registry[14])+"\nr15 = "+"0x"+Integer.toHexString(processor.registry[15])+"\nr16 = "+"0x"+Integer.toHexString(processor.registry[16])+"\nr17 = "+"0x"+Integer.toHexString(processor.registry[17])+"\nr18 = "+"0x"+Integer.toHexString(processor.registry[18])+"\nr19 = "+"0x"+Integer.toHexString(processor.registry[19])+"\nr20 = "+"0x"+Integer.toHexString(processor.registry[20])+"\nr21 = "+"0x"+Integer.toHexString(processor.registry[21])+"\nr22 = "+"0x"+Integer.toHexString(processor.registry[22])+"\nr23 = "+"0x"+Integer.toHexString(processor.registry[23])+"\nr24 (et) = "+"0x"+Integer.toHexString(processor.registry[24])+"\nr25 (bt) = "+"0x"+Integer.toHexString(processor.registry[25])+"\nr26 (gp) = "+"0x"+Integer.toHexString(processor.registry[26])+"\nr27 (sp) = "+"0x"+Integer.toHexString(processor.registry[27])+"\nr28 (fp) = "+"0x"+Integer.toHexString(processor.registry[28])+"\nr29 (ea) = "+"0x"+Integer.toHexString(processor.registry[29])+"\nr30 (sstatus) = "+"0x"+Integer.toHexString(processor.registry[30])+"\nr31 (ra) = "+"0x"+Integer.toHexString(processor.registry[31])+"\n");
    codeBox.setText("                                   Code Box"+"\n"+loadFile.getTextFile()+"\n");

    miscBox.setText("               Misc Box\nProgram Counter: 0x"+Integer.toHexString(processor.PC)+"\nCurrent Line: 0x"+Integer.toHexString(processor.line)+"\nProcessor State: "+processor.getProcessorState()+"\nReturn Address: 0x"+Integer.toHexString(processor.returnAddress)+"\nBreak Lines:\n"+processor.getBreakLines());

    memoryBox.setText("             Memory Box\n"+"0:Line:"+processor.memLines[0]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[0]])+"\n"+"1:Line:"+processor.memLines[1]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[1]])+"\n"+"2:Line:"+processor.memLines[2]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[2]])+"\n"+"3:Line:"+processor.memLines[3]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[3]])+"\n"+"4:Line:"+processor.memLines[4]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[4]])+"\n"+"5:Line:"+processor.memLines[5]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[5]])+"\n"+"6:Line:"+processor.memLines[6]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[6]])+"\n"+"7:Line:"+processor.memLines[7]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[7]])+"\n"+"8:Line:"+processor.memLines[8]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[8]])+"\n"+"9:Line:"+processor.memLines[9]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[9]])+"\n"+"10:Line:"+processor.memLines[10]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[10]])+"\n"+"11:Line:"+processor.memLines[11]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[11]])+"\n"+"12:Line:"+processor.memLines[12]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[12]])+"\n"+"13:Line:"+processor.memLines[13]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[13]])+"\n"+"14:Line:"+processor.memLines[14]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[14]])+"\n"+"15:Line:"+processor.memLines[15]+" Value: 0x"+Integer.toHexString(processor.memory[processor.memLines[15]])+"\n");
    errorBox.setText("Errors:\n"+getError() +"\n Messages:\n"+getMessage());
  //  threads.updateGUI=true;

    /*Section dedicated to updating the HEX DISPLAY*/
    /*MEMORY VALUES X-X are values for hex1, X-X for hex2, X-X for hex3, X-X for hex4*/
    //HOW DOES IT WORK?
    //EACH SEGMENT IS BASED ON THE BINARY 1 OR 0 OF THE VALUE OF THE MEMORY LOCATION WHERE ALL THE 7 SEGMENT DISPLAYS ARE
    //28 bits.
    //this is for hex display 1 all the way to the left
    	hexDisplayChecker();

        processor.memory[SWITCH1]=switch1State;
        processor.memory[SWITCH2]=switch2State;
        processor.memory[SWITCH3]=switch3State;
        processor.memory[SWITCH4]=switch4State;


        //hex 1 segment 0
        if(hexDisplaySegment[0]){
        hex1Segment0Display.setBackground(Color.blue);
        }
        else{
        hex1Segment0Display.setBackground(Color.lightGray);
        }
       //hex 1 segment 1
       if(hexDisplaySegment[1]){

        hex1Segment1Display.setBackground(Color.blue);
      } else{
        hex1Segment1Display.setBackground(Color.lightGray);

        }
        //hex 1 segment 2
        if(hexDisplaySegment[2]){

        hex1Segment2Display.setBackground(Color.blue);
      } else{
        hex1Segment2Display.setBackground(Color.lightGray);

      }
        //hex 1 segment 3
        if(hexDisplaySegment[3]){

        hex1Segment3Display.setBackground(Color.blue);
      } else{
        hex1Segment3Display.setBackground(Color.lightGray);

      }
        //hex 1 segment 4
        if(hexDisplaySegment[4]){

        hex1Segment4Display.setBackground(Color.blue);
      } else{
        hex1Segment4Display.setBackground(Color.lightGray);

         }
        //hex 1 segment 5
        if(hexDisplaySegment[5]){

        hex1Segment5Display.setBackground(Color.blue);
      } else{
        hex1Segment5Display.setBackground(Color.lightGray);

         }
        //hex 1 segment 6
        if(hexDisplaySegment[6]){

        hex1Segment6Display.setBackground(Color.blue);
      } else{
        hex1Segment6Display.setBackground(Color.lightGray);

         }
    //this is for hex display 2
        //hex 2 segment 0
        if(hexDisplaySegment[7]){

        hex2Segment0Display.setBackground(Color.blue);
      } else{
        hex2Segment0Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 1
        if(hexDisplaySegment[8]){

        hex2Segment1Display.setBackground(Color.blue);
      } else{
        hex2Segment1Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 2
        if(hexDisplaySegment[9]){

        hex2Segment2Display.setBackground(Color.blue);
      } else{
        hex2Segment2Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 3
        if(hexDisplaySegment[10]){

        hex2Segment3Display.setBackground(Color.blue);
      } else{
        hex2Segment3Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 4
        if(hexDisplaySegment[11]){

        hex2Segment4Display.setBackground(Color.blue);
      } else{
        hex2Segment4Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 5
        if(hexDisplaySegment[12]){

        hex2Segment5Display.setBackground(Color.blue);
      } else{
        hex2Segment5Display.setBackground(Color.lightGray);

         }
        //hex 2 segment 6
        if(hexDisplaySegment[13]){

        hex2Segment6Display.setBackground(Color.blue);
      } else{
        hex2Segment6Display.setBackground(Color.lightGray);

         }
    //this is for hex display 3
        //hex 3 segment 0
        if(hexDisplaySegment[14]){

        hex3Segment0Display.setBackground(Color.blue);
      } else{
        hex3Segment0Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 1
        if(hexDisplaySegment[15]){

        hex3Segment1Display.setBackground(Color.blue);
      } else{
        hex3Segment1Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 2
        if(hexDisplaySegment[16]){

        hex3Segment2Display.setBackground(Color.blue);
      } else{
        hex3Segment2Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 3
        if(hexDisplaySegment[17]){

        hex3Segment3Display.setBackground(Color.blue);
      } else{
        hex3Segment3Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 4
        if(hexDisplaySegment[18]){

        hex3Segment4Display.setBackground(Color.blue);
      } else{
        hex3Segment4Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 5
        if(hexDisplaySegment[19]){

        hex3Segment5Display.setBackground(Color.blue);
      } else{
        hex3Segment5Display.setBackground(Color.lightGray);

         }
        //hex 3 segment 6
        if(hexDisplaySegment[20]){

        hex3Segment6Display.setBackground(Color.blue);
      } else{
        hex3Segment6Display.setBackground(Color.lightGray);

         }
    //this is for hex display 4 all the way to the right
        //hex 4 segment 0
        if(hexDisplaySegment[21]){

        hex4Segment0Display.setBackground(Color.blue);
      } else{
        hex4Segment0Display.setBackground(Color.lightGray);

         }
        //hex 4 segment 1
        if(hexDisplaySegment[22]){

        hex4Segment1Display.setBackground(Color.blue);
      } else{
        hex4Segment1Display.setBackground(Color.lightGray);

         }
        //hex 4 segment 2
        if(hexDisplaySegment[23]){

        hex4Segment2Display.setBackground(Color.blue);
      } else{
        hex4Segment2Display.setBackground(Color.lightGray);

         }
        //hex 4 segment 3
        if(hexDisplaySegment[24]){

        hex4Segment3Display.setBackground(Color.blue);
      } else{
        hex4Segment3Display.setBackground(Color.lightGray);

         }
        //hex 4 segment 4
        if(hexDisplaySegment[25]){

        hex4Segment4Display.setBackground(Color.blue);
      } else{
        hex4Segment4Display.setBackground(Color.lightGray);

         }
        //hex 4 segment 5
        if(hexDisplaySegment[26]){

        hex4Segment5Display.setBackground(Color.blue);
      } else{
        hex4Segment5Display.setBackground(Color.lightGray);

      }
        //hex 4 segment 6
        if(hexDisplaySegment[27]){

        hex4Segment6Display.setBackground(Color.blue);
      } else{
        hex4Segment6Display.setBackground(Color.lightGray);

      }
  }
}
