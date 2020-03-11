package Support.Entities;

import java.util.ArrayList;
import java.util.Collections;

import Core.Program;
import Support.BinaryOperator;
import Support.Converter;

public class Payload {
	public String type = new String();
	public ArrayList<Byte> content;
	
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
				int value = BinaryOperator.pow(multi, power);
				addition = (byte)BinaryOperator.pow(multi, power);
				addIn = (byte)(addIn + addition); 
				Program.sysLog("AddIn: "+addIn+" | Addition: "+addition+" "
				+ "| Addition should be: "+multi+"^("+i+"%"+bitNo+") = "+multi+"^"+power+" = "+value);
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
			for(int x=bitNo;x>=0;x--) {
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
		int base	= size/8, 
			add		= size%8!=0? 1:0, 
			result	= base + add;
			
			return result;
		
	}
	
	public int getSignSize() {return getSignSize(this.content.size());}
	
	public void sign() {
		int number = 1+content.size()+BinaryOperator.getIntSize()+BinaryOperator.getCharSize()*3+getSignSize();

		ArrayList<Byte> 
			signs=signToArr(content), 
			temp= Converter.strToBin(type), 
			size= Converter.intToBin(number);
		
		temp.addAll(size);
		temp.addAll(signs);
		temp.addAll(content);
		content = new ArrayList<Byte>();
		content.addAll(temp);
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
	
	public static ArrayList<Byte> trim(ArrayList<Byte> input){
		Program.sysLog("Payload initializes trim...");
		ArrayList<Byte> internal = new ArrayList<> (), signs = new ArrayList<>();
		int intS	= BinaryOperator.getIntSize(),
			multip  = BinaryOperator.getMultiplier(),
			x		= BinaryOperator.getCharSize()*3,
			end		= 0,  i = 0, part = 0, ssize = 0;
		
		for(;i<intS;i++) {
			part = input.get(x+i)*BinaryOperator.pow(multip,i);
			part = part<0? -part:part;
			end += part;
		}
		x+=i; ssize = estimateSign(1+end-x);
		for(i = 0;i<ssize;i++) signs.add(input.get(x+i));
		x+=i;
		end = end>input.size()? input.size():end;
		for(;x<end;x++) {internal.add(input.get(x));}
		
		return internal;
	}
}
