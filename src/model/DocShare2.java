package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocShare2")
public class DocShare2{
	private int ID;
	private int IDUser;
	private int IDDoc;		

	public DocShare2() {

	}

	public DocShare2(int iDUser, int iDDoc) {
		IDUser = iDUser;
		IDDoc = iDDoc;
	}
	
	public DocShare2(int iD, int iDUser, int iDDoc) {
		ID = iD;
		IDUser = iDUser;
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

	public int getIDUser() {
		return IDUser;
	}

	public void setIDUser(int iDUser) {
		IDUser = iDUser;
	}

	public int getIDDoc() {
		return IDDoc;
	}

	public void setIDDoc(int iDDoc) {
		IDDoc = iDDoc;
	}
}
