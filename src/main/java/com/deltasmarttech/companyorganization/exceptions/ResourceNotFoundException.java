package com.deltasmarttech.companyorganization.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	String resourceName;
	String field;
	String fieldName;
	Integer fieldId;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResourceNotFoundException(String resourceName, String field, Integer fieldId) {
		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
}
