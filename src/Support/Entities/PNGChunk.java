package Support.Entities;

import Core.Program;
import Support.Enums.ChunkType;

public class PNGChunk {
	public int startIndex;
	public int endIndex;
	public ChunkType type;
	
	public PNGChunk(int start, int end, ChunkType type) {
		Program.sysLog("Created a PNGChunk of type "+type.name());
		this.startIndex = start;
		this.endIndex = end;
		this.type = type;
	}
}
