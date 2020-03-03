package Support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import Core.Program;

public class FileControler{
	public static final int BIT_NO = 3;
	
	private static JFileChooser 
			fileChooser = new JFileChooser(".");
	
	public static void setUp() {
		fileChooser.setFileFilter(new BMPFilter());
	}
	
	public static String getExtension(File f){
        String ext = null;
        String filename = f.getName();

        int i = filename.lastIndexOf('.');

        if(i > 0 && i < filename.length() - 1)
            ext = filename.substring(i + 1).toLowerCase();

        return ext;
    }
	
	public static void compare (ArrayList<Byte> tab1, ArrayList<Byte> tab2) {
		communicate("Initiating comparison...");
		if(tab1.size()!=tab2.size()) {
			communicate("Images have different sizes!");
		}
		else {
			int diffNo = 0;
			for(int i=0; i<tab1.size(); i++) {
				if(!tab1.get(i).equals(tab2.get(i))) diffNo++;
			}
			if(diffNo == tab1.size()) communicate("Images are completly different!");
			else
			if(diffNo>0){
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				communicate("There were "+diffNo+" differences out of "+tab1.size()+" bytes."
						+ "\nThat makes rate of difference to be "+df.format((float)diffNo*100F/(float)tab1.size())+"%");
			}
			else {
				communicate("Images are identical!");
			}
		}
	}
	
	public static void communicate(Object message) {
		Program.write("FileCTRL",message);
	}
	
	public static ArrayList<Byte> fileToByteArray(JFrame parent) {
		int choice = fileChooser.showOpenDialog(parent);
		File file;
		if(choice == JFileChooser.APPROVE_OPTION) {
			file  = fileChooser.getSelectedFile();
			Program.log("Loading "+file.getAbsolutePath());
			FileInputStream inStream;
			try {
				inStream = new FileInputStream(file);
				int content;
				ArrayList<Byte> temp = new  ArrayList<Byte>();
				while((content = inStream.read()) !=-1) {temp.add((byte)(content/*-44*/));}
				inStream.close();
				Program.log("Done. "+temp.size()+" bytes of data loaded.");				
				return temp;
			} catch (Exception e) {
				e.printStackTrace();
				Program.error("FileControler.saveToFile: "+e.toString());
				return null;
			}
		}
		else {
			Program.error("File Controler.fileToByteArray: choice is NULL");
			return null;
		}
	}
	
	public static boolean writeTo(int index, String message) {
		ArrayList<Byte> content = Program.register.get(index);
		if(content==null) {
			Program.error("Program attempted to enter a non-existant part of the register.");
			return false;
		}
		Program.register.get(index).clear();
		
		return true;
	}
	
	public static ArrayList<Byte> whiteOut(ArrayList<Byte> input){
		int i 	 = input.get(10) 
				 + input.get(11)*256 
				 + input.get(12)*256*256 
				 + input.get(13)*256*256*256,
				
			size =input.size()-64*4,
			start=i;
		
		ArrayList<Byte> temp = new ArrayList<Byte>();
		temp.addAll(input);

		while(i++<size) {
			Byte b = input.get(i);
			//System.out.print(b +"->");
			//if(start%4 == i%4) input.set(i,(byte) (b - (byte)(b%10)));
			if(start%4 == i%4) {
				b = b>0? (byte)(b-b%Math.pow(2, BIT_NO)):(byte)(b+b%Math.pow(2, BIT_NO));
				input.set(i, b);
			}
			//System.out.print(input.get(i) + "\n");
		}
		size = size-start;
		Program.log("Done. Image whitened out - "+size*Math.pow(2, BIT_NO)/256+" bytes ready to be written on.");
		return input;
	}/**/
	
	public static boolean saveToDefault(ArrayList<Byte> input) {
		return saveToFile(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\BMPile.bmp"), input);
	}
	
	public static boolean saveToFile(File file, ArrayList<Byte> input) {
		FileOutputStream inStream;
		try {
			inStream = new FileOutputStream(file);
			int content;
			for(int i=0; i<input.size();i++) {
				content = (int)input.get(i);
				inStream.write(content/*+44*/);
			}
			inStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Program.error("FileControler.saveToFile: "+e.toString());
			return false;
		}
	}
	
	public static boolean saveToFile(JFrame parent, ArrayList<Byte> input) {
		int choice = fileChooser.showSaveDialog(parent);
		File file;
		if(choice == JFileChooser.APPROVE_OPTION)
			file  = fileChooser.getSelectedFile();
		else
			file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\BMPile.bin");
		
		return saveToFile(file, input);
	}
}

class BMPFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		String ext = FileControler.getExtension(file);
		if(ext == null) 	return true;
		else if(ext.toLowerCase().equals("bmp")) return true;
		//else if(ext.toLowerCase().equals("png")) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		return "BMP";
	}
	
}
