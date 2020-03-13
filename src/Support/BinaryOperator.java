package Support;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import Core.Program;
import Support.Entities.PNGChunk;
import Support.Entities.Payload;
import Support.Entities.RegisterEntry;
import Support.Enums.ChunkType;
import Support.Enums.FileType;

@SuppressWarnings({ "unused", "deprecation" })
public class BinaryOperator {
	public static final int BIT_NO = 4;
	public static String lastMessage = new String();
	
	public static String getLastXChars(String input,int x) {
		if(x<input.length()) {input = input.substring(input.length()-x);}
		return input;
	}
	
	public static boolean compare(String first, String second) {
		int diffNo = 0, size;
		String result ="";
		if(first.length()!=second.length()) {
			result+="Strings' length are different!";
			size = first.length()>second.length()?second.length():first.length();
		}
		else size = first.length();
		
		for(int i=0; i<size;i++) if(first.charAt(i)!=second.charAt(i)) diffNo++;
		
		if(result.length()>0) result+="\nAs far as we can check them, tho:\n";
		result+=diffNo>0? "They are different on "+diffNo+" possitions.\n First String: '"+getLastXChars(first,10)+"'.\n Second String: '"+getLastXChars(second,10)+"'."
				:"Strings are the same.";
		
		Program.sysLog(result);
		
		return !(diffNo>0);
	}
	
	public static int pow2(int power) {return pow(2,power);}
	
	public static int pow(int base, int power) {
		int result = 1;
		for(int i=0; i<power;i++) {result*=base;}
		return result;
	}
	
	public static int getIntSize() {
		int base = (32/BIT_NO),
			add = (32%BIT_NO)>0? 1:0,
			result = base + add;
			
		return result;
	}
	
	public static int getCharSize() {
		int base = (16/BIT_NO),
			add = (16%BIT_NO)>0? 1:0,
			result = base + add;
		
		return result;
	}
	
	public static int getBinSize() {
		int base = (8/BIT_NO),
			add = (8%BIT_NO)>0? 1:0,
			result = base + add;
		
		return result;
	}
	
	public static int getMultiplier() {return pow2(BIT_NO);}
	
	public static ArrayList<Byte> strToBin(String input) {
		lastMessage = new String(); lastMessage += input.substring(3,input.length()-1);
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
		if(compare(input,binToStr(internal))) return internal;
		else return null;
	}
	
