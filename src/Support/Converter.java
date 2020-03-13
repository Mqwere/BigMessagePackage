package Support;

import java.util.ArrayList;

import Core.Program;

public class Converter {
	
	public static ArrayList<Byte> subList(ArrayList<Byte> input, int begin, int end){
		end = end>input.size()? input.size():end;
		end = end<0? input.size()+end:end;
		Program.sysLog("subList:  begin: "+begin+" end: "+end);
		ArrayList<Byte> output = new ArrayList<>();
		for(;begin<end;begin++) {output.add(input.get(begin));}
		return output;
	}
	
	public static ArrayList<Byte> intToBin(int input){
		Program.sysLog("Converter initializes int->bin...");
		ArrayList<Byte> internal = new ArrayList<>();
		int intSize 	= BinaryOperator.getIntSize(),
			multiplier 	= BinaryOperator.getMultiplier(),
			value 		= input;
		for(int i=0;i<intSize;i++) {
			internal.add((byte)(value%multiplier));
			value = value/multiplier;
		}
		if(value!=0) Program.error("intToBin did not make it through");
		return internal;
	}
	
	public static ArrayList<Byte> strToBin(String input) {
		Program.sysLog("Converter initializes str->bin...");
		ArrayList<Byte> internal = new ArrayList<Byte>();
		int charSize = BinaryOperator.getCharSize(),
			multiplier = BinaryOperator.getMultiplier();
		for(int x=0;x<input.length();x++) {
			int value = input.charAt(x);
			for(int i=0;i<charSize;i++) {
				internal.add((byte)(value%multiplier));
				value = value/multiplier;
			}
		}
		return internal;
	}
	
	public static ArrayList<Byte> contentToBin(ArrayList<Byte> input){
		Program.sysLog("Converter initializes str->bin...");
		ArrayList<Byte> internal = new ArrayList<Byte>();
		int binSize = BinaryOperator.getBinSize(),
			multiplier = BinaryOperator.getMultiplier();
		for(int x=0;x<input.size();x++) {
			int value = input.get(x);
			value = value<0? (byte)(-value):value;
			for(int i=0;i<binSize;i++) {
				internal.add((byte)(value%multiplier));
				value = value/multiplier;
			}
			if(value!=0) Program.error("binToBin did not make it through");
		}
		return internal;
	}
	
	public static ArrayList<Byte> decreaseUnderZero(ArrayList<Byte> array){
		for(int i=0; i<array.size();i++) {
			Byte b = array.get(i);
			if(b.intValue()<0) {
				String msg = "Changed value from "+array.get(i)+" to ";
				array.set(i, (byte)(array.get(i)-16));
				msg += array.get(i);
				Program.sysp(msg);
			}
		}
		
		return array;
	}
	
	public static ArrayList<Byte> binToContent(ArrayList<Byte> array) {
		ArrayList<Byte> internal = new ArrayList<Byte>();
		int arrSize = array.size(),
			binSize = BinaryOperator.getBinSize(),
			multiplier = BinaryOperator.getMultiplier(),
			x=0;
			
		while(x<arrSize-binSize) {
			int value = 0;
			for(int i=0; i<binSize; i++) {
				int part = array.get(x+i)*BinaryOperator.pow(multiplier,i);
				part = part>=0? part:-part;
				value += part;
			}
			value = value>=0? value:-value;
			internal.add((byte)value);
			x+=binSize;
		}
		//internal = decreaseUnderZero(internal);
		return internal;
	}
	
	public static String binToStr(ArrayList<Byte> array) {
		String internal = "";
		int arrSize = array.size(),
			charSize = BinaryOperator.getCharSize(),
			multiplier = BinaryOperator.getMultiplier(),
			x=0;
			
		while(x<arrSize-charSize) {
			int value = 0;
			for(int i=0; i<charSize; i++) {
				int part = array.get(x+i)*BinaryOperator.pow(multiplier,i);
				part = part>=0? part:-part;
				value += part;
			}
			value = value>=0? value:-value;
			if((char)value == '\4') break;
			internal += (char)value;
			x+=charSize;
		}
		return internal;
	}
	
	public static ArrayList<Byte> ByteToArrayList(byte[] input) {
		ArrayList<Byte> output = new ArrayList<Byte>();
		for(int i=0; i<input.length;i++) output.add(input[i]);
		return output;
	}
	
	public static byte[] ArrayListToByte(ArrayList<Byte> input) {
		byte[] output = new byte[input.size()];
		for(int i=0; i<input.size();i++) output[i] = input.get(i);
		return output;
	}
}
