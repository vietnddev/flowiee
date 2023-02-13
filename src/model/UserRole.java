package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserRole")
public class UserRole implements java.io.Serializable {
	private int ID;
	private int IDUser;
	private int IDRole;

	public UserRole() {
	}

	public UserRole(int iDUser, int iDRole) {
		IDUser = iDUser;
		IDRole = iDRole;
	}

	public UserRole(int iD, int iDUser, int iDRole) {
		ID = iD;
		IDUser = iDUser;
		IDRole = iDRole;
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

	public int getIDRole() {
		return IDRole;
	}

	public void setIDRole(int iDRole) {
		IDRole = iDRole;
	}
}
