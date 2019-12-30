package emulator;

/**
 *
 */
//package emulator;

/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */

 //we'll change these to non-star once we have everything we want in the program
import java.io.*;
import java.util.*;

public class BehavioralCore {
	String instruction;
	String command;
	int memLines[];
	int breakLines[];
	static String textFile;

	//declare arraylist to hold lines
	public static ArrayList<String> instructionArray = new ArrayList<String>();

	//Hashmap for labels
	public static HashMap<String, Integer> labels = new HashMap<>();
	/**
	 *
	 */
	public BehavioralCore() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param args
	 */
	public static void main() {
		// TODO Auto-generated method stub
	}

	public static ArrayList<String> parseFile(String textFile){
		//NOTE: current implementaion does NOT take into the fact that blank lines and LABELS on thier own line ispossible
		//temporary array to hold split lines fromtext file loaded
		String tempArray[] = textFile.split("\n");
		//get number of lines
		int numberOfLines = tempArray.length;
		//add files into arraylist from tempArray
		for(int i=0; i<numberOfLines;i++){
			instructionArray.add(tempArray[i]);
		}

		//Get address of all the LABELS
		String[] tokens;
		char labelTest;

		for(int i=0; i<numberOfLines;i++){
			tokens = tempArray[i].split("\\s+");  //Parse line based on spaces
			labelTest = tokens[0].charAt(tokens[0].length() - 1); //Get last char of first token
			if (labelTest == ':') { //Check to see if the first token's last character is a colon
				//Yes
				StringBuilder temp = new StringBuilder(tokens[0]);
	      temp.deleteCharAt(tokens[0].length() - 1); //Delete the colon from the end of the label
	      tokens[0] = temp.toString();

				labels.put(tokens[0], i*4); //word addressing => PC = line number * 4 => 0 => 4 => 8 => 12 => etc.
			}
		}

		return instructionArray;
	}
	public static String loadInstruction(ArrayList<String> instructionArray){
		System.out.println("behavioral core told to load an instruction");
		String instruction = instructionArray.get(processor.line);
		System.out.println("behavioral core loaded the instruction");
		return instruction;
	}
	public static void exeCommand(String command){
		//split commands based on \n

		//all commands: (ab lineNumber), (rb lineNumber) (rab lineNumber),(vm memoryStart)
		//split intrepret commands
		String[] splitCommandLines = command.split("\n");
		for(int i=0; i<splitCommandLines.length;i++){
		String[] splitCommand = splitCommandLines[i].split(" ");
			for(int q=1; q<splitCommand.length;q++){
			splitCommand[q]=Integer.toString(InstructionInterpreter.convertImmediate(splitCommand[q]));
			}
		//splitCommand[0] = command; splitCommand[1] = lineNumber
		if(splitCommand[0].equals("ab")){ //add break
			processor.breakLines.add(Integer.parseInt(splitCommand[1]));
			if(splitCommand.length>2){
				System.out.println("The ab command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The ab command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");

			}
		}
		else if(splitCommand[0].equals("rb")){//remove break
			processor.breakLines.remove((Integer.parseInt(splitCommand[1])));
			if(splitCommand.length>2){
				System.out.println("The rb command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The rb command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("rab")){//clear all breaks
			processor.breakLines.clear();
			if(splitCommand.length>1){
				System.out.println("The rab command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The rab command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}

		else if(splitCommand[0].equals("vm")){//view memory
			//put it in a memorybox The if statement assures that memory won't be viewed that doesn't exist
			splitCommand[2] = Integer.toString(InstructionInterpreter.convertImmediate(splitCommand[2]));
			if(Integer.parseInt(splitCommand[1])>=0 && Integer.parseInt(splitCommand[1])<=15){
				if(Integer.parseInt(splitCommand[2]) > 66546 || Integer.parseInt(splitCommand[2])<0){
					//do nothing
					System.out.println("Memory line specified is outside of memory.");
					GUI.addError("Memory line specified is outside of memory.");

				}
				else{
					//HashMap<String, Integer> memoryMap = new HashMap<>();
					//memoryMap.put()
					processor.memLines[Integer.parseInt(splitCommand[1])] = Integer.parseInt(splitCommand[2]); //vm 0-15 0xFFFF
				}
			}
			else{
				System.out.println("You can only specify 0-15 index for memory lines to specify");
				GUI.addError("You can only specify 0-15 index for memory lines to specify");
				//for(int i=0;i<15;i++){
					// if(processor.memLines[i] == 0 && memPlacedFlag == false){
					// 	processor.memLines[i] = memValue;
					// 	memPlacedFlag = true;
					// }
					// else if(i == 15 && memPlacedFlag == false){
					// 	processor.memLines =
					// }
				//}
			}

			//	else{
			//	processor.memLines[i] = (tempValue);
			//	}
			if(splitCommand.length>3){
				System.out.println("The vm command takes only two inputs after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The vm command takes only two inputs after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("clearMessages") || splitCommand[0].equals("cmes")){//view memory
			GUI.clearMessages();
			if(splitCommand.length>1){
				System.out.println("The cmes/clearMessages command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The cmes/clearMessages command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("rm")){//remove line of memory viewed at index
			//put it in a memorybox The if statement assures that memory won't be viewed that doesn't exist
			if(Integer.parseInt(splitCommand[1])>=0 && Integer.parseInt(splitCommand[1])<=15){

					//HashMap<String, Integer> memoryMap = new HashMap<>();
					//memoryMap.put()
					processor.memLines[Integer.parseInt(splitCommand[1])] = 0; //vm 0-15 0xFFFF
					GUI.addMessage("Memory line viewed at index "+splitCommand[1]+" cleared");
				 	System.out.println("Memory line viewed at index "+splitCommand[1]+" cleared");
			}
			else{
				System.out.println("You can only specify 0-15 index for memory lines to specify");
				GUI.addError("You can only specify 0-15 index for memory lines to specify");
			}

			if(splitCommand.length>2){
				System.out.println("The rm command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The rm command takes only one input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}else if(splitCommand[0].equals("ram")){//view memory
			//put it in a memorybox The if statement assures that memory won't be viewed that doesn't exist
				for(int q=0; q<processor.memLines.length; q++){
					processor.memLines[q] = 0; //removes all memory
				}
				GUI.addMessage("All selections for memory viewing cleared");
			 	System.out.println("All selections for memory viewing cleared");
			if(splitCommand.length>1){
				System.out.println("The ram command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The ram command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}else if(splitCommand[0].equals("rmLogs")){//view memory
			  GUI.logNumber=0;
			  if(new File("logs/").isDirectory()==false){
				  GUI.addMessage("/logs/ directory not found, creating directory");
				  new File("logs/").mkdirs();
				  GUI.addMessage("logs/ created in program folder");
			  }
			    for (File file: new File("logs/").listFiles()) {
					        if (!file.isDirectory())
					            file.delete();
			    }

			 GUI.logNumber=0;
			 	GUI.addMessage("All files in logs/ folder deleted");
			 	System.out.println("All files in logs/ folder deleted");

		if(splitCommand.length>1){
			System.out.println("The rmLogs command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			GUI.addMessage("The rmLogs command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
		}
		}
		else if(splitCommand[0].equals("veryslow")){
			processor.verySlowSpeed();
			System.out.println("The processor has been set to very slow speed");
			GUI.addMessage("The processor has been set to very slow speed");
			if(splitCommand.length>1){
				System.out.println("The veryslow command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The veryslow command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("slow")){
			processor.slowSpeed();
			System.out.println("The processor has been set to slow speed");
			GUI.addMessage("The processor has been set to slow speed");
			if(splitCommand.length>1){
				System.out.println("The slow command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The slow command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("normal")){
			processor.normalSpeed();
			System.out.println("The processor has been set to normal speed");
			GUI.addMessage("The processor has been set to normal speed");
			if(splitCommand.length>1){
				System.out.println("The normal command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The normal command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("fast")){
			processor.fastSpeed();
			System.out.println("The processor has been set to fast speed");
			GUI.addMessage("The processor has been set to fast speed");
			if(splitCommand.length>1){
				System.out.println("The fast command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The fast command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}
		else if(splitCommand[0].equals("extreme")){
			processor.extremeSpeed();
			System.out.println("The processor has been set to an extreme speed");
			GUI.addMessage("The processor has been set to an extreme speed");
			if(splitCommand.length>1){
				System.out.println("The extreme command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
				GUI.addMessage("The extreme command takes no input after the command name, and only one command per line. \n Further inputs are ignored.");
			}
		}

		else{
			//incorrect
			System.out.println("This isn't a real command. Valid commands are ab, rb, rab, cmes/clearMessages, vm, rm, ram, veryslow, slow, normal, fast, extreme, and rmLogs");
			GUI.addMessage("This isn't a real command. Valid commands are ab, rb, rab, cmes/clearMessages, vm, rm, ram, veryslow, slow, normal, fast, extreme, and rmLogs");
		}
		}

		//breakLines[0]= 1;
		//memLines[0]=1;
		//needs to be fed like such: command, mem number(if mem), or line number
		//this is not complete as of the night before increment 4
	//	String tempArray[] = textFile.split("\n");
	//	int tempNum=0;
		//if(tempArray[0] == "MA"){
	//		 tempNum = Integer.parseInt(tempArray[1]);
	//	}
		System.out.println(command);
	}


}
