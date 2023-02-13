package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocField")
public class DocField {
	private int ID;
	private int IDDocType;
	private String Type;
	private String Name;
	private boolean Required;
	private int Sort;		

	public DocField() {

	}
	
	public DocField(int iD, int iDDocType, String type, String name, boolean required, int sort) {
		ID = iD;
		IDDocType = iDDocType;
		Type = type;
		Name = name;
		Required = required;
		Sort = sort;
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

	public int getIDDocType() {
		return IDDocType;
	}

	public void setIDDocType(int iDDocType) {
		IDDocType = iDDocType;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean getRequired() {
		return Required;
	}

	public void setRequired(boolean required) {
		Required = required;
	}

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
	}

	@Override
	public String toString() {
		return "DocField [ID=" + ID + ", IDDocType=" + IDDocType + ", Type=" + Type + ", Name=" + Name + ", Required="
				+ Required + ", Sort=" + Sort + "]";
	}
}
