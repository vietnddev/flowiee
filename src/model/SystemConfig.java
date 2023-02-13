package model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name = "SystemConfig")
public class SystemConfig implements java.io.Serializable{
	
	private int ID;
	private String Code;
	private String Name;
	private String Address;
	private String Email;
	private String Phone;
	private String Logo;
	private String Favicon;
	private int LoginLock;
	private String Describes;
	
	public SystemConfig() {
		super();
	}
	public SystemConfig(String code, String name, String address, String email, String phone, String logo,
			String favicon, int loginLock, String describes) {
		Code = code;
		Name = name;
		Address = address;
		Email = email;
		Phone = phone;
		Logo = logo;
		Favicon = favicon;
		this.LoginLock = loginLock;
		Describes = describes;
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
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public String getAddress() {
		return Address;
	}
	
	public void setAddress(String address) {
		Address = address;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public String getPhone() {
		return Phone;
	}
	
	public void setPhone(String phone) {
		Phone = phone;
	}
	
	public String getLogo() {
		return Logo;
	}
	
	public void setLogo(String logo) {
		Logo = logo;
	}
	
	public String getFavicon() {
		return Favicon;
	}
	
	public void setFavicon(String favicon) {
		Favicon = favicon;
	}
	
	public int getLoginLock() {
		return LoginLock;
	}
	
	public void setLoginLock(int loginLock) {
		this.LoginLock = loginLock;
	}
	
	public String getDescribes() {
		return Describes;
	}
	
	public void setDescribes(String describes) {
		Describes = describes;
	}
}
