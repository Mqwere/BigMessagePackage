package Support;

import java.util.ArrayList;
import java.util.Random;

import Core.Program;
import Support.Entities.Payload;

public class TestManager {
	public static Random rand = new Random();
	
	private static void say(Object input) {
		if(Program.mainWindow!=null) Program.write("TstMngr", input);
		else {
			boolean logAll = Program.LOG_ALL;
			Program.LOG_ALL = true; // ensures that TestManager will be able to talk
			Program.sysLog(input);	
			Program.LOG_ALL = logAll;
		}
	}
	
	public static void trimmingTest() {
		//Byte[] data = {10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2};
		/**/
		Byte[] data = {10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,
					   10,12,-1,5,23,62,16,28,-3,56,38,12,52,-74,2,44,35,111,84,32,-78,2,78,26,93,12,73,12,-72,13,62,83,56,27,3,17,72,56,24,12,82,63,-43,66,27,21,21};
		/**/
		/*/
		Byte[] data = {	-10,-1,-11,1,-12,1,13,-1,14,1,15,1,-10,1,11,1,1,1,1,1,-1,1,1,1,1,1,1,1,1,1,1,1,1,-1,1,1,1};
		/**/
		/*/
		Byte[] data = {	-10,-1,-11,1,1,1,1};
		/**/
		ArrayList<Byte> array = new ArrayList<Byte>();	for(int i=0; i<data.length;i++) array.add(data[i]);
		Payload pl1 = new Payload("TXT",array);
		say(pl1);
		pl1.sign();
		say(pl1);
		say("Sign truth: "+pl1.sign.size()/7);
		int 	intS	= BinaryOperator.getIntSize(),
				x		= BinaryOperator.getCharSize()*3,
				start	= intS+x+(pl1.sign.size()/7);
		say(pl1.content.subList(start,start+20));
		pl1.trim();
		say(pl1);
	}
	
	public static void massPayloadTest(int iter) {for(int i=0; i<iter;iter++) payloadTest(i);}
	public static void payloadTest(int size) {
		int payloadSize = size;
		Payload test = Payload.generate(payloadSize),
				copy = new Payload("TST",test.content);
		test.sign();
		copy.illusorySign();
		int signSize = Payload.getSignSize(copy.content.size()),
			fullSize = copy.content.size() + signSize,
			estimate = fullSize/8 + ((fullSize%8!=0)? 1:0);
		/*/
		if(estimate!=signSize)
			say("Test Failed:\n"
					+ "payloadSize: "+payloadSize+"\n"
			+ 	"signSize: "+signSize+"\n"
			+ 	"fullSize: "+fullSize+"\n\n"
			+ 	"estSignSize: "+estimate+"\n----------------\n");
		else
			say("Test Succesful.");
		/**/
		int est = test.getThisFuckingCancerousPieceOfShit();
		if(fullSize!=est)
			say("Test Failed:\n"
			+ 	"fullSize: "+fullSize+"\n\n"
			+ 	"estFullSize: "+est+"\n----------------\n");
		else
			say("Test Succesful.");
	}
	
	public static void massSignsTest(int iter) {for(int i=0; i<iter;i++) signsTest(10*i,10*i);}
	public static void signsTest() {signsTest(20,50);}
	public static void signsTest(int A, int B) {
		int arrSize,
		min,
		max;
		if(A==B) arrSize = A;
		else {
			min = A>B? B:A;
			max = A>B? A:B;
			arrSize = min + (rand.nextInt()%((max-min)+1));
		}
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
				String out = null;
				Byte value = content.get(i);
				if(isNeg&&value >= 0) {
					out=("  Test failed: False Positive.");
				}
				else
				if(!isNeg&&value < 0) {
					out=("  Test failed: False Negative.");
				}
				i++;
				if(out!=null) say(out);
			}
			return;
		}
	}
}
