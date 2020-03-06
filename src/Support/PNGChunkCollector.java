package Support;

import java.util.ArrayList;

import Core.Program;
import Support.Entities.PNGChunk;
import Support.Entities.RegisterEntry;
import Support.Enums.ChunkType;

@Deprecated
public class PNGChunkCollector {
	private static ArrayList<ArrayList<PNGChunk>> collection = new ArrayList<>();
	
	public static void collect(RegisterEntry input) {
		Program.sysLog("PNGChunkCollector initializes collection.");
		collection.add(new ArrayList<PNGChunk>());
		int size = input.size();
		for(int i = 0; i<size-3; i++) {
			//Program.sysLog("Collection progress: "+(i/size-3));
			String check = new String();
			check += (char)((int)input.get(i+0));
			check += (char)((int)input.get(i+1));
			check += (char)((int)input.get(i+2));
			check += (char)((int)input.get(i+3));
			ChunkType type = ChunkType.checkType(check);
			if(type!=null) {
					i+=4;
				int start = i;
				switch(type) {
					case IDAT:
						for(;i<size-3;i++) {
							String check2 = new String();
							check2 += (char)((int)input.get(i+0));
							check2 += (char)((int)input.get(i+1));
							check2 += (char)((int)input.get(i+2));
							check2 += (char)((int)input.get(i+3));
							if(ChunkType.checkType(check2)!=null) {
								i--; 
								PNGChunkCollector.add(new PNGChunk(start,i,type));
								break;
							}
						}
						break;
					case IEND:
						PNGChunkCollector.add(new PNGChunk(i,i+3,type));
						i = size;
						break;
					case IHDR:
						for(;i<size-3;i++) {
							String check2 = new String();
							check2 += (char)((int)input.get(i+0));
							check2 += (char)((int)input.get(i+1));
							check2 += (char)((int)input.get(i+2));
							check2 += (char)((int)input.get(i+3));
							if(ChunkType.checkType(check2)!=null) {
								i--; 
								PNGChunkCollector.add(new PNGChunk(start,i,type));
								break;
							}
						}
						break;
					case PLTE:
						for(;i<size-3;i++) {
							String check2 = new String();
							check2 += (char)((int)input.get(i+0));
							check2 += (char)((int)input.get(i+1));
							check2 += (char)((int)input.get(i+2));
							check2 += (char)((int)input.get(i+3));
							if(ChunkType.checkType(check2)!=null) {
								i--; 
								PNGChunkCollector.add(new PNGChunk(start,i,type));
								break;
							}
						}
						break;
					default:
						break;
				}
			}
		}
		Program.sysLog("PNGChunkCollector finished the job.");
		Program.sysLog(showOff());
		Program.write("\n"+showOff());
	}
	
	public static ArrayList<PNGChunk> getList(int index){return collection.get(index);}
	
	public static ArrayList<PNGChunk> getLastList(){return  collection.get(size()-1);}
	
	public static void add(PNGChunk chunk) {
		if(size()<=0)collection.add(new ArrayList<PNGChunk>());
		getLastList().add(chunk);
	}
	
	public static void add(int index, PNGChunk chunk) {
		if(size()>index) collection.get(index).add(chunk);
		else Program.error("PNGChunkCollector does not have "+(index+1)+" arrays of chunks.");
	}
	
	public static void add(int index, int subindex,PNGChunk chunk) {
		if(size()>index) collection.get(index).add(subindex,chunk);
		else Program.error("PNGChunkCollector does not have "+(index+1)+" arrays of chunks.");
	}
	
	public static int size() {return collection.size();}
	
	public static PNGChunk get(int subindex) {return getLastList().get(subindex);}
	
	public static PNGChunk get(int index, int subindex) {return collection.get(index).get(subindex);}
	
	public static String showOff() {
		String output = "PNGChunkCollector:";
		int idat=0, iend=0, ihdr=0, plte=0;
		for(PNGChunk c: getLastList()) {
			switch(c.type) {
				case IDAT: idat++;	break;
				case IEND: iend++;	break;
				case IHDR: ihdr++;	break;
				case PLTE: plte++;	break;
				default:			break;
			}
		}
		if(ihdr>0) {output += "\n>  IHDR chunks: "+ihdr;}
		if(plte>0) {output += "\n>  PLTE chunks: "+plte;}
		if(idat>0) {output += "\n>  IDAT chunks: "+idat;}
		if(iend>0) {output += "\n>  IEND chunks: "+iend;}
		if(idat+iend+ihdr+plte==0) output += "\n No chunks found. The file is not a PNG image.";
			
		return output;
	}
}
