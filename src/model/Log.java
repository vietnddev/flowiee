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
@Table(name = "Log")
public class Log implements java.io.Serializable { // ok
	private int ID;
	private String Users;
	private String Action;
	private String Url;
	private String Created;
	private String IP;
	
	public Log() {
	}
	
	public Log(int iD, String users, String action, String url, String created, String iP) {
		ID = iD;
		Users = users;
		Action = action;
		Url = url;
		Created = created;
		IP = iP;
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
	
	public String getUsers() {
		return Users;
	}
	
	public void setUsers(String users) {
		Users = users;
	}
	
	public String getAction() {
		return Action;
	}
	
	public void setAction(String action) {
		Action = action;
	}
	
	public String getUrl() {
		return Url;
	}
	
	public void setUrl(String url) {
		Url = url;
	}
	
	public String getCreated() {
		return Created;
	}
	
	public void setCreated(String created) {
		Created = created;
	}
	
	public String getIP() {
		return IP;
	}
	
	public void setIP(String iP) {
		IP = iP;
	}
}
