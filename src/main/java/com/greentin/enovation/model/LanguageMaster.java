package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="master_language")
public class LanguageMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int langId;
	private String langName;
	
	public LanguageMaster() {}
	
	public LanguageMaster(int id){
		this.langId=id;
	}
	
	
	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	
}
