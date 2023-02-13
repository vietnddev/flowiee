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
@Table(name = "Notification")
public class Notification implements java.io.Serializable { // ok
	private int ID;
	private String Created;
	private String IDReceiver;
	private String IDSender;
	private boolean IsReaded;
	private boolean IsSendMail;
	private String Message;
	private int Type;
	private int IDOrders;

	public Notification() {
	}

	public Notification(int iD, String created, String iDReceiver, String iDSender, boolean isReaded,
			boolean isSendMail, String message, int type, int iDOrders) {
		ID = iD;
		Created = created;
		IDReceiver = iDReceiver;
		IDSender = iDSender;
		IsReaded = isReaded;
		IsSendMail = isSendMail;
		Message = message;
		Type = type;
		IDOrders = iDOrders;
	}

	public Notification(String created, String iDReceiver, String iDSender, boolean isReaded, boolean isSendMail,
			String message, int type, int iDOrders) {
		Created = created;
		IDReceiver = iDReceiver;
		IDSender = iDSender;
		IsReaded = isReaded;
		IsSendMail = isSendMail;
		Message = message;
		Type = type;
		IDOrders = iDOrders;
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

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	public String getIDReceiver() {
		return IDReceiver;
	}

	public void setIDReceiver(String iDReceiver) {
		IDReceiver = iDReceiver;
	}

	public String getIDSender() {
		return IDSender;
	}

	public void setIDSender(String iDSender) {
		IDSender = iDSender;
	}

	public boolean isIsReaded() {
		return IsReaded;
	}

	public void setIsReaded(boolean isReaded) {
		IsReaded = isReaded;
	}

	public boolean isIsSendMail() {
		return IsSendMail;
	}

	public void setIsSendMail(boolean isSendMail) {
		IsSendMail = isSendMail;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getIDOrders() {
		return IDOrders;
	}

	public void setIDOrders(int iDOrders) {
		IDOrders = iDOrders;
	}
}
