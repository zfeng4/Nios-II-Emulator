package emulator;

/**
 *
 */
//package emulator;
import java.util.*;
/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */
public class processor{

	/**
	 *
	 */
	public static int compareFlag;
	 static public int instructionOutputs[]=new int[5];
	 static public int instructionInputs[]=new int[5];
	 static public int memLines[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	 static public int line;
	 //static public int instructionNumber;
	static public ArrayList<Integer> breakLines = new ArrayList<Integer>();
	static public String instructionName;
	public static int processorState;//processorState is initally 2, or, not ready
	public static String processorStateString;
	public static int PC;
	public static int returnAddress;
	public static int[] memViewed={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static int[] registry={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static int[] memory= new int[65546];
	public static boolean processComplete;
	public static int prevRegister=0;
	public static int breakPoint=0;
	private static short shortval;
	private static byte byteval;
	private static int value1;
	private static int value2;
	private static int highOrder;
	public static boolean incomplete;
	public static String debugString;
	public static StringBuilder shift= new StringBuilder("");
	public static int processorDelay;//used to determine the speed of processor runtime
	/*
IMPORTANT NOTE FOR MEMORY:
MEMORY LOCATION X-X  are dedicated to the 4 buttons
MEMORY LOCATION X-X are dedicated to the 4 switches
MEMORY LOCATION X-X are dedicated to the 7 segment hex diplay

	*/
	public processor() {
		/*
		TODO
- Custom
- Shifting using string manip
		*/


	}
	public static void initialize(){
		processorState=2;
		incomplete=true;
		getProcessorState();
		PC=0;
		line=0;
		returnAddress=0;
		instructionInputs[0]=0;
		instructionInputs[1]=0;
		instructionInputs[2]=0;
		instructionInputs[3]=0;
		instructionInputs[4]=0;
		instructionOutputs[0]=0;
		instructionOutputs[1]=0;
		instructionOutputs[2]=0;
		instructionOutputs[3]=0;
		instructionOutputs[4]=0;
		instructionName="";
		for(int i=0; i<65546; i++){
			memory[i]=0;
		}
		compareFlag=0;
		processComplete=true;
		prevRegister=0;
		breakPoint=0;
		shortval=0;
		byteval=0;
		value1=0;
		value2=0;
		normalSpeed();
		System.out.println("ProcessorInitialized");


	}


	public static void runProcessor() {
		while(true){
			while(incomplete){
			registry[0]=0;
			registry[31]=returnAddress;

			getProcessorState();


			if(processorState==1 && processComplete){
				System.out.println("Processor do an instruction");
					if(line<BehavioralCore.instructionArray.size())
					{
						InstructionInterpreter.loadInst();
					}
					else{
						System.out.println("SUCCESSFULLY EXECUTED PROGRAM\n");
						GUI.addMessage("SUCCESSFULLY EXECUTED PROGRAM/\nLINE REFERENCED OUTSIDE OF PROGRAM SCOPE\nPLEASE RESET");
						processorState=0;
						incomplete=false;
					}
				System.out.println("Processor load an instruction");
				instructionInputs=InstructionInterpreter.instructionInputs;
				instructionOutputs=InstructionInterpreter.instructionOutputs;
				instructionName=InstructionInterpreter.instructionName;

				processComplete=false;
				breakFunc(breakLines, line);
				System.out.println("processor tested for breaks");
				executeInstruction(instructionInputs, instructionOutputs, instructionName);
				System.out.println("Processor executed an instruction");
				registry[0]=0;
				registry[31]=returnAddress;

				if(line+1<BehavioralCore.instructionArray.size())
				{
					PC=PC+4;
					line=PC/4;
					}
				else{
					System.out.println("SUCCESSFULLY EXECUTED PROGRAM");
					GUI.addMessage("SUCCESSFULLY EXECUTED PROGRAM/ LINE REFERENCED OUTSIDE OF PROGRAM SCOPE\nPLEASE RESET");
					processorState=0;
					incomplete=false;
				}

				breakFunc(breakLines, line);
				registry[0]=0;
				registry[31]=returnAddress;

				memView(memLines, memory);
				GUI.updateGui();
				}
				processComplete=true;
				 try {
						Thread.sleep(processorDelay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //Wait 1 second
			}
			processorState=0;
			 try {
					Thread.sleep(processorDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //Wait 1 second
	}
	}
	/*
	 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionInputs[2] >-1 && instructionInputs[2]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length && instructionOutputs[1] >-1 && instructionOutputs[1]<registry.length &&instructionOutputs[2] >-1 && instructionOutputs[2]<registry.length)
	 {

	 }
	 else{
	 	GUI.addError("Attempted to access nonexistent register index");
	 	System.out.println("Attempted to access nonexistent register index");
		pause();
	 }
	  */
	private static void executeInstruction(int instructionInputs[],int instructionOutputs[],String instructionName){
		String end;
		StringBuilder zero = new StringBuilder("0");
		StringBuilder one = new StringBuilder("1");
		boolean canProceed=false;

				if(instructionName.equals("add")){
					 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
					 {
						 registry[instructionOutputs[0]]=registry[instructionInputs[0]]+registry[instructionInputs[1]];
						 prevRegister=instructionOutputs[0];
					 }
					 else{
					 	GUI.addError("Attempted to access nonexistent register index");
					 	System.out.println("Attempted to access nonexistent register index");
						pause();
					 }

				} else if(instructionName.equals( "and")){
					 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
					 {
						 registry[instructionOutputs[0]]=registry[instructionInputs[0]] & registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
					 }
					 else{
					 	GUI.addError("Attempted to access nonexistent register index");
					 	System.out.println("Attempted to access nonexistent register index");
						pause();
					 }

				} else if(instructionName.equals( "addi")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]]=registry[instructionInputs[0]]+ instructionInputs[1];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "andhi")){
				//Integer value= instructionInputs[1];
				//short shortval= value.shortValue();
				//shortString= Integer.toBinaryString(shortval);
				//orRegString= Integer.toBinaryString(registry[instructionInputs[0]]);
				//cut the orRegString in half, take the front half, concatenate with the shortString, convert back to int.

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 highOrder = highOrder(instructionInputs[1]);
						registry[instructionOutputs[0]]=registry[instructionInputs[0]] & highOrder; //modify to be halfword
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				 } else if(instructionName.equals( "andi")){

						 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
						 {
							 registry[instructionOutputs[0]]=registry[instructionInputs[0]] & instructionInputs[1];
								prevRegister=instructionOutputs[0];
						 }
						 else{
						 	GUI.addError("Attempted to access nonexistent register index");
						 	System.out.println("Attempted to access nonexistent register index");
							pause();
						 }
				 }
				/*
			CASE KEY
			0){default true
			1){ less than
			2){greater than
			3){ equal to
			4){less than or equal to
			5){ greater than or equal to
			6){ not equal to
			7){ less than instant
			8){greater than instant
			9){ equal to instant
			10){less than or equal to instant
			11){ greater than or equal to instant
			12){ not equal to instant
			13){ less than unsigned
			14){greater than unisgned
			15){ equal to unsigned
			16){less than or equal to unsigned
			17){ greater than or equal to unsigned
			18){ not equal to unsigned
			19){ less than instant unsigned
			20){greater than instant unsigned
			21){ equal to instant unsigned
			22){less than or equal to instant unsigned
			23){ greater than or equal to instant unsigned
			24){ not equal to instant unsigned
				*/
				 else if(instructionName.equals( "beq")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(3);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
					 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "bge")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(5);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
					 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "bgeu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(17);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
						}
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "bgt")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(2);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
						}
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
			} else if(instructionName.equals( "bgtu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(14);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;

							}
					 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				}else if(instructionName.equals( "ble")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
							canProceed=compare(4);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
					 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "bleu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(16);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
					 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				//make unsigned
				} else if(instructionName.equals( "blt")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(1);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
						}
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "bltu")){



				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(13);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;
							}
					}
					else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }

				} else if(instructionName.equals( "bne")){


				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 if(instructionInputs[2]>-1 && (instructionInputs[2]/4) < BehavioralCore.instructionArray.size()){
						 canProceed=compare(6);
							if(canProceed){
							PC=instructionInputs[2]-4;
							line=PC/4;			 }
					 	}
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "br")){

				 if(instructionInputs[0]>-1 && (instructionInputs[0]/4) < BehavioralCore.instructionArray.size()){
						PC= instructionInputs[0]-4;
						line=PC/4;				 }
					 else{
							GUI.addError("Attempted to access nonexistent line");
						 	System.out.println("Attempted to access nonexistent line");
							pause();
					 }
				} else if(instructionName.equals( "break")){
				processorState=0;
				breakPoint=PC+4;
				} else if(instructionName.equals( "bret")){

				 if(breakPoint>-1 && (breakPoint/4) < BehavioralCore.instructionArray.size()){
					 processorState=1;
						PC=breakPoint;
						line=PC/4;					 }
					 else{
							GUI.addError("Attempted to access nonexistent line");
						 	System.out.println("Attempted to access nonexistent line");
							pause();
					 }
				} else if(instructionName.equals( "call")){

				 if(instructionInputs[0]>-1 && (instructionInputs[0]/4) < BehavioralCore.instructionArray.size()){
					 returnAddress=PC+4;
						PC=instructionInputs[0]-4;
						line=PC/4;						 }
					 else{
							GUI.addError("Attempted to access nonexistent line");
						 	System.out.println("Attempted to access nonexistent line");
							pause();
					 }
				//InstructionInterpreter.loadInst();
				//executeInstruction(instructionInputs,instructionOutputs,instructionName);
				// move PC to label location
				} else if(instructionName.equals( "callr")){

					 if(registry[instructionInputs[0]]>-1 && (registry[instructionInputs[0]]/4) < BehavioralCore.instructionArray.size()){
							returnAddress=PC+4;
							PC=registry[instructionInputs[0]];
							line=PC/4;						 }
						 else{
								GUI.addError("Attempted to access nonexistent line");
							 	System.out.println("Attempted to access nonexistent line");
								pause();
						 }


				// move PC to label location
				} else if(instructionName.equals( "cmpeq")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(3);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpeqi")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(9);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpge")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(5);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpgei")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(11);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpgeu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						canProceed=compare(17);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpgeui")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(23);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpgt")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 	canProceed=compare(2);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmpgti")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(8);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpgtu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(14);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmpgtui")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(20);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmple")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(4);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmplei")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 canProceed=compare(10);
						if(canProceed){
						registry[instructionOutputs[0]]=1;
						}
						else{
						registry[instructionOutputs[0]]=0;
						}
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpleu")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(16);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmpleui")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(22);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmplt")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(1);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmplti")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(7);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpltu")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(13);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmpltui")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(19);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "cmpne")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(6);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
					}
				} else if(instructionName.equals( "cmpnei")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					canProceed=compare(12);
					if(canProceed){
					registry[instructionOutputs[0]]=1;
					}
					else{
					registry[instructionOutputs[0]]=0;
					}
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "custom")){
//THIS FUNCTION REMAINS UNIMPLEMENTED
/*
if c == 1
then rC â†� fN(rA, rB, A, B, C)
else Ã˜ â†� fN(rA, rB, A, B, C)
custom N, xC, xA, xB
Where xA means either general purpose register rA,
or custom register cA.
custom 0, c6, r7, r8
*/
				prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "div")){
				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=registry[instructionInputs[0]]/registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "divu")){
				//https){//docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#divideUnsigned-int-int-

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]] = Integer.divideUnsigned(registry[instructionInputs[0]],registry[instructionInputs[1]]);
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "eret")){
// make error return messages.
/*
status â†� estatus
PC â†� ea
*/
				} else if(instructionName.equals( "jmp")){
				 if(instructionInputs[0] >-1 && instructionInputs[0] < registry.length)
				 {
					 if(registry[instructionInputs[0]]>-1 && (registry[instructionInputs[0]]/4) < BehavioralCore.instructionArray.size()){
						PC=registry[instructionInputs[0]]-4;
					 }
					 else{
							GUI.addError("Attempted to access nonexistent line");
						 	System.out.println("Attempted to access nonexistent line");
							pause();
					 }
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "jmpi")){
				 if(instructionInputs[0]>-1 && (instructionInputs[0]/4) < BehavioralCore.instructionArray.size()){
						PC=instructionInputs[0]-4;
						line=PC/4;
					 }
					 else{
							GUI.addError("Attempted to access nonexistent line");
						 	System.out.println("Attempted to access nonexistent line");
							pause();
					 }
				} else if(instructionName.equals( "ldb")){

					if(instructionInputs[1] >-1 && instructionInputs[1] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
					 {
						byteval= ((Integer)instructionInputs[0]).byteValue();
						if(byteval+registry[instructionInputs[1]]>-1 && byteval+registry[instructionInputs[1]]< memory.length){
							registry[instructionOutputs[0]]=memory[byteval+registry[instructionInputs[1]]];
							prevRegister=instructionOutputs[0];
						}
						else{
							GUI.addError("Attempted to load location outside of memory");
							System.out.println("Attempted to load location outside of memory");
							pause();
						}					 }
					 else{
					 	GUI.addError("Attempted to access nonexistent register index");
					 	System.out.println("Attempted to access nonexistent register index");
						pause();
					 }
				} else if(instructionName.equals( "ldbio")){ //IO NOT SUPPORTED
				//byteval= ((Integer)instructionInputs[0]).byteValue();
				//registry[instructionOutputs[0]]=memory[byteval+registry[instructionInputs[1]]];
				//prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "ldbu")){

				if(instructionInputs[1] >-1 && instructionInputs[1] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					value1=(int)Integer.toUnsignedLong(byteval);
					value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
					if(value1+value2>-1 && value1+value2< memory.length){
						registry[instructionOutputs[0]]=memory[value1+value2];
						prevRegister=instructionOutputs[0];
					}
					else{
						GUI.addError("Attempted to load location outside of memory");
						System.out.println("Attempted to load location outside of memory");
						pause();
					}
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ldbuio")){ //IO NOT SUPPORTED
	//			byteval= ((Integer)instructionInputs[0]).byteValue();
		//		value1=(int)Integer.toUnsignedLong(byteval);
			//  value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
				//registry[instructionOutputs[0]]=memory[value1+value2];
			//	prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "ldh")){

				if(instructionInputs[1] >-1 && instructionInputs[1] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					shortval= ((Integer)instructionInputs[0]).shortValue();
					if(shortval+registry[instructionInputs[1]]>-1 && shortval+registry[instructionInputs[1]]< memory.length){
						registry[instructionOutputs[0]]=memory[shortval+registry[instructionInputs[1]]];
						prevRegister=instructionOutputs[0];
					}
					else{
						GUI.addError("Attempted to load location outside of memory");
						System.out.println("Attempted to load location outside of memory");
						pause();
					}
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ldhio")){ //IO NOT SUPPORTED
				//shortval= ((Integer)instructionInputs[0]).shortValue();
				//registry[instructionOutputs[0]]=memory[shortval+registry[instructionInputs[1]]];
				//prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "ldhu")){

				if(instructionInputs[1] >-1 && instructionInputs[1] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					shortval= ((Integer)instructionInputs[0]).shortValue();
					value1=(int)Integer.toUnsignedLong(shortval);
	 			    value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
					if(value1+value2>-1 && value1+value2< memory.length){
						registry[instructionOutputs[0]]=memory[value1+value2];
						prevRegister=instructionOutputs[0];
					}
					else{
						GUI.addError("Attempted to load from location outside of memory");
						System.out.println("Attempted to load from location outside of memory");
						pause();
					}
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ldhuio")){ //IO NOT SUPPORTED
				//shortval= ((Integer)instructionInputs[0]).shortValue();
				//value1=(int)Integer.toUnsignedLong(shortval);
			  //value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
				//registry[instructionOutputs[0]]=memory[value1+value2];
				//prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "ldw")){

					if(instructionInputs[1] >-1 && instructionInputs[1] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
					 {
						if(instructionInputs[0]+registry[instructionInputs[1]]>-1 && instructionInputs[0]+registry[instructionInputs[1]]< memory.length){
							registry[instructionOutputs[0]]=memory[instructionInputs[0]+registry[instructionInputs[1]]];
							prevRegister=instructionOutputs[0];
						}
						else{
							GUI.addError("Attempted to load location outside of memory");
							System.out.println("Attempted to load location outside of memory");
							pause();
						}
						}
					 else{
					 	GUI.addError("Attempted to access nonexistent register index");
					 	System.out.println("Attempted to access nonexistent register index");
						pause();
					 }
				} else if(instructionName.equals( "ldwio")){ //IO NOT SUPPORTED
				//registry[instructionOutputs[0]]=memory[instructionInputs[0]+registry[instructionInputs[1]]];
				//prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "mov")){

				if(instructionInputs[0] >-1 && instructionInputs[0] < registry.length && instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					registry[instructionOutputs[0]]=registry[instructionInputs[0]];
					prevRegister=instructionOutputs[0];
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "movhi")){
				//shortval= ((Integer)instructionInputs[0]).shortValue();

				if(instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					highOrder = highOrder(instructionInputs[0]);
					registry[instructionOutputs[0]]=highOrder;
					prevRegister=instructionOutputs[0];
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "movi")){

				if(instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					registry[instructionOutputs[0]]=instructionInputs[0];
					prevRegister=instructionOutputs[0];
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "movia")){

				if(instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					registry[instructionOutputs[0]]=instructionInputs[0];
					prevRegister=instructionOutputs[0];
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "movui")){

				if(instructionOutputs[0] >-1 && instructionOutputs[0] < registry.length)
				 {
					value1=(int)Integer.toUnsignedLong(instructionInputs[0]);
					registry[instructionOutputs[0]]=value1;
					prevRegister=instructionOutputs[0];
					}
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "mul")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=registry[instructionInputs[0]]*registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "muli")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]]=registry[instructionInputs[0]]*instructionInputs[1];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "mulxss")){
				//make longs, take top 32 bits
				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {

					 registry[instructionOutputs[0]]=takeHigh(registry[instructionInputs[0]]*registry[instructionInputs[1]]);;

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "mulxsu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
						registry[instructionOutputs[0]]=takeHigh(registry[instructionInputs[0]]*value2);
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "mulxuu")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
						value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
						registry[instructionOutputs[0]]=takeHigh(value1*value2);
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "nextpc")){

				 if(instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=PC+4;
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "nop")){

				} else if(instructionName.equals( "nor")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]]= ~(registry[instructionInputs[0]] | registry[instructionInputs[1]]);
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "or")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] | registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "orhi")){
				//shortval= ((Integer)instructionInputs[1]).shortValue();
				//shortString= Integer.toBinaryString(shortval);
				//orRegString= Integer.toBinaryString(registry[instructionInputs[0]]);
				//cut the orRegString in half, take the front half, concatenate with the shortString, convert back to int.

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 highOrder = highOrder(instructionInputs[1]);
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] | highOrder;
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ori")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] | instructionInputs[1];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "rdctl")){
					//registry[instructionOutputs[0]]=registry[31];//change to control registry
			///	prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "rdprs")){


				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length && prevRegister>-1 && prevRegister<registry.length)
				 {
						registry[instructionOutputs[0]]=registry[prevRegister]+instructionInputs[0];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ret")){
				PC=returnAddress-4;
				line=PC/4;
				} else if(instructionName.equals( "rol")){
				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=Integer.rotateLeft(registry[instructionInputs[0]],registry[instructionInputs[1]]);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "roli")){
				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=Integer.rotateLeft(registry[instructionInputs[0]],instructionInputs[1]);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "ror")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]]=Integer.rotateRight(registry[instructionInputs[0]],registry[instructionInputs[1]]);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "sll")){ //sll r1, r2, r3

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					getShift();
					 for(int i = 0; i < registry[instructionInputs[1]]; i++){
							shift.deleteCharAt(0);
							shift = shift.append(zero);
						}
						end = "0b" + shift.toString();
						registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "slli")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						getShift();
						for(int i = 0; i < instructionInputs[1]; i++){
							shift.deleteCharAt(0);
							shift = shift.append(zero); // 1111 <- 0
						}
						end = "0b" + shift.toString();
						registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "sra")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					getShift();
					for(int i = 0; i < registry[instructionInputs[1]]; i++){
						if(shift.charAt(0) == '1'){
							shift.deleteCharAt(shift.length() - 1);
							shift = one.append(shift); // 1 -> 1111
						}
						else if(shift.charAt(0) == '0'){
							shift.deleteCharAt(shift.length() - 1);
							shift = zero.append(shift); // 0 -> 1111
						}
					}
					end = "0b" + shift.toString();
					registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
					prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "srai")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						getShift();
						for(int i = 0; i < instructionInputs[1]; i++){
							if(shift.charAt(0) == '1'){
								shift.deleteCharAt(shift.length() - 1);
								shift = one.append(shift); // 1 -> 1111
							}
							else if(shift.charAt(0) == '0'){
								shift.deleteCharAt(shift.length() - 1);
								shift = zero.append(shift); // 0 -> 1111
							}
						}
						end = "0b" + shift.toString();
						registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "srl")){

				if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					getShift();
					for(int i = 0; i < registry[instructionInputs[1]]; i++){
						shift.deleteCharAt(shift.length() - 1);
						shift = zero.append(shift); // 0 -> 1111
					}
					end = "0b" + shift.toString();
					registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
					prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "srli")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						getShift();
						for(int i = 0; i < instructionInputs[1]; i++){
							shift.deleteCharAt(shift.length() - 1);
							shift = zero.append(shift); // 0 -> 1111
						}
						end = "0b" + shift.toString();
						registry[instructionOutputs[0]] = InstructionInterpreter.convertImmediate(end);
						prevRegister=instructionOutputs[0];

				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "stb")){

					 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[1] >-1 && instructionOutputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						byteval= ((Integer)registry[instructionInputs[0]]).byteValue();
						if(instructionOutputs[0]+registry[instructionOutputs[1]]>-1 && instructionOutputs[0]+registry[instructionOutputs[1]]< memory.length){
							memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= byteval;
						}
						else{
							GUI.addError("Attempted to store to location outside of memory");
							System.out.println("Attempted to store to location outside of memory");
							pause();
						}
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
			} else if(instructionName.equals( "stbio")){ //IO NOT SUPPORTED
			//	byteval= ((Integer)registry[instructionInputs[0]]).byteValue();
				//memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= byteval;
				} else if(instructionName.equals( "sth")){

					 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[1] >-1 && instructionOutputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						shortval= ((Integer)registry[instructionInputs[0]]).shortValue();
						if(instructionOutputs[0]+registry[instructionOutputs[1]]>-1 && instructionOutputs[0]+registry[instructionOutputs[1]]< memory.length){
							memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= shortval;
						}
						else{
							GUI.addError("Attempted to store to location outside of memory");
							System.out.println("Attempted to store to location outside of memory");
							pause();
						}
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
			} else if(instructionName.equals( "sthio")){ //IO NOT SUPPORTED
				//shortval= ((Integer)registry[instructionInputs[0]]).shortValue();
				//memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= shortval;
				} else if(instructionName.equals("stw")){

					 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[1] >-1 && instructionOutputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
					 {
						 if(instructionOutputs[0]+registry[instructionOutputs[1]]>-1 && instructionOutputs[0]+registry[instructionOutputs[1]]< memory.length){
								memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= registry[instructionInputs[0]];
								System.out.println(Integer.toHexString(memory[instructionOutputs[0]+registry[instructionOutputs[1]]]) + "VALUE OF STW AT "+instructionOutputs[0]+registry[instructionOutputs[1]]);
								System.out.println("Memory Index is "+instructionOutputs[0] +"+ REGISTER"+instructionOutputs[1]+" "+registry[instructionOutputs[1]]);
						 }
							else{
								GUI.addError("Attempted to store to location outside of memory");
								System.out.println("Attempted to store to location outside of memory");
								pause();
							}
					 }
					 else{
					 	GUI.addError("Attempted to access nonexistent register index");
					 	System.out.println("Attempted to access nonexistent register index");
						pause();
					 }
			} else if(instructionName.equals( "stwio")){ //IO NOT SUPPORTED
				//memory[instructionOutputs[0]+registry[instructionOutputs[1]]]= registry[instructionInputs[0]];
				} else if(instructionName.equals( "sub")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]=registry[instructionInputs[0]]-registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "subi")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
					 registry[instructionOutputs[0]]=registry[instructionInputs[0]]-instructionInputs[1];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "sync")){
//for our } else if(instructionName.equals(, same as nop?
				} else if(instructionName.equals( "trap")){
/*
Operation estatus â†� status
PIE â†� 0
U â†� 0
ea â†� PC + 4
PC â†� exception handler address
*/
				} else if(instructionName.equals( "wrctl")){
//create control register
		//		registry[31]=registry[instructionInputs[0]];
		//		prevRegister=instructionOutputs[0];
				} else if(instructionName.equals( "wrprs")){
				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && prevRegister >-1 && prevRegister<registry.length)
				 {
						registry[prevRegister]=registry[instructionInputs[0]];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }

				} else if(instructionName.equals( "xor")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionInputs[1] >-1 && instructionInputs[1]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] ^ registry[instructionInputs[1]];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }
				} else if(instructionName.equals( "xorhi")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						highOrder = highOrder(instructionInputs[1]);
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] ^ highOrder;
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }

				} else if(instructionName.equals( "xori")){

				 if(instructionInputs[0] >-1 && instructionInputs[0]<registry.length && instructionOutputs[0] >-1 && instructionOutputs[0]<registry.length)
				 {
						registry[instructionOutputs[0]]= registry[instructionInputs[0]] ^ instructionInputs[1];
						prevRegister=instructionOutputs[0];
				 }
				 else{
				 	GUI.addError("Attempted to access nonexistent register index");
				 	System.out.println("Attempted to access nonexistent register index");
					pause();
				 }

				}else{
					GUI.addError("Unrecognized instruction");
					System.out.println("Unrecognized instruction");

				}



			processComplete=true;



	}


	public static void reset(){
		if(processorState !=2){
		PC=0;
		line=PC/4;
		processorState=0;
		returnAddress=0;
		for(int i=0; i<registry.length; i++){
			registry[i]=0;
		}
		for(int i=0; i<memory.length; i++){
			memory[i]=0;
		}
		compareFlag=0;
		for(int i=0; i<instructionInputs.length; i++){
			//instructionInputs[i]=0;
		}
		for(int i=0; i<instructionOutputs.length;i++){
		//	instructionOutputs[i]=0;
		}
	//	instructionName="";
		for(int i=0; i<breakLines.size();i++){
		/*
		breakLines[i]=NULL;
		*/
		}
	//	loadFile.PushFileIntoString();
		incomplete=true;
		System.out.println("IT HAS RESET");
		}
		else{
			GUI.addError("ERROR: There is no file to run");
			System.out.println("fileerrorreset\n");

		}
	}
	public static void pause(){
		if(processorState != 2){
			processorState = 0;
		}
		else{
		GUI.addError("ERROR: There is no file to run");
		System.out.println("fileerrorpause\n");
		}
	}

	public static void runP(){
		if(processorState==2){
			GUI.addError("ERROR: There is no file to run");
			System.out.println("fileerrorrun\n");
		}
		else if(processorState==0 || processorState==1){
			processorState = 1;
			System.out.println("Processor do a run");
		}
	}
	public static String getProcessorState(){
		if(processorState==0){
			processorStateString="Ready/Paused";
		}
		if(processorState==1){
			processorStateString="Running";
		}
		if(processorState==2){
			processorStateString="Not Ready/No File Loaded";
		}
		return processorStateString;
	}
	public static void stepUp(){
		if(processorState!=2){
		if(line<BehavioralCore.instructionArray.size())
		{
			if(processorState!=2){

				InstructionInterpreter.loadInst();
				instructionInputs=InstructionInterpreter.instructionInputs;
				instructionOutputs=InstructionInterpreter.instructionOutputs;
				instructionName=InstructionInterpreter.instructionName;

				pause();
				executeInstruction(instructionInputs,instructionOutputs,instructionName);
				if(line+1<BehavioralCore.instructionArray.size()){
				registry[0]=0;
				registry[31]=returnAddress;

				PC=PC+4;
				line=PC/4;
				}
				}
		}
		else{
		System.out.println("SUCCESSFULLY EXECUTED PROGRAM, PLEASE RESET");
		GUI.addMessage("SUCCESSFULLY EXECUTED PROGRAM/ LINE REFERENCED OUTSIDE OF PROGRAM SCOPE\nPLEASE RESET");
		pause();
		}
		}
		else{
			GUI.addError("ERROR: There is no file to run");
			System.out.println("fileerrorstepup\n");
		}

	}
	private static void breakFunc(ArrayList<Integer> breakLines,int line){
		for(int i=0; i<breakLines.size(); i++){
			if(breakLines.get(i)==line){
				pause();
			}
		}
	}
	public static String getBreakLines(){
		String allBreakLines="";
		for(int i=0; i<breakLines.size();i++){
			if(i==0){
				allBreakLines=Integer.toString(breakLines.get(i));
			}
			else{
			allBreakLines=allBreakLines+","+Integer.toString(breakLines.get(i));
			}
		}

		return allBreakLines;
	}
	public static int[] memView(int memLines[],int memory[]){
		for(int i=0; i<memLines.length; i++){
			memViewed[i]=memory[memLines[i]];
		}
		return memViewed;
	}

	/*
CASE KEY
0:default true
1: less than
2:greater than
3: equal to
4:less than or equal to
5: greater than or equal to
6: not equal to
7: less than instant
8:greater than instant
9: equal to instant
10:less than or equal to instant
11: greater than or equal to instant
12: not equal to instant
13: less than unsigned
14:greater than unisgned
15: equal to unsigned
16:less than or equal to unsigned
17: greater than or equal to unsigned
18: not equal to unsigned
19: less than instant unsigned
20:greater than instant unsigned
21: equal to instant unsigned
22:less than or equal to instant unsigned
23: greater than or equal to instant unsigned
24: not equal to instant unsigned
	*/
 	private static boolean compare(int compareFlag){
	boolean canProceed=false;
		switch(compareFlag){
			case 0:
			canProceed=true;
			case 1:
			if(registry[instructionInputs[0]]<registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 2:
			if(registry[instructionInputs[0]]>registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 3:
			if(registry[instructionInputs[0]]==registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 4:
			if(registry[instructionInputs[0]]<=registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 5:
			if(registry[instructionInputs[0]]>=registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 6:
			if(registry[instructionInputs[0]]!=registry[instructionInputs[1]]){
			canProceed=true;
			break;}
			case 7:
			if(registry[instructionInputs[0]]<instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 8:
			if(registry[instructionInputs[0]]>instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 9:
			if(registry[instructionInputs[0]]==instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 10:
			if(registry[instructionInputs[0]]<=instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 11:
			if(registry[instructionInputs[0]]>=instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 12:
			if(registry[instructionInputs[0]]!=instructionInputs[1]){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 13:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
			if(value1<value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 14:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
			if(value1>value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 15:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
 			if(value1==value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 16:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
 			if(value1<=value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 17:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
 			if(value1>=value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 18:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(registry[instructionInputs[1]]);
 			if(value1!=value2){
			canProceed=true;
			break;}
			case 19:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1<value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 20:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1>value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 21:
			registry[instructionInputs[0]]=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			instructionInputs[1]=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1==value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 22:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1<=value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 23:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1>=value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}
			case 24:
			value1=(int)Integer.toUnsignedLong(registry[instructionInputs[0]]);
			value2=(int)Integer.toUnsignedLong(instructionInputs[1]);
 			if(value1!=value2){
			canProceed=true;
			break;}
			else{
			canProceed=false;
			break;}

		}
		if(canProceed==true){
		System.out.println("CAN PROCEED");
		}
		else{
		System.out.println("CANT PROCEED");
		}
		return canProceed;
	}

	public static int highOrder(int number){
		String binary = Integer.toBinaryString(number);
		for(int i = binary.length(); i < 32; i++){
			binary = "0" + binary;
		}
		StringBuilder temp = new StringBuilder(binary);
		System.out.println(temp);
		for(int i = 0; i < 16; i++){
			temp.deleteCharAt(0);
			temp.append("0");
		}
		binary = temp.toString();
		binary = "0b" + binary;
		return InstructionInterpreter.convertImmediate(binary);
	}
	public static int takeHigh(int number){
		String binary = Integer.toBinaryString(number);
		for(int i = binary.length(); i < 64; i++){
			binary = "0" + binary;
		}
		StringBuilder temp = new StringBuilder(binary);
		System.out.println(temp);
		for(int i = 32; i < 64; i++){
			temp.deleteCharAt(i);
		}
		binary = temp.toString();
		binary = "0b" + binary;
		return InstructionInterpreter.convertImmediate(binary);
	}
	public static void normalSpeed(){
		processorDelay=100;
	}
	public static void slowSpeed(){
		processorDelay=500;
	}
	public static void verySlowSpeed(){
		processorDelay=1500;
	}
	public static void fastSpeed(){
		processorDelay=17;
	}
	public static void extremeSpeed(){
		processorDelay=1;
	}

	public static void getShift(){
		if(instructionInputs[0]>-1 && instructionInputs[0]<32){
			if(registry[instructionInputs[0]]<2){
				 shift = new StringBuilder("0000000000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<4){
				 shift = new StringBuilder("000000000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<8){
				 shift = new StringBuilder("00000000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<16){
				 shift = new StringBuilder("0000000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<32){
				 shift = new StringBuilder("000000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<64){
				 shift = new StringBuilder("00000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<128){
				 shift = new StringBuilder("0000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<256){
				 shift = new StringBuilder("000000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<512){
				 shift = new StringBuilder("00000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<1024){
				 shift = new StringBuilder("0000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<2048){
				 shift = new StringBuilder("000000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096){
				 shift = new StringBuilder("00000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*2){
				 shift = new StringBuilder("0000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*4){
				 shift = new StringBuilder("000000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*8){
				 shift = new StringBuilder("00000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*16){
				 shift = new StringBuilder("0000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*32){
				 shift = new StringBuilder("000000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*64){
				 shift = new StringBuilder("00000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*128){
				 shift = new StringBuilder("0000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*256){
				 shift = new StringBuilder("000000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*512){
				 shift = new StringBuilder("00000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*1024){
				 shift = new StringBuilder("0000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*2048){
				 shift = new StringBuilder("000000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096){
				 shift = new StringBuilder("00000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*2){
				 shift = new StringBuilder("0000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*4){
				 shift = new StringBuilder("000000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*8){
				 shift = new StringBuilder("00000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*16){
				 shift = new StringBuilder("0000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*32){
				 shift = new StringBuilder("000"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*64){
				 shift = new StringBuilder("00"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*128){
				 shift = new StringBuilder("0"+Integer.toBinaryString(registry[instructionInputs[0]]));
			}
			else if(registry[instructionInputs[0]]<5096*5096*256){
				 shift = new StringBuilder(Integer.toBinaryString(registry[instructionInputs[0]]));
			}

		}
	}
}
