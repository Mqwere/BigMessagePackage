package Support.Enums;

public enum FileType {
	BMP("bmp"), 
	PNG("png");
	
	public String extension;
	
	private FileType(String str) {
		this.extension = str;
	}
}
