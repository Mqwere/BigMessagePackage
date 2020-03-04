package Support;

import java.util.ArrayList;

import Core.Program;

public class BinaryOperations {
	
	public static int pow2(int power) {return pow(2,power);}
	
	public static int pow(int base, int power) {
		int result = 1;
		for(int i=0; i<power;i++) {result*=base;}
		return result;
	}
	
	public static int digiNo(int number) {
		return Integer.toString(number).length();
	}
	
	public static int getCharSize() {
		int base = (16/FileControler.BIT_NO),
			add = (16%FileControler.BIT_NO)>0? 1:0,
			result = base + add;
			
			//Program.sysp("charSize: "+base +" + "+add + " = " + result);
		
		return result;
	}
	
	public static int getMultiplier() {
		return pow2(FileControler.BIT_NO);
	}
	
	public static ArrayList<Byte> strToBin(String input) {
		ArrayList<Byte> internal = new ArrayList<Byte>();
		/// char has 16 bits of data... so we are gonna need ceil(16/BIT_NO) places to fill our one chara with
		int charSize = getCharSize(),
			multiplier = getMultiplier();
		for(int x=0;x<input.length();x++) {
			int value = input.charAt(x);
			for(int i=0;i<charSize;i++) {
				//Program.sysp(value+"/"+multiplier+"="+(value/multiplier)+"%"+(value%multiplier));
				internal.add((byte)(value%multiplier));
				value = value/multiplier;
			}
		}
		return internal;
	}
	
	public static String binToStr(ArrayList<Byte> array) {
		String internal = "";
		int charSize 	= getCharSize(),
			arrSize = array.size(),
			multiplier 	= getMultiplier(),
			x=0;
		//Program.sysp("binToStr - start, size: "+arrSize);
		while(x<arrSize-charSize) {
			int value = 0;
			for(int i=0; i<charSize; i++) {
				int part = array.get(x+i)*pow(multiplier,i);
				part = part>=0? part:-part;
				value += part;
			}
			value = value>=0? value:-value;
			if((char)value == '\4') break;
			internal += (char)value;
			x+=charSize;
		}
		//Program.sysp("binToStr - end");
		return internal;
	}
}