	public static String findSequence(ArrayList<Byte> array, String search) {
		String result = "";
		int charSize 	= getCharSize(),
			arrSize = array.size(),
			multiplier 	= getMultiplier(),
			x=0,
			hitNo = 0;
		while(x<arrSize-charSize) {
			result = "";
			for(int y=0; y<search.length();y++) {
				int value = 0;
				for(int i=0; i<charSize; i++) {
					int part = array.get(x+i)*pow(multiplier,i);
					part = part>=0? part:-part;
					value += part;
				}
				value = value>=0? value:-value;
				result+=(char)value;
			}
			if(result.equalsIgnoreCase(search)) hitNo++;
			x++;
		}
		result = "findSequence '"+search+"':"+hitNo;
		/*/
		String content = binToStr(array), scrap;
		hitNo = 0;
		while(x<content.length()-search.length()) {
			scrap = "";
			for(int i=0; i<search.length();i++) scrap+=content.charAt(x+i);
			if(scrap.equalsIgnoreCase(search)) hitNo++;
			x++;
		}
		result += ",   "+hitNo;
		/**/
		return result;
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
	
	public static ArrayList<Byte> translateBin(RegisterEntry input) {
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
		return internal;
	}
	
	public static String translate(RegisterEntry input) {
		Program.sysLog("BinaryOperator initializes translation for type "+input.type+"...");
		ArrayList<Byte> internal = translateBin(input);
		
		/*/
		Program.sysLog(findSequence(internal,"\4"));
		Program.sysLog(findSequence(internal,"donezo"));
		Program.sysLog(findSequence(internal,"1"));
		Program.sysLog(findSequence(internal,"emp"));
		Program.sysLog(findSequence(internal,"txt"));
		/**/
		
		/**/
		Program.sysLog("BinaryOperator finished translating.");
		String	content = BinaryOperator.binToStr(internal),
				pldType = content.substring(0, 3);
		if(pldType.equalsIgnoreCase("emp")) return content.substring(3);
		else {
			String dropZone = Program.DEFAULT_FILE+"."+pldType.toLowerCase();
			Program.log("Payload within is a file of type "+pldType.toUpperCase()+"."
					+ "\n Program will now save it to the default drop zone."
					+ "\n ("+dropZone+")");
			
			Payload payload = new Payload(input.type.name(),internal);
			payload.trim();
			/**/
			FileControler.savePayload(dropZone, payload.content);
			/**/
		}
		/**/
		return null;
	}
	
	public static byte stripModulo(byte b) {
		int multiplier = (int)Math.pow(2, BIT_NO);
		while(b%multiplier!=0) {b = (byte)(b-b%multiplier);}
		return b;
	}
	
	public static RegisterEntry writeTo(RegisterEntry input, Payload message){
		Program.sysLog("BinaryOperator initializes writeTo for type "+input.type+"...");
		int multiplier = (int)Math.pow(2, BIT_NO);
		switch(input.type) {
			case PNG:
			case BMP:
			int i 	 = input.get(10) 
				 	 + input.get(11)*256 
				 	 + input.get(12)*256*256 
				 	 + input.get(13)*256*256*256,
				 x 	 = 0,	
				 size = input.size(),
				 start = i;
			Program.sysLog("Write - start: "+start+", size: "+size);

			while(i<size) {
				Byte b = input.get(i), m;
				if(start%4 != (i+1)%4) {
					
					b = stripModulo(b);

					if(x<message.size()) {
						m = message.get(x); 
						m = m<0? (byte)(-m):m;
						b = b>=0? (byte)(b+m):(byte)(b-m);
						input.set(i, b);
						x++;
					}
					else break;
				}
				i++;
			}
			Program.log("Payload written to the image. Task completed.");
			
			/**/if(message.type.equals("emp"))/**/ while(!compare(translate(input),lastMessage)) {input = writeTo(input,message);}
			//else input = writeTo(input,message);
			return input;

			default: return input;
		}
		
	}
	
	public static ArrayList<Byte> fileToBMP(RegisterEntry entry){
		ArrayList<Byte> input = entry.content;
		Program.log("Initializing conversion from "+entry.type+" to BMP...");
		byte[] data = Converter.ArrayListToByte(input);
		Program.sysLog("Converter converted arrList to an array of size "+data.length);
        BufferedImage imag;
		try {
			imag = ImageIO.read(new ByteArrayInputStream(data));
			ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
	        ImageIO.write(imag, "BMP", baos);
	        byte[] output = baos.toByteArray();
			Program.log("Conversion accomplished!");
			Program.sysLog("Output of converted function has a size of "+output.length);
	        return Converter.ByteToArrayList(output);
		} catch (IOException e) {
			Program.error(e);
			return null;
		}
	}
	
	public static RegisterEntry analyze(RegisterEntry input){
		Program.sysLog("BinaryOperator initializes analysis for type "+input.type+"...");
		Date entry = new Date(), end;
		int limit = 0;
		switch(input.type) {
		case PNG:
			PNGChunkCollector.collect(input);
		default:
			input = new RegisterEntry(FileType.BMP,fileToBMP(input));
		case BMP:
			if(input.size()<14) {
				Program.error("Provided image does not have enough bytes to even be considered a carrier. Its size is "+input.size());
				return null;
			}
			int start = input.get(10) 
					 + input.get(11)*256 
					 + input.get(12)*256*256 
					 + input.get(13)*256*256*256,
					
				size = input.size();

			limit = ((size-start)*3)/(BinaryOperator.getCharSize()*4);
		/*case PNG:
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
			break;/**/
			break;
		}

		limit-=4;
		Program.imageCharsLimit = limit;
		Program.log("Image analyzed - "+limit*2+" bytes ready to be written on.");
		
		end = new Date();
		long diff = end.getTime() - entry.getTime();
		double speed = ((double)input.size()/(double)diff);
		String msg = speed>1000d? String.format("%1$,.2f",(speed/1000))+" MB/s": speed<1d? String.format("%1$,.2f",(speed*1000))+" B/s":String.format("%1$,.2f",speed)+" KB/s";
		Program.write("Loading took "+Stopper.evaluate()+" s");
		Program.write("Analysis took "+ String.format("%1$,.2f",((double)diff/1000d)) +" s\n"+input.size() 
		+" bytes processed, making the average analysis speed "+msg);
		
		return input;
	}/**/
	
}
