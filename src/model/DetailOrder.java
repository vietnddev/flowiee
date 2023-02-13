/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author viet0
 */
@Entity
@Table(name = "DetailOrder")
public class DetailOrder implements java.io.Serializable { // ok
	private int ID;
	private int IDOrders;
	private int IDProduct;
	private String Name;
	private Double UnitPrice;
	private int Quantity;
	private Double TotalMoney;
	private String Note;
	private boolean Status;
	
	public DetailOrder() {
	}

	public DetailOrder(int iDOrders, int iDProduct, String name, Double unitPrice, int quantity,
			Double totalMoney, String note, boolean status) {
		IDOrders = iDOrders;
		IDProduct = iDProduct;
		Name = name;
		UnitPrice = unitPrice;
		Quantity = quantity;
		TotalMoney = totalMoney;
		Note = note;
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

	public int getIDOrders() {
		return IDOrders;
	}

	public void setIDOrders(int iDOrders) {
		IDOrders = iDOrders;
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

	public Double getUnitPrice() {
		return UnitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		UnitPrice = unitPrice;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public Double getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		TotalMoney = totalMoney;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
}
