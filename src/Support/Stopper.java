package Support;

import java.util.Date;

public class Stopper{
	private static Date start = null;
	private static Date stop = null;
	
	public static void start() {
		start = new Date();
	}
	
	public static String stop() {
		stop = new Date();
		if(start!=null) return evaluate();
		else return "N/A";
	}
	
	public static String evaluate() {
		long diff = stop.getTime() - start.getTime();
		return String.format("%1$,.2f",(double)diff/1000d);
	}
	
	public static String evaluate(Date ED) {
		return evaluate(start, ED);
	}
	
	public static String evaluate(Date ST, Date ED) {
		long diff = ST.getTime() - ED.getTime();
		return String.format("%1$,.2f",(double)diff/1000d);
	}
	
	
}
