package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Role")
public class Role implements java.io.Serializable {
	private int ID;
	private int Sort;
	private int Type;
	private String Name;
	private String Describes;
	private boolean Status;
	private String Code;

	public Role() {
	}

	public Role(int iD, int sort, int type, String name, String code, String describes, boolean status) {
		ID = iD;
		Sort = sort;
		Type = type;
		Name = name;
		Describes = describes;
		Status = status;
		Code = code;
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

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
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

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}
	
	public String getDescribes() {
		return Describes;
	}

	public void setDescribes(String describes) {
		Describes = describes;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
}
