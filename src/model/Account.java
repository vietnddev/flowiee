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
@Table(name = "Account")
public class Account implements java.io.Serializable { // ok

	private int ID;
	private String Username;
	private String Password;
	private String Name;
	private boolean Gender;
	private String Phone;
	private String Email;
	private boolean IsAdmin;
	private String Avatar;
	private String Notes;
	private boolean Status;
	private MultipartFile mainImg;

	public Account() {
	}

	public Account(int iD, String username, String password, String name, boolean gender, String phone, String email,
			boolean isAdmin, String avatar, String notes, boolean status, MultipartFile mainImg) {
		ID = iD;
		Username = username;
		Password = password;
		Name = name;
		Gender = gender;
		Phone = phone;
		Email = email;
		IsAdmin = isAdmin;
		Avatar = avatar;
		Notes = notes;
		Status = status;
		this.mainImg = mainImg;
	}

	@Transient
	public MultipartFile getMultipartFile() {
		return mainImg;
	}

	public void setBrandImg(MultipartFile mainImg) {
		this.mainImg = mainImg;
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

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isGender() {
		return Gender;
	}

	public void setGender(boolean gender) {
		Gender = gender;
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

	public boolean isIsAdmin() {
		return IsAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		IsAdmin = isAdmin;
	}

	public String getAvatar() {
		return Avatar;
	}

	public void setAvatar(String avatar) {
		Avatar = avatar;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
}
