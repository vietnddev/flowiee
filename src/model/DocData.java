package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocData")
public class DocData {
	private int ID;
	private int IDDocField;
	private String Value;
	private int IDDoc;
	
	public DocData () {
	}
	
	public DocData(int iD, int iDDocField, String value, int iDDoc) {	
		ID = iD;
		IDDocField = iDDocField;
		Value = value;
		IDDoc = iDDoc;
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

	public int getIDDocField() {
		return IDDocField;
	}

	public void setIDDocField(int iDDocField) {
		IDDocField = iDDocField;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public int getIDDoc() {
		return IDDoc;
	}

	public void setIDDoc(int iDDoc) {
		IDDoc = iDDoc;
	}
}
