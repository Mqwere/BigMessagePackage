package Support.Entities;

import java.util.ArrayList;
import java.util.Collections;

import Core.Program;
import Support.BinaryOperator;
import Support.Converter;
import Support.TestManager;

public class Payload {
	public String type = new String();
	public ArrayList<Byte> content = null;
	public ArrayList<Boolean> sign = null;
	
	public Payload(String type) {
		this.type = type;
		this.content = new ArrayList<>();
	}
	
	public Payload(String type, ArrayList<Byte> content) {
		this(type);
		this.content.addAll(content);		
	}
	
	public static ArrayList<Byte> signToArr(ArrayList<Byte> input){ // uses input to generate an array of bytes, which indicate all the negative values
		ArrayList<Byte> signs = new ArrayList<Byte>();
		int bitNo = 7, multi = 2;
		Byte addIn = 0, addition;
		for(int i=0;i<input.size();i++) {
			Byte current = input.get(i);
			if(current<0) {
				int power = i%bitNo;
				//int value = BinaryOperator.pow(multi, power);
				addition = (byte)BinaryOperator.pow(multi, power);
				addIn = (byte)(addIn + addition); 
				//Program.sysLog("AddIn: "+addIn+" | Addition: "+addition+" " + "| Addition should be: "+multi+"^("+i+"%"+bitNo+") = "+multi+"^"+power+" = "+value);
			}
			if(i%bitNo==(bitNo-1) || i==input.size()-1) {
				signs.add(addIn);
				addIn = 0;
			}
		}
		return signs;
	}
	
	public static ArrayList<Boolean> arrToSign(ArrayList<Byte> input){
		ArrayList<Boolean> signs = new ArrayList<>();
		int bitNo = 7, multi = 2;
		for(int i=0;i<input.size();i++) {
			Byte current = input.get(i);
			ArrayList<Boolean> subSign = new ArrayList<>();
			for(int x=bitNo-1;x>=0;x--) {
				boolean cond = current>=BinaryOperator.pow(multi,x);
				if(cond) current = (byte)(current - BinaryOperator.pow(multi,x));
				subSign.add(cond);
			}
			Collections.reverse(subSign);
			signs.addAll(subSign);
		}
		return signs;
	}
	
	public static int getSignSize(int size) {
		int base	= size/7, 
			add		= size%7!=0? 1:0, 
			result	= base + add;
		
		return result;
	}
	
	public static int estimateSign(int size) {
		int base	= size/(8*BinaryOperator.getBinSize()), 
			add		= size%(8*BinaryOperator.getBinSize())!=0? 1:0, 
			result	= base + add;
		
			return result;
	}
	
	public int getSignSize() {return getSignSize(this.content.size());}
	
	public void sign() {
		int number = 1+BinaryOperator.getIntSize()+BinaryOperator.getCharSize()*3+getSignSize();

		ArrayList<Byte> 
			signs=signToArr(content), 
			temp= Converter.strToBin(type),
			tcontent = Converter.contentToBin(content),
			size= Converter.intToBin(number+tcontent.size());
		
		temp.addAll(size);
		temp.addAll(signs);
		temp.addAll(tcontent);
		content = new ArrayList<Byte>();
		content.addAll(temp);
		this.sign = arrToSign(signs);
	}
	
	public Byte get(int index) {
		return this.content.get(index);
	}
	
	public void set(int index, Byte b) {
		this.content.set(index, b);
	}
	
	public void add(Byte b) {
		this.content.add(b);
	}
	
	public void push(Byte b) {
		this.content.add(0, b);
	}
	
	public int size() {
		return this.content.size();
	}
	
	public void binToContent(ArrayList<Byte> input) {
		if(input==null) input = this.content;
		ArrayList<Byte> internal = new ArrayList<Byte>();
		int arrSize = input.size(),
			binSize = BinaryOperator.getBinSize(),
			multiplier = BinaryOperator.getMultiplier(),
			x=0, rbn = 0; // Real Byte No.
			
		while(x<arrSize-binSize+1) {
			int value = 0;
			for(int i=0; i<binSize; i++) {
				int part = input.get(x+i)*BinaryOperator.pow(multiplier,i);
				part = part>=0? part:-part;
				value += part;
			}
			try{value = this.sign.get(rbn)? -value:value;} catch(Exception e) {Program.sysLog("index: "+rbn+"/"+(arrSize/binSize)); System.exit(444);}
			internal.add((byte)value);
			rbn++;
			x+=binSize;
		}
		//return internal;
		this.content = new ArrayList<>();
		this.content.addAll(internal);
	}
	
