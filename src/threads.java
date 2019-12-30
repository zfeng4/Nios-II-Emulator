package emulator;

/**
 *
 */
//package emulator;

import java.lang.Thread;
/**
* @author Tyler Zinsmaster, Dennis Feng, Bryan Borer, Alex Chmelka, Dominic Hezel
 *
 */

public class threads extends Thread {

     //threads
     private Thread processorThread;
     //private String pThread= "processorThread";
     private Thread GUIThread;
    // private String gThread= "guiThread";

     private String threadName;
     public static boolean updateGUI = true;
     threads(String name){
       threadName=name;
     }
     public void run(){
      if(threadName.equals("processorThread")){
          processor.runProcessor();
      }
      else if(threadName.equals("guiThread")){
        while(true){
    //      if(updateGUI){
            GUI.updateGui();
//            updateGUI = false;
  //        }
            try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //Wait 1 second

        }
      }
     }
    public void start(){
      if(threadName.equals("processorThread")){
      System.out.println("Starting processor");
      processorThread= new Thread(this, threadName);
      processorThread.start();
      }
      if(threadName.equals("guiThread")){
  	  System.out.println("Starting GUI");
      GUIThread= new Thread(this, threadName);
      GUIThread.start();
      }
  //    System.out.println("Starting loadFile");
  //    loadFileThread= new Thread(this, lThread);
  //    loadFileThread.start();
  //    System.out.println("Starting BehavioralCore");
  //    BehavioralCoreThread= new Thread(this, bThread);
  //    BehavioralCoreThread.start();
  //    System.out.println("Starting instructionInterpreter");
  //    instructionInterpreterThread= new Thread(this, iThread);
  //    instructionInterpreterThread.start();
    }
}
