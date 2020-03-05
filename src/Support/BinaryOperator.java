package Support;

import java.util.ArrayList;
import java.util.Date;

import Core.Program;
import Support.Entities.PNGChunk;
import Support.Entities.RegisterEntry;
import Support.Enums.ChunkType;

public class BinaryOperator {
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
		
		return result;
	}
	
	public static int getMultiplier() {
		return pow2(BIT_NO);
	}
	
	public static ArrayList<Byte> strToBin(String input) {
		Program.sysLog("BinaryOperator initializes str->bin...");
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
		Program.sysLog("BinaryOperator finished str->bin");
		return internal;
	}
	
	public static String binToStr(ArrayList<Byte> array) {
		Program.sysLog("BinaryOperator initializes bin->str...");
		String internal = "";
		int charSize 	= getCharSize(),
			arrSize = array.size(),
			multiplier 	= getMultiplier(),
			x=0;
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
		Program.sysLog("BinaryOperator finished bin->str");
		return internal;
	}
	
	/////////////////////////////////////////////////////////
	
	public static String translate(RegisterEntry input) {
		Program.sysLog("BinaryOperator initializes translation for type "+input.type+"...");
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

				while(i<size) {
					Byte b = input.get(i);
					if(start%4 != (i+1)%4) {
						b =  (byte)(b%multiplier);
						internal.add(b);
					}
					i++;
				}
				Program.sysLog("BinaryOperator finished translating.");
				return BinaryOperator.binToStr(internal);
			case PNG: //TODO
				Program.sysLog("BinaryOperator finished translating.");
				return "unimplemented";
				
			default: 
				Program.sysLog("BinaryOperator finished translating.");
				return "wtf is this type even?";
		}
	}
	
	public static RegisterEntry writeTo(RegisterEntry input, ArrayList<Byte> message){
		Program.sysLog("BinaryOperator initializes writeTo for type "+input.type+"...");
		int multiplier = (int)Math.pow(2, BIT_NO);
		switch(input.type) {
			case BMP:
			int i 	 = input.get(10) 
				 	 + input.get(11)*256 
				 	 + input.get(12)*256*256 
				 	 + input.get(13)*256*256*256,
				 x 	 = 0,	
				 size = input.size(),
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
			
			case PNG: 
				ArrayList<PNGChunk> content = new ArrayList<>();
				content.addAll(PNGChunkCollector.getLastList());
				int chunkNo = content.size(), px = 0;
				for(int p=0; p<chunkNo;p++) {
					PNGChunk chunk = content.get(p);
					if(chunk.type==ChunkType.IDAT) {
						int fstart = chunk.startIndex, fend = chunk.endIndex, pi = fstart;
						while(pi<fend) {
							Byte b = input.get(pi);
							if(fstart%4 != (pi+1)%4) {
							//if(b.intValue()<0) b = (byte) -b;
							if(b%multiplier!=0) b = (byte)(b-b%multiplier);
							if(px<message.size()) {
								b = b>=0? (byte)(b+message.get(px)):(byte)(b-message.get(px));
								input.set(pi, b);
								px++;
							}
							else break;
							pi++;
							}
						}
					}
				}
				
				return input; //TODO
			
			default: return input;
		}
		
	}
	
	public static void analyze(RegisterEntry input){
		Program.sysLog("BinaryOperator initializes analysis for type "+input.type+"...");
		Date entry = new Date(), end;
		int limit = 0;
		switch(input.type) {
		case BMP:
			int start = input.get(10) 
					 + input.get(11)*256 
					 + input.get(12)*256*256 
					 + input.get(13)*256*256*256,
					
				size = input.size();

			limit = ((size-start)*3)/(BinaryOperator.getCharSize()*4);
			Program.sysLog("BMP Done");
			break;
		case PNG: //TODO?
			PNGChunkCollector.collect(input);
			ArrayList<PNGChunk> content = new ArrayList<>();
			content.addAll(PNGChunkCollector.getLastList());
			int chunkNo = content.size(),byteNo=0;
			for(int i=0; i<chunkNo;i++) {
				PNGChunk chunk = content.get(i);
				if(chunk.type==ChunkType.IDAT) {byteNo += (chunk.endIndex - chunk.startIndex)+1;}
			}
			limit = byteNo/BinaryOperator.getCharSize();
			Program.sysLog("PNG Done");
			break;
		default:
			break;
		}

		limit--;
		Program.imageCharsLimit = limit;
		Program.log("Image analyzed - "+limit*2+" bytes ready to be written on.");
		
		end = new Date();
		long diff = end.getTime() - entry.getTime();
		double speed = ((double)input.size()/(double)diff);
		String msg = speed>1000d? String.format("%1$,.2f",(speed/1000))+" MB/s": speed<1d? String.format("%1$,.2f",(speed*1000))+" B/s":String.format("%1$,.2f",speed)+" KB/s";
		Program.write("Loading took "+Stopper.evaluate()+" s");
		Program.write("Analysis took "+ String.format("%1$,.2f",((double)diff/1000d)) +" s\n"+input.size() 
		+" bytes processed, making the avevage speed "+msg);
	}/**/
	
}