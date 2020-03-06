package Support;

import java.util.ArrayList;

public class Converter {
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
