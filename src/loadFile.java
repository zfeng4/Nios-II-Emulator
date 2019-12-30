package emulator;

/**
 *
 */
//package emulator;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.*;
/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */

public class loadFile {
public static String textFile="";
	static public void PushFileIntoString() {

	  String fullFile = "";

		try{
			if(processor.processorState!=2){
				processor.reset();

			}
			textFile="";
				BehavioralCore.instructionArray.clear();

			System.out.println(BehavioralCore.instructionArray.size()+ "numleft");

			InstructionInterpreter.instruction="";
			fullFile = new String(Files.readAllBytes(Paths.get(GUI.filePath)));
			textFile=fullFile;
			BehavioralCore.instructionArray=BehavioralCore.parseFile(textFile);
			processor.processorState=0;// processor is readied.

			System.out.println("Pushed file into string");
			GUI.addMessage("Pushed file into string");
		}
		catch(IOException e){
			GUI.addError("No file at specified path");
			e.printStackTrace();
		}

	}
	public static String getTextFile(){
		return textFile;
	}

}
