package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocType")
public class DocType {
	private int ID;
	private String Name;
	private String Describes;
	private int FileCount;
	private int SizeSum;
	private int Sort;
	private boolean Status;

	public DocType() {

	}

	public DocType(int iD, String name, String describes, int fileCount, int sizeSum, int sort, boolean status) {
		ID = iD;
		Name = name;
		Describes = describes;
		FileCount = fileCount;
		SizeSum = sizeSum;
		Sort = sort;
		Status = status;
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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescribes() {
		return Describes;
	}

	public void setDescribes(String describes) {
		Describes = describes;
	}

	public int getFileCount() {
		return FileCount;
	}

	public void setFileCount(int fileCount) {
		FileCount = fileCount;
	}

	public int getSizeSum() {
		return SizeSum;
	}

	public void setSizeSum(int sizeSum) {
		SizeSum = sizeSum;
	}

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
}
