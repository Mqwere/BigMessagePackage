package Support;

import java.util.ArrayList;
import java.util.Random;

import Core.Program;
import Support.Entities.Payload;

public class TestManager {
	private static Random rand = new Random();
	
	private static void say(Object input) {
		if(Program.mainWindow!=null) Program.write("TstMngr", input);
		else {
			boolean logAll = Program.LOG_ALL;
			Program.LOG_ALL = true; // ensures that TestManager will be able to talk
			Program.sysLog(input);	
			Program.LOG_ALL = logAll;
		}
	}
	
	public static void payloadTest(int size) {
		int payloadSize = size, 
			signSize = Payload.getSignSize(size),
			fullSize = payloadSize + signSize,
			estimate = fullSize/8 + ((fullSize%8!=0)? 1:0);
		
		if(estimate!=signSize)
			say("Test Failed:\n"
					+ "payloadSize: "+payloadSize+"\n"
			+ 	"signSize: "+signSize+"\n"
			+ 	"fullSize: "+fullSize+"\n\n"
			+ 	"estSignSize: "+estimate+"\n----------------\n");
		
	}
	
	public static void signsTest() {
		int arrSize = 10//80 + rand.nextInt()%21
				;
		ArrayList<Byte> content = new ArrayList<>(), signArr;
		ArrayList<Boolean> output = new ArrayList<>();
		for(int i = 0; i<arrSize;i++) content.add(new Byte((byte)(rand.nextInt(9)-4)));
		signArr = Payload.signToArr(content);
		output	= Payload.arrToSign(signArr);
		
		if(content.size()>output.size()) say("Test failed: sizes of arrays dont match. \n(Content: "+content.size()+" SignArr: "+signArr.size()+" vs. Output: "+output.size()+")");
		else {
			int i = 0; boolean isOk = true;
			while(i<content.size() && isOk) {
				boolean isNeg = output.get(i);
				String out = new String();
				Byte value = content.get(i);
				out+=("Negative: "+isNeg+", Value: "+value);
				if(isNeg&&value >= 0) {
					out+=("  Test failed: False Positive.");
				}
				else
				if(!isNeg&&value < 0) {
					out+=("  Test failed: False Negative.");
				}
				i++;
				say(out);
			}
			return;
		}
	}
}