	public void illusorySign() {
		ArrayList<Byte> 
			tcontent = Converter.contentToBin(content);

		content = new ArrayList<Byte>();
		content.addAll(tcontent);
	}
	
	public int getThisFuckingCancerousPieceOfShit() {
		int intS	= BinaryOperator.getIntSize(),
			multip  = BinaryOperator.getMultiplier(),
			x		= BinaryOperator.getCharSize()*3,
			end		= 0,  i = 0, part = 0;
		
		for(;i<intS;i++) {
			part = content.get(x++)*BinaryOperator.pow(multip,i);
			part = part<0? -part:part;
			end += part;
		}
		//end = end>content.size()? content.size():end;
		Program.sysLog(content.size()+" v "+end);
		return (end-(BinaryOperator.getCharSize()*3 + intS + 1));
	}
	
	public void trim() {
		Program.sysLog("Payload initializes trim...");
		ArrayList<Byte> internal = new ArrayList<> (), signs = new ArrayList<>();
		int intS	= BinaryOperator.getIntSize(),
			multip  = BinaryOperator.getMultiplier(),
			x		= BinaryOperator.getCharSize()*3,
			end		= 0,  i = 0, part = 0, ssize = 0;
		
		for(;i<intS;i++) {
			part = content.get(x++)*BinaryOperator.pow(multip,i);
			part = part<0? -part:part;
			end += part;
		}

		Program.sysLog("content size: "+content.size()+" v end: "+end);
		end = end>content.size()? content.size():end;
		//ssize = estimateSign(end-(BinaryOperator.getCharSize()*3 + intS + 1)); 
		ssize = estimateSign(end); 
		Program.sysLog("x: "+x+" content(x): "+content.get(x));
		Program.sysLog("Sign Estimate: "+ssize+" From input: "+(1+end-x));
		for(i = 0;i<ssize;i++) 
			signs.add(content.get(x++));

		Program.sysLog("x: "+x+" content(x): "+content.get(x));
		//ssize = ssize%2==0? ssize/2:(ssize/2)+1;
		//for(i = 0;i<ssize;i++);
		this.sign = arrToSign(signs);
		for(;x<end;x++) {
			internal.add(content.get(x));
		}
		//this.binToContent(internal);
		this.content = new ArrayList<Byte>();
		this.content.addAll(internal);
		Program.sysLog("Trimming ended: "+internal.size()+" bytes at the end.");
	}
	
	public static ArrayList<Byte> trim(ArrayList<Byte> input){
		Program.sysLog("Payload initializes trim...");
		Program.log(Converter.binToStr(input));
		ArrayList<Byte> internal = new ArrayList<> (), signs = new ArrayList<>();
		ArrayList<Boolean> tranSigns = new ArrayList<>();
		int intS	= BinaryOperator.getIntSize(),
			multip  = BinaryOperator.getMultiplier(),
			x		= BinaryOperator.getCharSize()*3,
			end		= 0,  i = 0, part = 0, ssize = 0;
		
		for(;i<intS;i++) {
			part = input.get(x+i)*BinaryOperator.pow(multip,i);
			part = part<0? -part:part;
			end += part;
		}
		x+=i; 
		//ssize = estimateSign(1+end-x);
		ssize = estimateSign(end);
		for(i = 0;i<ssize;i++) signs.add(input.get(x+i));
		tranSigns = arrToSign(signs);
		x+=i; i = 0;
		end = end>input.size()? input.size():end;
		for(;x+i<end;i++) {
			Byte b = input.get(x+i);
				 b = tranSigns.get(i)? (byte)(-b):b;
			internal.add(b);
		}
		Program.sysLog("Trimming ended at "+i+"/"+tranSigns.size()+" of the boolean array");
		
		return internal;
	}
	
	public static Payload generate(int contSize) {
		int value;
		ArrayList<Byte> content = new ArrayList<Byte>();
		for(int i=0; i<contSize;i++){
			value = -10 + TestManager.rand.nextInt()%21;
			content.add((byte) value);
		}
		
		return new Payload("TST", content);
	}
	
	@Override
	public String toString() {
		String output = new String();
			output+="Type: Payload - "+this.type+"\n";
		if(this.sign!=null) 
			output+="Signs:\n"+this.sign.toString()+"\n";
		if(this.content!=null) 
			output+="Data:\n"+this.content.toString();
		return output;
	}
	
}

