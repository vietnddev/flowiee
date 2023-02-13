package model;
// Generated Nov 1, 2020 4:29:39 PM by Hibernate Tools 4.3.5.Final

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

/**
 * Image generated by hbm2java
 */
@Entity
@Table(name = "Image")
public class Image implements java.io.Serializable {

	private int ID;
	private int IDProduct;
	private String note;
	private boolean status;

	public Image() {
	}

	public Image(int iD, int iDProduct, String note, boolean status) {
		ID = iD;
		IDProduct = iDProduct;
		this.note = note;
		this.status = status;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}