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

@Entity
@Table(name = "Orders")
public class Orders implements java.io.Serializable {
	private int ID;
	private String Code;
	private int IDCustomer;
	private String Name;
	private String Phone;
	private String Email;
	private String Address;
	private String Note;
	private String Date;
	private Double TotalMoney;
	private String Sales;
	private String Channel;
	private String Status;

	public Orders() {
	}

	public Orders(int iD, String code, int iDCustomer, String name, String phone, String email, String address,
			String note, String date, Double totalMoney, String sales, String channel, String status) {
		ID = iD;
		Code = code;
		IDCustomer = iDCustomer;
		Name = name;
		Phone = phone;
		Email = email;
		Address = address;
		Note = note;
		Date = date;
		TotalMoney = totalMoney;
		Sales = sales;
		Channel = channel;
		Status = status;
	}

	public Orders(String code, int iDCustomer, String name, String phone, String email, String address, String note,
			String date, Double totalMoney, String sales, String channel, String status) {
		Code = code;
		IDCustomer = iDCustomer;
		Name = name;
		Phone = phone;
		Email = email;
		Address = address;
		Note = note;
		Date = date;
		TotalMoney = totalMoney;
		Sales = sales;
		Channel = channel;
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

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getIDCustomer() {
		return IDCustomer;
	}

	public void setIDCustomer(int iDCustomer) {
		IDCustomer = iDCustomer;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public Double getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		TotalMoney = totalMoney;
	}

	public String getSales() {
		return Sales;
	}

	public void setSales(String sales) {
		Sales = sales;
	}

	public String getChannel() {
		return Channel;
	}

	public void setChannel(String channel) {
		Channel = channel;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
