package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Comment")
public class Comment {
	private int ID;
	private int IDProduct;
	private String Name;
	private String Email;
	private String Phone;
	private String Created;
	private String Describes;
	private boolean Status;
	private int Code;

	public Comment() {

	}

	public Comment(int iD, int iDProduct, String name, String email, String phone, String created, String describes, boolean status, int code) {
		ID = iD;
		IDProduct = iDProduct;
		Name = name;
		Email = email;
		Phone = phone;
		Created = created;
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

	public int getIDProduct() {
		return IDProduct;
	}

	public void setIDProduct(int iDProduct) {
		IDProduct = iDProduct;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
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

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}
}
