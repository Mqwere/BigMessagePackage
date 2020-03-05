package Support;

import java.util.ArrayList;

import Core.Program;

public class BinaryOperations {
	public static final int BIT_NO = 1;
	
	public static int pow2(int power) {return pow(2,power);}
	
	public static int pow(int base, int power) {
		int result = 1;
		for(int i=0; i<power;i++) {result*=base;}
		return result;
	}
	
	public static int digiNo(int number) {
		return Integer.toString(number).length();
	}
	
	@SuppressWarnings("unused")
	public static int getCharSize() {
		int base = (16/BIT_NO),
			add = (16%BIT_NO)>0? 1:0,
			result = base + add;
			
			//Program.sysp("charSize: "+base +" + "+add + " = " + result);
		
		return result;
	}
	
	public static int getMultiplier() {
		return pow2(BIT_NO);
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
	
	/////////////////////////////////////////////////////////
	
	public static String translate(RegisterEntry input) {
		switch(input.type) {
			case BMP:
				ArrayList<Byte> internal = new ArrayList<Byte>();
				int i 	 = input.get(10) 
						 + input.get(11)*256 
						 + input.get(12)*256*256 
						 + input.get(13)*256*256*256,
					size = input.size(),//-64*4,
					multiplier = (int)Math.pow(2, BIT_NO),
					start = i;
				//Program.sysp("Translate - start: "+start+", size: "+size);

				while(i<size) {
					Byte b = input.get(i);
					if(start%4 != (i+1)%4) {
						b =  (byte)(b%multiplier);
						internal.add(b);
					}
					i++;
				}
				//Program.sysp("Translate - end");
				return BinaryOperations.binToStr(internal);
			case PNG:
				return "unimplemented";
				
			default: return "wtf is this type even?";
		}
	}
	
	public static RegisterEntry writeTo(RegisterEntry input, ArrayList<Byte> message){
		switch(input.type) {
			case BMP:
			int i 	 = input.get(10) 
				 	 + input.get(11)*256 
				 	 + input.get(12)*256*256 
				 	 + input.get(13)*256*256*256,
				 x 	 = 0,	
				 size = input.size(),
				 multiplier = (int)Math.pow(2, BIT_NO),
				 start = i;
			//Program.sysp("Write - start: "+start+", size: "+size);

			while(i<size) {
				Byte b = input.get(i);
				if(start%4 != (i+1)%4) {
					//if(b.intValue()<0) b = (byte) -b;
					if(b%multiplier!=0) b = (byte)(b-b%multiplier);
					if(x<message.size()) {
						b = b>=0? (byte)(b+message.get(x)):(byte)(b-message.get(x));
						input.set(i, b);
						x++;
					}
					else break;
				}
				i++;
			}
			Program.log("Message written to the image. Task accomplished.");
			return input;
			
			case PNG: return input;
			
			default: return input;
		}
		
	}
	
	public static void analyze(RegisterEntry input){
		switch(input.type) {
		case BMP:
			int start = input.get(10) 
					 + input.get(11)*256 
					 + input.get(12)*256*256 
					 + input.get(13)*256*256*256,
					
				size = input.size();

			int limit = ((size-start)*3)/(BinaryOperations.getCharSize()*4);
			limit--;
			Program.imageCharsLimit = limit;
			Program.log("Image analyzed - "+limit*2+" bytes ready to be written on.");
			break;
		case PNG:
			break;
		default:
			break;
		}
	}/**/
	
}
