package emulator;

import java.lang.StringBuilder;
/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */
public class InstructionInterpreter {

	public static	String instruction;
	public static	String instructionName;
		//int instructionNumber;
	public static	int instructionInputs[]={0,0,0,0};
	public static	int instructionOutputs[]={0,0,0,0};
	public static	int compareFlag=0;
	public InstructionInterpreter() {


	}
	public static void loadInst(){
		System.out.println("instructionInterpreter told to load instruction");
		instruction= BehavioralCore.loadInstruction(BehavioralCore.instructionArray);
		System.out.println("instructioninterpreter load an instruction from behavioral core");
		interpret(instruction);
		System.out.println("instructionInterpreter interpreted an instruction");
	}

	public static void interpret(String instruction){
		//parse the instruction using the nios2 manual
		//tokenize instruction;

		//R type
    //No label
		//instr RD, RS, RT
		//token[0] = instr
		//token[1] = RD,
		//token[2] = RS,
		//token[3] = RT

    //label
    //label: instr RD, RS, RT
    //token[0] = label:
		//token[1] = instr
		//token[2] = RD,
		//token[3] = RS,
		//token[4] = RT

		int instructionIndex;
		String tokens[] = instruction.split("\\s+"); //Parse line based on spaces
		String label = "";
		instructionName = tokens[0];

		char labelTest = tokens[0].charAt(tokens[0].length() - 1); //Get last char of first token

		if (labelTest != ':') { //Check to see if the first token's last character is a colon
			instructionIndex = 0; //No label
		 } else{
			StringBuilder temp = new StringBuilder(tokens[0]);
			temp.deleteCharAt(tokens[0].length() - 1); //Delete the colon from the end of the label
			tokens[0] = temp.toString();

			instructionIndex = 1; //Label exists - move all variable assignments up one index
			label = tokens[0]; //label w/o color
			instructionName = tokens[instructionIndex]; //ex: add, sub, ldw, etc.
		}
		System.out.println(processor.line);
		//System.out.println(instructionName+" "+tokens[instructionIndex+1]+tokens[instructionIndex+2]+tokens[instructionIndex+3]);
		System.out.println(instructionName);
			if(instructionName.equals("add")){ //add RD, RS, RT
				System.out.println(instructionName+" add");
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

				// System.out.println(instructionOutputs[0]);
				// System.out.println(instructionInputs[0]);
				// System.out.println(instructionInputs[1]);
			}
			 else if(instructionName.equals("and")){ //and RD, RS, RT
				System.out.println(instructionName+" and");

      instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("addi")){ //addi RD, RS, immd
				System.out.println(instructionName+" addi");

      instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

      System.out.println(instructionOutputs[0]);
      System.out.println(instructionInputs[0]);
      System.out.println(instructionInputs[1]);

			} else if(instructionName.equals("andhi")){
      instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("andi")){
      instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("beq")){ //beq RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
			label = tokens[instructionIndex + 3];
      instructionInputs[2] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("bge")){ //bge RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("bgeu")){ //bgeu RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("bgt")){ //bgt RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

		} else if(instructionName.equals("bgtu")){ //bgtu RS, RT, label
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
			label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

      } else if(instructionName.equals("ble")){ //ble RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

      } else if(instructionName.equals("bleu")){ //bleu RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

      } else if(instructionName.equals("blt")){ //blt RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

    	} else if(instructionName.equals("bltu")){ //bltu RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

      } else if(instructionName.equals("bne")){ //bne RS, RT, label
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 2]); //RT
      label = tokens[instructionIndex + 3];
			instructionInputs[2] = BehavioralCore.labels.get(label);

      } else if(instructionName.equals("br")){ //br label
      label = tokens[instructionIndex + 1];
			instructionInputs[0] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("break")){
      //Do nothing?

			} else if(instructionName.equals("bret")){
      //Do nothing?

			} else if(instructionName.equals("call")){ //call label
      label = tokens[instructionIndex + 1];
			instructionInputs[0] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("callr")){ //call RS
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]);

			} else if(instructionName.equals("cmpeq")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpeqi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpge")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpgei")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpgeu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpgeui")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpgt")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpgti")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpgtu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpgtui")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmple")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmplei")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpleu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpleui")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmplt")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmplti")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpltu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpltui")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("cmpne")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("cmpnei")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("custom")){ //TODO
			} else if(instructionName.equals("div")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("divu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("eret")){ //NOP
			} else if(instructionName.equals("jmp")){
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]); //RS

			} else if(instructionName.equals("jmpi")){
			instructionInputs[0] = convertImmediate(tokens[instructionIndex + 1]); //immediate

//Ex)){ ldw r4, x(r3)
//r4 - output
//x and r3 - inputs

			} else if(instructionName.equals("ldb")){ //ldb RD, imm(RS)
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionInputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionInputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("ldbio")){ //IO NOT SUPPORTED

			} else if(instructionName.equals("ldbu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionInputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionInputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("ldbuio")){ //IO NOT SUPPORTED

			} else if(instructionName.equals("ldh")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionInputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionInputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("ldhio")){ //IO NOT SUPPORTED

			} else if(instructionName.equals("ldhu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionInputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionInputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("ldhuio")){ //IO NOT SUPPORTED

			} else if(instructionName.equals("ldw")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionInputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionInputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			//Ex)){ ldw r4, x(r3)
			//r4 - output
			//x and r3 - inputs
			} else if(instructionName.equals("ldwio")){ //IO NOT SUPPORTED

			} else if(instructionName.equals("mov")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS

			} else if(instructionName.equals("movhi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertImmediate(tokens[instructionIndex + 2]); //immediate

			} else if(instructionName.equals("movi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertImmediate(tokens[instructionIndex + 2]); //immediate

			} else if(instructionName.equals("movia")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			label = tokens[instructionIndex + 2];
			instructionInputs[0] = BehavioralCore.labels.get(label);

			} else if(instructionName.equals("movui")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertImmediate(tokens[instructionIndex + 2]); //immediate

			} else if(instructionName.equals("mul")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("muli")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("mulxss")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("mulxsu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("mulxuu")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("nextpc")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD

			} else if(instructionName.equals("nop")){
			//nothing
			} else if(instructionName.equals("nor")){
			      instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			      instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT
			} else if(instructionName.equals("or")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("orhi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("ori")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("rdctl")){ //control register always R31??
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD

			} else if(instructionName.equals("rdprs")){ //rdprs rB, rA, IMM16
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			//rA not needed?
			instructionInputs[0] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("ret")){
			//nothing

			} else if(instructionName.equals("rol")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("roli")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("ror")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("sll")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("slli")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("sra")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("srai")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("srl")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("srli")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("stb")){
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionOutputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionOutputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("stbio")){

			} else if(instructionName.equals("sth")){
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionOutputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionOutputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("sthio")){

			} else if(instructionName.equals("stw")){
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 1]);
			instructionOutputs[0] = convertImmediate(separateImmediate(tokens[instructionIndex + 2]));
			instructionOutputs[1] = convertRegister(separateRegister(tokens[instructionIndex + 2]));

			} else if(instructionName.equals("stwio")){

			//Ex)){ stw r4, x(r3)
			//r4 - input
			//x and r3 - outputs

			} else if(instructionName.equals("sub")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("subi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("sync")){ //NOP
			} else if(instructionName.equals("trap")){ //NOP?
			} else if(instructionName.equals("wrctl")){ //wrctl ctlN, rA
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS

			} else if(instructionName.equals("wrprs")){
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS

			} else if(instructionName.equals("xor")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
			instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
			instructionInputs[1] = convertRegister(tokens[instructionIndex + 3]); //RT

			} else if(instructionName.equals("xorhi")){
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

			} else if(instructionName.equals("xori")){ //xori r3, r2, r1
			instructionOutputs[0] = convertRegister(tokens[instructionIndex + 1]); //RD
      instructionInputs[0] = convertRegister(tokens[instructionIndex + 2]); //RS
      instructionInputs[1] = convertImmediate(tokens[instructionIndex + 3]); //immediate

      } else{compareFlag=0;}


	}
	public static int convertRegister(String register){ //Turns a register into its number counterpart
		int num;
		char commaTest = register.charAt(register.length() - 1); //Get last char of first token
		StringBuilder temp = new StringBuilder(register);

		if(commaTest == ','){
			temp.deleteCharAt(register.length() - 1); //Delete the comma from the end of the register
		}

		if(temp.toString().equals("et")){
			return 24;
		}
		if(temp.toString().equals("bt")){
			return 25;
		}
		if(temp.toString().equals("gp")){
			return 26;
		}
		if(temp.toString().equals("sp")){
			return 27;
		}
		if(temp.toString().equals("fp")){
			return 28;
		}
		if(temp.toString().equals("ea")){
			return 29;
		}
		if(temp.toString().equals("sstatus")){
			return 30;
		}
		if(temp.toString().equals("ra")){
			return 31;
		}

		temp.deleteCharAt(0); //Delete R or r from RX
		register = temp.toString(); //Convert back to string
		num =   Integer.parseInt(register);

		return num;

	}

	public static int convertImmediate(String immediate){
	    int sum = 0, pow = 0;

	    if(immediate.length() > 1){
	      char numberBase = immediate.charAt(1);
	      //StringBuilder temp = new StringBuilder(immediate);

	      if(numberBase == 'x' || numberBase == 'X'){ //HEX 0x11
	        for (int i = immediate.length() - 1; i > 1 ; i--) {
	          sum += (convertHex(immediate.charAt(i)))*(Math.pow(16, pow));
	          pow++;
	        }
	        return sum;
	      } else if(numberBase == 'b' || numberBase == 'B'){ //Binary 0b00010001
	        for (int i = immediate.length() - 1; i > 1 ; i--) {
	          sum += (Character.getNumericValue(immediate.charAt(i)))*(Math.pow(2, pow));
	          pow++;
	        }
	        return sum;
	       } else{ //Decimal
	        return Integer.parseInt(immediate);
	      }
	    } else{
	      return Integer.parseInt(immediate);
	    }
	  }

		public static String separateImmediate(String string){
			//x(RD)
			//Want x
			String immediate;
			StringBuilder temp = new StringBuilder(string);
			temp.deleteCharAt(temp.length() - 1);
			temp.deleteCharAt(temp.length() - 1);
			temp.deleteCharAt(temp.length() - 1);
			temp.deleteCharAt(temp.length() - 1);
			//x
			if(temp.length() == 0){
				return "0";
			} else{
				immediate = temp.toString();
				return immediate;
			}
		}

		public static String separateRegister(String string){
			//x(RD)
			//Want RD
			StringBuilder temp;
			String register;
			String[] tokens;
			if(string.charAt(0) == '('){
				temp = new StringBuilder(string);
				temp.deleteCharAt(0);
				temp.deleteCharAt(temp.length() - 1);
				register = temp.toString();
			}else{
				tokens = string.split("\\s(");
				temp = new StringBuilder(tokens[1]);
				temp.deleteCharAt(temp.length() - 1);
				register = temp.toString();
			}

			return register;
		}


	//Extensive Error checking to think of:
	//-Instruction does not exist
	//-Instruction has too many arguments
	//-instruction has too few arguments
	//-register does not exist
	//-memory out of bounds
	//-arugment is immediate;requires register
	//-argument is register; requires immediate

	//public void errorInstructionDoesNotExist(){
	//	currentLine = processor.line;


	public static int convertHex(char hex){
		if(hex == '0'){
			return 0;
		}
		if(hex == '1'){
			return 1;
		}
		if(hex == '2'){
			return 2;
		}
		if(hex == '3'){
			return 3;
		}
		if(hex == '4'){
			return 4;
		}
		if(hex == '5'){
			return 5;
		}
		if(hex == '6'){
			return 6;
		}
		if(hex == '7'){
			return 7;
		}
		if(hex == '8'){
			return 8;
		}
		if(hex == '9'){
			return 9;
		}
		if(hex == 'A' || hex == 'a'){
			return 10;
		}
		if(hex == 'B'|| hex == 'b'){
			return 11;
		}
		if(hex == 'C'|| hex == 'c'){
			return 12;
		}
		if(hex == 'D'|| hex == 'd'){
			return 13;
		}
		if(hex == 'E'|| hex == 'e'){
			return 14;
		}
		if(hex == 'F'|| hex == 'f'){
			return 15;
		}

		return -1; //Error
	}


}
