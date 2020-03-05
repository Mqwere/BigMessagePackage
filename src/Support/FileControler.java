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
	
	public static void compare (RegisterEntry tab1, RegisterEntry tab2) {
		communicate("Initiating comparison...");
		if(tab1.size()!=tab2.size()) {
			communicate("Files have different sizes!");
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
	
	public static RegisterEntry fileToRegister(JFrame parent) {
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
				RegisterEntry output = new RegisterEntry(FileControler.getExtension(file),temp);
				return output;
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
	
	public static boolean saveToDefault(RegisterEntry input) {
		return saveToFile(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\BMPile.bmp"), input);
	}
	
	public static boolean saveToFile(File file, RegisterEntry input) {
		FileOutputStream inStream;
		try {
			inStream = new FileOutputStream(file);
			int content;
			for(int i=0; i<input.size();i++) {
				content = (int)input.get(i);
				inStream.write(content/*+44*/);
			}
			inStream.close();
			Program.log("File saved succesfully.\n("+file.getPath()+")");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Program.error("FileControler.saveToFile: "+e.toString());
			return false;
		}
	}
	
	public static boolean saveToFile(JFrame parent, RegisterEntry input) {
		int choice = fileChooser.showSaveDialog(parent);
		File file;
		if(choice == JFileChooser.APPROVE_OPTION)
			file  = fileChooser.getSelectedFile();
		else
			file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\BMPile.bmp");
		
		return saveToFile(file, input);
	}
}

class BMPFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		String ext = FileControler.getExtension(file);
		if(ext == null) 	return true;
		else if(ext.toLowerCase().equals(FileType.BMP.extension)||ext.toLowerCase().equals(FileType.PNG.extension)) return true;
		//else if(ext.toLowerCase().equals("png")) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		return "Applicable Image Files";
	}
	
}
