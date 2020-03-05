package Support.Entities;

import java.util.ArrayList;

import Support.Enums.FileType;

public class RegisterEntry {
	public ArrayList<Byte> content;
	public FileType type = null;
	
	public RegisterEntry(String ext) {
		for(FileType type: FileType.values()) if(ext.equalsIgnoreCase(type.extension)) this.type = type;
		if(type==null) type = FileType.BMP;
		content  = new ArrayList<Byte>();
	}

	public RegisterEntry(FileType type) {
		this.type = type;
		content  = new ArrayList<Byte>();
	}
	
	public RegisterEntry(String ext, ArrayList<Byte> data) {
		this(ext);
		this.content.addAll(data);
	}
	
	public RegisterEntry(FileType type, ArrayList<Byte> data) {
		this(type);
		this.content.addAll(data);
	}
	
	public Byte get(int index) {
		return this.content.get(index);
	}
	
	public void set(int index, Byte b) {
		this.content.set(index, b);
	}
	
	public int size() {
		return this.content.size();
	}
}
