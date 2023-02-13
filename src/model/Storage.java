package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Storage")
public class Storage {
	private int ID;
	private int IDParent;
	private int Type;
	private String Name;
	private String StgName;
	private String Describes;
	private String Extension;
	private double Size;
	private String Author;
	private String Path;
	private int IDDocType;
	
	public Storage() {
	}
	
	public Storage(int iD, int iDParent, int type, String name, String stgName, String describes, String extension,
			double size, String author, String path, int iDDocType) {
		ID = iD;
		IDParent = iDParent;
		Type = type;
		Name = name;
		StgName = stgName;
		Describes = describes;
		Extension = extension;
		Size = size;
		Author = author;
		Path = path;
		IDDocType = iDDocType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getIDParent() {
		return IDParent;
	}

	public void setIDParent(int iDParent) {
		IDParent = iDParent;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStgName() {
		return StgName;
	}

	public void setStgName(String stgName) {
		StgName = stgName;
	}

	public String getDescribes() {
		return Describes;
	}

	public void setDescribes(String describes) {
		Describes = describes;
	}

	public String getExtension() {
		return Extension;
	}

	public void setExtension(String extension) {
		Extension = extension;
	}

	public double getSize() {
		return Size;
	}

	public void setSize(double size) {
		Size = size;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	public int getIDDocType() {
		return IDDocType;
	}

	public void setIDDocType(int iDDocType) {
		IDDocType = iDDocType;
	}
}
