package Core;

import java.util.ArrayList;

import Core.MainWindow;
import Support.FileControler;
import Support.TestManager;
import Support.Entities.RegisterEntry;

public class Program {
	
	public static boolean LOG_ALL=true;
	
	public static MainWindow mainWindow = null;
	public static final int AREA_WIDTH 	= 440;
	public static final String DEFAULT_FILE= "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\BMPFile";
	public static final int LINE_LENGTH = ((AREA_WIDTH/10));
	public static int imageCharsLimit = 0;
	public static int filesNo = 0;
	public static ArrayList<RegisterEntry> register = new ArrayList<>();

	public static void main(String[] args) {
		
		TestManager.trimmingTest();
		
		//TestManager.signsTest(5000,5000);
		
		//TestManager.payloadTest(100000);
		
		/*/
		Program.mainWindow = new MainWindow();
		FileControler.setUp();
		/**/
	}
	
	public static String getNewFileLoc() {return getNewFileLoc("png");}
	
	public static String getNewFileLoc(String ext) {return DEFAULT_FILE+ (++filesNo) +"."+ext;}
	
	public static RegisterEntry getLastImage(){
		return register.get(Program.register.size()-1);
	}
	
	public static void sleep(long milis) {try {Thread.sleep(milis);} catch (InterruptedException e) {Program.error(e);}}
	
	public static void sysp(Object message) {
		String out;
		if(message.getClass() == String.class) {
			out = (String) message;
		}
		else out = message.toString();

		System.out.println(out);
	}
	
	public static void sysLog(Object message) {
		String out;
		if(message.getClass() == String.class) {
			out = (String) message;
		}
		else out = message.toString();
		if(LOG_ALL) System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
				+ "\n"+out);
	}
	
	public static void error(Object message) {
		String output = new String();
		if(message.getClass() == String.class) {
			String obj = (String)message;
			output = obj;
		}
		else
		if(message.getClass() == Boolean.class) {
			Boolean obj = (Boolean) message;
			output = Boolean.toString(obj);
		}
		else {
			output = message.toString();
		}
		output = output.toUpperCase();
		write("ERROR",output);
	}
	
	public static void print(Object message) {
		if(message.getClass() == String.class) {
			String obj = (String)message;
			mainWindow.addToArea(obj);
		}
		else {
			mainWindow.addToArea(message.toString());
		}
	}
	
	public static void sys(Object input) {
		write("SYSTEM", input);
	}
	
	public static void log(Object input) {
		write("LOG", input);
	}
	
	public static void write(Object input) {
		write("",input);
	}

	public static void write(String from, Object input) {
		String mess;
		String tab = new String("");
		if(input.getClass()==String.class) 
				mess = (String)input;
		else 	mess = input.toString();
		
		int offset = 4 + from.length();
		if(from.length()>0) {
			mainWindow.addToArea("["+from+"]: ");
		}
		else {
			offset = 1;
			mainWindow.addToArea(" "); 
		}
		
		for(int i=0; i<offset;i++) tab+=" ";
		if(mess.length()<=LINE_LENGTH) {
			mainWindow.addToArea(mess);		
		}
		else {
			String[] messes = mess.split("\n");
			
			for(int y=0; y<messes.length;y++) {
				String message = messes[y];
				String[] pieces = message.split(" ");
				int 	 length = pieces.length, temp = 0;
				for(int i = 0; i<length;i++) {
					if(temp + pieces[i].length()>(LINE_LENGTH-offset)) {
						if(pieces[i].length()>(LINE_LENGTH-offset)) {
							if(temp!=0) mainWindow.addToArea(" ");
							for(int x=0; x<pieces[i].length();x++) {
								if(temp%(LINE_LENGTH-offset)==(LINE_LENGTH-offset)-1) {
									mainWindow.addToArea("-"+"\n"+tab);
									temp = 0;
								}
								mainWindow.addToArea(pieces[i].charAt(x));
								temp++;
							}
						}
						else {
							mainWindow.addToArea("\n"+tab+pieces[i]);
							temp = pieces[i].length();
						}
					}
					else {
						if(temp!=0) mainWindow.addToArea(" ");
						mainWindow.addToArea(pieces[i]);
						temp += pieces[i].length()+1;
					}
				}
				mainWindow.addToArea("\n"+tab);
			}
		}
		mainWindow.addToArea("\n");
	}
}