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

/**
 *
 * @author viet0
 */
@Entity
@Table (name = "Dept")
public class Dept { //ok
    private int ID;         //1
    private String Code;    //2
    private String Name;    //3
    private String Describe;//4
    private Date Created;   //5
    private int UserCount;  //6
    private boolean Status; //7

    public Dept() {
    }

    public Dept(String Name) {
        this.Name = Name;
    }
    
    public Dept(int ID, String Code, String Name, String Describe, Date Created, int UserCount, boolean Status) {
        this.ID = ID;
        this.Code = Code;
        this.Name = Name;
        this.Describe = Describe;
        this.Created = Created;
        this.UserCount = UserCount;
        this.Status = Status;
    }
    
    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String Describe) {
        this.Describe = Describe;
    }

    public Date getCreated() {
        return Created;
    }

    public void setCreated(Date Created) {
        this.Created = Created;
    }

    public int getUserCount() {
        return UserCount;
    }

    public void setUserCount(int UserCount) {
        this.UserCount = UserCount;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.ID;
        hash = 29 * hash + Objects.hashCode(this.Code);
        hash = 29 * hash + Objects.hashCode(this.Name);
        hash = 29 * hash + Objects.hashCode(this.Describe);
        hash = 29 * hash + Objects.hashCode(this.Created);
        hash = 29 * hash + this.UserCount;
        hash = 29 * hash + (this.Status ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dept other = (Dept) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.UserCount != other.UserCount) {
            return false;
        }
        if (this.Status != other.Status) {
            return false;
        }
        if (!Objects.equals(this.Code, other.Code)) {
            return false;
        }
        if (!Objects.equals(this.Name, other.Name)) {
            return false;
        }
        if (!Objects.equals(this.Describe, other.Describe)) {
            return false;
        }
        if (!Objects.equals(this.Created, other.Created)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dept{" + "ID=" + ID + ", Code=" + Code + ", Name=" + Name + ", Describe=" + Describe + ", Created=" + Created + ", UserCount=" + UserCount + ", Status=" + Status + '}';
    }
    
}
