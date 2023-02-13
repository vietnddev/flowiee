package model;

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

@Entity
@Table(name = "Learning")
public class Learning {
	private int ID;
	private String Type;
	private String Name;
	private String Pronounce;
	private String Translate;
	private String Note;
	private String Created;
	private boolean Status;
	private boolean IsGrammar;
	private boolean IsVocabulary;

	public Learning() {
	}

	public Learning(int iD, String type, String name, String pronounce, String translate, String note, String created,
			boolean status, boolean isGrammar, boolean isVocabulary) {
		ID = iD;
		Type = type;
		Name = name;
		Pronounce = pronounce;
		Translate = translate;
		Note = note;
		Created = created;
		Status = status;
		IsGrammar = isGrammar;
		IsVocabulary = isVocabulary;
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

	public String getPronounce() {
		return Pronounce;
	}

	public void setPronounce(String pronounce) {
		Pronounce = pronounce;
	}

	public String getTranslate() {
		return Translate;
	}

	public void setTranslate(String translate) {
		Translate = translate;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public boolean isIsGrammar() {
		return IsGrammar;
	}

	public void setIsGrammar(boolean isGrammar) {
		IsGrammar = isGrammar;
	}

	public boolean isIsVocabulary() {
		return IsVocabulary;
	}

	public void setIsVocabulary(boolean isVocabulary) {
		IsVocabulary = isVocabulary;
	}
}
