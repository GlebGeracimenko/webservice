package com.gleb.dao.objects;

/**
 * Business object for style
 * 
 * @author Viacheslav Vasianovych
 *
 */
public class DBStyle {
	public static final String IDENTIFIER = "DBStyle";
	private String id;
	private String name;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
