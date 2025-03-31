package com.greentin.enovation.utils;

public class OwnerDataSet {

	private String ownerName;

	private String documentDetails;
	
	private String docName;
	
	private String docDate;

	public OwnerDataSet() {
		super();
	}
 
	public OwnerDataSet(String documentDetails) {
		super();
		this.documentDetails = documentDetails;
	}

	public OwnerDataSet(String ownerName, String docName, String docDate) {
		super();
		this.ownerName = ownerName;
		this.docName = docName;
		this.docDate = docDate;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(String documentDetails) {
		this.documentDetails = documentDetails;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
