package Support.Enums;

@Deprecated
public enum ChunkType {
	IHDR,
	PLTE,
	IDAT,
	IEND;
	
	public static ChunkType checkType(String input) {
		for(ChunkType c: ChunkType.values()) {
			if(c.name().equals(input)) return c;
		}
		return null;
	}
}
