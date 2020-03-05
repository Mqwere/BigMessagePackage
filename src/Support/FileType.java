package Support;

public enum FileType {
	BMP("bmp"), 
	PNG("png");
	
	String extension;
	
	private FileType(String str) {
		this.extension = str;
	}
}
