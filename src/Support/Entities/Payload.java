package Support.Entities;

import java.util.ArrayList;

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
	
	public ArrayList<Byte> getSigns(ArrayList<Byte> input){
		ArrayList<Byte> signs = new ArrayList<Byte>();
		int bitNo = 7, multi = BinaryOperator.getMultiplier();
		Byte addIn = 0;
		for(int i=0;i<input.size();i++) {
			Byte current = input.get(i);
			if(current<0)
				addIn = (byte)(addIn + BinaryOperator.pow(multi, i%bitNo));
			
			if(current%bitNo==bitNo-1 || i==input.size()-1) {
				signs.add(addIn);
				addIn = 0;
			}
		}
		
		return signs;
	}
	
	public int getSignSize() {
		int base	= this.content.size()/7, 
			add		= this.content.size()%7!=0? 1:0, 
			result	= base + add;
		
		return result;
	}
	
	public void sign() {
		int number = 1+content.size()+BinaryOperator.getIntSize()+BinaryOperator.getCharSize()*3;//+getSignSize();

		ArrayList<Byte> 
			//signs=getSigns(content), 
			temp= Converter.strToBin(type), 
			size= Converter.intToBin(number);
		
		temp.addAll(size);
		//temp.addAll(signs);
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
		ArrayList<Byte> internal = new ArrayList<> ();
		int intS	= BinaryOperator.getIntSize(),
			multip  = BinaryOperator.getMultiplier(),
			x		= BinaryOperator.getCharSize()*3,
			end		= 0, i = 0, part = 0;
		
		for(;i<intS;i++) {
			part = input.get(x+i)*BinaryOperator.pow(multip,i);
			part = part<0? -part:part;
			end += part;
		}
		Program.sysLog("X  (?): "+x);
		x+=i;
		Program.sysLog("X  (!): "+x);
		Program.sysLog("End(?): "+end);
		end = end>input.size()? input.size():end;
		Program.sysLog("End(!): "+end);
		for(;x<end;x++) {internal.add(input.get(x));}
		Program.sysLog("Internal: "+internal.size());
		
		return internal;
	}
}
