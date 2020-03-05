package Core;

import java.util.ArrayList;

import Core.MainWindow;
import Support.FileControler;
import Support.RegisterEntry;

public class Program {
	
	public static MainWindow mainWindow;
	public static final int AREA_WIDTH 	= 440;
	public static final int LINE_LENGHT = ((AREA_WIDTH/10));
	public static int imageCharsLimit = 0;
	public static ArrayList<RegisterEntry> register = new ArrayList<>();

	public static void main(String[] args) {
		/**/
		Program.mainWindow = new MainWindow();
		FileControler.setUp();
		/**/
	}
	
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
		if(mess.length()<=LINE_LENGHT) {
			mainWindow.addToArea(mess);		
		}
		else {
			String[] messes = mess.split("\n");
			
			for(int y=0; y<messes.length;y++) {
				String message = messes[y];
				String[] pieces = message.split(" ");
				int 	 length = pieces.length, temp = 0;
				for(int i = 0; i<length;i++) {
					if(temp + pieces[i].length()>(LINE_LENGHT-offset)) {
						if(pieces[i].length()>(LINE_LENGHT-offset)) {
							if(temp!=0) mainWindow.addToArea(" ");
							for(int x=0; x<pieces[i].length();x++) {
								if(temp%(LINE_LENGHT-offset)==(LINE_LENGHT-offset)-1) {
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