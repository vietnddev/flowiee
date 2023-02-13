/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author viet0
 */
@Entity
@Table(name = "Product")
public class Product implements java.io.Serializable { // ok

	private int ID;
	private String Code;
	private int IDBrand;
	private String Type;
	private String Name;
	private String Color;
	private Double Price;
	private String Size;
	private String Date;
	private int Storage;
	private int Quantity;
	private boolean HighLight;
	private String Describes;
	private boolean Status;
	private int Promotion;
	
	public Product() {
	}

	public Product(int iD, String code, int iDBrand, String type, String name, String color, Double price, String size,
			String date, int storage, int quantity, boolean highLight, String describes, boolean status, int promotion) {
		ID = iD;
		Code = code;
		IDBrand = iDBrand;
		Type = type;
		Name = name;
		Color = color;
		Price = price;
		Size = size;
		Date = date;
		Storage = storage;
		Quantity = quantity;
		HighLight = highLight;
		Describes = describes;
		Status = status;
		Promotion = promotion;
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

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getIDBrand() {
		return IDBrand;
	}

	public void setIDBrand(int iDBrand) {
		IDBrand = iDBrand;
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

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	@Column(name = "Date")
	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public int getStorage() {
		return Storage;
	}

	public void setStorage(int storage) {
		Storage = storage;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public boolean isHighLight() {
		return HighLight;
	}

	public void setHighLight(boolean highLight) {
		HighLight = highLight;
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

	public int getPromotion() {
		return Promotion;
	}

	public void setPromotion(int promotion) {
		Promotion = promotion;
	}
}
