package Support;

import java.util.ArrayList;

public class BinaryOperations {
	
	public static int getCharSize() {
		return 16/FileControler.BIT_NO + (16%FileControler.BIT_NO)>0? 1:0;
	}
	
	public static ArrayList<Byte> strToBin(String input) {
		ArrayList<Byte> internal = new ArrayList<Byte>();
		/// char has 16 bits of data... so we are gonna need ceil(16/BIT_NO) places to fill our one chara with
		int charSize = getCharSize();
		for(int x=0;x<input.length();x++) {
			int value = input.charAt(x);
			for(int i=0;i<charSize;i++) {
				internal.add((byte)(value%Math.pow(2, FileControler.BIT_NO)));
				value = value/(int)Math.pow(2, FileControler.BIT_NO);
			}
		}
		return internal;
	}
	
	public static String binToStr(ArrayList<Byte> array) {
		String internal = "";
		
		
		return internal;
	}
}
